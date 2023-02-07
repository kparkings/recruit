package com.arenella.recruit.candidates.extractors;

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
		
		
		boolean senior = documentText.contains("senior") || documentText.contains("expérimenté");
		boolean medior = documentText.contains("medior");
		boolean junior = documentText.contains("junior") || documentText.contains("entry level") || documentText.contains("graduate");
		
		
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
