package com.arenella.recruit.candidates.extractors;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;

import com.arenella.recruit.candidates.extractors.JobType.Type;

/**
* Class to extract Job Title from Document. Will take into account all the keywords in the
* document and make a guess as to the type of job the job specification relates to based upon the 
* number of occurances of varios keywords found in the document
* @author K Parkings
*/
@Component
public class JobTitleExtractor implements JobSpecifcationFilterExtractor{
	
	public static final JobType java 				= new JobType(JobType.Type.java, 				Set.of (" java","java,","java/","ontwikkelen van java", "programmeren van java", "engineer (java","developer java","java developer", "java software engineer", "java engineer", "java software ontwikkelaar", "java ontwikkelaar", "fullstack java", "java backend developer", "ontwikkelaar java", "développeur java", " java ","java ", " java,","dropwizard","struts","quarkus","jsp","spring","springboot","spring boot"));
	public static final JobType csharp 				= new JobType(JobType.Type.csharp, 				Set.of("c#/.net","c#.net", ".net software engineer", "c# developer", "c# software engineer", "c# engineer", "c# software ontwikkelaar", "c# ontwikkelaar", "fullstack c#", "c# backend developer", "c#", "entity framework", ".net", "asp.net"));
	public static final JobType ba 					= new JobType(JobType.Type.ba, 					Set.of("business analyst","business analist"));
	public static final JobType qa 					= new JobType(JobType.Type.qa, 					Set.of("quality engineer","/qa","quality assurance","testautomation specialist", "qa engineer","test engineer", "test automation engineer", "test specialist", "test analyst", "performance tester", "automation tester", "qa tester", "software tester", "penetration tester", "software testers", "test lead", "qauality engineer"));
	public static final JobType itSupport			= new JobType(JobType.Type.itSupport, 			Set.of("helpdeskmedewerker","supportmedewerker","servicedeskmedewerker", "it support", "it helpdesk","helpdesk support", "support engineer", "support developer", "support analyst", "tech support", "service agent", "support manager", "1st line support", "2nd line support", "3rd line support", "support specialist", "support technician"));
	public static final JobType uiux				= new JobType(JobType.Type.uiux, 				Set.of("ux/","/ux ","ui/ux", "interaction design","ui/ux designer", "ui designer", "ui engineer", "product designer"));
	public static final JobType projectManager		= new JobType(JobType.Type.projectManager, 		Set.of(" director", " cto ","pmo","project manager", "program manager", "it manager", "procurement manager", "control manager", "operations manager", "ops manager", "head of it", "infrastructure manager", "infra manager", "development manager", "engineering manager", "security manager", "services manager", "delivery manager", "service manager", "asset manager"));
	public static final JobType architect			= new JobType(JobType.Type.architect, 			Set.of("solution architect", "solutions architect", "enterprise architect", "application architect", "infrastructure architect", "security architect", "domain architect", "service now architect", "system architect", "systems architect","technical architect"));
	public static final JobType webDeveloper		= new JobType(JobType.Type.webDeveloper, 		Set.of("web developer","front end developer", "frontend developer", "front-end developer", "web ontwikkelaar", "FE developer", "front-end ontwikkelaar"));
	public static final JobType scrumMaster			= new JobType(JobType.Type.scrumMaster, 		Set.of("scrum master","scrummaster"));
	public static final JobType dataScientist		= new JobType(JobType.Type.dataScientist, 		Set.of("data engineer","data scientist", "data analyst", "data science"));
	public static final JobType networkAdmin		= new JobType(JobType.Type.networkAdmin, 		Set.of("infrastructure consultant","system engineer","sysadmin","systeembeheerder","linux systems engineer", "aws devops", "cloud devops", "azure devops", "platform engineer", "cloud engineer", "devops engineer","dev-ops engineer", "network admin", "network administrator", "network engineer", "network specialist", "system admin", "system administrator", "devops"));
	public static final JobType softwareDeveloper	= new JobType(JobType.Type.softwareDeveloper, 	Set.of("software ontwikkelaar", "software developer", "software engineer", "software engineers", "application engineer", "application developer"));
	public static final JobType itSecurity			= new JobType(JobType.Type.itSecurity, 			Set.of("security engineer", "ethical hacker", "security officer", "security consultant", "security specialist", "security engineering", "security lead", "cyber consultant", "security advisor", "security manager", "security operations"));
	public static final JobType itRecruiter			= new JobType(JobType.Type.itRecruiter, 		Set.of("it-recruitment","recruitment specialist", "recruitmentcampagne", "it recruiter ", "it recruiters", "recruitment consultant", "recruiter"));
	public static final JobType sdet 				= new JobType(JobType.Type.sdet, 				Set.of("engineer in test", "developer in test", "sdet"));
	public static final JobType kotlin 				= new JobType(JobType.Type.kotlin, 				Set.of("kotlin","spring","springboot","spring boot"));
	
	
	public static final JobType ruby 				= new JobType(JobType.Type.ruby, 				Set.of("ruby"));
	public static final JobType rubyOnRails 		= new JobType(JobType.Type.rubyOnRails, 		Set.of("ruby on rails"));
	public static final JobType go 					= new JobType(JobType.Type.go, 					Set.of("go programmer","developer go", "go developer","développeur go", "go développeur","ontwikkelaar go", "go ontwikkelaar","golang"));
	public static final JobType react 				= new JobType(JobType.Type.react, 				Set.of("react","reactjs","react.js"));
	public static final JobType vue 				= new JobType(JobType.Type.vue, 				Set.of("vue","vuejs","vue.js"));
	public static final JobType next 				= new JobType(JobType.Type.next, 				Set.of("next","nextjs","next.js"));
	public static final JobType expres 				= new JobType(JobType.Type.expres, 				Set.of("expres","expresjs","expres.js"));
	public static final JobType rust 				= new JobType(JobType.Type.rust, 				Set.of(" rust "));
	public static final JobType testManager 		= new JobType(JobType.Type.testManager, 		Set.of("test manager","qa manager"));
	public static final JobType productOwner 		= new JobType(JobType.Type.productOwner, 		Set.of("product owner", "product manager"));
	public static final JobType node 				= new JobType(JobType.Type.node, 				Set.of("node","nodejs","node.js"));
	public static final JobType python 				= new JobType(JobType.Type.python, 				Set.of("python", "flask", "django", "chrerrypy", "fastapi","odoo"));
	public static final JobType angular 			= new JobType(JobType.Type.angular, 			Set.of("angular","angularjs","angular.js"));
	public static final JobType php 				= new JobType(JobType.Type.php, 				Set.of("php", "laravel","sfmfony","lumen","cakephp","codeigniter","fuelphp"));
	public static final JobType android 			= new JobType(JobType.Type.android, 			Set.of("android","mobile"));
	public static final JobType ios 				= new JobType(JobType.Type.ios, 				Set.of("ios","mobile"));
	public static final JobType ccplusplus 			= new JobType(JobType.Type.ccplusplus, 			Set.of("c++","c/c++"));
	public static final JobType cobol 				= new JobType(JobType.Type.cobol, 				Set.of("cobol"));
	public static final JobType sap 				= new JobType(JobType.Type.sap, 				Set.of(" sap "));
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
	
