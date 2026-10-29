package com.arenella.recruit.messaging.adapters;

import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.IMMessageCreatedEvent;
import com.arenella.recruit.adapters.events.IMMessageReadEvent;
import com.arenella.recruit.campaigns.adapters.CampaignsExternalEventListener;
import com.arenella.recruit.emailservice.adapters.MessagingEmailServiceExternalEventListener;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;

/**
* An implementation of ExternalEventPublisher optimised to work when the 
* external services are physically located in the same Monolith.
* 
* Here we are calling we are connecting to the other services directly. If the 
* application is ever split middleware will be needed and this direct dependency
* will no longer exist. As this is a monolith we can connect directly but by adding 
* all the external dependencies into EventPublishers/Listeners it will be easy 
* to split the services at a later date
* 
* @author K Parkings
*/
@Service
public class MessagingMonolithExternalEventPublisher implements MessagingExternalEventPublisher{
	
	private MessagingEmailServiceExternalEventListener 		emailServiceExternalEventListener;
	
	private CampaignsExternalEventListener campaignsExternalEventListener;

	public MessagingMonolithExternalEventPublisher(MessagingEmailServiceExternalEventListener emailServiceExternalEventListener, CampaignsExternalEventListener campaignsExternalEventListener) {
		this.emailServiceExternalEventListener = emailServiceExternalEventListener;
		this.campaignsExternalEventListener = campaignsExternalEventListener;
	}
	
	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishSendEmailCommand(RequestSendEmailCommand command) {
		this.emailServiceExternalEventListener.listenForSendEmailCommand(command);
	}

	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishEvent(IMMessageCreatedEvent imMessageCreatedEvent) {
		this.campaignsExternalEventListener.listenFor(imMessageCreatedEvent);
	}

	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishEvent(IMMessageReadEvent imMessageReadEvent) {
		this.campaignsExternalEventListener.listenFor(imMessageReadEvent);
	}
	
}