package com.arenella.recruit.candidates.utils;

import java.util.Set;

import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;

/**
* Defines functionality for utilities relating to 
* determining Suggestions for candidates matching 
* specific filters
* @author K Parkings
*/
public interface CandidateSuggestionUtil {

	public enum suggestion_accuracy {perfect, excellent, good, average, poor}
	
	/**
	* Whether the Candidate is considered a perfect Match based 
	* upon the search criteria
	* @param candidate - Candidate to check 
	* @param filterOptions - Filter requirements to check against
	* @param searchTermKeywords - keywords extracted from the searchTerm
	* @return Whether or not the Candidate is considered a perfect match
	*/
	public boolean isPerfectMatch(CandidateSearchAccuracyWrapper candidate, CandidateFilterOptions filterOptions, Set<String> searchTermKeywords);
	
	/**
	* Whether the Candidate is considered an excellent Match based 
	* upon the search criteria
	* @param candidate - Candidate to check 
	* @param filterOptions - Filter requirements to check against
	* @param searchTermKeywords - keywords extracted from the searchTerm
	* @return Whether or not the Candidate is considered a perfect match
	*/
	public boolean isExcellentMatch(CandidateSearchAccuracyWrapper candidate, CandidateFilterOptions filterOptions, Set<String> searchTermKeywords);
	
	/**
	* Whether the Candidate is considered a good Match based 
	* upon the search criteria
	* @param candidate - Candidate to check 
	* @param filterOptions - Filter requirements to check against
	* @param searchTermKeywords - keywords extracted from the searchTerm
	* @return Whether or not the Candidate is considered a perfect match
	*/
	public boolean isGoodMatch(CandidateSearchAccuracyWrapper candidate, CandidateFilterOptions filterOptions, Set<String> searchTermKeywords);
	
	/**
	* Whether the Candidate is considered an average Match based 
	* upon the search criteria
	* @param candidate - Candidate to check 
	* @param filterOptions - Filter requirements to check against
	* @param searchTermKeywords - keywords extracted from the searchTerm
	* @return Whether or not the Candidate is considered a perfect match
	*/
	public boolean isAverageMatch(CandidateSearchAccuracyWrapper candidate, CandidateFilterOptions filterOptions, Set<String> searchTermKeywords);
	
	/**
	* Whether the Candidate is considered a perfect Match based 
	* upon the search criteria
	* @param candidate - Candidate to check 
	* @param filterOptions - Filter requirements to check against
	* @param searchTermKeywords - keywords extracted from the searchTerm
	* @return Whether or not the Candidate is considered a perfect match
	*/
	public boolean isPoorMatch(CandidateSearchAccuracyWrapper candidate, CandidateFilterOptions filterOptions, Set<String> searchTermKeywords);
	
}