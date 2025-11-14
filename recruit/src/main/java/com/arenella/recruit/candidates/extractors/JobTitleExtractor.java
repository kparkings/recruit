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
import java.util.Optional;

/**
* Class to extract Job Title from Document. Will take into account all the keywords in the
* document and make a guess as to the type of job the job specification relates to based upon the 
* number of occurances of varios keywords found in the document
* @author K Parkings
*/
@Component
public class JobTitleExtractor implements JobSpecifcationFilterExtractor{
	
	public static final JobType java 					= new JobType(JobType.Type.java, 					Set.of (" java","java,","java/","ontwikkelen van java", "programmeren van java", "engineer (java","developer java","java developer", "java software engineer", "java engineer", "java software ontwikkelaar", "java ontwikkelaar", "fullstack java", "java backend developer", "ontwikkelaar java", "développeur java", " java ","java ", " java,","dropwizard","struts","quarkus","jsp","spring","springboot","spring boot"));
	public static final JobType csharp 					= new JobType(JobType.Type.csharp, 					Set.of("c#/.net","c#.net", ".net software engineer", "c# developer", "c# software engineer", "c# engineer", "c# software ontwikkelaar", "c# ontwikkelaar", "fullstack c#", "c# backend developer", "c#", "entity framework", ".net", "asp.net"));
	public static final JobType ba 						= new JobType(JobType.Type.ba, 						Set.of("business analyst","business analist"));
	public static final JobType qa 						= new JobType(JobType.Type.qa, 						Set.of("quality engineer","/qa","quality assurance","testautomation specialist", "qa engineer","test engineer", "test automation engineer", "test specialist", "test analyst", "performance tester", "automation tester", "qa tester", "software tester", "penetration tester", "software testers", "test lead", "qauality engineer"));
	public static final JobType itSupport				= new JobType(JobType.Type.itSupport, 				Set.of("helpdeskmedewerker","supportmedewerker","servicedeskmedewerker", "it support", "it helpdesk","helpdesk support", "support engineer", "support developer", "support analyst", "tech support", "service agent", "support manager", "1st line support", "2nd line support", "3rd line support", "support specialist", "support technician"));
	public static final JobType uiux					= new JobType(JobType.Type.uiux, 					Set.of("ux/","/ux ","ui/ux", "interaction design","ui/ux designer", "ui designer", "ui engineer", "product designer"));
	public static final JobType architect				= new JobType(JobType.Type.architect, 				Set.of("architect", "solution architect", "solutions architect", "enterprise architect", "application architect", "infrastructure architect", "security architect", "domain architect", "service now architect", "system architect", "systems architect","technical architect"));
	public static final JobType webDeveloper			= new JobType(JobType.Type.webDeveloper, 			Set.of("web developer","front end developer", "frontend developer", "front-end developer", "web ontwikkelaar", "FE developer", "front-end ontwikkelaar"));
	public static final JobType scrumMaster				= new JobType(JobType.Type.scrumMaster, 			Set.of("scrum master","scrummaster"));
	public static final JobType dataScientist			= new JobType(JobType.Type.dataScientist, 			Set.of("bi analyst", "data engineer","data scientist", "data analyst", "data science"));
	public static final JobType networkAdmin			= new JobType(JobType.Type.networkAdmin, 			Set.of("infrastructure consultant","system engineer","sysadmin","systeembeheerder","linux systems engineer", "aws devops", "cloud devops", "azure devops", "platform engineer", "cloud engineer", "devops engineer","dev-ops engineer", "network admin", "network administrator", "network engineer", "network specialist", "system admin", "system administrator", "devops"));
	public static final JobType softwareDeveloper		= new JobType(JobType.Type.softwareDeveloper, 		Set.of("software ontwikkelaar", "software developer", "software engineer", "software engineers", "application engineer", "application developer"));
	public static final JobType itSecurity				= new JobType(JobType.Type.itSecurity, 				Set.of("security engineer", "ethical hacker", "security officer", "security consultant", "security specialist", "security engineering", "security lead", "cyber consultant", "security advisor", "security manager", "security operations"));
	public static final JobType itRecruiter				= new JobType(JobType.Type.itRecruiter, 			Set.of("it-recruitment","recruitment specialist", "recruitmentcampagne", "it recruiter ", "it recruiters", "recruitment consultant", "recruiter"));
	public static final JobType sdet 					= new JobType(JobType.Type.sdet, 					Set.of("engineer in test", "developer in test", "sdet"));
	public static final JobType kotlin 					= new JobType(JobType.Type.kotlin, 					Set.of("kotlin","spring","springboot","spring boot"));
	
	
	public static final JobType ruby 					= new JobType(JobType.Type.ruby, 					Set.of("ruby"));
	public static final JobType rubyOnRails 			= new JobType(JobType.Type.rubyOnRails, 			Set.of("ruby on rails"));
	public static final JobType go 						= new JobType(JobType.Type.go, 						Set.of("go programmer","developer go", "go developer","développeur go", "go développeur","ontwikkelaar go", "go ontwikkelaar","golang"));
	public static final JobType react 					= new JobType(JobType.Type.react, 					Set.of("react","reactjs","react.js"));
	public static final JobType vue 					= new JobType(JobType.Type.vue, 					Set.of("vue","vuejs","vue.js"));
	public static final JobType next 					= new JobType(JobType.Type.next, 					Set.of("next","nextjs","next.js"));
	public static final JobType expres 					= new JobType(JobType.Type.expres, 					Set.of("expres","expresjs","expres.js"));
	public static final JobType rust 					= new JobType(JobType.Type.rust, 					Set.of(" rust "));
	public static final JobType testManager 			= new JobType(JobType.Type.testManager, 			Set.of("test manager","qa manager"));
	public static final JobType productOwner 			= new JobType(JobType.Type.productOwner, 			Set.of("product owner", "product manager"));
	public static final JobType node 					= new JobType(JobType.Type.node, 					Set.of("node","nodejs","node.js"));
	public static final JobType python 					= new JobType(JobType.Type.python, 					Set.of("python", "flask", "django", "chrerrypy", "fastapi","odoo"));
	public static final JobType angular 				= new JobType(JobType.Type.angular, 				Set.of("angular","angularjs","angular.js"));
	public static final JobType php 					= new JobType(JobType.Type.php, 					Set.of("php", "laravel","sfmfony","lumen","cakephp","codeigniter","fuelphp"));
	public static final JobType android 				= new JobType(JobType.Type.android, 				Set.of("android"));
	public static final JobType ios 					= new JobType(JobType.Type.ios, 					Set.of("ios"));
	public static final JobType ccplusplus 				= new JobType(JobType.Type.ccplusplus, 				Set.of("c++","c/c++"));
	public static final JobType cobol 					= new JobType(JobType.Type.cobol, 					Set.of("cobol"));
	public static final JobType sap 					= new JobType(JobType.Type.sap, 					Set.of(" sap "));
	
