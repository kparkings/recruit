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
public class CandidateFunctionExtractorBOOPUPDATE {

	private Map<FUNCTION,	Set<String>> all 			= new HashMap<>();
	
	/**
	* Sets up the object
	*/
	@PostConstruct
	public void init() {
	
		all.put(FUNCTION.RUBY, 						Set.of("ruby"));
		all.put(FUNCTION.RUBY_ON_RAILS, 			Set.of("ruby on rails"));
		all.put(FUNCTION.GO, 						Set.of("go"));
		all.put(FUNCTION.REACT, 					Set.of("react","reactjs","react.js"));
		all.put(FUNCTION.VUE, 						Set.of("vue","vuejs","vue.js"));
		all.put(FUNCTION.NEXT, 						Set.of("next","nextjs","next.js"));
		all.put(FUNCTION.EXPRES, 					Set.of("expres","expresjs","expres.js"));
		all.put(FUNCTION.RUST, 						Set.of("rust"));
		all.put(FUNCTION.TEST_MANAGER, 				Set.of("test manager","qa manager"));
		all.put(FUNCTION.PRODUCT_OWNER,				Set.of("product owner","pmo"));
		all.put(FUNCTION.NODE, 						Set.of("node","nodejs","node.js"));
		all.put(FUNCTION.PYTHON, 					Set.of("python"));
		all.put(FUNCTION.ANGULAR, 					Set.of("angular","angularjs","angular.js"));
		all.put(FUNCTION.PHP, 						Set.of("php"));
		all.put(FUNCTION.ANDROID, 					Set.of("android"));
		all.put(FUNCTION.IOS, 						Set.of("ios"));
		all.put(FUNCTION.KOTLIN, 					Set.of("kotlin"));
	
	}
	
	
	
	/**
	* Refer to CandidateFunctionExtractor for details
	*/
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
