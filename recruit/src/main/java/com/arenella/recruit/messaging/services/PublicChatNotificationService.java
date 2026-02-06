package com.arenella.recruit.messaging.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.messaging.beans.PublicChatNotification;

/**
* Defines services for interacting with PublicChatNotifications 
*/
public interface PublicChatNotificationService {

	/**
	* Saves or Updates a notification
	* @param notification - Notification to persist
	*/
	public void persistNotification(PublicChatNotification notification);
	
	/**
	* Deletes a Notification if it exists
	* @param notificationId - Id of notification to Delete
	* @param userId - Authenticated User
	*/
	public void deleteNotification(UUID notificationId, String userId);
	
	/**
	* Deletes all notification for a given User
	* @param userId - Unique Id of the User
	*/
	public void deleteNotificationsForUser(String userId);
	
	/**
	* Deletes all notification for a given Chat
	* @param chatId - Unique Id of the Chat
	*/
	public void deleteNotificationsForChat(UUID chatId);
	
	/**
	* Retrieves all the Users notifications
	* @param userId - Unique id of User to fetch Notifications for
	* @return Users notifications
	*/
	public Set<PublicChatNotification> fetchNotificationsForUser(String userId);
	
	/**
	* Retrieves a Notification by its Id
	* @param notificationId - Id of Notification
	* @return Notification
	*/
	public Optional<PublicChatNotification> fetchNotificationById(UUID notificationId, String userId);
	
	/**
	* Sends email's to user informing them of unread notifications for notifications 
	* before the cuttoff where no email has been sent previously
	* @param cuttoff - lte date/time
	*/
	public void mailNotificationReminders(LocalDateTime cuttoff);
		
}