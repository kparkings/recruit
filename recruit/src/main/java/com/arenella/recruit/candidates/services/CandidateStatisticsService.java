package com.arenella.recruit.candidates.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateRoleStats;
import com.arenella.recruit.candidates.beans.CandidateSearchEvent;
import com.arenella.recruit.candidates.beans.RecruiterStats;
import com.arenella.recruit.candidates.controllers.CandidateStatisticsController.STAT_PERIOD;
import com.arenella.recruit.candidates.controllers.SearchStats;

/**
* Defines services relating to Candidate statistics
* @author K Parkings
*/
public interface CandidateStatisticsService {

	public enum NEW_STATS_TYPE {NEW_CANDIDATES, NEW_CANDIDATE_BREAKDOWN}
	
	/**
	* Returns the total number of active candidates in the system
	* @return number of candidates actively looking for a new role
	*/
	public Long fetchNumberOfAvailableCandidates();
	
	/**
	* Returns a Summary of the number of candidates available 
	* per role
	* @throws Exception
	* @return
	*/
	public List<CandidateRoleStats> fetchCandidateRoleStats() throws Exception;
	
	/**
	* Logs details of a Candidate Search performed
	* @param filterOptions - Contains details of the Search
	*/
	public void logCandidateSearchEvent(CandidateFilterOptions filterOptions);
	
	/**
	* Fetches Candidates added since the last time the method was called
	* @return New Candidates
	*/
	public Set<Candidate> fetchNewCandidates(LocalDate since);	
	
	/**
	* Returns the last date that a request for new Candidates was run
	* @param statsType - Type of stat being worked with
	* @return Last date the request for candidates was run
	*/
	public LocalDate getLastRunDateNewCandidateStats(NEW_STATS_TYPE statsType);

	/**
	* Returns Stat's for a specific Recruiter
	* @param recruiterId - Id of Recruiter to return stats for
	* @param period 	 - Period to return stats for
	* @return Stat's
	*/
	public RecruiterStats fetchSearchStatsForRecruiter(String recruiterId, STAT_PERIOD period);

	/**
	* Returns a breakdown of Search behavior
	* @return Stats relating to Search behavior
	*/
	public Set<CandidateSearchEvent> fetchCandidateSearchEvents(int inPastDays);
	
}
