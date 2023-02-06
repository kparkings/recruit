package com.arenella.recruit.candidates.extractors;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
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
	
	final static JobType java 				= new JobType(JobType.Type.java, 				Set.of("ontwikkelen van java", "programmeren van java", "engineer (java","developer (java","java developer", "java software engineer", "java engineer", "java software ontwikkelaar", "java ontwikkelaar", "fullstack java", "java backend developer"));
	final static JobType csharp 			= new JobType(JobType.Type.csharp, 				Set.of("c#.net", ".net software engineer", "c# developer", "c# software engineer", "c# engineer", "c# software ontwikkelaar", "c# ontwikkelaar", "fullstack c#", "c# backend developer"));
	final static JobType ba 				= new JobType(JobType.Type.ba, 					Set.of("business analyst","business analist"));
	final static JobType qa 				= new JobType(JobType.Type.qa, 					Set.of("quality assurance","testautomation specialist", "qa engineer","test engineer", "test automation engineer", "test specialist", "test analyst", "performance tester", "automation tester", "qa tester", "software tester", "penetration tester", "software testers", "test lead"));
	final static JobType itSupport			= new JobType(JobType.Type.itSupport, 			Set.of("helpdeskmedewerker","supportmedewerker","servicedeskmedewerker", "it support", "it helpdesk","helpdesk support", "support engineer", "support developer", "support analyst", "tech support", "service agent", "support manager", "1st line support", "2nd line support", "3rd line support", "support specialist", "support technician"));
	final static JobType uiux				= new JobType(JobType.Type.uiux, 				Set.of("ui/ux designer", "ui designer", "ui engineer", "product designer"));
	final static JobType projectManager		= new JobType(JobType.Type.projectManager, 		Set.of("project manager", "program manager", "it manager", "procurement manager", "control manager", "operations manager", "ops manager", "head of it", "infrastructure manager", "infra manager", "development manager", "engineering manager", "security manager", "services manager", "delivery manager", "service manager", "asset manager"));
	final static JobType architect			= new JobType(JobType.Type.architect, 			Set.of("solution architect", "solutions architect", "enterprise architect", "application architect", "infrastructure architect", "security architect", "domain architect", "service now architect", "system architect", "systems architect"));
	final static JobType webDeveloper		= new JobType(JobType.Type.webDeveloper, 		Set.of("web developer","front end developer", "frontend developer", "front-end developer", "web ontwikkelaar", "FE developer", "front-end ontwikkelaar"));
	final static JobType scrumMaster		= new JobType(JobType.Type.scrumMaster, 		Set.of("scrum master","scrummaster"));
	final static JobType dataScientist		= new JobType(JobType.Type.dataScientist, 		Set.of("data engineer","data scientist", "data analyst", "data science"));
	final static JobType networkAdmin		= new JobType(JobType.Type.networkAdmin, 		Set.of("linux systems engineer", "aws devops", "cloud devops", "azure devops", "platform engineer", "cloud engineer", "devops engineer","dev-ops engineer", "network admin", "network administrator", "network engineer", "network specialist", "system admin", "system administrator"));
	final static JobType softwareDeveloper	= new JobType(JobType.Type.softwareDeveloper, 	Set.of("php developer", "software ontwikkelaar", "php ontwikkelaar", "software developer", "software engineer", "software engineers", "application engineer", "application developer"));
	final static JobType itSecurity			= new JobType(JobType.Type.itSecurity, 			Set.of("security engineer", "ethical hacker", "security officer", "security consultant", "security specialist", "security engineering", "security lead", "cyber consultant", "security advisor", "security manager", "security operations"));
	final static JobType itRecruiter		= new JobType(JobType.Type.itRecruiter, 		Set.of("it-recruitment","recruitment specialist", "recruitmentcampagne", "recruitment consultant", "recruiter"));
	final static JobType sdet 				= new JobType(JobType.Type.sdet, 				Set.of("engineer in test", "developer in test", "sdet"));
	
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
		
		jobs.stream().forEach(jobType -> {
			jobType.getTitles().stream().forEach(title -> {
				if (documentText.contains(title)) {
					scored.get(jobType.getType()).addAndGet(getNumOccurrencesOfMatchingTitle(documentText, title));
				}
			});
		});
		
		Comparator<AtomicInteger> comparator = Comparator.comparing(o1 -> o1.get());
		
		JobType.Type type = Collections.max(scored.entrySet(), Map.Entry.comparingByValue(comparator)).getKey();
		
		if (type == JobType.Type.softwareDeveloper) {
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
		
		switch(type) {
			case csharp:
			case java:{
				return type;
			}
			default:{
				return JobType.Type.softwareDeveloper;
			}
		}
		
	}
	
	private int getNumOccurrencesOfMatchingTitle(String documentText, String title) {
		
		int 	count 	= 0;
		String 	tmp 	= new String(documentText);
		
		while(tmp.contains(title)) {
			tmp 	= tmp.replaceFirst(title, "");
			count 	= count +1;
		}
		
		return count;
	}
	
}