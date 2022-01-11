package com.arenella.recruit.recruiters.adapters;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;

/**
* Defines functionality for publishing Events to external services 
* @author K Parkings
*/
public interface RecruitersExternalEventPublisher {

	/**
	* Posts an event informing the world that a Recruiter account was 
	* created
	* @param event - Event advertising the fact a new User account for a recruiter was created
	*/
	public void publishRecruiterAccountCreatedEvent(RecruiterCreatedEvent event);

	/**
	* Publishes an event informing that a Recruiter has no open subscriptions
	* @param recruiterId - Recruiter event relates to
	*/
	public void publishRecruiterNoOpenSubscriptionsEvent(String recruiterId);

	/**
	* Publishes an Event informing that a Recruiter has an Open subscription
	* @param recruiterId - Recruiter event relates to
	*/
	public void publishRecruiterHasOpenSubscriptionEvent(String recruiterId);
	
}
