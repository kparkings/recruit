package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.PendingCandidate;
import com.arenella.recruit.candidates.controllers.CandidateController.CANDIDATE_UPDATE_ACTIONS;
import com.arenella.recruit.candidates.services.CandidateDownloadService;
import com.arenella.recruit.candidates.services.CandidateService;

/**
* Unit tests for the CandidateController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CandidateControllerTest {
	
	@Mock
	private CandidateService			mockCandidateService;
	
	@Mock
	private CandidateDownloadService 	mockCandidateDownloadService;
	
	@Mock
	private Authentication 				mockAuthentication;
	
	@InjectMocks
	private CandidateController 		controller;
	
	/**
	* Tests Incoming Candidate is converted to Domain Candidate
	* and passed to the service to be persisted
	* @throws Exception
	*/
	@Test
	public void testPersistCandidate() throws Exception{
		
	}
	
	/**
	* Tests all candidates are returned
	* @throws Exception
	*/
	@Test
	public void testGetCandidates() throws Exception{
		
	}
	
	/**
	* Tests retrieval of Paginated candidates
	* @throws Exception
	*/
	@Test
	public void testGetCandidate() throws Exception{
	
		Page<Candidate> candidatePage = Page.empty();
		
		Mockito.when(this.mockCandidateService.getCandidates(Mockito.any(), Mockito.any())).thenReturn(candidatePage);
		
		this.controller.getCandidate(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,false,null);

		Mockito.verify(this.mockCandidateService).getCandidates(Mockito.any(), Mockito.any());
		
	}
	
	/**
	* Tests retrieval of candidate suggestions
	* @throws Exception
	*/
	@Test
	public void testGetCandidate_suggestions() throws Exception{
		
		Page<CandidateSearchAccuracyWrapper> candidatePage = Page.empty();
		
		Mockito.when(this.mockCandidateService.getCandidateSuggestions(Mockito.any(), Mockito.any())).thenReturn(candidatePage);
		
		PageRequest mockPageRequest = Mockito.mock(PageRequest.class);
		Mockito.when(mockPageRequest.getPageSize()).thenReturn(1);
		
		this.controller.getCandidate(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,true,mockPageRequest);
		
		Mockito.verify(this.mockCandidateService).getCandidateSuggestions(Mockito.any(), Mockito.any());
	
	}
	
	/**
	* Happy path test for updating Candidate
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidate() throws Exception {
		
		final String					candidateId	 = "100";
		final CANDIDATE_UPDATE_ACTIONS	updateAction = CANDIDATE_UPDATE_ACTIONS.disable;
		
		Mockito.doNothing().when(this.mockCandidateService).updateCandidate(candidateId, updateAction);
		
		ResponseEntity<Void> response = controller.updateCandidate("{}","100", CANDIDATE_UPDATE_ACTIONS.disable);
		
		Mockito.verify(this.mockCandidateService).updateCandidate(candidateId, updateAction);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Happy path test for UpdateCandidateflaggedAsUnavailable
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidateflaggedAsUnavailable() throws Exception {
		
		final long		candidateId	 			= 100L;
		final boolean 	flaggedAsUnavailable	= true;
		
		Mockito.doNothing().when(this.mockCandidateService).flagCandidateAvailability(candidateId, flaggedAsUnavailable);
		
		ResponseEntity<Void> response = controller.updateCandidateflaggedAsUnavailable("{}",candidateId, flaggedAsUnavailable);
		
		Mockito.verify(this.mockCandidateService).flagCandidateAvailability(candidateId, flaggedAsUnavailable);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests successful addition of a pending candidate 
	* @throws Exception
	*/
	@Test
	public void testAddPendingCandidate() throws Exception {
		
		Mockito.doNothing().when(mockCandidateService).persistPendingCandidate(Mockito.any(PendingCandidate.class));
		
		PendingCandidateAPIInbound pendingCandidate = PendingCandidateAPIInbound.builder().build();
		
		this.controller.addPendingCandidate(pendingCandidate);
		
		Mockito.verify(mockCandidateService).persistPendingCandidate(Mockito.any(PendingCandidate.class));
	}

	/**
	* Tests retrieval of PendingCandidates
	* @throws Exception
	*/
	@Test
	public void testGetAllPendingCandidates() throws Exception {
		
		final String c1Id = "123e4567-e89b-12d3-a456-426614174000";
		
		Set<PendingCandidate> pendingCandidates = new HashSet<>();
		
		PendingCandidate c1 = PendingCandidate.builder().pendingCandidateId(UUID.fromString(c1Id)).build();
		
		pendingCandidates.add(c1);
		
		Mockito.when(mockCandidateService.getPendingCandidates()).thenReturn(pendingCandidates);
		
		ResponseEntity<Set<PendingCandidateAPIOutbound>> response = controller.getAllPendingCandidates();
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		
		response.getBody().stream().filter(pc -> pc.getPendingCandidateId().toString().equals(c1Id)).findAny().orElseThrow();
		
	}
	
}