package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;
import com.arenella.recruit.recruiters.utils.RecruiterSubscriptionActionHandler;

/**
* Unit tests for the YearlySubscriptionActionHandler class
* @author K Parkings
*/
public class YearlySubscriptionActionHandlerTest {

	/**
	* Tests if a non admin user attempts to activate the subscription an exception
	* is thrown
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_activateSubscription_nonAdmin() throws Exception {
		
		RecruiterSubscriptionActionHandler actionHandler = new YearlySubscriptionActionHandler();
		
		assertThrows(IllegalAccessException.class, () -> {
			actionHandler.performAction(null, null, subscription_action.ACTIVATE_SUBSCRIPTION, false);
		});
		
	}
	
	/**
	* Tests if a non admin user attempts to disable the subscription an exception
	* is thrown
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_disableSubscription_nonAdmin() throws Exception {
		
		RecruiterSubscriptionActionHandler actionHandler = new YearlySubscriptionActionHandler();
		
		assertThrows(IllegalAccessException.class, () -> {
			actionHandler.performAction(null, null, subscription_action.DISABLE_PENDING_PAYMENT, false);
		});
		
	}
	
	/**
	* Tests if an admin user attempts to activate but the subscription is in an invalid state for 
	* this actions
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_activateSubscription_admin_invalidState() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.ACTIVE;
		
		YearlyRecruiterSubscription subscription = YearlyRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																		.build();
		
		RecruiterSubscriptionActionHandler actionHandler = new YearlySubscriptionActionHandler();
		
		assertThrows(IllegalStateException.class, () -> {
			actionHandler.performAction(null, subscription, subscription_action.ACTIVATE_SUBSCRIPTION, true);
		});
		
	}
	
	/**
	* Happy Path
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_activateSubscriptionInActivePendingPayement_admin() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.ACTIVE_PENDING_PAYMENT;
		
		YearlyRecruiterSubscription subscription = YearlyRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																		.build();
		
		RecruiterSubscriptionActionHandler actionHandler = new YearlySubscriptionActionHandler();
		
		actionHandler.performAction(null, subscription, subscription_action.ACTIVATE_SUBSCRIPTION, true);
		
		assertNotNull(subscription.getActivatedDate());
		assertEquals(subscription_status.ACTIVE, subscription.getStatus());
		
	}

	/**
	* Happy Path
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_activateSubscriptionInDisabledPendingPayement_admin() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.DISABLED_PENDING_PAYMENT;
		
		YearlyRecruiterSubscription subscription = YearlyRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																		.build();
		
		RecruiterSubscriptionActionHandler actionHandler = new YearlySubscriptionActionHandler();
		
		actionHandler.performAction(null, subscription, subscription_action.ACTIVATE_SUBSCRIPTION, true);
		
		assertNotNull(subscription.getActivatedDate());
		assertEquals(subscription_status.ACTIVE, subscription.getStatus());
		
	}
	
	/**
	* Tests if an admin user attempts to disable pending payment but the subscription is in an invalid state for 
	* this actions
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_disablePendingPayment_admin_invalidState() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.ACTIVE;
		
		YearlyRecruiterSubscription subscription = YearlyRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																		.build();
		
		RecruiterSubscriptionActionHandler actionHandler = new YearlySubscriptionActionHandler();
		
		assertThrows(IllegalStateException.class, () -> {
			actionHandler.performAction(null, subscription, subscription_action.DISABLE_PENDING_PAYMENT, true);
		});
		
	}
	
	/**
	* Happy Path
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_disableSubscriptionInActivePendingPayement_admin() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.ACTIVE_PENDING_PAYMENT;
		
		YearlyRecruiterSubscription subscription = YearlyRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																		.build();
		
		RecruiterSubscriptionActionHandler actionHandler = new YearlySubscriptionActionHandler();
		
		actionHandler.performAction(null, subscription, subscription_action.DISABLE_PENDING_PAYMENT, true);
		
		assertEquals(subscription_status.DISABLED_PENDING_PAYMENT, subscription.getStatus());
		
	}
	
	/**
	* Tests non admin users cannot end a subscription that is disabled because of an unpaid invoice
	* @throws Exception
	*/
	@Test
	public void testEndSubscription_nonAdmin_disabledPendingPayment() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.DISABLED_PENDING_PAYMENT;
		
		YearlyRecruiterSubscription subscription = YearlyRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																		.build();
		
		RecruiterSubscriptionActionHandler actionHandler = new YearlySubscriptionActionHandler();
		
		assertThrows(IllegalAccessException.class, () -> {
			actionHandler.performAction(null, subscription, subscription_action.END_SUBSCRIPTION, false);
		});
		
	}
	
	/**
	* Tests that it is not possible to end a subscriptions which is already ended
	* @throws Exception
	*/
	@Test
	public void testEndSubscription_alreadyEndedt() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.SUBSCRIPTION_ENDED;
		
		YearlyRecruiterSubscription subscription = YearlyRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																		.build();
		
		RecruiterSubscriptionActionHandler actionHandler = new YearlySubscriptionActionHandler();
		
		assertThrows(IllegalStateException.class, () -> {
			actionHandler.performAction(null, subscription, subscription_action.END_SUBSCRIPTION, false);
		});
		
	}

	/**
	* Happy Path
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_endSubscription_admin() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.ACTIVE;
		
		YearlyRecruiterSubscription subscription = YearlyRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																		.build();
		
		RecruiterSubscriptionActionHandler actionHandler = new YearlySubscriptionActionHandler();
		
		actionHandler.performAction(null, subscription, subscription_action.END_SUBSCRIPTION, true);
		
		assertEquals(subscription_status.SUBSCRIPTION_ENDED, subscription.getStatus());
		assertFalse(subscription.isCurrentSubscription());
		
	}
	
}