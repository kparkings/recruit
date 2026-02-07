package com.arenella.recruit.messaging.controllers;

import java.time.LocalDateTime;
import java.util.UUID;

import com.arenella.recruit.messaging.beans.PublicChatNotification;
import com.arenella.recruit.messaging.beans.PublicChatNotification.NotificationType;

public class PublicChatNotificationAPIOutbound {

	private UUID 						notificationId;
	private NotificationType 			type;
	private LocalDateTime 				created;
	private UUID 						chatId;
	private String 						destinationUserId;	//This so we can get users notifications
	private ChatParticipantAPIOutbound 	initiatingUser;	//This so we can show who replied
	private boolean 					viewed;

	/**
	* Constructor based on a builder
	* @param builder - Contains initialization values
	*/
	public PublicChatNotificationAPIOutbound(PublicChatNotificationAPIOutboundBuilder builder) {
		this.notificationId 			= builder.notificationId;
		this.type						= builder.type;
		this.created 					= builder.created;
		this.chatId 					= builder.chatId;
		this.destinationUserId			= builder.destinationUserId;
		this.initiatingUser 			= builder.initiatingUser;
		this.viewed 					= builder.viewed;
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
	* Returns the User whose action
	* resulted in the Notification being created
	* @return Id of sender
	*/
	public ChatParticipantAPIOutbound getInitiatingUser() {
		return this.initiatingUser;
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
	* Returns a builder for the class 
	* @return
	*/
	public static PublicChatNotificationAPIOutboundBuilder builder() {
		return new PublicChatNotificationAPIOutboundBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class PublicChatNotificationAPIOutboundBuilder{
		
		private UUID 							notificationId;
		private NotificationType 				type;
		private LocalDateTime 					created;
		private UUID 							chatId;
		private String 							destinationUserId;
		private ChatParticipantAPIOutbound 		initiatingUser;
		private boolean 						viewed;
		
		/**
		* Initializes Builder from existing instance
		* @param publicChatNotification - Contains initialization values
		* @return Builder 
		*/
		public PublicChatNotificationAPIOutboundBuilder publicChatNotification(PublicChatNotification publicChatNotification, ChatParticipantAPIOutbound initiatingUser){
			this.notificationId 		= publicChatNotification.getNotificationId();
			this.type					= publicChatNotification.getType();
			this.created 				= publicChatNotification.getCreated();
			this.chatId 				= publicChatNotification.getChatId();
			this.destinationUserId		= publicChatNotification.getDestinationUserId();
			this.initiatingUser 		= initiatingUser;
			this.viewed 				= publicChatNotification.isViewed();
	
			return this;
		}
		
		/**
		* Sets the unique identifier of the Notification
		* @param notificationId - id
		* @return Builder
		*/
		public PublicChatNotificationAPIOutboundBuilder notificationId(UUID notificationId) {
			this.notificationId = notificationId;
			return this;
			
		}
		
		/**
		* Sets the type of action the notification is related to
		* @param type - type of Notification
		* @return Builder
		*/
		public PublicChatNotificationAPIOutboundBuilder type(NotificationType type) {
			this.type = type;
			return this;
			
		}
		
		/**
		* Sets when the Notification was created
		* @param created - Creation date/time
		* @return Builder
		*/
		public PublicChatNotificationAPIOutboundBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the Id of the Chat the notification relates to 
		* @param chatId - UniqueId of the Chat
		* @return Builder
		*/
		public PublicChatNotificationAPIOutboundBuilder chatId(UUID chatId) {
			this.chatId = chatId;
			return this;
		}
		
		/**
		* Sets the Id of the User that the notification is intended for
		* @param destinationUserId - Unique id of User whose the notification is intended for
		* @return Builder
		*/
		public PublicChatNotificationAPIOutboundBuilder destinationUserId(String destinationUserId) {
			this.destinationUserId = destinationUserId;
			return this;
		}
		
		/**
		* Sets the of the User that performed the action that generated
		* the notification
		* @param initiatingUserId - Unique id of User whose action resulted in the notification
		* @return Builder
		*/
		public PublicChatNotificationAPIOutboundBuilder initiatingUser(ChatParticipantAPIOutbound initiatingUser) {
			this.initiatingUser = initiatingUser;
			return this;
		}
		
		/**
		* Sets whether the notification has been viewed by the Chat owner
		* @param viewed - Whether the notification has been viewed
		* @return Builder 
		*/
		public PublicChatNotificationAPIOutboundBuilder viewed(boolean viewed) {
			this.viewed = viewed;
			return this;
		}
		
		/**
		* Returns an initialized instance of the Class
		* @return initialized instance
		*/
		public PublicChatNotificationAPIOutbound build() {
			return new PublicChatNotificationAPIOutbound(this);
		}
		
	}
	
}