	public static final JobType salesforce 				= new JobType(JobType.Type.salesforce, 				Set.of("salesforce"));
	
	public static final JobType softwareArchitect 		= new JobType(JobType.Type.softwareArchitect, 		Set.of("software architect", "application architect")); 
	public static final JobType dataArchitect 			= new JobType(JobType.Type.dataArchitect, 			Set.of("data architect"));		
	public static final JobType infrastructureArchitect = new JobType(JobType.Type.infrastructureArchitect, Set.of("infrascructure architect", "infra architect", "network architect"));
	public static final JobType solutionsArchitect 		= new JobType(JobType.Type.solutionsArchitect, 		Set.of("solutions architect"));
	public static final JobType enterpriseArchitect 	= new JobType(JobType.Type.enterpriseArchitect, 	Set.of("enterprise architect"));
	
	public static final JobType projectManager			= new JobType(JobType.Type.projectManager, 			Set.of("projektleiter","pmo","project manager", "it manager", "procurement manager", "control manager", "engineering manager", "security manager", "services manager", "service manager", "asset manager"));
	public static final JobType softwareManager 		= new JobType(JobType.Type.softwareManager, 		Set.of("software manager","application manager", "development manager"));
	public static final JobType infrastructureManager 	= new JobType(JobType.Type.infrastructureManager, 	Set.of("infrascructure manager", "infra manager", "network manager","operations manager", "ops manager"));
	public static final JobType cto 					= new JobType(JobType.Type.cto, 					Set.of(" cto","head of it"));
	public static final JobType diretor 				= new JobType(JobType.Type.diretor, 				Set.of("director"));
	public static final JobType programmeManager 		= new JobType(JobType.Type.programmeManager, 		Set.of("programme manager","program manager"));
	public static final JobType deliveryManager 		= new JobType(JobType.Type.deliveryManager, 		Set.of("delivery manager"));
	
