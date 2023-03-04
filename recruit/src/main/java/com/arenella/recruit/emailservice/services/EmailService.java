package com.arenella.recruit.emailservice.services;

import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.EmailAttachment;

/**
* Services for interacting with Email's
* @author K Parkings
*/
public interface EmailService {

	/**
	* Retrieves all email's for a Recipient
	* @return unique id of the recipient
	*/
	public Set<Email> fetchEmailsByRecipientId(String recipientId);

	/**
	* Returns an email attachment
	* @param emailId			- Id of email owning the attachment
	* @param attachmentId 		- Id of the attachment to return
	* @param recipientID 		- currently authenticated user
	* @return Attachment
	*/
	public EmailAttachment fetchAttachment(UUID emailId, UUID attachmentId, String recipientId);

	/**
	* Updates the read status of an Email
	* @param emailId - Unique id of the email
	* @param read	 - read status to update email to
	* @param authorizedUsersId - id of currently authorized user
	*/
	public void setEmailReadStatus(UUID emailId, boolean read, String authorizedUsersId);
	
}
