package com.arenella.recruit.recruiters.utils;

import org.springframework.stereotype.Component;

import com.arenella.recruit.recruiters.beans.CreditBasedSubscriptionActionHandler;
import com.arenella.recruit.recruiters.beans.PaidPeriodSubscriptionActionHandler;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Factory for retrieving a RecruiterSubscription 
* based upon the type
* @author K Parkings
*/
@Component
public class RecruiterSubscriptionFactory {
	
	private CreditBasedSubscriptionActionHandler 	creditBasedActionHandler;
	private PaidPeriodSubscriptionActionHandler 	paidPeriodSubscriptionActionHandler;
	
	/**
	* 
	* @param creditBasedActionHandler				- Handles actions relating to credit based access
	* @param paidPeriodSubscriptionActionHandler	- Hanles action relating to paid subscription based access
	*/
	public RecruiterSubscriptionFactory(CreditBasedSubscriptionActionHandler creditBasedActionHandler, PaidPeriodSubscriptionActionHandler paidPeriodSubscriptionActionHandler) {
		this.creditBasedActionHandler 				= creditBasedActionHandler;
		this.paidPeriodSubscriptionActionHandler 	= paidPeriodSubscriptionActionHandler;
	}
	
	/**
	* Returns appropriate Action handler for the Subscription type
	* @param subscriptionType - type of Subscription the Action handler is for
	* @return ActionHandler for Subscription type
	*/
	public RecruiterSubscriptionActionHandler getActionHandlerByType(subscription_type subscriptionType) {
		
		if (subscriptionType == null) {
			throw new IllegalArgumentException("Unknown subscriptionType null");
		}
		
		return switch(subscriptionType) {
				case ONE_MONTH_SUBSCRIPTION, 
					 THREE_MONTHS_SUBSCRIPTION, 
					 SIX_MONTHS_SUBSCRIPTION, 
					 YEAR_SUBSCRIPTION -> paidPeriodSubscriptionActionHandler;
				case CREDIT_BASED_SUBSCRIPTION -> creditBasedActionHandler;
				default ->  throw new IllegalArgumentException("Unknown subscriptionType: "  + subscriptionType);
		};
		
	}
	
}