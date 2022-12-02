package com.arenella.recruit.emailservice.adapters;

/**
* Defines functionality for listening to Events from external Services
* @author K Parkings
*/
public interface EmailServiceExternalEventListener {

	/**
	* Listens for events requesting an email is sent
	* @param event - RecruiterCreatedEvent
	*/
	public void listenForEmailEvent(RequestSendEmailEvent event);
	
}
