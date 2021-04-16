package com.arenella.recruit.candidates.services;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arenella.recruit.candudates.beans.Candidate;
import com.arenella.recruit.candudates.beans.CandidateFilterOptions;

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
	
	/**
	* Retrieves a specific Candidate based upon 
	* their candidate ID
	* @param candidateId - Unique Id of the Candidate
	* @return Candidate matching the Id
	*/
	public Candidate getCandidate(String candidateId);
	
}
