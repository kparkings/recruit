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
	
}
