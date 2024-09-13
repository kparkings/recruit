package com.arenella.recruit.candidates.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.controllers.CandidateAPIOutbound;
import com.arenella.recruit.candidates.controllers.CandidateSuggestionAPIOutbound;
import com.arenella.recruit.candidates.services.CandidateService;

import java.util.List;

/**
* Unit tests for the CandidateSearchUtil class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CandidateSearchUtilTest {

	@InjectMocks 
	private CandidateSearchUtil 					util 						= new CandidateSearchUtil();
	
	@Mock
	private CandidateService 						mockCandidateService;
	
	@Mock
	private Pageable 								mockPageable;
	
	@Mock
	private Page<CandidateSearchAccuracyWrapper>	mockPage;
	
	/**
	* Tests all candidates have appropriate details masked
	* @throws Exception
	*/
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	@Test
	public void testRecriterWithExpiredCredits() throws Exception{
		
		final String 	firstName 	= "Keivn";
		final String 	surname 	= "parkings";
		final String 	email 		= "kparkings@gmail.com";
		final int 		pageSize 	= 1;
		
		CandidateFilterOptions filters = CandidateFilterOptions.builder().available(true).build();
		
		List<CandidateSearchAccuracyWrapper> pageItems = List.of(
				new CandidateSearchAccuracyWrapper(Candidate.builder().available(true).firstname(firstName).surname(surname).email(email).build()),
				new CandidateSearchAccuracyWrapper(Candidate.builder().available(false).firstname(firstName).surname(surname).email(email).build())
		);
		
		Page<CandidateSearchAccuracyWrapper> pageResults = new PageImpl(pageItems);
		
		ArgumentCaptor<CandidateFilterOptions> filtersArgCapt = ArgumentCaptor.forClass(CandidateFilterOptions.class);
		
		Mockito.when(this.mockPageable.getPageSize()).thenReturn(pageSize);
		Mockito.when(this.mockCandidateService.getCandidateSuggestions(filtersArgCapt.capture(), Mockito.eq(pageSize), Mockito.eq(false))).thenReturn(pageResults);
		
		Page<CandidateAPIOutbound> results = this.util.searchAndPackageForAPIOutput(true, true, true, filters, mockPageable, false);
		
		assertTrue(filtersArgCapt.getValue().isAvailable().isEmpty());
		
		CandidateSuggestionAPIOutbound available 	= (CandidateSuggestionAPIOutbound) results.get().filter(a -> ((CandidateSuggestionAPIOutbound) a).isAvailable()).findAny().get();
		CandidateSuggestionAPIOutbound unavailable 	= (CandidateSuggestionAPIOutbound) results.get().filter(a -> ((CandidateSuggestionAPIOutbound) a).isAvailable() == false).findAny().get();

		
		assertEquals(firstName, available.getFirstname());
		assertEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, 	available.getSurname());
		assertEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, 	available.getEmail());
		
		assertEquals(firstName, unavailable.getFirstname());
		assertEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, 	unavailable.getSurname());
		assertEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, 	unavailable.getEmail());
		
	}
	
	/**
	* Tests only candidates have that are not available have appropriate details masked 
	* but rest remain unmasked
	* @throws Exception
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testRecruiterWithRemainingCredits() throws Exception{
	
		final String firstName 	= "Keivn";
		final String surname 	= "parkings";
		final String email 		= "kparkings@gmail.com";
		
		final int pageSize = 1;
		CandidateFilterOptions filters = CandidateFilterOptions.builder().available(true).build();
		
		List<CandidateSearchAccuracyWrapper> pageItems = List.of(
				new CandidateSearchAccuracyWrapper(Candidate.builder().available(true).firstname(firstName).surname(surname).email(email).build()),
				new CandidateSearchAccuracyWrapper(Candidate.builder().available(false).firstname(firstName).surname(surname).email(email).build())
		);
		
		Page<CandidateSearchAccuracyWrapper> pageResults = new PageImpl(pageItems);
		
		ArgumentCaptor<CandidateFilterOptions> filtersArgCapt = ArgumentCaptor.forClass(CandidateFilterOptions.class);
		
		Mockito.when(this.mockPageable.getPageSize()).thenReturn(pageSize);
		Mockito.when(this.mockCandidateService.getCandidateSuggestions(filtersArgCapt.capture(), Mockito.eq(pageSize), Mockito.eq(false))).thenReturn(pageResults);
		
		Page<CandidateAPIOutbound> results = this.util.searchAndPackageForAPIOutput(true, true, false, filters, mockPageable, false);
		
		assertTrue(filtersArgCapt.getValue().isAvailable().isEmpty());
		
		CandidateSuggestionAPIOutbound available 	= (CandidateSuggestionAPIOutbound) results.get().filter(a -> ((CandidateSuggestionAPIOutbound) a).isAvailable() == true).findAny().get();
		CandidateSuggestionAPIOutbound unavailable 	= (CandidateSuggestionAPIOutbound) results.get().filter(a -> ((CandidateSuggestionAPIOutbound) a).isAvailable() == false).findAny().get();

		assertEquals(firstName, available.getFirstname());
		assertEquals(" ", 		available.getSurname());
		assertEquals(email, 	available.getEmail());
		
		assertEquals(firstName, unavailable.getFirstname());
		assertEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, unavailable.getSurname());
		assertEquals(CandidateSuggestionAPIOutbound.CENSORED_ITEM, unavailable.getEmail());
		
	}
	
	/**
	* Tests all Candidate's details remain unmasked 
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testNonCreditSubscriptionRecruiter() throws Exception{
	
		final String firstName 	= "Keivn";
		final String surname 	= "parkings";
		final String email 		= "kparkings@gmail.com";
		final int pageSize = 1;
		CandidateFilterOptions filters = CandidateFilterOptions.builder().available(true).build();
		
		List<CandidateSearchAccuracyWrapper> pageItems = List.of(
				new CandidateSearchAccuracyWrapper(Candidate.builder().available(true).firstname(firstName).surname(surname).email(email).build()),
				new CandidateSearchAccuracyWrapper(Candidate.builder().available(false).firstname(firstName).surname(surname).email(email).build())
		);
		
		Page<CandidateSearchAccuracyWrapper> pageResults = new PageImpl(pageItems);
		
		ArgumentCaptor<CandidateFilterOptions> filtersArgCapt = ArgumentCaptor.forClass(CandidateFilterOptions.class);
		
		Mockito.when(this.mockPageable.getPageSize()).thenReturn(pageSize);
		Mockito.when(this.mockCandidateService.getCandidateSuggestions(filtersArgCapt.capture(), Mockito.eq(1), Mockito.eq(false), Mockito.eq(false))).thenReturn(pageResults);
		
		Page<CandidateAPIOutbound> results =  this.util.searchAndPackageForAPIOutput(true, false, false, filters, mockPageable, false);
		
		assertTrue(filtersArgCapt.getValue().isAvailable().get());
		
		CandidateSuggestionAPIOutbound available 	= (CandidateSuggestionAPIOutbound) results.get().filter(a -> ((CandidateSuggestionAPIOutbound) a).isAvailable() == true).findAny().get();
		CandidateSuggestionAPIOutbound unavailable 	= (CandidateSuggestionAPIOutbound) results.get().filter(a -> ((CandidateSuggestionAPIOutbound) a).isAvailable() == false).findAny().get();

		assertEquals(firstName, available.getFirstname());
		assertEquals(surname, 	available.getSurname());
		assertEquals(email, 	available.getEmail());
		
		assertEquals(firstName, unavailable.getFirstname());
		assertEquals(surname, 	unavailable.getSurname());
		assertEquals(email, 	unavailable.getEmail());
		
	}
	
}