		Map<JobType.Type, AtomicInteger> scored = new LinkedHashMap<>();
	
		scored.put(JobType.Type.itRecruiter, 		new AtomicInteger(0));
		scored.put(JobType.Type.java, 				new AtomicInteger(0));
		scored.put(JobType.Type.csharp, 			new AtomicInteger(0));
		scored.put(JobType.Type.ba, 				new AtomicInteger(0));
		scored.put(JobType.Type.qa, 				new AtomicInteger(0));
		scored.put(JobType.Type.itSupport, 			new AtomicInteger(0));
		scored.put(JobType.Type.uiux, 				new AtomicInteger(0));
		scored.put(JobType.Type.projectManager, 	new AtomicInteger(0));
		scored.put(JobType.Type.architect, 			new AtomicInteger(0));
		scored.put(JobType.Type.webDeveloper, 		new AtomicInteger(0));
		scored.put(JobType.Type.scrumMaster, 		new AtomicInteger(0));
		scored.put(JobType.Type.dataScientist, 		new AtomicInteger(0));
		scored.put(JobType.Type.networkAdmin, 		new AtomicInteger(0));
		scored.put(JobType.Type.softwareDeveloper, 	new AtomicInteger(0));
		scored.put(JobType.Type.itSecurity, 		new AtomicInteger(0));
		scored.put(JobType.Type.sdet, 				new AtomicInteger(0));
		scored.put(JobType.Type.kotlin, 			new AtomicInteger(0));
		
