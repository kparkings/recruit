package com.arenella.recruit.recruiters.adapters;


import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.RecruiterUserAccountCreatedEvent;

/**
* Implementation of ExternalEventListener optimised to 
* work for when the services are physically located in 
* the same Monolith.
* @author K Parkings
*/
@Service
public class RecruitersMonolithExternalEventListener implements RecruitersExternalEventListener{
	
	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterAccountCreatedEvent(RecruiterUserAccountCreatedEvent event) {
		// TODO Auto-generated method stub
		
	}

}