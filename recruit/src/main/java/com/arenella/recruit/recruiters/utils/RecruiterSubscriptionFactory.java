package com.arenella.recruit.recruiters.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arenella.recruit.recruiters.beans.CreditBasedSubscriptionActionHandler;
import com.arenella.recruit.recruiters.beans.FirstGenRecruiterSubscription;
import com.arenella.recruit.recruiters.beans.PaidPeriodSubscriptionActionHandler;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;
import com.arenella.recruit.recruiters.beans.TrialPeriodSubscriptionActionHandler;

/**
* Factory for retrieving a RecruiterSubscription 
* based upon the type
* @author K Parkings
*/
@Component
public class RecruiterSubscriptionFactory {
	
	//TODO: Remove all Trial period code
	@Autowired
	private TrialPeriodSubscriptionActionHandler 	trialPeriodActionHandler;
	
	@Autowired
	private CreditBasedSubscriptionActionHandler 	creditBasedActionHandler;
	
	
	@Autowired
	private PaidPeriodSubscriptionActionHandler 	paidPeriodSubscriptionActionHandler;
	
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
				case FIRST_GEN -> FirstGenRecruiterSubscription.getActionHandler();
				case TRIAL_PERIOD -> trialPeriodActionHandler;
				case ONE_MONTH_SUBSCRIPTION, 
					 THREE_MONTHS_SUBSCRIPTION, 
					 SIX_MONTHS_SUBSCRIPTION, 
					 YEAR_SUBSCRIPTION -> paidPeriodSubscriptionActionHandler;
				case CREDIT_BASED_SUBSCRIPTION -> creditBasedActionHandler;
				default ->  throw new IllegalArgumentException("Unknown subscriptionType: "  + subscriptionType);
		};
		
	}
	
}
