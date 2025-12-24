package com.arenella.recruit.messaging.adapters;

import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;

/**
* Defines functionality for publishing Events to external services 
* @author K Parkings
*/
public interface MessagingExternalEventPublisher {

	/**
	* Published a command to send an email
	* @param command - Contains details of email to be sent
	*/
	public void publishSendEmailCommand(RequestSendEmailCommand command);

}
