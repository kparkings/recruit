package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;

/**
* Unit tests for the TrialPeriodSubscription class
* @author K Parkings
*/
public class TrialPeriodrSubscriptionTest {

	/**
	* Tests creation via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final LocalDateTime 		activatedDate 		= LocalDateTime.of(2021, 12, 24, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.ACTIVE;
		
		TrialPeriodSubscription subscription = TrialPeriodSubscription
																.builder()
																	.activateDate(activatedDate)
																	.created(created)
																	.recruiterId(recruiterId)
																	.subscriptionId(subscriptionId)
																	.status(status)
																	.currentSubscription(true)
																.build();
		
		assertEquals(activatedDate, 											subscription.getActivatedDate());
		assertEquals(created, 													subscription.getCreated());
		assertEquals(recruiterId, 												subscription.getRecruiterId());
		assertEquals(status, 													subscription.getStatus());
		assertEquals(subscriptionId, 											subscription.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.TRIAL_PERIOD, 		subscription.getType());
		assertTrue(subscription.isCurrentSubscription());
		
	}
	
	/**
	* Tests correct attributes are updated as part of activation
	* @throws Exception
	*/
	@Test 
	public void testActivateSubscription() throws Exception {
		
		TrialPeriodSubscription subscription = TrialPeriodSubscription.builder().build();
		
		assertEquals(subscription_status.AWAITING_ACTIVATION, subscription.getStatus());
		assertNull(subscription.getActivatedDate());
		
		subscription.activateSubscription();
		
		assertTrue(subscription.getActivatedDate() instanceof LocalDateTime);
		assertEquals(subscription_status.ACTIVE, subscription.getStatus());
		
	}
	
	/**
	* Tests Exception thrown if previous activation has taken place
	* @throws Exception
	*/
	@Test 
	public void testActivateSubscription_previouslyActivated() throws Exception {
		
		TrialPeriodSubscription subscription = TrialPeriodSubscription.builder().build();
		
		subscription.endSubscription();
		
		assertThrows(IllegalStateException.class, () -> {
			subscription.activateSubscription();
		});
		
		assertEquals(subscription_status.SUBSCRIPTION_ENDED, subscription.getStatus());
		
	}
	
	/**
	* Tests end subscription updates the correct attributes
	* @throws Exception
	*/
	@Test
	public void testEndSubscription() throws Exception {
		
		TrialPeriodSubscription subscription = TrialPeriodSubscription.builder().build();
		
		subscription.endSubscription();
		
		assertEquals(subscription_status.SUBSCRIPTION_ENDED, subscription.getStatus());
		
	}
	
	/**
	* Tests non admin user cannot activate the subscription
	* @throws Exception
	*/
	@Test
	public void testActivateSubscription_nonAdmin() throws Exception {
	
		TrialPeriodSubscription subscription = TrialPeriodSubscription.builder().build();
		
		TrialPeriodSubscription.getActionHandler();
		assertThrows(IllegalAccessException.class, () -> {
			TrialPeriodSubscription.getActionHandler().performAction(null, subscription, subscription_action.ACTIVATE_SUBSCRIPTION, false);
		});
	
	}
	
	/**
	* Tests non admin user cannot reject the subscription
	* @throws Exception
	*/
	@Test
	public void testRejectSubscription_nonAdmin() throws Exception {
		
		TrialPeriodSubscription subscription = TrialPeriodSubscription.builder().build();
		
		TrialPeriodSubscription.getActionHandler();
		assertThrows(IllegalAccessException.class, () -> {
			TrialPeriodSubscription.getActionHandler().performAction(null, subscription, subscription_action.REJECT_SUBSCRIPTION, false);
		});
		
	}
	
	/**
	* Tests happy path
	* @throws Exception
	*/
	@Test
	public void testPerformAction_ActivateSubscription_nonAdmin() throws Exception {
		
		TrialPeriodSubscription subscription = TrialPeriodSubscription
																.builder()
																	.status(subscription_status.AWAITING_ACTIVATION)
																	.currentSubscription(false)
																.build();
		
		TrialPeriodSubscription.getActionHandler().performAction(null, subscription, subscription_action.ACTIVATE_SUBSCRIPTION, true);
		
		assertEquals(subscription_status.ACTIVE, subscription.getStatus());
		assertTrue(subscription.isCurrentSubscription());
		assertNotNull(subscription.getActivatedDate());
		
	}
	
	/**
	* Tests happy path
	* @throws Exception
	*/
	@Test
	public void testPerformAction_RejectSubscription_nonAdmin() throws Exception {
		
		TrialPeriodSubscription subscription = TrialPeriodSubscription
																.builder()
																	.status(subscription_status.AWAITING_ACTIVATION)
																	.currentSubscription(false)
																.build();
		
		TrialPeriodSubscription.getActionHandler().performAction(null, subscription, subscription_action.REJECT_SUBSCRIPTION, true);
		
		assertEquals(subscription_status.SUBSCRIPTION_ENDED, subscription.getStatus());
		assertFalse(subscription.isCurrentSubscription());
		
	}
	
	/**
	* Tests happy path
	* @throws Exception
	*/
	@Test
	public void testPerformAction_endActiveSubscription_nonAdmin() throws Exception {
		
		TrialPeriodSubscription subscription = TrialPeriodSubscription
																.builder()
																	.status(subscription_status.ACTIVE)
																	.currentSubscription(false)
																.build();
		
		TrialPeriodSubscription.getActionHandler().performAction(null, subscription, subscription_action.END_SUBSCRIPTION, true);
		
		assertEquals(subscription_status.SUBSCRIPTION_ENDED, subscription.getStatus());
		assertFalse(subscription.isCurrentSubscription());
		
	}
	
}