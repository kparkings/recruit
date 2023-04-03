package com.arenella.recruit.candidates.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.CandidateAccountCreatedEvent;
import com.arenella.recruit.adapters.events.CandidatePasswordUpdatedEvent;
import com.arenella.recruit.adapters.events.CandidateUpdatedEvent;
import com.arenella.recruit.authentication.adapters.AuthenticationExternalEventListener;
import com.arenella.recruit.emailservice.adapters.EmailServiceExternalEventListener;


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
public class CandidatesMonolithExternalEventPublisher implements CandidatesExternalEventPublisher{

	@Autowired
	private AuthenticationExternalEventListener authenticationExternalEventListener;
	
	@Autowired
	private EmailServiceExternalEventListener emailServiceExternalEventListener;
	
	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishCandidateAccountCreatedEvent(CandidateAccountCreatedEvent event) {
		this.authenticationExternalEventListener.listenForCandidateAccountCreatedEvent(event);
		this.emailServiceExternalEventListener.listenForCandidateCreatedEvent(event);
	}

	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishCandidatePasswordUpdated(CandidatePasswordUpdatedEvent event) {
		this.authenticationExternalEventListener.listenForCandidatePasswordUpdatedEvent(event);
	}

	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishCandidateAccountUpdatedEvent(CandidateUpdatedEvent event) {
		this.emailServiceExternalEventListener.listenForCandidateUpdatedEvent(event);
	}
	
}