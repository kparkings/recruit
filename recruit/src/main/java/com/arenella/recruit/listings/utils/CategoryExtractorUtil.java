package com.arenella.recruit.listings.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingAlertFilterOptions;

/**
* Class to extract Job Title from Document. Will take into account all the keywords in the
* document and make a guess as to the type of job the job specification relates to based upon the 
* number of occurances of varios keywords found in the document
* @author K Parkings
*/
@Component
public class CategoryExtractorUtil{
	
	//public static final ListingCategory scrumMaster			= new ListingCategory(Listing.TECH.scrumMaster, 		Set.of("scrum master","scrummaster"));
	//public static final ListingCategory dataScientist		= new ListingCategory(Listing.TECH.dataScientist, 		Set.of("data engineer","data scientist", "data analyst", "data science"));
	//public static final ListingCategory softwareDeveloper	= new ListingCategory(Listing.TECH.softwareDeveloper, 	Set.of("php developer", "software ontwikkelaar", "php ontwikkelaar", "software developer", "software engineer", "software engineers", "application engineer", "application developer"));
		
	
	public static final ListingCategory java 				= new ListingCategory(Listing.TECH.JAVA, 				Set.of("ontwikkelen van java", "programmeren van java", "engineer (java","developer (java","java developer", "java software engineer", "java engineer", "java software ontwikkelaar", "java ontwikkelaar", "fullstack java", "java backend developer"));
	public static final ListingCategory csharp 				= new ListingCategory(Listing.TECH.DOT_NET, 			Set.of("c#/.net","c#.net", ".net software engineer", "c# developer", "c# software engineer", "c# engineer", "c# software ontwikkelaar", "c# ontwikkelaar", "fullstack c#", "c# backend developer"));
	public static final ListingCategory ba 					= new ListingCategory(Listing.TECH.BUSINESS_ANALYSTS, 	Set.of("business analyst","business analist"));
	public static final ListingCategory qa 					= new ListingCategory(Listing.TECH.TESTING, 			Set.of("engineer in test", "developer in test", "sdet","quality assurance","testautomation specialist", "qa engineer","test engineer", "test automation engineer", "test specialist", "test analyst", "performance tester", "automation tester", "qa tester", "software tester", "penetration tester", "software testers", "test lead"));
	public static final ListingCategory itSupport			= new ListingCategory(Listing.TECH.IT_SUPPORT, 			Set.of("helpdeskmedewerker","supportmedewerker","servicedeskmedewerker", "it support", "it helpdesk","helpdesk support", "support engineer", "support developer", "support analyst", "tech support", "service agent", "support manager", "1st line support", "2nd line support", "3rd line support", "support specialist", "support technician"));
	public static final ListingCategory uiux				= new ListingCategory(Listing.TECH.UI_UX, 				Set.of("ui/ux designer", "ui designer", "ui engineer", "product designer"));
	public static final ListingCategory projectManager		= new ListingCategory(Listing.TECH.PROJECT_NANAGMENT, 	Set.of("project manager", "program manager", "it manager", "procurement manager", "control manager", "operations manager", "ops manager", "head of it", "infrastructure manager", "infra manager", "development manager", "engineering manager", "security manager", "services manager", "delivery manager", "service manager", "asset manager"));
	public static final ListingCategory architect			= new ListingCategory(Listing.TECH.ARCHITECT, 			Set.of("solution architect", "solutions architect", "enterprise architect", "application architect", "infrastructure architect", "security architect", "domain architect", "service now architect", "system architect", "systems architect","technical architect"));
	public static final ListingCategory webDeveloper		= new ListingCategory(Listing.TECH.WEB, 				Set.of("web developer","front end developer", "frontend developer", "front-end developer", "web ontwikkelaar", "FE developer", "front-end ontwikkelaar"));
	public static final ListingCategory networkAdmin		= new ListingCategory(Listing.TECH.NETWORKS, 			Set.of("linux systems engineer", "aws devops", "cloud devops", "azure devops", "platform engineer", "cloud engineer", "devops engineer","dev-ops engineer", "network admin", "network administrator", "network engineer", "network specialist", "system admin", "system administrator"));
	public static final ListingCategory itSecurity			= new ListingCategory(Listing.TECH.SECURITY, 			Set.of("security engineer", "ethical hacker", "security officer", "security consultant", "security specialist", "security engineering", "security lead", "cyber consultant", "security advisor", "security manager", "security operations"));
	public static final ListingCategory itRecruiter			= new ListingCategory(Listing.TECH.REC2REC, 			Set.of("it-recruitment","recruitment specialist", "recruitmentcampagne", "it recruiter ", "it recruiters", "recruitment consultant", "recruiter"));
	//DEV_OPS
	//CLOUD
	//CLOUD, 
	//BI
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, ListingAlertFilterOptions.ListingAlertFilterOptionsBuilder filterBuilder) {
	
		Map<Listing.TECH, AtomicInteger> scored = new LinkedHashMap<>();
	
		scored.put(Listing.TECH.JAVA, 				new AtomicInteger(0));
		scored.put(Listing.TECH.DOT_NET, 			new AtomicInteger(0));
		scored.put(Listing.TECH.BUSINESS_ANALYSTS, 	new AtomicInteger(0));
		scored.put(Listing.TECH.TESTING, 			new AtomicInteger(0));
		scored.put(Listing.TECH.IT_SUPPORT, 		new AtomicInteger(0));
		scored.put(Listing.TECH.UI_UX, 				new AtomicInteger(0));
		scored.put(Listing.TECH.PROJECT_NANAGMENT, 	new AtomicInteger(0));
		scored.put(Listing.TECH.ARCHITECT, 			new AtomicInteger(0));
		scored.put(Listing.TECH.WEB, 				new AtomicInteger(0));
		scored.put(Listing.TECH.NETWORKS, 			new AtomicInteger(0));
		scored.put(Listing.TECH.REC2REC, 			new AtomicInteger(0));
		//DEV_OPS
		//CLOUD
		//CLOUD, 
		//BI
			
		Set<ListingCategory> categories = new LinkedHashSet<>();
		
		categories.add(itRecruiter);
		categories.add(java);
		categories.add(csharp);
		categories.add(ba);
		categories.add(qa);
		categories.add(itSupport);
		categories.add(uiux);
		categories.add(projectManager);
		categories.add(architect);
		categories.add(webDeveloper);
		categories.add(networkAdmin);
		categories.add(itSecurity);
		//jobs.add(scrumMaster);
		//jobs.add(dataScientist);
		//jobs.add(softwareDeveloper);
		
		categories.stream().forEach(jobType -> 
			jobType.getKeywords().stream().forEach(title -> {
				if (documentText.toLowerCase().contains(title)) {
					scored.get(jobType.getCategory()).addAndGet(getNumOccurrencesOfMatchingTitle(documentText.toLowerCase(), title));
				}
			})
		);
		
		Comparator<AtomicInteger> comparator = Comparator.comparing(o1 -> o1.get());
		
		Listing.TECH type = Collections.max(scored.entrySet(), Map.Entry.comparingByValue(comparator)).getKey();
		
		/**
		* If no matched don't provide job title filter 
		*/
		if (scored.values().stream().filter(v -> v.get() > 0).findAny().isEmpty()) {
			return;
		}
		
		filterBuilder.categories(Set.of(type));
		
	}
	
	/**
	* For when a job spec is identified as a Software Developer we want to try and narrow down 
	* to the type of developer. In this case the job spec contains more matches for software developer 
	* keywords that the specific development type but we then take the most mentioned specific 
	* developer type and use that. If no specific type can be identified we revert to the more 
	* general software developer type
	*/
	//private Listing.TECH applySoftwareDeveloperWeighting(Map<Listing.TECH, AtomicInteger> scored, ListingAlertFilterOptions.ListingAlertFilterOptionsBuilder filterBuilder, Comparator<AtomicInteger> comparator) {
		
	//	Map<Listing.TECH, AtomicInteger> weighted = new HashMap<>(scored); 
		
	//	weighted.remove(Listing.TECH.softwareDeveloper);
		
	//	Listing.TECH type = Collections.max(weighted.entrySet(), Map.Entry.comparingByValue(comparator)).getKey();
		
	//	return switch(type) {
	//		case csharp, java -> type;
	//		default -> JobType.Type.softwareDeveloper;
	//	};
		
	//}
	
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