package com.arenella.recruit.recruiters.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.recruiters.beans.RecruiterMarketplaceViewStatsAPIOutput;
import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent;
import com.arenella.recruit.recruiters.services.SupplyAndDemandService;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
* Unit tests for the SupplyAndDemandStatisticsContoller class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class SupplyAndDemandStatisticsContollerTest {

	final String recruiterId = "kparkings";
	
	@InjectMocks 
	private SupplyAndDemandStatisticsContoller 	controller 				= new SupplyAndDemandStatisticsContoller();
	
	@Mock
	private SupplyAndDemandService 				supplyAndDemandService;
	
	/**
	* Tests response from request to fetch weekly stat's for Open Positions
	* @throws Exception
	*/
	@Test
	public void testGetOpenPositionsWeeklyStats() throws Exception{
		
		Mockito.when(this.supplyAndDemandService.fetchOpenPositionViewStats()).thenReturn(Set.of(SupplyAndDemandEvent
				.builder()
					.created(LocalDateTime.of(2022, 12, 13,20, 46)).recruiterId(recruiterId).build()));
		
		ResponseEntity<RecruiterMarketplaceViewStatsAPIOutput> response = controller.getOpenPositionsWeeklyStats();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		response.getBody().getStats().stream().filter(s -> s.getRecruiterId().equals(recruiterId)).findAny().orElseThrow();
		
	}
	
	/*
	* Tests response from request to fetch weekly stat's for Offered Candidates
	* @throws Exception
	*/
	@Test
	public void testGetOfferedCandidatesWeeklyStats() throws Exception{
		
		Mockito.when(this.supplyAndDemandService.fetchOfferedCandidateViewStats()).thenReturn(Set.of(SupplyAndDemandEvent
				.builder()
					.created(LocalDateTime.of(2022, 12, 13,20, 46)).recruiterId(recruiterId).build()));
		
		ResponseEntity<RecruiterMarketplaceViewStatsAPIOutput> response = controller.getOfferedCandidatesWeeklyStats();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		response.getBody().getStats().stream().filter(s -> s.getRecruiterId().equals(recruiterId)).findAny().orElseThrow();
		
	}
	
}