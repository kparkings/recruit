package com.arenella.recruit.candidates.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.dao.CandidateDao;
import com.arenella.recruit.candidates.dao.CandidateStatsEmailRequestedsDao;
import com.arenella.recruit.candidates.dao.NewCandidateStatsTypeDao;
import com.arenella.recruit.candidates.services.CandidateStatisticsServiceImpl;
import com.arenella.recruit.candidates.services.CandidateStatisticsService.NEW_STATS_TYPE;

/**
* Unit tests for the CandidateStatisticsServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CandidateStatisticsServiceImplTest {

	private static final SecurityContext	mockSecurityContext		= Mockito.mock(SecurityContext.class);
	private static final Authentication 	mockAuthentication 		= Mockito.mock(Authentication.class);
	
	@Mock
	private CandidateDao 						mockCandidateDao;
	
	@Mock
	private CandidateStatsEmailRequestedsDao 	emailStatsRequestDao;
	
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
	* Tests logging of event for email request
	* If Admin should not be logged
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testlogEventEmailRequested_admin() throws Exception {
		
		final Long candidateId = 111L;
		final String userId = "kparkings";
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getName()).thenReturn(userId);
		
		this.service.logEventEmailRequested(candidateId);

		Mockito.verify(this.emailStatsRequestDao, Mockito.never()).persistEmailRequestedEvent(Mockito.any(),Mockito.any(), Mockito.any(),Mockito.eq(candidateId));
		
		SecurityContextHolder.clearContext();
		
	}
	
	/**
	* Tests logging of event for email request
	* If Recruiter should be logged
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testlogEventEmailRequested_recruiter() throws Exception {
		
		final Long candidateId = 111L;
		final String userId = "kparkings";
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getName()).thenReturn(userId);
		
		this.service.logEventEmailRequested(candidateId);

		Mockito.verify(this.emailStatsRequestDao).persistEmailRequestedEvent(Mockito.any(),Mockito.any(), Mockito.eq(userId),Mockito.eq(candidateId));
		
		SecurityContextHolder.clearContext();
		
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