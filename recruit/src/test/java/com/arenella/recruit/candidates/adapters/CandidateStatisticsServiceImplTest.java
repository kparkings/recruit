package com.arenella.recruit.candidates.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.arenella.recruit.candidates.beans.CandidateProfileViewedEvent;
import com.arenella.recruit.candidates.controllers.CandidateStatisticsController.STAT_PERIOD;
import com.arenella.recruit.candidates.dao.CandidateProfileViewedEventEntityDao;
import com.arenella.recruit.candidates.dao.CandidateSearchStatisticsDao;
import com.arenella.recruit.candidates.dao.NewCandidateStatsTypeDao;
import com.arenella.recruit.candidates.entities.CandidateSearchEventEntity;
import com.arenella.recruit.candidates.repos.CandidateRepository;
import com.arenella.recruit.candidates.services.CandidateStatisticsServiceImpl;
import com.arenella.recruit.candidates.utils.ArenellaSecurityUtil;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.services.CandidateStatisticsService.NEW_STATS_TYPE;

import static org.mockito.Mockito.*;

/**
* Unit tests for the CandidateStatisticsServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class CandidateStatisticsServiceImplTest {

	@Mock
	private CandidateRepository 					mockCandidateRepo;
	
	@Mock
	private SecurityContext 						mockSecurityContext;
	
	@Mock
	private	Authentication							mockAuthentication;
	
	@Mock
	private NewCandidateStatsTypeDao				mockNewCandidateStatsTypeDao;
	
	@Mock
	private CandidateSearchStatisticsDao 			mockStatisticsDao;
	
	@Mock
	private CandidateProfileViewedEventEntityDao 	mockCandidateProfileViewedEventEntityDao;
	
	@Mock
	private ArenellaSecurityUtil					mockSecUtil;
	
	@InjectMocks
	private CandidateStatisticsServiceImpl 	service	= new CandidateStatisticsServiceImpl();
	
	/**
	* Sets up test environment 
	*/
	@BeforeEach
	void init() {
		SecurityContextHolder.setContext(mockSecurityContext);
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication);
		
	}
	
	/**
	* Tests retrieval of available Candidate count
	* @throws Exception
	*/
	@Test
	void testFetchNumberOfAvailableCandidates() {
		
		final Long availableCandidates = 787L;
		
		Mockito.when(mockCandidateRepo.getCountByAvailable(true)).thenReturn(availableCandidates);
		
		assertEquals(availableCandidates, service.fetchNumberOfAvailableCandidates());
		
	}
	
	/**
	* Tests retrieval of new Candidates
	* @throws Exception
	*/
	@Test
	void testFetchNewCandidates() {
		
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
	void testGetLastRunDateNewCandidateStats() {
		
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
	void fetchSearchStatsForRecruiter() {
		
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

	/**
	* Tests retrieval of events
	* @throws IllegalAccessException 
	*/
	@Test
	void testFetchCandidateProfileViewedEventForCandidate_happyPath() throws IllegalAccessException {
		
		final String candidatedId = "100";
		
		when(this.mockCandidateProfileViewedEventEntityDao.fetchCandidateProfileViewedEventsByCandidateId(candidatedId))
		.thenReturn(Set.of(CandidateProfileViewedEvent.builder().build(), CandidateProfileViewedEvent.builder().build()));
		
		when(this.mockSecUtil.isRecruiter()).thenReturn(false);
		when(this.mockSecUtil.isCandidate()).thenReturn(false);
		
		assertEquals(2, this.service.fetchCandidateProfileViewedEventForCandidate(candidatedId).size());
		
	}
	
	/**
	* Tests if user is recruiter they do not have access
	* @throws IllegalAccessException 
	*/
	@Test
	void testFetchCandidateProfileViewedEventForCandidate_isRecruiter() {
		
		final String candidatedId = "100";
		
		when(this.mockSecUtil.isRecruiter()).thenReturn(true);
		
		IllegalAccessException ex = assertThrows(IllegalAccessException.class, () -> {
			this.service.fetchCandidateProfileViewedEventForCandidate(candidatedId);
		});
		
		assertEquals(CandidateStatisticsServiceImpl.ERR_MSG_NOT_AVAILABLE_RECRUITERS, ex.getMessage());
		
	}

	/**
	* Tests if user is candidate tries to view another Candidates information they do not have access
	* @throws IllegalAccessException 
	*/
	@Test
	void testFetchCandidateProfileViewedEventForCandidate_isADifferentCandidate() {
		
		final String candidatedId = "100";
		
		when(this.mockSecUtil.isRecruiter()).thenReturn(false);
		when(this.mockSecUtil.isCandidate()).thenReturn(true);
		when(this.mockSecUtil.getUserId()).thenReturn("SomeOtherCandidateId");
		
		IllegalAccessException ex = assertThrows(IllegalAccessException.class, () -> {
			this.service.fetchCandidateProfileViewedEventForCandidate(candidatedId);
		});
		
		assertEquals(CandidateStatisticsServiceImpl.ERR_MSG_NOT_AVAILABLE_OTHER_CANDIDATE, ex.getMessage());
		
	}
	
	/**
	* Tests retrieval of events
	* @throws IllegalAccessException 
	*/
	@Test
	void testFetchCandidateProfileViewedEventForRecruiter_isCandidate() {
		
		final String recruiterId = "rec1";
			
		when(this.mockSecUtil.isCandidate()).thenReturn(true);
		
		IllegalAccessException ex = assertThrows(IllegalAccessException.class, () -> {
			this.service.fetchCandidateProfileViewedEventForRecruiter(recruiterId);
		});
		
		assertEquals(CandidateStatisticsServiceImpl.ERR_MSG_NOT_AVAILABLE_CANDIDATES, ex.getMessage());
		
	}
	
	/**
	* Tests retrieval of events
	* @throws IllegalAccessException 
	*/
	@Test
	void testFetchCandidateProfileViewedEventForRecruiter_isAnotherRecruiter() {
		
		final String recruiterId = "rec1";
			
		when(this.mockSecUtil.isRecruiter()).thenReturn(true);
		when(this.mockSecUtil.getUserId()).thenReturn("SomeOtherRecruiterId");
		
		IllegalAccessException ex = assertThrows(IllegalAccessException.class, () -> {
			this.service.fetchCandidateProfileViewedEventForRecruiter(recruiterId);
		});
		
		assertEquals(CandidateStatisticsServiceImpl.ERR_MSG_NOT_AVAILABLE_OTHER_RECRUITERS, ex.getMessage());
		
	}
	
	/**
	* Tests retrieval of events
	* @throws IllegalAccessException 
	*/
	@Test
	void testFetchCandidateProfileViewedEventForRecruiter() throws IllegalAccessException {
		
		final String recruiterId = "rec1";
		
		when(this.mockCandidateProfileViewedEventEntityDao.fetchCandidateProfileViewedEventsByRecruiterId(recruiterId))
		.thenReturn(Set.of(CandidateProfileViewedEvent.builder().build(), CandidateProfileViewedEvent.builder().build()));
		
		when(this.mockSecUtil.isRecruiter()).thenReturn(false);
		when(this.mockSecUtil.isCandidate()).thenReturn(false);
		
		assertEquals(2, this.service.fetchCandidateProfileViewedEventForRecruiter(recruiterId).size());
		
	}

	/**
	* Tests retrieval of events
	*/
	@Test
	void testFetchCandidateProfileViewedEventNewerThat() {
		
		final LocalDateTime since = LocalDateTime.of(2025, 5, 2, 14, 14);
		
		when(this.mockCandidateProfileViewedEventEntityDao.fetchCandidateProfileViewedEvents(since))
		.thenReturn(Set.of(CandidateProfileViewedEvent.builder().build(), CandidateProfileViewedEvent.builder().build()));
		
		assertEquals(2, this.service.fetchCandidateProfileViewedEventNewerThat(since).size());
		
	}
	
	/**
	* In the case a Candidate exists, tests that the event was 
	* logged
	*/
	@Test
	void testRegisterCandidateProfileView_knownCandidate() {
		
		when(this.mockCandidateRepo.existsById(anyLong())).thenReturn(true);
		
		this.service.registerCandidateProfileView("1", "recruiterId");
		
		verify(this.mockCandidateProfileViewedEventEntityDao, times(1)).save(any(CandidateProfileViewedEvent.class));
		
	}
	
	/**
	* In the case a Candidate does not exists, tests that the event was 
	* not logged
	*/
	@Test
	void testRegisterCandidateProfileView_unknownCandidate() {
		
		when(this.mockCandidateRepo.existsById(anyLong())).thenReturn(false);
		
		this.service.registerCandidateProfileView("1", "recruiterId");
		
		verify(this.mockCandidateProfileViewedEventEntityDao, never()).save(any(CandidateProfileViewedEvent.class));
		
	}
 	
}