package com.arenella.recruit.candidates.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Functions for extracting Candidate Functions
* @author K Parkings
*/
@Component
public class CandidateFunctionExtractorImpl implements CandidateFunctionExtractor {

	private Map<FUNCTION,Set<String>> all = new HashMap<>();
		
	/**
	* Sets up the object
	*/
	@PostConstruct
	public void init() {
		all.put(FUNCTION.CSHARP_DEV, 				(Set<String>)Set.of("c# developer","c#",".net","dotnet","wpf","asp","vb.net","csharp","asp.net"));
		all.put(FUNCTION.SUPPORT, 					(Set<String>)Set.of("it support analyst","support","helpdesk","service desk"));
		all.put(FUNCTION.BA, 						(Set<String>)Set.of("business analyst","ba"));
		all.put(FUNCTION.UI_UX, 					(Set<String>) Set.of("ui/ux designer","ui\\ux","designer","ui","ux"));
		all.put(FUNCTION.PROJECT_MANAGER, 			(Set<String>)Set.of("project manager","manager","product owner", "pm"));
		all.put(FUNCTION.ARCHITECT, 				(Set<String>)Set.of("architect","solutions","enterprise"));
		all.put(FUNCTION.TESTER, 					(Set<String>)Set.of("test analyst","tester","test", "qa","automation","manual","quality","assurance", "selenium", "cucumber","testing","robot"));
		all.put(FUNCTION.WEB_DEV, 					(Set<String>)Set.of("web developer","front end","front-end","js","vue","vuejs","vue.js","react","node","node.js","php","wordpress"));
		all.put(FUNCTION.SCRUM_MASTER, 				(Set<String>)Set.of("scrum master","scrum","master"));
		all.put(FUNCTION.DATA_SCIENTIST, 			(Set<String>)Set.of("data scientist","data", "data analyst","bi","business intelligence","python"));
		all.put(FUNCTION.NETWORK_ADMINISTRATOR, 	(Set<String>)Set.of("network administrator","devops","network","admin","administrator","ops","operations", "cisco", "cloud", "windows", "ansible", "kubernetes", "salesforce","docker", "citrix", "servicenow", "tibco", "warehouse", "terraform", "dns", "o365", "vmware", "scripting", "firewall", "wireshark", "azure"));
		all.put(FUNCTION.SOFTWARE_DEVELOPER, 		(Set<String>)Set.of("software developer","php","python","wordpress","software engineer", "golang", "c++","vb", "go developer", "cobol", "pl-sql","t-sql", "r developer", "groovy", "sql", "swift", "ios", "scala", "microservices", "oracle", "react", "redux", "android", "node", "plsql", "vue", "bdd", "laravel", "dba", "kotlin", "node.js", "ruby", "embedded", "oauth", "liferay"));
		all.put(FUNCTION.IT_SECURITY, 				(Set<String>)Set.of("it security","security","cyber","malware","owasp", "pen"));
		all.put(FUNCTION.IT_RECRUITER, 				(Set<String>)Set.of("it recruiter","recruiter","account manager"));
		all.put(FUNCTION.SOFTWARE_DEV_IN_TEST, 		(Set<String>)Set.of("sdet","tester","test", "developer in test","qa","automation tester", "selenium", "cucumber","testing"));
		all.put(FUNCTION.JAVA_DEV, 					(Set<String>)Set.of("java developer","java","j2ee","java8","spring"));
	}
	
	/**
	* Refer to CandidateFunctionExtractor for details
	*/
	@Override
	public Set<FUNCTION> extractFunctions(String searchText) {
		
		Set<FUNCTION> identifiedFunctions = new HashSet<>();
		
		if (!StringUtils.hasText(searchText)) {
			return Set.of();
		}
		final String sanitizedSearchText = searchText.toLowerCase();
		
		all.keySet().forEach(function -> {
			if (all.get(function).stream().filter(value -> sanitizedSearchText.contains(value)).count() > 0) {
				identifiedFunctions.add(function);
			}
		});
		
		return identifiedFunctions;
	}

}
