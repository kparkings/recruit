package com.arenella.recruit.messaging.beans;

import java.time.LocalDateTime;
import java.util.Optional;

/**
* Class represents a Participant in a Chat.  
*/
public class ChatParticipant {

	public enum CHAT_PARTICIPANT_TYPE {RECRUITER, CANDIDATE, SYSTEM}
	
	private String 					participantId;
	private CHAT_PARTICIPANT_TYPE 	type;
	private String					firstName;
	private String					surname;
	private Photo 					photo;
	private boolean					disableNotificationEmails;
	private LocalDateTime			lastNotificationEmailSent;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization details
	*/
	public ChatParticipant(ChatParticipantBuilder builder) {
		this.participantId 	= builder.participantId;
		this.type 			= builder.type;
		this.firstName 		= builder.firstName;
		this.surname		= builder.surname;
		this.photo 			= builder.photo;
		this.disableNotificationEmails = builder.disableNotificationEmails;
		this.lastNotificationEmailSent = builder.lastNotificationEmailSent;
	}
	
	/**
	* Returns the Id of the Participant
	* @return unique Id
	*/
	public String getParticipantId() {
		return this.participantId;
	}
	
	/**
	* Returns the Type/Role the participant has in 
	* the System
	* @return
	*/
	public CHAT_PARTICIPANT_TYPE getType() {
		return this.type;
	}
	
	/**
	* Returns the first name of the Participant
	* @return first name of the Participant
	*/
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	* Returns the surname of the Participant
	* @return first name of the Participant
	*/
	public String getSurame() {
		return this.surname;
	}
	
	/**
	* Returns, where it has been provided a photo 
	* of the Participant
	* @return
	*/
	public Optional<Photo> getPhoto(){
		return Optional.ofNullable(this.photo);
	}
	
	/**
	* Returns whether or not the User has opted out of receiving 
	* email's relating to notifications
	* @return Whether user wants to receive email's or not for notifications
	*/
	public boolean isDisableNotificationEmails() {
		return this.disableNotificationEmails;
	}
	
	/**
	* If a notification email has already been sent, then returns when 
	* the last email was sent
	* @return last time email sent
	*/
	public Optional<LocalDateTime> getLastNotificationEmailSent() {
		return Optional.ofNullable(this.lastNotificationEmailSent);
	}
	
	/**
	* Returns a builder for the class
	* @return Builder
	*/
	public static ChatParticipantBuilder builder() {
		return new ChatParticipantBuilder();
	}
	
	/**
	* Builder for the class
	*/
	public static class ChatParticipantBuilder {
		
		private String 					participantId;
		private CHAT_PARTICIPANT_TYPE 	type;
		private String					firstName;
		private String					surname;
		private Photo 					photo;
		private boolean					disableNotificationEmails;
		private LocalDateTime			lastNotificationEmailSent;
		
		public ChatParticipantBuilder chatParticipant(ChatParticipant participant) {
			this.participantId 				= participant.participantId;
			this.type 						= participant.type;
			this.firstName					= participant.firstName;
			this.surname					= participant.surname;
			this.photo						= participant.photo;
			this.disableNotificationEmails 	= participant.disableNotificationEmails;
			this.lastNotificationEmailSent 	= participant.lastNotificationEmailSent;
			
			return this;
		}
		
		/**
		* 
		* @param participantId
		* @return Builder
		*/
		public ChatParticipantBuilder participantId(String participantId) {
			this.participantId = participantId;
			return this;
		}
		
		/**
		* Sets the type of the Participant
		* @param type - Role participant performs
		* @return Builder
		*/
		public ChatParticipantBuilder type(CHAT_PARTICIPANT_TYPE type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the first name of the Participant
		* @param firstName - Users name
		* @return Builder
		*/
		public ChatParticipantBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		* Sets the surname of the Participant
		* @param surname - Users name
		* @return Builder
		*/
		public ChatParticipantBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Participants Photo
		* @param photo		- Photo of Participant
		* @return Builder
		*/
		public ChatParticipantBuilder photo(Photo photo) {
			this.photo = photo;
			return this;
		}
		
		/**
		* Sets whether to disable sending of Notification email's to the user 
		* @param disableNotificationEmails - Whether to disable email notifications
		* @return Builder
		*/
		public ChatParticipantBuilder disableNotificationEmails(boolean disableNotificationEmails) {
			this.disableNotificationEmails = disableNotificationEmails;
			return this;
		}
		
		/**
		* Sets the last time a notification email was sent to the user
		* @param lastNotificationEmailSent - last time email was sent
		* @return Builder
		*/
		public ChatParticipantBuilder lastNotificationEmailSent(LocalDateTime lastNotificationEmailSent) {
			this.lastNotificationEmailSent = lastNotificationEmailSent;
			return this;
		}
		
		/**
		* Returns initialized instance 
		* @return Initialized instance
		*/
		public ChatParticipant build() {
			return new ChatParticipant(this);
		}
		
	}
	
}