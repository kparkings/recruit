package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.recruiters.adapters.RecruitersExternalEventPublisher;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;

/**
* Unit tests for the TrialPeriodSubscription ActionHandler class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class TrialPeriodrSubscriptionActionHandlerTest {

	@InjectMocks
	TrialPeriodSubscriptionActionHandler 	handler;
	
	@Mock
	RecruitersExternalEventPublisher 		mockExternEventPublisher;
	
	/**
	* Tests handler for combination
	* STATE: 	AWAITING_ACTIVATION
	* ACTION:	ACTIVATE_SUBSCRIPTION
	* @throws Exception
	*/
	@Test
	public void testActionHandler_awaitingActivation_activateSubscription() throws Exception {
		
		RecruiterSubscription 	subscription1 	= TrialPeriodSubscription.builder().subscriptionId(UUID.randomUUID()).currentSubscription(true).build();
		RecruiterSubscription 	subscription2 	= TrialPeriodSubscription.builder().subscriptionId(UUID.randomUUID()).currentSubscription(false).status(subscription_status.AWAITING_ACTIVATION).build();
		Recruiter 				recruiter 		= Recruiter
													.builder()
													.subscriptions(Set.of(subscription1, subscription2))
													.build();
		
		handler.performAction(recruiter, subscription2, subscription_action.ACTIVATE_SUBSCRIPTION, true);
		
		assertTrue(subscription2.isCurrentSubscription());
		
		assertEquals(subscription_status.ACTIVE, subscription2.getStatus());
		
	}
	
	/**
	* Tests handler for combination
	* STATE: 	AWAITING_ACTIVATION
	* ACTION:	REJECT_SUBSCRIPTION
	* @throws Exception
	*/
	@Test
	public void testActionHandler_awaitingActivation_rejectSubscription() throws Exception {
		
		RecruiterSubscription 	subscription1 	= TrialPeriodSubscription.builder().subscriptionId(UUID.randomUUID()).currentSubscription(true).build();
		RecruiterSubscription 	subscription2 	= TrialPeriodSubscription.builder().subscriptionId(UUID.randomUUID()).currentSubscription(false).status(subscription_status.AWAITING_ACTIVATION).build();
		Recruiter 				recruiter 		= Recruiter
													.builder()
													.subscriptions(Set.of(subscription1, subscription2))
													.build();
		
		handler.performAction(recruiter, subscription2, subscription_action.REJECT_SUBSCRIPTION, true);
		
		assertTrue(subscription1.isCurrentSubscription());
		assertFalse(subscription2.isCurrentSubscription());
		
		assertEquals(subscription_status.SUBSCRIPTION_ENDED, subscription2.getStatus());
		
	}
	
	/**
	* Tests Exception thrown if an unsupported action 
	* is attempted
	* STATE: 	AWAITING_ACTIVATION
	* @throws Exception
	*/
	@Test
	public void testActionHandler_awaitingActivation_unsupportedAction() throws Exception {
		
		RecruiterSubscription 	subscription1 	= TrialPeriodSubscription.builder().subscriptionId(UUID.randomUUID()).currentSubscription(true).build();
		RecruiterSubscription 	subscription2 	= TrialPeriodSubscription.builder().subscriptionId(UUID.randomUUID()).currentSubscription(false).status(subscription_status.AWAITING_ACTIVATION).build();
		Recruiter 				recruiter 		= Recruiter
													.builder()
														.subscriptions(Set.of(subscription1, subscription2))
													.build();
	
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			handler.performAction(recruiter, subscription2, subscription_action.END_SUBSCRIPTION, true);
		});
			
	}
	
	/**
	* Tests handler for combination
	* STATE: 	ACTIVE
	* ACTION:	END_SUBSCRIPTION
	* @throws Exception
	*/
	@Test
	public void testActionHandler_active_endSubscription() throws Exception {
		
		RecruiterSubscription 	subscription2 	= TrialPeriodSubscription.builder().subscriptionId(UUID.randomUUID()).currentSubscription(true).status(subscription_status.ACTIVE).build();
		Recruiter 				recruiter 		= Recruiter
													.builder()
														.subscriptions(Set.of(subscription2))
														.userId("kparkings")
													.build();
		
		handler.performAction(recruiter, subscription2, subscription_action.END_SUBSCRIPTION, true);
		
		assertFalse(subscription2.isCurrentSubscription());
		
		assertEquals(subscription_status.SUBSCRIPTION_ENDED, subscription2.getStatus());
		
		Mockito.verify(mockExternEventPublisher).publishRecruiterNoOpenSubscriptionsEvent(recruiter.getUserId());
		
	}
	
	/**
	* Tests Exception thrown if an unsupported action 
	* is attempted
	* STATE: 	ACTIVE
	* @throws Exception
	*/
	@Test
	public void testActionHandler_active_unsupportedAction() throws Exception {
		
		RecruiterSubscription 	subscription1 	= TrialPeriodSubscription.builder().subscriptionId(UUID.randomUUID()).currentSubscription(true).status(subscription_status.ACTIVE).build();
		Recruiter 				recruiter 		= Recruiter
													.builder()
													.subscriptions(Set.of(subscription1))
													.build();
	
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			handler.performAction(recruiter, subscription1, subscription_action.ACTIVATE_SUBSCRIPTION, true);
		});
			
	}
	
	/**
	* Tests Exception thrown if an unsupported state 
	* is attempted
	* STATE: 	DISABLED_PENDING_PAYMENT
	* @throws Exception
	*/
	@Test
	public void testActionHandler_unsupported_state() throws Exception {
		
		RecruiterSubscription 	subscription1 	= TrialPeriodSubscription.builder().subscriptionId(UUID.randomUUID()).currentSubscription(true).status(subscription_status.DISABLED_PENDING_PAYMENT).build();
		Recruiter 				recruiter 		= Recruiter
													.builder()
													.subscriptions(Set.of(subscription1))
													.build();
	
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			handler.performAction(recruiter, subscription1, subscription_action.ACTIVATE_SUBSCRIPTION, true);
		});
			
	}
	
}