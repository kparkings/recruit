package com.arenella.recruit.candidates.extractors;

import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;

/**
* Class to extract the required seniority for the role
* @author K Parkings
*/
@Component
public class SeniorityExtractor implements JobSpecifcationFilterExtractor{
	
	/**
	* Refer to JobSpecifcationFilterExtractor interface for details
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder) {
		
		boolean senior = documentText.contains("senior");
		boolean medior = documentText.contains("medior");
		boolean junior = documentText.contains("junior") || documentText.contains("entry level") || documentText.contains("graduate");
		
		if (!senior && !medior && !junior) {
			return;
		}
		
		if (senior && medior && junior) {
			return;
		}
		
		if (medior && junior) {
			filterBuilder.experienceGTE("0");
			filterBuilder.experienceLTE("4");
			return;
		}
		
		if (medior && senior) {
			filterBuilder.experienceGTE("4");
			filterBuilder.experienceLTE("8");
			return;
		}
		
		if (senior && !medior && !junior) {
			filterBuilder.experienceGTE("8");
			filterBuilder.experienceLTE("");
			return;
		}
		
		if (medior && !senior && !junior) {
			filterBuilder.experienceGTE("4");
			filterBuilder.experienceLTE("8");
			return;
		}
		
		if (junior && !senior && !medior) {
			filterBuilder.experienceGTE("");
			filterBuilder.experienceLTE("2");
			return;
		}
		
	}
	
}
