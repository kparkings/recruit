package com.arenella.recruit.candidates.extractors;

import com.arenella.recruit.candidates.beans.CandidateExtractedFilters.CandidateExtractedFiltersBuilder;

/**
* Defines behaviour for extracting Job specification filters from a documents text
*/
public interface JobSpecifcationFilterExtractor {

	/**
	* Sets filters related to a Candidate search based upon the text from a job specification
	* @param documentText	- Test from job specification document
	* @param filterBuilder	- Builder for filters that can be set by the extractors
	*/
	public void extractFilters(String documentText, CandidateExtractedFiltersBuilder filterBuilder);
	
}
