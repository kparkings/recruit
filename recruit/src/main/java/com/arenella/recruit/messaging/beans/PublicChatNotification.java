package com.arenella.recruit.messaging.beans;

import java.time.LocalDateTime;
import java.util.UUID;

/**
* Class represents a notification that an action has happened 
* relating to the User's public Chat and they need to 
* view this action.
* @param who
*/
public class PublicChatNotification {

	public enum NotificationType {REPLY, LIKE}
	
	private UUID 				notificationId;
	private NotificationType 	type;
	private LocalDateTime 		created;
	private UUID 				chatId;
	private String 				destinationUserId;	//This so we can get users notifications
	private String 				initiatingUserId;	//This so we can show who replied
	private boolean 			viewed;
	private boolean 			notificationEmailSent;

	/**
	* Constructor based on a builder
	* @param builder - Contains initialization values
	*/
	public PublicChatNotification(PublicChatNotificationBuilder builder) {
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
	* Returns the type of the Notification
	* @return - type of action being notified
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
	* Returns the unique Id of the User who the Notification 
	* is intended for
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
	public static PublicChatNotificationBuilder builder() {
		return new PublicChatNotificationBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class PublicChatNotificationBuilder{
		
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
		public PublicChatNotificationBuilder publicChatNotification(PublicChatNotification publicChatNotification){
			this.notificationId 		= publicChatNotification.notificationId;
			this.type					= publicChatNotification.type;
			this.created 				= publicChatNotification.created;
			this.chatId 				= publicChatNotification.chatId;
			this.destinationUserId		= publicChatNotification.destinationUserId;
			this.initiatingUserId 		= publicChatNotification.initiatingUserId;
			this.viewed 				= publicChatNotification.viewed;
			this.notificationEmailSent 	= publicChatNotification.notificationEmailSent;

			return this;
		}
		
		/**
		* Sets the unique identifier of the Notification
		* @param notificationId - id
		* @return Builder
		*/
		public PublicChatNotificationBuilder notificationId(UUID notificationId) {
			this.notificationId = notificationId;
			return this;
			
		}
		
		/**
		* Sets the type of action the notification is related to
		* @param type - type of Notification
		* @return Builder
		*/
		public PublicChatNotificationBuilder type(NotificationType type) {
			this.type = type;
			return this;
			
		}
		
		/**
		* Sets when the Notification was created
		* @param created - Creation date/time
		* @return Builder
		*/
		public PublicChatNotificationBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the Id of the Chat the notification relates to 
		* @param chatId - UniqueId of the Chat
		* @return Builder
		*/
		public PublicChatNotificationBuilder chatId(UUID chatId) {
			this.chatId = chatId;
			return this;
		}
		
		/**
		* Sets the Id of the User that the notification is intended for
		* @param destinationUserId - Unique id of User whose the notification is intended for
		* @return Builder
		*/
		public PublicChatNotificationBuilder destinationUserId(String destinationUserId) {
			this.destinationUserId = destinationUserId;
			return this;
		}
		
		/**
		* Sets the Id of the User that performed the action that generated
		* the notification
		* @param initiatingUserId - Unique id of User whose action resulted in the notification
		* @return Builder
		*/
		public PublicChatNotificationBuilder initiatingUserId(String initiatingUserId) {
			this.initiatingUserId = initiatingUserId;
			return this;
		}
		
		/**
		* Sets whether the notification has been viewed by the Chat owner
		* @param viewed - Whether the notification has been viewed
		* @return Builder 
		*/
		public PublicChatNotificationBuilder viewed(boolean viewed) {
			this.viewed = viewed;
			return this;
		}
		
		/**
		* Sets whether an email has been sent to inform the user that they 
		* have an outstanding notification
		* @param notificationEmailSent - whether notification email sent
		* @return Builder
		*/
		public PublicChatNotificationBuilder notificationEmailSent(boolean notificationEmailSent) {
			this.notificationEmailSent = notificationEmailSent;
			return this;
		}

		/**
		* Returns an initialized instance of the Class
		* @return initialized instance
		*/
		public PublicChatNotification build() {
			return new PublicChatNotification(this);
		}
		
	}
	
}