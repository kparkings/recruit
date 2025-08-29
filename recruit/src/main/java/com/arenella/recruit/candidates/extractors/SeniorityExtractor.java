package com.arenella.recruit.candidates.extractors;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;

/**
* Class to extract the required seniority for the role
* @author K Parkings
*/
@Component
public class SeniorityExtractor implements JobSpecifcationFilterExtractor{
	
	public static final String JUNIOR_EDGE_MIN = "";
	public static final String JUNIOR_EDGE_MAX = "3";
	public static final String MEDIOR_EDGE_MIN = "3";
	public static final String MEDIOR_EDGE_MAX = "5";
	public static final String SENIOR_EDGE_MIN = "5";
	public static final String SENIOR_EDGE_MAX = "";
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
		
		//6 jaar ervaring - Add these checking for all common levels also in english and french
		
		Set<String> senior = new HashSet<>();
		Set<String> medior = new HashSet<>();
		Set<String> junior = new HashSet<>();
				
		senior.addAll(Set.of("principal developer","principal engineer","lead engineer","lead developer","senior","expérimenté","expert ","ervaren", " lead "));
		medior.addAll(Set.of("medior","midweight"," - 5 year"," - 5 jaar"));
		junior.addAll(Set.of("junior","entry level","graduate "," - 3 year"," - 3 jaar"));
		
		for (int i=1 ; i <= 3; i++) {
			junior.add("minimum "+i +" year");
			junior.add(i+" years");
			junior.add(i+"+ years");
			junior.add("minimaal "+i + " jaar");
			junior.add(i+" jaar");
			junior.add(i+"+ jaar");
			
			junior.add(i+" ans");
			junior.add(i+"+ ans");
			
			
		}
		
		for (int i=3 ; i <= 4; i++) {
			medior.add("minimum "+i+" year");
			medior.add(i+" years");
			medior.add(i+"+ years");
			medior.add("minimaal "+i + " jaar");
			medior.add(i+" jaar");
			medior.add(i+"+ jaar");
			medior.add("+"+i + " ");
			medior.add(">"+i + " ");
		
			medior.add(i+" ans");
			medior.add(i+"+ ans");
		}
		
		for (int i=5 ; i <= 30; i++) {
			senior.add("minimum "+i+" year");
			senior.add(i+" years");
			senior.add(i+"+ years");
			senior.add("minimaal "+i + " jaar");
			senior.add(i+" jaar");
			senior.add(i+"+ jaar");
			senior.add("+"+i + " ");
			senior.add(">"+i + " ");
			
			senior.add(i+" ans");
			senior.add(i+"+ ans");
		}
		
		boolean juniorMatches = junior.stream().anyMatch(documentText.replaceAll(" - 5", "")::contains);
		boolean mediorMatches = medior.stream().anyMatch(documentText.replaceAll(" - 5", "")::contains);
		boolean seniorMatches = senior.stream().anyMatch(documentText.replaceAll(" - 5", "")::contains);
		
		//List<String> debugJunior = junior.stream().filter(documentText.replaceAll(" - 5", "")::contains).toList();
		//List<String> debugMedior = medior.stream().filter(documentText.replaceAll(" - 5", "")::contains).toList();
		//List<String> debugSenior = senior.stream().filter(documentText.replaceAll(" - 5", "")::contains).toList();
		
		if (!seniorMatches && !mediorMatches && !juniorMatches) {
			return;
		}
		
		if (seniorMatches && mediorMatches && juniorMatches) {
			return;
		}
		
		if (juniorMatches && mediorMatches) {
			filterBuilder.experienceGTE(JUNIOR_EDGE_MIN);
			filterBuilder.experienceLTE(MEDIOR_EDGE_MAX);
			return;
		}
		
		if (mediorMatches && seniorMatches) {
			filterBuilder.experienceGTE(MEDIOR_EDGE_MIN);
			filterBuilder.experienceLTE(SENIOR_EDGE_MAX);
			return;
		}
		
		if (juniorMatches && seniorMatches) {
			filterBuilder.experienceGTE(JUNIOR_EDGE_MIN);
			filterBuilder.experienceLTE(SENIOR_EDGE_MAX);
			return;
		}
		
		if (juniorMatches && !seniorMatches && !mediorMatches) {
			filterBuilder.experienceGTE(JUNIOR_EDGE_MIN);
			filterBuilder.experienceLTE(JUNIOR_EDGE_MAX);
			return;
		}
		
		if (mediorMatches && !seniorMatches && !juniorMatches) {
			filterBuilder.experienceGTE(MEDIOR_EDGE_MIN);
			filterBuilder.experienceLTE(MEDIOR_EDGE_MAX);
			return;
		}
		
		if (seniorMatches && !mediorMatches && !juniorMatches) {
			filterBuilder.experienceGTE(SENIOR_EDGE_MIN);
			filterBuilder.experienceLTE(SENIOR_EDGE_MAX);
			return;
		}
	}
	
}
