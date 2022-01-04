package com.arenella.recruit.recruiters.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Unit tests for the RecruiterSubscriptionFactory class
* @author K Parkings
*/
public class RecruiterSubscriptionFactoryTest {

	/**
	* Tests exception thrown if type is null
	* @throws Exception
	*/
	@Test
	public void testNoTypeSpecified() throws Exception {
		
		RecruiterSubscriptionFactory factory = new RecruiterSubscriptionFactory();
		
		assertThrows(IllegalArgumentException.class, () -> {
			factory.getActionHandlerByType(null);
		});
		
	}
	
	/**
	* Tests factory returns correct ActionHandler for type
	* @throws Exception
	*/
	@Test
	public void testGetActionHandlers() throws Exception {
		
		RecruiterSubscriptionFactory factory = new RecruiterSubscriptionFactory();
		
		assertNotNull(factory.getActionHandlerByType(subscription_type.FIRST_GEN));
		assertNotNull(factory.getActionHandlerByType(subscription_type.TRIAL_PERIOD));
		assertNotNull(factory.getActionHandlerByType(subscription_type.YEAR_SUBSCRIPTION));
		
	}
}
