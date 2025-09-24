package com.arenella.recruit.recruiters.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.arenella.recruit.adapters.events.RecruiterDeletedEvent;
import com.arenella.recruit.adapters.events.RecruiterPasswordUpdatedEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.recruiters.beans.RecruiterCredit;
import com.arenella.recruit.recruiters.adapters.RecruitersExternalEventPublisher;
import com.arenella.recruit.recruiters.beans.CreditBasedSubscription;
import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.beans.PaidPeriodRecruiterSubscription;
import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.Recruiter.language;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.INVOICE_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;
import com.arenella.recruit.recruiters.dao.OpenPositionDao;
import com.arenella.recruit.recruiters.dao.RecruiterCreditDao;
import com.arenella.recruit.recruiters.dao.RecruiterDao;
import com.arenella.recruit.recruiters.dao.RecruiterProfileDao;
import com.arenella.recruit.recruiters.entities.RecruiterEntity;
import com.arenella.recruit.recruiters.entities.RecruiterSubscriptionEntity;
import com.arenella.recruit.recruiters.utils.RecruiterSubscriptionActionHandler;
import com.arenella.recruit.recruiters.utils.RecruiterSubscriptionFactory;

/**
* Unit tests for the RecruiterService
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class RecruiterServiceImplTest {

	@InjectMocks
	private RecruiterService 					service 	= new RecruiterServiceImpl();
	
	@Mock
	private RecruiterDao 						mockDao;
	
	@Mock
	private RecruiterSubscriptionFactory 		mockRecruiterSubscriptionFactory;
	
	@Mock
	private RecruiterSubscriptionActionHandler 	mockRecruiterSubscriptionActionHandler;
	
	@Mock
	private RecruitersExternalEventPublisher	mockExternEventPublisher;

	@Mock
	private RecruiterCreditDao					mockCreditDao;
	
	@Mock
	private RecruiterProfileDao					mockRecruiterProfileDao;
	
	@Mock
	private OpenPositionDao						mockOpenPositionDao;
	
	private static final String 			USER_ID 				= "kparkings";
	private static final LocalDate 			ACCOUNT_CREATED 		= LocalDate.of(2021, 10, 13);
	private static final String 			COMPANY_NAME 			= "arenella-ict";
	private static final String 			EMAIL 					= "admin@arenella-ict.com";
	private static final String 			FIRST_NAME 				= "kevin";
	private static final language 			LANG 					= language.DUTCH;
	private static final String 			SURNAME 				= "parkings";
	private static final SecurityContext 	MOCK_SECURITY_CONTEXT 	= mock(SecurityContext.class);
	private static final Authentication 	MOCK_AUTHENTICATION 	= mock(Authentication.class);
	
	/**
	* Sets up security context to make user Admin
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setupSecurityContextForAdmin() {
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(authorities);
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn(USER_ID);

	}
	
	/**
	* Tests happy path for updating an existing Recruiter
	* @throws Exception
	*/
	@Test
	void testUpdateRecruiter() throws Exception{
		
		final String 	companyNameUpdt 		= "arenella-ictnew";
		final String 	emailUpdt 				= "admin@arenella-ict.comnew";
		final String 	firstnameUpdt 			= "kevinnew";
		final language 	langUpdt 				= language.FRENCH;
		final String 	surnameUpdt 			= "parkingsnew";
		
		this.setupSecurityContextForAdmin();
		
		ArgumentCaptor<RecruiterEntity>			argCaptEntity 			= ArgumentCaptor.forClass(RecruiterEntity.class);
		ArgumentCaptor<RecruiterUpdatedEvent>	argRecruiterUpdtEvent 	= ArgumentCaptor.forClass(RecruiterUpdatedEvent.class);
		
		RecruiterEntity recruiterEntity = RecruiterEntity
											.builder()
												.active(true)
												.companyName(COMPANY_NAME)
												.email(EMAIL)
												.firstName(FIRST_NAME)
												.language(LANG)
												.surname(SURNAME)
												.userId(USER_ID)
												.accountCreated(ACCOUNT_CREATED)
											.build();
									
		Mockito.when(this.mockDao.findById(USER_ID)).thenReturn(Optional.of(recruiterEntity));
		Mockito.when(this.mockDao.save(argCaptEntity.capture())).thenReturn(null);
		Mockito.doNothing().when(this.mockExternEventPublisher).publishRecruiterAccountUpdatedEvent(argRecruiterUpdtEvent.capture());
		
		Recruiter recruiter = Recruiter
								.builder()
									.active(false)
									.companyName(companyNameUpdt)
									.email(emailUpdt)
									.firstName(firstnameUpdt)
									.language(langUpdt)
									.surname(surnameUpdt)
									.userId(USER_ID)
								.build();
		
		this.service.updateRecruiter(recruiter);
		
		RecruiterEntity entity = argCaptEntity.getValue();
		
		assertEquals(companyNameUpdt, 	entity.getCompanyName());
		assertEquals(emailUpdt, 		entity.getEmail());
		assertEquals(firstnameUpdt, 	entity.getFirstName());
		assertEquals(langUpdt, 			entity.getLanguage());
		assertEquals(surnameUpdt, 		entity.getSurname());
		assertEquals(USER_ID, 		entity.getUserId());
		assertTrue(entity.isActive());
		assertNotNull(entity.getAccountCreated());
		
		Mockito.verify(this.mockDao).save(recruiterEntity);
		
		assertEquals(emailUpdt, argRecruiterUpdtEvent.getValue().getEmail());
		
	}
	
	/**
	* Tests if an exception occurred on the DB transaction commit that a compensation
	* event is sent to revert the changes that would have been sent when the original
	* update event was sent
	* @throws Exception
	*/
	@Test
	void testUpdateRecruiter_compensationEvent() throws Exception{
		
		final String 	companyNameUpdt 		= "arenella-ictnew";
		final String 	emailUpdt 				= "admin@arenella-ict.comnew";
		final String 	firstnameUpdt 			= "kevinnew";
		final language 	langUpdt 				= language.FRENCH;
		final String 	surnameUpdt 			= "parkingsnew";
		
		this.setupSecurityContextForAdmin();
		
		ArgumentCaptor<RecruiterEntity>			argCaptEntity 			= ArgumentCaptor.forClass(RecruiterEntity.class);
		ArgumentCaptor<RecruiterUpdatedEvent>	argRecruiterUpdtEvent 	= ArgumentCaptor.forClass(RecruiterUpdatedEvent.class);
		
		RecruiterEntity recruiterEntity = RecruiterEntity
											.builder()
												.active(true)
												.companyName(COMPANY_NAME)
												.email(EMAIL)
												.firstName(FIRST_NAME)
												.language(LANG)
												.surname(SURNAME)
												.userId(USER_ID)
												.accountCreated(ACCOUNT_CREATED)
											.build();
									
		Mockito.when(this.mockDao.findById(USER_ID)).thenReturn(Optional.of(recruiterEntity));
		
		Mockito.when(this.mockDao.save(argCaptEntity.capture())).thenThrow(new RuntimeException());
		
		Mockito.doNothing().when(this.mockExternEventPublisher).publishRecruiterAccountUpdatedEvent(argRecruiterUpdtEvent.capture());
		
		Recruiter recruiter = Recruiter
								.builder()
									.active(false)
									.companyName(companyNameUpdt)
									.email(emailUpdt)
									.firstName(firstnameUpdt)
									.language(langUpdt)
									.surname(surnameUpdt)
									.userId(USER_ID)
								.build();
		
		this.service.updateRecruiter(recruiter);
		
		RecruiterEntity entity = argCaptEntity.getValue();
		
		assertEquals(companyNameUpdt, 	entity.getCompanyName());
		assertEquals(emailUpdt, 		entity.getEmail());
		assertEquals(firstnameUpdt, 	entity.getFirstName());
		assertEquals(langUpdt, 			entity.getLanguage());
		assertEquals(surnameUpdt, 		entity.getSurname());
		assertEquals(USER_ID, 			entity.getUserId());
		assertTrue(entity.isActive());
		assertNotNull(entity.getAccountCreated());
		
		Mockito.verify(this.mockDao).save(recruiterEntity);
		
		assertEquals(EMAIL, argRecruiterUpdtEvent.getValue().getEmail());
		
	}
	
	/**
	* Tests that if the logged in user is not the owner of the Recuiter 
	* being updated and is not an Admin user an exception will be thrown
	* @throws Exception
	*/
	@Test
	void testUpdateRecruiter_notAdminOrOwnRecruiter() {
		
		final String 	companyNameUpdt 		= "arenella-ictnew";
		final String 	emailUpdt 				= "admin@arenella-ict.comnew";
		final String 	firstnameUpdt 			= "kevinnew";
		final language 	langUpdt 				= language.FRENCH;
		final String 	surnameUpdt 			= "parkingsnew";
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(Set.of());
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn("NotOwnUser");
		
		RecruiterEntity recruiterEntity = RecruiterEntity
											.builder()
												.active(true)
												.companyName(COMPANY_NAME)
												.email(EMAIL)
												.firstName(FIRST_NAME)
												.language(LANG)
												.surname(SURNAME)
												.userId(USER_ID)
												.accountCreated(ACCOUNT_CREATED)
											.build();
									
		Mockito.when(this.mockDao.findById(USER_ID)).thenReturn(Optional.of(recruiterEntity));
		
		Recruiter recruiter = Recruiter
								.builder()
									.active(false)
									.companyName(companyNameUpdt)
									.email(emailUpdt)
									.firstName(firstnameUpdt)
									.language(langUpdt)
									.surname(surnameUpdt)
									.userId(USER_ID)
								.build();
		
		
		Assertions.assertThrows(IllegalAccessException.class, ()->{
			this.service.updateRecruiter(recruiter);
		});
			
	}
	
	/**
	* Tests failure case where Recruiter to update does not exist
	* @throws Exception
	*/
	@Test
	void testUpdateRecruiter_unknownRecruiter() {
		
		this.setupSecurityContextForAdmin();
		
		Recruiter recruiter = Recruiter
								.builder()
									.active(false)
									.companyName(COMPANY_NAME)
									.email(EMAIL)
									.firstName(FIRST_NAME)
									.language(LANG)
									.surname(SURNAME)
									.userId(USER_ID)
								.build();
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateRecruiter(recruiter);
		});
		
	}
	
	/**
	* Tests Fetch of all recruiters
	* @throws Exception
	*/
	@Test
	void testFetchRecruiters() {
		
		Set<RecruiterEntity> recruiterEntities = Set.of(RecruiterEntity
																.builder()
																	.active(true)
																	.companyName(COMPANY_NAME)
																	.email(EMAIL)
																	.firstName(FIRST_NAME)
																	.language(LANG)
																	.surname(SURNAME)
																	.userId(USER_ID)
																	.accountCreated(ACCOUNT_CREATED)
																.build());
		
		Mockito.when(this.mockDao.findAll()).thenReturn(recruiterEntities);
		
		Set<Recruiter> recruiters = this.service.fetchRecruiters();
		
		Recruiter recruiter = recruiters.stream().findFirst().get();
		
		assertEquals(COMPANY_NAME, 	recruiter.getCompanyName());
		assertEquals(EMAIL, 		recruiter.getEmail());
		assertEquals(FIRST_NAME, 	recruiter.getFirstName());
		assertEquals(LANG, 			recruiter.getLanguage());
		assertEquals(SURNAME, 		recruiter.getSurname());
		assertEquals(USER_ID, 			recruiter.getUserId());
		assertTrue(recruiter.isActive());
		assertNotNull(recruiter.getAccountCreated());
		
	}

	/**
	* Tests happy path for Fetching a specific Recruiter
	* @throws Exception
	*/
	@Test
	void testFetchRecruiter() throws Exception{
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(Set.of());
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn(USER_ID);
		
		RecruiterEntity recruiterEntity = RecruiterEntity
													.builder()
														.active(true)
														.companyName(COMPANY_NAME)
														.email(EMAIL)
														.firstName(FIRST_NAME)
														.language(LANG)
														.surname(SURNAME)
														.userId(USER_ID)
														.accountCreated(ACCOUNT_CREATED)
													.build();

		Mockito.when(this.mockDao.findByUserIdIgnoreCase(USER_ID)).thenReturn(Optional.of(recruiterEntity));
		
		Recruiter recruiter = this.service.fetchRecruiter(USER_ID);
		
		assertEquals(COMPANY_NAME, 	recruiter.getCompanyName());
		assertEquals(EMAIL, 		recruiter.getEmail());
		assertEquals(FIRST_NAME, 	recruiter.getFirstName());
		assertEquals(LANG, 			recruiter.getLanguage());
		assertEquals(SURNAME, 		recruiter.getSurname());
		assertEquals(USER_ID, 		recruiter.getUserId());
		assertTrue(recruiter.isActive());
		assertNotNull(recruiter.getAccountCreated());

	}
	
	/**
	* Tests Failure path where fetch of unknown Recruiter is performed
	* @throws Exception
	*/
	@Test
	void testFetchRecruiter_unknownRecruiter() {
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(Set.of());
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn("unknownUser");
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.fetchRecruiter("unknownUser");
		});
	}
	
	/**
	* Tests Failure path where a Recruiter attempts to access another Recruiters details
	* @throws Exception
	*/
	@Test
	void testFetchRecruiter_recruiterAccessingAnotherRecruiter() {
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(Set.of());
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn("NotThisRecruiter");
		
		assertThrows(IllegalAccessException.class, () -> {
			this.service.fetchRecruiter("aUser");
		});
	}
	
	/**
	* Tests that the Admin Users can fetch a Recruiter
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testFetchRecruiter_asAdmin() throws Exception {
	
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(authorities);
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn(USER_ID);
		
		RecruiterEntity recruiterEntity = RecruiterEntity
													.builder()
														.active(true)
														.companyName(COMPANY_NAME)
														.email(EMAIL)
														.firstName(FIRST_NAME)
														.language(LANG)
														.surname(SURNAME)
														.userId(USER_ID)
														.accountCreated(ACCOUNT_CREATED)
													.build();

		Mockito.when(this.mockDao.findByUserIdIgnoreCase(USER_ID)).thenReturn(Optional.of(recruiterEntity));
		
		Recruiter recruiter = this.service.fetchRecruiter(USER_ID);
		
		assertEquals(COMPANY_NAME, 	recruiter.getCompanyName());
		assertEquals(EMAIL, 		recruiter.getEmail());
		assertEquals(FIRST_NAME, 	recruiter.getFirstName());
		assertEquals(LANG, 			recruiter.getLanguage());
		assertEquals(SURNAME, 		recruiter.getSurname());
		assertEquals(USER_ID, 		recruiter.getUserId());
		assertTrue(recruiter.isActive());
		assertNotNull(recruiter.getAccountCreated());
		
	}
	
	/**
	* Tests an Exception is thrown if the recruiter is unknown
	* @throws Exception
	*/
	@Test
	void testPerformSubscriptionAction_unknownRecruiter() {
		
		final String 				recruiterId 		= "kparkings";
		final UUID 					subscriptionId 		= UUID.randomUUID();
		final subscription_action 	action 				= subscription_action.ACTIVATE_SUBSCRIPTION;
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(Set.of());
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.performSubscriptionAction(recruiterId, subscriptionId, action);
		});
		
	}
	
	/**
	* Tests an Exception is thrown if the subscription is unknown
	* @throws Exception
	*/
	@Test
	void testPerformSubscriptionAction_unknownSubscription() {
		
		final String 				recruiterId 		= "kparkings";
		final UUID 					subscriptionId 		= UUID.randomUUID();
		final subscription_action 	action 				= subscription_action.ACTIVATE_SUBSCRIPTION;
		final Recruiter				recruiter			= Recruiter.builder().build();
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(Set.of());
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn(recruiterId);
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.performSubscriptionAction(recruiterId, subscriptionId, action);
		});
		
	}
	
	/**
	* Tests an Exception is thrown if after the Action is performed the Recruiter has 
	* more than one current subscription
	* @throws Exception
	*/
	@Test
	void testPerformSubscriptionAction_multipleCurrentSubscriptions() throws Exception {
		
		final String 				recruiterId 		= "kparkings";
		final UUID 					subscriptionId1 	= UUID.randomUUID();
		final UUID 					subscriptionId2 	= UUID.randomUUID();
		final subscription_action 	action 				= subscription_action.ACTIVATE_SUBSCRIPTION;
		final Recruiter				recruiter			= Recruiter
															.builder()
																.subscriptions(Set.of(CreditBasedSubscription.builder().subscriptionId(subscriptionId1).currentSubscription(true).build(),
																					  CreditBasedSubscription.builder().subscriptionId(subscriptionId2).currentSubscription(true).build()
																				)
																				)
															.build();
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(Set.of());
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		Mockito.when(this.mockRecruiterSubscriptionFactory.getActionHandlerByType(Mockito.any())).thenReturn(mockRecruiterSubscriptionActionHandler);
		Mockito.when(mockRecruiterSubscriptionActionHandler.performAction(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
		
		assertThrows(IllegalStateException.class, () -> {
			this.service.performSubscriptionAction(recruiterId, subscriptionId1, action);
		});
		
	}
	
	/**
	* Tests the happy path
	* @throws Exception
	*/
	@Test
	void testPerformSubscriptionAction() throws Exception {
		
		final String 				recruiterId 		= "kparkings";
		final UUID 					subscriptionId1 	= UUID.randomUUID();
		final UUID 					subscriptionId2 	= UUID.randomUUID();
		final subscription_action 	action 				= subscription_action.ACTIVATE_SUBSCRIPTION;
		final Recruiter				recruiter			= Recruiter
															.builder()
																.subscriptions(Set.of(CreditBasedSubscription.builder().subscriptionId(subscriptionId1).currentSubscription(true).build(),
																					  CreditBasedSubscription.builder().subscriptionId(subscriptionId2).currentSubscription(false).build()
																				)
																				)
															.build();
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(Set.of());
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		Mockito.when(this.mockRecruiterSubscriptionFactory.getActionHandlerByType(Mockito.any())).thenReturn(mockRecruiterSubscriptionActionHandler);
		Mockito.when(mockRecruiterSubscriptionActionHandler.performAction(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
		
		this.service.performSubscriptionAction(recruiterId, subscriptionId1, action);
		
		Mockito.verify(this.mockDao).save(Mockito.any(RecruiterEntity.class));
		
	}
	
	/**
	* Test method can be called by admin users
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testPerformSubscriptionAction_admin() throws Exception {
		
		final String 				recruiterId 		= "kparkings";
		final UUID 					subscriptionId1 	= UUID.randomUUID();
		final UUID 					subscriptionId2 	= UUID.randomUUID();
		final subscription_action 	action 				= subscription_action.ACTIVATE_SUBSCRIPTION;
		final Recruiter				recruiter			= Recruiter
															.builder()
																.subscriptions(Set.of(CreditBasedSubscription.builder().subscriptionId(subscriptionId1).currentSubscription(true).build(),
																					  CreditBasedSubscription.builder().subscriptionId(subscriptionId2).currentSubscription(false).build()
																				)
																				)
															.build();
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(authorities);
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn(USER_ID);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		Mockito.when(this.mockRecruiterSubscriptionFactory.getActionHandlerByType(Mockito.any())).thenReturn(mockRecruiterSubscriptionActionHandler);
		Mockito.when(mockRecruiterSubscriptionActionHandler.performAction(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
		
		this.service.performSubscriptionAction(recruiterId, subscriptionId1, action);
		
		Mockito.verify(this.mockDao).save(Mockito.any(RecruiterEntity.class));
		
	}
	
	/**
	* Test method can not by another recruiter where the user is not an admin user
	* @throws Exception
	*/
	@Test
	void testPerformSubscriptionAction_unauthorisedUser() {
		
		final String 				recruiterId 		= "kparkings";
		final UUID 					subscriptionId1 	= UUID.randomUUID();
		final subscription_action 	action 				= subscription_action.ACTIVATE_SUBSCRIPTION;
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(Set.of());
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn("userAttemptingToChangeOtherUsersSubscription");
		
		assertThrows(IllegalAccessException.class, () -> {
			this.service.performSubscriptionAction(recruiterId, subscriptionId1, action);
		});
		
	}
	
	/**
	* Tests case Recruiter adding Subscription for another Recruiter
	* Should never be allowed 
	* @throws Exception
	*/
	@Test
	void testAddSubscription_unauthorized() {
		
		final String 				recruiterId 		= "kparkings";
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(Set.of());
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn("userAttemptingToChangeOtherUsersSubscription");
		
		assertThrows(IllegalAccessException.class, () -> {
			this.service.addSubscription(recruiterId, subscription_type.YEAR_SUBSCRIPTION, INVOICE_TYPE.PERSON);
		});
		
	}
	
	/**
	* Tests case recruiter is not known 
	* @throws Exception
	*/
	@Test
	void testAddSubscription_unknownRecruiter() {
		
		final String 				recruiterId 		= "kparkings";
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(Set.of());
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.addSubscription(recruiterId, subscription_type.YEAR_SUBSCRIPTION, INVOICE_TYPE.PERSON);
		});
		
	} 

	/**
	* Tests that no existing YEAR_SUBSCRIPTION exists that has not already ended 
	* @throws Exception
	*/
	@Test
	void testAddSubscription_yearSubscription_existingYearSubscription() {
		
		final String 				recruiterId 		= "kparkings";
		final Recruiter				recruiter			= Recruiter.builder().userId(recruiterId).subscriptions(Set.of(PaidPeriodRecruiterSubscription.builder().type(subscription_type.YEAR_SUBSCRIPTION).status(subscription_status.AWAITING_ACTIVATION).build())).build();
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(Set.of());
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		
		assertThrows(IllegalStateException.class, () -> {
			this.service.addSubscription(recruiterId, subscription_type.YEAR_SUBSCRIPTION, INVOICE_TYPE.PERSON);
		});
		
	}
	
	/**
	* Tests that if there is an existing FIRST_GEN subscription that is still open that it 
	* gets closed  
	* @throws Exception
	*/
	@Test
	void testAddSubscription_yearSubscription_existingYearSubscription_ended() throws Exception {
		
		final String 				recruiterId 		= "kparkings";
		final Recruiter				recruiter			= Recruiter.builder().userId(recruiterId).subscriptions(Set.of(PaidPeriodRecruiterSubscription.builder().type(subscription_type.YEAR_SUBSCRIPTION).status(subscription_status.SUBSCRIPTION_ENDED).build())).build();
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(Set.of());
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		
		this.service.addSubscription(recruiterId, subscription_type.YEAR_SUBSCRIPTION, INVOICE_TYPE.PERSON);
		
		Mockito.verify(this.mockDao).save(Mockito.any());
		
		Mockito.verify(this.mockExternEventPublisher).publishSubscriptionAddedEvent(Mockito.any(SubscriptionAddedEvent.class));
		
	}
	
	/**
	* Tests that if there is an existing CREDIT_BASED subscription that is still open that it 
	* gets closed  
	* @throws Exception
	*/
	@Test
	void testAddSubscription_yearSubscription_existingCreditBasedSubscription_ended() throws Exception {
		
		final String 				recruiterId 		= "kparkings";
		final Recruiter				recruiter			= Recruiter.builder().userId(recruiterId).subscriptions(Set.of(CreditBasedSubscription.builder().status(subscription_status.ACTIVE).build())).build();
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(Set.of());
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		
		this.service.addSubscription(recruiterId, subscription_type.YEAR_SUBSCRIPTION, INVOICE_TYPE.PERSON);
		
		Mockito.verify(this.mockDao).save(Mockito.any());
		
		Mockito.verify(this.mockExternEventPublisher).publishSubscriptionAddedEvent(Mockito.any(SubscriptionAddedEvent.class));
		
	}
	
	/**
	* Tests happy path where the is no currently open trial period subscription  
	* @throws Exception
	*/
	@Test
	void testAddSubscription_yearSubscription_success_noOpenTrialPeriod() throws Exception {
		
		final String 				recruiterId 				= "kparkings";
		final UUID					existingSubscriptionId	 	= UUID.randomUUID();
		final Recruiter				recruiter					= Recruiter.builder().userId(recruiterId).subscriptions(Set.of(PaidPeriodRecruiterSubscription.builder().type(subscription_type.YEAR_SUBSCRIPTION).subscriptionId(existingSubscriptionId).status(subscription_status.SUBSCRIPTION_ENDED).build())).build();
		
		SecurityContextHolder.setContext(MOCK_SECURITY_CONTEXT);
		
		Mockito.when(MOCK_SECURITY_CONTEXT.getAuthentication()).thenReturn(MOCK_AUTHENTICATION);
		Mockito.when(MOCK_AUTHENTICATION.getAuthorities()).thenReturn(Set.of());
		Mockito.when(MOCK_AUTHENTICATION.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		
		ArgumentCaptor<RecruiterEntity> entityArgCapt = ArgumentCaptor.forClass(RecruiterEntity.class);
		
		Mockito.when(this.mockDao.save(entityArgCapt.capture())).thenReturn(null);
		
		this.service.addSubscription(recruiterId, subscription_type.YEAR_SUBSCRIPTION, INVOICE_TYPE.PERSON);
		
		Mockito.verify(this.mockDao).save(Mockito.any());
		
		RecruiterEntity savedEntity = entityArgCapt.getValue();
		
		assertEquals(2, savedEntity.getSubscriptions().size()); 
		
		RecruiterSubscriptionEntity subEntity = savedEntity.getSubscriptions().stream().filter(se -> se.getSubscriptionId() != existingSubscriptionId).findFirst().get();
		
		assertEquals(subEntity.getActivatedDate(), subEntity.getCreated());
		assertTrue(subEntity.isCurrentSubscription());
		assertEquals(subscription_status.ACTIVE_PENDING_PAYMENT, subEntity.getStatus());
		
		
		RecruiterSubscriptionEntity subEntityExisting = savedEntity.getSubscriptions().stream().filter(se -> se.getSubscriptionId() == existingSubscriptionId).findFirst().get();
		assertFalse(subEntityExisting.isCurrentSubscription());
		assertEquals(subscription_status.SUBSCRIPTION_ENDED, subEntityExisting.getStatus());
		
		Mockito.verify(this.mockExternEventPublisher).publishSubscriptionAddedEvent(Mockito.any(SubscriptionAddedEvent.class));
		
	}
	
	/**
	* Tests Exception thrown if more than one recruiter with the same email
	* address exist
	* @throws Exception
	*/
	@Test
	void testResetPassword_multipleMatchingRecruiters() {
	
		final String emailAddress = "admin@arenella-ict.com";
		
		Mockito.when(this.mockDao.findRecruitersByEmail(emailAddress)).thenReturn(Set.of(Recruiter.builder().build(), Recruiter.builder().build()));
		
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
	void testResetPassword_noMatchingRecruiters() {
		
		final String emailAddress = "admin@arenella-ict.com";
		
		Mockito.when(this.mockDao.findRecruitersByEmail(emailAddress)).thenReturn(Set.of());
		
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
	void testResetPassword() {
		
		final String emailAddress = "admin@arenella-ict.com";
		
		ArgumentCaptor<RecruiterPasswordUpdatedEvent> 	pwdUpdtArgCapt 	= ArgumentCaptor.forClass(RecruiterPasswordUpdatedEvent.class);
		ArgumentCaptor<RequestSendEmailCommand>			emailArgCapt	= ArgumentCaptor.forClass(RequestSendEmailCommand.class);
		
		Mockito.doNothing().when(this.mockExternEventPublisher).publishRecruiterPasswordUpdated(pwdUpdtArgCapt.capture());
		Mockito.doNothing().when(this.mockExternEventPublisher).publishSendEmailCommand(emailArgCapt.capture());
		
		Mockito.when(this.mockDao.findRecruitersByEmail(emailAddress)).thenReturn(Set.of(Recruiter.builder().firstName(FIRST_NAME).userId(USER_ID).build()));
		
		this.service.resetPassword(emailAddress);
		
		assertEquals(USER_ID, pwdUpdtArgCapt.getValue().getRecruiterId());
		assertEquals(EmailTopic.PASSWORD_RESET, emailArgCapt.getValue().getTopic());
		
		Map<String,Object> model = emailArgCapt.getValue().getModel();
		
		assertEquals(USER_ID, model.get("userId"));
		assertEquals(FIRST_NAME, model.get("firstname"));
		assertNotNull(model.get("password"));
		
	}
	
	/**
	* Tests successful deletion of Recruiter
	* @throws Exception
	*/
	@Test
	void deleteRecruiter_happyPath() {
		
		ArgumentCaptor<RecruiterDeletedEvent> argCaptEvent = ArgumentCaptor.forClass(RecruiterDeletedEvent.class);
		
		final String recruiterId = "aRecId";
		
		Mockito.when(this.mockOpenPositionDao.findAllOpenPositionsByRecruiterId(recruiterId)).thenReturn(Set.of(OpenPosition.builder().id(UUID.randomUUID()).build(),OpenPosition.builder().id(UUID.randomUUID()).build()));
		Mockito.when(this.mockCreditDao.getByRecruiterId(recruiterId)).thenReturn(Optional.of(RecruiterCredit.builder().credits(1).recruiterId(recruiterId).build()));
		Mockito.doNothing().when(this.mockExternEventPublisher).publishRecruiterAccountDeleted(argCaptEvent.capture());
		
		this.service.deleteRecruiter(recruiterId);
		
		Mockito.verify(this.mockOpenPositionDao, Mockito.times(2)).deleteById(Mockito.any());
		Mockito.verify(this.mockCreditDao).deleteById(recruiterId);
		Mockito.verify(this.mockRecruiterProfileDao).deleteById(recruiterId);
		Mockito.verify(this.mockDao).deleteById(recruiterId);
		Mockito.verify(this.mockExternEventPublisher).publishRecruiterAccountDeleted(Mockito.any());
		
		assertEquals(recruiterId, argCaptEvent.getValue().getRecruiterId());
		
	}
	
}