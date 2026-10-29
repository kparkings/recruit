package com.arenella.recruit.messaging.controllers;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Class represents a Message that will be sent to multiple
* selected Users 
*/
@JsonDeserialize(builder=MultiUserChatMessageAPIInbound.MultiUserChatMessageAPIInboundBuilder.class)
public class MultiUserChatMessageAPIInbound {

	private Set<String> recipientIds	= new LinkedHashSet<>();
	private String 		message;
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public MultiUserChatMessageAPIInbound(MultiUserChatMessageAPIInboundBuilder builder) {
		this.recipientIds.clear();
		this.recipientIds.addAll(builder.recipientIds);
		this.message = builder.message;
	}
	
	/**
	* Returns the list of recipients to send message to
	* @return Ids of recipients
	*/
	public Set<String> getRecipientIds(){
		return this.recipientIds;
	}
	
	/**
	* Returns the message to send to the recipients
	* @return
	*/
	public String getMessage() {
		return this.message;
	}
	
	/**
	* Returns a Builder for the class 
	* @return
	*/
	public static MultiUserChatMessageAPIInboundBuilder builder() {
		return new MultiUserChatMessageAPIInboundBuilder();
	}
	
	/**
	* Builder 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class MultiUserChatMessageAPIInboundBuilder{
		
		private Set<String> recipientIds	= new LinkedHashSet<>();
		private String 		message;
		
		/**
		* Sets the IDs of the Recripients that will receive the Message
		* @param recipientIds - Ids of Recipients
		* @return Builder
		*/
		public MultiUserChatMessageAPIInboundBuilder recipientIds(Set<String> recipientIds) {
			this.recipientIds.clear();
			this.recipientIds.addAll(recipientIds);
			return this;
		}
		
		/**
		* Sets the message the Recipient's will be sent
		* @param message - Message to be sent
		* @return Builder
		*/
		public MultiUserChatMessageAPIInboundBuilder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		* Returns initialized instance of the Class
		* @return Initialized instance of the Class
		*/
		public MultiUserChatMessageAPIInbound build() {
			return new MultiUserChatMessageAPIInbound(this);
		}
		
	}
	
}