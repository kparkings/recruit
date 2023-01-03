package com.arenella.recruit.emailservice.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.emailservice.beans.Recipient;
import com.arenella.recruit.emailservice.beans.Recipient.RECIPIENT_TYPE;
import com.arenella.recruit.emailservice.services.EmailDispatcherService;
import com.arenella.recruit.emailservice.services.RecipientService;


/**
* Implementation of EmailServiceExternalEventListener optimized to 
* work for when the services are physically located in 
* the same Monolith.
* @author K Parkings
*/
@Service
public class EmailServiceMonolithExternalEventListener implements EmailServiceExternalEventListener{

	@Autowired
	private EmailDispatcherService 	emailService;
	
	@Autowired
	private RecipientService		recipientService;
	
	/**
	* Refer to EmailServiceExternalEventListener interface for details 
	*/
	@Override
	public void listenForSendEmailCommand(RequestSendEmailCommand command) {
		this.emailService.handleSendEmailCommand(command);
	}

	/**
	* Refer to EmailServiceExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event) {
		this.recipientService.addRecipient(new Recipient(event.getRecruiterId(), RECIPIENT_TYPE.RECRUITER, event.getFirstName(), event.getEmail()));
	}


	//TODO: 1. Need to listen for event for recruiter created
	//		2. Need table to store recruiterId + RecruierEmal
	//		3. RecruiterAccountCreatedEvent does not contain email or name currently??
	//		4. Change RequestSendEmailCommand to just have id's of recruiters
	//		5. EmailService to fetch recruiters email from own DB schema
	

}