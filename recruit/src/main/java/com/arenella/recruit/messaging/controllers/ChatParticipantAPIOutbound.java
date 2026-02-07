package com.arenella.recruit.messaging.controllers;

import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.Photo;

import com.arenella.recruit.messaging.beans.ChatParticipant.CHAT_PARTICIPANT_TYPE;

/**
* Represents a Participant in an instant Chat message 
*/
public class ChatParticipantAPIOutbound {

	private String 					id;
	private CHAT_PARTICIPANT_TYPE 	type;
	private String 					firstName;
	private String 					surname;
	private Photo 					photo;
	
	/**
	* Constructor
	* @param builder - Contains initialization information
	*/
	public ChatParticipantAPIOutbound(ChatParticipantAPIOutboundBuilder builder) {
		this.id 		= builder.id;
		this.type		= builder.type;
		this.firstName 	= builder.firstName;
		this.surname 	= builder.surname;
		this.photo		= builder.photo;
	}
	
	/**
	* Returns Participants unique id
	* @return id
	*/
	public String getId() {
		return this.id;
	}
	
	/**
	* Returns what type of user the Participant is
	* @return type of Participant
	*/
	public CHAT_PARTICIPANT_TYPE getType() {
		return this.type;
	}
	
	/**
	* Returns Participants firsName
	* @return First name of the Participant
	*/
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	* Returns the surname of the Participant
	* @return Surname of the Participant
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* Returns Participants Photo
	* @return photo
	*/
	public Photo getPhoto() {
		return this.photo;
	}
	
	/**
	* Returns a Builder for the class
	* @return BUilder
	*/
	public static ChatParticipantAPIOutboundBuilder builder() {
		return new ChatParticipantAPIOutboundBuilder();
	}
	
	/**
	* Builder for class 
	*/
	public static class ChatParticipantAPIOutboundBuilder{
	
		private String id;
		private CHAT_PARTICIPANT_TYPE type;
		private String firstName;
		private String surname;
		private Photo photo;
		
		/**
		* Populates builder with values from a ChatParticipant
		* @param chatParticipant - contains initialization values 
		* @return BuildeR
		*/
		public ChatParticipantAPIOutboundBuilder chatParticipant(ChatParticipant chatParticipant) {
			this.id 		= chatParticipant.getParticipantId();
			this.type		= chatParticipant.getType();
			this.firstName 	= chatParticipant.getFirstName();
			this.surname 	= chatParticipant.getSurame();
			
			chatParticipant.getPhoto().ifPresent(p -> photo = p);
			
			return this;
		}
		
		/**
		* Returns an initialized ChatParticipantAPIOutbound
		* @return ChatParticipantAPIOutbound instance
		*/
		public ChatParticipantAPIOutbound build() {
			return new ChatParticipantAPIOutbound(this);
		} 
	}
}
