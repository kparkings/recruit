package com.arenella.recruit.authentication.adapters;

import com.arenella.recruit.adapters.events.RecruiterUserAccountCreatedEvent;

/**
* Defines functionality for publishing Events to external services 
* @author K Parkings
*/
public interface AuthenticationExternalEventPublisher {

	/**
	* Posts an event informing the world that a Recruiter account was 
	* created
	* @param event - Event advertising the fact a new User account for a recruiter was created
	*/
	public void publishRecruiterAccountCreatedEvent(RecruiterUserAccountCreatedEvent event);
	
}
