package com.arenella.recruit.candidates.adapters;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.CandidateSearchAlert;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.beans.PendingCandidate;
import com.arenella.recruit.candidates.controllers.CandidateController.CANDIDATE_UPDATE_ACTIONS;
import com.arenella.recruit.candidates.controllers.SavedCandidate;
import com.arenella.recruit.candidates.dao.CandidateDao;
import com.arenella.recruit.candidates.dao.CandidateSearchAlertDao;
import com.arenella.recruit.candidates.dao.CandidateSkillsDao;
import com.arenella.recruit.candidates.dao.PendingCandidateDao;
import com.arenella.recruit.candidates.dao.SavedCandidateDao;
import com.arenella.recruit.candidates.entities.CandidateEntity;
import com.arenella.recruit.candidates.entities.PendingCandidateEntity;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.services.CandidateServiceImpl;
import com.arenella.recruit.candidates.services.CandidateStatisticsService;
import com.arenella.recruit.candidates.utils.CandidateFunctionExtractorImpl;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil;
import com.arenella.recruit.candidates.utils.SkillsSynonymsUtil;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;

/**
* Unit tests for the CandidateServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CandidateServiceImplTest {

	@Mock
	private CandidateDao 					mockCandidateDao;
	
	@Mock
	private PendingCandidateDao 			mockPendingCandidateDao;
	
	@Mock
	private ExternalEventPublisher			mockExternalEventPublisher;
	
	@Mock
	private CandidateStatisticsService 		mockStatisticsService;
	
	@Mock
	private CandidateSearchAlertDao			mockSkillAlertDao;
	
	@Mock
	private SecurityContext					mockSecurityContext;
	
	@Mock
	private Authentication					mockAuthentication;
	
	@Mock
	private CandidateSuggestionUtil			mockSuggestionUtil;
	
	@Mock
	private SkillsSynonymsUtil				mockSkillsSynonymsUtil;	
	
	@Mock
	private CandidateSkillsDao				mockSkillDao;
	
	@Mock
	private SavedCandidateDao				mockSavedCandidateDao;
	
	@Spy
	private CandidateFunctionExtractorImpl	mockCandidateFunctionExtractor;
	
	@InjectMocks
	private CandidateServiceImpl 			service 					= new CandidateServiceImpl();
	
	@BeforeEach
	public void init() throws Exception{
		SecurityContextHolder.setContext(mockSecurityContext);
		
	}
	
	/**
	* Tests Exception is thrown if attempt is made to 
	* update a non existent Candidate
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidate_unknownCandidate() throws Exception {
		
		Mockito.when(mockCandidateDao.findCandidateById(0)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(RuntimeException.class , () -> {
			service.updateCandidate("1", CANDIDATE_UPDATE_ACTIONS.disable);
		});
		
	}
	
	/**
	* Tests setting a Candidate as active
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidate_enable() throws Exception {
		
		ArgumentCaptor<Candidate> 	captor 		= ArgumentCaptor.forClass(Candidate.class);
		Candidate 					candidate 	= Candidate.builder().available(false).build();
		
		Mockito.when(mockCandidateDao.findCandidateById(1L)).thenReturn(Optional.of(candidate));
		
		this.service.updateCandidate("1", CANDIDATE_UPDATE_ACTIONS.enable);
		
		Mockito.verify(mockCandidateDao).saveCandidate(captor.capture());
		Mockito.verify(this.mockExternalEventPublisher, Mockito.never()).publishCandidateNoLongerAvailableEvent(Mockito.any());
		
		assertTrue(captor.getValue().isAvailable());
		
	}
	
	/**
	* Tests setting the Candidate as not active
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidate_disable() throws Exception {
		
		ArgumentCaptor<Candidate> 	captor 		= ArgumentCaptor.forClass(Candidate.class);
		Candidate 					candidate 	= Candidate.builder().candidateId("123").available(true).build();
		
		Mockito.when(mockCandidateDao.findCandidateById(1L)).thenReturn(Optional.of(candidate));
		
		this.service.updateCandidate("1", CANDIDATE_UPDATE_ACTIONS.disable);
		
		Mockito.verify(mockCandidateDao).saveCandidate(captor.capture());
		Mockito.verify(this.mockExternalEventPublisher).publishCandidateNoLongerAvailableEvent(Mockito.any());
		
		assertFalse(captor.getValue().isAvailable());
		
	}
		
	/**
	* Tests Exception thrown if Candidate does not exist
	* @throws Exception
	*/
	@Test
	public void testFlagCandidateAvailability_unknownCandidate() throws Exception{
		
		Mockito.when(this.mockCandidateDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.flagCandidateAvailability(4L, false);
		});
		
	}
	
	/**
	* Test setting of flaggedAsUnavailable attribute
	* @throws Exception
	*/
	@Test
	public void testFlagCandidateAvailability() throws Exception{
		
		ArgumentCaptor<CandidateEntity> captor = ArgumentCaptor.forClass(CandidateEntity.class);
		CandidateEntity candidateEntity = CandidateEntity.builder().build();
		
		Mockito.when(this.mockCandidateDao.findById(Mockito.anyLong())).thenReturn(Optional.of(candidateEntity));
		Mockito.when(this.mockCandidateDao.save(captor.capture())).thenReturn(null);
		
		this.service.flagCandidateAvailability(4L, true);
		
		Mockito.verify(this.mockCandidateDao).save(candidateEntity);
		
		assertTrue(captor.getValue().isFlaggedAsUnavailable());
		
	}
	
	/**
	* Tests Exception thrown if Candidate does not exist
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidatesLastAvailabilityCheck_unknownCandidate() throws Exception{
		
		Mockito.when(this.mockCandidateDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateCandidatesLastAvailabilityCheck(4L);
		});
		
	}
	
	/**
	* Test setting of flaggedAsUnavailable attribute
	* @throws Exception
	*/
	@Test
	public void testUpdateCandidatesLastAvailabilityCheck() throws Exception{
		
		ArgumentCaptor<CandidateEntity> captor = ArgumentCaptor.forClass(CandidateEntity.class);
		CandidateEntity candidateEntity = CandidateEntity.builder().build();
		
		Mockito.when(this.mockCandidateDao.findById(Mockito.anyLong())).thenReturn(Optional.of(candidateEntity));
		Mockito.when(this.mockCandidateDao.save(captor.capture())).thenReturn(null);
		
		this.service.updateCandidatesLastAvailabilityCheck(4L);
		
		Mockito.verify(this.mockCandidateDao).save(candidateEntity);
		
	}

	
	/**
	* Tests Exception is thrown if an attempt is made to persist 
	* a PendingCandidate without specifying an Id
	* @throws Exception
	*/
	@Test
	public void testPersistPendingCandidate_noId() throws Exception {
		
		PendingCandidate pendingCandidate = PendingCandidate.builder().build();
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.persistPendingCandidate(pendingCandidate);
		});
		
	}
	
	/**
	* Tests Exception is thrown if an attempt is made to persist 
	* a PendingCandidate with an existing Id
	* @throws Exception
	*/
	@Test
	public void testPersistPendingCandidate_existingId() throws Exception {
		
		PendingCandidate pendingCandidate = PendingCandidate.builder().build();
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.persistPendingCandidate(pendingCandidate);
		});
		
	}
	
	/**
	* Tests successful persisting of PendingCandidate
	* @throws Exception
	*/
	@Test
	public void testPersistPendingCandidate() throws Exception {
		
		PendingCandidate pendingCandidate = PendingCandidate
													.builder()
														.pendingCandidateId(UUID.randomUUID())
													.build();
		
		Mockito.when(mockPendingCandidateDao.existsById(Mockito.any())).thenReturn(false);
		
		this.service.persistPendingCandidate(pendingCandidate);
		
		Mockito.verify(mockPendingCandidateDao).save(Mockito.any(PendingCandidateEntity.class));
		
	}
	
	/**
	* Tests persisting of Alert
	* @throws Exception
	*/
	@Test
	public void testAddSearchAlert() throws Exception{
		
		ReflectionTestUtils.invokeMethod(mockCandidateFunctionExtractor, "init");
		
		ArgumentCaptor<CandidateSearchAlert> argCapt = ArgumentCaptor.forClass(CandidateSearchAlert.class);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("reruiter1");

		Mockito.doNothing().when(this.mockSkillAlertDao).saveAlert(argCapt.capture());
		
		CandidateSearchAlert alert = CandidateSearchAlert.builder().functions(Set.of(FUNCTION.IT_RECRUITER)).build();
		
		this.service.addSearchAlert(alert, "");
		
		assertNotNull(alert.getAlertId());
		
		Mockito.verify(this.mockSkillAlertDao).saveAlert(alert);
		
		assertEquals(1, argCapt.getValue().getFunctions().size());
		assertTrue(argCapt.getValue().getFunctions().contains(FUNCTION.IT_RECRUITER));
		
	}

	/**
	* Tests persisting of Alert when searchText is available. In this case FUNCTIONS 
	* must be defined from the searchText and override any existing defined FUNCTIONS
	* @throws Exception
	*/
	@Test
	public void testAddSearchAlert_hasSearchText() throws Exception{
		
		ReflectionTestUtils.invokeMethod(mockCandidateFunctionExtractor, "init");
		
		ArgumentCaptor<CandidateSearchAlert> argCapt = ArgumentCaptor.forClass(CandidateSearchAlert.class);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("reruiter1");
		Mockito.doNothing().when(this.mockSkillAlertDao).saveAlert(argCapt.capture());
				
		CandidateSearchAlert alert = 
				CandidateSearchAlert
				.builder()
					.functions(Set.of(FUNCTION.ARCHITECT))
				.build();
		
		this.service.addSearchAlert(alert, "java developer");
		
		assertNotNull(alert.getAlertId());
		
		Mockito.verify(this.mockSkillAlertDao).saveAlert(alert);
		
		assertEquals(1, argCapt.getValue().getFunctions().size());
		assertTrue(argCapt.getValue().getFunctions().contains(FUNCTION.JAVA_DEV));
		
	}
	
	/**
	* Tests retrieval of Alerts for current Recruiter
	* @throws Exception
	*/
	@Test
	public void testGetAlertsForCurrentUser() throws Exception{
		
		final String recruiterId = "reruiter1";
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(recruiterId);
		
		Mockito.when(this.mockSkillAlertDao
				.fetchAlertsByRecruiterId(Mockito.anyString()))
				.thenReturn(Set.of(CandidateSearchAlert.builder().build(),
						CandidateSearchAlert.builder().build()));
		
		Set<CandidateSearchAlert> alerts = this.service.getAlertsForCurrentUser();
		
		assertEquals(2, alerts.size());
		
		Mockito.verify(this.mockSkillAlertDao).fetchAlertsByRecruiterId(recruiterId);
		
	}
	
	/**
	* Tests attempt made to delete unknown Alert
	* @throws Exception
	*/
	@Test
	public void testDeleteCandidateSearchAlert_unknownId() throws Exception{
		
		UUID id = UUID.randomUUID();
		
		Mockito.when(this.mockSkillAlertDao.getchAlertById(id)).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteSearchAlert(id);
		});
		
	}
	
	/**
	* Tests if User is owner of Alert the Alert is Deleted
	* @throws Exception
	*/
	@Test
	public void testDeleteCandidateSearchAlert_owner() throws Exception{
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("reruiter1");
		
		UUID id = UUID.randomUUID();
		
		Mockito.when(this.mockSkillAlertDao.getchAlertById(id)).thenReturn(Optional.of(CandidateSearchAlert.builder().recruiterId("reruiter1").build()));
		
		this.service.deleteSearchAlert(id);
		
		Mockito.verify(this.mockSkillAlertDao).deleteById(id);
		
	}
	
	/**
	* Tests if User is not the owner of Alert the Alert is not Deleted
	* @throws Exception
	*/
	@Test
	public void testDeleteCandidateSearchAlert_isNotOwner() throws Exception{
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("reruiter1");
		
		UUID id = UUID.randomUUID();
		
		Mockito.when(this.mockSkillAlertDao.getchAlertById(id)).thenReturn(Optional.of(CandidateSearchAlert.builder().recruiterId("r1").build()));
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteSearchAlert(id);
			
		});
		
	}
	
	/**
	* Tests poor rating is returned if no Candidate was found
	* @throws Exception
	*/
	@Test
	public void testDoTestCandidateAlert_noMatchingCandidate() throws Exception{
		
		final long candidateId = 404;
		
		CandidateFilterOptions filterOptions = 
				CandidateFilterOptions
					.builder()
						.skills(Set.of("JAVA"))
						.dutch(LEVEL.PROFICIENT)
						.french(LEVEL.PROFICIENT)
						.english(LEVEL.PROFICIENT)
					.build();
		
		ArgumentCaptor<CandidateFilterOptions> filterArgCaptor = ArgumentCaptor.forClass(CandidateFilterOptions.class);
		
		Mockito.when(this.mockCandidateDao.findCandidates(filterArgCaptor.capture())).thenReturn(Set.of());
		
		suggestion_accuracy accuracy = this.service.doTestCandidateAlert(candidateId, filterOptions);
		
		assertEquals(suggestion_accuracy.poor, accuracy);
		
		CandidateFilterOptions  captorResult = filterArgCaptor.getValue();
		
		assertTrue(captorResult.getSkills().isEmpty());
		assertTrue(captorResult.getDutch().isEmpty());
		assertTrue(captorResult.getEnglish().isEmpty());
		assertTrue(captorResult.getFrench().isEmpty());
		
	}

	/**
	* Tests rating is returned if Candidate was found
	* @throws Exception
	*/
	@Test
	public void testDoTestCandidateAlert_MatchingCandidate() throws Exception{
		
		final long candidateId = 404;
		
		CandidateFilterOptions filterOptions = 
				CandidateFilterOptions
					.builder()
						.skills(Set.of("JAVA"))
						.dutch(LEVEL.PROFICIENT)
						.french(LEVEL.PROFICIENT)
						.english(LEVEL.PROFICIENT)
					.build();
		
		ArgumentCaptor<CandidateFilterOptions> filterArgCaptor = ArgumentCaptor.forClass(CandidateFilterOptions.class);
		
		Mockito
			.when(this.mockCandidateDao.findCandidates(filterArgCaptor.capture()))
			.thenReturn(Set.of(Candidate.builder().candidateId(String.valueOf(candidateId)).build()));
		
		Mockito
			.when(this.mockSuggestionUtil.isPerfectMatch(Mockito.any(CandidateSearchAccuracyWrapper.class), filterArgCaptor.capture()))
			.thenReturn(true);
		
		Mockito
			.doNothing().when(this.mockSkillsSynonymsUtil).addSynonymsForSkills(Mockito.anySet(), Mockito.anySet());
		
		suggestion_accuracy accuracy = this.service.doTestCandidateAlert(candidateId, filterOptions);
		
		assertEquals(suggestion_accuracy.perfect, accuracy);
		
		CandidateFilterOptions  captorResult = filterArgCaptor.getValue();
		
		assertFalse(captorResult.getSkills().isEmpty());
		assertFalse(captorResult.getDutch().isEmpty());
		assertFalse(captorResult.getEnglish().isEmpty());
		assertFalse(captorResult.getFrench().isEmpty());
		
	}
	
	/**
	* Tests poor rating is returned if Candidate was found but the accuracy was less than
	* good.
	* @throws Exception
	*/
	@Test
	public void testDoTestCandidateAlert_MatchingCandidate_no_positive_accuracy() throws Exception{
		
		final long candidateId = 404;
		
		CandidateFilterOptions filterOptions = 
				CandidateFilterOptions
					.builder()
						.skills(Set.of("JAVA"))
						.dutch(LEVEL.PROFICIENT)
						.french(LEVEL.PROFICIENT)
						.english(LEVEL.PROFICIENT)
					.build();
		
		Mockito
			.when(this.mockCandidateDao.findCandidates(Mockito.any()))
			.thenReturn(Set.of(Candidate.builder().candidateId(String.valueOf(candidateId)).build()));
		
		Mockito
			.when(this.mockSuggestionUtil.isPerfectMatch(Mockito.any(CandidateSearchAccuracyWrapper.class), Mockito.any()))
			.thenReturn(false);
		
		Mockito
		.when(this.mockSuggestionUtil.isExcellentMatch(Mockito.any(CandidateSearchAccuracyWrapper.class), Mockito.any()))
		.thenReturn(false);
	
		Mockito
		.when(this.mockSuggestionUtil.isGoodMatch(Mockito.any(CandidateSearchAccuracyWrapper.class), Mockito.any()))
		.thenReturn(false);
	
		Mockito
			.doNothing().when(this.mockSkillsSynonymsUtil).addSynonymsForSkills(Mockito.anySet(), Mockito.anySet());
		
		suggestion_accuracy accuracy = this.service.doTestCandidateAlert(candidateId, filterOptions);
		
		assertEquals(suggestion_accuracy.poor, accuracy);
		
	}
	
	/**
	* Test exception thrown if filters could not be extracted
	* @throws Exception
	*/
	@Test
	public void testExtractFiltersFromDocument_failure() throws Exception{
		
		Assertions.assertThrows(RuntimeException.class, () ->{
			this.service.extractFiltersFromDocument(null, null);
		});
		
	}
	
	/**
	* Tests exception is thrown in the Candidate being added was 
	* previously added
	* @throws Exception
	*/
	@Test
	public void testAddSavedCanidate_already_exists() throws Exception{
		
		Mockito.when(this.mockSavedCandidateDao.exists(Mockito.anyString(), Mockito.anyLong())).thenReturn(true);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.addSavedCanidate(SavedCandidate.builder().candidateId(1001).userId("kparkings").build());
		});
		
	}
	
	/**
	* Tests happy path
	* @throws Exception
	*/
	@Test
	public void testAddSavedCanidate() throws Exception{
		
		Mockito.when(this.mockSavedCandidateDao.exists(Mockito.anyString(), Mockito.anyLong())).thenReturn(false);
		
		this.service.addSavedCanidate(SavedCandidate.builder().candidateId(1001).userId("kparkings").build());
		
		Mockito.verify(this.mockSavedCandidateDao).persistSavedCandidate(Mockito.any(SavedCandidate.class));
		
	}
	
	/**
	* Tests Fetch of SavedCandidates
	* @throws Exception
	*/
	@Test
	public void testFetchSavedCandidatesForUser() throws Exception{
		
		final String 	userId 		= "kparkings";
		final long 		candidate1 	= 1001;
		final long 		candidate2 	= 1007;
		
		Set<SavedCandidate> savedCandidates = new HashSet<>();
		
		savedCandidates.add(SavedCandidate.builder().candidateId(1001).userId(userId).build());
		savedCandidates.add(SavedCandidate.builder().candidateId(1007).userId(userId).build());
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(userId);
		Mockito.when(this.mockCandidateDao.findCandidateById(candidate1)).thenReturn(Optional.of(Candidate.builder().available(true).candidateId(String.valueOf(candidate1)).build()));
		Mockito.when(this.mockCandidateDao.findCandidateById(candidate2)).thenReturn(Optional.of(Candidate.builder().available(true).candidateId(String.valueOf(candidate2)).build()));
		Mockito.when(this.mockCandidateDao.existsById(Mockito.anyLong())).thenReturn(true);
		Mockito.when(this.mockSavedCandidateDao.fetchSavedCandidatesByUserId(Mockito.anyString())).thenReturn(savedCandidates);
		
		Map<SavedCandidate, Candidate> result = this.service.fetchSavedCandidatesForUser();
		
		SavedCandidate sc1 = result.keySet().stream().filter(c -> c.getCandidateId() == candidate1).findAny().orElseThrow();
		SavedCandidate sc2 = result.keySet().stream().filter(c -> c.getCandidateId() == candidate2).findAny().orElseThrow();
		
		assertEquals(String.valueOf(candidate1), result.get(sc1).getCandidateId());
		assertEquals(String.valueOf(candidate2), result.get(sc2).getCandidateId());
		
	}
	
	/**
	* Tests Fetch of SavedCandidates
	* @throws Exception
	*/
	@Test
	public void testFetchSavedCandidatesForUser_missingCandidate() throws Exception{
		
		final String 	userId 		= "kparkings";
		final long 		candidate1 	= 1001;
		final long 		candidate2 	= 1007;
		
		Set<SavedCandidate> savedCandidates = new HashSet<>();
		
		savedCandidates.add(SavedCandidate.builder().candidateId(1001).userId(userId).build());
		savedCandidates.add(SavedCandidate.builder().candidateId(1007).userId(userId).build());
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(userId);
		Mockito.when(this.mockCandidateDao.findCandidateById(candidate1)).thenReturn(Optional.of(Candidate.builder().candidateId(String.valueOf(candidate1)).build()));
		Mockito.when(this.mockCandidateDao.existsById(candidate1)).thenReturn(true);
		Mockito.when(this.mockCandidateDao.existsById(candidate2)).thenReturn(false);
		Mockito.when(this.mockSavedCandidateDao.fetchSavedCandidatesByUserId(Mockito.anyString())).thenReturn(savedCandidates);
		
		Map<SavedCandidate, Candidate> result = this.service.fetchSavedCandidatesForUser();
		
		result.keySet().stream().filter(c -> c.getCandidateId() == candidate1).findAny().orElseThrow();
		
		assertTrue(result.keySet().stream().filter(c -> c.getCandidateId() == candidate2).findAny().isEmpty());
		
	}

	/**
	* Test case that attempt is made to remove a SavedCandidate that 
	* does not exist
	* @throws Exception
	*/
	@Test
	public void testRemoveSavedCanidate_doesnt_exist() throws Exception{
		
		final long savedCandidate1 	= 1001;
		
		Mockito.when(this.mockSavedCandidateDao.exists(Mockito.anyString(), Mockito.anyLong())).thenReturn(false);
		Mockito.when(mockAuthentication.getName()).thenReturn("kparkings");
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.removeSavedCandidate(savedCandidate1, mockAuthentication);
		});
		
	}
	
	/**
	* Tests the happy path
	* @throws Exception
	*/
	@Test
	public void testRemoveSavedCanidate() throws Exception{
		
		final long savedCandidate1 	= 1001;
		
		Mockito.when(this.mockSavedCandidateDao.exists(Mockito.anyString(), Mockito.anyLong())).thenReturn(true);
		Mockito.when(mockAuthentication.getName()).thenReturn("kparkings");
		
		this.service.removeSavedCandidate(savedCandidate1, mockAuthentication);
		
		Mockito.verify(this.mockSavedCandidateDao).delete(Mockito.anyString(), Mockito.anyLong());
		
	}
	
	/**
	* Tests exception thrown if attempt is made to update a SavedCandidate 
	* that does not exist
	* @throws Exception
	*/
	@Test
	public void testUpdateSavedCanidate_doesnt_exist() throws Exception{
		
		final long savedCandidate1 	= 1001;
		
		Mockito.when(this.mockSavedCandidateDao.exists(Mockito.anyString(), Mockito.anyLong())).thenReturn(false);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateSavedCandidate(SavedCandidate.builder().userId("kparkings").candidateId(savedCandidate1).build());
		});
		
	}
	
	/**
	* Tests happy path
	* @throws Exception
	*/
	@Test
	public void testUpdateSavedCanidate() throws Exception{
		
		final long savedCandidate1 	= 1001;
		
		Mockito.when(this.mockSavedCandidateDao.exists(Mockito.anyString(), Mockito.anyLong())).thenReturn(true);
		
		this.service.updateSavedCandidate(SavedCandidate.builder().userId("kparkings").candidateId(savedCandidate1).build());
		
		Mockito.verify(this.mockSavedCandidateDao).updateSavedCandidate(Mockito.any(SavedCandidate.class));
		
	}
	
}