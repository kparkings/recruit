package com.arenella.recruit.emailservice.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;

/**
* Primary Key definition for RecipentEntity
* @author K Parkings
*/
@Embeddable
public class ContactEntityPK implements Serializable{

	private static final long serialVersionUID = -4177140334471310817L;

	@Enumerated(EnumType.STRING)
	private ContactType 	contactType;
	private String			contactId;
	
	/**
	* Default Constructor 
	*/
	public ContactEntityPK() {
		//Hibernate
	}
	
	/**
	* Constructor
	* @param contactType - Type of Contacct
	* @param contactId   - Unique ID of contact in scope of ContactType
	*/
	public ContactEntityPK(ContactType contactType, String contactId) {
		this.contactType 	= contactType;
		this.contactId 		= contactId;
	}
	
	/**
	* Returns Unique ID of recipient in scope of ContactType
	* @return Unique ID of recipient in scope of ContactType
	*/
	public ContactType getContactType() {
		return this.contactType;
	}
	
	/**
	* Returns  Type of Contact
	* @return Type of Contact
	*/
	public String getContactId() {
		return this.contactId;
	}
	
}