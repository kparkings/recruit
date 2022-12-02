package com.arenella.recruit.emailservice.adapters;

import org.springframework.stereotype.Service;


/**
* Implementation of EmailServiceExternalEventListener optimised to 
* work for when the services are physically located in 
* the same Monolith.
* @author K Parkings
*/
@Service
public class EmailServiceMonolithExternalEventListener implements EmailServiceExternalEventListener{

	/**
	* Refer to EmailServiceExternalEventListener interface for details 
	*/
	@Override
	public void listenForEmailEvent(RequestSendEmailEvent event) {
		
	}

	

}