package com.arenella.recruit.adapters.events;

/**
* Event requesting contact from one recruiter to another
* @author K Parkings
*/
public class RecruiterContactRequestEvent {

	final String title;
	final String message;
	final String senderRecruiterId;
	final String recipientRecruiterId;
	
	/**
	* 
	* @param senderRecruiterId
	* @param recipientRecruiterId
	* @param message
	*/
	public RecruiterContactRequestEvent(String senderRecruiterId, String recipientRecruiterId, String title, String message) {
		this.recipientRecruiterId 	= recipientRecruiterId;
		this.senderRecruiterId 		= senderRecruiterId;
		this.title					= title;
		this.message 				= message;
	}
	
	/**
	* Returns the unique id of the receiving recruiter
	* @return
	*/
	public String getRecipientRecruiterId() {
		return this.recipientRecruiterId;
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