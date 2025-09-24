package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Unit tests for the SubscriptionAddedEvent class
* @author K Parkings
*/
class SubscriptionAddedEventTest {

	final String 			recruiterId 		= "rec33";
	final subscription_type subscriptionType 	= subscription_type.SIX_MONTHS_SUBSCRIPTION;
	
	/**
	* Tests the Constructor
	* @throws Exception
	*/
	@Test
	void testConstructr() {
		
		SubscriptionAddedEvent event = new SubscriptionAddedEvent(recruiterId, subscriptionType);
		
		assertEquals(recruiterId, 		event.getRecruiterId());
		assertEquals(subscriptionType, 	event.getSubscriptionType());
		
	}
	
}
