package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;

/**
* Unit tests for the YearlyRecruiterSubscription class
* @author K Parkings
*/
public class YearlyRecruiterSubscriptionTest {

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
		
		YearlyRecruiterSubscription subscription = YearlyRecruiterSubscription
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
		assertEquals(RecruiterSubscription.subscription_type.YEAR_SUBSCRIPTION, subscription.getType());
		assertTrue(subscription.isCurrentSubscription());
		
	}
	
	/**
	* Tests activation of a Subscription
	* @throws Exception
	*/
	@Test
	public void testActivateSubscription() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.AWAITING_ACTIVATION;
		
		YearlyRecruiterSubscription subscription = YearlyRecruiterSubscription
				.builder()
					.created(created)
					.recruiterId(recruiterId)
					.subscriptionId(subscriptionId)
					.status(status)
					.currentSubscription(true)
				.build();
		
		assertNull(subscription.getActivatedDate());
		assertEquals(status, subscription.getStatus());
		
		subscription.activateSubscription();
		
		assertNotNull(subscription.getActivatedDate());
		assertEquals(subscription_status.ACTIVE, subscription.getStatus());
		
	}
	
	/**
	* Tests disabling of description due to an unpaid invoice
	* @throws Exception
	*/
	@Test
	public void testDisablePendingPayment() throws Exception {
		
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
		
		assertEquals(status, subscription.getStatus());
		
		subscription.disablePendingPayment();
		
		assertEquals(subscription_status.DISABLED_PENDING_PAYMENT, subscription.getStatus());
		
	}
	
	/**
	* Tests disabling of description due to an unpaid invoice
	* @throws Exception
	*/
	@Test
	public void testEndSubscription() throws Exception {
		
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
		
		assertEquals(status, subscription.getStatus());
		assertTrue(subscription.isCurrentSubscription());
		
		subscription.endSubscription();
		
		assertEquals(subscription_status.SUBSCRIPTION_ENDED, subscription.getStatus());
		assertFalse(subscription.isCurrentSubscription());
		
	}
	

	
}