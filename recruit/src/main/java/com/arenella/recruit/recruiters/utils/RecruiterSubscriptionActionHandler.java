package com.arenella.recruit.recruiters.utils;

import java.util.Optional;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.SubscriptionActionFeedback;

/**
* Defines an class as being an ActionHandler for a RecruiterSubscription. The 
* ActionHandlers are responsible for validation and carrying out actions on a
* Recruiters subscription;
* @author K Parkings
*/
public interface RecruiterSubscriptionActionHandler {
	
	/**
	* Performs the action on the subscription
	* @param recruiter		- Recruiter the Subscription belongs to 
	* @param subscription 	- Subscription to perform the action on
	* @param action 		- Action to perform
	* @param isAdminUser	- Whether user is admin. Some actions are restricted to Admin users
	* @return Some feedback appropriate to the action performed
	* @throws IllegalAccessException
	*/
	public Optional<SubscriptionActionFeedback> performAction(Recruiter recruiter, RecruiterSubscription subscription, subscription_action action, Boolean isAdminUser) throws IllegalAccessException;
	
}