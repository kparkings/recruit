package com.arenella.recruit.messaging.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.messaging.beans.PublicChatNotification;
import com.arenella.recruit.messaging.beans.PublicChatNotification.NotificationType;
import com.arenella.recruit.messaging.dao.PublicChatNotificationDao;

/**
* Unit tests for the PublicChatNotificationImp class 
*/
@ExtendWith(MockitoExtension.class)
class PublicChatNotificationServiceImplTest {

	@Mock
	private PublicChatNotificationDao 			mockDao;
	
	@Mock
	private Principal 							mockPrincipal;
	
	@InjectMocks
	private PublicChatNotificationServiceImpl 	service;
	
	/**
	* Tests persisting a new Notification 
	*/
	@Test
	void testPersistNotification() {
		
		this.service.persistNotification(PublicChatNotification.builder().build());
		
		verify(this.mockDao).saveNotification(any(PublicChatNotification.class));
		
	}
	
	/**
	* Tests happy path for deletion of Notification where 
	* current user is the owner of the notification 
	*/
	@Test
	void testDeleteNotificationIsOwner() {
		
		final UUID 		notificationId 	= UUID.randomUUID();
		final String 	userId 			= "rec001";
	
		when(this.mockDao.fetchNotificationsById(notificationId)).thenReturn(Optional.of(PublicChatNotification.builder().notificationId(notificationId).destinationUserId(userId).build()));
		
		this.service.deleteNotification(notificationId, userId);
		
		verify(this.mockDao).deleteById(notificationId);
	
	
	}
	
	/**
	* Tests exception is thrown if Notification exists but current 
	* user is not the owner of the Notification 
	*/
	@Test
	void testDeleteNotificationIsNOtOwner() {
		
		final UUID 		notificationId 	= UUID.randomUUID();
		final String 	userId 			= "rec001";
	
		when(this.mockDao.fetchNotificationsById(notificationId)).thenReturn(Optional.of(PublicChatNotification.builder().notificationId(notificationId).destinationUserId("anotherUser").build()));
		
		assertThrows(RuntimeException.class, () -> {
			this.service.deleteNotification(notificationId, userId);
		});
			
	}
	
	/**
	* Test a user that added a Like can remove the notification
	*/
	@Test
	void testDeleteNotificationLike() {
		
		final UUID 		notificationId 	= UUID.randomUUID();
		final String 	userId 			= "rec001";
	
		when(this.mockDao.fetchNotificationsById(notificationId)).thenReturn(Optional.of(PublicChatNotification.builder().type(NotificationType.LIKE).notificationId(notificationId).initiatingUserId(userId).build()));
		
		this.service.deleteNotification(notificationId, userId);
		
		verify(this.mockDao).deleteById(notificationId);
		
	}
	
	/**
	* Tests deleting all Notifications for a specific user
	*/
	@Test
	void testDeleteNotificationsForUser() {
		
		final UUID 		notificationId1 	= UUID.randomUUID();
		final UUID 		notificationId2 	= UUID.randomUUID();
		final UUID 		notificationId3 	= UUID.randomUUID();
		final String 	userId 				= "rec001";
		
		when(this.mockDao.fetchNotificationsForUser(userId))
			.thenReturn(Set.of(
					PublicChatNotification.builder().notificationId(notificationId1).destinationUserId(userId).build(),
					PublicChatNotification.builder().notificationId(notificationId2).destinationUserId(userId).build(),
					PublicChatNotification.builder().notificationId(notificationId3).destinationUserId(userId).build()));
		
		this.service.deleteNotificationsForUser(userId);
		
		verify(this.mockDao, times(3)).deleteById(any(UUID.class));
		
	}
	
	/**
	* Tests deleting all Notifications for a specific chat
	*/
	@Test
	void testDeleteNotificationsForChat() {
		
		final UUID 		notificationId1 	= UUID.randomUUID();
		final UUID 		notificationId2 	= UUID.randomUUID();
		final UUID 		notificationId3 	= UUID.randomUUID();
		final UUID 		chatId 				= UUID.randomUUID();
		
		when(this.mockDao.fetchNotificationsForChat(chatId))
			.thenReturn(Set.of(
					PublicChatNotification.builder().notificationId(notificationId1).chatId(chatId).build(),
					PublicChatNotification.builder().notificationId(notificationId2).chatId(chatId).build(),
					PublicChatNotification.builder().notificationId(notificationId3).chatId(chatId).build()));
		
		this.service.deleteNotificationsForChat(chatId);
		
		verify(this.mockDao, times(3)).deleteById(any(UUID.class));
		
	}
	
	/**
	* Tests deleting all Notifications for a specific User
	*/
	@Test
	void testFetchNotificationsForUser() {
		
		final UUID 		notificationId1 	= UUID.randomUUID();
		final UUID 		notificationId2 	= UUID.randomUUID();
		final UUID 		notificationId3 	= UUID.randomUUID();
		final String 	userId 				= "aRec1";
		
		when(this.mockDao.fetchNotificationsForUser(userId))
			.thenReturn(Set.of(
					PublicChatNotification.builder().notificationId(notificationId1).initiatingUserId(userId).build(),
					PublicChatNotification.builder().notificationId(notificationId2).initiatingUserId(userId).build(),
					PublicChatNotification.builder().notificationId(notificationId3).initiatingUserId(userId).build()));
		
		Set<PublicChatNotification> notifications = this.service.fetchNotificationsForUser(userId);
		
		assertEquals(3, notifications.size());
		
	}
	
