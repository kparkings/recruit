package com.arenella.recruit.emailservice.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.arenella.recruit.emailservice.beans.Recipient.RECIPIENT_TYPE;

/**
* Primary Key definition for RecipentEntity
* @author K Parkings
*/
@Embeddable
public class RecipientEntityPK implements Serializable{

	private static final long serialVersionUID = -4177140334471310817L;

	@Enumerated(EnumType.STRING)
	private RECIPIENT_TYPE 	recipientType;
	private String			recipientId;
	
	/**
	* Default Constructor 
	*/
	public RecipientEntityPK() {
		//Hibernate
	}
	
	/**
	* Constructor
	* @param recipeintType - Type of Recipient
	* @param recipeintId   - Unique ID of recipient in scope of RecipientType
	*/
	public RecipientEntityPK(RECIPIENT_TYPE recipientType, String recipientId) {
		this.recipientType 	= recipientType;
		this.recipientId 	= recipientId;
	}
	
	/**
	* Returns Unique ID of recipient in scope of RecipientType
	* @return Unique ID of recipient in scope of RecipientType
	*/
	public RECIPIENT_TYPE getRecipientType() {
		return this.recipientType;
	}
	
	/**
	* Returns  Type of Recipient
	* @return Type of Recipient
	*/
	public String getRecipientId() {
		return this.recipientId;
	}
	
}