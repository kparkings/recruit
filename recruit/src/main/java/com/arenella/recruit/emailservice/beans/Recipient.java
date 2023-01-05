package com.arenella.recruit.emailservice.beans;

import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.RecipientType;

/**
* Class represnets an Email recipient
* @author K Parkings
*/
public class Recipient {

	final String 			id;
	final RecipientType 	recipientType;
	final String 			firstName;
	final String 			email;
	
	/**
	* Constructor
	* @param id				- Unique id of the Recipient
	* @param recipientType	- Type of the Recipient
	* @param firstName		- firstName of the Recipient
	* @param email			- Email address of the Recipient
	*/
	public Recipient(String id, RecipientType recipientType, String firstName, String email) {
		this.id 			= id;
		this.recipientType 	= recipientType;
		this.firstName 		= firstName;
		this.email 			= email;
	}
	
	/**
	* Returns the Id of the Recipient. Unique in combination
	* with recipientType value
	* @return id of the recipient
	*/
	public String getId() {
		return this.id;
	}
	
	/**
	* Returns the recipentType of the Recipient. Unique in combination
	* with id value
	* @return id of the recipient
	*/
	public RecipientType getRecipientType() {
		return this.recipientType;
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