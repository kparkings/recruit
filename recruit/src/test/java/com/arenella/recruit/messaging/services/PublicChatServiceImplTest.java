package com.arenella.recruit.messaging.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.PublicChat;
import com.arenella.recruit.messaging.beans.PublicChat.AUDIENCE_TYPE;
import com.arenella.recruit.messaging.beans.PublicChatNotification;
import com.arenella.recruit.messaging.beans.PublicChatNotification.NotificationType;
import com.arenella.recruit.messaging.dao.PublicChatDao;

/**
* Unit tests for the PublicChatServiceImpl class 
*/
@ExtendWith(MockitoExtension.class)
class PublicChatServiceImplTest {

	private static final UUID 				ID 						= UUID.randomUUID();
	private static final AUDIENCE_TYPE		AUDIENCE_TYPE_VAL 		= AUDIENCE_TYPE.RECRUITER;
	private static final UUID				PARENT_ID 				= UUID.randomUUID();
	private static final String				OWNER_ID 				= "aRec1";
	private static final LocalDateTime 		CREATED 				= LocalDateTime.of(2026, 1, 15, 11, 02);
	private static final String 			MESSAGE 				= "A message";
	private static final Set<String>		likes					= Set.of("rec2","can33");
	
	@Mock
	private Principal 						mockPrincipal;
	
	@Mock
	private PublicChatDao 					mockChatDao; 
	
	@Mock
	private PublicChatNotificationService 	mockNotificationService;
	
	@Mock
	private ParticipantService 				mockParticipantService;
	
	@InjectMocks
	private PublicChatServiceImpl service;
	
	/**
	* Tests successful creating on a new Chat 
	*/
	@Test
	void testCreateChatHappyPathNewChat() {
		
		ArgumentCaptor<PublicChat> argCapt = ArgumentCaptor.forClass(PublicChat.class);
		
		when(this.mockPrincipal.getName()).thenReturn(OWNER_ID);
		when(this.mockChatDao.existsById(PARENT_ID)).thenReturn(true);
		when(this.mockChatDao.fetchPublicChat(any())).thenReturn(Optional.of(PublicChat.builder().build()));
		
		this.service.createChat(PARENT_ID, MESSAGE, this.mockPrincipal);
		
		verify(this.mockChatDao).saveChat(argCapt.capture());
		
		PublicChat chat = argCapt.getValue();
		
		assertEquals(AUDIENCE_TYPE.ALL, 	chat.getAudienceType());
		assertEquals(PARENT_ID, 			chat.getParentChat().orElse(null));
		assertEquals(OWNER_ID, 				chat.getOwnerId());
		assertEquals(MESSAGE, 				chat.getMessage());
		
		assertNotNull(chat.getId());
		assertNotNull(chat.getCreated());
		assertTrue(chat.getLikes().isEmpty());
		
		verify(this.mockNotificationService).persistNotification(any(PublicChatNotification.class));
		
	}
	
