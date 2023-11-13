package com.arenella.recruit.recruiters.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.recruiters.beans.TrialPeriodSubscriptionActionHandler;
import com.arenella.recruit.recruiters.beans.PaidPeriodSubscriptionActionHandler;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Unit tests for the RecruiterSubscriptionFactory class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class RecruiterSubscriptionFactoryTest {
	
	@InjectMocks
	private RecruiterSubscriptionFactory factory = new RecruiterSubscriptionFactory();
	
	@Mock
	private TrialPeriodSubscriptionActionHandler 	trialPeriodActionHander;
	
	@Mock
	private PaidPeriodSubscriptionActionHandler 	paidPeriodSubscriptionActionHandler;
	
	
	/**
	* Tests exception thrown if type is null
	* @throws Exception
	*/
	@Test
	public void testNoTypeSpecified() throws Exception {
		
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
		
		assertNotNull(factory.getActionHandlerByType(subscription_type.FIRST_GEN));
		assertNotNull(factory.getActionHandlerByType(subscription_type.TRIAL_PERIOD));
		assertNotNull(factory.getActionHandlerByType(subscription_type.YEAR_SUBSCRIPTION));
		
	}
	
	/**
	* Tests Exception is thrown if Action used with CREDIT_BASED_SUBSCRIPTION
	* @return
	* @throws Exception
	*/
	@Test
	public void testgetActionHandlerByType_credits() throws Exception{
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.factory.getActionHandlerByType(subscription_type.CREDIT_BASED_SUBSCRIPTION);
		});
		
	}
}
