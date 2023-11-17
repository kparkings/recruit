package com.arenella.recruit.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.events.CandidateAccountCreatedEvent;
import com.arenella.recruit.adapters.events.CandidatePasswordUpdatedEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterPasswordUpdatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.authentication.adapters.AuthenticationMonolithExternalEventListener;
import com.arenella.recruit.authentication.beans.User.USER_ROLE;
import com.arenella.recruit.authentication.enums.AccountType;
import com.arenella.recruit.authentication.services.AccountService;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Unit tests for the AuthenticationMonolithExternalEventListener class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class AuthenticationMonolithExternalEventListenerTest {
	
	@InjectMocks
	private AuthenticationMonolithExternalEventListener eventListener = new AuthenticationMonolithExternalEventListener();
	
	@Mock
	private AccountService mockAccountService;
	
	@Captor
	private ArgumentCaptor<Set<USER_ROLE>> userRoleArgCapt;
	
	/**
	* Tests correct action is carried out when a RecruiterCreatedEvent is received
	* @throws Exception
	*/
	@Test
	public void testListenForRecruiterCreatedEvent() throws Exception{
		
		final String recruiterId 		= "kparkings";
		final String encryptedPassword	= "fesf777@@@7d!";
		
		RecruiterCreatedEvent event = RecruiterCreatedEvent.builder().recruiterId(recruiterId).encryptedPassword(encryptedPassword).build();
		
		Mockito.doNothing().when(this.mockAccountService).createAccount(recruiterId, encryptedPassword, AccountType.RECRUITER);
		
		this.eventListener.listenForRecruiterCreatedEvent(event);
		
		Mockito.verify(mockAccountService).createAccount(recruiterId, encryptedPassword, AccountType.RECRUITER);
		
	}
	
	/**
	* Tests correct action is carried out when a RecruiterNoOpenSubscriptionEvent is received
	* @throws Exception
	*/
	@Test
	public void testListenForRecruiterNoOpenSubscriptionsEvent() throws Exception {
	
		final String 			recruiterId 		= "kparkings";
		final Set<USER_ROLE>	userRoles 			= Set.of(USER_ROLE.recruiterNoSubscrition);
		
		RecruiterNoOpenSubscriptionEvent event = new RecruiterNoOpenSubscriptionEvent(recruiterId);
		
		Mockito.doNothing().when(this.mockAccountService).replaceRolesForUser(recruiterId, userRoles);
		
		this.eventListener.listenForRecruiterNoOpenSubscriptionsEvent(event);
	
		Mockito.verify(this.mockAccountService).replaceRolesForUser(recruiterId, userRoles);
		Mockito.verify(this.mockAccountService).updateUsersCreditStatus(recruiterId, false);
	}
	
	/**
	* Tests correct action is carried out when a RecruiterHasOpenSubscriptionEvent is received
	* @throws Exception
	*/
	@Test
	public void testListenForRecruiterHasOpenSubscriptionEvent() throws Exception {
	
		final String 			recruiterId 		= "kparkings";
		
		SubscriptionAddedEvent event = new SubscriptionAddedEvent(recruiterId, subscription_type.ONE_MONTH_SUBSCRIPTION);
		
		Mockito.doNothing().when(this.mockAccountService).replaceRolesForUser(Mockito.eq(recruiterId), userRoleArgCapt.capture());
		
		this.eventListener.listenForSubscriptionAddedEvent(event);
	
		Mockito.verify(this.mockAccountService).replaceRolesForUser(Mockito.eq(recruiterId), Mockito.anySet());
		Mockito.verify(this.mockAccountService).updateUsersCreditStatus(recruiterId, false);
		
		Set<USER_ROLE> roles = userRoleArgCapt.getValue();
		
		assertTrue(roles.size() == 1);
		assertEquals(USER_ROLE.recruiter, roles.stream().findFirst().get());
		
	}

	/**
	* Tests correct action is carried out when a RecruiterPasswordUpdatedEvent is received
	* @throws Exception
	*/
	@Test
	public void testListenForRecruiterPasswordUpdatedEvent() throws Exception{
		
		final String userId 	= "kparkings";
		final String password 		= "sklf@@9";
		
		Mockito.doNothing().when(this.mockAccountService).updateUserPassword(userId, password);
		
		RecruiterPasswordUpdatedEvent event = new RecruiterPasswordUpdatedEvent(userId, password);
		
		this.eventListener.listenForRecruiterPasswordUpdatedEvent(event);
		
		Mockito.verify(this.mockAccountService).updateUserPassword(userId, password);
		
	}
	
	/**
	* Tests listening of CandidateAccountCreatedEvent
	* @throws Exception
	*/
	@Test
	public void testListenForCandidateAccountCreatedEvent() throws Exception{
		
		CandidateAccountCreatedEvent event = new CandidateAccountCreatedEvent("can1","@@3##d31@");
		
		this.eventListener.listenForCandidateAccountCreatedEvent(event);
		
		Mockito.verify(this.mockAccountService).createAccount(event.getCandidateId(), event.getEncryptedPassword(), AccountType.CANDIDATE);
		
	}
	
	/**
	* Tests listening of CandidatePasswordUpdatedEvent
	* @throws Exception
	*/
	@Test
	public void testListenForCandidatePasswordUpdatedEvent() throws Exception{
		
		CandidatePasswordUpdatedEvent event = new CandidatePasswordUpdatedEvent("can1", "33344!!F$%");
		
		this.eventListener.listenForCandidatePasswordUpdatedEvent(event);
		
		Mockito.verify(this.mockAccountService).updateUserPassword(event.getCandidateId(), event.getNewPassword());
		
	}
	
}