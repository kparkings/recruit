package com.arenella.recruit.candidates.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.dao.CandidateDao;
import com.arenella.recruit.candidates.dao.NewCandidateStatsTypeDao;
import com.arenella.recruit.candidates.services.CandidateStatisticsServiceImpl;
import com.arenella.recruit.candidates.services.CandidateStatisticsService.NEW_STATS_TYPE;

/**
* Unit tests for the CandidateStatisticsServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CandidateStatisticsServiceImplTest {

	@Mock
	private CandidateDao 						mockCandidateDao;
	
	@Mock
	private NewCandidateStatsTypeDao			mockNewCandidateStatsTypeDao;
	
	@InjectMocks
	private CandidateStatisticsServiceImpl 	service	= new CandidateStatisticsServiceImpl();
	
	/**
	* Tests retrieval of available Candidate count
	* @throws Exception
	*/
	@Test
	public void testFetchNumberOfAvailableCandidates() throws Exception {
		
		final Long availableCandidates = 787L;
		
		Mockito.when(mockCandidateDao.countByAvailable(true)).thenReturn(availableCandidates);
		
		assertEquals(availableCandidates, service.fetchNumberOfAvailableCandidates());
		
	}
	
	/**
	* Tests retrieval of new Candidates
	* @throws Exception
	*/
	@Test
	public void testFetchNewCandidates() throws Exception{
		
		final Candidate candidate = Candidate.builder().build();
		
		Mockito.when(this.mockCandidateDao.findNewSinceLastDate(Mockito.any(LocalDate.class))).thenReturn(Set.of(candidate));
		
		Set<Candidate> candidates =  this.service.fetchNewCandidates(LocalDate.of(2022, 11, 5));
		
		assertEquals(candidate, candidates.stream().findFirst().get());
		
	}
	
	/**
	* Tests retrieval of last date run for new Candidate stat type
	* @throws Exception
	*/
	@Test
	public void testGetLastRunDateNewCandidateStats() throws Exception {
		
		final LocalDate since = LocalDate.of(2022, 11, 5);
		
		Mockito.when(this.mockNewCandidateStatsTypeDao.fetchAndSetLastRequested(NEW_STATS_TYPE.NEW_CANDIDATE_BREAKDOWN)).thenReturn(since);
		
		LocalDate lastRunDate = this.service.getLastRunDateNewCandidateStats(NEW_STATS_TYPE.NEW_CANDIDATE_BREAKDOWN);
		
		assertEquals(since, lastRunDate);
		
	}
	
}