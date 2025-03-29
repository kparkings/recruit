package com.arenella.recruit.candidates.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Functions for extracting Candidate Functions
* @author K Parkings
*/
@Component
public class CandidateFunctionExtractorImpl implements CandidateFunctionExtractor {

	private Map<FUNCTION,	Set<String>> all 			= new HashMap<>();
		
	/**
	* Sets up the object
	*/
	@PostConstruct
	public void init() {
		all.put(FUNCTION.CSHARP_DEV, 				Set.of("c# developer","c#",".net","dotnet","wpf","asp","vb.net","csharp","asp.net"));
		all.put(FUNCTION.SUPPORT, 					Set.of("it support analyst","support","helpdesk","service desk"));
		all.put(FUNCTION.BA, 						Set.of("business analyst","ba"));
		all.put(FUNCTION.UI_UX, 					Set.of("ui/ux designer","ui\\ux","designer","ui","ux"));
		all.put(FUNCTION.PROJECT_MANAGER, 			Set.of("project manager", "pm", "it project manager", "pmo"));
		all.put(FUNCTION.ARCHITECT, 				Set.of("architect","solutions","enterprise"));
		all.put(FUNCTION.TESTER, 					Set.of("test analyst","tester","qa","automation","manual","quality","assurance", "selenium", "cucumber","testing","robot","playwright"));
		all.put(FUNCTION.WEB_DEV, 					Set.of("web developer","front end","front-end","js","wordpress"));
		all.put(FUNCTION.SCRUM_MASTER, 				Set.of("scrum master","scrum","master"));
		all.put(FUNCTION.DATA_SCIENTIST, 			Set.of("data scientist","data", "data analyst","bi","business intelligence"));
		all.put(FUNCTION.NETWORK_ADMINISTRATOR, 	Set.of("network administrator","devops","network","admin","administrator","ops","operations", "cisco", "cloud", "windows", "ansible", "kubernetes", "salesforce","docker", "citrix", "servicenow", "tibco", "warehouse", "terraform", "dns", "o365", "vmware", "scripting", "firewall", "wireshark", "azure"));
		all.put(FUNCTION.SOFTWARE_DEVELOPER, 		Set.of("software developer","wordpress","software engineer", "golang", "c++","vb",  "cobol", "pl-sql","t-sql", "r developer", "groovy", "sql", "swift",  "scala", "microservices", "oracle", "plsql", "bdd", "dba", "embedded", "oauth", "liferay"));
		all.put(FUNCTION.IT_SECURITY, 				Set.of("it security","security","cyber","malware","owasp", "pen"));
		all.put(FUNCTION.IT_RECRUITER, 				Set.of("it recruiter","recruiter","account manager"));
		all.put(FUNCTION.SOFTWARE_DEV_IN_TEST, 		Set.of("sdet","tester", "developer in test","qa","automation tester", "selenium", "cucumber","testing"));
		all.put(FUNCTION.JAVA_DEV, 					Set.of("java developer","java","j2ee","java8","spring"));
	
		all.put(FUNCTION.RUBY, 						Set.of("ruby"));
		all.put(FUNCTION.RUBY_ON_RAILS, 			Set.of("ruby on rails"));
		all.put(FUNCTION.GO, 						Set.of("go"));
		all.put(FUNCTION.REACT, 					Set.of("react","reactjs","react.js"));
		all.put(FUNCTION.VUE, 						Set.of("vue","vuejs","vue.js"));
		all.put(FUNCTION.NEXT, 						Set.of("next","nextjs","next.js"));
		all.put(FUNCTION.EXPRES, 					Set.of("expres","expresjs","expres.js"));
		all.put(FUNCTION.RUST, 						Set.of("rust"));
		all.put(FUNCTION.TEST_MANAGER, 				Set.of("test manager","qa manager"));
		all.put(FUNCTION.PRODUCT_OWNER,				Set.of("product owner"));
		all.put(FUNCTION.NODE, 						Set.of("node","nodejs","node.js"));
		all.put(FUNCTION.PYTHON, 					Set.of("python"));
		all.put(FUNCTION.ANGULAR, 					Set.of("angular","angularjs","angular.js"));
		all.put(FUNCTION.PHP, 						Set.of("php", "laravel"));
		all.put(FUNCTION.ANDROID, 					Set.of("android","mobile"));
		all.put(FUNCTION.IOS, 						Set.of("ios","mobile"));
		all.put(FUNCTION.KOTLIN, 					Set.of("kotlin"));
	
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
		
		/**
		* Means user doesn't have to add a trailing space to trigger match 
		*/
		searchText = searchText + " ";
		
		final String sanitizedSearchText = searchText.toLowerCase();
		
		
		
		all.keySet().forEach(function -> {
			if (all.get(function).stream().filter(value -> sanitizedSearchText.contains(value + " ")).count() > 0) {
				identifiedFunctions.add(function);
			}
		});
		
		return identifiedFunctions;
	}

}
