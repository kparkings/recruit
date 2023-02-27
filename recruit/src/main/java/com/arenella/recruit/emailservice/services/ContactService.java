package com.arenella.recruit.emailservice.services;

import com.arenella.recruit.emailservice.beans.Contact;

/**
* Defines Services for Email Contact
* @author K Parkings
*/
public interface ContactService {

	/**
	* Adds a new Contact
	*/
	public void addContact(Contact contact);

	/**
	* refer to the ContactService for details
	*/
	void updateContact(Contact contact);
	
}
