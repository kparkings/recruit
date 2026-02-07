package com.arenella.recruit.messaging.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.messaging.beans.PublicChatNotification;
import com.arenella.recruit.messaging.beans.PublicChatNotification.NotificationType;

/**
* Unit tests for the PublicChatNotificationEntity class 
*/
class PublicChatNotificationEntityTest {

	private static final UUID 				NOTIFICATION_ID 			= UUID.randomUUID();
	private static final NotificationType	NOTIFICATION_TYPE			= NotificationType.REPLY;
	private static final LocalDateTime 		CREATED 					= LocalDateTime.of(2026, 2, 6, 18, 11, 01);
	private static final UUID 				CHAT_ID 					= UUID.randomUUID();
	private static final String 			DESTINATION_USER_ID 		= "rec2";
	private static final String 			INITIATING_USER_ID 			= "rec1";
	private static final boolean 			VIEWED 						= false;
	private static final boolean 			NOTIFICATION_EMAIL_SENT		= true;

	/**
	* Tests construction based on Builder 
	*/
	@Test
	void testConstruction() {
		
		PublicChatNotificationEntity notification = PublicChatNotificationEntity
				.builder()
					.notificationId(NOTIFICATION_ID)
					.type(NOTIFICATION_TYPE)
					.created(CREATED)
					.chatId(CHAT_ID)
					.destinationUserId(DESTINATION_USER_ID)
					.initiatingUserId(INITIATING_USER_ID)
					.viewed(VIEWED)
					.notificationEmailSent(NOTIFICATION_EMAIL_SENT)
				.build();
		
		assertEquals(NOTIFICATION_ID, 			notification.getNotificationId());
		assertEquals(NOTIFICATION_TYPE, 		notification.getType());
		assertEquals(CREATED, 					notification.getCreated());
		assertEquals(CHAT_ID, 					notification.getChatId());
		assertEquals(DESTINATION_USER_ID, 		notification.getDestinationUserId());
		assertEquals(INITIATING_USER_ID, 		notification.getInitiatingUserId());
		assertEquals(VIEWED, 					notification.isViewed());
		assertEquals(NOTIFICATION_EMAIL_SENT, 	notification.isNotificationEmailSent());
		
	}
	
	/**
	* Tests construction based on an existing Notification 
	*/
	@Test
	void testConstructionFromExistingNotification() {
		
		PublicChatNotification old = PublicChatNotification
				.builder()
					.notificationId(NOTIFICATION_ID)
					.type(NOTIFICATION_TYPE)
					.created(CREATED)
					.chatId(CHAT_ID)
					.destinationUserId(DESTINATION_USER_ID)
					.initiatingUserId(INITIATING_USER_ID)
					.viewed(VIEWED)
					.notificationEmailSent(NOTIFICATION_EMAIL_SENT)
				.build();
		
		assertEquals(NOTIFICATION_ID, 			old.getNotificationId());
		assertEquals(NOTIFICATION_TYPE, 		old.getType());
		assertEquals(CREATED, 					old.getCreated());
		assertEquals(CHAT_ID, 					old.getChatId());
		assertEquals(DESTINATION_USER_ID, 		old.getDestinationUserId());
		assertEquals(INITIATING_USER_ID, 		old.getInitiatingUserId());
		assertEquals(VIEWED, 					old.isViewed());
		assertEquals(NOTIFICATION_EMAIL_SENT, 	old.isNotificationEmailSent());
		
		PublicChatNotificationEntity notification = PublicChatNotificationEntity.builder().publicChatNotification(old).build();
		
		assertEquals(NOTIFICATION_ID, 			notification.getNotificationId());
		assertEquals(NOTIFICATION_TYPE, 		notification.getType());
		assertEquals(CREATED, 					notification.getCreated());
		assertEquals(CHAT_ID, 					notification.getChatId());
		assertEquals(DESTINATION_USER_ID, 		notification.getDestinationUserId());
		assertEquals(INITIATING_USER_ID, 		notification.getInitiatingUserId());
		assertEquals(VIEWED, 					notification.isViewed());
		assertEquals(NOTIFICATION_EMAIL_SENT, 	notification.isNotificationEmailSent());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	*/
	@Test
	void fromEntity() {
		
		PublicChatNotificationEntity entity = PublicChatNotificationEntity
				.builder()
					.notificationId(NOTIFICATION_ID)
					.type(NOTIFICATION_TYPE)
					.created(CREATED)
					.chatId(CHAT_ID)
					.destinationUserId(DESTINATION_USER_ID)
					.initiatingUserId(INITIATING_USER_ID)
					.viewed(VIEWED)
					.notificationEmailSent(NOTIFICATION_EMAIL_SENT)
				.build();
		
		assertEquals(NOTIFICATION_ID, 			entity.getNotificationId());
		assertEquals(NOTIFICATION_TYPE, 		entity.getType());
		assertEquals(CREATED, 					entity.getCreated());
		assertEquals(CHAT_ID, 					entity.getChatId());
		assertEquals(DESTINATION_USER_ID, 		entity.getDestinationUserId());
		assertEquals(INITIATING_USER_ID, 		entity.getInitiatingUserId());
		assertEquals(VIEWED, 					entity.isViewed());
		assertEquals(NOTIFICATION_EMAIL_SENT, 	entity.isNotificationEmailSent());

		PublicChatNotification notification = PublicChatNotificationEntity.fromEntity(entity);

		assertEquals(NOTIFICATION_ID, 			notification.getNotificationId());
		assertEquals(NOTIFICATION_TYPE, 		notification.getType());
		assertEquals(CREATED, 					notification.getCreated());
		assertEquals(CHAT_ID, 					notification.getChatId());
		assertEquals(DESTINATION_USER_ID, 		notification.getDestinationUserId());
		assertEquals(INITIATING_USER_ID, 		notification.getInitiatingUserId());
		assertEquals(VIEWED, 					notification.isViewed());
		assertEquals(NOTIFICATION_EMAIL_SENT, 	notification.isNotificationEmailSent());
		
	}
}