package com.arenella.recruit.listings.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

/**
* Util for extracting Functions and synonyms from a piece 
* textt
*/
@Component
public class ListingFunctionSynonymUtil {

	public static enum TYPE {	JAVA, DOTNETT, PYTHTON, GO, DEVOPS, SRE, UIUX, QA, SUPPORT, SECURITY, BA, ARCHITECT, BI, 
								RECRUITER, WEB, RUBY, RUBY_ON_RAILS, PYTHON, PHP, SAP, IOS, ANDROID, KOTLIN, SCRUM_MASTER, MANAGER, DJANGO, DBA};
	
	private static final Set<String> GERNERIC_TERM_EXCLUSIONS = Set.of(	"DEVELOPER", "ONTWIKKELAAR", "PROGRAMMER", "EXPERT", "MEDIOR", "CONSULTANT");
	
	private static final Map<TYPE, Set<String>> all = new HashMap<>();
	
	/**
	* Constructor 
	* Sets up synonym data  
	*/
	public ListingFunctionSynonymUtil() {
		all.put(TYPE.JAVA, 			Set.of("JAVA","J2EE","SPRING"));
		all.put(TYPE.DOTNETT, 		Set.of("C#","DOTNETT", "DOT NET", ".NET","WPF"));
		all.put(TYPE.PYTHTON, 		Set.of("PYTHON"));
		all.put(TYPE.GO,			Set.of("GO"));
		all.put(TYPE.DEVOPS, 		Set.of("DEVOPS", "NETWORK", "CLOUD", "AZURE", "AWS", "LINUX", "OPS"));
		all.put(TYPE.SRE, 			Set.of("SRE", "SITE RELIABILITY"));
		all.put(TYPE.UIUX, 			Set.of("UIUX", "UI", "UX"));
		all.put(TYPE.QA, 			Set.of("QA", "TESTER", "TEST", "SDET"));
		all.put(TYPE.SUPPORT, 		Set.of("SUPPORT", "HELPDESK", "SERVICE DESK"));
		all.put(TYPE.SECURITY, 		Set.of("SECURITY", "HACKER", "PENTESTER", "DevSecOps"));
		all.put(TYPE.BA, 			Set.of("BA", "BUSINESS ANALYST"));
		all.put(TYPE.ARCHITECT, 	Set.of("ARCHITECT"));
		all.put(TYPE.BI, 			Set.of("BUSINESS INTELLIGENT", "BI", "DATA ANALYST", "DATA ENGINEER", "POWERBI"));
		all.put(TYPE.RECRUITER, 	Set.of("RECRUITER", "RECRUITMENT"));
		all.put(TYPE.WEB, 			Set.of("FE", "REACT", "ANGULAR", "VUE", "NEXT","TYPESCRIPT","WEB"));
		all.put(TYPE.RUBY, 			Set.of("RUBY"));
		all.put(TYPE.RUBY_ON_RAILS, Set.of("RUBY ON RAILS"));
		all.put(TYPE.PYTHON, 		Set.of("PYTHON"));
		all.put(TYPE.PHP, 			Set.of("PHP","LARAVEL"));
		all.put(TYPE.SAP, 			Set.of("SAP"));
		all.put(TYPE.IOS, 			Set.of("IOS","MOBILE"));
		all.put(TYPE.ANDROID, 		Set.of("ANDROID","MOBILE","KOTLIN"));
		all.put(TYPE.KOTLIN, 		Set.of("KOTLIN"));
		all.put(TYPE.SCRUM_MASTER, 	Set.of("SCRUM MASTER", "AGILE", "PRODUCT OWNER"));
		all.put(TYPE.MANAGER, 		Set.of("PMO", "MANAGER", "PRODUCT OWNER"));
		all.put(TYPE.DJANGO, 		Set.of("DJANGO"));
		all.put(TYPE.DBA, 			Set.of("DBA","ORACLE","MYSQL","POSTGRES","POSTGRESSQL","DATATBASE","T-SQL","PL-SQL"));
	}
	
	/**
	* Attempts to determine the type of function the User is searching for. Along with their 
	* original search term adds the synonyms for the detected function type
	* @param searchTerm - Term user is searching on
	* @return synonyms for searched function type
	*/
	public static Set<String> extractAllFunctionAndSynonyms(String searchTerm){
		
		Set<String> functionsAndSynonyms = new HashSet<>();
		
		searchTerm = searchTerm.toUpperCase();
		searchTerm = searchTerm.trim();
		
		functionsAndSynonyms.add(searchTerm);
		
		AtomicReference<String> rawTerm = new AtomicReference<>(searchTerm);
		
		GERNERIC_TERM_EXCLUSIONS.stream().forEach(term -> rawTerm.set(rawTerm.get().replaceAll(term, "")));
		
		all.keySet().forEach(tech -> {
			all.get(tech).forEach(synonym -> {
				if (rawTerm.get().contains(synonym)) {
					functionsAndSynonyms.addAll(all.get(tech));
				}
			});
		});
		
		return functionsAndSynonyms;
		
	}
	
}