	public static final JobType mobileDeveloper 		= new JobType(JobType.Type.mobileDeveloper, 		Set.of("mobile developer", "mobile ontwikkelaar"));
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
	
		Map<JobType.Type, AtomicInteger> scored = new LinkedHashMap<>();
	
		scored.put(JobType.Type.itRecruiter, 				new AtomicInteger(0));
		scored.put(JobType.Type.java, 						new AtomicInteger(0));
		scored.put(JobType.Type.csharp, 					new AtomicInteger(0));
		scored.put(JobType.Type.ba, 						new AtomicInteger(0));
		scored.put(JobType.Type.qa, 						new AtomicInteger(0));
		scored.put(JobType.Type.itSupport, 					new AtomicInteger(0));
		scored.put(JobType.Type.uiux, 						new AtomicInteger(0));
		scored.put(JobType.Type.projectManager, 			new AtomicInteger(0));
		scored.put(JobType.Type.architect, 					new AtomicInteger(0));
		scored.put(JobType.Type.webDeveloper, 				new AtomicInteger(0));
		scored.put(JobType.Type.scrumMaster, 				new AtomicInteger(0));
		scored.put(JobType.Type.dataScientist, 				new AtomicInteger(0));
		scored.put(JobType.Type.networkAdmin, 				new AtomicInteger(0));
		scored.put(JobType.Type.softwareDeveloper, 			new AtomicInteger(0));
		scored.put(JobType.Type.itSecurity, 				new AtomicInteger(0));
		scored.put(JobType.Type.sdet, 						new AtomicInteger(0));
		scored.put(JobType.Type.kotlin, 					new AtomicInteger(0));
		
		scored.put(JobType.Type.ruby, 						new AtomicInteger(0));
		scored.put(JobType.Type.rubyOnRails, 				new AtomicInteger(0));
		scored.put(JobType.Type.go, 						new AtomicInteger(0));
		scored.put(JobType.Type.react, 						new AtomicInteger(0));
		scored.put(JobType.Type.vue, 						new AtomicInteger(0));
		scored.put(JobType.Type.next, 						new AtomicInteger(0));
		scored.put(JobType.Type.expres, 					new AtomicInteger(0));
		scored.put(JobType.Type.rust, 						new AtomicInteger(0));
		scored.put(JobType.Type.testManager, 				new AtomicInteger(0));
		scored.put(JobType.Type.productOwner, 				new AtomicInteger(0));
		scored.put(JobType.Type.node, 						new AtomicInteger(0));
		scored.put(JobType.Type.python, 					new AtomicInteger(0));
		scored.put(JobType.Type.angular, 					new AtomicInteger(0));
		scored.put(JobType.Type.php, 						new AtomicInteger(0));
		scored.put(JobType.Type.android, 					new AtomicInteger(0));
		scored.put(JobType.Type.ios, 						new AtomicInteger(0));
		scored.put(JobType.Type.ccplusplus, 				new AtomicInteger(0));
		scored.put(JobType.Type.cobol, 						new AtomicInteger(0));
		scored.put(JobType.Type.sap, 						new AtomicInteger(0));
		
		scored.put(JobType.Type.salesforce, 				new AtomicInteger(0));
		
		scored.put(JobType.Type.softwareArchitect, 			new AtomicInteger(0)); 
		scored.put(JobType.Type.dataArchitect, 				new AtomicInteger(0));		
		scored.put(JobType.Type.infrastructureArchitect, 	new AtomicInteger(0));
		scored.put(JobType.Type.solutionsArchitect, 		new AtomicInteger(0));
		scored.put(JobType.Type.enterpriseArchitect, 		new AtomicInteger(0));
		
