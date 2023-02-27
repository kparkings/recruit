package com.arenella.recruit.emailservice.beans;

import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;

/**
* Class represnets an Email Contact
* @author K Parkings
*/
public class Contact {

	final String 			id;
	final ContactType 		contactType;
	final String 			firstName;
	final String 			email;
	
	/**
	* Constructor
	* @param id				- Unique id of the Contact
	* @param contactType	- Type of the Contact
	* @param firstName		- firstName of the Contact
	* @param email			- Email address of the Contact
	*/
	public Contact(String id, ContactType contactType, String firstName, String email) {
		this.id 			= id;
		this.contactType 	= contactType;
		this.firstName 		= firstName;
		this.email 			= email;
	}
	
	/**
	* Returns the Id of the Contact. Unique in combination
	* with contactType value
	* @return id of the Contact
	*/
	public String getId() {
		return this.id;
	}
	
	/**
	* Returns the contactType of the Contact. Unique in combination
	* with id value
	* @return id of the Contact
	*/
	public ContactType getContactType() {
		return this.contactType;
	}
	
	/**
	* Returns the firstName of the Recipient
	* @return firstName of the Recipient
	*/
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	* Returns the email Address of the Recipient
	* @return Email address of the Recipient
	*/
	public String getEmail() {
		return this.email;
	}
	
}