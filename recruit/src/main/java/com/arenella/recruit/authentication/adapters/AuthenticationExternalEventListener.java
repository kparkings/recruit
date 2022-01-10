package com.arenella.recruit.authentication.adapters;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterHasOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;

/**
* Defines functionality for listening to Events from external Services
* @author K Parkings
*/
public interface AuthenticationExternalEventListener {

	/**
	* Listens for events relating to the creation of a Recruiter
	* @param event - RecruiterCreatedEvent
	*/
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event);
	
	/**
	* Listens for event informing that a Recruiter has no 
	* Open subscriptions
	*/
	public void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent event);
	
	/**
	* Listens for event informing that a Recruiter has received 
	* an open Subscription
	*/
	public void listenForRecruiterHasOpenSubscriptionEvent(RecruiterHasOpenSubscriptionEvent event);
	
}
