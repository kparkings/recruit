package com.arenella.recruit.messaging.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.arenella.recruit.messaging.beans.PublicChatNotification;
import com.arenella.recruit.messaging.beans.PublicChatNotification.NotificationType;
import com.arenella.recruit.messaging.dao.PublicChatNotificationDao;

/**
* Services for interacting with PublicChatNotification's 
*/
@Service
public class PublicChatNotificationServiceImpl implements PublicChatNotificationService{

	private PublicChatNotificationDao dao;
	
	/**
	* Constructor
	* @param dao - Repository for PublicChatNotification's
	*/
	public PublicChatNotificationServiceImpl(PublicChatNotificationDao dao) {
		this.dao = dao;
	}
	
	/**
	* Refer to the PublicChatNotificationService interface for details 
	*/
	@Override
	public void persistNotification(PublicChatNotification notification) {
		this.dao.saveNotification(notification);
	}

	/**
	* Refer to the PublicChatNotificationService interface for details 
	*/
	@Override
	public void deleteNotification(UUID notificationId,  String userId) {
		this.dao.fetchNotificationsById(notificationId).ifPresent(notification -> {
			
			/**
			* User that added the like can remove it
			*/
			if (notification.getType() == NotificationType.LIKE && notification.getInitiatingUserId().equals(userId)) {
				this.dao.deleteById(notificationId);
				return;
			}
			
			if (!notification.getDestinationUserId().equals(userId)) {
				throw new RuntimeException("Cannot delete another user's notificaton");
			}
		
			this.dao.deleteById(notificationId);
		});
	}

	/**
	* Refer to the PublicChatNotificationService interface for details 
	*/
	@Override
	public void deleteNotificationsForUser(String userId) {
		this.dao.fetchNotificationsForUser(userId).stream().forEach(notification -> {
			this.dao.deleteById(notification.getNotificationId());
		});
	}

	/**
	* Refer to the PublicChatNotificationService interface for details 
	*/
	@Override
	public void deleteNotificationsForChat(UUID chatId) {
		this.dao.fetchNotificationsForChat(chatId).stream().forEach(notification -> {
			this.dao.deleteById(notification.getNotificationId());
		});
	}

	/**
	* Refer to the PublicChatNotificationService interface for details 
	*/
	@Override
	public Set<PublicChatNotification> fetchNotificationsForUser(String userId) {
		return this.dao.fetchNotificationsForUser(userId);
	}

	/**
	* Refer to the PublicChatNotificationService interface for details 
	*/
	@Override
	public Optional<PublicChatNotification> fetchNotificationById(UUID notificationId, String userId) {
		
		Optional<PublicChatNotification> notificationOpt = this.dao.fetchNotificationsById(notificationId);
		
		notificationOpt.ifPresent(notification -> {
			
			if (!notification.getInitiatingUserId().equals(userId)) {
				throw new RuntimeException("Cannot delete another user's notificaton");
			}
		
		});
		
		return notificationOpt;
		
	}
 
	/**
	* Refer to the PublicChatNotificationService interface for details 
	*/
	@Override
	public void mailNotificationReminders(LocalDateTime cuttoff) {
		this.dao.fetchUnviewedNotificationsBefore(cuttoff).stream().filter(n -> n.getType() == NotificationType.REPLY).forEach(notification -> {
			//TODO: Call email service
		});
		
	}

	/**
	* Refer to the PublicChatNotificationService interface for details 
	*/
	@Override
	public Set<PublicChatNotification> fetchNotificationsForChat(UUID chatId) {
		return this.dao.fetchNotificationsForChat(chatId);
	}
	
	/**
	* Refer to the PublicChatNotificationService interface for details 
	*/
	@Override
	public void setNotificationViewedStatus(UUID notificationId, boolean viewedStatus, String authenticatedUser) {
		
		PublicChatNotification notification = this.dao.fetchNotificationsById(notificationId).orElseThrow(() -> new RuntimeException("Cannot toggle value for unknown Notification"));
		
		if (!notification.getDestinationUserId().equals(authenticatedUser)) {
			throw new RuntimeException("Cannot update another User's notification.");
		}
		
		this.dao.saveNotification(PublicChatNotification.builder().publicChatNotification(notification).viewed(viewedStatus).build());
		
	}

}