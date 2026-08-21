package com.arenella.recruit.campaigns.adapters;

/**
* Defines functionality for publishing Events to external services 
* @author K Parkings
*/
public interface CampaignExternalEventPublisher {
	
	/**
	* Published a command to send an email
	* @param command - Contains details of email to be sent
	*/
	public void publishSendEmailCommand(ExternalCandiateMessageSendEmailCommand command);

	
}
