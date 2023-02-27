package com.arenella.recruit.emailservice.services;

import java.util.Set;

import com.arenella.recruit.emailservice.beans.Email;

/**
* Services for interacting with Email's
* @author K Parkings
*/
public interface EmailService {

	/**
	* Retrieves all emails for a Recipient
	* @return unique id of the recipient
	*/
	public Set<Email> fetchEmailsByRecipientId(String recipientId);
	
}
