package com.arenella.recruit.adapters.events;

/**
* Event requesting contact from one recruiter to another
* @author K Parkings
*/
public class ContactRequestEvent {

	final String title;
	final String message;
	final String senderRecruiterId;
	final String recipientId;
	
	/**
	* 
	* @param senderRecruiterId
	* @param recipientRecruiterId
	* @param message
	*/
	public ContactRequestEvent(String senderRecruiterId, String recipientId, String title, String message) {
		this.recipientId 			= recipientId;
		this.senderRecruiterId 		= senderRecruiterId;
		this.title					= title;
		this.message 				= message;
	}
	
	/**
	* Returns the unique id of the receiving User
	* @return
	*/
	public String getRecipientId() {
		return this.recipientId;
	}

	/**
	* Returns the unique id of the sending recruiter
	* @return id
	*/
	public String getSenderRecruiterId() {
		return this.senderRecruiterId;
	}
	
	/**
	* Returns the message title
	* @return title
	*/
	public String getTitle() {
		return this.title;
	}
	
	/**
	* Returns the email message
	* @return
	*/
	public String getMessage() {
		return this.message;
	}
}