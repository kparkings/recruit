package com.arenella.recruit.messaging.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the PublicChatNotification class 
*/
class PublicChatNotificationTest {

	private static final UUID 				NOTIFICATION_ID 			= UUID.randomUUID();
	private static final LocalDateTime 		CREATED 					= LocalDateTime.of(2026, 2, 6, 18, 11, 01);
	private static final UUID 				CHAT_ID 					= UUID.randomUUID();
	private static final String 			INITIATING_USER_ID 			= "rec1";
	private static final boolean 			VIEWED 						= false;
	private static final boolean 			NOTIFICATION_EMAIL_SENT		= true;

	/**
	* Tests construction based on Builder 
	*/
	@Test
	void testConstruction() {
		
		PublicChatNotification notification = PublicChatNotification
				.builder()
					.notificationId(NOTIFICATION_ID)
					.created(CREATED)
					.chatId(CHAT_ID)
					.initiatingUserId(INITIATING_USER_ID)
					.viewed(VIEWED)
					.notificationEmailSent(NOTIFICATION_EMAIL_SENT)
				.build();
		
		assertEquals(NOTIFICATION_ID, 			notification.getNotificationId());
		assertEquals(CREATED, 					notification.getCreated());
		assertEquals(CHAT_ID, 					notification.getChatId());
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
					.created(CREATED)
					.chatId(CHAT_ID)
					.initiatingUserId(INITIATING_USER_ID)
					.viewed(VIEWED)
					.notificationEmailSent(NOTIFICATION_EMAIL_SENT)
				.build();
		
		assertEquals(NOTIFICATION_ID, 			old.getNotificationId());
		assertEquals(CREATED, 					old.getCreated());
		assertEquals(CHAT_ID, 					old.getChatId());
		assertEquals(INITIATING_USER_ID, 		old.getInitiatingUserId());
		assertEquals(VIEWED, 					old.isViewed());
		assertEquals(NOTIFICATION_EMAIL_SENT, 	old.isNotificationEmailSent());
		
		PublicChatNotification notification = PublicChatNotification.builder().publicChatNotification(old).build();
		
		assertEquals(NOTIFICATION_ID, 			notification.getNotificationId());
		assertEquals(CREATED, 					notification.getCreated());
		assertEquals(CHAT_ID, 					notification.getChatId());
		assertEquals(INITIATING_USER_ID, 		notification.getInitiatingUserId());
		assertEquals(VIEWED, 					notification.isViewed());
		assertEquals(NOTIFICATION_EMAIL_SENT, 	notification.isNotificationEmailSent());
		
	}
	
}