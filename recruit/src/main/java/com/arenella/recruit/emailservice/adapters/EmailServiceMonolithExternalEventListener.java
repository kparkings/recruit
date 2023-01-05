package com.arenella.recruit.emailservice.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.RecipientType;
import com.arenella.recruit.emailservice.beans.Recipient;
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
		this.recipientService.addRecipient(new Recipient(event.getRecruiterId(), RecipientType.RECRUITER, event.getFirstName(), event.getEmail()));
	}

}