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

	/**
	* Published a command to send an email to an External Candidate to obtain their permission 
	* to store their details in the system 
	* @param command - Contains details of email to be send
	*/
	public void publishExternalCandiateMessageSendEmailCommand(ExternalCandiateAddedToSystemSendEmailCommand command);
	
}
