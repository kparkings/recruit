package com.arenella.recruit.recruiters.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterHasOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.authentication.adapters.AuthenticationExternalEventListener;
import com.arenella.recruit.emailservice.adapters.EmailServiceExternalEventListener;
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
public class RecruitersMonolithExternalEventPublisher implements RecruitersExternalEventPublisher{

	@Autowired
	private AuthenticationExternalEventListener authenticationExternalEventListener;
	
	@Autowired
	private EmailServiceExternalEventListener emailServiceExternalEventListener;
	
	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishRecruiterAccountCreatedEvent(RecruiterCreatedEvent event) {
		this.authenticationExternalEventListener.listenForRecruiterCreatedEvent(event);
	}

	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishRecruiterNoOpenSubscriptionsEvent(String recruiterId) {
		this.authenticationExternalEventListener.listenForRecruiterNoOpenSubscriptionsEvent(new RecruiterNoOpenSubscriptionEvent(recruiterId));
	}

	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishRecruiterHasOpenSubscriptionEvent(String recruiterId) {
		this.authenticationExternalEventListener.listenForRecruiterHasOpenSubscriptionEvent(new RecruiterHasOpenSubscriptionEvent(recruiterId));
	}

	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishSendEmailCommand(RequestSendEmailCommand command) {
		this.emailServiceExternalEventListener.listenForSendEmailCommand(command);
	}

}