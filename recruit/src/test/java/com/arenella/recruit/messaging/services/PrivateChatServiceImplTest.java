package com.arenella.recruit.messaging.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.messaging.beans.PrivateChat;
import com.arenella.recruit.messaging.dao.PrivateChatDao;

/**
* Unit tests for the PrivateChatServiceImpl class 
*/
@ExtendWith(MockitoExtension.class)
class PrivateChatServiceImplTest {
	
	private static final boolean 		NON_DEFAULT_BLOCKED_BY_RECIPIENT 	= true;
	private static final boolean 		NON_DEFAULT_BLOCKED_BY_SENDER		= true;
	private static final LocalDateTime 	NON_DEFUALT_CREATED 				= LocalDateTime.of(2025, 11, 27, 23, 8, 1);
	private static final LocalDateTime 	NON_DEFAULT_UPDATED 				= LocalDateTime.of(2025, 11, 27, 23, 8, 2);
	private static final LocalDateTime 	NON_DEFUALT_KEYPRESS_SENDER 		= LocalDateTime.of(2025, 11, 27, 23, 8, 3);
	private static final LocalDateTime 	NON_DEFAULT_KEYPRESS_RECIPIENT 		= LocalDateTime.of(2025, 11, 27, 23, 8, 4);
	private static final LocalDateTime 	NON_DEFUALT_LASTVIEW_SENDER 		= LocalDateTime.of(2025, 11, 27, 23, 8, 5);
	private static final LocalDateTime 	NON_DEFAULT_LASTVIEW_RECIPIENT 		= LocalDateTime.of(2025, 11, 27, 23, 8, 6);
	
	@Mock
	private PrivateChatDao 			mockChatDao;
	
	@InjectMocks
	private PrivateChatServiceImpl 	service;
	
	/**
	* Tests retrieval of User's chats 
	*/
	@Test
	void testFetchUserChats() {
		
		final String userId = "1234";
		
		PrivateChat chat1 = PrivateChat.builder().id(UUID.randomUUID()).build();
		PrivateChat chat2 = PrivateChat.builder().id(UUID.randomUUID()).build();
		
		when(mockChatDao.fetchUserChats(userId)).thenReturn(Set.of(chat1,chat2));
		
		assertTrue(service.getUsersChats(userId).stream().filter(c -> c.getId().equals(chat1.getId())).findAny().isPresent());
		assertTrue(service.getUsersChats(userId).stream().filter(c -> c.getId().equals(chat2.getId())).findAny().isPresent());
	
		assertEquals(2, service.getUsersChats(userId).size());
		
	}
	
