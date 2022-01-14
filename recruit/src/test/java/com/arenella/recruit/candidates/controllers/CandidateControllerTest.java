package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
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
	* Tests Admin users can filter on reserver attributes
	* @throws Exception
	*/
	@Test
	public void testGetCandidate_isAdmin_reservedAttributes() throws Exception {
		
		final String firstname 		= "kevin";
		final String surname 		= "parkings";
		final String email 			= "kparkings@gmail.com";
		
		ArgumentCaptor<CandidateFilterOptions> filterCaptor = ArgumentCaptor.forClass(CandidateFilterOptions.class);
		
		List<GrantedAuthority> roles = List.of("ROLE_ADMIN").stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
		
		UsernamePasswordAuthenticationToken  authToken = new UsernamePasswordAuthenticationToken("username", "", roles);
		
		SecurityContextHolder.getContext().setAuthentication(authToken);
		
		@SuppressWarnings("unchecked")
		Page<Candidate> mockPage = Mockito.mock(Page.class);
		
		Mockito.when(mockCandidateService.getCandidates(filterCaptor.capture(), Mockito.any())).thenReturn(mockPage);
		
		controller.getCandidate(null, null, null, null, null, null, null, null, null, null, null, null, null, firstname, surname, email, null, null);
		
		filterCaptor.getValue().getFirstname().orElseThrow();
		filterCaptor.getValue().getSurname().orElseThrow();
		filterCaptor.getValue().getEmail().orElseThrow();
		
	}
	
	/**
	* Tests all candidates are returned
	* @throws Exception
	*/
	@Test
	public void testGetCandidates() throws Exception{
		
	}
	
	/**
	* Tests the requested Candiate is returned
	* @throws Exception
	*/
	@Test
	public void testGetCandidate() throws Exception{
		
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