package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.candidates.beans.Candidate;
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
public class CandidateStatisticsControllerTest {

	@Mock
	private CandidateStatisticsService			mockCandidateStatisticsService;
	
	@InjectMocks
	CandidateStatisticsController controller = new CandidateStatisticsController();
	
	/**
	* Tests return of total active users
	* @throws Exception
	*/
	@Test
	public void testFetchNumberOfCandidates() throws Exception{
		
		final Long numberOfAvailableCandidates = 88L;
		
		Mockito.when(mockCandidateStatisticsService.fetchNumberOfAvailableCandidates()).thenReturn(numberOfAvailableCandidates);
		
		ResponseEntity<Long> response = controller.fetchNumberOfCandidates();
		
		assertEquals(HttpStatus.OK, 				response.getStatusCode());
		assertEquals(numberOfAvailableCandidates, 	response.getBody());
		
	}
	
	/**
	* Tests return of total active users
	* @throws Exception
	*/
	@Test
	public void testFetchNewCandidates() throws Exception{
		
		final String 		ROLE_SOUGHT 	= "Java Developer";
		final LocalDate		newSinceDate	= LocalDate.of(2022, 11, 5);
		final Candidate 	candidate 		= Candidate.builder().roleSought(ROLE_SOUGHT).build();
			
		Mockito.when(mockCandidateStatisticsService.fetchNewCandidates(newSinceDate)).thenReturn(Set.of(candidate));
		Mockito.when(mockCandidateStatisticsService.getLastRunDateNewCandidateStats(NEW_STATS_TYPE.NEW_CANDIDATES)).thenReturn(newSinceDate);
		
		ResponseEntity<NewCandidatesAPIOutbound> response = controller.fetchNewCandidates();
		
		assertEquals(HttpStatus.OK, 				response.getStatusCode());
		assertEquals(ROLE_SOUGHT, 	response.getBody().getCandidateSummary().stream().findFirst().get().getFunctionDesc());
		
	}
	
	/**
	* Tests return of total active users
	* @throws Exception
	*/
	@Test
	public void testFetchNewCandidatesBreakdown() throws Exception{
		
		final FUNCTION 		ROLE_SOUGHT 	= FUNCTION.JAVA_DEV;
		final LocalDate		newSinceDate	= LocalDate.of(2022, 11, 5);
		final Candidate 	candidate 		= Candidate.builder().function(ROLE_SOUGHT).build();
			
		Mockito.when(mockCandidateStatisticsService.fetchNewCandidates(newSinceDate)).thenReturn(Set.of(candidate));
		Mockito.when(mockCandidateStatisticsService.getLastRunDateNewCandidateStats(NEW_STATS_TYPE.NEW_CANDIDATE_BREAKDOWN)).thenReturn(newSinceDate);
		
		ResponseEntity<NewCandidateSummaryAPIOutbound> response = controller.fetchNewCandidatesBreakdown();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ROLE_SOUGHT, 	response.getBody().getCandidatesSummary().stream().findFirst().get().getFunctionType());
		
		
	}
	
	/**
	* Tests stat's are returned
	* @throws Exception
	*/
	@Test
	public void testFetchSearchStatsForRecruiter() throws Exception{
		
		final String recruiterId = "recruiter1";
		
		Mockito.when(this.mockCandidateStatisticsService.fetchSearchStatsForRecruiter(recruiterId, STAT_PERIOD.DAY)).thenReturn(new RecruiterStats(Set.of()));
		ResponseEntity<RecruiterStats> response = this.controller.fetchSearchStatsForRecruiter(recruiterId, STAT_PERIOD.DAY);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody() instanceof RecruiterStats);
	
	}
	
	/**
	* Tests endpoint for retrieving search history statistics 
	*/
	@Test
	public void testfetchSearchHistory() {
		
		Mockito.when(this.mockCandidateStatisticsService.fetchCandidateSearchEvents(30)).thenReturn(Set.of());
		
		ResponseEntity<SearchStats> response = this.controller.fetchSearchHistory();
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody() instanceof SearchStats);
		
	}
	
}