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
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Unit tests for the PaidPeriodRecruiterSubscription class
* @author K Parkings
*/
public class PaidPeriodRecruiterSubscriptionTest {

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
		final subscription_type		type				= subscription_type.SIX_MONTHS_SUBSCRIPTION;
		final subscription_status 	status 				= subscription_status.ACTIVE;
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
																			.builder()
																				.activateDate(activatedDate)
																				.created(created)
																				.recruiterId(recruiterId)
																				.subscriptionId(subscriptionId)
																				.status(status)
																				.type(type)
																				.currentSubscription(true)
																			.build();
		
		assertEquals(activatedDate, 											subscription.getActivatedDate());
		assertEquals(created, 													subscription.getCreated());
		assertEquals(recruiterId, 												subscription.getRecruiterId());
		assertEquals(status, 													subscription.getStatus());
		assertEquals(type, 														subscription.getType());
		assertEquals(subscriptionId, 											subscription.getSubscriptionId());
		assertEquals(type, 														subscription.getType());
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
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
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
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
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
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
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
	
	/**
	* Tests renewal of a Subscription. This occurs when the end of the 
	* subscription period has arrived and a new payment is required
	* @throws Exception
	*/
	@Test
	public void testRenewSubscription() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final LocalDateTime			activationDate		= LocalDateTime.of(2021, 12, 18, 10, 10).minusYears(1);
		final subscription_status 	status 				= subscription_status.ACTIVE;
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
				.builder()
					.created(created)
					.recruiterId(recruiterId)
					.subscriptionId(subscriptionId)
					.status(status)
					.activateDate(activationDate)
					.currentSubscription(true)
				.build();
		
		assertEquals(status, 			subscription.getStatus());
		assertEquals(activationDate, 	subscription.getActivatedDate());
		
		subscription.renewSubscription();
		
		assertEquals(subscription_status.ACTIVE_PENDING_PAYMENT, 	subscription.getStatus());
		assertEquals(activationDate.plusYears(1), 					subscription.getActivatedDate());
		
	}
	
	/**
	* Tests check to see if the subscription period has elapsed since the last activation date
	*/
	@Test
	public void testPeriodElapsedSinceActivation() throws Exception {

		assertTrue(PaidPeriodRecruiterSubscription.hasPeriodElapsedSinceActivation(PaidPeriodRecruiterSubscription.builder().type(subscription_type.YEAR_SUBSCRIPTION).activateDate(LocalDateTime.now().minusMonths(13)).build()));
		assertFalse(PaidPeriodRecruiterSubscription.hasPeriodElapsedSinceActivation(PaidPeriodRecruiterSubscription.builder().type(subscription_type.YEAR_SUBSCRIPTION).activateDate(LocalDateTime.now().minusMonths(1)).build()));
		
		assertTrue(PaidPeriodRecruiterSubscription.hasPeriodElapsedSinceActivation(PaidPeriodRecruiterSubscription.builder().type(subscription_type.ONE_MONTH_SUBSCRIPTION).activateDate(LocalDateTime.now().minusDays(60)).build()));
		assertFalse(PaidPeriodRecruiterSubscription.hasPeriodElapsedSinceActivation(PaidPeriodRecruiterSubscription.builder().type(subscription_type.ONE_MONTH_SUBSCRIPTION).activateDate(LocalDateTime.now().minusDays(1)).build()));
		
		assertTrue(PaidPeriodRecruiterSubscription.hasPeriodElapsedSinceActivation(PaidPeriodRecruiterSubscription.builder().type(subscription_type.THREE_MONTHS_SUBSCRIPTION).activateDate(LocalDateTime.now().minusMonths(4)).build()));
		assertFalse(PaidPeriodRecruiterSubscription.hasPeriodElapsedSinceActivation(PaidPeriodRecruiterSubscription.builder().type(subscription_type.THREE_MONTHS_SUBSCRIPTION).activateDate(LocalDateTime.now().minusMonths(2)).build()));
		
		assertTrue(PaidPeriodRecruiterSubscription.hasPeriodElapsedSinceActivation(PaidPeriodRecruiterSubscription.builder().type(subscription_type.SIX_MONTHS_SUBSCRIPTION).activateDate(LocalDateTime.now().minusMonths(7)).build()));
		assertFalse(PaidPeriodRecruiterSubscription.hasPeriodElapsedSinceActivation(PaidPeriodRecruiterSubscription.builder().type(subscription_type.SIX_MONTHS_SUBSCRIPTION).activateDate(LocalDateTime.now().minusMonths(5)).build()));
		
		
	}
	
}