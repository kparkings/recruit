package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.authentication.spring.filters.ClaimsUsernamePasswordAuthenticationToken;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.CandidateSearchAlert;
import com.arenella.recruit.candidates.beans.CandidateSkill;
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
	private CandidateService							mockCandidateService;
	
	@Mock
	private Authentication 								mockAuthentication;
	
	@Mock
	private MultipartFile								mockMultipartFile;
	
	@Mock
	private Principal									mockPrincipal;
	
	@Mock
	private ClaimsUsernamePasswordAuthenticationToken 	mockUsernamePasswordAuthenticationToken;
	
	@InjectMocks
	private CandidateController 						controller;
	
	/**
	* Tests retrieval of candidate suggestions
	* @throws Exception
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testGetCandidate_suggestions() throws Exception{
	
		final String email 		= "kparkings@gmail.com";
		final String firstname 	= "kevin";
		final String surname 	= "parkings";
		
		Candidate candidate = Candidate.builder().firstname(firstname).surname(surname).email(email).build();
		CandidateSearchAccuracyWrapper wrapper = new CandidateSearchAccuracyWrapper(candidate);
		
		org.springframework.data.domain.PageImpl<CandidateSearchAccuracyWrapper> page = new org.springframework.data.domain.PageImpl(List.of(wrapper));
		Page<CandidateSearchAccuracyWrapper> candidatePage = page;
		
		Mockito.when(this.mockCandidateService.getCandidateSuggestions(Mockito.any(), Mockito.any())).thenReturn(candidatePage);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(Set.of());
		
		PageRequest mockPageRequest = Mockito.mock(PageRequest.class);
		Mockito.when(mockPageRequest.getPageSize()).thenReturn(1);
		
		Page<CandidateAPIOutbound> results = this.controller.getCandidate(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,true,null,null,null,null, mockPageRequest, this.mockUsernamePasswordAuthenticationToken);
		
		Mockito.verify(this.mockCandidateService).getCandidateSuggestions(Mockito.any(), Mockito.any());
	
		CandidateSuggestionAPIOutbound candidateFirst = (CandidateSuggestionAPIOutbound) results.get().findFirst().get();
	
		assertNotEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, candidateFirst.getFirstname());
		assertNotEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, candidateFirst.getSurname());
		assertNotEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, candidateFirst.getEmail());
		
	}
	
	/**
	* Tests retrieval of candidate suggestions when User has no credits
	* @throws Exception
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testGetCandidate_suggestions_noCredits() throws Exception{
	
		final String email 		= "kparkings@gmail.com";
		final String firstname 	= "kevin";
		final String surname 	= "parkings";
		
		Candidate candidate = Candidate.builder().firstname(firstname).surname(surname).email(email).ownerId("c1").build();
		CandidateSearchAccuracyWrapper wrapper = new CandidateSearchAccuracyWrapper(candidate);
		
		org.springframework.data.domain.PageImpl<CandidateSearchAccuracyWrapper> page = new org.springframework.data.domain.PageImpl(List.of(wrapper));
		Page<CandidateSearchAccuracyWrapper> candidatePage = page;
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
		
		Mockito.when(this.mockCandidateService.getCandidateSuggestions(Mockito.any(), Mockito.any())).thenReturn(candidatePage);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn(firstname);
		
		PageRequest mockPageRequest = Mockito.mock(PageRequest.class);
		Mockito.when(mockPageRequest.getPageSize()).thenReturn(1);
		
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getClaim("useCredits")).thenReturn(Optional.of(Boolean.TRUE));
		Mockito.when(this.mockCandidateService.hasCreditsLeft(Mockito.anyString())).thenReturn(false);
		
		Page<CandidateAPIOutbound> results = this.controller.getCandidate(null,null, null,null,null,null,null,null,null,null,null,null,null,null,null,null,true,null,null,null,null, mockPageRequest, this.mockUsernamePasswordAuthenticationToken);
		
		Mockito.verify(this.mockCandidateService).getCandidateSuggestions(Mockito.any(), Mockito.any());
	
		CandidateSuggestionAPIOutbound candidateFirst = (CandidateSuggestionAPIOutbound) results.get().findFirst().get();
	
		assertEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, candidateFirst.getFirstname());
		assertEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, candidateFirst.getSurname());
		assertEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, candidateFirst.getEmail());
		
	}
	
	/**
	* Tests retrieval of candidate suggestions when user is Candidate. In which case a filter on CandidateId of just the 
	* logged in user should be in the filters
	* @throws Exception
	*/
	@SuppressWarnings("unchecked")
	@Test
	public void testGetCandidate_suggestions_candidate() throws Exception{
		
		final String candidateId = "9876";
		
		ArgumentCaptor<CandidateFilterOptions> filterArgcapt = ArgumentCaptor.forClass(CandidateFilterOptions.class);
		Page<CandidateSearchAccuracyWrapper> candidatePage = Page.empty();
		
		@SuppressWarnings("rawtypes")
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));
		
		Mockito.when(this.mockCandidateService.getCandidateSuggestions(filterArgcapt.capture(), Mockito.any())).thenReturn(candidatePage);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn(candidateId);
		
		PageRequest mockPageRequest = Mockito.mock(PageRequest.class);
		Mockito.when(mockPageRequest.getPageSize()).thenReturn(1);
		
		Set<String> ids = new HashSet<>();
		ids.add("notOwncandidateId");
		this.controller.getCandidate(null,null,ids,null,null,null,null,null,null,null,null,null,null,null,null,null,true,null,null,null,null, mockPageRequest, this.mockUsernamePasswordAuthenticationToken);
		
		Mockito.verify(this.mockCandidateService).getCandidateSuggestions(Mockito.any(), Mockito.any());
		assertEquals(1, filterArgcapt.getValue().getCandidateIds().size());
		assertEquals(candidateId, filterArgcapt.getValue().getCandidateIds().stream().findFirst().get());
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
		
		this.controller.addPendingCandidate(pendingCandidate, Optional.empty());
		
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
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
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
		final String	firstName	= "fred";
		final Candidate candidate 	= Candidate.builder().firstname(firstName).build();
		
		Mockito.when(this.mockCandidateService.fetchCandidate(Mockito.anyString(), Mockito.anyString(), Mockito.anyCollection())).thenReturn(candidate);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(Set.of());
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn(candidateId);
		
		ResponseEntity<CandidateFullProfileAPIOutbound> response = this.controller.fetchCandidate(candidateId, mockUsernamePasswordAuthenticationToken);
		
		assertEquals(firstName, response.getBody().getFirstname());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody() instanceof CandidateFullProfileAPIOutbound);
		
	}
	
	/**
	* Tests request to fetch a specific Candidate when the User has credit based access but 
	* no remaining credits. A censored version of the Candidate should be returned
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testFetchCandidate_no_credits() throws Exception{
		
		final String 	candidateId 	= "1234";
		final String 	userId	 		= "rec33";
		final Candidate candidate 		= Candidate.builder().firstname("").surname("").email("").build();
		
		Mockito.when(this.mockCandidateService.fetchCandidate(Mockito.anyString(), Mockito.anyString(), Mockito.anyCollection())).thenReturn(candidate);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(Set.of());
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn(candidateId);
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
		
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn(userId);
		
		Mockito.when(this.mockUsernamePasswordAuthenticationToken.getClaim("useCredits")).thenReturn(Optional.of(Boolean.TRUE));
		Mockito.when(this.mockCandidateService.hasCreditsLeft(Mockito.anyString())).thenReturn(false);
		
		ResponseEntity<CandidateFullProfileAPIOutbound> response = this.controller.fetchCandidate(candidateId, mockUsernamePasswordAuthenticationToken);
		
		assertEquals("-", response.getBody().getFirstname());
		
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
	
	/**
	* Tests handling of request to delete a Candidate
	* @throws Exception
	*/
	@Test
	public void testCandidate() throws Exception{
			
		final String candidateId = "1234";
		
		ResponseEntity<Void> response = this.controller.deleteCandidate(candidateId);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	/**
	* 
	* @throws Exception
	*/
	@Test
	public void testContactRecruiterForOpenPosition() throws Exception{
		
		final String candidateId 	= "123";
		final String title			= "aTitle";
		final String message		= "aMessage";
		
		ResponseEntity<Void> response = this.controller.contactRecruiterForOpenPosition(candidateId, title, message, mockPrincipal);
	
		Mockito.verify(this.mockCandidateService).sendEmailToCandidate(message, candidateId, title, mockPrincipal.getName());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	/**
	* Tests Fetch of remaining credit count for a User
	* @throws Exception
	*/
	@Test
	public void testFetchRemainingCreditCount() throws Exception{
	
		final int count = 6;
		
		Mockito.when(mockPrincipal.getName()).thenReturn("rec22");
		Mockito.when(this.mockCandidateService.getCreditCountForUser(Mockito.anyString())).thenReturn(count);
		
		ResponseEntity<Integer> response = this.controller.fetchRemainingCreditCount(mockPrincipal);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(count, response.getBody());
	
	}
	
	/**
	* Tests request to extract filters from a job specification in text for
	* @throws Exception
	*/
	@Test
	public void testExtractSearchFiltersFromText() throws Exception{
	
		Mockito.when(this.mockCandidateService.extractFiltersFromText(Mockito.anyString())).thenReturn(CandidateExtractedFilters.builder().build());
		
		ResponseEntity<CandidateExtractedFilters> response = this.controller.extractSearchFiltersFromText("some text");
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		
	}
	
	/**
	* Tests endpoint for fetching skills with validation pending
	* @throws Exception
	*/
	@Test
	public void testFetchValidationPendingCandidateSkills() throws Exception{
		
		Mockito.when(this.mockCandidateService.fetchPendingCandidateSkills()).thenReturn(Set.of(CandidateSkill.builder().skill("java").build()));
		
		ResponseEntity<Set<CandidateSkillAPIOutbound>> response = this.controller.fetchValidationPendingCandidateSkills();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		assertEquals("java",response.getBody().stream().findFirst().get().getSkill());
		
	}
	
	/**
	* Tests endpoint for updating existing skills validation statuses
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidateSkills() throws Exception {
		
		Set<CandidateSkillAPIInbound> skills = Set.of(CandidateSkillAPIInbound.builder().skill("pmo").build());
		
		ResponseEntity<Void> response = this.controller.updateCandidateSkills(skills);
		
		Mockito.verify(this.mockCandidateService).updateCandidateSkills(Mockito.anySet());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
}