package com.arenella.recruit.emailservice.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.emailservice.services.EmailDispatcherService;


/**
* Implementation of EmailServiceExternalEventListener optimised to 
* work for when the services are physically located in 
* the same Monolith.
* @author K Parkings
*/
@Service
public class EmailServiceMonolithExternalEventListener implements EmailServiceExternalEventListener{

	@Autowired
	private EmailDispatcherService emailService;
	
	/**
	* Refer to EmailServiceExternalEventListener interface for details 
	*/
	@Override
	public void listenForSendEmailCommand(RequestSendEmailCommand command) {
		this.emailService.handleSendEmailCommand(command);
	}

	

}