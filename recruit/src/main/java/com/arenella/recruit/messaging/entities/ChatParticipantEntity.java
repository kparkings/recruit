package com.arenella.recruit.messaging.entities;

import java.time.LocalDateTime;
import java.util.Optional;

import com.arenella.recruit.messaging.beans.Photo;
import com.arenella.recruit.messaging.beans.Photo.PHOTO_FORMAT;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.ChatParticipant.CHAT_PARTICIPANT_TYPE;
import com.arenella.recruit.messaging.beans.ChatParticipant.ChatParticipantBuilder;

import jakarta.persistence.Table;

/**
* Entity representation of a Chat Participant 
*/
@Entity
@Table(schema="chats", name="chat_participants")
public class ChatParticipantEntity {

	@Id
	@Column(name="id")
	private String 					participantId;
	
	@Column(name="type")
	@Enumerated(EnumType.STRING)
	private CHAT_PARTICIPANT_TYPE 	type;
	
	@Column(name="first_name")
	private String					firstName;
	@Column(name="surname")
	private String					surname;
	
	@Column(name="photo_bytes")
	private byte[] 					photoBytes;
	
	@Column(name="photo_format")
	@Enumerated(EnumType.STRING)
	private PHOTO_FORMAT 			photoFormat;
	
	@Column(name="disable_noficication_emails")
	private boolean					disableNotificationEmails;
	
	@Column(name="last_nofication_email_sent")
	private LocalDateTime			lastNotificationEmailSent;
	
	@Column(name="last_nofication_newsfeed_view")
	private LocalDateTime			lastTimeNewsFeedViewed;
	
	/**
	* Default constructor 
	*/
	public ChatParticipantEntity() {
		//Hibernate
	}
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization details
	*/
	public ChatParticipantEntity(ChatParticipantEntityBuilder builder) {
		
		this.participantId 				= builder.participantId;
		this.type 						= builder.type;
		this.firstName 					= builder.firstName;
		this.surname					= builder.surname;
		this.disableNotificationEmails 	= builder.disableNotificationEmails;
		this.lastNotificationEmailSent 	= builder.lastNotificationEmailSent;
		this.lastTimeNewsFeedViewed		= builder.lastTimeNewsFeedViewed;
		
		Optional.ofNullable(builder.photo).ifPresent(photo -> {
			this.photoBytes 	= photo.imageBytes();
			this.photoFormat	= photo.format();
		});
		
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
		
		if (Optional.ofNullable(this.photoBytes).isPresent() && Optional.ofNullable(this.photoFormat).isPresent()) {
			return Optional.of(new Photo(this.photoBytes, this.photoFormat));
		}
		
		return Optional.empty();
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
	* Returns the last time the ChatParticipant has viewed the newsfeed 
	* @return last time viewed
	*/
	public Optional<LocalDateTime> getLastTimeNewsFeedViewed() {
		return Optional.ofNullable(this.lastTimeNewsFeedViewed);
	}
	
	/**
	* Returns a builder for the class
	* @return Builder
	*/
	public static ChatParticipantEntityBuilder builder() {
		return new ChatParticipantEntityBuilder();
	}
	
	/**
	* Builder for the class
	*/
	public static class ChatParticipantEntityBuilder {
		
		private String 					participantId;
		private CHAT_PARTICIPANT_TYPE 	type;
		private String					firstName;
		private String					surname;
		private Photo 					photo;
		private boolean					disableNotificationEmails;
		private LocalDateTime			lastNotificationEmailSent;
		private LocalDateTime			lastTimeNewsFeedViewed;
		
		/**
		* 
		* @param participantId
		* @return Builder
		*/
		public ChatParticipantEntityBuilder participantId(String participantId) {
			this.participantId = participantId;
			return this;
		}
		
		/**
		* Sets the type of the Participant
		* @param type - Role participant performs
		* @return Builder
		*/
		public ChatParticipantEntityBuilder type(CHAT_PARTICIPANT_TYPE type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the first name of the Participant
		* @param firstName - Users name
		* @return Builder
		*/
		public ChatParticipantEntityBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		* Sets the surname of the Participant
		* @param surname - Users name
		* @return Builder
		*/
		public ChatParticipantEntityBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Participants Photo
		* @param photo		- Photo of Participant
		* @return Builder
		*/
		public ChatParticipantEntityBuilder photo(Photo photo) {
			this.photo = photo;
			return this;
		}
		
		/**
		* Sets whether to disable sending of Notification email's to the user 
		* @param disableNotificationEmails - Whether to disable email notifications
		* @return Builder
		*/
		public ChatParticipantEntityBuilder disableNotificationEmails(boolean disableNotificationEmails) {
			this.disableNotificationEmails = disableNotificationEmails;
			return this;
		}
		
		/**
		* Sets the last time a notification email was sent to the user
		* @param lastNotificationEmailSent - last time email was sent
		* @return Builder
		*/
		public ChatParticipantEntityBuilder lastNotificationEmailSent(LocalDateTime lastNotificationEmailSent) {
			this.lastNotificationEmailSent = lastNotificationEmailSent;
			return this;
		}
		
		/**
		* Sets the last time the ChatParticipant viewed the Newsfeed
		* @param lastTimeNewsFeedViewed - Last time viewed
		* @return Builder
		*/
		public ChatParticipantEntityBuilder lastTimeNewsFeedViewed(LocalDateTime lastTimeNewsFeedViewed) {
			this.lastTimeNewsFeedViewed = lastTimeNewsFeedViewed;
			return this;
		}
		
		/**
		* Returns initialized instance 
		* @return Initialized instance
		*/
		public ChatParticipantEntity build() {
			return new ChatParticipantEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity to Domain representation
	* @param entity - To convert
	* @return converted
	*/
	public static ChatParticipant fromEntity(ChatParticipantEntity entity) {
		
		ChatParticipantBuilder builder = ChatParticipant.builder();
		
		builder
			.participantId(entity.getParticipantId())
			.firstName(entity.getFirstName())
			.surname(entity.getSurame())
			.type(entity.getType())
			.disableNotificationEmails(entity.isDisableNotificationEmails())
			.lastTimeNewsFeedViewed(entity.getLastTimeNewsFeedViewed().orElse(null))
			.lastNotificationEmailSent(entity.getLastNotificationEmailSent().orElse(null));
		
		
		entity.getPhoto().ifPresent(p -> {
			builder.photo(new Photo(p.imageBytes(), p.format()));
		});
			
		return builder.build();
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param entity - To convert
	* @return converted
	*/
	public static ChatParticipantEntity toEntity(ChatParticipant participant) {
		
		ChatParticipantEntityBuilder builder = ChatParticipantEntity.builder();
		
		builder
			.participantId(participant.getParticipantId())
			.firstName(participant.getFirstName())
			.surname(participant.getSurame())
			.type(participant.getType())
			.disableNotificationEmails(participant.isDisableNotificationEmails())
			.lastTimeNewsFeedViewed(participant.getLastTimeNewsFeedViewed().orElse(null))
			.lastNotificationEmailSent(participant.getLastNotificationEmailSent().orElse(null));
		
		participant.getPhoto().ifPresent(p -> {
			builder.photo(new Photo(p.imageBytes(), p.format()));
		});
			
		return builder.build();
	}
	
}