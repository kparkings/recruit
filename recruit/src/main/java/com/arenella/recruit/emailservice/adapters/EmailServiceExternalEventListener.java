package com.arenella.recruit.emailservice.adapters;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;

/**
* Defines functionality for listening to Events from external Services
* @author K Parkings
*/
public interface EmailServiceExternalEventListener {

	/**
	* Listens for events requesting an email is sent
	* @param command - Command to request sending of email
	*/
	public void listenForSendEmailCommand(RequestSendEmailCommand command);

	/**
	* Listens for events for the creation of a new recruiter
	* @param event - Recruiter created
	*/
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event);
	
}
