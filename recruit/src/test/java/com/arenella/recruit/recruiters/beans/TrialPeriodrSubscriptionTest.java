package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

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
																.build();
		
		assertEquals(activatedDate, 											subscription.getActivatedDate());
		assertEquals(created, 													subscription.getCreated());
		assertEquals(recruiterId, 												subscription.getRecruiterId());
		assertEquals(status, 													subscription.getStatus());
		assertEquals(subscriptionId, 											subscription.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.TRIAL_PERIOD, subscription.getType());
		
	}
	
}