		scored.put(JobType.Type.ruby, 				new AtomicInteger(0));
		scored.put(JobType.Type.rubyOnRails, 		new AtomicInteger(0));
		scored.put(JobType.Type.go, 				new AtomicInteger(0));
		scored.put(JobType.Type.react, 				new AtomicInteger(0));
		scored.put(JobType.Type.vue, 				new AtomicInteger(0));
		scored.put(JobType.Type.next, 				new AtomicInteger(0));
		scored.put(JobType.Type.expres, 			new AtomicInteger(0));
		scored.put(JobType.Type.rust, 				new AtomicInteger(0));
		scored.put(JobType.Type.testManager, 		new AtomicInteger(0));
		scored.put(JobType.Type.productOwner, 		new AtomicInteger(0));
		scored.put(JobType.Type.node, 				new AtomicInteger(0));
		scored.put(JobType.Type.python, 			new AtomicInteger(0));
		scored.put(JobType.Type.angular, 			new AtomicInteger(0));
		scored.put(JobType.Type.php, 				new AtomicInteger(0));
		scored.put(JobType.Type.android, 			new AtomicInteger(0));
		scored.put(JobType.Type.ios, 				new AtomicInteger(0));
		scored.put(JobType.Type.ccplusplus, 		new AtomicInteger(0));
		scored.put(JobType.Type.cobol, 				new AtomicInteger(0));
		scored.put(JobType.Type.sap, 				new AtomicInteger(0));
		
		
		Set<JobType> jobs = new LinkedHashSet<>();
		
		jobs.add(itRecruiter);
		jobs.add(java);
		jobs.add(csharp);
		jobs.add(ba);
		jobs.add(qa);
		jobs.add(itSupport);
		jobs.add(uiux);
		jobs.add(projectManager);
		jobs.add(architect);
		jobs.add(webDeveloper);
		jobs.add(scrumMaster);
		jobs.add(dataScientist);
		jobs.add(networkAdmin);
		jobs.add(softwareDeveloper);
		jobs.add(itSecurity);
		jobs.add(sdet);
		jobs.add(kotlin);
		
		jobs.add(ruby);
		jobs.add(rubyOnRails);
		jobs.add(go);
		jobs.add(react);
		jobs.add(vue);
		jobs.add(next);
		jobs.add(expres);
		jobs.add(rust);
		jobs.add(testManager);
		jobs.add(productOwner);
		jobs.add(node);
		jobs.add(python);
		jobs.add(angular);
		jobs.add(php);
		jobs.add(android);
		jobs.add(ios);
		jobs.add(ccplusplus);
		jobs.add(cobol);
		jobs.add(sap);
		
		Set<JobType.Type> softwareDevelopmentTypes = new HashSet<>();
		softwareDevelopmentTypes.add(java.getType());
		softwareDevelopmentTypes.add(csharp.getType());
		softwareDevelopmentTypes.add(webDeveloper.getType());
		softwareDevelopmentTypes.add(softwareDeveloper.getType());
		softwareDevelopmentTypes.add(kotlin.getType());
		softwareDevelopmentTypes.add(ruby.getType());
		softwareDevelopmentTypes.add(rubyOnRails.getType());
		softwareDevelopmentTypes.add(go.getType());
		softwareDevelopmentTypes.add(react.getType());
		softwareDevelopmentTypes.add(vue.getType());
		softwareDevelopmentTypes.add(next.getType());
		softwareDevelopmentTypes.add(expres.getType());
		softwareDevelopmentTypes.add(rust.getType());
		softwareDevelopmentTypes.add(node.getType());
		softwareDevelopmentTypes.add(python.getType());
		softwareDevelopmentTypes.add(angular.getType());
		softwareDevelopmentTypes.add(php.getType());
		softwareDevelopmentTypes.add(android.getType());
		softwareDevelopmentTypes.add(ios.getType());
		softwareDevelopmentTypes.add(ccplusplus.getType());
		softwareDevelopmentTypes.add(cobol.getType());
		softwareDevelopmentTypes.add(sap.getType());
		
