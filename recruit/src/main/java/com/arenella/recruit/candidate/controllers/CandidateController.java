package com.arenella.recruit.candidate.controllers;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.candidate.services.CandidateService;

/**
* REST API for working with Candidates
* @author K Parkings
*/
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CandidateController {

	@Autowired
	private CandidateService candidateService;
	
	/**
	* Adds a new Candidate
	* @param candidate - Contains candidate details
	* @return id of the candidate
	*/
	@PostMapping(path="candidate", consumes="application/json", produces="application/json")
	public void addCandidate(@RequestBody CandidateAPIInbound candidate) {
		candidateService.persistCandidate(CandidateAPIInbound.convertToCandidate(candidate));
	}
	
	/**
	* Returns all available Candidates
	* @return Available Candidates
	*/
	@GetMapping(path="candidate")
	public Set<CandidateAPIOutbound> getCandidate() {
		return candidateService.getCandidates().stream().map(candidate -> CandidateAPIOutbound.convertFromCandidate(candidate)).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
}
