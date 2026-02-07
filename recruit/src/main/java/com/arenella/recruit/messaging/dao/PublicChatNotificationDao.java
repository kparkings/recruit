package com.arenella.recruit.messaging.dao;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.messaging.beans.PublicChatNotification;
import com.arenella.recruit.messaging.entities.PublicChatNotificationEntity;

/**
* Repository for PublicChatNotificationEntity objects 
*/
@Repository
public interface PublicChatNotificationDao extends ListCrudRepository<PublicChatNotificationEntity, UUID>{

	@Query("from PublicChatNotificationEntity where destinationUserId = :userId")
	List<PublicChatNotificationEntity> findByUserId(String userId);
	
	@Query("from PublicChatNotificationEntity where chatId = :chatId")
	List<PublicChatNotificationEntity> findByChatId(UUID chatId);
	
	@Query("from PublicChatNotificationEntity where created <= :cuttOff and viewed = false and notificationEmailSent = false")
	List<PublicChatNotificationEntity> findByRequiresEmail(LocalDateTime cuttoff);
	
	/**
	* Persists a Notification
	* @param notification - Notification to persist
	*/
	default void saveNotification(PublicChatNotification notification) {
		this.save(PublicChatNotificationEntity.builder().publicChatNotification(notification).build());
	}
	
	/**
	* Fetches a Notification based upon its Id
	* @param notificationId - Id of Notification
	* @return
	*/
	default Optional<PublicChatNotification> fetchNotificationsById(UUID notificationId) {
		return this.findById(notificationId).map(PublicChatNotificationEntity::fromEntity);
	}
	
	/**
	* Fetches notifications for a User
	* @param userId - Id of User to fetch Notification for 
	* @return User's Notifications
	*/
	default Set<PublicChatNotification> fetchNotificationsForUser(String userId) {
		return this.findByUserId(userId).stream().map(PublicChatNotificationEntity::fromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Returns all notifications for a specific Chat
	* @param chatId - Unique identifier of the Chat
	* @return Chat's Notifications
	*/
	default Set<PublicChatNotification> fetchNotificationsForChat(UUID chatId) {
		return this.findByChatId(chatId).stream().map(PublicChatNotificationEntity::fromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Returns all notification before a given moment in time that have 
	* not already been viewed by the user and where no email has been sent informing them 
	* of the Notification's existence
	* @param cuttoff - lte date time
	* @return Notifications
	*/
	default Set<PublicChatNotification> fetchUnviewedNotificationsBefore(LocalDateTime cuttoff) {
		return this.findByRequiresEmail(cuttoff).stream().map(PublicChatNotificationEntity::fromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
}