		scored.put(JobType.Type.softwareManager, 			new AtomicInteger(0));
		scored.put(JobType.Type.infrastructureManager, 		new AtomicInteger(0));
		scored.put(JobType.Type.cto, 						new AtomicInteger(0));
		scored.put(JobType.Type.diretor, 					new AtomicInteger(0));
		scored.put(JobType.Type.programmeManager, 			new AtomicInteger(0));
		scored.put(JobType.Type.deliveryManager, 			new AtomicInteger(0));
		
		scored.put(JobType.Type.mobileDeveloper, 			new AtomicInteger(0));
		
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
		
		jobs.add(salesforce);
		
		jobs.add(softwareArchitect); 
		jobs.add(dataArchitect);		
		jobs.add(infrastructureArchitect);
		jobs.add(solutionsArchitect);
		jobs.add(enterpriseArchitect);
		
		jobs.add(softwareManager);
		jobs.add(infrastructureManager);
		jobs.add(cto);
		jobs.add(diretor);
		jobs.add(programmeManager);
		jobs.add(deliveryManager);
		jobs.add(projectManager);
		
		jobs.add(mobileDeveloper);
		
		Set<JobType.Type> softwareDevelopmentTypes = new HashSet<>();
		softwareDevelopmentTypes.add(java.getType());
		softwareDevelopmentTypes.add(csharp.getType());
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
		
		/**
		* If generic role is present and also more specific role. 
		* Removes the generic role
		* Example: Software Developer and Java Developer. Java is more specific 
		* so we remote Software Developer
		*/
		
		
		JobType.Type type = Collections.max(scored.entrySet(), Map.Entry.comparingByValue(comparator)).getKey();
		
		/**
		* If generic role is present and also more specific role. 
		* Removes the generic role
		* Example: Software Developer and Java Developer. Java is more specific 
		* so we remote Software Developer
		*/
		
		Set<Type> foundTypes = scored.entrySet().stream().filter(kv -> kv.getValue().intValue() > 0).toList().stream().map(Entry::getKey).collect(Collectors.toSet());
		
		if (type == JobType.Type.softwareDeveloper) {
			this.removeParentType(JobType.Type.softwareDeveloper, JobType.getSoftwareDeveloperTypes(), foundTypes, scored);
			type = Collections.max(scored.entrySet(), Map.Entry.comparingByValue(comparator)).getKey();
		}
		
		if (type == JobType.Type.webDeveloper) {
			this.removeParentType(JobType.Type.webDeveloper, JobType.getWebDeveloperTypes(), foundTypes, scored);
			type = Collections.max(scored.entrySet(), Map.Entry.comparingByValue(comparator)).getKey();
		}
		
		if (type == JobType.Type.architect) {
			this.removeParentType(JobType.Type.architect, JobType.getArchitectTypes(), foundTypes, scored);
			type = Collections.max(scored.entrySet(), Map.Entry.comparingByValue(comparator)).getKey();
		}
		
		if (type == JobType.Type.projectManager) {
			this.removeParentType(JobType.Type.projectManager, JobType.getManagerTypes(), foundTypes, scored);
			type = Collections.max(scored.entrySet(), Map.Entry.comparingByValue(comparator)).getKey();
		}
		
		if(type == JobType.Type.mobileDeveloper || type == JobType.Type.ios || type == JobType.Type.android) {
			type = handleMobileCase(scored.entrySet(), type);
		}
		
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
		OptionalInt tieScore 					= scored.values().stream().mapToInt(v -> v.get()).max();
		Set<Type> 	tiedJobTypes 				= scored.entrySet().stream().filter(a -> a.getValue().get() == tieScore.getAsInt()).map(Entry::getKey).collect(Collectors.toSet());
		
		/**
		* If tie and at least 1 result is not a parent group, remove parent groups to ensure
		* first child is found
		*/
		if(tiedJobTypes.stream().filter(t -> !JobType.PARENT_TYPES.contains(t)).toList().size() > 0 ) {
			JobType.PARENT_TYPES.forEach(tiedJobTypes::remove);
		}
		
