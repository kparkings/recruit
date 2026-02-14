package com.arenella.recruit.messaging.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import com.arenella.recruit.authentication.spring.filters.ClaimsUsernamePasswordAuthenticationToken;
import com.arenella.recruit.messaging.beans.ChatMessage;
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
	private PrivateChatDao 								mockChatDao;
	
	@Mock
	private ClaimsUsernamePasswordAuthenticationToken	mockPrincipal;
	
	@Mock
	private GrantedAuthority							mockGrantedAuthority;
	
	@InjectMocks
	private PrivateChatServiceImpl 						service;
	
	/**
	* Sets up test environment 
	*/
	@BeforeEach
	void init() {
		lenient().when(this.mockPrincipal.getAuthorities()).thenReturn(Set.of(mockGrantedAuthority));
		lenient().when(this.mockGrantedAuthority.getAuthority()).thenReturn("ROLE_RECRUITER");
	}
	
	/**
	* Test Exception is thrown if User does not have permission 
	* to use Chat services
	*/
	@Test
	void testFetchUserChatsNoChatPermissions() {
		
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(true));
		
		assertThrows(RuntimeException.class, ()-> 
			service.getUsersChats(this.mockPrincipal)
		);
		
	}
	
	/**
	* Tests retrieval of User's chats 
	*/
	@Test
	void testFetchUserChats() {
		
		final String userId = "1234";
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		
		PrivateChat chat1 = PrivateChat.builder().id(UUID.randomUUID()).build();
		PrivateChat chat2 = PrivateChat.builder().id(UUID.randomUUID()).build();
		
		when(mockChatDao.fetchUserChats(userId)).thenReturn(Set.of(chat1,chat2));
		
		assertTrue(service.getUsersChats(this.mockPrincipal).stream().filter(c -> c.getId().equals(chat1.getId())).findAny().isPresent());
		assertTrue(service.getUsersChats(this.mockPrincipal).stream().filter(c -> c.getId().equals(chat2.getId())).findAny().isPresent());
	
		assertEquals(2, service.getUsersChats(this.mockPrincipal).size());
		
	}
	
	/**
	* Test Exception is thrown if User does not have permission 
	* to use Chat services
	*/
	@Test
	void testFetchChatNoChatPermissions() {
		
		final UUID 		chatId 	= UUID.randomUUID();
		
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(true));
		
		assertThrows(RuntimeException.class, ()-> 
		this.service.getChat(chatId, this.mockPrincipal)
		);
		
	}
	
	/**
	* Test Exception is thrown if unknown Chat is requested
	*/
	@Test
	void testFetchChatUnknownChat() {
		
		final UUID 		chatId 	= UUID.randomUUID();
		
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.empty());
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()->{
			this.service.getChat(chatId, this.mockPrincipal);
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
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.of(chat));
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()->{
			this.service.getChat(chatId, this.mockPrincipal);
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
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.of(chat));
		
		PrivateChat requestedChat = this.service.getChat(chatId, this.mockPrincipal);
		
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
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.of(chat));
		
		PrivateChat requestedChat = this.service.getChat(chatId, this.mockPrincipal);
		
		assertEquals(chatId, requestedChat.getId());
		
	}
	
	/**
	* Test Exception is thrown if User does not have permission 
	* to use Chat services
	*/
	@Test
	void testSaveChatNoChatPermissions() {
		
		final UUID 			chatId 		= UUID.randomUUID();
		final String		senderId 	= "rec001";
		final String		recipientId	= "5577";
		final PrivateChat 	chat 		= PrivateChat.builder().id(chatId).senderId(senderId).recipientId(recipientId).build();
		
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(true));
		
		assertThrows(RuntimeException.class, ()-> 
			this.service.saveChat(chat, this.mockPrincipal)
		);
		
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
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.existsById(chatId)).thenReturn(true);
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.saveChat(chat, this.mockPrincipal);
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
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.existsById(chatId)).thenReturn(false);
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.saveChat(chat, this.mockPrincipal);
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
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.existsById(chatId)).thenReturn(false);
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.saveChat(chat, this.mockPrincipal);
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
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.existsById(chatId)).thenReturn(false);
		when(this.mockChatDao.fetchUserChats(userId)).thenReturn(Set.of(PrivateChat.builder().senderId(userId).recipientId(recipientId).build()));
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.saveChat(chat, this.mockPrincipal);
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
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.existsById(chatId)).thenReturn(false);
		when(this.mockChatDao.fetchUserChats(userId)).thenReturn(Set.of(PrivateChat.builder().senderId(recipientId).recipientId(userId).build()));
	
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.saveChat(chat, this.mockPrincipal);
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
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		doNothing().when(mockChatDao).saveChat(chatArgCapt.capture());
		when(this.mockChatDao.existsById(chatId)).thenReturn(false);
		when(this.mockChatDao.fetchUserChats(userId)).thenReturn(Set.of(PrivateChat.builder().senderId(userId).recipientId(recipientIdOther).build(),PrivateChat.builder().senderId(recipientIdOther).recipientId(userId).build()));
	
		this.service.saveChat(chat, this.mockPrincipal);
		
		verify(this.mockChatDao).saveChat(any(PrivateChat.class));
		
		PrivateChat cpt = chatArgCapt.getValue();
		
		assertEquals(chat.getId(), 				cpt.getId());
		assertEquals(chat.getSenderId(), 		cpt.getSenderId());
		assertEquals(chat.getRecipientId(), 	cpt.getRecipientId());
		assertNotNull(chat.getCreated());
		assertNotEquals(NON_DEFUALT_CREATED, 	cpt.getCreated());
		assertEquals(cpt.getCreated(), 			cpt.getLastUpdated());
		
		assertEquals(cpt.getCreated(), 			cpt.getLastViewedBySender().get());
		assertEquals(cpt.getCreated(), 			cpt.getLastViewedByRecipient().get());
		
		assertFalse(cpt.isBlockedBySender());
		assertFalse(cpt.isBlockedByRecipient());
		assertTrue(cpt.getLastKeyPressSender().isEmpty());
		assertTrue(cpt.getLastKeyPressRecipient().isEmpty());
		
	}
	
	/**
	* Test Exception is thrown if User does not have permission 
	* to use Chat services
	*/
	@Test
	void testDeleteUserChatsNoChatPermissions() {
		
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(true));
		
		assertThrows(RuntimeException.class, ()-> 
			this.service.deleteUserChats(this.mockPrincipal)
		);
		
	}
	
	/**
	* Tests deletion of Chats where User is either
	* Sender or Recipient 
	*/
	@Test
	void testDeleteUserChats() {
		
		final String userId = "kprec01";
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.service.getUsersChats(this.mockPrincipal)).thenReturn(Set.of(PrivateChat.builder().id(UUID.randomUUID()).build(),PrivateChat.builder().id(UUID.randomUUID()).build()));
		
		this.service.deleteUserChats(this.mockPrincipal);
		
		verify(this.mockChatDao, times(2)).deleteById(Mockito.any(UUID.class));
		
	}
	
	/**
	* Test Exception is thrown if User does not have permission 
	* to use Chat services
	*/
	@Test
	void testAddMessageNoChatPermissions() {
		
		final UUID 		chatId 		= UUID.randomUUID();
		final String 	message		= "aMessage";
		
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(true));
		
		assertThrows(RuntimeException.class, ()-> 
			this.service.addMessage(chatId, message, this.mockPrincipal)
		);
		
	}
	
	/**
	* Tests exception thrown if request made to add 
	* message to non existent Chat 
	*/
	@Test
	void testAddMessageNoMatchingChat() {
		
		final UUID 		chatId 		= UUID.randomUUID();
		final String 	message		= "aMessage";
		
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.empty());
		
		RuntimeException ex = assertThrows(RuntimeException.class, () -> this.service.addMessage(chatId, message, this.mockPrincipal));
		
		assertEquals(PrivateChatServiceImpl.ERR_MSG_CANNOT_ADD_MESSAGE, ex.getMessage());
	
	}
	
	/**
	* Tests exception thrown if request made to add 
	* message to a Chat where the User is nether the 
	* Sender or Recipient in the Chat exchange 
	*/
	@Test
	void testAddMessageUserNeitherSenderNorRecipient() {
		
		final UUID 			chatId 			= UUID.randomUUID();
		final String 		chatSenderId 	= "u1";
		final String 		chatRecipientId = "u2";
		final String 		userId 			= "aUser";
		final String 		message			= "aMessage";
		final PrivateChat 	chat 			= PrivateChat.builder().senderId(chatSenderId).recipientId(chatRecipientId).build();
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.of(chat));
		
		RuntimeException ex = assertThrows(RuntimeException.class, () -> this.service.addMessage(chatId, message, this.mockPrincipal));
		
		assertEquals(PrivateChatServiceImpl.ERR_MSG_CANNOT_ADD_MESSAGE, ex.getMessage());
		
		when(this.mockPrincipal.getName()).thenReturn(chatSenderId);
		assertDoesNotThrow(()-> this.service.addMessage(chatId, message, this.mockPrincipal));
		when(this.mockPrincipal.getName()).thenReturn(chatRecipientId);
		assertDoesNotThrow(()-> this.service.addMessage(chatId, message, this.mockPrincipal));
		
	}

	/**
	* Tests if chat has been blocked by the Sender no new messages
	* can be added 
	*/
	@Test
	void testAddMessageChatBlockedBySender() {
		
		final UUID 			chatId 			= UUID.randomUUID();
		final String 		chatSenderId 	= "u1";
		final String 		chatRecipientId = "u2";
		final String 		message			= "aMessage";
		final PrivateChat 	chat 			= PrivateChat.builder().senderId(chatSenderId).recipientId(chatRecipientId).blockedBySender(true).build();
		
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.of(chat));
		
		IllegalStateException ex = assertThrows(IllegalStateException.class, ()->{
			this.service.addMessage(chatId, message, this.mockPrincipal);
		});
		
		assertEquals("Blocked", ex.getMessage());
		
	}
	
	/**
	* Tests if chat has been blocked by the Sender no new messages
	* can be added 
	*/
	@Test
	void testAddMessageChatBlockedByRecipient() {
		
		final UUID 			chatId 			= UUID.randomUUID();
		final String 		chatSenderId 	= "u1";
		final String 		chatRecipientId = "u2";
		final String 		message			= "aMessage";
		final PrivateChat 	chat 			= PrivateChat.builder().senderId(chatSenderId).senderId(chatRecipientId).blockedByRecipient(true).build();
		
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.of(chat));
		
		IllegalStateException ex = assertThrows(IllegalStateException.class, ()->{
			this.service.addMessage(chatId, message, this.mockPrincipal);
		});
		
		assertEquals("Blocked", ex.getMessage());
		
	}
	
	
	/**
	* Tests if validation is successful Message is added and 
	* persisted to the Chat 
	*/
	@Test
	void testAddMessageHappyPath() {
		
		final UUID 			chatId 			= UUID.randomUUID();
		final String 		chatSenderId 	= "u1";
		final String 		chatRecipientId = "u2";
		final String 		message			= "aMessage";
		final PrivateChat 	chat 			= PrivateChat.builder().senderId(chatSenderId).recipientId(chatRecipientId).build();
		
		ArgumentCaptor<PrivateChat> argCaptChat = ArgumentCaptor.forClass(PrivateChat.class);
		
		when(this.mockPrincipal.getName()).thenReturn(chatRecipientId);
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.of(chat));
		doNothing().when(this.mockChatDao).saveChat(argCaptChat.capture());
		
		this.service.addMessage(chatId, message, this.mockPrincipal);
		
		verify(this.mockChatDao).saveChat(chat);
		
		ChatMessage chatMessage = argCaptChat.getValue().getReplies().values().stream().findFirst().orElseThrow();
		
		assertTrue(chatMessage.getId() 		instanceof UUID);
		assertTrue(chatMessage.getCreated() instanceof LocalDateTime);
		
		assertEquals(chatId, 			chatMessage.getChatId());
		assertEquals(message, 			chatMessage.getMessage());
		assertEquals(chatRecipientId, 	chatMessage.getSenderId());
		assertEquals(chatSenderId, 		chatMessage.getRecipientId());
		
	}
	
	/**
	* Test Exception is thrown if User does not have permission 
	* to use Chat services
	*/
	@Test
	void testDeleteMessageNoChatPermissions() {
		
		final UUID 		chatId 		= UUID.randomUUID();
		final UUID 		messageId	= UUID.randomUUID();
		
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(true));
		
		assertThrows(RuntimeException.class, ()-> 
			this.service.deleteMessage(chatId, messageId, this.mockPrincipal)
		);
		
	}
	
	/**
	* Tests Exception is thrown if associated Chat is not found
	*/
	@Test
	void testDeleteMessageFromNonExistentChat() {
	
		final UUID 		chatId 		= UUID.randomUUID();
		final UUID 		messageId	= UUID.randomUUID();
		
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.empty());
		
		RuntimeException ex = assertThrows(RuntimeException.class, () -> this.service.deleteMessage(chatId, messageId, this.mockPrincipal));
		
		assertEquals(PrivateChatServiceImpl.ERR_MSG_CANNOT_DEL_MESSAGE, ex.getMessage());
		
	}
	
	/**
	* Tests Exception is thrown if associated Chat is available but the 
	* message does not exist in the Chat
	*/
	@Test
	void testDeleteNonExistentMessageFromChat() {
	
		final UUID 			chatId 			= UUID.randomUUID();
		final UUID 			messageId		= UUID.randomUUID();
		final String 		chatSenderId 	= "u1";
		final String 		chatRecipientId = "u2";
		final PrivateChat 	chat 			= PrivateChat.builder().senderId(chatSenderId).recipientId(chatRecipientId).build();
		
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.of(chat));
		
		RuntimeException ex = assertThrows(RuntimeException.class, () -> this.service.deleteMessage(chatId, messageId, this.mockPrincipal));
		
		assertEquals(PrivateChatServiceImpl.ERR_MSG_CANNOT_DEL_MESSAGE, ex.getMessage());
		
	}
	
	/**
	* Tests Exception is thrown if associated Chat is available but the 
	* message does not exist in the Chat
	*/
	@Test
	void testDeleteMessageFromAnotherSender() {
	
		final UUID 			chatId 			= UUID.randomUUID();
		final UUID 			messageId		= UUID.randomUUID();
		final String 		userId 			= "aUser";
		final String 		chatSenderId 	= "u1";
		final String 		chatRecipientId = "u2";
		final ChatMessage	message			= ChatMessage.builder().id(messageId).senderId(chatRecipientId).build();
		final PrivateChat 	chat 			= PrivateChat.builder().senderId(chatSenderId).recipientId(chatRecipientId).build();
	
		chat.addReply(message);
		
		when(this.mockPrincipal.getName()).thenReturn(userId);
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.of(chat));
		
		RuntimeException ex = assertThrows(RuntimeException.class, () -> this.service.deleteMessage(chatId, messageId, this.mockPrincipal));
		
		assertEquals(PrivateChatServiceImpl.ERR_MSG_CANNOT_DEL_MESSAGE, ex.getMessage());
		
	}
	
	/**
	* Tests happy path where user is the sender and message exists on chat
	*/
	@Test
	void testDeleteMessageHappyPath() {
	
		final UUID 			chatId 			= UUID.randomUUID();
		final UUID 			messageId		= UUID.randomUUID();
		final String 		chatSenderId 	= "u1";
		final String 		chatRecipientId = "u2";
		final ChatMessage	message			= ChatMessage.builder().id(messageId).senderId(chatSenderId).build();
		final PrivateChat 	chat 			= PrivateChat.builder().senderId(chatSenderId).recipientId(chatRecipientId).build();
	
		ArgumentCaptor<PrivateChat> argCaptChat = ArgumentCaptor.forClass(PrivateChat.class);
		
		chat.addReply(message);
		
		assertNotNull(chat.getReplies().get(messageId));
		
		doNothing().when(this.mockChatDao).saveChat(argCaptChat.capture());
		
		when(this.mockPrincipal.getName()).thenReturn(chatSenderId);
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.of(chat));
		
		this.service.deleteMessage(chatId, messageId, this.mockPrincipal);
		
		verify(this.mockChatDao).saveChat(chat);
		
		assertNull(argCaptChat.getValue().getReplies().get(messageId));
		
	}
	
	/**
	* Tests exception thrown if attempts is made to delete a message when 
	* the Chat has been blocked by the Sender
	*/
	@Test
	void testDeleteMessageBlockedBySender() {
	
		final UUID 			chatId 			= UUID.randomUUID();
		final UUID 			messageId		= UUID.randomUUID();
		final String 		chatSenderId 	= "u1";
		final String 		chatRecipientId = "u2";
		final PrivateChat 	chat 			= PrivateChat.builder().senderId(chatSenderId).recipientId(chatRecipientId).blockedBySender(true).build();
	
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.of(chat));
		
		IllegalStateException ex = assertThrows(IllegalStateException.class, ()->{
			this.service.deleteMessage(chatId, messageId, this.mockPrincipal);
		});
		
		assertEquals("Blocked", ex.getMessage());
	}
	
	/**
	* Tests exception thrown if attempts is made to delete a message when 
	* the Chat has been blocked by the Recipient
	*/
	@Test
	void testDeleteMessageBlockedByRecipient() {
	
		final UUID 			chatId 			= UUID.randomUUID();
		final UUID 			messageId		= UUID.randomUUID();
		final String 		chatSenderId 	= "u1";
		final String 		chatRecipientId = "u2";
		final PrivateChat 	chat 			= PrivateChat.builder().senderId(chatSenderId).recipientId(chatRecipientId).blockedByRecipient(true).build();
	
		when(this.mockPrincipal.getClaim("useCredits")).thenReturn(Optional.of(false));
		when(this.mockChatDao.fetchChatById(chatId)).thenReturn(Optional.of(chat));
		
		IllegalStateException ex = assertThrows(IllegalStateException.class, ()->{
			this.service.deleteMessage(chatId, messageId, this.mockPrincipal);
		});
		
		assertEquals("Blocked", ex.getMessage());
	}
	
	/**
	* Tests retrieval of Chats possibly needing undread message
	* reminders to be sent
	*/
	@Test
	void testGetUnblockedChatsBeforeCuttoff() {
		
		final LocalDateTime cuttoff 	= LocalDateTime.of(2025, 12, 18, 21, 9);
		final UUID			chatId 		= UUID.randomUUID();
		when(this.mockChatDao.getUnblockedChatsBeforeCuttoff(cuttoff)).thenReturn(Set.of(PrivateChat.builder().id(chatId).build()));
		
		Set<PrivateChat> chats = this.service.getUnblockedChatsBeforeCuttoff(cuttoff);

		assertTrue(chats.stream().filter(c -> c.getId().equals(chatId)).findAny().isPresent());
		
	}
	
	/**
	* Tests deletion of Chats for User
	*/
	@Test
	void testSystemDeleteChatsForUser() {
		
		final String userId = "aUserId";
		
		when(this.mockChatDao.fetchUserChats(userId)).thenReturn(Set.of(PrivateChat.builder().build(), PrivateChat.builder().build()));
		
		this.service.systemDeleteChatsForUser(userId);
		
		verify(this.mockChatDao, times(2)).deleteById(any());
		
	}
	
}