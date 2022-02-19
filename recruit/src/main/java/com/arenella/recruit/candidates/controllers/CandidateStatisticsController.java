package com.arenella.recruit.candidates.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.candidates.services.CandidateStatisticsService;

/**
* Provides statistics relating to Candidates
* @author K Parkings
*/
@RestController
public class CandidateStatisticsController {

	@Autowired
	private CandidateStatisticsService candidateStatisticsService;
	
	/**
	* Returns the total number of candidates actively 
	* looking for a new role
	* @return number of available candidates
	*/
	@GetMapping(path="candidate/stats/total-active")
	public ResponseEntity<Long> fetchNumberOfCandidates() {
		return ResponseEntity.ok(candidateStatisticsService.fetchNumberOfAvailableCandidates());
	}
	
	/**
	* Returns a breakdown of the number of available candidates
	* per Function
	* @return Stats about available Candidates by role
	*/
	@GetMapping(path="candidate/stat/function-count")
	public ResponseEntity<List<CandidateRoleStatsAPIOutbound>> fetchCandidateRoleStats(){
		return ResponseEntity.ok(candidateStatisticsService.fetchCandidateRoleStats().stream().map(stat -> CandidateRoleStatsAPIOutbound.convertFromDomain(stat)).collect(Collectors.toCollection(LinkedList::new)));
	}
	
	/**
	* Logs that a request was made to view a Candidates email address
	* @param candidateId - Id of candidate whose email address was requested
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECRUITER')")
	@PostMapping(path="candidate/stat/email-request")
	public ResponseEntity<Void> logEventEmailRequestedEvent(@RequestBody() long candidateId) {
		this.candidateStatisticsService.logEventEmailRequested(candidateId);
		return ResponseEntity.ok().build();
	}
	
	/**
	* Returns stats relating to email addresses requested by
	* individual recruiters
	* @return
	*/
	@GetMapping(path="candidate/stat/email-request")
	public ResponseEntity<EmailRequestsStatisticsAPIOutbound> fetchEmailRequestStatus(){
		
		return ResponseEntity.ok(new EmailRequestsStatisticsAPIOutbound(this.candidateStatisticsService.fetchEmailRequestEvents()));
		
	}
	
}