		if (tieScore.isPresent() && tieScore.getAsInt() != 0 && tiedJobTypes.size() > 1) {
			
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
	* Rule. If both Android and IOS use mobile developer
	* 		If only IOS and mobile use IOS
	* 		IF only Andorid and mobile use Android
	* 		IF only mobile use mobile
	*/
	private JobType.Type handleMobileCase(Set<Entry<Type, AtomicInteger>> scored, Type originalType) {
		
		
		Entry<Type, AtomicInteger> mobile 	= scored.stream().filter(e -> e.getKey() == Type.mobileDeveloper).findAny().orElseThrow();
		Entry<Type, AtomicInteger> ios 		= scored.stream().filter(e -> e.getKey() == Type.ios).findAny().orElseThrow();
		Entry<Type, AtomicInteger> android 	= scored.stream().filter(e -> e.getKey() == Type.android).findAny().orElseThrow();
		
		boolean isMobile 	= mobile.getValue().get() 	> 0;
		boolean isIos 		= ios.getValue().get()	 	> 0;
		boolean isAndroid 	= android.getValue().get() 	> 0;
		
		JobType.Type foundType = null;
		
		if (isMobile && !isIos && !isAndroid) {
			foundType = JobType.Type.mobileDeveloper;
		}
		
		if (!isMobile && !isIos && isAndroid) {
			foundType = JobType.Type.android;
		}
		
		if (!isMobile && !isAndroid && isIos) {
			foundType = JobType.Type.ios;
		}
		
		if (isMobile && isAndroid && !isIos) {
			foundType = JobType.Type.android;
		}
		
		if (isMobile && isIos && !isAndroid) {
			foundType = JobType.Type.ios;
		}
		
		if (!isMobile && isAndroid && isIos) {
			foundType = JobType.Type.mobileDeveloper;
		}
		
		if (isMobile && isAndroid && isIos) {
			foundType =  JobType.Type.mobileDeveloper;
		}
		
		Optional.of(foundType).ifPresent(ft -> {
			switch(ft) {
				case JobType.Type.mobileDeveloper -> {
					ios.getValue().set(0);
					android.getValue().set(0);
					mobile.getValue().set(1);
				}
				case JobType.Type.ios -> {
					mobile.getValue().set(0);
					android.getValue().set(0);
				}
				case JobType.Type.android -> {
					mobile.getValue().set(0);
					ios.getValue().set(0);
				}
				default -> {}
			}
		});
		
		return Optional.ofNullable(foundType).isPresent() ? foundType : originalType;
	}
	
	/**
	* Some JobTypes fall into a broader parent category. This method looks to see
	* if the job types identified in the document text  are a subType of the partent 
	* type. If that is the case we remove the parent type so that the more specific 
	* sub type is selected
	* @param parentType	- Such as SoftwareDeveloper or WebDeveloper
	* @param subTypes	- Such as Java, C#
	* @param foundTypes - Types found in the document
	* @param scored     - Contains the previously scored JobTypes
	*/
	private void removeParentType(JobType.Type parentType, Set<JobType.Type> subTypes, Set<JobType.Type> foundTypes, Map<Type, AtomicInteger> scored ) {
		foundTypes.forEach(k -> {
			if (subTypes.contains(k)) {
				scored.get(parentType).set(0);
			}
		});
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
		
		JobType.Type typePreFilter = Collections.max(weighted.entrySet(), Map.Entry.comparingByValue(comparator)).getKey();
		
		Map<JobType.Type, AtomicInteger> weightedDeveloperRoles = weighted.entrySet().stream().filter(i -> JobType.getSoftwareDeveloperTypes().contains(i.getKey()) || JobType.getWebDeveloperTypes().contains(i.getKey())).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
		
		JobType.Type type = Collections.max(weightedDeveloperRoles.entrySet(), Map.Entry.comparingByValue(comparator)).getKey();
	
		if (scored.get(type).intValue() == 0) {
			return typePreFilter;
		}
		
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