	/**
	* Tests failure case where Chat is new but has a parent that 
	* doesn't exist
	*/
	@Test
	void testCreateNewChatUnknownParent() {
		
		when(this.mockChatDao.existsById(PARENT_ID)).thenReturn(false);
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.createChat(PARENT_ID, MESSAGE, this.mockPrincipal);
		});
		
		assertEquals(PublicChatService.ERR_MSG_UNKNOWN_PARENT, ex.getMessage());
			
		verify(this.mockChatDao, never()).saveChat(any(PublicChat.class));
		verify(this.mockNotificationService, never()).persistNotification(any(PublicChatNotification.class));
		
	}
	
	/**
	* Tests successful update of an existing Chat 
	*/
	@Test
	void testUPdateChatHappyPathExistingChat() {
		
		final String newMessage = "a new Messaeg";
		
		ArgumentCaptor<PublicChat> argCaptChat = ArgumentCaptor.forClass(PublicChat.class);
		
		PublicChat oldChat = PublicChat
				.builder()
					.id(ID)
					.audienceType(AUDIENCE_TYPE_VAL)
					.parentChat(PARENT_ID)
					.ownerId(OWNER_ID)
					.created(CREATED)
					.message(MESSAGE)
					.likes(likes)
				.build();
		
		PublicChat chat = PublicChat
				.builder()
					.id(ID)
					.audienceType(AUDIENCE_TYPE_VAL)
					.parentChat(PARENT_ID)
					.ownerId(OWNER_ID)
					.created(CREATED)
					.message(newMessage)
					.likes(likes)
				.build();
		
		when(this.mockPrincipal.getName()).thenReturn(OWNER_ID);
		when(this.mockChatDao.existsById(ID)).thenReturn(true);
		when(this.mockChatDao.fetchChatById(ID)).thenReturn(Optional.of(oldChat));
		
		doNothing().when(this.mockChatDao).saveChat(argCaptChat.capture());
		
		this.service.updateChat(ID, MESSAGE,  this.mockPrincipal);
		
		verify(this.mockChatDao).saveChat(any(PublicChat.class));
		
		PublicChat persistedChat = argCaptChat.getValue();
		
		assertEquals(oldChat.getId(), 				persistedChat.getId());
		assertEquals(oldChat.getAudienceType(), 	persistedChat.getAudienceType());
		assertEquals(oldChat.getParentChat(), 		persistedChat.getParentChat());
		assertEquals(oldChat.getOwnerId(), 			persistedChat.getOwnerId());
		assertEquals(oldChat.getCreated(), 			persistedChat.getCreated());
		assertEquals(newMessage, 					chat.getMessage());
		assertEquals(oldChat.getLikes().size(), 	persistedChat.getLikes().size());
		
	}
	
	/**
	* Tests failure case where attempt is made to update 
	* another users Chat 
	*/
	@Test
	void testExistingChatUpdateOtherUsersChat() {
		
		PublicChat chat = PublicChat
				.builder()
					.id(ID)
					.audienceType(AUDIENCE_TYPE_VAL)
					.parentChat(PARENT_ID)
					.ownerId(OWNER_ID)
					.created(CREATED)
					.message(MESSAGE)
					.likes(likes)
				.build();
		
		when(this.mockPrincipal.getName()).thenReturn("notTheUserInTheChatId");
		when(this.mockChatDao.existsById(ID)).thenReturn(true);
		when(this.mockChatDao.fetchChatById(ID)).thenReturn(Optional.of(chat));
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateChat(ID, MESSAGE, this.mockPrincipal);
		});
		
		assertEquals(PublicChatService.ERR_MSG_CANT_UPDATE_CHAT_FOR_ANOTHER_USER, ex.getMessage());
			
		verify(this.mockChatDao, never()).saveChat(any(PublicChat.class));
		
	}
	
	/**
	* Tests that only the message is able to be updated. All the other values are considered immutable 
	*/
	@Test
	void testUpdateChatOnlyMessageCanBeChanged() {
		
		final String newMessage = "a new Messaeg";
		
		final UUID parentId = UUID.randomUUID();
		
		ArgumentCaptor<PublicChat> argCaptChat = ArgumentCaptor.forClass(PublicChat.class);
		
		PublicChat oldChat = PublicChat
				.builder()
					.id(ID)
					.audienceType(AUDIENCE_TYPE_VAL)
					.parentChat(PARENT_ID)
					.ownerId(OWNER_ID)
					.created(CREATED)
					.message(MESSAGE)
					.likes(likes)
				.build();
		
		PublicChat chat = PublicChat
				.builder()
					.id(ID)
					.audienceType(AUDIENCE_TYPE.CANDIDATE)
					.parentChat(parentId)
					.ownerId(OWNER_ID)
					.created(LocalDateTime.of(2026, 1, 13, 15, 35, 11))
					.message(newMessage)
					.likes(Set.of())
				.build();
		
		when(this.mockPrincipal.getName()).thenReturn(OWNER_ID);
		when(this.mockChatDao.existsById(ID)).thenReturn(true);
		when(this.mockChatDao.fetchChatById(ID)).thenReturn(Optional.of(oldChat));
		
		doNothing().when(this.mockChatDao).saveChat(argCaptChat.capture());
		
		this.service.updateChat(ID, MESSAGE, this.mockPrincipal);
		
		verify(this.mockChatDao).saveChat(any(PublicChat.class));
		
		PublicChat persistedChat = argCaptChat.getValue();
		
		assertEquals(oldChat.getId(), 				persistedChat.getId());
		assertEquals(oldChat.getAudienceType(), 	persistedChat.getAudienceType());
		assertEquals(oldChat.getParentChat(), 		persistedChat.getParentChat());
		assertEquals(oldChat.getOwnerId(), 			persistedChat.getOwnerId());
		assertEquals(oldChat.getCreated(), 			persistedChat.getCreated());
		assertEquals(newMessage, 					chat.getMessage());
		assertEquals(oldChat.getLikes().size(), 	persistedChat.getLikes().size());
		
	}
	
	/**
	* Tests happy path deletion of Chat and children
	*/
	@Test
	void testDeleteChat() {
		
		final UUID childCahat1Id 		= UUID.randomUUID();
		final UUID childCahat2Id 		= UUID.randomUUID();
		final UUID grandChildCahat1Id 	= UUID.randomUUID();
		final UUID grandChildCahat2Id 	= UUID.randomUUID();
		
		PublicChat parentChat 		= PublicChat.builder().id(ID).ownerId(OWNER_ID).build();
		PublicChat childChat1 		= PublicChat.builder().id(childCahat1Id).parentChat(ID).ownerId("can19").build();
		PublicChat childChat2 		= PublicChat.builder().id(childCahat2Id).parentChat(ID).ownerId(OWNER_ID).build();
		PublicChat gandchildChat1 	= PublicChat.builder().id(grandChildCahat1Id).parentChat(childCahat1Id).ownerId(OWNER_ID).build();
		PublicChat gandchildChat2 	= PublicChat.builder().id(grandChildCahat2Id).parentChat(childCahat2Id).ownerId("rec33").build();
		
		when(this.mockPrincipal.getName()).thenReturn(OWNER_ID);
		when(this.mockChatDao.existsById(ID)).thenReturn(true);
		when(this.mockChatDao.fetchChatById(ID)).thenReturn(Optional.of(parentChat));
		
		when(this.mockChatDao.fetchChatChildren(ID)).thenReturn(Set.of(childChat1, childChat2));
		when(this.mockChatDao.fetchChatChildren(childCahat1Id)).thenReturn(Set.of(gandchildChat1));
		when(this.mockChatDao.fetchChatChildren(childCahat2Id)).thenReturn(Set.of(gandchildChat2));
		when(this.mockChatDao.fetchChatChildren(grandChildCahat1Id)).thenReturn(Set.of());
		when(this.mockChatDao.fetchChatChildren(grandChildCahat2Id)).thenReturn(Set.of());
		
		this.service.deleteChat(ID, mockPrincipal);
		
		verify(this.mockChatDao).deleteById(ID);
		verify(this.mockChatDao).deleteById(childCahat1Id);
		verify(this.mockChatDao).deleteById(childCahat2Id);
		verify(this.mockChatDao).deleteById(grandChildCahat1Id);
		verify(this.mockChatDao).deleteById(grandChildCahat2Id);
		
	}
	
	/**
	* Tests exception is thrown if attempt is made to delete
	* a non existing Chat
	*/
	@Test
	void testDeleteChatDoesntExist() {
		
		when(this.mockChatDao.existsById(ID)).thenReturn(false);
		
		assertThrows(IllegalArgumentException.class, ()-> {
			this.service.deleteChat(ID, mockPrincipal);
		});
		
	}
	
	/**
	* Tests exception is thrown if attempt is made to delete
	* a Chat belonging to another USer
	*/
	@Test
	void testDeleteChatUserIsNoOwner() {
		
		when(this.mockChatDao.existsById(ID)).thenReturn(true);
		when(this.mockChatDao.fetchChatById(ID)).thenReturn(Optional.of(PublicChat.builder().ownerId("notIt").build()));
		
		assertThrows(IllegalArgumentException.class, ()-> {
			this.service.deleteChat(ID, mockPrincipal);
		});
		
	}
	
	/**
	* Tests fetching a Chat where the Chat exists
	*/
	@Test
	void testFetchChatExists() {
		
		when(this.mockChatDao.fetchChatById(ID)).thenReturn(Optional.of(PublicChat.builder().build()));

		assertNotNull(this.service.fetchChat(ID));
		
	}
	
	/**
	* Test fetch of top level chats
	*/
	@Test
	void testFetchTopLevelChats() {
		
		when(this.mockChatDao.fetchTopLevelChats(1, 20)).thenReturn(Set.of(PublicChat.builder().build(),PublicChat.builder().build()));

		assertEquals(2, this.service.fetchTopLevelChats(1, 20).size());
	}
	
	/**
	* Tests exception thrown if not existed Chat is requested
	*/
	@Test
	void testFetchChatDoesNoExists() {
		
		when(this.mockChatDao.fetchChatById(ID)).thenReturn(Optional.empty());
		
		assertThrows(RuntimeException.class, () -> {
			this.service.fetchChat(ID);
		});
		
	}
	
	/**
	* Tests return of direct Children for a given Chat. 
	*/
	@Test
	void testChatChildren() {
		
		when(this.mockChatDao.fetchChatChildren(ID)).thenReturn(Set.of(PublicChat.builder().build(), PublicChat.builder().build()));
		
		assertEquals(2, this.service.fetchChatChildren(ID).size());
	}
	
	/**
	* Tests deletion of Chats older than or equal to a given date
	*/
	@Test
	void testDeleteChatsOlderThan() {
		
		final UUID childCahat1Id 		= UUID.randomUUID();
		final UUID childCahat2Id 		= UUID.randomUUID();
		final UUID grandChildCahat1Id 	= UUID.randomUUID();
		final UUID grandChildCahat2Id 	= UUID.randomUUID();
		
		PublicChat parentChat 		= PublicChat.builder().id(ID).ownerId(OWNER_ID).build();
		PublicChat childChat1 		= PublicChat.builder().id(childCahat1Id).parentChat(ID).ownerId("can19").build();
		PublicChat childChat2 		= PublicChat.builder().id(childCahat2Id).parentChat(ID).ownerId(OWNER_ID).build();
		PublicChat gandchildChat1 	= PublicChat.builder().id(grandChildCahat1Id).parentChat(childCahat1Id).ownerId(OWNER_ID).build();
		PublicChat gandchildChat2 	= PublicChat.builder().id(grandChildCahat2Id).parentChat(childCahat2Id).ownerId("rec33").build();
	
		when(this.mockChatDao.fetchTopLevelChatsOlderThanOrEqualTo(LocalDate.of(2026, 1, 23))).thenReturn(Set.of(parentChat));
		
		when(this.mockChatDao.fetchChatChildren(ID)).thenReturn(Set.of(childChat1, childChat2));
		when(this.mockChatDao.fetchChatChildren(childCahat1Id)).thenReturn(Set.of(gandchildChat1));
		when(this.mockChatDao.fetchChatChildren(childCahat2Id)).thenReturn(Set.of(gandchildChat2));
		when(this.mockChatDao.fetchChatChildren(grandChildCahat1Id)).thenReturn(Set.of());
		when(this.mockChatDao.fetchChatChildren(grandChildCahat2Id)).thenReturn(Set.of());
		
		this.service.deleteChatsOlderThan(LocalDate.of(2026, 1, 23));
		
		verify(this.mockChatDao).deleteById(ID);
		verify(this.mockChatDao).deleteById(childCahat1Id);
		verify(this.mockChatDao).deleteById(childCahat2Id);
		verify(this.mockChatDao).deleteById(grandChildCahat1Id);
		verify(this.mockChatDao).deleteById(grandChildCahat2Id);
		
	}
	
	/**
	* Tests adding Like to an existing post
	*/
	@Test
	void testToggleLikeForChatLike() {
		
		when(this.mockPrincipal.getName()).thenReturn(OWNER_ID);
		when(this.mockChatDao.fetchPublicChat(any())).thenReturn(Optional.of(PublicChat.builder().build()));
		ArgumentCaptor<PublicChat> argCapt = ArgumentCaptor.forClass(PublicChat.class);
		
		Set<String> chatLikes = Set.of("rec33", "can44");
		
		when(this.mockChatDao.fetchChatById(ID)).thenReturn(Optional.of(PublicChat.builder().likes(chatLikes).build()));
		
		doNothing().when(this.mockChatDao).saveChat(argCapt.capture());
		
		this.service.toggleLikeForChat(ID, this.mockPrincipal.getName());
		
		verify(this.mockChatDao).saveChat(any(PublicChat.class));
		
		PublicChat chat = argCapt.getValue();
		
		assertEquals(3, chat.getLikes().size());
		assertTrue(chat.getLikes().contains("rec33"));
		assertTrue(chat.getLikes().contains("can44"));
		assertTrue(chat.getLikes().contains(this.mockPrincipal.getName()));
		
		verify(this.mockNotificationService).persistNotification(any(PublicChatNotification.class));
	}
	
	/**
	* Tests removing like from an existing post 
	*/
	@Test
	void testToggleLikeForChatUnlike() {
		
		when(this.mockPrincipal.getName()).thenReturn(OWNER_ID);
		
		ArgumentCaptor<PublicChat> argCapt = ArgumentCaptor.forClass(PublicChat.class);
		
		Set<String> likes = Set.of("rec33", "can44", this.mockPrincipal.getName());
		
		when(this.mockChatDao.fetchChatById(ID)).thenReturn(Optional.of(PublicChat.builder().likes(likes).build()));
		
		PublicChatNotification notification = PublicChatNotification.builder().type(NotificationType.LIKE).initiatingUserId(this.mockPrincipal.getName()).build();
		when(this.mockNotificationService.fetchNotificationsForChat(any(UUID.class))).thenReturn(Set.of(notification));
			
		
		doNothing().when(this.mockChatDao).saveChat(argCapt.capture());
		
		this.service.toggleLikeForChat(ID, this.mockPrincipal.getName());
		
		verify(this.mockChatDao).saveChat(any(PublicChat.class));
		
		PublicChat chat = argCapt.getValue();
		
		assertEquals(2, chat.getLikes().size());
		assertTrue(chat.getLikes().contains("rec33"));
		assertTrue(chat.getLikes().contains("can44"));
		
		verify(this.mockNotificationService, never()).persistNotification(any(PublicChatNotification.class));
		verify(this.mockNotificationService).deleteNotification(any(), anyString());
	}
	
	/**
	* Tests adding like to an non existent post
	*/
	@Test
	void testToggleLikeForChatUnknownChat() {
		
		when(this.mockPrincipal.getName()).thenReturn(OWNER_ID);
		
		when(this.mockChatDao.fetchChatById(ID)).thenReturn(Optional.empty());
		
		this.service.toggleLikeForChat(ID, this.mockPrincipal.getName());
		
		verify(this.mockChatDao, never()).saveChat(any(PublicChat.class));
		
	}
	
	/**
	* Test case in which no Newsfeed items ( PublicChat's ) exist
	*/
	@Test
	void testIsUnreadNewsFeedItemsNoChats() {
		
		final String userId = "candidate1";
		
		when(this.service.fetchTopLevelChats(0, 1)).thenReturn(Set.of());
		
		assertFalse(this.service.isUnreadNewsFeedItems(userId));
	}
	
	/**
	* Test case in which the specified ChatParticipant 
	* doesn't exist
	*/
	@Test
	void testIsUnreadNewsFeedItemsNoMatchingParticipant() {
		
		final String userId = "candidate1";
		
		when(this.service.fetchTopLevelChats(0, 1)).thenReturn(Set.of(PublicChat.builder().build()));
		when(this.mockParticipantService.fetchById(userId)).thenReturn(Optional.empty());
		
		assertFalse(this.service.isUnreadNewsFeedItems(userId));
		
	}
	
	/**
	* Test case in which the specified ChatParticipant 
	* exists but has never viewed the newsfeed
	*/
	@Test
	void testIsUnreadNewsFeedItemsNoLastView() {
		
		final String 		userId 	= "candidate1";
		final LocalDateTime created = LocalDateTime.of(2026,2, 27, 15, 28, 11);
		
		when(this.service.fetchTopLevelChats(0, 1)).thenReturn(Set.of(PublicChat.builder().created(created).build()));
		when(this.mockParticipantService.fetchById(userId)).thenReturn(Optional.of(ChatParticipant.builder().build()));
		
		assertTrue(this.service.isUnreadNewsFeedItems(userId));
		
	}
	
	/**
	* Test case in which the specified ChatParticipant 
	* exists and has viewed the newsfeed but no new
	* posts have been added since the last view
	*/
	@Test
	void testIsUnreadNewsFeedItemsNoNewPosts() {
		
		final String 			userId 				= "candidate1";
		final LocalDateTime 	created 			= LocalDateTime.of(2026,2, 27, 15, 28, 11);
		final LocalDateTime 	lastViewcreated 	= LocalDateTime.of(2026,2, 27, 16, 28, 11);
		
		when(this.service.fetchTopLevelChats(0, 1)).thenReturn(Set.of(PublicChat.builder().created(created).build()));
		when(this.mockParticipantService.fetchById(userId)).thenReturn(Optional.of(ChatParticipant.builder().lastTimeNewsFeedViewed(lastViewcreated).build()));
		
		assertFalse(this.service.isUnreadNewsFeedItems(userId));
		
	}
	
	/**
	* Test case in which the specified ChatParticipant 
	* exists and has viewed the newsfeed but no new
	* posts have been added since the last view
	*/
	@Test
	void testIsUnreadNewsFeedItemsNewPosts() {
		
		final String 		userId 				= "candidate1";
		final LocalDateTime created 			= LocalDateTime.of(2026,2, 27, 15, 28, 11);
		final LocalDateTime lastViewcreated 	= LocalDateTime.of(2026,2, 27, 14, 28, 11);
		
		when(this.service.fetchTopLevelChats(0, 1)).thenReturn(Set.of(PublicChat.builder().created(created).build()));
		when(this.mockParticipantService.fetchById(userId)).thenReturn(Optional.of(ChatParticipant.builder().lastTimeNewsFeedViewed(lastViewcreated).build()));
		
		assertTrue(this.service.isUnreadNewsFeedItems(userId));
		
	}
		
}