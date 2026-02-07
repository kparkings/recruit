package com.arenella.recruit.messaging.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.arenella.recruit.messaging.beans.PublicChatNotification;
import com.arenella.recruit.messaging.beans.PublicChatNotification.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Entity representation of PublicChatNotification  
*/
@Entity
@Table(schema="chats", name="public_chat_notifications")
public class PublicChatNotificationEntity {

	@Id
	@Column(name="notification_id")
	private UUID notificationId;
	
	@Column(name="type")
	@Enumerated(EnumType.STRING)
	private NotificationType type;
	
	@Column(name="created")
	private LocalDateTime created;
	
	@Column(name="chat_id")
	private UUID  chatId;
	
	@Column(name="destination_user")
	private String destinationUserId;
	
	@Column(name="initiating_user")
	private String  initiatingUserId;
	
	@Column(name="viewed")
	private boolean  viewed;
	
	@Column(name="notification_email_sent")
	private boolean notificationEmailSent;

	/**
	* Default constructor 
	*/
	public PublicChatNotificationEntity() {
		//Hibernate
	}
	/**
	* Constructor based on a builder
	* @param builder - Contains initialization values
	*/
	public PublicChatNotificationEntity(PublicChatNotificationEntityBuilder builder) {
		this.notificationId 			= builder.notificationId;
		this.type						= builder.type;
		this.created 					= builder.created;
		this.chatId 					= builder.chatId;
		this.destinationUserId			= builder.destinationUserId;
		this.initiatingUserId 			= builder.initiatingUserId;
		this.viewed 					= builder.viewed;
		this.notificationEmailSent 		= builder.notificationEmailSent;
	}
	
	/**
	* Returns the unique id of the Notification
	* @return Id
	*/
	public UUID getNotificationId() {
		return this.notificationId;
	}
	
	/**
	* Returns the type of action that resulted 
	* in the Notification
	* @return type of Notification
	*/
	public NotificationType getType() {
		return this.type;
	}
	
	/**
	* Returns the moment the Notification was created
	* @return creation time
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}
	
	/**
	* Returns the unique id of the Chat the 
	* Notification relates to. This is the 
	* lowest level Chat. I.E a reply
	* @return id of Chat
	*/
	public UUID getChatId() {
		return this.chatId;
	}
	
	/**
	* Returns the unique Id of the User who is the 
	* recipient of the Notification
	* @return Id of notification recipient
	*/
	public String getDestinationUserId() {
		return this.destinationUserId;
	}
	
	/**
	* Returns the unique Id of the User whose action
	* resulted in the Notification being created
	* @return Id of sender
	*/
	public String getInitiatingUserId() {
		return this.initiatingUserId;
	}
	
	/**
	* Whether the chat owner has viewed the 
	* Notification
	* @return is the Notification has been viewed
	*/
	public boolean isViewed() {
		return this.viewed;
	}
	
	/**
	* Returns whether an email has been sent to the owner of the 
	* Chat informing them of an open notification
	* @return
	*/
	public boolean isNotificationEmailSent() {
		return this.notificationEmailSent;
	}
	
	/**
	* Returns a builder for the class 
	* @return
	*/
	public static PublicChatNotificationEntityBuilder builder() {
		return new PublicChatNotificationEntityBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class PublicChatNotificationEntityBuilder{
		
		private UUID 				notificationId;
		private NotificationType 	type;
		private LocalDateTime 		created;
		private UUID 				chatId;
		private String 				destinationUserId;
		private String 				initiatingUserId;
		private boolean 			viewed;
		private boolean 			notificationEmailSent;
		
		/**
		* Initializes Builder from existing instance
		* @param publicChatNotification - Contains initialization values
		* @return Builder 
		*/
		public PublicChatNotificationEntityBuilder publicChatNotification(PublicChatNotification publicChatNotification){
			this.notificationId 		= publicChatNotification.getNotificationId();
			this.type					= publicChatNotification.getType();
			this.created 				= publicChatNotification.getCreated();
			this.chatId 				= publicChatNotification.getChatId();
			this.destinationUserId		= publicChatNotification.getDestinationUserId();
			this.initiatingUserId 		= publicChatNotification.getInitiatingUserId();
			this.viewed 				= publicChatNotification.isViewed();
			this.notificationEmailSent 	= publicChatNotification.isNotificationEmailSent();

			return this;
		}

		/**
		* Sets the unique identifier of the Notification
		* @param notificationId - id
		* @return Builder
		*/
		public PublicChatNotificationEntityBuilder notificationId(UUID notificationId) {
			this.notificationId = notificationId;
			return this;
			
		}
		
		/**
		* Sets the type of the Notification. The type of action that caused
		* the Notification to be created
		* @param type - Type of the notification
		* @return Builder
		*/
		public PublicChatNotificationEntityBuilder type(NotificationType type) {
			this.type = type;
			return this;
			
		}
		
		/**
		* Sets when the Notification was created
		* @param created - Creation date/time
		* @return Builder
		*/
		public PublicChatNotificationEntityBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the Id of the Chat the notification relates to 
		* @param chatId - UniqueId of the Chat
		* @return Builder
		*/
		public PublicChatNotificationEntityBuilder chatId(UUID chatId) {
			this.chatId = chatId;
			return this;
		}
		
		/**
		* Sets the Id of the User the Notification is intended for
		* @param destinationUserId - Unique id of User the notification is for
		* @return Builder
		*/
		public PublicChatNotificationEntityBuilder destinationUserId(String destinationUserId) {
			this.destinationUserId = destinationUserId;
			return this;
		}
		
		/**
		* Sets the Id of the User that performed the action that generated
		* the notification
		* @param initiatingUserId - Unique id of User whose action resulted in the notification
		* @return Builder
		*/
		public PublicChatNotificationEntityBuilder initiatingUserId(String initiatingUserId) {
			this.initiatingUserId = initiatingUserId;
			return this;
		}
		
		/**
		* Sets whether the notification has been viewed by the Chat owner
		* @param viewed - Whether the notification has been viewed
		* @return Builder 
		*/
		public PublicChatNotificationEntityBuilder viewed(boolean viewed) {
			this.viewed = viewed;
			return this;
		}
		
		/**
		* Sets whether an email has been sent to inform the user that they 
		* have an outstanding notification
		* @param notificationEmailSent - whether notification email sent
		* @return Builder
		*/
		public PublicChatNotificationEntityBuilder notificationEmailSent(boolean notificationEmailSent) {
			this.notificationEmailSent = notificationEmailSent;
			return this;
		}

		/**
		* Returns an initialized instance of the Class
		* @return initialized instance
		*/
		public PublicChatNotificationEntity build() {
			return new PublicChatNotificationEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity to Domain representation
	* @param entity - To convert
	* @return converted 
	*/
	public static PublicChatNotification fromEntity(PublicChatNotificationEntity entity) {
		return PublicChatNotification
				.builder()
					.notificationId(entity.getNotificationId())
					.type(entity.getType())
					.created(entity.getCreated())
					.chatId(entity.getChatId())
					.destinationUserId(entity.getDestinationUserId())
					.initiatingUserId(entity.getInitiatingUserId())
					.viewed(entity.isViewed())
					.notificationEmailSent(entity.isNotificationEmailSent())
				.build();
	}
	
}