		jobs.stream().forEach(jobType -> {
		
			AtomicReference<String>  tmpDocumentText = new AtomicReference<>();
			tmpDocumentText.set(documentText);
		
			jobType.getTitles().stream().forEach(title -> {
				if (tmpDocumentText.get().contains(title)) {
					WeightResult result = getNumOccurrencesOfMatchingTitle(tmpDocumentText.get(), title);
					tmpDocumentText.set(result.documentText) ;
					scored.get(jobType.getType()).addAndGet(result.count);
				}
			});
		});
		
		Comparator<AtomicInteger> comparator = Comparator.comparing(o1 -> o1.get());
		
		JobType.Type type = Collections.max(scored.entrySet(), Map.Entry.comparingByValue(comparator)).getKey();
		
		if (softwareDevelopmentTypes.contains(type)) {
			type = applySoftwareDeveloperWeighting(scored, filterBuilder, comparator);
		}
		
		/**
		* If no matched don't provide job title filter 
		*/
		if (scored.values().stream().filter(v -> v.get() > 0).findAny().isEmpty()) {
			return;
		}
		
		/**
		* As recruiters mention their own job title in spec's if there is a tie and 
		* it is between IT recruiter and another give preference to other job type 
		*/
		if (type == JobType.Type.itRecruiter) {
			Set<Entry<Type, AtomicInteger>> 	nextBest 		= scored.entrySet().stream().filter(es -> es.getKey() != JobType.Type.itRecruiter).collect(Collectors.toSet());
			JobType.Type 						nextBestType 	= Collections.max(nextBest, Map.Entry.comparingByValue(comparator)).getKey();
			
			if (scored.get(type).get() == scored.get(nextBestType).get()) {
				type = nextBestType;
			}
			
			
		}
		
		filterBuilder.jobTitle(type.role);
		
		/**
		* If there is a tie. Use first occurrence of 
		*/
		OptionalInt tieScore 		= scored.values().stream().mapToInt(v -> v.get()).max();
		Set<Type> 	tiedJobTypes 	= scored.entrySet().stream().filter(a -> a.getValue().get() == tieScore.getAsInt()).map(Entry::getKey).collect(Collectors.toSet());
		
		if(tieScore.isPresent() && tieScore.getAsInt() != 0 && tiedJobTypes.size() > 1) {
			
			AtomicReference<Type> 	xType 		= new AtomicReference<>();
			AtomicInteger 			xFirstPos 	= new AtomicInteger(1000000);
			
			jobs.stream().filter(jt -> tiedJobTypes.contains(jt.getType())).forEach(jobType -> 
				
				jobType.getTitles().stream().forEach(title -> {
					if(documentText.contains(title) && documentText.indexOf(title) < xFirstPos.get()) {
						xType.set(jobType.getType());
						xFirstPos.set(documentText.indexOf(title));
					}
				})
			);
			
			filterBuilder.jobTitle(xType.get().role);
		}
		
	}
	
	/**
	* For when a job spec is identified as a Software Developer we want to try and narrow down 
	* to the type of developer. In this case the job spec contains more matches for software developer 
	* keywords that the specific development type but we then take the most mentioned specific 
	* developer type and use that. If no specific type can be identified we revert to the more 
	* general software developer type
	*/
	private JobType.Type applySoftwareDeveloperWeighting(Map<JobType.Type, AtomicInteger> scored, CandidateExtractedFiltersBuilder filterBuilder, Comparator<AtomicInteger> comparator) {
		
		Map<JobType.Type, AtomicInteger> weighted = new HashMap<>(scored); 
		
		weighted.remove(JobType.Type.softwareDeveloper);
		
		JobType.Type type = Collections.max(weighted.entrySet(), Map.Entry.comparingByValue(comparator)).getKey();
	
		return switch(type) {
			case webDeveloper, csharp, java, kotlin, ruby, rubyOnRails, go, react, vue, next, expres, rust, node, python, angular, php, android, ios, ccplusplus, cobol, sap -> type;
			default -> JobType.Type.softwareDeveloper;
		};
		
	}
	
	private WeightResult getNumOccurrencesOfMatchingTitle(String documentText, String title) {
		
		int 	count 	= 0;
		
		while(documentText.contains(title)) {
			
			if(title.equals("c++")) {
				documentText 	= documentText.replaceFirst("c\\+\\+", "");
			} else {
				documentText 	= documentText.replaceFirst(title, "");
			}
			
			count 	= count +1;
		}
		
		return new WeightResult(documentText, count);
	}
	
	private record WeightResult(String documentText, Integer count) {}
	
}