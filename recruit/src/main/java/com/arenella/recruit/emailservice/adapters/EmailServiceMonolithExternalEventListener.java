package com.arenella.recruit.emailservice.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.emailservice.beans.Contact;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.services.ContactService;
import com.arenella.recruit.emailservice.services.EmailDispatcherService;

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
	private ContactService			contactService;
	
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
		this.contactService.addContact(new Contact(event.getRecruiterId(), ContactType.RECRUITER, event.getFirstName(), event.getEmail()));
	}

	/**
	* Refer to EmailServiceExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterUpdatedEvent(RecruiterUpdatedEvent event) {
		this.contactService.updateContact(new Contact(event.getRecruiterId(), ContactType.RECRUITER, event.getFirstName(), event.getEmail()));
	}

}