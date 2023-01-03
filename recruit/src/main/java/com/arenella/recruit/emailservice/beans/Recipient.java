package com.arenella.recruit.emailservice.beans;

/**
* Class represnets an Email recipient
* @author K Parkings
*/
public class Recipient {

	public static enum RECIPIENT_TYPE {RECRUITER}
	
	final String 			id;
	final RECIPIENT_TYPE 	recipientType;
	final String 			firstName;
	final String 			email;
	
	/**
	* Constructor
	* @param id				- Unique id of the Recipient
	* @param recipientType	- Type of the Recipient
	* @param firstName		- firstName of the Recipient
	* @param email			- Email address of the Recipient
	*/
	public Recipient(String id, RECIPIENT_TYPE recipientType, String firstName, String email) {
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
	public RECIPIENT_TYPE getRecipientType() {
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