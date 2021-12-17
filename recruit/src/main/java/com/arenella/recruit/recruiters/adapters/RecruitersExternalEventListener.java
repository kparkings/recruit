package com.arenella.recruit.recruiters.adapters;

import com.arenella.recruit.adapters.events.RecruiterUserAccountCreatedEvent;

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
	
}
