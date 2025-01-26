package com.arenella.recruit.candidates.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.controllers.CandidateStatisticsController.STAT_PERIOD;
import com.arenella.recruit.candidates.dao.CandidateSearchStatisticsDao;
import com.arenella.recruit.candidates.dao.NewCandidateStatsTypeDao;
import com.arenella.recruit.candidates.entities.CandidateSearchEventEntity;
import com.arenella.recruit.candidates.repos.CandidateRepository;
import com.arenella.recruit.candidates.services.CandidateStatisticsServiceImpl;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.services.CandidateStatisticsService.NEW_STATS_TYPE;

/**
* Unit tests for the CandidateStatisticsServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CandidateStatisticsServiceImplTest {

	@Mock
	private CandidateRepository 				mockCandidateRepo;
	
	@Mock
	private SecurityContext 					mockSecurityContext;
	
	@Mock
	private	Authentication						mockAuthentication;
	
	@Mock
	private NewCandidateStatsTypeDao			mockNewCandidateStatsTypeDao;
	
	@Mock
	private CandidateSearchStatisticsDao 		mockStatisticsDao;
	
	@InjectMocks
	private CandidateStatisticsServiceImpl 	service	= new CandidateStatisticsServiceImpl();
	
	/**
	* Sets up test environment 
	*/
	@BeforeEach
	public void init() throws Exception{
		SecurityContextHolder.setContext(mockSecurityContext);
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
		
	}
	
	/**
	* Tests retrieval of available Candidate count
	* @throws Exception
	*/
	@Test
	public void testFetchNumberOfAvailableCandidates() throws Exception {
		
		final Long availableCandidates = 787L;
		
		Mockito.when(mockCandidateRepo.getCountByAvailable(true)).thenReturn(availableCandidates);
		
		assertEquals(availableCandidates, service.fetchNumberOfAvailableCandidates());
		
	}
	
	/**
	* Tests retrieval of new Candidates
	* @throws Exception
	*/
	@Test
	public void testFetchNewCandidates() throws Exception{
		
		final Candidate candidate = Candidate.builder().build();
		
		Mockito.when(this.mockCandidateRepo.findNewSinceLastDate(Mockito.any(LocalDate.class), Mockito.any())).thenReturn(Set.of(candidate));
		
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
	
	/**
	* Tests fetching of RecruiterStats
	* @throws Exception
	*/
	@Test
	public void fetchSearchStatsForRecruiter() throws Exception{
		
		final String recruiterId = "recruiter1";
		
		ArgumentCaptor<LocalDate> argCaptSince = ArgumentCaptor.forClass(LocalDate.class);
		
		Mockito.when(this.mockStatisticsDao.fetchEventForRecruiter(Mockito.anyString(), argCaptSince.capture())).thenReturn(Set.of());
		
		this.service.fetchSearchStatsForRecruiter(recruiterId, STAT_PERIOD.WEEK);
	
		assertEquals(LocalDate.now().minusWeeks(1), argCaptSince.getValue());
		
	}
	
	/**
	* Test call to retrieve Events 
	*/
	@Test
	void testFetchCandidateSearchEvents() {
		this.service.fetchCandidateSearchEvents(30);
		Mockito.verify(this.mockStatisticsDao).fetchEventsSince(Mockito.any(LocalDate.class));
	}
	
	/**
	* Tests correct events are logged. for a search request. This will be
	* 1 x event for each Country and Functions combination
	* or where there are no combinations
	* 1x event for each function
	* 1x event for each country
	* or where there are no functions of countrie
	* 1 x event
	*/
	@Test
	void testLogCandidateSearchEvent_noFilters() {
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Set<CandidateSearchEventEntity>> events = ArgumentCaptor.forClass(Set.class);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(this.mockStatisticsDao.saveAll(events.capture())).thenReturn(null);
		
		this.service.logCandidateSearchEvent(CandidateFilterOptions.builder().build());
		
		Mockito.verify(this.mockStatisticsDao).saveAll(Mockito.anySet());
		
		Set<CandidateSearchEventEntity> results = events.getValue(); 
		assertEquals(1, results.size());
		
	}
	
	/**
	* Refer to testLogCandidateSearchEvent_noFilters
	*/
	@Test
	void testLogCandidateSearchEvent_justCountryFilters() {
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Set<CandidateSearchEventEntity>> events = ArgumentCaptor.forClass(Set.class);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(this.mockStatisticsDao.saveAll(events.capture())).thenReturn(null);
		
		this.service.logCandidateSearchEvent(CandidateFilterOptions
				.builder()
				.countries(Set.of(COUNTRY.AUSTRIA, COUNTRY.BELGIUM))
				.build());
		
		Mockito.verify(this.mockStatisticsDao).saveAll(Mockito.anySet());
		
		Set<CandidateSearchEventEntity> results = events.getValue(); 
		assertEquals(2, results.size());
		
		assertTrue(results.stream().filter(e -> e.getCountry().get() == COUNTRY.BELGIUM).findAny().isPresent());
		assertTrue(results.stream().filter(e -> e.getCountry().get() == COUNTRY.BELGIUM).findAny().isPresent());
		
	}

	/**
	* Refer to testLogCandidateSearchEvent_noFilters
	*/
	@Test
	void testLogCandidateSearchEvent_justFunctionFilters() {
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Set<CandidateSearchEventEntity>> events = ArgumentCaptor.forClass(Set.class);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(this.mockStatisticsDao.saveAll(events.capture())).thenReturn(null);
		
		this.service.logCandidateSearchEvent(CandidateFilterOptions
				.builder()
				.functions(Set.of(FUNCTION.ARCHITECT, FUNCTION.BA))
				.build());
		
		Mockito.verify(this.mockStatisticsDao).saveAll(Mockito.anySet());
		
		Set<CandidateSearchEventEntity> results = events.getValue(); 
		assertEquals(2, results.size());
		
		assertTrue(results.stream().filter(e -> e.getFunction().get() == FUNCTION.ARCHITECT).findAny().isPresent());
		assertTrue(results.stream().filter(e -> e.getFunction().get() == FUNCTION.BA).findAny().isPresent());
		
	}
	
	/**
	* Refer to testLogCandidateSearchEvent_noFilters
	*/
	@Test
	void testLogCandidateSearchEvent_CountryAndFunctionFilters() {
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Set<CandidateSearchEventEntity>> events = ArgumentCaptor.forClass(Set.class);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(this.mockStatisticsDao.saveAll(events.capture())).thenReturn(null);
		
		this.service.logCandidateSearchEvent(CandidateFilterOptions
				.builder()
				.countries(Set.of(COUNTRY.AUSTRIA, COUNTRY.BELGIUM))
				.functions(Set.of(FUNCTION.ARCHITECT, FUNCTION.BA))
				.build());
		
		Mockito.verify(this.mockStatisticsDao).saveAll(Mockito.anySet());
		
		Set<CandidateSearchEventEntity> results = events.getValue(); 
		assertEquals(4, results.size());
		
		assertTrue(results.stream().filter(e -> e.getCountry().get() == COUNTRY.AUSTRIA && e.getFunction().get() == FUNCTION.ARCHITECT).findAny().isPresent());
		assertTrue(results.stream().filter(e -> e.getCountry().get() == COUNTRY.AUSTRIA && e.getFunction().get() == FUNCTION.BA).findAny().isPresent());
		assertTrue(results.stream().filter(e -> e.getCountry().get() == COUNTRY.BELGIUM && e.getFunction().get() == FUNCTION.ARCHITECT).findAny().isPresent());
		assertTrue(results.stream().filter(e -> e.getCountry().get() == COUNTRY.BELGIUM && e.getFunction().get() == FUNCTION.BA).findAny().isPresent());
		
		
	}
	
}