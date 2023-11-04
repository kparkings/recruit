package com.arenella.recruit.recruiters.adapters;

import com.arenella.recruit.adapters.events.RecruiterHasOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;

/**
* Defines Listener for external Events relating to listings 
* @author K Parkings
*/
public interface ListingsExternalEventListener {

	/**
	* Listener for event indicating that the Recruiter has no active subscription
	* @param recruiterNoOpenSubscriptionEvent
	*/
	void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent recruiterNoOpenSubscriptionEvent);

	/**
	* Listener for event indicating that the Recruiter has active subscription
	* @param recruiterHasOpenSubscriptionEvent
	*/
	void listenForRecruiterHasOpenSubscriptionsEvent(RecruiterHasOpenSubscriptionEvent recruiterHasOpenSubscriptionEvent);

}
