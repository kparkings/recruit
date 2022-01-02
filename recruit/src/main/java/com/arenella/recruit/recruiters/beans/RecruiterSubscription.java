package com.arenella.recruit.recruiters.beans;

import java.time.LocalDateTime;
import java.util.UUID;

/**
* Represents a Subscription that the Recruiter that gives them the right
* to access features in the site
* @author K Parkings
*/
public interface RecruiterSubscription {

	static enum subscription_type 	{FIRST_GEN, TRIAL_PERIOD, YEAR_SUBSCRIPTION}
	static enum subscription_status {AWAITING_ACTIVATION, ACTIVE, DISABLED_PENDING_PAYMENT, SUBSCRIPTION_ENDED}
	
	/**
	* Returns the unique Id of the Subscription
	* @return Unique Id of the subscription
	*/
	public UUID getSubscriptionId();
	
	/**
	* Returns when the Subscriptions was created
	* @return Creation Date
	*/
	public LocalDateTime getCreated();
	
	/**
	* Returns when the Subscription is considered active from
	* @return When the subscriptions becomes active
	*/
	public LocalDateTime getActivatedDate();
	
	/**
	* Returns the uniqueId of the Recruiter the Subscription is
	* associated with 
	* @return Id of Recruiter
	*/
	public String getRecruiterId();
	
	/**
	* Returns whether or not the subscription is active or not
	* @return Whether the Subscription is active
	*/
	public boolean isCurrentSubscription();
	
	/**
	* Returns the type of the Subscription
	* @return Type of Subscription
	*/
	public subscription_type getType();
	
	/**
	* Returns the current status of the Subscription
	* @return
	*/
	public subscription_status getStatus();
	
	//public Set<SubscriptionActions> getActions();
	
}
