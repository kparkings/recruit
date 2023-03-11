package com.arenella.recruit.emailservice.services;

import java.util.Set;

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
	* Updates a Contact
	*/
	void updateContact(Contact contact);

	/**
	* Fetches contacts with matching contactIds
	* @param contactIds - Id's to filter on
	* @return Contacts
	*/
	public Set<Contact> fetchContacts(Set<String> contactIds);
	
}