	/**
	* Test Exception is thrown if unknown Chat is requested
	*/
	@Test
	void testFetchChatUnknownChat() {
		
		final String 	userId 	= "1234";
		final UUID 		chatId 	= UUID.randomUUID();
		
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.empty());
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()->{
			this.service.getChat(chatId, userId);
		});
		
		assertEquals(PrivateChatService.ERR_MSG_UNKNOWN_CHAT, ex.getMessage());
		
	}
	
	/**
	* Test Exception is thrown if a Use that is neither the Sender or the 
	* Recipient attempts to fetch the Chat
	*/
	@Test
	void testFetchChatOtherUsersChat() {
		
		final String 		userId 		= "1234";
		final String		senderId 	= "rec001";
		final String		recipientId	= "5577";
		final UUID 			chatId 		= UUID.randomUUID();
		final PrivateChat 	chat 		= PrivateChat.builder().id(chatId).senderId(senderId).recipientId(recipientId).build();
		
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.of(chat));
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()->{
			this.service.getChat(chatId, userId);
		});
		
		assertEquals(PrivateChatService.ERR_MSG_UNAUTHORISED_CHAT, ex.getMessage());
		
	}
	
	/**
	* Tests successful retrieval of Chat by the User 
	*/
	@Test
	void testFetchChatHappyPathUserIsSender() {
		
		final String 		userId 		= "1234";
		final String		recipientId	= "5577";
		final UUID 			chatId 		= UUID.randomUUID();
		final PrivateChat 	chat 		= PrivateChat.builder().id(chatId).senderId(userId).recipientId(recipientId).build();
		
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.of(chat));
		
		PrivateChat requestedChat = this.service.getChat(chatId, userId);
		
		assertEquals(chatId, requestedChat.getId());
		
	}
	
	/**
	* Tests successful retrieval of Chat by the User 
	*/
	@Test
	void testFetchChatHappyPathUserIsRecipient() {
		
		final String 		userId 		= "1234";
		final String		senderId 	= "rec001";
		final UUID 			chatId 		= UUID.randomUUID();
		final PrivateChat 	chat 		= PrivateChat.builder().id(chatId).senderId(senderId).recipientId(userId).build();
		
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.of(chat));
		
		PrivateChat requestedChat = this.service.getChat(chatId, userId);
		
		assertEquals(chatId, requestedChat.getId());
		
	}
	
	/**
	* Tests that if Chat already exists, Exception is thrown
	*/
	@Test
	void saveChatAlreadyExists() {
		
		final UUID 			chatId 		= UUID.randomUUID();
		final String		userId		= "authenticatedUser";
		final String		senderId 	= "rec001";
		final String		recipientId	= "5577";
		final PrivateChat 	chat 		= PrivateChat.builder().id(chatId).senderId(senderId).recipientId(recipientId).build();
		
		when(this.mockChatDao.existsById(chatId)).thenReturn(true);
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.saveChat(chat, userId);
		});
		
		assertEquals(PrivateChatService.ERR_MSG_CANNOT_CREATE_CHAT, ex.getMessage());
		verify(this.mockChatDao, never()).saveChat(any(PrivateChat.class));
		
	}
	
	/**
	* Tests that it is not possible to create a Chat for another User
	*/
	@Test
	void testCreateChatForAnotherUser() {
		
		final UUID 			chatId 		= UUID.randomUUID();
		final String		userId		= "authenticatedUser";
		final String		senderId 	= "rec001";
		final String		recipientId	= "5577";
		final PrivateChat 	chat 		= PrivateChat.builder().id(chatId).senderId(senderId).recipientId(recipientId).build();
		
		when(this.mockChatDao.existsById(chatId)).thenReturn(false);
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.saveChat(chat, userId);
		});
		
		assertEquals(PrivateChatService.ERR_MSG_CANNOT_CREATE_CHAT_FOR_ANOTHER_USER, ex.getMessage());
		verify(this.mockChatDao, never()).saveChat(any(PrivateChat.class));
		
	}
	
	/**
	* Tests that it is not possible to create a Chat with yourself
	*/
	@Test
	void testCreateChatWithYourelf() {
		
		final UUID 			chatId 		= UUID.randomUUID();
		final String		userId		= "authenticatedUser";
		final PrivateChat 	chat 		= PrivateChat.builder().id(chatId).senderId(userId).recipientId(userId).build();
		
		when(this.mockChatDao.existsById(chatId)).thenReturn(false);
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.saveChat(chat, userId);
		});
		
		assertEquals(PrivateChatService.ERR_MSG_CANNOT_CREATE_CHAT_WITH_YOURSELF, ex.getMessage());
		verify(this.mockChatDao, never()).saveChat(any(PrivateChat.class));
		
	}
	
	/**
	* Tests if no existing Chat has the same combination of Sender and Recipient
	*/
	@Test
	void testCreateChatCombinationExistsSenderIsUser() {
		
		final UUID 			chatId 		= UUID.randomUUID();
		final String		userId		= "authenticatedUser";
		final String		recipientId	= "5577";
		final PrivateChat 	chat 		= PrivateChat.builder().id(chatId).senderId(userId).recipientId(recipientId).build();
		
		when(this.mockChatDao.existsById(chatId)).thenReturn(false);
		when(this.mockChatDao.fetchUserChats(userId)).thenReturn(Set.of(PrivateChat.builder().senderId(userId).recipientId(recipientId).build()));
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.saveChat(chat, userId);
		});
		
		assertEquals(PrivateChatService.ERR_MSG_CANNOT_CREATE_CHAT_COMBINATION_EXISTS, ex.getMessage());
		verify(this.mockChatDao, never()).saveChat(any(PrivateChat.class));
		
	}
	
	/**
	* Tests if no existing Chat has the same combination of Sender and Recipient
	*/
	@Test
	void testCreateChatCombinationExistsRecipientIsUser() {
		
		final UUID 			chatId 		= UUID.randomUUID();
		final String		userId		= "authenticatedUser";
		final String		recipientId	= "5577";
		final PrivateChat 	chat 		= PrivateChat.builder().id(chatId).senderId(userId).recipientId(recipientId).build();
		
		when(this.mockChatDao.existsById(chatId)).thenReturn(false);
		when(this.mockChatDao.fetchUserChats(userId)).thenReturn(Set.of(PrivateChat.builder().senderId(recipientId).recipientId(userId).build()));
	
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.saveChat(chat, userId);
		});
		
		assertEquals(PrivateChatService.ERR_MSG_CANNOT_CREATE_CHAT_COMBINATION_EXISTS, ex.getMessage());
		verify(this.mockChatDao, never()).saveChat(any(PrivateChat.class));
		
	}
	
	/**
	* Tests happy path. 
	* Verifies that only attributes permissible to be manually set 
	* can be set and will be overridden even if provided before
	* persistence takes place 
	*/
	@Test
	void testCreateChatSuccess() {
		
		final ArgumentCaptor<PrivateChat> chatArgCapt = ArgumentCaptor.forClass(PrivateChat.class);
		
		final UUID 			chatId 				= UUID.randomUUID();
		final String		userId				= "authenticatedUser";
		final String		recipientId			= "5577";
		final String		recipientIdOther	= "5578";
		final PrivateChat 	chat 				= PrivateChat
				.builder()
					.id(chatId)
					.senderId(userId)
					.recipientId(recipientId)
					.blockedByRecipient(NON_DEFAULT_BLOCKED_BY_RECIPIENT)
					.blockedBySender(NON_DEFAULT_BLOCKED_BY_SENDER)
					.created(NON_DEFUALT_CREATED)
					.lastKeyPressRecipient(NON_DEFAULT_KEYPRESS_RECIPIENT)
					.lastKeyPressSender(NON_DEFUALT_KEYPRESS_SENDER)
					.lastUpdate(NON_DEFAULT_UPDATED)
					.lastViewedByRecipient(NON_DEFAULT_LASTVIEW_RECIPIENT)
					.lastViewedBySender(NON_DEFUALT_LASTVIEW_SENDER)
				.build();
		
		doNothing().when(mockChatDao).saveChat(chatArgCapt.capture());
		when(this.mockChatDao.existsById(chatId)).thenReturn(false);
		when(this.mockChatDao.fetchUserChats(userId)).thenReturn(Set.of(PrivateChat.builder().senderId(userId).recipientId(recipientIdOther).build(),PrivateChat.builder().senderId(recipientIdOther).recipientId(userId).build()));
	
		this.service.saveChat(chat, userId);
		
		verify(this.mockChatDao).saveChat(any(PrivateChat.class));
		
		PrivateChat cpt = chatArgCapt.getValue();
		
		assertEquals(chat.getId(), 				cpt.getId());
		assertEquals(chat.getSenderId(), 		cpt.getSenderId());
		assertEquals(chat.getRecipientId(), 	cpt.getRecipientId());
		assertNotNull(chat.getCreated());
		assertNotEquals(NON_DEFUALT_CREATED, 	cpt.getCreated());
		assertEquals(cpt.getCreated(), 			cpt.getLastUpdated());
		assertFalse(cpt.isBlockedBySender());
		assertFalse(cpt.isBlockedByRecipient());
		assertTrue(cpt.getLastKeyPressSender().isEmpty());
		assertTrue(cpt.getLastKeyPressRecipient().isEmpty());
		assertTrue(cpt.getLastViewedBySender().isEmpty());
		assertTrue(cpt.getLastViewedByRecipient().isEmpty());
		
	}
	
	/**
	* Tests deletion of Chats where User is either
	* Sender or Recipient 
	*/
	@Test
	void testDeleteUserChats() {
		
		final String userId = "kprec01";
		
		when(this.service.getUsersChats(userId)).thenReturn(Set.of(PrivateChat.builder().id(UUID.randomUUID()).build(),PrivateChat.builder().id(UUID.randomUUID()).build()));
		
		this.service.deleteUserChats(userId);
		
		verify(this.mockChatDao, times(2)).deleteById(Mockito.any(UUID.class));
		
	}
	
}