package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;

/**
* Unit tests for the FirstGenRecruiterSubscription class
* @author K Parkings
*/
public class FirstGenRecruiterSubscriptionTest {

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
		
		FirstGenRecruiterSubscription subscription = FirstGenRecruiterSubscription
																			.builder()
																				.activateDate(activatedDate)
																				.created(created)
																				.recruiterId(recruiterId)
																				.subscriptionId(subscriptionId)
																				.status(status)
																				.currentSubscription(true)
																			.build();
		
		assertEquals(activatedDate, 									subscription.getActivatedDate());
		assertEquals(created, 											subscription.getCreated());
		assertEquals(recruiterId, 										subscription.getRecruiterId());
		assertEquals(status, 											subscription.getStatus());
		assertEquals(subscriptionId, 									subscription.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.FIRST_GEN, subscription.getType());
		assertTrue(subscription.isCurrentSubscription());
		
	}
	
	/**
	* Test it is possible to end the Subscription 
	*/
	@Test
	public void testEndSubscription() {
		
		FirstGenRecruiterSubscription subscription = FirstGenRecruiterSubscription
																			.builder()
																			.status(subscription_status.ACTIVE)
																			.currentSubscription(true)
																			.build();
	
		assertEquals(subscription_status.ACTIVE, subscription.getStatus());
		assertTrue(subscription.isCurrentSubscription());
		
		subscription.endSubscription();
	
		assertEquals(subscription_status.SUBSCRIPTION_ENDED, subscription.getStatus());
		assertFalse(subscription.isCurrentSubscription());
		
	}
	
}