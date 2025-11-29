package com.arenella.recruit.messaging.controllers;

import java.util.UUID;

import com.arenella.recruit.messaging.beans.PrivateChat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Request to create a new PrivateChat
*/
@JsonDeserialize(builder=PrivateChatAPIInbound.PrivateChatAPIInboundBuilder.class)
public class PrivateChatAPIInbound {

	private String					senderId;
	private String					recipientId;

	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public PrivateChatAPIInbound(PrivateChatAPIInboundBuilder builder) {
		this.senderId 				= builder.senderId;
		this.recipientId 			= builder.recipientId;
	}
	
	/**
	* Returns the id of the Sender
	* @return unique id
	*/
	public String getSenderId() {
		return this.senderId;
	}
	
	/**
	* Returns the id of the recipient
	* @return unique id
	*/
	public String getRecipientId() {
		return this.recipientId;
	}
	
	/**
	* Returns a builder for the Class
	* @return Builder
	*/
	public static PrivateChatAPIInboundBuilder builder() {
		return new PrivateChatAPIInboundBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class PrivateChatAPIInboundBuilder {
		
		private String					senderId;
		private String					recipientId;
		
		/**
		* Sets the uniqueId of the sender that initiated the chat
		* @param senderId - Unique id
		* @return Builder
		*/
		public PrivateChatAPIInboundBuilder senderId(String senderId) {
			this.senderId = senderId;
			return this;
		}
		
		/**
		* Sends the Recipient of the Chat
		* @param recipientId - UniqueId
		* @return Builder
		*/
		public PrivateChatAPIInboundBuilder recipientId(String recipientId){
			this.recipientId = recipientId;
			return this;
		}
		
		/**
		* Returns an initialized instance
		* @return instance 
		*/
		public PrivateChatAPIInbound build() {
			return new PrivateChatAPIInbound(this);
		}
		
	}
	
	/**
	* Converts from API Inbound representation to the Domain 
	* representation. 
	* In this case most of the details are empty and this is 
	* a new chat and the Service will be responsible for 
	* validation and initializing the PrivateChat
	* @param chat
	* @return
	*/
	public static PrivateChat toDomain(PrivateChatAPIInbound chat) {
		return PrivateChat
				.builder()
				.id(UUID.randomUUID())
				.senderId(chat.getSenderId())
				.recipientId(chat.getRecipientId())
				.build();
	}
	
}