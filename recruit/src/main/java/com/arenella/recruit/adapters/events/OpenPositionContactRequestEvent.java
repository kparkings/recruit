package com.arenella.recruit.adapters.events;

import java.util.UUID;

/**
* Event representing a contact request being sent
* to a Recruiter relating to an OpenPosition
* @author K Parkings
*/
public class OpenPositionContactRequestEvent {

	private String 	senderId;
	private String 	senderName;
	private String 	recipientId;
	private UUID	openPositionId;
	private String 	openPositionTitle;
	private String 	message;
	
	/**
	* Constructor
	* @param builder - Contains the initialization values
	*/
	public OpenPositionContactRequestEvent(OpenPositionContactRequestEventBuilder builder) {
		this.senderId			= builder.senderId;
		this.senderName			= builder.senderName;
		this.recipientId		= builder.recipientId;
		this.openPositionId 	= builder.openPositionId;
		this.openPositionTitle 	= builder.openPositionTitle;
		this.message			= builder.message;
	}
	
	/**
	* Returns the unique id of the Sender
	* @return id of the Sender
	*/
	public String getSenderId() {
		return this.senderId;
	}
	
	/**
	* Returns the name of the Sender
	* @return name of the Sender
	*/
	public String getSenderName() {
		return this.senderName;
	}
	
	/**
	* Returns the unique id of the recipient of the 
	* message
	* @return recipient id
	*/
	public String getRecipientId() {
		return this.recipientId;
	}
	
	/**
	* Returns the unique id of the Open Position
	* @return openPositon unique id
	*/
	public UUID	getOpenPositionId() {
		return this.openPositionId;
	}
	
	/**
	* Returns the title of the Open Position
	* @return title
	*/
	public String getOpenPositionTitle() {
		return this.openPositionTitle;
	}
	
	/**
	* Returns the message being sent to the owner of the
	* Open Position
	* @return message
	*/
	public String getMessage() {
		return this.message;
	}
	
	/**
	* Builder for the OpenPositionContactRequestEvent class
	* @return Builder for the class
	*/
	public static OpenPositionContactRequestEventBuilder builder(){
		return new OpenPositionContactRequestEventBuilder();
	}
	/**
	* Builder for the OpenPositionContactRequestEvent class
	* @author K Parkings
	*/
	public static class OpenPositionContactRequestEventBuilder {
	
		private String 	senderId;
		private String 	senderName;
		private String 	recipientId;
		private UUID	openPositionId;
		private String 	openPositionTitle;
		private String 	message;
		
		/**
		* Sets the unique id of the Sender the message
		* @param senderId - id of Sender
		* @return Builder
		*/
		public OpenPositionContactRequestEventBuilder senderId(String senderId) {
			this.senderId = senderId;
			return this;
		}
		
		/**
		* Sets the name of the Sender
		* @param senderName - Senders name
		* @return Builder
		*/
		public OpenPositionContactRequestEventBuilder senderName(String senderName) {
			this.senderName = senderName;
			return this;
		}
		
		/**
		* Sets the unique id of the Recipient
		* @param recipientId - Id of the Recipient
		* @return Builder
		*/
		public OpenPositionContactRequestEventBuilder recipientId(String recipientId) {
			this.recipientId = recipientId;
			return this;
		}
		
		/**
		* Sets the unique id of the Open Position
		* @param openPositionId - id
		* @return Builder
		*/
		public OpenPositionContactRequestEventBuilder openPositionId(UUID openPositionId) {
			this.openPositionId = openPositionId;
			return this;
		}
		
		/**
		* Sets the title of the Open Position
		* @param openPositionTitle - title
		* @return Builder
		*/
		public OpenPositionContactRequestEventBuilder openPositionTitle(String openPositionTitle) {
			this.openPositionTitle = openPositionTitle;
			return this;
		}
		
		/**
		* Sets the message being sent
		* @param message - message text
		* @return Builder
		*/
		public OpenPositionContactRequestEventBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Returns an instantiated instance of the class
		* @return instance of class
		*/
		public OpenPositionContactRequestEvent build() {
			return new OpenPositionContactRequestEvent(this);
		}
		
	}
	
}