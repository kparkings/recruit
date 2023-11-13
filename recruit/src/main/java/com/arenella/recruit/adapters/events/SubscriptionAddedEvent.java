package com.arenella.recruit.adapters.events;

import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Event informing that a Subscription was added for a Recruiter 
*/
public class SubscriptionAddedEvent {

	private String 				recruiterId;
	private subscription_type 	subscriptionType;
	
	/**
	* Constructor
	* @param recruiterId 		- Id of Recruiter with new Subscription
	* @param subscriptionType	 - type of subscription that was added 
	*/
	public SubscriptionAddedEvent(String recruiterId, subscription_type subscriptionType) {
		this.recruiterId 		= recruiterId;
		this.subscriptionType 	= subscriptionType;
	}
	
	/**
	* Returns the unique id of the Recruiter
	* @return id of the Recruiter
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}
	
	/**
	* Returns the type of the Subscription added for the Recruiter
	* @return type of the Subscription
	*/
	public subscription_type getSubscriptionType() {
		return this.subscriptionType;
	}
	
}
