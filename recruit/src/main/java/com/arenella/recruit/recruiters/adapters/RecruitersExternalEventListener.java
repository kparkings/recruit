package com.arenella.recruit.recruiters.adapters;

import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterUserAccountCreatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;

/**
* Defines functionality for listening to Events from external Services
* @author K Parkings
*/
public interface RecruitersExternalEventListener {

	/**
	* Listens for events relating to the creation of a user account 
	* for a Recruiter
	* @param event - RecruiterCreatedEvent
	*/
	public void listenForRecruiterAccountCreatedEvent(RecruiterUserAccountCreatedEvent event);
	
	/**
	* Listener for event indicating that the Recruiter has no active subscription
	* @param recruiterNoOpenSubscriptionEvent
	*/
	void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent recruiterNoOpenSubscriptionEvent);

	/**
	* Listener for event indicating that the Recruiter has active subscription
	* @param recruiterHasOpenSubscriptionEvent
	*/
	void listenForSubscriptionAddedEvent(SubscriptionAddedEvent event);
	
}
