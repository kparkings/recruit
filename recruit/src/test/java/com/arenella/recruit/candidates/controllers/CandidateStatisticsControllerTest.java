package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateProfileViewedEvent;
import com.arenella.recruit.candidates.beans.RecruiterStats;
import com.arenella.recruit.candidates.controllers.CandidateStatisticsController.STAT_PERIOD;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.services.CandidateStatisticsService;
import com.arenella.recruit.candidates.services.CandidateStatisticsService.NEW_STATS_TYPE;

/**
* Unit tests for the CandidateStatisticsController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class CandidateStatisticsControllerTest {

	@Mock
	private CandidateStatisticsService			mockCandidateStatisticsService;
	
	@Mock
	private Principal							mockPrincipal;
	
	@InjectMocks
	CandidateStatisticsController controller = new CandidateStatisticsController();
	
	/**
	* Tests return of total active users
	* @throws Exception
	*/
	@Test
	void testFetchNumberOfCandidates() {
		
		final Long numberOfAvailableCandidates = 88L;
		
		when(mockCandidateStatisticsService.fetchNumberOfAvailableCandidates()).thenReturn(numberOfAvailableCandidates);
		
		ResponseEntity<Long> response = controller.fetchNumberOfCandidates();
		
		assertEquals(HttpStatus.OK, 				response.getStatusCode());
		assertEquals(numberOfAvailableCandidates, 	response.getBody());
		
	}
	
	/**
	* Tests return of total active users
	* @throws Exception
	*/
	@Test
	void testFetchNewCandidates() {
		
		final String 		ROLE_SOUGHT 	= "Java Developer";
		final LocalDate		newSinceDate	= LocalDate.of(2022, 11, 5);
		final Candidate 	candidate 		= Candidate.builder().roleSought(ROLE_SOUGHT).build();
			
		when(mockCandidateStatisticsService.fetchNewCandidates(newSinceDate)).thenReturn(Set.of(candidate));
		when(mockCandidateStatisticsService.getLastRunDateNewCandidateStats(NEW_STATS_TYPE.NEW_CANDIDATES)).thenReturn(newSinceDate);
		
		ResponseEntity<NewCandidatesAPIOutbound> response = controller.fetchNewCandidates();
		
		assertEquals(HttpStatus.OK, 				response.getStatusCode());
		assertEquals(ROLE_SOUGHT, 	response.getBody().getCandidateSummary().stream().findFirst().get().getFunctionDesc());
		
	}
	
	/**
	* Tests return of total active users
	* @throws Exception
	*/
	@Test
	void testFetchNewCandidatesBreakdown() {
		
		final FUNCTION 		roleSought 		= FUNCTION.JAVA_DEV;
		final LocalDate		newSinceDate	= LocalDate.of(2022, 11, 5);
		final Candidate 	candidate 		= Candidate.builder().functions(Set.of(roleSought)).build();
			
		when(mockCandidateStatisticsService.fetchNewCandidates(newSinceDate)).thenReturn(Set.of(candidate));
		when(mockCandidateStatisticsService.getLastRunDateNewCandidateStats(NEW_STATS_TYPE.NEW_CANDIDATE_BREAKDOWN)).thenReturn(newSinceDate);
		
		ResponseEntity<NewCandidateSummaryAPIOutbound> response = controller.fetchNewCandidatesBreakdown();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(roleSought, 	response.getBody().getCandidatesSummary().stream().findFirst().get().getFunctionType());
		
		
	}
	
	/**
	* Tests stat's are returned
	* @throws Exception
	*/
	@Test
	void testFetchSearchStatsForRecruiter() {
		
		final String recruiterId = "recruiter1";
		
		when(this.mockCandidateStatisticsService.fetchSearchStatsForRecruiter(recruiterId, STAT_PERIOD.DAY)).thenReturn(new RecruiterStats(Set.of()));
		ResponseEntity<RecruiterStats> response = this.controller.fetchSearchStatsForRecruiter(recruiterId, STAT_PERIOD.DAY);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody() instanceof RecruiterStats);
	
	}
	
	/**
	* Tests end-point for retrieving search history statistics 
	*/
	@Test
	void testfetchSearchHistory() {
		
		when(this.mockCandidateStatisticsService.fetchCandidateSearchEvents(30)).thenReturn(Set.of());
		
		ResponseEntity<SearchStats> response = this.controller.fetchSearchHistory();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody() instanceof SearchStats);
		
	}
	
	/**
	* Test fetch of profiles views for Candidate
	* @throws IllegalAccessException 
	*/
	@Test
	void testFetchCandidateProfileViewedEventForCandidate() throws IllegalAccessException {
		
		LocalDateTime event1Viewed = LocalDateTime.of(2025, 5, 7, 10, 11, 12);
		LocalDateTime event2Viewed = LocalDateTime.of(2025, 5, 7, 10, 12, 12);
		LocalDateTime event3Viewed = LocalDateTime.of(2025, 1, 7, 10, 11, 12);
		
		CandidateProfileViewedEvent event1 = CandidateProfileViewedEvent.builder().viewed(event1Viewed).build();
		CandidateProfileViewedEvent event2 = CandidateProfileViewedEvent.builder().viewed(event2Viewed).build();
		CandidateProfileViewedEvent event3 = CandidateProfileViewedEvent.builder().viewed(event3Viewed).build();
		
		when(this.mockCandidateStatisticsService
				.fetchCandidateProfileViewedEventForCandidate(any())).thenReturn(Set.of(event1, event2, event3));
		
		ResponseEntity<Set<BucketAPIOutbound>> response = this.controller.fetchCandidateProfileViewedEventForCandidate("candidateId");
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());

		assertEquals(2, response.getBody().stream().filter(b -> b.bucketId().equals("2025 - 19")).findFirst().orElseThrow().count());
		assertEquals(1, response.getBody().stream().filter(b -> b.bucketId().equals("2025 - 2")).findFirst().orElseThrow().count());
		
	}

	/**
	* Test fetch of profiles views for Recruiter
	* @throws IllegalAccessException 
	*/
	@Test
	void testFetchCandidateProfileViewedEventForRecruiter() throws IllegalAccessException {
		
		LocalDateTime event1Viewed = LocalDateTime.of(2025, 5, 7, 10, 11, 12);
		LocalDateTime event2Viewed = LocalDateTime.of(2025, 5, 7, 10, 12, 12);
		LocalDateTime event3Viewed = LocalDateTime.of(2025, 1, 7, 10, 11, 12);
		
		CandidateProfileViewedEvent event1 = CandidateProfileViewedEvent.builder().viewed(event1Viewed).build();
		CandidateProfileViewedEvent event2 = CandidateProfileViewedEvent.builder().viewed(event2Viewed).build();
		CandidateProfileViewedEvent event3 = CandidateProfileViewedEvent.builder().viewed(event3Viewed).build();
		
		when(this.mockCandidateStatisticsService
				.fetchCandidateProfileViewedEventForRecruiter(any())).thenReturn(Set.of(event1, event2, event3));
		
		ResponseEntity<Set<BucketAPIOutbound>> response = this.controller.fetchCandidateProfileViewedEventForRecruiter("rec1");
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());

		assertEquals(2, response.getBody().stream().filter(b -> b.bucketId().equals("2025 - 19")).findFirst().orElseThrow().count());
		assertEquals(1, response.getBody().stream().filter(b -> b.bucketId().equals("2025 - 2")).findFirst().orElseThrow().count());
		
	}
	
	/**
	* Test fetch of profiles views in period
	*/
	@Test
	void testFetchCandidateProfileViewedEvents() {
		
		LocalDateTime event1Viewed = LocalDateTime.of(2025, 5, 7, 10, 11, 12);
		LocalDateTime event2Viewed = LocalDateTime.of(2025, 5, 7, 10, 12, 12);
		LocalDateTime event3Viewed = LocalDateTime.of(2025, 1, 7, 10, 11, 12);
		
		CandidateProfileViewedEvent event1 = CandidateProfileViewedEvent.builder().viewed(event1Viewed).build();
		CandidateProfileViewedEvent event2 = CandidateProfileViewedEvent.builder().viewed(event2Viewed).build();
		CandidateProfileViewedEvent event3 = CandidateProfileViewedEvent.builder().viewed(event3Viewed).build();
		
		when(this.mockCandidateStatisticsService
				.fetchCandidateProfileViewedEventNewerThat(any())).thenReturn(Set.of(event1, event2, event3));
		
		ResponseEntity<Set<BucketAPIOutbound>> response = this.controller.fetchCandidateProfileViewedEvents(2);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());

		assertEquals(2, response.getBody().stream().filter(b -> b.bucketId().equals("2025 - 19")).findFirst().orElseThrow().count());
		assertEquals(1, response.getBody().stream().filter(b -> b.bucketId().equals("2025 - 2")).findFirst().orElseThrow().count());
		
	}
	
	/**
	* Tests registering a profile view of a Candidate by a Recruiter
	*/
	@Test
	void testRegisterCandidateProfileView() {
		
		when(this.mockPrincipal.getName()).thenReturn("rec1");
		doNothing().when(this.mockCandidateStatisticsService).registerCandidateProfileView(anyString(), anyString());
		
		ResponseEntity<Void> response = this.controller.registerCandidateProfileView("candiadte1", mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
}