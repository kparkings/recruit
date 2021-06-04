package com.arenella.recruit.candidates.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.candidates.services.CandidateStatisticsService;

/**
* Provides statistics relating to Candidates
* @author K Parkings
*/
@CrossOrigin(origins = {	"https://api-arenella-ict.wosah.nl/authenticate"	
		,	"https://arenella-ict.wosah.nl"
		, 	"http://arenella-ict.wosah.nl"
		,	"https://arenella-ict.wosah.nl/"
		, 	"http://arenella-ict.wosah.nl/"
		,  	"http://api-arenella-ict.wosah.nl/"
		, 	"https://api-arenella-ict.wosah.nl/"
		, 	"http://api-arenella-ict.wosah.nl"
		, 	"https://api-arenella-ict.wosah.nl"
		,	"http://api.arenella-ict.com/"
		, 	"htts://api.arenella-ict.com/"
		, 	"http://127.0.0.1:4200"
		, 	"http://127.0.0.1:8080"
		, 	"http://127.0.0.1:9090"
		,	"https://www.arenella-ict.com"
		, 	"https://www.arenella-ict.com:4200"
		, 	"https://www.arenella-ict.com:8080"}, allowedHeaders = "*")
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
	* @return
	*/
	@GetMapping(path="candidate/stat/function-count")
	public ResponseEntity<List<CandidateRoleStatsAPIOutbound>> fetchCandidateRoleStats(){
		return ResponseEntity.ok(candidateStatisticsService.fetchCandidateRoleStats().stream().map(stat -> CandidateRoleStatsAPIOutbound.convertFromDomain(stat)).collect(Collectors.toCollection(LinkedList::new)));
	}
	
}