	/**
	* Test happy path for fetching a notification where the 
	* authenticated user is the owner
	*/
	@Test
	void fetchNotificationByIdExists() {
		
		final UUID 		notificationId 	= UUID.randomUUID();
		final String 	userId 			= "rec1";
		
		when(this.mockDao.fetchNotificationsById(notificationId)).thenReturn(Optional.of(PublicChatNotification.builder().initiatingUserId(userId).build()));
		
		Optional<PublicChatNotification> notification = this.service.fetchNotificationById(notificationId, userId);
		
		assertTrue(notification.isPresent());
		
	}
	
	/**
	* Test fetching a notification where the 
	* notification doesn't exist
	*/
	@Test
	void fetchNotificationByIdDoesNotExists() {
		
		final UUID 		notificationId 	= UUID.randomUUID();
		final String 	userId 			= "rec1";
		
		when(this.mockDao.fetchNotificationsById(notificationId)).thenReturn(Optional.empty());
		
		Optional<PublicChatNotification> notification = this.service.fetchNotificationById(notificationId, userId);
		
		assertTrue(notification.isEmpty());
		
	}

	/**
	* Tests fetching a notification where the notification exists but 
	* the authenticated user is not the owner
	*/
	@Test
	void fetchNotificationByIdExistsForOtherUser() {
		
		final UUID 		notificationId 	= UUID.randomUUID();
		final String 	userId 			= "rec1";
		
		when(this.mockDao.fetchNotificationsById(notificationId)).thenReturn(Optional.of(PublicChatNotification.builder().initiatingUserId("otherUserId").build()));
		
		assertThrows(RuntimeException.class, () -> {
			this.service.fetchNotificationById(notificationId, userId);
		});
		
	}
	
	/**
	* Tests fetching of Notification for a specific Chat
	*/
	@Test
	void testFetchNotificationsForChat() {
		
		final UUID 		chatId 			= UUID.randomUUID();
		
		when(this.mockDao.fetchNotificationsForChat(chatId)).thenReturn(Set.of(PublicChatNotification.builder().initiatingUserId("otherUserId").build()));
		
		Set<PublicChatNotification> results = this.service.fetchNotificationsForChat(chatId);
		
		assertEquals(1, results.size());
		
	}
	
	/**
	* Tests the viewed status is updated for a Notification where the user is 
	* the recipient of the notification
	*/
	@Test
	void testSetNotificationViewedStatusHappyPath() {
		
		final UUID 		notificationId 	= UUID.randomUUID();
		final boolean 	viewed 			= true;
		final String 	username 		= "authenticatedUser1";
		
		//when(this.mockPrincipal.getName()).thenReturn(username);
		when(this.mockDao.fetchNotificationsById(notificationId)).thenReturn(Optional.of(PublicChatNotification.builder().destinationUserId(username).build()));
		
		this.service.setNotificationViewedStatus(notificationId, viewed, username);
		
		verify(this.mockDao).saveNotification(any());
		
	}

	/**
	* Tests an Exception is thrown if the authenticated user attempts to set
	* the viewed status for a Notification that they are not the recipient of
	*/
	@Test
	void testSetNotificationViewedStatusFailurePathUnknownNotification() {
		
		final UUID 		notificationId 	= UUID.randomUUID();
		final boolean 	viewed 			= true;
		final String 	username 		= "authenticatedUser1";
		
		//when(this.mockPrincipal.getName()).thenReturn(username);
		when(this.mockDao.fetchNotificationsById(notificationId)).thenReturn(Optional.empty());
		
		RuntimeException ex = assertThrows(RuntimeException.class, () ->{
			this.service.setNotificationViewedStatus(notificationId, viewed, username);
		});
		 
		assertEquals(PublicChatNotificationServiceImpl.ERR_MSG_CANNOT_DELETE_UNKNOWM_NOTIFICATION, ex.getMessage());
		
		verify(this.mockDao, never()).saveNotification(any());
		
	}
	
	/**
	* Tests an Exception is thrown if the authenticated user attempts to set
	* the viewed status for a Notification that they are not the recipient of
	*/
	@Test
	void testSetNotificationViewedStatusFailurePathNotDestinationUser() {
		
		final UUID 		notificationId 	= UUID.randomUUID();
		final boolean 	viewed 			= true;
		final String 	username 		= "authenticatedUser1";
		
		//when(this.mockPrincipal.getName()).thenReturn(username);
		when(this.mockDao.fetchNotificationsById(notificationId)).thenReturn(Optional.of(PublicChatNotification.builder().destinationUserId("notDestinationUser").build()));
		
		RuntimeException ex = assertThrows(RuntimeException.class, () ->{
			this.service.setNotificationViewedStatus(notificationId, viewed, username);
		});
		 
		assertEquals(PublicChatNotificationServiceImpl.ERR_MSG_CANNOT_DELETE_OTHER_USERS_NOTIFICATION, ex.getMessage());
		
		verify(this.mockDao, never()).saveNotification(any());
	}

	
}