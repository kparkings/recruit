package com.arenella.recruit.candidates.services;

import java.util.List;

import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateRoleStats;

/**
* Defines services relating to Candidate statistics
* @author K Parkings
*/
public interface CandidateStatisticsService {

	/**
	* Returns the total number of active candidates in the system
	* @return number of candidates actively looking for a new role
	*/
	public Long fetchNumberOfAvailableCandidates();
	
	/**
	* Returns a Summary of the number of candidates available 
	* per role
	* @return
	*/
	public List<CandidateRoleStats> fetchCandidateRoleStats();
	
	/**
	* Logs details of a Candidate Search performed
	* @param filterOptions - Contains detials of the Search
	*/
	public void logCandidateSearchEvent(CandidateFilterOptions filterOptions);
	
}
