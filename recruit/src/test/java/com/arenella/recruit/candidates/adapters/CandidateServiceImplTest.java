package com.arenella.recruit.candidates.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.adapters.events.CandidateAccountCreatedEvent;
import com.arenella.recruit.adapters.events.CandidateDeletedEvent;
import com.arenella.recruit.adapters.events.CandidatePasswordUpdatedEvent;
import com.arenella.recruit.adapters.events.CandidateUpdateEvent;
import com.arenella.recruit.adapters.events.CandidateUpdatedEvent;
import com.arenella.recruit.adapters.events.ContactRequestEvent;
import com.arenella.recruit.authentication.spring.filters.ArenellaRoleManager;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.CANDIDATE_TYPE;
import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.candidates.beans.CandidateExtractedFilters;
import com.arenella.recruit.candidates.beans.Contact.CONTACT_TYPE;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.CandidateSearchAlert;
import com.arenella.recruit.candidates.beans.CandidateSkill;
import com.arenella.recruit.candidates.beans.CandidateUpdateRequest;
import com.arenella.recruit.candidates.beans.Contact;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.beans.PendingCandidate;
import com.arenella.recruit.candidates.beans.RecruiterCredit;
import com.arenella.recruit.candidates.beans.SavedCandidateSearch;
import com.arenella.recruit.candidates.controllers.CandidateController.CANDIDATE_UPDATE_ACTIONS;
import com.arenella.recruit.candidates.controllers.CandidateValidationException;
import com.arenella.recruit.candidates.controllers.SavedCandidate;
import com.arenella.recruit.candidates.dao.CandidateRecruiterCreditDao;
import com.arenella.recruit.candidates.dao.CandidateSearchAlertDao;
import com.arenella.recruit.candidates.dao.CandidateSkillsDao;
import com.arenella.recruit.candidates.dao.PendingCandidateDao;
import com.arenella.recruit.candidates.dao.RecruiterContactDao;
import com.arenella.recruit.candidates.dao.SavedCandidateDao;
import com.arenella.recruit.candidates.dao.SavedCandidateSearchEntityDao;
import com.arenella.recruit.candidates.dao.SkillUpdateStatDao;
import com.arenella.recruit.candidates.entities.CandidateSkillEntity.VALIDATION_STATUS;
import com.arenella.recruit.candidates.entities.PendingCandidateEntity;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;
import com.arenella.recruit.candidates.extractors.DocumentFilterExtractionUtil;
import com.arenella.recruit.candidates.repos.CandidateRepository;
import com.arenella.recruit.candidates.services.CandidateServiceImpl;
import com.arenella.recruit.candidates.services.CandidateStatisticsService;
import com.arenella.recruit.candidates.services.CityService;
import com.arenella.recruit.candidates.utils.CandidateFunctionExtractorImpl;
import com.arenella.recruit.candidates.utils.CandidateImageFileSecurityParser;
import com.arenella.recruit.candidates.utils.CandidateImageManipulator;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil;
import com.arenella.recruit.candidates.utils.SkillsSynonymsUtil;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.newsfeed.beans.NewsFeedItem.NEWSFEED_ITEM_TYPE;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;

