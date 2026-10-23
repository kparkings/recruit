package com.arenella.recruit.messaging.adapters;

import com.arenella.recruit.adapters.events.IMMessageCreatedEvent;
import com.arenella.recruit.adapters.events.IMMessageReadEvent;
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
	
	/**
	* Published an event stating that an Instant message was created
	* @param imMessageCreatedEvent - Details of the IM
	*/
	public void publishEvent(IMMessageCreatedEvent imMessageCreatedEvent);
	
	/**
	* Published an event stating that an Instant message was read
	* @param imMessageReadEvent - Details of the IM
	*/
	public void publishEvent(IMMessageReadEvent imMessageReadEvent);

}
