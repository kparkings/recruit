package com.arenella.recruit.recruiters.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.recruiters.beans.RecruiterMarketplaceViewStatsAPIOutput;
import com.arenella.recruit.recruiters.services.SupplyAndDemandService;

/**
* Statistics Controller for SupplyAndDemand
* @author K Parkings
*/
@RestController
public class SupplyAndDemandStatisticsContoller {

	@Autowired
	private SupplyAndDemandService supplyAndDemandService;
	
	/**
	* Returns stats for Open Positions viewed by Recruiter for the current week
	* @return Stats for current week
	*/
	@GetMapping(value="/v1/open-position/stats/week/")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<RecruiterMarketplaceViewStatsAPIOutput> getOpenPositionsWeeklyStats(){
		return ResponseEntity.status(HttpStatus.OK).body(new RecruiterMarketplaceViewStatsAPIOutput(supplyAndDemandService.fetchOpenPositionViewStats()));
	}
	
	/**
	* Returns stats for offered candidates viewed by Recruiter for the current week
	* @return Stats for current week
	*/
	@GetMapping(value="/v1/offered-candidate/stats/week/")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<RecruiterMarketplaceViewStatsAPIOutput> getOfferedCandidatesWeeklyStats(){
		return ResponseEntity.status(HttpStatus.OK).body(new RecruiterMarketplaceViewStatsAPIOutput(supplyAndDemandService.fetchOfferedCandidateViewStats()));
	}
	
}
