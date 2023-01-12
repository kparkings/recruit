package com.arenella.recruit.emailservice.services;

import com.arenella.recruit.emailservice.beans.Recipient;

/**
* Defines Services for Email Recipients
* @author K Parkings
*/
public interface RecipientService {

	/**
	* Adds a new Recipient
	*/
	public void addRecipient(Recipient recipient);

	/**
	* refer to the RecipientService for details
	*/
	void updateRecipient(Recipient recipient);
	
}