/**
* Unit tests for the CandidateServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class CandidateServiceImplTest {

	@Mock
	private CandidateRepository 					mockCandidateRepo;

	@Mock
	private PendingCandidateDao 					mockPendingCandidateDao;
	
	@Mock
	private ExternalEventPublisher					mockExternalEventPublisher;
	
	@Mock
	private CandidateStatisticsService 				mockStatisticsService;
	
	@Mock
	private CandidateSearchAlertDao					mockSkillAlertDao;
	
	@Mock
	private SecurityContext							mockSecurityContext;
	
	@Mock
	private Authentication							mockAuthentication;
		
	@Mock
	private CandidateSuggestionUtil					mockSuggestionUtil;
	
	@Mock
	private SkillsSynonymsUtil						mockSkillsSynonymsUtil;	
	
	@Mock
	private CandidateSkillsDao						mockSkillDao;
	
	@Mock
	private SavedCandidateDao						mockSavedCandidateDao;
	
	@Mock
	private CandidateImageFileSecurityParser		mockImageFileSecurityParser;
	
	@Mock
	private CandidateImageManipulator				mockImageManipulator;
	
	@Mock
	private RecruiterContactDao						mockContactDao;
	
	@Mock
	private CandidateRecruiterCreditDao				mockCreditDao;
	
	@Mock
	private DocumentFilterExtractionUtil			mockDocumentFilterExtractionUtil;
	
	@Spy
	private CandidateFunctionExtractorImpl			mockCandidateFunctionExtractor;
	
	@Mock
	private ElasticsearchClient 					mockEsClient;
	
	@Mock
	private SkillUpdateStatDao						mockkillUpdateStatDao;
	
	@Spy
	private ArenellaRoleManager						spyRoleManager;
	
	@Mock
	private CityService								mockCityService;
	
	@Mock
	private SavedCandidateSearchEntityDao			mockSavedCandidateSearchEntityDao;
	
	@InjectMocks
	private CandidateServiceImpl 					service 					= new CandidateServiceImpl();
	
	/**
	* Sets up test environment 
	*/
	@BeforeEach
	void init() {
		SecurityContextHolder.setContext(mockSecurityContext);
	}
	
	/**
	* Tests exception is thrown if Email already used for the Candidate 
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testPersistCandidate_emailAlreadyExists() throws Exception{

		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(this.mockCandidateRepo.emailInUse(anyString(), any())).thenReturn(true);
		
		assertThrows(CandidateValidationException.class, () -> {
			this.service.persistCandidate(Candidate.builder().email("admin@arenella-ict.com").build());
		});
		
	}
	
	/**
	* Tests OwnerId is added if the User adding the Candidate is not an admin user 
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testPersistCandidate_NonAdminUserOwnerId() throws Exception{

		ArgumentCaptor<Candidate> argCapt = ArgumentCaptor.forClass(Candidate.class);
		
		Contact contact = Contact.builder().contactType(CONTACT_TYPE.RECRUITER).email("kp@test.it").firstname("kevin").surname("parkings").userId("kp1").build();
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
		
		when(mockCandidateRepo.saveCandidate(argCapt.capture())).thenReturn(0L);
		
		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn("kp1");
		
		when(this.mockContactDao.getByTypeAndId(any(), any())).thenReturn(Optional.of(contact));
		
		this.service.persistCandidate(Candidate.builder().candidateId("123").firstname("kevin").email("admin@arenella-ict.com").build());
		
		Assertions.assertEquals(CANDIDATE_TYPE.MARKETPLACE_CANDIDATE, argCapt.getValue().getCandidateType());
		Assertions.assertEquals("kp1", argCapt.getValue().getOwnerId().get());
	}
	
	/**
	* Tests OwnerId is not added if the User adding the Candidate is not an admin user 
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testPersistCandidate_adminUserOwnerId() throws Exception{

		ArgumentCaptor<Candidate> argCapt = ArgumentCaptor.forClass(Candidate.class);
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		when(mockCandidateRepo.saveCandidate(argCapt.capture())).thenReturn(0L);
		
		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		
		this.service.persistCandidate(Candidate.builder().candidateId("123").firstname("kevin").email("admin@arenella-ict.com").build());
		
		Assertions.assertEquals(CANDIDATE_TYPE.CANDIDATE, argCapt.getValue().getCandidateType());
		Assertions.assertTrue(argCapt.getValue().getOwnerId().isEmpty());
	}
	
	/**
	* Tests exception is thrown if Email not used for the Pending Candidate 
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testPersistCandidate() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		when(this.mockCandidateRepo.emailInUse(anyString(), any())).thenReturn(false);
	
		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);

		this.service.persistCandidate(Candidate
				.builder()
					.candidateId("1000")
					.email("admin@arenella-ict.com")
					.firstname("kevin")
					.country(COUNTRY.BELGIUM)
					.city("Brussels")
				.build());
		
		verify(this.mockExternalEventPublisher).publishCandidateAccountCreatedEvent(any(CandidateAccountCreatedEvent.class));
		verify(this.mockExternalEventPublisher).publishSendEmailCommand(any(RequestSendEmailCommand.class));
		verify(this.mockExternalEventPublisher).publishCandidateCreatedEvent(any(CandidateCreatedEvent.class));
		verify(this.mockExternalEventPublisher).publishCandidateUpdateEvent(any(CandidateUpdateEvent.class));
		verify(this.mockCityService).performNewCityCheck(COUNTRY.BELGIUM, "Brussels");
		
	}
	
	/**
	* If Recruiter created the Candidate then there should be no User created and no email with the 
	* Users login details sent
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testPersistCandidate_recruiter() throws Exception{
		
		Contact contact = Contact.builder().firstname("kevin").surname("parkings").email("no-reply@arenella-ict.com").build();
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn("kp1");
		
		when(this.mockContactDao.getByTypeAndId(CONTACT_TYPE.RECRUITER, "kp1")).thenReturn(Optional.of(contact)); 

		this.service.persistCandidate(Candidate
				.builder()
					.candidateId("1000")
					.email("no-reply@arenella-ict.com")
					.firstname("kevin")
				.build());
		
		verify(this.mockExternalEventPublisher, times(0)).publishCandidateAccountCreatedEvent(any(CandidateAccountCreatedEvent.class));
		verify(this.mockExternalEventPublisher, times(0)).publishSendEmailCommand(any(RequestSendEmailCommand.class));
		verify(this.mockExternalEventPublisher, times(0)).publishCandidateCreatedEvent(any(CandidateCreatedEvent.class));
		
	}
	/**
	* Tests exception is thrown if Email not used for the Pending Candidate 
	*/
	@Test
	void testPersistPendingCandidate_emailAlreadyExists() {
		
		when(this.mockPendingCandidateDao.emailInUse(anyString())).thenReturn(true);
		
		assertThrows(CandidateValidationException.class, () -> {
			this.service.persistPendingCandidate(PendingCandidate.builder().email("admin@arenella-ict.com").build());
		});
		
	}
	
	/**
	* Tests Exception is thrown if attempt is made to 
	* update a non existent Candidate
	* @throws Exception
	*/
	@Test
	void testUpdateCandidate_unknownCandidate() {
		
		when(mockCandidateRepo.findCandidateById(0)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(RuntimeException.class , () -> {
			service.updateCandidate("1", CANDIDATE_UPDATE_ACTIONS.disable);
		});
		
		verify(this.mockExternalEventPublisher, never()).publishCandidateUpdateEvent(any());
		
	}
	
	/**
	* Tests setting a Candidate as active
	* @throws Exception
	*/
	@Test
	void testUpdateCandidate_enable() {
		
		ArgumentCaptor<Candidate> 				captor 				= ArgumentCaptor.forClass(Candidate.class);
		ArgumentCaptor<CandidateUpdateEvent> 	newsFeedCaptor 		= ArgumentCaptor.forClass(CandidateUpdateEvent.class);
		Candidate 								candidate 			= Candidate.builder().available(false).build();
		
		when(mockCandidateRepo.findCandidateById(1L)).thenReturn(Optional.of(candidate));
		
		this.service.updateCandidate("1", CANDIDATE_UPDATE_ACTIONS.enable);
		
		verify(mockCandidateRepo).saveCandidate(captor.capture());
		verify(this.mockExternalEventPublisher, never()).publishCandidateNoLongerAvailableEvent(any());
		
		assertTrue(captor.getValue().isAvailable());
		
		verify(this.mockExternalEventPublisher).publishCandidateUpdateEvent(newsFeedCaptor.capture());
		
		assertEquals(NEWSFEED_ITEM_TYPE.CANDIDATE_BECAME_AVAILABLE, newsFeedCaptor.getValue().getItemType());
		
	}
	
	/**
	* Tests if update was enable and Candidate was already enabled that no newsfeed item was created
	* @throws Exception
	*/
	@Test
	void testUpdateCandidate_enable_alreadyEnabled() {
		
		ArgumentCaptor<CandidateUpdateEvent> 	newsFeedCaptor 		= ArgumentCaptor.forClass(CandidateUpdateEvent.class);
		Candidate 								candidate 			= Candidate.builder().available(true).build();
		
		when(mockCandidateRepo.findCandidateById(1L)).thenReturn(Optional.of(candidate));
		
		this.service.updateCandidate("1", CANDIDATE_UPDATE_ACTIONS.enable);
		
		verify(this.mockExternalEventPublisher, never()).publishCandidateUpdateEvent(newsFeedCaptor.capture());
		
	}
	
	/**
	* Tests if update was disable and Candidate was already disabled that no newsfeed item was created
	* @throws Exception
	*/
	@Test
	void testUpdateCandidate_enable_alreadyDisabled() {
		
		ArgumentCaptor<CandidateUpdateEvent> 	newsFeedCaptor 		= ArgumentCaptor.forClass(CandidateUpdateEvent.class);
		Candidate 								candidate 			= Candidate.builder().candidateId("1").available(false).build();
		
		when(mockCandidateRepo.findCandidateById(1L)).thenReturn(Optional.of(candidate));
		
		this.service.updateCandidate("1", CANDIDATE_UPDATE_ACTIONS.disable);
		
		verify(this.mockExternalEventPublisher, never()).publishCandidateUpdateEvent(newsFeedCaptor.capture());
		
	}
	
	/**
	* Tests setting the Candidate as not active
	* @throws Exception
	*/
	@Test
	void testUpdateCandidate_disable() {
		
		ArgumentCaptor<Candidate> 				captor 				= ArgumentCaptor.forClass(Candidate.class);
		ArgumentCaptor<CandidateUpdateEvent> 	newsFeedCaptor 		= ArgumentCaptor.forClass(CandidateUpdateEvent.class);
		Candidate 								candidate 			= Candidate.builder().candidateId("123").available(true).build();
		
		when(mockCandidateRepo.findCandidateById(1L)).thenReturn(Optional.of(candidate));
		
		this.service.updateCandidate("1", CANDIDATE_UPDATE_ACTIONS.disable);
		
		verify(mockCandidateRepo).saveCandidate(captor.capture());
		verify(this.mockExternalEventPublisher).publishCandidateNoLongerAvailableEvent(any());
		
		assertFalse(captor.getValue().isAvailable());
		
		verify(this.mockExternalEventPublisher).publishCandidateUpdateEvent(newsFeedCaptor.capture());
		
		assertEquals(NEWSFEED_ITEM_TYPE.CANDIDATE_BECAME_UNAVAILABLE, newsFeedCaptor.getValue().getItemType());
		
	}
	
	/**
	* Tests Exception thrown if Candidate does not exist
	* @throws Exception
	*/
	@Test
	void testUpdateCandidatesLastAvailabilityCheck_unknownCandidate() {
		
		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateCandidatesLastAvailabilityCheck(4L);
		});
		
	}
	
	/**
	* Test setting of flaggedAsUnavailable attribute
	* @throws Exception
	*/
	@Test
	void testUpdateCandidatesLastAvailabilityCheck() {
		
		ArgumentCaptor<Candidate> captor = ArgumentCaptor.forClass(Candidate.class);
		Candidate candidate = Candidate.builder().build();
		
		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.of(candidate));
		when(this.mockCandidateRepo.saveCandidate(captor.capture())).thenReturn(0L);
		
		this.service.updateCandidatesLastAvailabilityCheck(4L);
		
		verify(this.mockCandidateRepo).saveCandidate(candidate);
		
	}

	/**
	* Tests Exception is thrown if an attempt is made to persist 
	* a PendingCandidate without specifying an Id
	* @throws Exception
	*/
	@Test
	void testPersistPendingCandidate_noId() {
		
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
	void testPersistPendingCandidate_existingId() {
		
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
	void testPersistPendingCandidate() {
		
		PendingCandidate pendingCandidate = PendingCandidate
													.builder()
														.pendingCandidateId(UUID.randomUUID())
													.build();
		
		when(mockPendingCandidateDao.existsById(any())).thenReturn(false);
		
		this.service.persistPendingCandidate(pendingCandidate);
		
		verify(mockPendingCandidateDao).save(any(PendingCandidateEntity.class));
		
	}
	
	/**
	* Tests persisting of Alert
	* @throws Exception
	*/
	@Test
	void testAddSearchAlert() {
		
		ReflectionTestUtils.invokeMethod(mockCandidateFunctionExtractor, "init");
		
		ArgumentCaptor<CandidateSearchAlert> argCapt = ArgumentCaptor.forClass(CandidateSearchAlert.class);
		
		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		
		when(mockAuthentication.getPrincipal()).thenReturn("recruiter1");

		doNothing().when(this.mockSkillAlertDao).saveAlert(argCapt.capture());
		
		CandidateSearchAlert alert = CandidateSearchAlert.builder().functions(Set.of(FUNCTION.IT_RECRUITER)).build();
		
		this.service.addSearchAlert(alert, "");
		
		assertNotNull(alert.getAlertId());
		
		verify(this.mockSkillAlertDao).saveAlert(alert);
		
		assertEquals(1, argCapt.getValue().getFunctions().size());
		assertTrue(argCapt.getValue().getFunctions().contains(FUNCTION.IT_RECRUITER));
		
	}

	/**
	* Tests persisting of Alert when searchText is available. In this case FUNCTIONS 
	* must be defined from the searchText and override any existing defined FUNCTIONS
	* @throws Exception
	*/
	@Test
	void testAddSearchAlert_hasSearchText() {
		
		ReflectionTestUtils.invokeMethod(mockCandidateFunctionExtractor, "init");
		
		ArgumentCaptor<CandidateSearchAlert> argCapt = ArgumentCaptor.forClass(CandidateSearchAlert.class);
		
		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getPrincipal()).thenReturn("recruiter1");
		doNothing().when(this.mockSkillAlertDao).saveAlert(argCapt.capture());
				
		CandidateSearchAlert alert = 
				CandidateSearchAlert
				.builder()
					.functions(Set.of(FUNCTION.ARCHITECT))
				.build();
		
		this.service.addSearchAlert(alert, "java developer");
		
		assertNotNull(alert.getAlertId());
		
		verify(this.mockSkillAlertDao).saveAlert(alert);
		
		assertEquals(1, argCapt.getValue().getFunctions().size());
		assertTrue(argCapt.getValue().getFunctions().contains(FUNCTION.JAVA_DEV));
		
	}
	
	/**
	* Tests retrieval of Alerts for current Recruiter
	* @throws Exception
	*/
	@Test
	void testGetAlertsForCurrentUser() {
		
		final String recruiterId = "recruiter1";
		
		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		
		when(mockAuthentication.getPrincipal()).thenReturn(recruiterId);
		
		when(this.mockSkillAlertDao
				.fetchAlertsByRecruiterId(anyString()))
				.thenReturn(Set.of(CandidateSearchAlert.builder().build(),
						CandidateSearchAlert.builder().build()));
		
		Set<CandidateSearchAlert> alerts = this.service.getAlertsForCurrentUser();
		
		assertEquals(2, alerts.size());
		
		verify(this.mockSkillAlertDao).fetchAlertsByRecruiterId(recruiterId);
		
	}
	
	/**
	* Tests attempt made to delete unknown Alert
	* @throws Exception
	*/
	@Test
	void testDeleteCandidateSearchAlert_unknownId() {
		
		UUID id = UUID.randomUUID();
		
		when(this.mockSkillAlertDao.getchAlertById(id)).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteSearchAlert(id);
		});
		
	}
	
	/**
	* Tests if User is owner of Alert the Alert is Deleted
	* @throws Exception
	*/
	@Test
	void testDeleteCandidateSearchAlert_owner() {
		
		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		
		when(mockAuthentication.getPrincipal()).thenReturn("recruiter1");
		
		UUID id = UUID.randomUUID();
		
		when(this.mockSkillAlertDao.getchAlertById(id)).thenReturn(Optional.of(CandidateSearchAlert.builder().recruiterId("recruiter1").build()));
		
		this.service.deleteSearchAlert(id);
		
		verify(this.mockSkillAlertDao).deleteById(id);
		
	}
	
	/**
	* Tests if User is not the owner of Alert the Alert is not Deleted
	* @throws Exception
	*/
	@Test
	void testDeleteCandidateSearchAlert_isNotOwner() {
		
		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		
		when(mockAuthentication.getPrincipal()).thenReturn("recruiter1");
		
		UUID id = UUID.randomUUID();
		
		when(this.mockSkillAlertDao.getchAlertById(id)).thenReturn(Optional.of(CandidateSearchAlert.builder().recruiterId("r1").build()));
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteSearchAlert(id);
			
		});
		
	}
	
	/**
	* Tests poor rating is returned if no Candidate was found
	* @throws Exception
	*/
	@Test
	void testDoTestCandidateAlert_noMatchingCandidate() throws Exception{
		
		final long candidateId = 404;
		
		CandidateFilterOptions filterOptions = 
				CandidateFilterOptions
					.builder()
						.skills(Set.of("JAVA"))
						.languages(Set.of(
								LANGUAGE.ENGLISH,
								LANGUAGE.DUTCH,
								LANGUAGE.FRENCH)
						)
					.build();
		
		ArgumentCaptor<CandidateFilterOptions> filterArgCaptor = ArgumentCaptor.forClass(CandidateFilterOptions.class);
		
		when(this.mockCandidateRepo.findCandidates(filterArgCaptor.capture(), any(), eq(0), eq(10000))).thenReturn(Set.of());
		
		suggestion_accuracy accuracy = this.service.doTestCandidateAlert(candidateId, filterOptions);
		
		assertEquals(suggestion_accuracy.poor, accuracy);
		
		CandidateFilterOptions  captorResult = filterArgCaptor.getValue();
		
		assertTrue(captorResult.getSkills().isEmpty());
		
	}

	/**
	* Tests rating is returned if Candidate was found
	* @throws Exception
	*/
	@Test
	void testDoTestCandidateAlert_MatchingCandidate() throws Exception{
		
		final long candidateId = 404;
		
		CandidateFilterOptions filterOptions = 
				CandidateFilterOptions
					.builder()
						.skills(Set.of("JAVA"))
						.languages(Set.of(
								LANGUAGE.ENGLISH,
								LANGUAGE.DUTCH,
								LANGUAGE.FRENCH)
						)
					.build();
		
		ArgumentCaptor<CandidateFilterOptions> filterArgCaptor = ArgumentCaptor.forClass(CandidateFilterOptions.class);
		
		when(this.mockCandidateRepo.findCandidates(filterArgCaptor.capture(), any(), eq(0), eq(10000)))
			.thenReturn(Set.of(Candidate.builder().candidateId(String.valueOf(candidateId)).build()));
		
		when(this.mockSuggestionUtil.isPerfectMatch(any(CandidateSearchAccuracyWrapper.class), filterArgCaptor.capture(), any()))
			.thenReturn(true);
		
		doNothing().when(this.mockSkillsSynonymsUtil).addSynonymsForSkills(anySet(), anySet());
		
		suggestion_accuracy accuracy = this.service.doTestCandidateAlert(candidateId, filterOptions);
		
		assertEquals(suggestion_accuracy.perfect, accuracy);
		
		CandidateFilterOptions  captorResult = filterArgCaptor.getValue();
		
		assertFalse(captorResult.getSkills().isEmpty());
		
	}
	
	/**
	* Tests poor rating is returned if Candidate was found but the accuracy was less than
	* good.
	* @throws Exception
	*/
	@Test
	void testDoTestCandidateAlert_MatchingCandidate_no_positive_accuracy() throws Exception{
		
		final long candidateId = 404;
		
		CandidateFilterOptions filterOptions = 
				CandidateFilterOptions
					.builder()
						.skills(Set.of("JAVA"))
						.languages(Set.of(
								LANGUAGE.ENGLISH,
								LANGUAGE.DUTCH,
								LANGUAGE.FRENCH)
						)
					.build();
		
		when(this.mockCandidateRepo.findCandidates(any(),any(), eq(0), eq(10000)))
			.thenReturn(Set.of(Candidate.builder().candidateId(String.valueOf(candidateId)).build()));
		
		when(this.mockSuggestionUtil.isPerfectMatch(any(CandidateSearchAccuracyWrapper.class), any(), any()))
			.thenReturn(false);
		
		when(this.mockSuggestionUtil.isExcellentMatch(any(CandidateSearchAccuracyWrapper.class), any(), any()))
			.thenReturn(false);
	
		when(this.mockSuggestionUtil.isGoodMatch(any(CandidateSearchAccuracyWrapper.class), any(), any()))
			.thenReturn(false);
	
		doNothing().when(this.mockSkillsSynonymsUtil).addSynonymsForSkills(anySet(), anySet());
		
		suggestion_accuracy accuracy = this.service.doTestCandidateAlert(candidateId, filterOptions);
		
		assertEquals(suggestion_accuracy.poor, accuracy);
		
	}
	
	/**
	* Test exception thrown if filters could not be extracted
	* @throws Exception
	*/
	@Test
	void testExtractFiltersFromDocument_failure() throws Exception{
		
		doThrow(RuntimeException.class).when(this.mockDocumentFilterExtractionUtil).extractFilters(null, null);
		
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
	void testAddSavedCanidate_already_exists() {
		
		when(this.mockSavedCandidateDao.exists(anyString(), anyLong())).thenReturn(true);
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.addSavedCanidate(SavedCandidate.builder().candidateId(1001).userId("kparkings").build());
		});
		
	}
	
	/**
	* Tests happy path
	* @throws Exception
	*/
	@Test
	void testAddSavedCanidate() {
		
		when(this.mockSavedCandidateDao.exists(anyString(), anyLong())).thenReturn(false);
		
		this.service.addSavedCanidate(SavedCandidate.builder().candidateId(1001).userId("kparkings").build());
		
		verify(this.mockSavedCandidateDao).persistSavedCandidate(any(SavedCandidate.class));
		
	}
	
	/**
	* Tests Fetch of SavedCandidates
	* @throws Exception
	*/
	@Test
	void testFetchSavedCandidatesForUser() {
		
		final String 	userId 		= "kparkings";
		final long 		candidate1 	= 1001;
		final long 		candidate2 	= 1007;
		
		Set<SavedCandidate> savedCandidates = new HashSet<>();
		
		savedCandidates.add(SavedCandidate.builder().candidateId(1001).userId(userId).build());
		savedCandidates.add(SavedCandidate.builder().candidateId(1007).userId(userId).build());
		
		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getPrincipal()).thenReturn(userId);
		when(this.mockCandidateRepo.findCandidateById(candidate1)).thenReturn(Optional.empty());
		when(this.mockCandidateRepo.findCandidateById(candidate2)).thenReturn(Optional.of(Candidate.builder().available(true).firstname("Boop").candidateId(String.valueOf(candidate2)).build()));
		when(this.mockSavedCandidateDao.fetchSavedCandidatesByUserId(anyString())).thenReturn(savedCandidates);
		Map<SavedCandidate, Candidate> result = this.service.fetchSavedCandidatesForUser();
		
		SavedCandidate sc1 = result.keySet().stream().filter(c -> c.getCandidateId() == candidate1).findAny().orElseThrow();
		SavedCandidate sc2 = result.keySet().stream().filter(c -> c.getCandidateId() == candidate2).findAny().orElseThrow();
		
		assertEquals(String.valueOf(candidate1), result.get(sc1).getCandidateId());
		assertEquals(String.valueOf(candidate2), result.get(sc2).getCandidateId());
		
		assertNotEquals("Candidate No Longer Available", result.get(sc2).getFirstname());
		assertEquals("Candidate No Longer Available", result.get(sc1).getFirstname());
	}

	/**
	* Test case that attempt is made to remove a SavedCandidate that 
	* does not exist
	* @throws Exception
	*/
	@Test
	void testRemoveSavedCanidate_doesnt_exist() {
		
		final long savedCandidate1 	= 1001;
		
		when(this.mockSavedCandidateDao.exists(anyString(), anyLong())).thenReturn(false);
		when(mockAuthentication.getName()).thenReturn("kparkings");
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.removeSavedCandidate(savedCandidate1, mockAuthentication);
		});
		
	}
	
	/**
	* Tests the happy path
	* @throws Exception
	*/
	@Test
	void testRemoveSavedCanidate() {
		
		final long savedCandidate1 	= 1001;
		
		when(this.mockSavedCandidateDao.exists(anyString(), anyLong())).thenReturn(true);
		when(mockAuthentication.getName()).thenReturn("kparkings");
		
		this.service.removeSavedCandidate(savedCandidate1, mockAuthentication);
		
		verify(this.mockSavedCandidateDao).delete(anyString(), anyLong());
		
	}
	
	/**
	* Tests exception thrown if attempt is made to update a SavedCandidate 
	* that does not exist
	* @throws Exception
	*/
	@Test
	void testUpdateSavedCanidate_doesnt_exist() {
		
		final long savedCandidate1 	= 1001;
		
		when(this.mockSavedCandidateDao.exists(anyString(), anyLong())).thenReturn(false);
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateSavedCandidate(SavedCandidate.builder().userId("kparkings").candidateId(savedCandidate1).build());
		});
		
	}
	
	/**
	* Tests happy path
	* @throws Exception
	*/
	@Test
	void testUpdateSavedCanidate() {
		
		final long savedCandidate1 	= 1001;
		
		when(this.mockSavedCandidateDao.exists(anyString(), anyLong())).thenReturn(true);
		
		this.service.updateSavedCandidate(SavedCandidate.builder().userId("kparkings").candidateId(savedCandidate1).build());
		
		verify(this.mockSavedCandidateDao).updateSavedCandidate(any(SavedCandidate.class));
		
	}
	
	/**
	* Tests Exception is thrown if User is a Candidate trying to view another 
	* Candidates profile
	* @throws Exception
	*/
	@Test
	void testFetchCandidate_candidate_other_candidate() {
		
		GrantedAuthority 				mockGA = mock(GrantedAuthority.class);
		Collection<GrantedAuthority> 	authorities = Set.of(mockGA);
		
		when(mockGA.getAuthority()).thenReturn("ROLE_CANDIDATE");
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.fetchCandidate("1960", "1961", authorities);
		});
		
	}
	
	/**
	* Tests case User is recruiter and attempts to view and unavailable candidate without 
	* a paid subscription
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testFetchCandidate_candidate_is_not_avaialble_and_recruiter_no_paid_subscription() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn("user1");
		
		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.of(Candidate.builder().available(false).build()));
		when(this.mockCreditDao.getByRecruiterId(anyString())).thenReturn(Optional.of(RecruiterCredit.builder().paidSubscription(false).build()));
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.fetchCandidate("1960", "1961", authorities);
		});
		
	}
	
	/**
	* Tests case User is recruiter and attempts to view and unavailable candidate with 
	* a paid subscription
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testFetchCandidate_candidate_is_not_avaialble_and_recruiter_has_paid_subscription() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn("user1");
		
		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.of(Candidate.builder().available(false).build()));
		when(this.mockCreditDao.getByRecruiterId(anyString())).thenReturn(Optional.of(RecruiterCredit.builder().paidSubscription(true).build()));
		
		Candidate candidate = 	this.service.fetchCandidate("1960", "1961", authorities);
		
		assertNotNull(candidate);
		
	}
	
	/**
	* Tests Exception is thrown if Candidate not found
	* @throws Exception
	*/
	@Test
	void testFetchCandidate_candidate_own_candidate_not_found() {
		
		GrantedAuthority 				mockGA = mock(GrantedAuthority.class);
		Collection<GrantedAuthority> 	authorities = Set.of(mockGA);
		
		when(mockGA.getAuthority()).thenReturn("ROLE_CANDIDATE");
		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.fetchCandidate("1961", "1961", authorities);
		});
		
		verify(this.mockCandidateRepo).findCandidateById(anyLong());
		
	}

	/**
	* Tests happy path for Candidate requesting own Candidate profile
	* @throws Exception
	*/
	@Test
	void testFetchCandidate_candidate_own_candidate_found() {
		
		GrantedAuthority 				mockGA = mock(GrantedAuthority.class);
		Collection<GrantedAuthority> 	authorities = Set.of(mockGA);
		
		when(mockGA.getAuthority()).thenReturn("ROLE_CANDIDATE");
		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.of(Candidate.builder().build()));
		
		Candidate candidate = this.service.fetchCandidate("1961", "1961", authorities);
		
		assertNotNull(candidate);
		
	}
	
	/**
	* Tests happy path for Admin requesting a Candidate profile
	* @throws Exception
	*/
	@Test
	void testFetchCandidate_admin_candidate_found() {
		
		GrantedAuthority 				mockGA = mock(GrantedAuthority.class);
		Collection<GrantedAuthority> 	authorities = Set.of(mockGA);
		
		when(mockGA.getAuthority()).thenReturn("ROLE_ADMIN");
		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.of(Candidate.builder().build()));
		
		Candidate candidate = this.service.fetchCandidate("1961", "1961", authorities);
		
		assertNotNull(candidate);
		
	}
	
	/**
	* Tests happy path for Recruiter requesting a Candidate profile
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testFetchCandidate_recruiter_candidate_found() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn("user1");
		
		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.of(Candidate.builder().build()));
		when(this.mockCreditDao.getByRecruiterId(anyString())).thenReturn(Optional.of(RecruiterCredit.builder().paidSubscription(true).build()));
		
		Candidate candidate = this.service.fetchCandidate("1961", "1961", authorities);
		
		assertNotNull(candidate);
		
	}
	
	/**
	* Test candidate cannot update another canidates profile
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testUpdateCandidateProfile_other_candidate() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn("222");
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateCandidateProfile(CandidateUpdateRequest.builder().candidateId("111").build());
		});
		
		verify(this.mockExternalEventPublisher, never()).publishCandidateUpdateEvent(any());
		
	}

	/**
	* Test same email can exist for recruiters
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testUpdateCandidateProfile_recruiter_sameEmail() throws Exception{
		
		final String 		candidateId 			= "123";
		final FUNCTION		function				= FUNCTION.JAVA_DEV;
		final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
		final String 		city 					= "Den Haag";
		final String 		email					= "admin@arenella-ict.com";
		final String 		roleSought				= "Senior java Dev";
		final boolean 		available 				= true;
		final boolean 		flaggedAsUnavailable	= true;
		final FREELANCE 	freelance 				= FREELANCE.TRUE;
		final PERM 			perm 					= PERM.TRUE;
		final LocalDate 	lastAvailabilityCheck 	= LocalDate.of(1980, 12, 3);
		final LocalDate 	registerd 				= LocalDate.of(2021, 02, 20);
		final int 			yearsExperience 		= 21;
		final Set<String>	skills					= new LinkedHashSet<>();
		final Set<Language>	languages				= new LinkedHashSet<>();
		final String		skill					= "Java";
		final Language		language				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
		final String		firstName				= "kevin";
		final String 		surname					= "parkings";
		
		final FUNCTION		functionUpdt				= FUNCTION.CSHARP_DEV;
		final COUNTRY 		countryUpdt 				= COUNTRY.BELGIUM;
		final String 		cityUpdt 					= "Brussels";
		final String 		emailUpdt					= "kparkings@gmail.nl";
		final String 		roleSoughtUpdt				= "Senior C# Dev";
		final FREELANCE 	freelanceUpdt 				= FREELANCE.FALSE;
		final PERM 			permUpdt 					= PERM.FALSE;
		final int 			yearsExperienceUpdt 		= 22;
		final Set<Language>	languagesUpdt				= new LinkedHashSet<>();
		final Language		languageUpdt				= Language.builder().language(LANGUAGE.FRENCH).level(LEVEL.BASIC).build();
		final String		firstNameUpdt				= "kevin1";
		final String 		surnameUpdt					= "parkings1";
		
		ArgumentCaptor<CandidateUpdatedEvent> caEventArgCapt = ArgumentCaptor.forClass(CandidateUpdatedEvent.class);
		
		languages.add(language);
		languagesUpdt.add(languageUpdt);
		
		skills.add(skill);
		
		Candidate original = Candidate
				.builder()
					.available(available)
					.candidateId(candidateId)
					.city(city)
					.country(country)
					.email(email)
					.firstname(firstName)
					.flaggedAsUnavailable(flaggedAsUnavailable)
					.freelance(freelance)
					.functions(Set.of(function))
					.languages(languages)
					.lastAvailabilityCheck(lastAvailabilityCheck)
					.perm(perm)
					.registerd(registerd)
					.roleSought(roleSought)
					.skills(skills)
					.surname(surname)
					.yearsExperience(yearsExperience)
				.build();
		
		ArgumentCaptor<Candidate> candidateArgCapt = ArgumentCaptor.forClass(Candidate.class);
		
		CandidateUpdateRequest update = CandidateUpdateRequest
				.builder()
					.candidateId(candidateId)
					.city(cityUpdt)
					.country(countryUpdt)
					.email(emailUpdt)
					.firstname(firstNameUpdt)
					.freelance(freelanceUpdt)
					.functions(Set.of(functionUpdt))
					.languages(languagesUpdt)
					.perm(permUpdt)
					.roleSought(roleSoughtUpdt)
					.surname(surnameUpdt)
					.yearsExperience(yearsExperienceUpdt)
				.build();
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn(candidateId);
		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.of(original));
		doNothing().when(this.mockExternalEventPublisher).publishCandidateAccountUpdatedEvent(caEventArgCapt.capture());
		
		when(this.mockCandidateRepo.saveCandidate(candidateArgCapt.capture())).thenReturn(0L);
		
		this.service.updateCandidateProfile(update);
		
		Candidate persisted = candidateArgCapt.getValue();
		
		assertEquals(candidateId, 			persisted.getCandidateId());
		assertEquals(functionUpdt, 			persisted.getFunctions().toArray()[0]);
		assertEquals(countryUpdt, 			persisted.getCountry());
		assertEquals(cityUpdt, 				persisted.getCity());
		assertEquals(emailUpdt, 			persisted.getEmail());
		assertEquals(roleSoughtUpdt, 		persisted.getRoleSought());
		assertEquals(available, 			persisted.isAvailable());
		assertEquals(flaggedAsUnavailable, 	persisted.isFlaggedAsUnavailable());
		assertEquals(freelanceUpdt, 		persisted.isFreelance());
		assertEquals(permUpdt, 				persisted.isPerm());
		assertEquals(lastAvailabilityCheck, persisted.getLastAvailabilityCheckOn());
		assertEquals(registerd, 			persisted.getRegisteredOn());
		assertEquals(yearsExperienceUpdt, 	persisted.getYearsExperience());
		
		assertTrue(persisted.getSkills().contains(skill));
		persisted.getLanguages().stream().filter(l -> l.getLanguage() == languageUpdt.getLanguage()).findAny().orElseThrow();
		
		verify(this.mockExternalEventPublisher).publishCandidateAccountUpdatedEvent(any(CandidateUpdatedEvent.class));
		
		assertEquals(candidateId, 			persisted.getCandidateId());
		assertEquals(firstNameUpdt, 		persisted.getFirstname());
		assertEquals(surnameUpdt, 			persisted.getSurname());
		assertEquals(emailUpdt, 			persisted.getEmail());
		
		verify(this.mockExternalEventPublisher).publishCandidateUpdateEvent(any());
		
	}
	
	/**
	* Test can only update if candidate or recruiter
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testUpdateCandidateProfile_non_candidate_or_recruiter() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn("222");
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateCandidateProfile(CandidateUpdateRequest.builder().candidateId("222").build());
		});
		
		verify(this.mockExternalEventPublisher, never()).publishCandidateUpdateEvent(any());
		
	}

	/**
	* Tests case the candidate does not exist
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testUpdateCandidateProfile_unknown_candidate() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn("222");
		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateCandidateProfile(CandidateUpdateRequest.builder().candidateId("222").build());
		});
		
		verify(this.mockExternalEventPublisher, never()).publishCandidateUpdateEvent(any());
		
	}
	
	/**
	* Test that it is not possible to update the email to an emal already being used
	* by another candidate
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testUpdateCandidateProfile_update_email_to_already_in_use_email() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn("222");
		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.of(Candidate.builder().build()));
		when(this.mockCandidateRepo.emailInUseByOtherUser(anyString(), anyLong(), any())).thenReturn(true);
		
		assertThrows(IllegalStateException.class, () -> {
			this.service.updateCandidateProfile(CandidateUpdateRequest.builder().email("k@1.nl").candidateId("222").build());
		});
		
		verify(this.mockExternalEventPublisher, never()).publishCandidateUpdateEvent(any());
		
	}
	
	/**
	* Test happy path for the updating of an existing Candidate
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testUpdateCandidateProfile() throws Exception{
		
		final String 		candidateId 			= "123";
		final FUNCTION		function				= FUNCTION.JAVA_DEV;
		final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
		final String 		city 					= "Den Haag";
		final String 		email					= "admin@arenella-ict.com";
		final String 		roleSought				= "Senior java Dev";
		final boolean 		available 				= true;
		final boolean 		flaggedAsUnavailable	= true;
		final FREELANCE 	freelance 				= FREELANCE.TRUE;
		final PERM 			perm 					= PERM.TRUE;
		final LocalDate 	lastAvailabilityCheck 	= LocalDate.of(1980, 12, 3);
		final LocalDate 	registerd 				= LocalDate.of(2021, 02, 20);
		final int 			yearsExperience 		= 21;
		final Set<String>	skills					= new LinkedHashSet<>();
		final Set<Language>	languages				= new LinkedHashSet<>();
		final String		skill					= "Java";
		final Language		language				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
		final String		firstName				= "kevin";
		final String 		surname					= "parkings";
		
		final FUNCTION		functionUpdt				= FUNCTION.CSHARP_DEV;
		final COUNTRY 		countryUpdt 				= COUNTRY.BELGIUM;
		final String 		cityUpdt 					= "Brussels";
		final String 		emailUpdt					= "kparkings@gmail.nl";
		final String 		roleSoughtUpdt				= "Senior C# Dev";
		final FREELANCE 	freelanceUpdt 				= FREELANCE.FALSE;
		final PERM 			permUpdt 					= PERM.FALSE;
		final int 			yearsExperienceUpdt 		= 22;
		final Set<Language>	languagesUpdt				= new LinkedHashSet<>();
		final Language		languageUpdt				= Language.builder().language(LANGUAGE.FRENCH).level(LEVEL.BASIC).build();
		final String		firstNameUpdt				= "kevin1";
		final String 		surnameUpdt					= "parkings1";
		
		ArgumentCaptor<CandidateUpdatedEvent> caEventArgCapt = ArgumentCaptor.forClass(CandidateUpdatedEvent.class);
		
		languages.add(language);
		languagesUpdt.add(languageUpdt);
		
		skills.add(skill);
		
		Candidate original = Candidate
				.builder()
					.available(available)
					.candidateId(candidateId)
					.city(city)
					.country(country)
					.email(email)
					.firstname(firstName)
					.flaggedAsUnavailable(flaggedAsUnavailable)
					.freelance(freelance)
					.functions(Set.of(function))
					.languages(languages)
					.lastAvailabilityCheck(lastAvailabilityCheck)
					.perm(perm)
					.registerd(registerd)
					.roleSought(roleSought)
					.skills(skills)
					.surname(surname)
					.yearsExperience(yearsExperience)
				.build();
		
		ArgumentCaptor<Candidate> candidateArgCapt = ArgumentCaptor.forClass(Candidate.class);
		
		CandidateUpdateRequest update = CandidateUpdateRequest
				.builder()
					.candidateId(candidateId)
					.city(cityUpdt)
					.country(countryUpdt)
					.email(emailUpdt)
					.firstname(firstNameUpdt)
					.freelance(freelanceUpdt)
					.functions(Set.of(functionUpdt))
					.languages(languagesUpdt)
					.perm(permUpdt)
					.roleSought(roleSoughtUpdt)
					.surname(surnameUpdt)
					.yearsExperience(yearsExperienceUpdt)
				.build();
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn(candidateId);
		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.of(original));
		when(this.mockCandidateRepo.emailInUseByOtherUser(anyString(), anyLong(), any())).thenReturn(false);
		doNothing().when(this.mockExternalEventPublisher).publishCandidateAccountUpdatedEvent(caEventArgCapt.capture());
		
		when(this.mockCandidateRepo.saveCandidate(candidateArgCapt.capture())).thenReturn(0L);
		
		this.service.updateCandidateProfile(update);
		
		Candidate persisted = candidateArgCapt.getValue();
		
		assertEquals(candidateId, 			persisted.getCandidateId());
		assertEquals(functionUpdt, 			persisted.getFunctions().toArray()[0]);
		assertEquals(countryUpdt, 			persisted.getCountry());
		assertEquals(cityUpdt, 				persisted.getCity());
		assertEquals(emailUpdt, 			persisted.getEmail());
		assertEquals(roleSoughtUpdt, 		persisted.getRoleSought());
		assertEquals(available, 			persisted.isAvailable());
		assertEquals(flaggedAsUnavailable, 	persisted.isFlaggedAsUnavailable());
		assertEquals(freelanceUpdt, 		persisted.isFreelance());
		assertEquals(permUpdt, 				persisted.isPerm());
		assertEquals(lastAvailabilityCheck, persisted.getLastAvailabilityCheckOn());
		assertEquals(registerd, 			persisted.getRegisteredOn());
		assertEquals(yearsExperienceUpdt, 	persisted.getYearsExperience());
		
		assertTrue(persisted.getSkills().contains(skill));
		persisted.getLanguages().stream().filter(l -> l.getLanguage() == languageUpdt.getLanguage()).findAny().orElseThrow();
		
		verify(this.mockExternalEventPublisher).publishCandidateAccountUpdatedEvent(any(CandidateUpdatedEvent.class));
		
		assertEquals(candidateId, 			persisted.getCandidateId());
		assertEquals(firstNameUpdt, 		persisted.getFirstname());
		assertEquals(surnameUpdt, 			persisted.getSurname());
		assertEquals(emailUpdt, 			persisted.getEmail());
		
		verify(this.mockExternalEventPublisher).publishCandidateUpdateEvent(any());
	}
	
	/**
	* Test happy path for the updating of an existing Candidate
	* when the skills have been changed
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testUpdateCandidateProfile_skillsChanged() throws Exception{
		
		final String 		candidateId 			= "123";
		final FUNCTION		function				= FUNCTION.JAVA_DEV;
		final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
		final String 		city 					= "Den Haag";
		final String 		email					= "admin@arenella-ict.com";
		final String 		roleSought				= "Senior java Dev";
		final boolean 		available 				= true;
		final boolean 		flaggedAsUnavailable	= true;
		final FREELANCE 	freelance 				= FREELANCE.TRUE;
		final PERM 			perm 					= PERM.TRUE;
		final LocalDate 	lastAvailabilityCheck 	= LocalDate.of(1980, 12, 3);
		final LocalDate 	registerd 				= LocalDate.of(2021, 02, 20);
		final int 			yearsExperience 		= 21;
		final Set<String>	skills					= new LinkedHashSet<>();
		final Set<Language>	languages				= new LinkedHashSet<>();
		final String		skill					= "Java";
		final Language		language				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
		final String		firstName				= "kevin";
		final String 		surname					= "parkings";
		
		final FUNCTION		functionUpdt				= FUNCTION.CSHARP_DEV;
		final COUNTRY 		countryUpdt 				= COUNTRY.BELGIUM;
		final String 		cityUpdt 					= "Brussels";
		final String 		emailUpdt					= "kparkings@gmail.nl";
		final String 		roleSoughtUpdt				= "Senior C# Dev";
		final FREELANCE 	freelanceUpdt 				= FREELANCE.FALSE;
		final PERM 			permUpdt 					= PERM.FALSE;
		final int 			yearsExperienceUpdt 		= 22;
		final Set<Language>	languagesUpdt				= new LinkedHashSet<>();
		final Language		languageUpdt				= Language.builder().language(LANGUAGE.FRENCH).level(LEVEL.BASIC).build();
		final String		firstNameUpdt				= "kevin1";
		final String 		surnameUpdt					= "parkings1";
		
		ArgumentCaptor<CandidateUpdatedEvent> caEventArgCapt = ArgumentCaptor.forClass(CandidateUpdatedEvent.class);
		
		languages.add(language);
		languagesUpdt.add(languageUpdt);
		
		skills.add(skill);
		
		Candidate original = Candidate
				.builder()
					.available(available)
					.candidateId(candidateId)
					.city(city)
					.country(country)
					.email(email)
					.firstname(firstName)
					.flaggedAsUnavailable(flaggedAsUnavailable)
					.freelance(freelance)
					.functions(Set.of(function))
					.languages(languages)
					.lastAvailabilityCheck(lastAvailabilityCheck)
					.perm(perm)
					.registerd(registerd)
					.roleSought(roleSought)
					.skills(skills)
					.surname(surname)
					.yearsExperience(yearsExperience)
				.build();
		
		ArgumentCaptor<Candidate> candidateArgCapt = ArgumentCaptor.forClass(Candidate.class);
		
		CandidateUpdateRequest update = CandidateUpdateRequest
				.builder()
					.candidateId(candidateId)
					.city(cityUpdt)
					.country(countryUpdt)
					.email(emailUpdt)
					.firstname(firstNameUpdt)
					.freelance(freelanceUpdt)
					.functions(Set.of(functionUpdt))
					.languages(languagesUpdt)
					.perm(permUpdt)
					.roleSought(roleSoughtUpdt)
					.surname(surnameUpdt)
					.yearsExperience(yearsExperienceUpdt)
					.skills(Set.of("css"))
				.build();
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn(candidateId);
		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.of(original));
		when(this.mockCandidateRepo.emailInUseByOtherUser(anyString(), anyLong(), any())).thenReturn(false);
		doNothing().when(this.mockExternalEventPublisher).publishCandidateAccountUpdatedEvent(caEventArgCapt.capture());
		
		when(this.mockCandidateRepo.saveCandidate(candidateArgCapt.capture())).thenReturn(0L);
		
		this.service.updateCandidateProfile(update);
		
		Candidate persisted = candidateArgCapt.getValue();
		
		assertEquals(candidateId, 			persisted.getCandidateId());
		assertEquals(functionUpdt, 			persisted.getFunctions().toArray()[0]);
		assertEquals(countryUpdt, 			persisted.getCountry());
		assertEquals(cityUpdt, 				persisted.getCity());
		assertEquals(emailUpdt, 			persisted.getEmail());
		assertEquals(roleSoughtUpdt, 		persisted.getRoleSought());
		assertEquals(available, 			persisted.isAvailable());
		assertEquals(flaggedAsUnavailable, 	persisted.isFlaggedAsUnavailable());
		assertEquals(freelanceUpdt, 		persisted.isFreelance());
		assertEquals(permUpdt, 				persisted.isPerm());
		assertEquals(lastAvailabilityCheck, persisted.getLastAvailabilityCheckOn());
		assertEquals(registerd, 			persisted.getRegisteredOn());
		assertEquals(yearsExperienceUpdt, 	persisted.getYearsExperience());
		
		persisted.getLanguages().stream().filter(l -> l.getLanguage() == languageUpdt.getLanguage()).findAny().orElseThrow();
		
		verify(this.mockExternalEventPublisher).publishCandidateAccountUpdatedEvent(any(CandidateUpdatedEvent.class));
		
		assertEquals(candidateId, 			persisted.getCandidateId());
		assertEquals(firstNameUpdt, 		persisted.getFirstname());
		assertEquals(surnameUpdt, 			persisted.getSurname());
		assertEquals(emailUpdt, 			persisted.getEmail());
		assertEquals(1, persisted.getSkills().size());
		
		persisted.getSkills().stream().filter(s -> s.equals("css")).findAny().orElseThrow();
		
		verify(this.mockExternalEventPublisher).publishCandidateUpdateEvent(any());
		
	}
	
	/**
	* Tests deletion of Candidate by another Candidate results in Exception
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testDeleteProfile_canidate_deleting_other_candidate() {
		
		final String candidateId = "1234";
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn("8888");

		assertThrows(IllegalStateException.class, () -> {
			this.service.deleteCandidate(candidateId);
		});
		
	}
	
	/**
	* Tests deletion of Candidate that does not exist results in Exception
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testDeleteProfile_canidate_doesnt_exist() {
		
		final String candidateId = "1234";
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn(candidateId);

		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteCandidate(candidateId);
		});
		
	}
	
	/**
	* Tests deletion of Candidate by itself
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testDeleteProfile_happy_path_candidate() throws Exception{
		
		final String candidateId = "1234";
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn(candidateId);

		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.of(Candidate.builder().build()));
		
		this.service.deleteCandidate(candidateId);
		
		verify(this.mockCandidateRepo).deleteById(Long.valueOf(candidateId));
		verify(this.mockExternalEventPublisher).publishCandidateDeletedEvent(any(CandidateDeletedEvent.class));
		verify(this.mockkillUpdateStatDao).deleteById(Long.valueOf(candidateId));
		
		
	
	}
	
	/**
	* Tests deletion of Candidate by Administrator
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testDeleteProfile_happy_path_admin() throws Exception{
		
		final String candidateId = "1234";
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);

		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.of(Candidate.builder().build()));
		
		this.service.deleteCandidate(candidateId);
		
		verify(this.mockCandidateRepo).deleteById(Long.valueOf(candidateId));
		verify(this.mockExternalEventPublisher).publishCandidateDeletedEvent(any(CandidateDeletedEvent.class));
		
	}
	
	/**
	* Tests deletion of Candidate by Administrator
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testDeleteProfile_happy_path_recruiter() throws Exception{
		
		final String candidateId = "1234";
		final String recruiterId = "7777";
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn(recruiterId);

		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.of(Candidate.builder().ownerId(recruiterId).build()));
		
		this.service.deleteCandidate(candidateId);
		
		verify(this.mockCandidateRepo).deleteById(Long.valueOf(candidateId));
		verify(this.mockExternalEventPublisher).publishCandidateDeletedEvent(any(CandidateDeletedEvent.class));
		
	}

	/**
	* Tests deletion of Candidate by recruiter not owned by the recruiter
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testDeleteProfile_not_owned_by_recruiter() {
		
		final String candidateId = "1234";
		final String recruiterId = "7777";
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		when(mockAuthentication.getPrincipal()).thenReturn(recruiterId);

		when(this.mockCandidateRepo.findCandidateById(anyLong())).thenReturn(Optional.of(Candidate.builder().ownerId("999").build()));
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteCandidate(candidateId);
		});
		
	}
	
	/**
	* Tests if MultipartFile is empty, empty Photo returned
	* @throws Exception
	*/
	@Test
	void testConvertToPhoto() throws Exception{
		
		MultipartFile mpf = null;
		
		assertTrue(this.service.convertToPhoto(Optional.ofNullable(mpf)).isEmpty());
		
	}
	
	/**
	* Tests if Unsafe file Exception thrown
	* @throws Exception
	*/
	@Test
	void testConvertToPhoto_unsafeFile() throws Exception{
		
		MultipartFile mockMpf = mock(MultipartFile.class);
		
		when(mockMpf.getBytes()).thenReturn(new byte[]{});
		when(this.mockImageFileSecurityParser.isSafe(new byte[]{})).thenReturn(false);
		
		assertThrows(RuntimeException.class, () -> {
			this.service.convertToPhoto(Optional.ofNullable(mockMpf));
		});
		
	}
	
	/**
	* Tests Happy Path
	* @throws Exception
	*/
	@Test
	void testConvertToPhoto_happypath() throws Exception{
		
		MultipartFile mockMpf = mock(MultipartFile.class);
		
		when(mockMpf.getBytes()).thenReturn(new byte[]{});
		when(this.mockImageFileSecurityParser.isSafe(new byte[]{})).thenReturn(true);
		when(this.mockImageManipulator.toProfileImage(new byte[]{}, PHOTO_FORMAT.jpeg)).thenReturn(new byte[]{});
		
		Optional<Photo> photo = this.service.convertToPhoto(Optional.ofNullable(mockMpf));
		
		photo.orElseThrow();
		
	}
	
	/**
	* Tests sending of a contact request to a candidate. Request should be sent to the candidate 
	* themselves
	* @throws Exception
	*/
	@Test
	void testsendEmailToCandidate_standardCandidate() throws Exception{
		
		ArgumentCaptor<ContactRequestEvent> argCapt = ArgumentCaptor.forClass(ContactRequestEvent.class); 
		
		final String message 		= "aMessage";
		final String candidateId 	= "123";
		final String title 			= "aTitle";
		final String userId 		= "aUserId";
		
		Candidate candidate = Candidate.builder().candidateId("124").build();
		
		when(this.mockCandidateRepo.findCandidateById(Long.valueOf(candidateId))).thenReturn(Optional.of(candidate));
		doNothing().when(this.mockExternalEventPublisher).publishContactRequestEvent(argCapt.capture());
		
		this.service.sendEmailToCandidate(message, candidateId, title, userId);
		
		assertEquals(message, 		argCapt.getValue().getMessage());
		assertEquals(candidateId, 	argCapt.getValue().getRecipientId());
		assertEquals(title, 		argCapt.getValue().getTitle());
		assertEquals(userId, 		argCapt.getValue().getSenderRecruiterId());
		
	}
	
	/**
	* Tests sending of a contact request to a candidate owner by a recruiter. Request should be sent to 
	* the owner of the candidate and not the candidate themselves 
	* themselves
	* @throws Exception
	*/
	@Test
	void testsendEmailToCandidate_candidateOwnedByRecruiter() throws Exception{
		
		ArgumentCaptor<ContactRequestEvent> argCapt = ArgumentCaptor.forClass(ContactRequestEvent.class); 
		
		final String message 		= "aMessage";
		final String candidateId 	= "123";
		final String title 			= "aTitle";
		final String userId 		= "aUserId";
		final String ownerId		= "rec33";
		
		Candidate candidate = Candidate.builder().candidateId("124").firstname("kevn").surname("parkings").ownerId(ownerId).build();
		
		when(this.mockCandidateRepo.findCandidateById(Long.valueOf(candidateId))).thenReturn(Optional.of(candidate));
		doNothing().when(this.mockExternalEventPublisher).publishContactRequestEvent(argCapt.capture());
		
		this.service.sendEmailToCandidate(message, candidateId, title, userId);
		
		assertEquals(message, 		argCapt.getValue().getMessage());
		assertEquals(ownerId,	 	argCapt.getValue().getRecipientId());
		assertEquals(userId, 		argCapt.getValue().getSenderRecruiterId());
		
		assertEquals("Contact Request for " + candidate.getFirstname() + " " + candidate.getSurname(), argCapt.getValue().getTitle());
		
	}
	
	/**
	* Tests case credits left
	* @throws Exception
	*/
	@Test
	void testHasCreditsLeft_userNotFound() {
		
		final String userId = "kparkings";
		
		when(this.mockCreditDao.getByRecruiterId(userId)).thenReturn(Optional.empty());
		
		assertFalse(this.service.hasCreditsLeft(userId));
	}
	
	/**
	* Tests case credits left
	* @throws Exception
	*/
	@Test
	void testHasCreditsLeft_true() {
		
		final String userId = "kparkings";
		
		RecruiterCredit rc = RecruiterCredit.builder().credits(1).recruiterId(userId).build();
		
		when(this.mockCreditDao.getByRecruiterId(userId)).thenReturn(Optional.of(rc));
		
		assertTrue(this.service.hasCreditsLeft(userId));
	}
	
	/**
	* Tests case no credits left
	* @throws Exception
	*/
	@Test
	void testHasCreditsLeft_false() {
		
		final String userId = "kparkings";
		
		RecruiterCredit rc = RecruiterCredit.builder().credits(0).recruiterId(userId).build();
		
		when(this.mockCreditDao.getByRecruiterId(userId)).thenReturn(Optional.of(rc));
		
		assertFalse(this.service.hasCreditsLeft(userId));
	}
	
	/**
	* Happy path
	* Test case where paidSubscription value exists but is not being updated
	* @throws Exception
	*/
	@Test
	void testUpdateCreditsForUser() {
		
		final String 	userId 		= "kparkings";
		final int 		credits 	= 20;

		ArgumentCaptor<RecruiterCredit> argCapt = ArgumentCaptor.forClass(RecruiterCredit.class);
		
		RecruiterCredit recCredits = RecruiterCredit.builder().recruiterId(userId).credits(30).paidSubscription(true).build();
		
		when(this.mockCreditDao.getByRecruiterId(userId)).thenReturn(Optional.of(recCredits));
		doNothing().when(this.mockCreditDao).persist(argCapt.capture());
		
		this.service.updateCreditsForUser(userId, credits);
	
		verify(this.mockCreditDao).persist(any());
		
		assertEquals(credits, argCapt.getValue().getCredits());
		assertTrue(argCapt.getValue().hasPaidSubscription());
	}
	
	/**
	* Test case where paidSubscription value is provided
	* @throws Exception
	*/
	@Test
	void testUpdateCreditsForUser_paidSubscriptin_specified() {
		
		final String 	userId 		= "kparkings";
		final int 		credits 	= 20;

		ArgumentCaptor<RecruiterCredit> argCapt = ArgumentCaptor.forClass(RecruiterCredit.class);
		
		RecruiterCredit recCredits = RecruiterCredit.builder().recruiterId(userId).credits(30).paidSubscription(true).build();
		
		when(this.mockCreditDao.getByRecruiterId(userId)).thenReturn(Optional.of(recCredits));
		doNothing().when(this.mockCreditDao).persist(argCapt.capture());
		
		this.service.updateCreditsForUser(userId, credits, Optional.of(false));
	
		verify(this.mockCreditDao).persist(any());
		
		assertEquals(credits, argCapt.getValue().getCredits());
		assertFalse(argCapt.getValue().hasPaidSubscription());
	}
	
	/**
	* If no credits for user does nothing
	* @throws Exception
	*/
	@Test
	void testUpdateCreditsForUser_unknownRecruiter() {
		
		final String 	userId 		= "kparkings";
		final int 		credits 	= 20;

		when(this.mockCreditDao.getByRecruiterId(userId)).thenReturn(Optional.empty());
		
		this.service.updateCreditsForUser(userId, credits);
	
		verify(this.mockCreditDao, never()).persist(any());
		
	}
	
	/**
	* Tests case no credits
	* @throws Exception
	*/
	@Test
	void testGetCreditCountForUser_unknownUser() {
		
		when(this.mockCreditDao.getByRecruiterId(anyString())).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.getCreditCountForUser("rec22");
		});
		
	}
	
	/**
	* Tests case no credits
	* @throws Exception
	*/
	@Test
	void testGetCreditCountForUser() {
		
		final int credits = 5;
		
		RecruiterCredit rc = RecruiterCredit.builder().credits(credits).build();
		
		when(this.mockCreditDao.getByRecruiterId(anyString())).thenReturn(Optional.of(rc));
		
		assertEquals(credits, this.service.getCreditCountForUser("rec22"));
		
	}
	
	/**
	* Test extraction of filters
	* @throws Exception
	*/
	@Test
	void testExtractFiltersFromText() {
	
		when(this.mockDocumentFilterExtractionUtil.extractFilters("")).thenReturn(CandidateExtractedFilters.builder().build());
		
		CandidateExtractedFilters filters = this.service.extractFiltersFromText("");
	
		assertNotNull(filters);
		
	}
	
	/**
	* Tests retrieval of Skills pending validation
	* @throws Exception
	*/
	@Test
	void testFetchPendingCandidateSkills() {
		
		this.service.fetchPendingCandidateSkills();
		
		verify(this.mockSkillDao).getValidationPendingSkills();
		
	}
	
	/**
	* Test that existing skills are sent to be updated
	* @throws Exception
	*/
	@SuppressWarnings("unchecked")
	@Test
	void testUpdateCandidateSkills() {
		
		final CandidateSkill java 	= CandidateSkill.builder().skill("java").validationStatus(VALIDATION_STATUS.ACCEPTED).build();
		final CandidateSkill nutmeg = CandidateSkill.builder().skill("nutmeg").validationStatus(VALIDATION_STATUS.REJECTED).build();
		final CandidateSkill dog 	= CandidateSkill.builder().skill("dog").validationStatus(VALIDATION_STATUS.REJECTED).build();
		
		ArgumentCaptor<Set<CandidateSkill>> skillCaptor = ArgumentCaptor.forClass(Set.class);
		
		Set<CandidateSkill> skills = Set.of(java, nutmeg, dog);
		
		doNothing().when(this.mockSkillDao).persistExistingSkills(skillCaptor.capture());
		when(this.mockSkillDao.existsById("java")).thenReturn(true);
		when(this.mockSkillDao.existsById("nutmeg")).thenReturn(true);
		when(this.mockSkillDao.existsById("dog")).thenReturn(true);
		
		this.service.updateCandidateSkills(skills);
		
		Set<CandidateSkill> results = skillCaptor.getValue();
		
		results.stream().filter(s -> s == java).findAny().orElseThrow();
		results.stream().filter(s -> s == nutmeg).findAny().orElseThrow();
		results.stream().filter(s -> s == dog).findAny().orElseThrow();

	}

	/**
	* Test that only existing skills are sent to be updated. If a non existing Skill 
	* is sent to be updated an exception should be thrown and no updates should take place
	* @throws Exception
	*/
	@Test
	void testUpdateCandidateSkills_unknownSkill() {
		
		final CandidateSkill java 	= CandidateSkill.builder().skill("java").validationStatus(VALIDATION_STATUS.ACCEPTED).build();
		final CandidateSkill nutmeg = CandidateSkill.builder().skill("nutmeg").validationStatus(VALIDATION_STATUS.REJECTED).build();
		final CandidateSkill dog 	= CandidateSkill.builder().skill("dog").validationStatus(VALIDATION_STATUS.REJECTED).build();
		
		Set<CandidateSkill> skills = Set.of(java, nutmeg, dog);
		
		when(this.mockSkillDao.existsById(anyString())).thenReturn(false);
		
		assertThrows(IllegalStateException.class, () -> {
			this.service.updateCandidateSkills(skills);
			
		});
		
		verify(this.mockSkillDao, never()).persistExistingSkills(anySet());

	}
	
	/**
	* Tests case user has a paid subscription
	* @throws Exception
	*/
	@Test
	void testHasPaidSubscription_true() {
		
		when(this.mockCreditDao.getByRecruiterId(anyString())).thenReturn(Optional.of(RecruiterCredit.builder().paidSubscription(true).build()));
		
		assertTrue(this.service.hasPaidSubscription("userId"));
		
	}
	
	/**
	* Tests case user does not have a paid subscription
	* @throws Exception
	*/
	@Test
	void testHasPaidSubscription_false() {
		
		when(this.mockCreditDao.getByRecruiterId(anyString())).thenReturn(Optional.of(RecruiterCredit.builder().paidSubscription(false).build()));
		
		assertFalse(this.service.hasPaidSubscription("userId"));
		
	}
	
	/**
	* Test fetch of count - available
	* @throws Exception
	*/
	@Test
	void testGetAccountByAvailable_available() {
		
		this.service.getCountByAvailable(true);
		
		verify(this.mockCandidateRepo).getCountByAvailable(true);
		
	}
	
	/**
	* Test fetch of count - unavailable
	* @throws Exception
	*/
	@Test
	void testGetAccountByAvailable_unavailable() {
		
		this.service.getCountByAvailable(true);
		
		verify(this.mockCandidateRepo).getCountByAvailable(true);
		
	}
	
	/**
	* Tests Exception thrown if more than one recruiter with the same email
	* address exist
	* @throws Exception
	*/
	@Test
	void testResetPassword_multipleMatchingRecruiters() throws Exception{
	
		final String emailAddress = "admin@arenella-ict.com";
		
		when(this.mockCandidateRepo.findCandidates(any(), any(), eq(0), eq(2))).thenReturn(Set.of(Candidate.builder().build(), Candidate.builder().build()));
	
		Assertions.assertThrows(IllegalArgumentException.class, () ->{
			this.service.resetPassword(emailAddress);
		});
		
	}
	
	/**
	* Tests Exception thrown if no0 recruiter with the email
	* address exist
	* @throws Exception
	*/
	@Test
	void testResetPassword_noMatchingRecruiters() throws Exception{
		
		final String emailAddress = "admin@arenella-ict.com";
		
		when(this.mockCandidateRepo.findCandidates(any(), any(), eq(0), eq(2))).thenReturn(Set.of());
		
		Assertions.assertThrows(IllegalArgumentException.class, () ->{
			this.service.resetPassword(emailAddress);
		});
	}

	/**
	* Tests Happy Path
	* 	- Sends Event to notify Auth service of Password change
	* 	- Sends Email to notify Recruiter of new Password
	* @throws Exception
	*/
	@Test
	void testResetPassword() throws Exception{
		
		final String 	emailAddress 	= "admin@arenella-ict.com";
		final String 	firstname 		= "kevin";
		final String	userId 			= "1234";
		
		ArgumentCaptor<CandidatePasswordUpdatedEvent> 	pwdUpdtArgCapt 	= ArgumentCaptor.forClass(CandidatePasswordUpdatedEvent.class);
		ArgumentCaptor<RequestSendEmailCommand>			emailArgCapt	= ArgumentCaptor.forClass(RequestSendEmailCommand.class);
		
		doNothing().when(this.mockExternalEventPublisher).publishCandidatePasswordUpdated(pwdUpdtArgCapt.capture());
		doNothing().when(this.mockExternalEventPublisher).publishSendEmailCommand(emailArgCapt.capture());
		when(this.mockCandidateRepo.findCandidates(any(), any(), eq(0), eq(2))).thenReturn(Set.of(Candidate.builder().firstname(firstname).candidateId(userId).build()));
		
		this.service.resetPassword(emailAddress);
		
		assertEquals(userId, pwdUpdtArgCapt.getValue().getCandidateId());
		assertEquals(EmailTopic.PASSWORD_RESET, emailArgCapt.getValue().getTopic());
		
		Map<String,Object> model = emailArgCapt.getValue().getModel();
		
		assertEquals(userId, model.get("userId"));
		assertEquals(firstname, model.get("firstname"));
		assertNotNull(model.get("password"));
		
	}
	
	/**
	* Tests deletion of Candidates owned by a given Recruiter
	* @throws Exception
	*/
	@Test
	void testDeleteCandidatesForOwnedByRecruiter() throws Exception{
		
		final String recruiterId = "rec1";
		
		when(this.mockCandidateRepo.findCandidates(any(), any(), eq(0), eq(750))).thenReturn(Set.of(Candidate.builder().build(), Candidate.builder().build()));
		
		this.service.deleteCandidatesForOwnedByRecruiter(recruiterId);
		
		//Cant verift x 2 Candidate deletes called as service is not a mock
	}
	
	/**
	* Tests removal of a Recruiter's saved Candidates
	* @throws Exception
	*/
	@Test
	void testDeleteSavedCandidatesForRecruiter() {
		
		final String recId = "aRecId";
		
		when(this.mockSavedCandidateDao.fetchSavedCandidatesByUserId(recId)).thenReturn(Set.of(SavedCandidate.builder().candidateId(1).build(),SavedCandidate.builder().candidateId(2).build()));
		
		this.service.deleteSavedCandidatesForRecruiter(recId);
		
		verify(this.mockSavedCandidateDao, times(2)).delete(eq(recId), anyLong());
		
	}
	
	/**
	* Tests Recruiter Credit deletion
	* @throws Exception
	*/
	@Test
	void testDeleteCreditsForRecruiter() {
		
		final String recruiterId = "aRecId1";
		
		this.service.deleteCreditsForRecruiter(recruiterId);
		
		verify(this.mockCreditDao).deleteById(recruiterId);
		
	}
	
	/**
	* Tests Recruiter Credit deletion
	* @throws Exception
	*/
	@Test
	void testDeleteContactForRecruiter() {
		
		final String recruiterId = "aRecId1";
		
		this.service.deleteContactForRecruiter(recruiterId);
		
		verify(this.mockContactDao).deleteByRecruiterId(recruiterId);
		
	}

	/**
	* Tests case where Search already exists and is being 
	* persisted for the second time
	*/
	@Test
	void testCreateSavedCandidateSearchRequestExist() {
		
		final UUID 					searchId 		= UUID.randomUUID(); 
		final SavedCandidateSearch 	existingSearch 	= 
				SavedCandidateSearch
				.builder()
					.id(searchId)
					.userId("rec2")
				.build();
		
		when(this.mockSavedCandidateSearchEntityDao.existsById(searchId)).thenReturn(true);
		
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.createSavedCandidateSearchRequest(existingSearch);
		});
		
		assertEquals(CandidateServiceImpl.ERR_MSG_SAVED_SEARCH_CREATE_TWICE, ex.getMessage());
		
	}
	
	/**
	* Tests case where Search already exists and is being 
	* persisted for the second time
	*/
	@Test
	void testCreateSavedCandidateSearchRequestDoesntExist() {
		
		final UUID 					searchId 		= UUID.randomUUID(); 
		final SavedCandidateSearch 	existingSearch 	= 
				SavedCandidateSearch
				.builder()
					.id(searchId)
					.userId("rec2")
				.build();
		
		when(this.mockSavedCandidateSearchEntityDao.existsById(searchId)).thenReturn(false);
		
		
		this.service.createSavedCandidateSearchRequest(existingSearch);
		
		verify(this.mockSavedCandidateSearchEntityDao).saveSearch(any());
		
	}
	
	/**
	* Tests case where Search doesn't already exists and is being 
	* persisted for the first time
	*/
	@Test
	void testUpdateSavedCandidateSearchRequestDoesntExist() {
		
		final String 				userId = "rec1";
		final SavedCandidateSearch 	search = 
				SavedCandidateSearch
				.builder()
					.id(UUID.randomUUID())
					.userId(userId)
				.build();
		
		when(this.mockSavedCandidateSearchEntityDao.fetchSavedCandidateSearchById(any())).thenReturn(Optional.empty());
		
		this.service.updateSavedCandidateSearchRequest(search);
		
		verify(this.mockSavedCandidateSearchEntityDao).saveSearch(any(SavedCandidateSearch.class));
		
	}
	
	/**
	* Tests case where Search already exists and is being updated
	*/
	@Test
	void testUPdateSavedCandidateSearchRequestExists() {
		
		final UUID 					searchId 		= UUID.randomUUID(); 
		final String 				userId 			= "rec1";
		final SavedCandidateSearch 	existingSearch 	= 
				SavedCandidateSearch
				.builder()
					.id(searchId)
					.userId(userId)
				.build();
		final SavedCandidateSearch 	search 			= 
				SavedCandidateSearch
				.builder()
					.id(searchId)
					.userId(userId)
				.build();
		
		when(this.mockSavedCandidateSearchEntityDao.fetchSavedCandidateSearchById(any())).thenReturn(Optional.of(existingSearch));
		
		this.service.updateSavedCandidateSearchRequest(search);
		
		verify(this.mockSavedCandidateSearchEntityDao).saveSearch(any(SavedCandidateSearch.class));
		
	}
	
	/**
	* Tests case where an attempt is made to update an existing Search
	* that belongs to another user
	*/
	@Test
	void testUpdateSavedCandidateSearchRequestBelongsToAnotherUser() {
		
		final UUID 					searchId 		= UUID.randomUUID(); 
		final String 				userId 			= "rec1";
		final SavedCandidateSearch 	existingSearch 	= 
				SavedCandidateSearch
				.builder()
					.id(searchId)
					.userId("rec2")
				.build();
		final SavedCandidateSearch 	search 			= 
				SavedCandidateSearch
				.builder()
					.id(searchId)
					.userId(userId)
				.build();
		
		when(this.mockSavedCandidateSearchEntityDao.fetchSavedCandidateSearchById(any())).thenReturn(Optional.of(existingSearch));
		
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateSavedCandidateSearchRequest(search);
		});
		
		assertEquals(CandidateServiceImpl.ERR_MSG_SAVED_SEARCH_SAVE_OTHER_USERS, ex.getMessage());
		
	}
	
	/**
	* Tests fetching of saved searches for the current authenticated user
	*/
	@Test
	void testFetchSavedCandidatrSearches() {
		
		final String 				userId = "rec1";
		final SavedCandidateSearch 	search = SavedCandidateSearch.builder().build();
		
		when(this.mockSavedCandidateSearchEntityDao.fetchSavedCandidateSearchs(userId)).thenReturn(Set.of(search));
		
		Set<SavedCandidateSearch> results = this.service.fetchSavedCandidateSearches(userId);
		
		assertTrue(results.contains(search));
		
	}
	
	/**
	* Tests deletion happy path
	*/
	@Test
	void testDeleteSavedCandidateSearchSuccess() {
		
		final UUID 					searchId 		= UUID.randomUUID(); 
		final String 				userId 			= "rec1";
		final SavedCandidateSearch 	existingSearch 	= 
				SavedCandidateSearch
				.builder()
					.id(searchId)
					.userId(userId)
				.build();
		
		when(this.mockSavedCandidateSearchEntityDao.fetchSavedCandidateSearchById(any())).thenReturn(Optional.of(existingSearch));
		
		this.service.deleteSavedCandidateSearch(searchId, userId);
		
		verify(this.mockSavedCandidateSearchEntityDao).deleteById(searchId);
		
		
	}
	
	/**
	* Tests failure path where the Search to delete does not 
	* exist
	*/
	@Test
	void testDeleteSavedCandidateSearchDoesNotExists() {
		
		final UUID 					searchId 		= UUID.randomUUID(); 
		final String 				userId 			= "rec1";
		
		when(this.mockSavedCandidateSearchEntityDao.fetchSavedCandidateSearchById(any())).thenReturn(Optional.empty());
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteSavedCandidateSearch(searchId, userId);
		});
		
		assertEquals(CandidateServiceImpl.ERR_MSG_SAVED_SEARCH_DELETE_NON_EXISTENT, ex.getMessage());
	}
	
	/**
	* Tests failure path where the Search to delete belongs to 
	* another user
	*/
	@Test
	void testDeleteSavedCandidateSearchBelongsToOtherUser() {
		
		final UUID 					searchId 		= UUID.randomUUID(); 
		final String 				userId 			= "rec1";
		final SavedCandidateSearch 	search 			= 
				SavedCandidateSearch
				.builder()
					.id(searchId)
					.userId("rec2")
				.build();
		
		when(this.mockSavedCandidateSearchEntityDao.fetchSavedCandidateSearchById(any())).thenReturn(Optional.of(search));
		
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteSavedCandidateSearch(searchId, userId);
		});
		
		assertEquals(CandidateServiceImpl.ERR_MSG_SAVED_SEARCH_DELETE_OTHER_USERS, ex.getMessage());
		
	}
	
	/**
	* Test all Saved searched belonging to a user are deleted
	*/
	@Test
	void testDeleteSavedCandidateSearchesForUser() {
		
		final String userId = "rec1";
		final SavedCandidateSearch s1 = SavedCandidateSearch.builder().userId(userId).id(UUID.randomUUID()).build();
		final SavedCandidateSearch s2 = SavedCandidateSearch.builder().userId(userId).id(UUID.randomUUID()).build();
		
		when(this.mockSavedCandidateSearchEntityDao.fetchSavedCandidateSearchs(userId)).thenReturn(Set.of(s1,s2));
		
		this.service.deleteSavedCandidateSearchesForUser(userId);
		
		verify(this.mockSavedCandidateSearchEntityDao).deleteById(s1.getId());
		verify(this.mockSavedCandidateSearchEntityDao).deleteById(s2.getId());
		
		verify(this.mockSavedCandidateSearchEntityDao, times(2)).deleteById(any());
		
	}
	
}