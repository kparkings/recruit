package com.arenella.recruit.candidates.services;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.controllers.CandidateController.CANDIDATE_UPDATE_ACTIONS;

/**
* Defines services available for interacting with Candidates
* @author K Parkings
*/
public interface CandidateService {

	/**
	* Persists a Candidate. 
	* @param candidate - Candidate to persist
	* @return CandidateId
	*/
	public void persistCandidate(Candidate candidate);
	
	/**
	* Performs an update on a specific Candidate
	* @param candidateId	- Unique identifier of the Candidate to be updated
	* @param updateAction	- Update action to be performed
	*/
	public void updateCandidate(String candidateId, CANDIDATE_UPDATE_ACTIONS updateAction);
	
	/**
	* Flags a Candidate as being unavailable. This does not deactivate the Candidate as 
	* the availability needs to be confirmed by an admin user.
	* @param candidateId - Unique Id of the candidate
	* @param available 		- Whether or not the candidate is available
	*/
	public void flagCandidateAvailability(long candidateId, boolean available);
	
	/**
	* Retrieves a list of Candidates
	* @param filterOptions  - filters to apply to the results
	* @return Candidates
	*/
	public Page<Candidate> getCandidates(CandidateFilterOptions filterOptions, Pageable pageable);
	
	/**
	* Retrieves a list of Candidates
	* @param filterOptions - filters to apply to the results
	* @return Candidates
	*/
	public Set<Candidate> getCandidates(CandidateFilterOptions filterOptions);
	
}
