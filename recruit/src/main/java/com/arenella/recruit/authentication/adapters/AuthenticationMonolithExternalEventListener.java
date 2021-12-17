package com.arenella.recruit.authentication.adapters;

import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;

/**
* Implementation of ExternalEventListener optimised to 
* work for when the services are physically located in 
* the same Monolith.
* @author K Parkings
*/
@Service
public class AuthenticationMonolithExternalEventListener implements AuthenticationExternalEventListener{

	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event) {
		// TODO Auto-generated method stub
		
	}

}