package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.authentication.spring.filters.ClaimsUsernamePasswordAuthenticationToken;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.CandidateSearchAlert;
import com.arenella.recruit.candidates.beans.CandidateSkill;
import com.arenella.recruit.candidates.beans.CandidateUpdateRequest;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.PendingCandidate;
import com.arenella.recruit.candidates.beans.SavedCandidateSearch;
import com.arenella.recruit.candidates.controllers.CandidateController.CANDIDATE_UPDATE_ACTIONS;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.services.CandidateService;
import com.arenella.recruit.candidates.utils.CandidateSearchUtil;
import com.arenella.recruit.candidates.utils.GeoZoneSearchUtil.GEO_ZONE;
import com.arenella.recruit.curriculum.enums.FileType;

/**
* Unit tests for the CandidateController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class CandidateControllerTest {
	
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
	
	@Mock
	private HttpServletResponse 						mockResponse;
	
	@Mock
	private CandidateSearchUtil							mockCandidateSearchUtil;
	
	@InjectMocks
	private CandidateController 						controller;
	
	@Mock
	private Pageable 									mockPageable;
	
	/**
	* Happy path test for updating Candidate
	* @throws Exception
	*/
	@Test
	void testUpdateCandidate() {
		
		final String					candidateId	 = "100";
		final CANDIDATE_UPDATE_ACTIONS	updateAction = CANDIDATE_UPDATE_ACTIONS.disable;
		
		doNothing().when(this.mockCandidateService).updateCandidate(candidateId, updateAction);
		
		ResponseEntity<Void> response = controller.updateCandidate("{}","100", CANDIDATE_UPDATE_ACTIONS.disable);
		
		verify(this.mockCandidateService).updateCandidate(candidateId, updateAction);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Happy path test for updateCandidatesLastAvailabilityCheck
	* @throws Exception
	*/
	@Test
	void testUpdateCandidatesLastAvailabilityCheck() {
		
		final long		candidateId	 			= 100L;
		
		doNothing().when(this.mockCandidateService).updateCandidatesLastAvailabilityCheck(candidateId);
		
		ResponseEntity<Void> response = controller.updateCandidatesLastAvailabilityCheck("{}",candidateId);
		
		verify(this.mockCandidateService).updateCandidatesLastAvailabilityCheck(candidateId);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests successful addition of a pending candidate 
	* @throws Exception
	*/
	@Test
	void testAddPendingCandidate() throws Exception {
		
		doNothing().when(mockCandidateService).persistPendingCandidate(any(PendingCandidate.class));
		
		PendingCandidateAPIInbound pendingCandidate = PendingCandidateAPIInbound.builder().build();
		
		this.controller.addPendingCandidate(pendingCandidate, Optional.empty());
		
		verify(mockCandidateService).persistPendingCandidate(any(PendingCandidate.class));
	}

	/**
	* Tests retrieval of PendingCandidates
	* @throws Exception
	*/
	@Test
	void testGetAllPendingCandidates() {
		
		final String c1Id = "123e4567-e89b-12d3-a456-426614174000";
		
		Set<PendingCandidate> pendingCandidates = new HashSet<>();
		
		PendingCandidate c1 = PendingCandidate.builder().pendingCandidateId(UUID.fromString(c1Id)).build();
		
		pendingCandidates.add(c1);
		
		when(mockCandidateService.getPendingCandidates()).thenReturn(pendingCandidates);
		
		ResponseEntity<Set<PendingCandidateAPIOutbound>> response = controller.getAllPendingCandidates();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		response.getBody().stream().filter(pc -> pc.getPendingCandidateId().toString().equals(c1Id)).findAny().orElseThrow();
		
	}
	
	/**
	* Tests fetch of Alerts for Recruiter
	* @throws Exception
	*/
	@Test
	void testGetRecruiterAlerts() {
		
		when(this.mockCandidateService.getAlertsForCurrentUser()).thenReturn(Set.of(
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
	void testDeleteSearchAlert() {
		
		ResponseEntity<Void> response = this.controller.deleteSearchAlert(UUID.randomUUID());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Test extraction of search filters end point
	* @throws Exception
	*/
	@Test
	void testExtractSearchFiltersFromDocument() throws Exception{
		
		when(this.mockMultipartFile.getOriginalFilename()).thenReturn("jobSpec.doc");
		when(this.mockMultipartFile.getBytes()).thenReturn(new byte[2]);
		when(this.mockCandidateService.extractFiltersFromDocument(FileType.doc, this.mockMultipartFile.getBytes())).thenReturn(CandidateExtractedFilters.builder().build());
		
		ResponseEntity<CandidateExtractedFilters> response = this.controller.extractSearchFiltersFromDocument(mockMultipartFile);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		assertTrue(response.getBody() instanceof CandidateExtractedFilters);
		
	}
	
	/**
	* Tests sending of request to remove saved candidate
	* @throws Exception
	*/
	@Test
	void testRemoveSavedCandidates() {
		
		ResponseEntity<Void> response = this.controller.removeSavedCandidate(123, mockPrincipal);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
	
	}
	
	/**
	* Tests request to update existing saved candidate
	* @throws Exception
	*/
	@Test
	void testUpdateSavedCandidates() {
	
		ResponseEntity<Void> response = this.controller.updateSavedCandidate(SavedCandidateAPIInbound.builder().build(), this.mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests request to add a saved candidate for a User
	* @throws Exception
	*/
	@Test
	void testAddSavedCandidates() {
		
		ResponseEntity<Void> response = this.controller.addSavedCandidate(SavedCandidateAPIInbound.builder().build(), mockPrincipal);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
	}
	
	/**
	* Tests request to fetch a specific User
	* @throws Exception
	*/
	@Test
	void testFetchCandidate() {
		
		final String 	candidateId = "1234";
		final String	firstName	= "fred";
		final Candidate candidate 	= Candidate.builder().firstname(firstName).build();
		
		when(this.mockCandidateService.fetchCandidate(anyString(), anyString(), anyCollection())).thenReturn(candidate);
		when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(Set.of());
		when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn(candidateId);
		
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
	void testFetchCandidate_no_credits() {
		
		final String 	candidateId 	= "1234";
		final String 	userId	 		= "rec33";
		final Candidate candidate 		= Candidate.builder().firstname("").surname("").email("").build();
		
		when(this.mockCandidateService.fetchCandidate(anyString(), anyString(), anyCollection())).thenReturn(candidate);
		when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(Set.of());
		when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn(candidateId);
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
		
		when(this.mockUsernamePasswordAuthenticationToken.getAuthorities()).thenReturn(authorities);
		when(this.mockUsernamePasswordAuthenticationToken.getName()).thenReturn(userId);
		
		when(this.mockUsernamePasswordAuthenticationToken.getClaim("useCredits")).thenReturn(Optional.of(Boolean.TRUE));
		when(this.mockCandidateService.hasCreditsLeft(anyString())).thenReturn(false);
		
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
	void testUpdateCandidateProfile() throws Exception{
	
		final String candidateId = "1234";
		
		MultipartFile mockPhoto = mock(MultipartFile.class);
		
		ArgumentCaptor<CandidateUpdateRequest> candidateArgCapt = ArgumentCaptor.forClass(CandidateUpdateRequest.class);
		
		CandidateUpdateRequestAPIInbound request = CandidateUpdateRequestAPIInbound.builder().build();
		
		doNothing().when(this.mockCandidateService).updateCandidateProfile(candidateArgCapt.capture());
		
		ResponseEntity<Void> response = this.controller.updateCandidateProfile(request, Optional.of(mockPhoto), candidateId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		assertEquals(candidateId, candidateArgCapt.getValue().getCandidateId());
		
	}
	
	/**
	* Tests handling of request to delete a Candidate
	* @throws Exception
	*/
	@Test
	void testCandidate() {
			
		final String candidateId = "1234";
		
		ResponseEntity<Void> response = this.controller.deleteCandidate(candidateId);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	/**
	* 
	* @throws Exception
	*/
	@Test
	void testContactRecruiterForOpenPosition() {
		
		final String candidateId 	= "123";
		final String title			= "aTitle";
		final String message		= "aMessage";
		
		ResponseEntity<Void> response = this.controller.contactRecruiterForOpenPosition(candidateId, title, message, mockPrincipal);
	
		verify(this.mockCandidateService).sendEmailToCandidate(message, candidateId, title, mockPrincipal.getName());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	/**
	* Tests Fetch of remaining credit count for a User
	* @throws Exception
	*/
	@Test
	void testFetchRemainingCreditCount() {
	
		final int count = 6;
		
		when(mockPrincipal.getName()).thenReturn("rec22");
		when(this.mockCandidateService.getCreditCountForUser(anyString())).thenReturn(count);
		
		ResponseEntity<Integer> response = this.controller.fetchRemainingCreditCount(mockPrincipal);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(count, response.getBody());
	
	}
	
	/**
	* Tests request to extract filters from a job specification in text for
	* @throws Exception
	*/
	@Test
	void testExtractSearchFiltersFromText() throws Exception{
	
		when(this.mockCandidateService.extractFiltersFromText(anyString())).thenReturn(CandidateExtractedFilters.builder().build());
		
		ResponseEntity<CandidateExtractedFilters> response = this.controller.extractSearchFiltersFromText("some text");
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		
	}
	
	/**
	* Tests endpoint for fetching skills with validation pending
	* @throws Exception
	*/
	@Test
	void testFetchValidationPendingCandidateSkills() {
		
		when(this.mockCandidateService.fetchPendingCandidateSkills()).thenReturn(Set.of(CandidateSkill.builder().skill("java").build()));
		
		ResponseEntity<Set<CandidateSkillAPIOutbound>> response = this.controller.fetchValidationPendingCandidateSkills();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		assertEquals("java",response.getBody().stream().findFirst().get().getSkill());
		
	}
	
	/**
	* Tests endpoint for updating existing skills validation statuses
	* @throws Exception
	*/
	@Test
	void testUpdateCandidateSkills() {
		
		Set<CandidateSkillAPIInbound> skills = Set.of(CandidateSkillAPIInbound.builder().skill("pmo").build());
		
		ResponseEntity<Void> response = this.controller.updateCandidateSkills(skills);
		
		verify(this.mockCandidateService).updateCandidateSkills(anySet());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests retrieval of Candidate counts information
	* @throws Exception
	*/
	@Test
	void testGetCandidateCounts() {
		
		final long available 	= 10;
		final long unavailable 	= 30;
		
		when(this.mockCandidateService.getCountByAvailable(true)).thenReturn(available);
		when(this.mockCandidateService.getCountByAvailable(false)).thenReturn(unavailable);
		
		ResponseEntity<CandidateAvailabilityCountAPIOutbound> response = this.controller.getCandidateCounts();
	
		assertEquals(HttpStatus.OK, 			response.getStatusCode());
		assertEquals(available, 				response.getBody().getAvailable());
		assertEquals(unavailable, 				response.getBody().getUnavailable());
		assertEquals(available + unavailable, 	response.getBody().getTotalRegisteredCandidates());
	}
	
	/**
	* Tests retrieval of SupportedLanguages
	* @throws Exception
	*/
	@Test
	void testfetchSupportedLanguages() {
		ResponseEntity<Set<LANGUAGE>> response = this.controller.fetchSupportedLanguages();
		assertEquals(HttpStatus.OK, 			response.getStatusCode());
		Arrays.stream(LANGUAGE.values()).forEach(l -> response.getBody().contains(l));
	}

	/**
	* Tests retrieval of GeoZones
	* @throws Exception
	*/
	@Test
	void testfetchGeoZones() {
		ResponseEntity<Set<GEO_ZONE>> response = this.controller.fetchGeoZones();
		assertEquals(HttpStatus.OK, 			response.getStatusCode());
		Arrays.stream(GEO_ZONE.values()).forEach(l -> response.getBody().contains(l));
	}

	/**
	* Tests retrieval of Supported Counties
	* @throws Exception
	*/
	@Test
	void testfetchSupportedCountries() {
		ResponseEntity<Set<CountryEnumAPIOutbound>> response = this.controller.fetchSupportedCountries();
		assertEquals(HttpStatus.OK, 			response.getStatusCode());
		Arrays.stream(COUNTRY.values()).forEach(l -> response.getBody().stream().filter(c -> c.getName() == l).findAny().orElseThrow());
	}
	
	/**
	* Tests email reset request
	* @throws Exception
	*/
	@Test
	void testResetPassword() {
		
		final String email = "kparkings";
		
		doNothing().when(this.mockCandidateService).resetPassword(anyString());
		
		ResponseEntity<Void> response = this.controller.resetPassword(email);
		
		verify(this.mockCandidateService).resetPassword(eq(email));
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests text is returned
	* @throws Exception
	*/
	@Test
	void testConfirmOwnAvailability() {
		
		final String candidateId 	= "1234";
		final UUID requestToken 	= UUID.randomUUID();
		final Boolean isAvailable 	= true;
				
		ResponseEntity<String> response = this.controller.confirmOwnAvailability(candidateId, requestToken, isAvailable);
		
		verify(this.mockCandidateService).performConfirmCandidateAvailability(candidateId, requestToken, isAvailable);
		
		assertEquals("Thanks you. Your'e availability has been updated in the system.", response.getBody());
		
	}
	
	/**
	* Tests Create
	*/
	@Test
	void testCreateSavedCandidateSearchRequest() {
		SavedCandidateSearchRequestCreateAPIInbound inbound = SavedCandidateSearchRequestCreateAPIInbound.builder().build();
		ResponseEntity<Void> response = this.controller.createSavedCandidateSearchRequest(inbound, mockPrincipal);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	
	/**
	* Tests Update
	*/
	@Test
	void testUpdateSavedCandidateSearchRequest() {
		
		final UUID id = UUID.randomUUID();
		
		SavedCandidateSearchRequestUpdateAPIInbound inbound = SavedCandidateSearchRequestUpdateAPIInbound.builder().build();
		ResponseEntity<Void> response = this.controller.updateSavedCandidateSearchRequest(id, inbound, mockPrincipal);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	/**
	* Tests Fetch
	*/
	@Test
	void testFetchSavedCandidateSearchRequest() {
		
		when(this.mockCandidateService.fetchSavedCandidateSearches(anyString())).thenReturn(Set.of(SavedCandidateSearch.builder().userId("rec1").build()));
		when(this.mockPrincipal.getName()).thenReturn("rec1");
		
		ResponseEntity<Set<SavedCandidateSearchRequestAPIOutbound>> response = this.controller.fetchSavedCandidateSearchRequest(mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, response.getBody().size());
		
	}
	
	/**
	* Tests Delete
	*/
	@Test
	void testdeleteSavedCandidateSearchRequest() {
		
		final UUID id = UUID.randomUUID();
		
		ResponseEntity<Void> response = this.controller.deleteSavedCandidateSearchRequest(id, mockPrincipal);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
}