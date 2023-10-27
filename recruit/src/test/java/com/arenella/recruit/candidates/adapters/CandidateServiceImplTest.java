package com.arenella.recruit.candidates.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.adapters.actions.GrantCreditCommand;
import com.arenella.recruit.adapters.events.CandidateAccountCreatedEvent;
import com.arenella.recruit.adapters.events.CandidateDeletedEvent;
import com.arenella.recruit.adapters.events.CandidateUpdatedEvent;
import com.arenella.recruit.adapters.events.ContactRequestEvent;
import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.CANDIDATE_TYPE;
import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.candidates.beans.Contact.CONTACT_TYPE;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.CandidateSearchAlert;
import com.arenella.recruit.candidates.beans.CandidateUpdateRequest;
import com.arenella.recruit.candidates.beans.Contact;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.beans.PendingCandidate;
import com.arenella.recruit.candidates.beans.RecruiterCredit;
import com.arenella.recruit.candidates.controllers.CandidateController.CANDIDATE_UPDATE_ACTIONS;
import com.arenella.recruit.candidates.controllers.CandidateValidationException;
import com.arenella.recruit.candidates.controllers.SavedCandidate;
import com.arenella.recruit.candidates.dao.CandidateDao;
import com.arenella.recruit.candidates.dao.CandidateRecruiterCreditDao;
import com.arenella.recruit.candidates.dao.CandidateSearchAlertDao;
import com.arenella.recruit.candidates.dao.CandidateSkillsDao;
import com.arenella.recruit.candidates.dao.PendingCandidateDao;
import com.arenella.recruit.candidates.dao.RecruiterContactDao;
import com.arenella.recruit.candidates.dao.SavedCandidateDao;
import com.arenella.recruit.candidates.entities.CandidateEntity;
import com.arenella.recruit.candidates.entities.PendingCandidateEntity;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;
import com.arenella.recruit.candidates.services.CandidateServiceImpl;
import com.arenella.recruit.candidates.services.CandidateStatisticsService;
import com.arenella.recruit.candidates.utils.CandidateFunctionExtractorImpl;
import com.arenella.recruit.candidates.utils.CandidateImageFileSecurityParser;
import com.arenella.recruit.candidates.utils.CandidateImageManipulator;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil;
import com.arenella.recruit.candidates.utils.SkillsSynonymsUtil;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;

