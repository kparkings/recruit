package com.arenella.recruit.recruiters.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.authentication.adapters.AuthenticationExternalEventListener;

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
	
	/**
	* Refer to the ExternalEventPublisher interface for details 
	*/
	@Override
	public void publishRecruiterAccountCreatedEvent(RecruiterCreatedEvent event) {
		
		authenticationExternalEventListener.listenForRecruiterCreatedEvent(null);
		
	}

}