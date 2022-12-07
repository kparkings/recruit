package com.arenella.recruit.emailservice.adapters;

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
	
}
