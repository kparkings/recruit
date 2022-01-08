package com.arenella.recruit.recruiters.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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

import com.arenella.recruit.recruiters.beans.FirstGenRecruiterSubscription;
import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.Recruiter.language;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;
import com.arenella.recruit.recruiters.beans.TrialPeriodSubscription;
import com.arenella.recruit.recruiters.beans.YearlyRecruiterSubscription;
import com.arenella.recruit.recruiters.dao.RecruiterDao;
import com.arenella.recruit.recruiters.entities.RecruiterEntity;
import com.arenella.recruit.recruiters.entities.RecruiterSubscriptionEntity;
import com.arenella.recruit.recruiters.utils.RecruiterSubscriptionActionHandler;
import com.arenella.recruit.recruiters.utils.RecruiterSubscriptionFactory;

/**
* Unit tests for the RecruiterService
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class RecruiterServiceImplTest {

	@InjectMocks
	private RecruiterService 					service 	= new RecruiterServiceImpl();
	
	@Mock
	private RecruiterDao 						mockDao;
	
	@Mock
	private RecruiterSubscriptionFactory 		mockRecruiterSubscriptionFactory;
	
	@Mock
	private RecruiterSubscriptionActionHandler 	mockRecruiterSubscriptionActionHandler;
	
	private static final String 			userId 					= "kparkings";
	private static final LocalDate 			accountCreated 			= LocalDate.of(2021, 10, 13);
	private static final String 			companyName 			= "arenella-ict";
	private static final String 			email 					= "kparkings@gmail.com";
	private static final String 			firstname 				= "kevin";
	private static final language 			lang 					= language.DUTCH;
	private static final String 			surname 				= "parkings";
	private static final SecurityContext 	mockSecurityContext 	= Mockito.mock(SecurityContext.class);
	private static final Authentication 	mockAuthentication 		= Mockito.mock(Authentication.class);
	
	/**
	* Tests happy path for creating a new Recruiter account
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testAddRecruiter() throws Exception {
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getName()).thenReturn(userId);
		
		ArgumentCaptor<RecruiterEntity> argCaptEntity = ArgumentCaptor.forClass(RecruiterEntity.class);
		
		Mockito.when(this.mockDao.existsById(userId)).thenReturn(false);
		Mockito.when(this.mockDao.save(argCaptEntity.capture())).thenReturn(null);
		Mockito.when(this.mockRecruiterSubscriptionFactory.getActionHandlerByType(Mockito.any())).thenReturn(TrialPeriodSubscription.getActionHandler());
	
		Recruiter recruiter = Recruiter
								.builder()
									.active(false)
									.companyName(companyName)
									.email(email)
									.firstName(firstname)
									.language(lang)
									.surname(surname)
									.userId(userId)
								.build();
		
		this.service.addRecruiter(recruiter);
		
		RecruiterEntity entity = argCaptEntity.getValue();
		
		assertEquals(1, entity.getSubscriptions().size());
		assertEquals(subscription_type.TRIAL_PERIOD, entity.getSubscriptions().stream().findFirst().get().getType());
		
		assertEquals(companyName, 	entity.getCompanyName());
		assertEquals(email, 		entity.getEmail());
		assertEquals(firstname, 	entity.getFirstName());
		assertEquals(lang, 			entity.getLanguage());
		assertEquals(surname, 		entity.getSurname());
		assertEquals(userId, 		entity.getUserId());
		assertTrue(entity.isActive());
		assertNotNull(entity.getAccountCreated());
		
	}
	
	/**
	* Tests failure case of creating a new Recruiter where an account
	* with the same userId already exists
	* @throws Exception
	*/
	@Test
	public void testAddRecruiter_alreadyExists() throws Exception {
		
		Mockito.when(this.mockDao.existsById(userId)).thenReturn(true);
		
		Recruiter recruiter = Recruiter
								.builder()
									.active(false)
									.companyName(companyName)
									.email(email)
									.firstName(firstname)
									.language(lang)
									.surname(surname)
									.userId(userId)
								.build();
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.addRecruiter(recruiter);
		});
		
	}
	
	/**
	* Tests happy path for updating an exsiting Recruiter
	* @throws Exception
	*/
	@Test
	public void testUpdateRecruiter() throws Exception{
		
		final String 	companyNameUpdt 		= "arenella-ictnew";
		final String 	emailUpdt 				= "kparkings@gmail.comnew";
		final String 	firstnameUpdt 			= "kevinnew";
		final language 	langUpdt 				= language.FRENCH;
		final String 	surnameUpdt 			= "parkingsnew";
		
		ArgumentCaptor<RecruiterEntity> argCaptEntity = ArgumentCaptor.forClass(RecruiterEntity.class);
		
		RecruiterEntity recruiterEntity = RecruiterEntity
											.builder()
												.active(true)
												.companyName(companyName)
												.email(email)
												.firstName(firstname)
												.language(lang)
												.surname(surname)
												.userId(userId)
												.accountCreated(accountCreated)
											.build();
									
		Mockito.when(this.mockDao.findById(userId)).thenReturn(Optional.of(recruiterEntity));
		Mockito.when(this.mockDao.save(argCaptEntity.capture())).thenReturn(null);
		
		Recruiter recruiter = Recruiter
								.builder()
									.active(false)
									.companyName(companyNameUpdt)
									.email(emailUpdt)
									.firstName(firstnameUpdt)
									.language(langUpdt)
									.surname(surnameUpdt)
									.userId(userId)
								.build();
		
		this.service.updateRecruiter(recruiter);
		
		RecruiterEntity entity = argCaptEntity.getValue();
		
		assertEquals(companyNameUpdt, 	entity.getCompanyName());
		assertEquals(emailUpdt, 		entity.getEmail());
		assertEquals(firstnameUpdt, 	entity.getFirstName());
		assertEquals(langUpdt, 			entity.getLanguage());
		assertEquals(surnameUpdt, 		entity.getSurname());
		assertEquals(userId, 		entity.getUserId());
		assertTrue(entity.isActive());
		assertNotNull(entity.getAccountCreated());
		
		Mockito.verify(this.mockDao).save(recruiterEntity);
		
	}
	
	/**
	* Tests failure case where Recruiter to update does not exist
	* @throws Exception
	*/
	@Test
	public void testUpdateRecruiter_unknownRecruiter() throws Exception {
		
		Recruiter recruiter = Recruiter
								.builder()
									.active(false)
									.companyName(companyName)
									.email(email)
									.firstName(firstname)
									.language(lang)
									.surname(surname)
									.userId(userId)
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
	public void testFetchRecruiters() throws Exception{
		
		Set<RecruiterEntity> recruiterEntities = Set.of(RecruiterEntity
																.builder()
																	.active(true)
																	.companyName(companyName)
																	.email(email)
																	.firstName(firstname)
																	.language(lang)
																	.surname(surname)
																	.userId(userId)
																	.accountCreated(accountCreated)
																.build());
		
		Mockito.when(this.mockDao.findAll()).thenReturn(recruiterEntities);
		
		Set<Recruiter> recruiters = this.service.fetchRecruiters();
		
		Recruiter recruiter = recruiters.stream().findFirst().get();
		
		assertEquals(companyName, 	recruiter.getCompanyName());
		assertEquals(email, 		recruiter.getEmail());
		assertEquals(firstname, 	recruiter.getFirstName());
		assertEquals(lang, 			recruiter.getLanguage());
		assertEquals(surname, 		recruiter.getSurname());
		assertEquals(userId, 			recruiter.getUserId());
		assertTrue(recruiter.isActive());
		assertNotNull(recruiter.getAccountCreated());
		
	}

	/**
	* Tests happy path for Fetching a specific Recruiter
	* @throws Exception
	*/
	@Test
	public void testFetchRecruiter() throws Exception{
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(mockAuthentication.getName()).thenReturn(userId);
		
		RecruiterEntity recruiterEntity = RecruiterEntity
													.builder()
														.active(true)
														.companyName(companyName)
														.email(email)
														.firstName(firstname)
														.language(lang)
														.surname(surname)
														.userId(userId)
														.accountCreated(accountCreated)
													.build();

		Mockito.when(this.mockDao.findByUserIdIgnoreCase(userId)).thenReturn(Optional.of(recruiterEntity));
		
		Recruiter recruiter = this.service.fetchRecruiter(userId);
		
		assertEquals(companyName, 	recruiter.getCompanyName());
		assertEquals(email, 		recruiter.getEmail());
		assertEquals(firstname, 	recruiter.getFirstName());
		assertEquals(lang, 			recruiter.getLanguage());
		assertEquals(surname, 		recruiter.getSurname());
		assertEquals(userId, 		recruiter.getUserId());
		assertTrue(recruiter.isActive());
		assertNotNull(recruiter.getAccountCreated());

	}
	
	/**
	* Tests Failure path where fetch of unknown Recruiter is performed
	* @throws Exception
	*/
	@Test
	public void testFetchRecruiter_unknownRecruiter() throws Exception {
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(mockAuthentication.getName()).thenReturn("unknownUser");
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.fetchRecruiter("unknownUser");
		});
	}
	
	/**
	* Tests Failure path where a Recruiter attempts to access another Recruiters details
	* @throws Exception
	*/
	@Test
	public void testFetchRecruiter_recruiterAccessingAnotherRecruiter() throws Exception {
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(mockAuthentication.getName()).thenReturn("NotThisRecruiter");
		
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
	public void testFetchRecruiter_asAdmin() throws Exception {
	
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getName()).thenReturn(userId);
		
		RecruiterEntity recruiterEntity = RecruiterEntity
													.builder()
														.active(true)
														.companyName(companyName)
														.email(email)
														.firstName(firstname)
														.language(lang)
														.surname(surname)
														.userId(userId)
														.accountCreated(accountCreated)
													.build();

		Mockito.when(this.mockDao.findByUserIdIgnoreCase(userId)).thenReturn(Optional.of(recruiterEntity));
		
		Recruiter recruiter = this.service.fetchRecruiter(userId);
		
		assertEquals(companyName, 	recruiter.getCompanyName());
		assertEquals(email, 		recruiter.getEmail());
		assertEquals(firstname, 	recruiter.getFirstName());
		assertEquals(lang, 			recruiter.getLanguage());
		assertEquals(surname, 		recruiter.getSurname());
		assertEquals(userId, 		recruiter.getUserId());
		assertTrue(recruiter.isActive());
		assertNotNull(recruiter.getAccountCreated());
		
	}
	
	/**
	* Tests an Exception is thrown if the recruiter is unknown
	* @throws Exception
	*/
	@Test
	public void testPerformSubscriptionAction_unknownRecruiter() throws Exception {
		
		final String 				recruiterId 		= "kparkings";
		final UUID 					subscriptionId 		= UUID.randomUUID();
		final subscription_action 	action 				= subscription_action.ACTIVATE_SUBSCRIPTION;
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(mockAuthentication.getName()).thenReturn(recruiterId);
		
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
	public void testPerformSubscriptionAction_unknownSubscription() throws Exception {
		
		final String 				recruiterId 		= "kparkings";
		final UUID 					subscriptionId 		= UUID.randomUUID();
		final subscription_action 	action 				= subscription_action.ACTIVATE_SUBSCRIPTION;
		final Recruiter				recruiter			= Recruiter.builder().build();
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(mockAuthentication.getName()).thenReturn(recruiterId);
		
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
	public void testPerformSubscriptionAction_multipleCurrentSubscriptions() throws Exception {
		
		final String 				recruiterId 		= "kparkings";
		final UUID 					subscriptionId1 	= UUID.randomUUID();
		final UUID 					subscriptionId2 	= UUID.randomUUID();
		final subscription_action 	action 				= subscription_action.ACTIVATE_SUBSCRIPTION;
		final Recruiter				recruiter			= Recruiter
															.builder()
																.subscriptions(Set.of(TrialPeriodSubscription.builder().subscriptionId(subscriptionId1).currentSubscription(true).build(),
																					  TrialPeriodSubscription.builder().subscriptionId(subscriptionId2).currentSubscription(true).build()
																				)
																				)
															.build();
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(mockAuthentication.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		Mockito.when(this.mockRecruiterSubscriptionFactory.getActionHandlerByType(Mockito.any())).thenReturn(mockRecruiterSubscriptionActionHandler);
		Mockito.doNothing().when(mockRecruiterSubscriptionActionHandler).performAction(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		
		assertThrows(IllegalStateException.class, () -> {
			this.service.performSubscriptionAction(recruiterId, subscriptionId1, action);
		});
		
	}
	
	/**
	* Tests the happy path
	* @throws Exception
	*/
	@Test
	public void testPerformSubscriptionAction() throws Exception {
		
		final String 				recruiterId 		= "kparkings";
		final UUID 					subscriptionId1 	= UUID.randomUUID();
		final UUID 					subscriptionId2 	= UUID.randomUUID();
		final subscription_action 	action 				= subscription_action.ACTIVATE_SUBSCRIPTION;
		final Recruiter				recruiter			= Recruiter
															.builder()
																.subscriptions(Set.of(TrialPeriodSubscription.builder().subscriptionId(subscriptionId1).currentSubscription(true).build(),
																					  TrialPeriodSubscription.builder().subscriptionId(subscriptionId2).currentSubscription(false).build()
																				)
																				)
															.build();
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(mockAuthentication.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		Mockito.when(this.mockRecruiterSubscriptionFactory.getActionHandlerByType(Mockito.any())).thenReturn(mockRecruiterSubscriptionActionHandler);
		Mockito.doNothing().when(mockRecruiterSubscriptionActionHandler).performAction(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		
		this.service.performSubscriptionAction(recruiterId, subscriptionId1, action);
		
		Mockito.verify(this.mockDao).save(Mockito.any(RecruiterEntity.class));
		
	}
	
	/**
	* Test method can be called by admin users
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testPerformSubscriptionAction_admin() throws Exception {
		
		final String 				recruiterId 		= "kparkings";
		final UUID 					subscriptionId1 	= UUID.randomUUID();
		final UUID 					subscriptionId2 	= UUID.randomUUID();
		final subscription_action 	action 				= subscription_action.ACTIVATE_SUBSCRIPTION;
		final Recruiter				recruiter			= Recruiter
															.builder()
																.subscriptions(Set.of(TrialPeriodSubscription.builder().subscriptionId(subscriptionId1).currentSubscription(true).build(),
																					  TrialPeriodSubscription.builder().subscriptionId(subscriptionId2).currentSubscription(false).build()
																				)
																				)
															.build();
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthentication.getName()).thenReturn(userId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		Mockito.when(this.mockRecruiterSubscriptionFactory.getActionHandlerByType(Mockito.any())).thenReturn(mockRecruiterSubscriptionActionHandler);
		Mockito.doNothing().when(mockRecruiterSubscriptionActionHandler).performAction(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		
		this.service.performSubscriptionAction(recruiterId, subscriptionId1, action);
		
		Mockito.verify(this.mockDao).save(Mockito.any(RecruiterEntity.class));
		
	}
	
	/**
	* Test method can not by another recruiter where the user is not an admin user
	* @throws Exception
	*/
	@Test
	public void testPerformSubscriptionAction_unauthorisedUser() throws Exception {
		
		final String 				recruiterId 		= "kparkings";
		final UUID 					subscriptionId1 	= UUID.randomUUID();
		final subscription_action 	action 				= subscription_action.ACTIVATE_SUBSCRIPTION;
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(mockAuthentication.getName()).thenReturn("userAttemptingToChangeOtherUsersSubscription");
		
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
	public void testAddSubscription_unauthorized() throws Exception {
		
		final String 				recruiterId 		= "kparkings";
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(mockAuthentication.getName()).thenReturn("userAttemptingToChangeOtherUsersSubscription");
		
		assertThrows(IllegalAccessException.class, () -> {
			this.service.addSubscription(recruiterId, subscription_type.YEAR_SUBSCRIPTION);
		});
		
	}
	
	/**
	* Tests case recruiter is not known 
	* @throws Exception
	*/
	@Test
	public void testAddSubscription_unknownRecruiter() throws Exception {
		
		final String 				recruiterId 		= "kparkings";
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(mockAuthentication.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.addSubscription(recruiterId, subscription_type.YEAR_SUBSCRIPTION);
		});
		
	} 

	/**
	* Tests that no existing YEAR_SUBSCRIPTION exists that has not already ended 
	* @throws Exception
	*/
	@Test
	public void testAddSubscription_yearSubscription_existingYearSubscription() throws Exception {
		
		final String 				recruiterId 		= "kparkings";
		final Recruiter				recruiter			= Recruiter.builder().userId(recruiterId).subscriptions(Set.of(YearlyRecruiterSubscription.builder().status(subscription_status.AWAITING_ACTIVATION).build())).build();
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(mockAuthentication.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		
		assertThrows(IllegalStateException.class, () -> {
			this.service.addSubscription(recruiterId, subscription_type.YEAR_SUBSCRIPTION);
		});
		
	}
	
	/**
	* Tests that if there is an existing FIRST_GEN subscription that is still open that it 
	* gets closed  
	* @throws Exception
	*/
	@Test
	public void testAddSubscription_yearSubscription_existingYearSubscription_ended() throws Exception {
		
		final String 				recruiterId 		= "kparkings";
		final Recruiter				recruiter			= Recruiter.builder().userId(recruiterId).subscriptions(Set.of(YearlyRecruiterSubscription.builder().status(subscription_status.SUBSCRIPTION_ENDED).build())).build();
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(mockAuthentication.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		
		this.service.addSubscription(recruiterId, subscription_type.YEAR_SUBSCRIPTION);
		
		Mockito.verify(this.mockDao).save(Mockito.any());
		
	}
	
	/**
	* Tests happy path where the is no currently open trial period subscription  
	* @throws Exception
	*/
	@Test
	public void testAddSubscription_yearSubscription_success_noOpenTrialPeriod() throws Exception {
		
		final String 				recruiterId 				= "kparkings";
		final UUID					existingSubscriptionId	 	= UUID.randomUUID();
		final Recruiter				recruiter					= Recruiter.builder().userId(recruiterId).subscriptions(Set.of(YearlyRecruiterSubscription.builder().subscriptionId(existingSubscriptionId).status(subscription_status.SUBSCRIPTION_ENDED).build())).build();
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(mockAuthentication.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		
		ArgumentCaptor<RecruiterEntity> entityArgCapt = ArgumentCaptor.forClass(RecruiterEntity.class);
		
		Mockito.when(this.mockDao.save(entityArgCapt.capture())).thenReturn(null);
		
		this.service.addSubscription(recruiterId, subscription_type.YEAR_SUBSCRIPTION);
		
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
		
	}
	
	/**
	* Tests happy path where the is a currently open trial period subscription and it 
	* has still got days left before it runs out 
	* @throws Exception
	*/
	@Test
	public void testAddSubscription_yearSubscription_success_OpenTrialPeriod() throws Exception {
		
		final String 				recruiterId 				= "kparkings";
		final UUID					existingSubscriptionId	 	= UUID.randomUUID();
		final LocalDateTime			existingCreatedActivated	= LocalDateTime.now().minusDays(1);
		final Recruiter				recruiter					= Recruiter.builder().userId(recruiterId).subscriptions(Set.of(TrialPeriodSubscription.builder().subscriptionId(existingSubscriptionId).created(existingCreatedActivated).activateDate(existingCreatedActivated).currentSubscription(true).status(subscription_status.ACTIVE).build())).build();
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(mockAuthentication.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		
		ArgumentCaptor<RecruiterEntity> entityArgCapt = ArgumentCaptor.forClass(RecruiterEntity.class);
		
		Mockito.when(this.mockDao.save(entityArgCapt.capture())).thenReturn(null);
		
		this.service.addSubscription(recruiterId, subscription_type.YEAR_SUBSCRIPTION);
		
		Mockito.verify(this.mockDao).save(Mockito.any());
		
		RecruiterEntity savedEntity = entityArgCapt.getValue();
		
		assertEquals(2, savedEntity.getSubscriptions().size()); 
		
		RecruiterSubscriptionEntity subEntity = savedEntity.getSubscriptions().stream().filter(se -> se.getSubscriptionId() != existingSubscriptionId).findFirst().get();
		
		assertNotEquals(subEntity.getActivatedDate(), subEntity.getCreated());
		assertTrue(subEntity.isCurrentSubscription());
		assertEquals(subscription_status.ACTIVE_PENDING_PAYMENT, subEntity.getStatus());
		
		RecruiterSubscriptionEntity subEntityExisting = savedEntity.getSubscriptions().stream().filter(se -> se.getSubscriptionId() == existingSubscriptionId).findFirst().get();
		assertFalse(subEntityExisting.isCurrentSubscription());
		assertEquals(subscription_status.SUBSCRIPTION_ENDED, subEntityExisting.getStatus());
		
	}
	
	/**
	* Tests that any currently open FIRST_GEN subscriptions are ended
	* @throws Exception
	*/
	@Test
	public void testAddSubscription_yearSubscription_success_OpenFirstGen() throws Exception {
		
		final String 				recruiterId 				= "kparkings";
		final UUID					existingSubscriptionId	 	= UUID.randomUUID();
		final LocalDateTime			existingCreatedActivated	= LocalDateTime.now().minusDays(1);
		final Recruiter				recruiter					= Recruiter.builder().userId(recruiterId).subscriptions(Set.of(FirstGenRecruiterSubscription.builder().subscriptionId(existingSubscriptionId).created(existingCreatedActivated).activateDate(existingCreatedActivated).currentSubscription(true).status(subscription_status.ACTIVE).build())).build();
		
		SecurityContextHolder.setContext(mockSecurityContext);
		
		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(Set.of());
		Mockito.when(mockAuthentication.getName()).thenReturn(recruiterId);
		
		Mockito.when(this.mockDao.findRecruiterById(recruiterId)).thenReturn(Optional.of(recruiter));
		
		ArgumentCaptor<RecruiterEntity> entityArgCapt = ArgumentCaptor.forClass(RecruiterEntity.class);
		
		Mockito.when(this.mockDao.save(entityArgCapt.capture())).thenReturn(null);
		
		this.service.addSubscription(recruiterId, subscription_type.YEAR_SUBSCRIPTION);
		
		Mockito.verify(this.mockDao).save(Mockito.any());
		
		RecruiterEntity savedEntity = entityArgCapt.getValue();
		
		assertEquals(2, savedEntity.getSubscriptions().size()); 
		
		RecruiterSubscriptionEntity subEntityExisting = savedEntity.getSubscriptions().stream().filter(se -> se.getSubscriptionId() == existingSubscriptionId).findFirst().get();
		assertFalse(subEntityExisting.isCurrentSubscription());
		assertEquals(subscription_status.SUBSCRIPTION_ENDED, subEntityExisting.getStatus());
		
	}
	
}