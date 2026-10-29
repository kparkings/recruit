package com.arenella.recruit.adapters.events;

/**
* Event to indicate an Instant message has been read 
*/
public class IMMessageReadEvent {

	private String senderId;
	private String recipientId;
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public IMMessageReadEvent(IMMessageReadEventBuilder builder) {
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
	public static IMMessageReadEventBuilder builder() {
		return new IMMessageReadEventBuilder();
	}
	
	/**
	* Builder for the class
	*/
	public static class IMMessageReadEventBuilder{
	
		private String senderId;
		private String recipientId;
		
		/**
		* Sets the sender in the IM chat
		* @param senderId - Id of the Sender
		* @return Builder
		*/
		public IMMessageReadEventBuilder senderId(String senderId) {
			this.senderId = senderId;
			return this;
		}
		
		/**
		* Sets the Recipient in the IM chat
		* @param recipientId - Id of the Recipient
		* @return Builder
		*/
		public IMMessageReadEventBuilder recipientId(String recipientId) {
			this.recipientId = recipientId;
			return this;
		}
	
		/**
		* Returns an initialized instance
		* @return Initialized instance
		*/
		public IMMessageReadEvent build() {
			return new IMMessageReadEvent(this);
		}
		
	}
	
}
