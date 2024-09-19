package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Unit tests for the SubscriptionAPIInbound class
* @author K Parkings
*/
public class SubscriptionAPIInboundTest {

	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		final subscription_type type = subscription_type.CREDIT_BASED_SUBSCRIPTION;
		
		SubscriptionAPIInbound subscription = SubscriptionAPIInbound.builder().type(type).build();
		
		assertEquals(subscription.getType(), type);
		
	}
	
}
