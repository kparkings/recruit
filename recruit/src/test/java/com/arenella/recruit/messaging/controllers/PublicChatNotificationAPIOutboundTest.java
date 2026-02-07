package com.arenella.recruit.messaging.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.messaging.beans.PublicChatNotification;
import com.arenella.recruit.messaging.beans.PublicChatNotification.NotificationType;

/**
* Unit tests for the PublicChatNotificationAPIOutbound class 
*/
class PublicChatNotificationAPIOutboundTest {

	private static final UUID 						NOTIFICATION_ID 			= UUID.randomUUID();
	private static final NotificationType			TYPE						= NotificationType.LIKE;
	private static final LocalDateTime 				CREATED 					= LocalDateTime.of(2026, 2, 6, 18, 11, 01);
	private static final UUID 						CHAT_ID 					= UUID.randomUUID();
	private static final String 					DESTINATION_USER_ID 		= "rec2";
	private static final String 					INITIATING_USER_ID 			= "rec1";
	private static final ChatParticipantAPIOutbound INITIATING_USER 			= ChatParticipantAPIOutbound.builder().build();
	private static final boolean 					VIEWED 						= false;
	private static final boolean 					NOTIFICATION_EMAIL_SENT		= true;

	/**
	* Tests construction based on Builder 
	*/
	@Test
	void testConstruction() {
		
		PublicChatNotificationAPIOutbound notification = PublicChatNotificationAPIOutbound
				.builder()
					.notificationId(NOTIFICATION_ID)
					.type(TYPE)
					.created(CREATED)
					.chatId(CHAT_ID)
					.destinationUserId(DESTINATION_USER_ID)
					.initiatingUser(INITIATING_USER)
					.viewed(VIEWED)
				.build();
		
		assertEquals(NOTIFICATION_ID, 			notification.getNotificationId());
		assertEquals(TYPE, 						notification.getType());
		assertEquals(CREATED, 					notification.getCreated());
		assertEquals(CHAT_ID, 					notification.getChatId());
		assertEquals(DESTINATION_USER_ID, 		notification.getDestinationUserId());
		assertEquals(INITIATING_USER, 			notification.getInitiatingUser());
		assertEquals(VIEWED, 					notification.isViewed());
		
	}
	
	/**
	* Tests construction based on an existing Notification 
	*/
	@Test
	void testConstructionFromExistingNotification() {
		
		PublicChatNotification old = PublicChatNotification
				.builder()
					.notificationId(NOTIFICATION_ID)
					.type(TYPE)
					.created(CREATED)
					.chatId(CHAT_ID)
					.destinationUserId(DESTINATION_USER_ID)
					.initiatingUserId(INITIATING_USER_ID)
					.viewed(VIEWED)
					.notificationEmailSent(NOTIFICATION_EMAIL_SENT)
				.build();
		
		assertEquals(NOTIFICATION_ID, 			old.getNotificationId());
		assertEquals(TYPE, 						old.getType());
		assertEquals(CREATED, 					old.getCreated());
		assertEquals(CHAT_ID, 					old.getChatId());
		assertEquals(DESTINATION_USER_ID, 		old.getDestinationUserId());
		assertEquals(INITIATING_USER_ID, 		old.getInitiatingUserId());
		assertEquals(VIEWED, 					old.isViewed());
		assertEquals(NOTIFICATION_EMAIL_SENT, 	old.isNotificationEmailSent());
		
		PublicChatNotificationAPIOutbound notification = PublicChatNotificationAPIOutbound.builder().publicChatNotification(old, INITIATING_USER).build();
		
		assertEquals(NOTIFICATION_ID, 			notification.getNotificationId());
		assertEquals(TYPE, 						notification.getType());
		assertEquals(CREATED, 					notification.getCreated());
		assertEquals(CHAT_ID, 					notification.getChatId());
		assertEquals(DESTINATION_USER_ID, 		notification.getDestinationUserId());
		assertEquals(INITIATING_USER, 			notification.getInitiatingUser());
		assertEquals(VIEWED, 					notification.isViewed());
		
	}
	
}
