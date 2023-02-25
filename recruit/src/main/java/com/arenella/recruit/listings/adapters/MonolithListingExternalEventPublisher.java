package com.arenella.recruit.listings.adapters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arenella.recruit.emailservice.adapters.EmailServiceExternalEventListener;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand.Attachment;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.RecipientType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;

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
@Component
public class MonolithListingExternalEventPublisher implements ExternalEventPublisher{

	@Autowired
	private EmailServiceExternalEventListener 	emailServiceListener;
	
	/**
	* Refer to ExternalEventPublisher for details 
	*/
	@Override
	public void publicRequestSendListingContactEmailCommand(RequestListingContactEmailCommand command) {
	
		//TODO: [KP] Need to send two emails. 	1. exten to inform Recruiter of new messat
		//										2. interne with actual message and attachements
	
		Map<String, Object> modelExt = new HashMap<>();
		modelExt.put("listingName", 	command.getListingName());
		modelExt.put("senderName", 		command.getSenderName());
		modelExt.put("recipientId", 	command.getRecruiterId());
		
		Map<String, Object> modelInt = new HashMap<>();
		modelInt.put("file", 			command.getFile());
		modelInt.put("fileType", 		command.getFileType());
		modelInt.put("listingName", 	command.getListingName());
		modelInt.put("message", 		command.getMessage());
		modelInt.put("senderEmail", 	command.getSenderEmail());
		modelInt.put("senderName", 		command.getSenderName());
		modelInt.put("recipientId", 	command.getRecruiterId());
		
		RequestSendEmailCommand cInt = 
				RequestSendEmailCommand
					.builder()
						.emailType(EmailType.INTERN)
						.model(modelInt)
						.persistable(true)
						.recipients(Set.of(new EmailRecipient<String>(command.getRecruiterId(), RecipientType.RECRUITER)))
						.sender(new Sender<>(UUID.randomUUID(), SenderType.SYSTEM, "kparkings@gmail.com"))
						.title("Arenella-ICT - Reaction To Job Posting")
						.topic(EmailTopic.LISTING_RECRUITER_CONTACT_REQUEST)
						.attachments(Set.of(new Attachment(command.getFileType(), command.getFile())))
					.build();
		
		this.emailServiceListener.listenForSendEmailCommand(cInt);
		
		RequestSendEmailCommand cExt = 
				RequestSendEmailCommand
					.builder()
						.emailType(EmailType.EXTERN)
						.model(modelExt)
						.persistable(false)
						.recipients(Set.of(new EmailRecipient<String>(command.getRecruiterId(), RecipientType.RECRUITER)))
						.sender(new Sender<>(UUID.randomUUID(), SenderType.SYSTEM, "kparkings@gmail.com"))
						.title("Arenella-ICT - Reaction To Job Posting")
						.topic(EmailTopic.LISTING_RECRUITER_CONTACT_REQUEST)
					.build();
		
		this.emailServiceListener.listenForSendEmailCommand(cExt);
		
	}

}
