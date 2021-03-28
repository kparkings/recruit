package com.arenella.recruit.candidates.services;

import java.util.Set;

import com.arenella.recruit.candudates.beans.Candidate;

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
	* Retrieves a list of Candidates
	* @return Candidates
	*/
	public Set<Candidate> getCandidates();
	
	/**
	* Retrieves a specific Candidate based upon 
	* their candidate ID
	* @param candidateId - Unique Id of the Candidate
	* @return Candidate matching the Id
	*/
	public Candidate getCandidate(String candidateId);
	
}
