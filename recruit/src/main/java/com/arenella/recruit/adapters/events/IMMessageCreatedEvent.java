package com.arenella.recruit.adapters.events;

/**
* Event stating that a new IM message was created 
*/
public class IMMessageCreatedEvent {

	private String senderId;
	private String recipientId;
	
	/**
	* Constructor based opon a builder
	* @param builder - Contains initialization values
	*/
	public IMMessageCreatedEvent(IMMessageCreatedEventBuilder builder) {
		this.senderId 		= builder.senderId;
		this.recipientId 	= builder.recipientId;
	}
	
	/**
	* Returns the senders Id
	* @return - Id of the Sender of the IM
	*/
	public String getSenderId() {
		return this.senderId;
	}
	
	/**
	* Returns the Recipients Id
	* @return - Id of the Recipient of the IM
	*/
	public String getRecipientId() {
		return this.recipientId;
	}
	
	/**
	* Returns a builder for the class
	* @return Builder
	*/
	public static IMMessageCreatedEventBuilder builder() {
		return new IMMessageCreatedEventBuilder();
	}
	
	/**
	* Builder for the class
	*/
	public static class IMMessageCreatedEventBuilder{
	
		private String senderId;
		private String recipientId;
		
		/**
		* Sets the sender in the IM chat
		* @param senderId - Id of the Sender
		* @return Builder
		*/
		public IMMessageCreatedEventBuilder senderId(String senderId) {
			this.senderId = senderId;
			return this;
		}
		
		/**
		* Sets the Recipient in the IM chat
		* @param recipientId - Id of the Recipient
		* @return Builder
		*/
		public IMMessageCreatedEventBuilder recipientId(String recipientId) {
			this.recipientId = recipientId;
			return this;
		}
	
		/**
		* Returns an initialized instance
		* @return Initialized instance
		*/
		public IMMessageCreatedEvent build() {
			return new IMMessageCreatedEvent(this);
		}
		
	}
	
}
