package com.arenella.recruit.candidates.extractors;

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
		
		
		boolean senior = documentText.contains("lead engineer") || documentText.contains("lead developer") || documentText.contains("senior") || documentText.contains("expérimenté") || documentText.contains("expert ") || documentText.contains("ervaren") || documentText.contains("minimum 5") || documentText.contains("minimum 10") || documentText.contains("8 years") || documentText.contains("8 jaar")|| documentText.contains("10 years") || documentText.contains("10 jaar");
		boolean medior = documentText.contains("medior") || documentText.contains("midweight") || documentText.contains("3+") || documentText.contains("+3 ") || documentText.contains(">3") || documentText.contains("4+") || documentText.contains("+4 ") || documentText.contains(">4");
		boolean junior = documentText.contains("junior") || documentText.contains("entry level") || documentText.contains("graduate ");
		
		Set<String> years = Set.of("+5 ","+6 ","+7 ","+8 ","+9 ","+10 ","+20 ","5+","6+","7+","8+","9+","10+","20+",">5 ",">6 ",">7 ",">8 ",">9 ",">10 ",">20");
		
		if (!senior) {
			senior = years.stream().filter(y -> documentText.contains(y)).findAny().isPresent();
		}
		
		if (!senior && !medior && !junior) {
			return;
		}
		
		if (senior && medior && junior) {
			return;
		}
		
		if (junior && medior) {
			filterBuilder.experienceGTE(JUNIOR_EDGE_MIN);
			filterBuilder.experienceLTE(MEDIOR_EDGE_MAX);
			return;
		}
		
		if (medior && senior) {
			filterBuilder.experienceGTE(MEDIOR_EDGE_MIN);
			filterBuilder.experienceLTE(SENIOR_EDGE_MAX);
			return;
		}
		
		if (junior && senior) {
			filterBuilder.experienceGTE(JUNIOR_EDGE_MIN);
			filterBuilder.experienceLTE(SENIOR_EDGE_MAX);
			return;
		}
		
		if (junior && !senior && !medior) {
			filterBuilder.experienceGTE(JUNIOR_EDGE_MIN);
			filterBuilder.experienceLTE(JUNIOR_EDGE_MAX);
			return;
		}
		
		if (medior && !senior && !junior) {
			filterBuilder.experienceGTE(MEDIOR_EDGE_MIN);
			filterBuilder.experienceLTE(MEDIOR_EDGE_MAX);
			return;
		}
		
		if (senior && !medior && !junior) {
			filterBuilder.experienceGTE(SENIOR_EDGE_MIN);
			filterBuilder.experienceLTE(SENIOR_EDGE_MAX);
			return;
		}
	}
	
}
