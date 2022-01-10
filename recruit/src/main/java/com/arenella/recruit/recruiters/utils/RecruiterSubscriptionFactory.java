package com.arenella.recruit.recruiters.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arenella.recruit.recruiters.beans.FirstGenRecruiterSubscription;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;
import com.arenella.recruit.recruiters.beans.TrialPeriodSubscriptionActionHandler;
import com.arenella.recruit.recruiters.beans.YearlySubscriptionActionHandler;

/**
* Factory for retrieving a RecruiterSubscription 
* based upon the type
* @author K Parkings
*/
@Component
public class RecruiterSubscriptionFactory {
	
	@Autowired
	private TrialPeriodSubscriptionActionHandler 	trialPeriodActionHandler;
	
	@Autowired
	private YearlySubscriptionActionHandler 		yearlySubscriptionActionHandler;
	
	/**
	* Returns appropriate Action handler for the Subscription type
	* @param subscriptionType - type of Subscription the Action handler is for
	* @return ActionHandler for Subscription type
	*/
	public RecruiterSubscriptionActionHandler getActionHandlerByType(subscription_type subscriptionType) {
		
		if (subscriptionType == null) {
			throw new IllegalArgumentException("Unknown subscriptionType null");
		}
		
		switch(subscriptionType) {
			case FIRST_GEN:{
				return FirstGenRecruiterSubscription.getActionHandler();
			}
			case TRIAL_PERIOD:{
				return trialPeriodActionHandler;
			}
			case YEAR_SUBSCRIPTION:{
				return yearlySubscriptionActionHandler;
			}
			default:{
				throw new IllegalArgumentException("Unknown subscriptionType: "  + subscriptionType);
			}
		}
		
	}
	
}
