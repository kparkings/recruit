package com.arenella.recruit.adapters.events;

import java.util.UUID;

public class OfferedCandidateContactRequestEvent {

	private String	senderId;
	private String	senderName;
	private String	recipientId;
	private UUID	offeredCandidateId;
	private String 	offeredCandidateTitle;
	private String 	message;
	
	/**
	* Constructor
	* @param builder - Contains the initialization values
	*/
	public OfferedCandidateContactRequestEvent(OfferedCandidateContactRequestEventBuilder builder) {
		this.senderId				= builder.senderId;
		this.senderName				= builder.senderName;
		this.recipientId			= builder.recipientId;
		this.offeredCandidateId		= builder.offeredCandidateId;
		this.offeredCandidateTitle 	= builder.offeredCandidateTitle;
		this.message				= builder.message;
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
	* Returns the Id of the offered candidate
	* @return id of the offered candidate
	*/
	public UUID	getOfferedCandidateId() {
		return this.offeredCandidateId;
	}
	
	/**
	* Returns the title of the offered candidate
	* @return title of the offered candidates
	*/
	public String getOfferedCandidateTitle() {
		return this.offeredCandidateTitle;
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
	* Builder for the OfferedCandidateContactRequestEvent class
	* @return Builder for the class
	*/
	public static OfferedCandidateContactRequestEventBuilder builder(){
		return new OfferedCandidateContactRequestEventBuilder();
	}
	/**
	* Builder for the OpenPositionContactRequestEvent class
	* @author K Parkings
	*/
	public static class OfferedCandidateContactRequestEventBuilder {
	
		private String 	senderId;
		private String 	senderName;
		private String 	recipientId;
		private UUID	offeredCandidateId;
		private String 	offeredCandidateTitle;
		private String 	message;
		
		/**
		* Sets the unique id of the Sender the message
		* @param senderId - id of Sender
		* @return Builder
		*/
		public OfferedCandidateContactRequestEventBuilder senderId(String senderId) {
			this.senderId = senderId;
			return this;
		}
		
		/**
		* Sets the name of the Sender
		* @param senderName - Senders name
		* @return Builder
		*/
		public OfferedCandidateContactRequestEventBuilder senderName(String senderName) {
			this.senderName = senderName;
			return this;
		}
		
		/**
		* Sets the unique id of the Recipient
		* @param recipientId - Id of the Recipient
		* @return Builder
		*/
		public OfferedCandidateContactRequestEventBuilder recipientId(String recipientId) {
			this.recipientId = recipientId;
			return this;
		}
		
		/**
		* Sets the id of the offered candidate the contact request relates to
		* @param offeredCandidateId - id of offered candidate
		* @return Builder
		*/
		public OfferedCandidateContactRequestEventBuilder  offeredCandidateId(UUID offeredCandidateId) {
			this.offeredCandidateId = offeredCandidateId;
			return this;
		}
		
		/**
		* Sets the title of the Offered candidate the contact request relates to
		* @param offeredCandidateTitle - title of offered candidate
		* @return Builder
		*/
		public OfferedCandidateContactRequestEventBuilder offeredCandidateTitle(String offeredCandidateTitle) {
			this.offeredCandidateTitle = offeredCandidateTitle;
			return this;
		}
		
		/**
		* Sets the message being sent
		* @param message - message text
		* @return Builder
		*/
		public OfferedCandidateContactRequestEventBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Returns an instantiated instance of the class
		* @return instance of class
		*/
		public OfferedCandidateContactRequestEvent build() {
			return new OfferedCandidateContactRequestEvent(this);
		}
		
	}
}