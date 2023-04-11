package com.arenella.recruit.emailservice.services;

import java.util.Set;

import com.arenella.recruit.emailservice.beans.Contact;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;

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

	/**
	* Deletes a Contact based upon their Id
	* @param contactType - type of contact
	* @param candidateId - id of contact to delete
	*/
	public void deleteContact(ContactType contactType, String contactId);
	
}
