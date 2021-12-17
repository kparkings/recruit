package com.arenella.recruit.authentication.adapters;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;

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
	
}
