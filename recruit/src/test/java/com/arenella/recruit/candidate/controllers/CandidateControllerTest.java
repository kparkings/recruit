package com.arenella.recruit.candidate.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.controllers.CandidateController;
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
		
		controller.getCandidate(null, null, null, null, null, null, null, null, null, null, null, null, null, firstname, surname, email, null);
		
		filterCaptor.getValue().getFirstname().orElseThrow();
		filterCaptor.getValue().getSurname().orElseThrow();
		filterCaptor.getValue().getEmail().orElseThrow();
		
	}
	
	/**
	* Tests non Admin users cant filter on reserver attributes
	* @throws Exception
	*/
	@Test
	public void testGetCandidate_non_Admin_reservedAttributes() throws Exception {
		
		final String firstname 		= "kevin";
		final String surname 		= "parkings";
		final String email 			= "kparkings@gmail.com";
		
		ArgumentCaptor<CandidateFilterOptions> filterCaptor = ArgumentCaptor.forClass(CandidateFilterOptions.class);
		
		List<GrantedAuthority> roles = List.of("ROLE_RECRUITER").stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
		
		UsernamePasswordAuthenticationToken  authToken = new UsernamePasswordAuthenticationToken("username", "", roles);
		
		SecurityContextHolder.getContext().setAuthentication(authToken);
		
		@SuppressWarnings("unchecked")
		Page<Candidate> mockPage = Mockito.mock(Page.class);
		
		Mockito.when(mockCandidateService.getCandidates(filterCaptor.capture(), Mockito.any())).thenReturn(mockPage);
		
		controller.getCandidate(null, null, null, null, null, null, null, null, null, null, null, null, null, firstname, surname, email, null);
		
		assertTrue(filterCaptor.getValue().getFirstname().isEmpty());
		assertTrue(filterCaptor.getValue().getSurname().isEmpty());
		assertTrue(filterCaptor.getValue().getEmail().isEmpty());
		
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
	
}