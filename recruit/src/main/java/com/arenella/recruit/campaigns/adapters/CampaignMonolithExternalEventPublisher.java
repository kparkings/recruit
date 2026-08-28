package com.arenella.recruit.campaigns.adapters;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.arenella.recruit.emailservice.adapters.MessagingEmailServiceExternalEventListener;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;

/**
* Monolith implementation of CampaignExternalEventPublisher
* As this is a Monolith version with no middleware connects directly to listeners in other 
* pseudo microservices directly.
*/
@Service
public class CampaignMonolithExternalEventPublisher implements CampaignExternalEventPublisher{

	private final MessagingEmailServiceExternalEventListener 		emailServiceExternalEventListener;
	
	/**
	* 
	* @param emailServiceExternalEventListener
	*/
	public CampaignMonolithExternalEventPublisher(MessagingEmailServiceExternalEventListener emailServiceExternalEventListener) {
		this.emailServiceExternalEventListener = emailServiceExternalEventListener;
	}
	
	/**
	* Refer to the CampaignExternalEventPublisher interface for details 
	*/
	@Override
	public void publishSendEmailCommand(ExternalCandiateMessageSendEmailCommand command) {
		
		Map<String, Object> modelExt = new HashMap<>();
		modelExt.put("message", 			command.getMessage()); 
		modelExt.put("recruiterName", 		command.getRecruiterName()); 
		modelExt.put("recruiterCompany", 	command.getRecruiterCompany()); 
		modelExt.put("recruiterEmail", 		command.getRecruiterEmail()); 
		modelExt.put("campaignOrRole", 		command.getCampaignOrRole()); 
			
		RequestSendEmailCommand cExt = 
				RequestSendEmailCommand
					.builder()
						.emailType(EmailType.EXTERN)
						.model(modelExt)
						.persistable(false)
						.recipients(command.getRecipients())
						.sender(new Sender<>(UUID.randomUUID(), "", SenderType.SYSTEM, "no-reply@arenella-ict.com"))
						.title("Arenella-ICT - Message from Recruiter")
						.topic(EmailTopic.CAMPAIGN_MESSAGE)
					.build();
		
		this.emailServiceExternalEventListener.listenForSendEmailCommand(cExt);
		
	}
	
	/**
	* Refer to the CampaignExternalEventPublisher interface for details 
	*/
	@Override
	public void publishExternalCandiateMessageSendEmailCommand(ExternalCandiateAddedToSystemSendEmailCommand command) {
		
		Map<String, Object> modelExt = new HashMap<>();
		modelExt.put("message", 			command.getMessage()); 
		modelExt.put("recruiterName", 		command.getRecruiterName()); 
		modelExt.put("recruiterCompany", 	command.getRecruiterCompany()); 
		modelExt.put("recruiterEmail", 		command.getRecruiterEmail()); 
		modelExt.put("campaignOrRole", 		command.getCampaignOrRole()); 
			
		RequestSendEmailCommand cExt = 
				RequestSendEmailCommand
					.builder()
						.emailType(EmailType.EXTERN)
						.model(modelExt)
						.persistable(false)
						.recipients(command.getRecipients())
						.sender(new Sender<>(UUID.randomUUID(), "", SenderType.SYSTEM, "no-reply@arenella-ict.com"))
						.title("Arenella-ICT - Your details have been added to our system.")
						.topic(EmailTopic.CAMPAIGN_EXT_CANDIIDATE_ADDED)
					.build();
		
		this.emailServiceExternalEventListener.listenForSendEmailCommand(cExt);
		
	}

}
