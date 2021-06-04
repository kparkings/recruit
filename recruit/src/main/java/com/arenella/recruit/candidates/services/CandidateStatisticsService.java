package com.arenella.recruit.candidates.services;

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
	
}