/**
* Unit tests for the CandidateServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class CandidateServiceImplTest {

	@Mock
	private CandidateDao 							mockCandidateDao;
	
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
	
	@Spy
	private CandidateFunctionExtractorImpl			mockCandidateFunctionExtractor;
	
	@InjectMocks
	private CandidateServiceImpl 					service 					= new CandidateServiceImpl();
	
	/**
	* Sets up test environment 
	*/
	@BeforeEach
	public void init() throws Exception{
		SecurityContextHolder.setContext(mockSecurityContext);
	}
	
	/**
	* Tests exception is thrown if Email already used for the Candidate 
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testPersistCandidate_emailAlreadyExists() {

		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(this.mockCandidateDao.emailInUse(Mockito.anyString())).thenReturn(true);
		
		assertThrows(CandidateValidationException.class, () -> {
			this.service.persistCandidate(Candidate.builder().email("kparkings@gmail.com").build());
		});
		
	}
	
	/**
	* Tests OwnerId is added if the User adding the Candidate is not an admin user 
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testPersistCandidate_NonAdminUserOwnerId() {

		ArgumentCaptor<CandidateEntity> argCapt = ArgumentCaptor.forClass(CandidateEntity.class);
		
		Contact contact = Contact.builder().contactType(CONTACT_TYPE.RECRUITER).email("kp@test.it").firstname("kevin").surname("parkings").userId("kp1").build();
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
		
		Mockito.when(mockCandidateDao.save(argCapt.capture())).thenReturn(CandidateEntity.builder().candidateId("123").build());
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("kp1");
		
		Mockito.when(this.mockContactDao.getByTypeAndId(Mockito.any(), Mockito.any())).thenReturn(Optional.of(contact));
		
		this.service.persistCandidate(Candidate.builder().candidateId("123").firstname("kevin").email("kparkings@gmail.com").build());
		
		Assertions.assertEquals(CANDIDATE_TYPE.MARKETPLACE_CANDIDATE, argCapt.getValue().getCandidateType());
		Assertions.assertEquals("kp1", argCapt.getValue().getOwnerId().get());
	}
	
	/**
	* Tests OwnerId is not added if the User adding the Candidate is not an admin user 
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testPersistCandidate_adminUserOwnerId() {

		ArgumentCaptor<CandidateEntity> argCapt = ArgumentCaptor.forClass(CandidateEntity.class);
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		Mockito.when(mockCandidateDao.save(argCapt.capture())).thenReturn(CandidateEntity.builder().candidateId("123").build());
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		
		this.service.persistCandidate(Candidate.builder().candidateId("123").firstname("kevin").email("kparkings@gmail.com").build());
		
		Assertions.assertEquals(CANDIDATE_TYPE.CANDIDATE, argCapt.getValue().getCandidateType());
		Assertions.assertTrue(argCapt.getValue().getOwnerId().isEmpty());
	}
	
	/**
	* Tests exception is thrown if Email not used for the Pending Candidate 
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testPersistCandidate() {
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		Mockito.when(this.mockCandidateDao.emailInUse(Mockito.anyString())).thenReturn(false);
		Mockito.when(this.mockCandidateDao.save(Mockito.any())).thenReturn(CandidateEntity.builder().candidateId("1000").build());

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);

		this.service.persistCandidate(Candidate
				.builder()
					.candidateId("1000")
					.email("kparkings@gmail.com")
					.firstname("kevin")
				.build());
		
		Mockito.verify(this.mockExternalEventPublisher).publishCandidateAccountCreatedEvent(Mockito.any(CandidateAccountCreatedEvent.class));
		Mockito.verify(this.mockExternalEventPublisher).publishSendEmailCommand(Mockito.any(RequestSendEmailCommand.class));
		Mockito.verify(this.mockExternalEventPublisher).publishCandidateCreatedEvent(Mockito.any(CandidateCreatedEvent.class));
		
	}
	
	/**
	* If Recruiter created the Candidate then there should be no User created and no email with the 
	* Users login details sent
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testPersistCandidate_recruiter() {
		
		Contact contact = Contact.builder().firstname("kevin").surname("parkings").email("kparkings@gmail.com").build();
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		//Mockito.when(this.mockCandidateDao.emailInUse(Mockito.anyString())).thenReturn(false);
		Mockito.when(this.mockCandidateDao.save(Mockito.any())).thenReturn(CandidateEntity.builder().candidateId("1000").build());

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("kp1");
		
		Mockito.when(this.mockContactDao.getByTypeAndId(CONTACT_TYPE.RECRUITER, "kp1")).thenReturn(Optional.of(contact)); 

		this.service.persistCandidate(Candidate
				.builder()
					.candidateId("1000")
					.email("kparkings@gmail.com")
					.firstname("kevin")
				.build());
		
		Mockito.verify(this.mockExternalEventPublisher, Mockito.times(0)).publishCandidateAccountCreatedEvent(Mockito.any(CandidateAccountCreatedEvent.class));
		Mockito.verify(this.mockExternalEventPublisher, Mockito.times(0)).publishSendEmailCommand(Mockito.any(RequestSendEmailCommand.class));
		Mockito.verify(this.mockExternalEventPublisher, Mockito.times(0)).publishCandidateCreatedEvent(Mockito.any(CandidateCreatedEvent.class));
		
	}
	/**
	* Tests exception is thrown if Email not used for the Pending Candidate 
	*/
	@Test
	public void testPersistPendingCandidate_emailAlreadyExists() {
		
		Mockito.when(this.mockPendingCandidateDao.emailInUse(Mockito.anyString())).thenReturn(true);
		
		assertThrows(CandidateValidationException.class, () -> {
			this.service.persistPendingCandidate(PendingCandidate.builder().email("kparkings@gmail.com").build());
		});
		
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
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("recruiter1");

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
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("recruiter1");
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
		
		final String recruiterId = "recruiter1";
		
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
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("recruiter1");
		
		UUID id = UUID.randomUUID();
		
		Mockito.when(this.mockSkillAlertDao.getchAlertById(id)).thenReturn(Optional.of(CandidateSearchAlert.builder().recruiterId("recruiter1").build()));
		
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
		
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("recruiter1");
		
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
	
	/**
	* Tests Exception is thrown if User is a Candidate trying to view another 
	* Candidates profile
	* @throws Exception
	*/
	@Test
	public void testFetchCandidate_candidate_other_candidate() throws Exception{
		
		GrantedAuthority 				mockGA = Mockito.mock(GrantedAuthority.class);
		Collection<GrantedAuthority> 	authorities = Set.of(mockGA);
		
		Mockito.when(mockGA.getAuthority()).thenReturn("ROLE_CANDIDATE");
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.fetchCandidate("1960", "1961", authorities);
		});
		
	}
	
	/**
	* Tests Exception is thrown if Candidate not found
	* @throws Exception
	*/
	@Test
	public void testFetchCandidate_candidate_own_candidate_not_found() throws Exception{
		
		GrantedAuthority 				mockGA = Mockito.mock(GrantedAuthority.class);
		Collection<GrantedAuthority> 	authorities = Set.of(mockGA);
		
		Mockito.when(mockGA.getAuthority()).thenReturn("ROLE_CANDIDATE");
		Mockito.when(this.mockCandidateDao.findCandidateById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.fetchCandidate("1961", "1961", authorities);
		});
		
		Mockito.verify(this.mockCandidateDao).findCandidateById(Mockito.anyLong());
		
	}

	/**
	* Tests happy path for Candidate requesting own Candidate profile
	* @throws Exception
	*/
	@Test
	public void testFetchCandidate_candidate_own_candidate_found() throws Exception{
		
		GrantedAuthority 				mockGA = Mockito.mock(GrantedAuthority.class);
		Collection<GrantedAuthority> 	authorities = Set.of(mockGA);
		
		Mockito.when(mockGA.getAuthority()).thenReturn("ROLE_CANDIDATE");
		Mockito.when(this.mockCandidateDao.findCandidateById(Mockito.anyLong())).thenReturn(Optional.of(Candidate.builder().build()));
		
		Candidate candidate = this.service.fetchCandidate("1961", "1961", authorities);
		
		assertNotNull(candidate);
		
	}
	
	/**
	* Tests happy path for Admin requesting a Candidate profile
	* @throws Exception
	*/
	@Test
	public void testFetchCandidate_admin_candidate_found() throws Exception{
		
		GrantedAuthority 				mockGA = Mockito.mock(GrantedAuthority.class);
		Collection<GrantedAuthority> 	authorities = Set.of(mockGA);
		
		Mockito.when(mockGA.getAuthority()).thenReturn("ROLE_ADMIN");
		Mockito.when(this.mockCandidateDao.findCandidateById(Mockito.anyLong())).thenReturn(Optional.of(Candidate.builder().build()));
		
		Candidate candidate = this.service.fetchCandidate("1961", "1961", authorities);
		
		assertNotNull(candidate);
		
	}
	
	/**
	* Tests happy path for Recruiter requesting a Candidate profile
	* @throws Exception
	*/
	@Test
	public void testFetchCandidate_recruiter_candidate_found() throws Exception{
		
		GrantedAuthority 				mockGA = Mockito.mock(GrantedAuthority.class);
		Collection<GrantedAuthority> 	authorities = Set.of(mockGA);
		
		Mockito.when(mockGA.getAuthority()).thenReturn("ROLE_RECRUITER");
		Mockito.when(this.mockCandidateDao.findCandidateById(Mockito.anyLong())).thenReturn(Optional.of(Candidate.builder().build()));
		
		Candidate candidate = this.service.fetchCandidate("1961", "1961", authorities);
		
		assertNotNull(candidate);
		
	}
	
	/**
	* Test candidate cannot update another canidates profile
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testUpdateCandidateProfile_other_candidate() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("222");
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateCandidateProfile(CandidateUpdateRequest.builder().candidateId("111").build());
		});
		
	}

	/**
	* Test same email can exist for recruiters
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testUpdateCandidateProfile_recruiter_sameEmail() throws Exception{
		
		final String 		candidateId 			= "123";
		final FUNCTION		function				= FUNCTION.JAVA_DEV;
		final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
		final String 		city 					= "Den Haag";
		final String 		email					= "kparkings@gmail.com";
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
					.function(function)
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
					.function(functionUpdt)
					.languages(languagesUpdt)
					.perm(permUpdt)
					.roleSought(roleSoughtUpdt)
					.surname(surnameUpdt)
					.yearsExperience(yearsExperienceUpdt)
				.build();
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(candidateId);
		Mockito.when(this.mockCandidateDao.findCandidateById(Mockito.anyLong())).thenReturn(Optional.of(original));
		//Mockito.when(this.mockCandidateDao.emailInUseByOtherUser(Mockito.anyString(), Mockito.anyLong())).thenReturn(true);
		Mockito.doNothing().when(this.mockExternalEventPublisher).publishCandidateAccountUpdatedEvent(caEventArgCapt.capture());
		
		Mockito.doNothing().when(this.mockCandidateDao).saveCandidate(candidateArgCapt.capture());
		
		this.service.updateCandidateProfile(update);
		
		Candidate persisted = candidateArgCapt.getValue();
		
		assertEquals(candidateId, 			persisted.getCandidateId());
		assertEquals(functionUpdt, 			persisted.getFunction());
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
		
		Mockito.verify(this.mockExternalEventPublisher).publishCandidateAccountUpdatedEvent(Mockito.any(CandidateUpdatedEvent.class));
		
		assertEquals(candidateId, 			persisted.getCandidateId());
		assertEquals(firstNameUpdt, 		persisted.getFirstname());
		assertEquals(surnameUpdt, 			persisted.getSurname());
		assertEquals(emailUpdt, 			persisted.getEmail());
		
	}
	
	/**
	* Test can only update if candidate or recruiter
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testUpdateCandidateProfile_non_candidate_or_recruiter() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("222");
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateCandidateProfile(CandidateUpdateRequest.builder().candidateId("222").build());
		});
		
	}

	/**
	* Tests case the candidate does not exist
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testUpdateCandidateProfile_unknown_candidate() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("222");
		Mockito.when(this.mockCandidateDao.findCandidateById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateCandidateProfile(CandidateUpdateRequest.builder().candidateId("222").build());
		});
		
	}
	
	/**
	* Test that it is not possible to update the email to an emal already being used
	* by another candidate
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testUpdateCandidateProfile_update_email_to_already_in_use_email() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("222");
		Mockito.when(this.mockCandidateDao.findCandidateById(Mockito.anyLong())).thenReturn(Optional.of(Candidate.builder().build()));
		Mockito.when(this.mockCandidateDao.emailInUseByOtherUser(Mockito.anyString(), Mockito.anyLong())).thenReturn(true);
		
		assertThrows(IllegalStateException.class, () -> {
			this.service.updateCandidateProfile(CandidateUpdateRequest.builder().email("k@1.nl").candidateId("222").build());
		});
		
	}
	
	/**
	* Test happy path for the updating of an existing Candidate
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testUpdateCandidateProfile() throws Exception{
		
		final String 		candidateId 			= "123";
		final FUNCTION		function				= FUNCTION.JAVA_DEV;
		final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
		final String 		city 					= "Den Haag";
		final String 		email					= "kparkings@gmail.com";
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
					.function(function)
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
					.function(functionUpdt)
					.languages(languagesUpdt)
					.perm(permUpdt)
					.roleSought(roleSoughtUpdt)
					.surname(surnameUpdt)
					.yearsExperience(yearsExperienceUpdt)
				.build();
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(candidateId);
		Mockito.when(this.mockCandidateDao.findCandidateById(Mockito.anyLong())).thenReturn(Optional.of(original));
		Mockito.when(this.mockCandidateDao.emailInUseByOtherUser(Mockito.anyString(), Mockito.anyLong())).thenReturn(false);
		Mockito.doNothing().when(this.mockExternalEventPublisher).publishCandidateAccountUpdatedEvent(caEventArgCapt.capture());
		
		Mockito.doNothing().when(this.mockCandidateDao).saveCandidate(candidateArgCapt.capture());
		
		this.service.updateCandidateProfile(update);
		
		Candidate persisted = candidateArgCapt.getValue();
		
		assertEquals(candidateId, 			persisted.getCandidateId());
		assertEquals(functionUpdt, 			persisted.getFunction());
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
		
		Mockito.verify(this.mockExternalEventPublisher).publishCandidateAccountUpdatedEvent(Mockito.any(CandidateUpdatedEvent.class));
		
		assertEquals(candidateId, 			persisted.getCandidateId());
		assertEquals(firstNameUpdt, 		persisted.getFirstname());
		assertEquals(surnameUpdt, 			persisted.getSurname());
		assertEquals(emailUpdt, 			persisted.getEmail());
		
	}
	
	/**
	* Test happy path for the updating of an existing Candidate
	* when the skills have been changed
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testUpdateCandidateProfile_skillsChanged() throws Exception{
		
		final String 		candidateId 			= "123";
		final FUNCTION		function				= FUNCTION.JAVA_DEV;
		final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
		final String 		city 					= "Den Haag";
		final String 		email					= "kparkings@gmail.com";
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
					.function(function)
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
					.function(functionUpdt)
					.languages(languagesUpdt)
					.perm(permUpdt)
					.roleSought(roleSoughtUpdt)
					.surname(surnameUpdt)
					.yearsExperience(yearsExperienceUpdt)
					.skills(Set.of("css"))
				.build();
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(candidateId);
		Mockito.when(this.mockCandidateDao.findCandidateById(Mockito.anyLong())).thenReturn(Optional.of(original));
		Mockito.when(this.mockCandidateDao.emailInUseByOtherUser(Mockito.anyString(), Mockito.anyLong())).thenReturn(false);
		Mockito.doNothing().when(this.mockExternalEventPublisher).publishCandidateAccountUpdatedEvent(caEventArgCapt.capture());
		
		Mockito.doNothing().when(this.mockCandidateDao).saveCandidate(candidateArgCapt.capture());
		
		this.service.updateCandidateProfile(update);
		
		Candidate persisted = candidateArgCapt.getValue();
		
		assertEquals(candidateId, 			persisted.getCandidateId());
		assertEquals(functionUpdt, 			persisted.getFunction());
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
		
		Mockito.verify(this.mockExternalEventPublisher).publishCandidateAccountUpdatedEvent(Mockito.any(CandidateUpdatedEvent.class));
		
		assertEquals(candidateId, 			persisted.getCandidateId());
		assertEquals(firstNameUpdt, 		persisted.getFirstname());
		assertEquals(surnameUpdt, 			persisted.getSurname());
		assertEquals(emailUpdt, 			persisted.getEmail());
		assertEquals(1, persisted.getSkills().size());
		
		persisted.getSkills().stream().filter(s -> s.equals("css")).findAny().orElseThrow();
		
	}
	
	/**
	* Tests deletion of Candidate by another Candidate results in Exception
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testDeleteProfile_canidate_deleting_other_candidate() throws Exception{
		
		final String candidateId = "1234";
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn("8888");

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
	public void testDeleteProfile_canidate_doesnt_exist() throws Exception{
		
		final String candidateId = "1234";
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(candidateId);

		Mockito.when(this.mockCandidateDao.findCandidateById(Mockito.anyLong())).thenReturn(Optional.empty());
		
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
	public void testDeleteProfile_happy_path_candidate() throws Exception{
		
		final String candidateId = "1234";
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(candidateId);

		Mockito.when(this.mockCandidateDao.findCandidateById(Mockito.anyLong())).thenReturn(Optional.of(Candidate.builder().build()));
		
		this.service.deleteCandidate(candidateId);
		
		Mockito.verify(this.mockCandidateDao).deleteById(Long.valueOf(candidateId));
		Mockito.verify(this.mockSavedCandidateDao).deleteByCandidateId(Long.valueOf(candidateId));
		Mockito.verify(this.mockExternalEventPublisher).publishCandidateDeletedEvent(Mockito.any(CandidateDeletedEvent.class));
	
	}
	
	/**
	* Tests deletion of Candidate by Administrator
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testDeleteProfile_happy_path_admin() throws Exception{
		
		final String candidateId = "1234";
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);

		Mockito.when(this.mockCandidateDao.findCandidateById(Mockito.anyLong())).thenReturn(Optional.of(Candidate.builder().build()));
		
		this.service.deleteCandidate(candidateId);
		
		Mockito.verify(this.mockCandidateDao).deleteById(Long.valueOf(candidateId));
		Mockito.verify(this.mockSavedCandidateDao).deleteByCandidateId(Long.valueOf(candidateId));
		Mockito.verify(this.mockExternalEventPublisher).publishCandidateDeletedEvent(Mockito.any(CandidateDeletedEvent.class));
		
	}
	
	/**
	* Tests deletion of Candidate by Administrator
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testDeleteProfile_happy_path_recruiter() throws Exception{
		
		final String candidateId = "1234";
		final String recruiterId = "7777";
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(recruiterId);

		Mockito.when(this.mockCandidateDao.findCandidateById(Mockito.anyLong())).thenReturn(Optional.of(Candidate.builder().ownerId(recruiterId).build()));
		
		this.service.deleteCandidate(candidateId);
		
		Mockito.verify(this.mockCandidateDao).deleteById(Long.valueOf(candidateId));
		Mockito.verify(this.mockSavedCandidateDao).deleteByCandidateId(Long.valueOf(candidateId));
		Mockito.verify(this.mockExternalEventPublisher).publishCandidateDeletedEvent(Mockito.any(CandidateDeletedEvent.class));
		
	}

	/**
	* Tests deletion of Candidate by recruiter not owned by the recruiter
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testDeleteProfile_not_owned_by_recruiter() throws Exception{
		
		final String candidateId = "1234";
		final String recruiterId = "7777";
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(recruiterId);

		Mockito.when(this.mockCandidateDao.findCandidateById(Mockito.anyLong())).thenReturn(Optional.of(Candidate.builder().ownerId("999").build()));
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteCandidate(candidateId);
		});
		
	}
	
	/**
	* Tests if MultipartFile is empty, empty Photo returned
	* @throws Exception
	*/
	@Test
	public void testConvertToPhoto() throws Exception{
		
		MultipartFile mpf = null;
		
		assertTrue(this.service.convertToPhoto(Optional.ofNullable(mpf)).isEmpty());
		
	}
	
	/**
	* Tests if Unsafe file Exception thrown
	* @throws Exception
	*/
	@Test
	public void testConvertToPhoto_unsafeFile() throws Exception{
		
		MultipartFile mockMpf = Mockito.mock(MultipartFile.class);
		
		Mockito.when(mockMpf.getBytes()).thenReturn(new byte[]{});
		Mockito.when(this.mockImageFileSecurityParser.isSafe(new byte[]{})).thenReturn(false);
		
		assertThrows(RuntimeException.class, () -> {
			this.service.convertToPhoto(Optional.ofNullable(mockMpf));
		});
		
	}
	
	/**
	* Tests Happy Path
	* @throws Exception
	*/
	@Test
	public void testConvertToPhoto_happypath() throws Exception{
		
		MultipartFile mockMpf = Mockito.mock(MultipartFile.class);
		
		Mockito.when(mockMpf.getBytes()).thenReturn(new byte[]{});
		Mockito.when(this.mockImageFileSecurityParser.isSafe(new byte[]{})).thenReturn(true);
		Mockito.when(this.mockImageManipulator.toProfileImage(new byte[]{}, PHOTO_FORMAT.jpeg)).thenReturn(new byte[]{});
		
		Optional<Photo> photo = this.service.convertToPhoto(Optional.ofNullable(mockMpf));
		
		photo.orElseThrow();
		
	}
	
	/**
	* Tests sending of a contact request to a candidate. Request should be sent to the candidate 
	* themselves
	* @throws Exception
	*/
	@Test
	public void testsendEmailToCandidate_standardCandidate() throws Exception{
		
		ArgumentCaptor<ContactRequestEvent> argCapt = ArgumentCaptor.forClass(ContactRequestEvent.class); 
		
		final String message 		= "aMessage";
		final String candidateId 	= "123";
		final String title 			= "aTitle";
		final String userId 		= "aUserId";
		
		Candidate candidate = Candidate.builder().candidateId("124").build();
		
		Mockito.when(this.mockCandidateDao.findCandidateById(Long.valueOf(candidateId))).thenReturn(Optional.of(candidate));
		Mockito.doNothing().when(this.mockExternalEventPublisher).publishContactRequestEvent(argCapt.capture());
		
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
	public void testsendEmailToCandidate_candidateOwnedByRecruiter() throws Exception{
		
		ArgumentCaptor<ContactRequestEvent> argCapt = ArgumentCaptor.forClass(ContactRequestEvent.class); 
		
		final String message 		= "aMessage";
		final String candidateId 	= "123";
		final String title 			= "aTitle";
		final String userId 		= "aUserId";
		final String ownerId		= "rec33";
		
		Candidate candidate = Candidate.builder().candidateId("124").firstname("kevn").surname("parkings").ownerId(ownerId).build();
		
		Mockito.when(this.mockCandidateDao.findCandidateById(Long.valueOf(candidateId))).thenReturn(Optional.of(candidate));
		Mockito.doNothing().when(this.mockExternalEventPublisher).publishContactRequestEvent(argCapt.capture());
		
		this.service.sendEmailToCandidate(message, candidateId, title, userId);
		
		assertEquals(message, 		argCapt.getValue().getMessage());
		assertEquals(ownerId,	 	argCapt.getValue().getRecipientId());
		assertEquals(userId, 		argCapt.getValue().getSenderRecruiterId());
		
		assertEquals("Contact Request for " + candidate.getFirstname() + " " + candidate.getSurname(), argCapt.getValue().getTitle());
		
	}
	
	/**
	* Test updating the RecruiterCredits
	* @throws Exception
	*/
	@Test
	public void testUpdateCredits() throws Exception{
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Set<RecruiterCredit>> argCapt = ArgumentCaptor.forClass(Set.class);
		
		RecruiterCredit rc1 = RecruiterCredit.builder().recruiterId("recruiter1").credits(2).build();
		RecruiterCredit rc2 = RecruiterCredit.builder().recruiterId("recruiter1").credits(5).build();
		
		Mockito.when(this.mockCreditDao.fetchRecruiterCredits()).thenReturn(Set.of(rc1,rc2));
		Mockito.doNothing().when(this.mockCreditDao).saveAll(argCapt.capture());
		
		this.service.updateCredits(new GrantCreditCommand());
		
		if (argCapt.getValue().stream().filter(rc -> rc.getCredits() != RecruiterCredit.DEFAULT_CREDITS).findAny().isPresent()) {
			throw new RuntimeException();
		}
		
	}
	
}