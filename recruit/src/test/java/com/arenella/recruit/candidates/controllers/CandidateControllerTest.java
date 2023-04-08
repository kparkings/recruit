package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.CandidateSearchAlert;
import com.arenella.recruit.candidates.beans.CandidateUpdateRequest;
import com.arenella.recruit.candidates.beans.PendingCandidate;
import com.arenella.recruit.candidates.controllers.CandidateController.CANDIDATE_UPDATE_ACTIONS;

import com.arenella.recruit.candidates.services.CandidateService;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Unit tests for the CandidateController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CandidateControllerTest {
	
	@Mock
	private CandidateService					mockCandidateService;
	
	@Mock
	private Authentication 						mockAuthentication;
	
	@Mock
	private MultipartFile						mockMultipartFile;
	
	@Mock
	private Principal							mockPrincipal;
	
	@Mock
	private UsernamePasswordAuthenticationToken mockUsernamePasswordAuthenticationToken;
	
	@InjectMocks
	private CandidateController 				controller;
	
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
	* Tests retrieval of candidate suggestions
	* @throws Exception
	*/
	@Test
	public void testGetCandidate_suggestions() throws Exception{
		
		Page<CandidateSearchAccuracyWrapper> candidatePage = Page.empty();
		
		Mockito.when(this.mockCandidateService.getCandidateSuggestions(Mockito.any(), Mockito.any())).thenReturn(candidatePage);
		
		PageRequest mockPageRequest = Mockito.mock(PageRequest.class);
		Mockito.when(mockPageRequest.getPageSize()).thenReturn(1);
		
		this.controller.getCandidate(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,true,null,null, mockPageRequest);
		
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
	* Happy path test for updateCandidatesLastAvailabilityCheck
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidatesLastAvailabilityCheck() throws Exception {
		
		final long		candidateId	 			= 100L;
		
		Mockito.doNothing().when(this.mockCandidateService).updateCandidatesLastAvailabilityCheck(candidateId);
		
		ResponseEntity<Void> response = controller.updateCandidatesLastAvailabilityCheck("{}",candidateId);
		
		Mockito.verify(this.mockCandidateService).updateCandidatesLastAvailabilityCheck(candidateId);
		
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
	
	/**
	* Tests fetch of Alerts for Recruiter
	* @throws Exception
	*/
	@Test
	public void testGetRecruiterAlerts() throws Exception{
		
		Mockito.when(this.mockCandidateService.getAlertsForCurrentUser()).thenReturn(Set.of(
			CandidateSearchAlert.builder().build(),
			CandidateSearchAlert.builder().build(),
			CandidateSearchAlert.builder().build()
		));
		
		ResponseEntity<Set<CandidateSearchAlertAPIOutbound>> response = this.controller.getRecruiterAlerts();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(3, response.getBody().size());
		
	}
	
	/**
	* Test deletion of SearchAlert
	* @throws Exception
	*/
	@Test
	public void testDeleteSearchAlert() throws Exception{
		
		ResponseEntity<Void> response = this.controller.deleteSearchAlert(UUID.randomUUID());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Test extraction of search filters end point
	* @throws Exception
	*/
	@Test
	public void testExtractSearchFiltersFromDocument() throws Exception{
		
		Mockito.when(this.mockMultipartFile.getOriginalFilename()).thenReturn("jobSpec.doc");
		Mockito.when(this.mockMultipartFile.getBytes()).thenReturn(new byte[2]);
		Mockito.when(this.mockCandidateService.extractFiltersFromDocument(FileType.doc, this.mockMultipartFile.getBytes())).thenReturn(CandidateExtractedFilters.builder().build());
		
		ResponseEntity<CandidateExtractedFilters> response = this.controller.extractSearchFiltersFromDocument(mockMultipartFile);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		assertTrue(response.getBody() instanceof CandidateExtractedFilters);
		
	}
	
	/**
	* Tests request for saved candidates are returned
	* @throws Exception
	*/
	@Test
	public void testFetchSavedCandidates() throws Exception{
		
	}
	
	/**
	* Tests sending of request to remove saved candidate
	* @throws Exception
	*/
	@Test
	public void testRemoveSavedCandidates() throws Exception{
		
		ResponseEntity<Void> response = this.controller.removeSavedCandidate(123, mockPrincipal);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
	
	}
	
	/**
	* Tests request to update existing saved candidate
	* @throws Exception
	*/
	@Test
	public void testUpdateSavedCandidates() throws Exception{
	
		ResponseEntity<Void> response = this.controller.updateSavedCandidate(SavedCandidateAPIInbound.builder().build(), this.mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests request to add a saved candidate for a User
	* @throws Exception
	*/
	@Test
	public void testAddSavedCandidates() throws Exception{
		
		ResponseEntity<Void> response = this.controller.addSavedCandidate(SavedCandidateAPIInbound.builder().build(), mockPrincipal);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		
	}
	
	/**
	* Tests request to fetch a specific User
	* @throws Exception
	*/
	@Test
	public void testFetchCandidate() throws Exception{
		
		final String 	candidateId = "1234";
		final Candidate candidate = Candidate.builder().build();
		
		Mockito.when(this.mockCandidateService.fetchCandidate(Mockito.anyString(), Mockito.anyString(), Mockito.anyCollection())).thenReturn(candidate);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(Set.of());
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn(candidateId);
		
		ResponseEntity<CandidateFullProfileAPIOutbound> response = this.controller.fetchCandidate(candidateId, mockUsernamePasswordAuthenticationToken);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody() instanceof CandidateFullProfileAPIOutbound);
		
	}
	
	/**
	* Test request to update Candidates profile
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidateProfile() throws Exception{
	
		final String candidateId = "1234";
		
		MultipartFile mockPhoto = Mockito.mock(MultipartFile.class);
		
		ArgumentCaptor<CandidateUpdateRequest> candidateArgCapt = ArgumentCaptor.forClass(CandidateUpdateRequest.class);
		
		CandidateUpdateRequestAPIInbound request = CandidateUpdateRequestAPIInbound.builder().build();
		
		Mockito.doNothing().when(this.mockCandidateService).updateCandidateProfile(candidateArgCapt.capture());
		
		ResponseEntity<Void> response = this.controller.updateCandidateProfile(request, Optional.of(mockPhoto), candidateId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		assertEquals(candidateId, candidateArgCapt.getValue().getCandidateId());
		
	}
	
}