package com.arenella.recruit.messaging.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.PrivateChat;
import com.arenella.recruit.messaging.services.ParticipantService;
import com.arenella.recruit.messaging.services.PrivateChatService;

/**
* Unit tests for the PrivateChatController Class 
*/
@ExtendWith(MockitoExtension.class)
class PrivateChatControllerTest {

	@Mock
	private PrivateChatService 		mockPrivateChatService;
	
	@Mock
	private ParticipantService		mockParticipantService;
	
	@Mock
	private Principal				mockPrincipal;
	
	@InjectMocks
	private PrivateChatController 	controller;
	
	/**
	* Tests creation of new PrivateChat 
	*/
	@Test
	void testCreatePrivateChat() {
	
		final ArgumentCaptor<PrivateChat> 	chatArgCapt 	= ArgumentCaptor.forClass(PrivateChat.class);
		final UUID							id 				= UUID.randomUUID();
		final String 						senderId 		= "1234";
		final String 						recipientId 	= "5678";
		
		when(this.mockPrivateChatService.saveChat(chatArgCapt.capture(), Mockito.any())).thenReturn(id);
		
		PrivateChatAPIInbound 	chat 		= PrivateChatAPIInbound.builder().senderId(senderId).recipientId(recipientId).build();
		ResponseEntity<UUID> 	response 	= this.controller.createPrivateChat(chat, mockPrincipal);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertTrue(response.getBody() instanceof UUID);
		
	}
	
	/**
	* Tests retrieval of a requested PrivateChat 
	*/
	@Test
	void testFetchPrivateChatById() {
		
		final String 			senderId 	= "rec1";
		final String 			receiverId 	= "can1";
		final UUID 				chatId 		= UUID.randomUUID();
		final PrivateChat 		chat 		= PrivateChat.builder().id(chatId).senderId(senderId).recipientId(receiverId).build();
		
		final ChatParticipant 	sender 		= ChatParticipant.builder().participantId("rec1").firstName("Bob").surname("Parkings").build();
		final ChatParticipant 	receiver 	= ChatParticipant.builder().participantId("can1").firstName("Lorraine").surname("Parkings").build();
		
		when(this.mockPrivateChatService.getChat(chatId, mockPrincipal)).thenReturn(chat);
		when(this.mockParticipantService.fetchById(senderId)).thenReturn(Optional.of(sender));
		when(this.mockParticipantService.fetchById(receiverId)).thenReturn(Optional.of(receiver));
		
		ResponseEntity<PrivateChatAPIOutbound> response = controller.fetchPrivateChatById(chatId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(chatId, response.getBody().getId());
		
		assertEquals(senderId,   response.getBody().getSender().getId());
		assertEquals(receiverId, response.getBody().getRecipient().getId());
	}
	
	/**
	* Tests if Sender is not available an exception is thrown
	*/
	@Test
	void testFetchPrivateChatByIdNoSender() {
		
		final String 			senderId 	= "rec1";
		final String 			receiverId 	= "can1";
		final UUID 				chatId 		= UUID.randomUUID();
		final PrivateChat 		chat 		= PrivateChat.builder().id(chatId).senderId(senderId).recipientId(receiverId).build();
		
		when(this.mockPrivateChatService.getChat(chatId, mockPrincipal)).thenReturn(chat);
		when(this.mockParticipantService.fetchById(senderId)).thenReturn(Optional.empty());
		
		assertThrows(IllegalStateException.class, () -> {
			controller.fetchPrivateChatById(chatId, mockPrincipal);
		});
		
	}
	
	/**
	* Tests if Recipient is not available an exception is thrown 
	*/
	@Test
	void testFetchPrivateChatByIdNoRecipient() {
		
		final String 			senderId 	= "rec1";
		final String 			receiverId 	= "can1";
		final UUID 				chatId 		= UUID.randomUUID();
		final PrivateChat 		chat 		= PrivateChat.builder().id(chatId).senderId(senderId).recipientId(receiverId).build();
		
		final ChatParticipant 	sender 		= ChatParticipant.builder().participantId("rec1").firstName("Bob").surname("Parkings").build();
		
		when(this.mockPrivateChatService.getChat(chatId, mockPrincipal)).thenReturn(chat);
		when(this.mockParticipantService.fetchById(senderId)).thenReturn(Optional.of(sender));
		when(this.mockParticipantService.fetchById(receiverId)).thenReturn(Optional.empty());
		
		assertThrows(IllegalStateException.class, () -> {
			controller.fetchPrivateChatById(chatId, mockPrincipal);
		});
		
	}
	
	/**
	* Tests retrieval of Chat's for a specific User 
	*/
	@Test
	void testFetchChatsByUserId() {
		
		final String			senderId1 		= "s1";
		final String			recipientId1 	= "r1";
		final ChatParticipant	sender1			= ChatParticipant.builder().participantId(senderId1).build();
		final ChatParticipant	recipient1		= ChatParticipant.builder().participantId(recipientId1).build();
		final UUID 				chatId1 		= UUID.randomUUID();
		final PrivateChat 		chat1 			= PrivateChat.builder().id(chatId1).senderId(senderId1).recipientId(recipientId1).build();
		
		when(this.mockPrivateChatService.getUsersChats(mockPrincipal)).thenReturn(Set.of(chat1));
		when(this.mockParticipantService.fetchById(senderId1)).thenReturn(Optional.of(sender1));
		when(this.mockParticipantService.fetchById(recipientId1)).thenReturn(Optional.of(recipient1));
		ResponseEntity<Set<PrivateChatAPIOutbound>> response = controller.fetchChatsByUserId(mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(chatId1, response.getBody().stream().findFirst().get().getId());
		
	}
	
	/**
	* Tests retrieval of Chat's for a specific User where one of the Chats has no 
	* associated Sender. In this case the Chat should be excluded from the results
	*/
	@Test
	void testFetchChatsByUserIdChatWithMissingSender() {
		
		final String			senderId1 		= "s1";
		final String			recipientId1 	= "r1";
		final UUID 				chatId1 		= UUID.randomUUID();
		final PrivateChat 		chat1 			= PrivateChat.builder().id(chatId1).senderId(senderId1).recipientId(recipientId1).build();
		
		final String			senderId2		= "s2";
		final String			recipientId2 	= "r2";
		final ChatParticipant	sender2			= ChatParticipant.builder().participantId(senderId2).build();
		final ChatParticipant	recipient2		= ChatParticipant.builder().participantId(recipientId2).build();
		final UUID 				chatId2 		= UUID.randomUUID();
		final PrivateChat 		chat2 			= PrivateChat.builder().id(chatId2).senderId(senderId2).recipientId(recipientId2).build();
		
		when(this.mockPrivateChatService.getUsersChats(mockPrincipal)).thenReturn(Set.of(chat1, chat2));
		when(this.mockParticipantService.fetchById(senderId1)).thenReturn(Optional.empty());
		when(this.mockParticipantService.fetchById(senderId2)).thenReturn(Optional.of(sender2));
		when(this.mockParticipantService.fetchById(recipientId2)).thenReturn(Optional.of(recipient2));
		ResponseEntity<Set<PrivateChatAPIOutbound>> response = controller.fetchChatsByUserId(mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(chatId2, response.getBody().stream().findFirst().get().getId());
		assertEquals(1, response.getBody().size());
	}
	
	/**
	* Tests retrieval of Chat's for a specific User where one of the Chats has no 
	* associated Receiver. In this case the Chat should be excluded from the results
	*/
	@Test
	void testFetchChatsByUserIdChatWithMissinRecipient() {
		
		final String			senderId1 		= "s1";
		final String			recipientId1 	= "r1";
		final ChatParticipant	sender1			= ChatParticipant.builder().participantId(senderId1).build();
		final UUID 				chatId1 		= UUID.randomUUID();
		final PrivateChat 		chat1 			= PrivateChat.builder().id(chatId1).senderId(senderId1).recipientId(recipientId1).build();
		
		final String			senderId2		= "s2";
		final String			recipientId2 	= "r2";
		final ChatParticipant	sender2			= ChatParticipant.builder().participantId(senderId2).build();
		final ChatParticipant	recipient2		= ChatParticipant.builder().participantId(recipientId2).build();
		final UUID 				chatId2 		= UUID.randomUUID();
		final PrivateChat 		chat2 			= PrivateChat.builder().id(chatId2).senderId(senderId2).recipientId(recipientId2).build();
		
		when(this.mockPrivateChatService.getUsersChats(mockPrincipal)).thenReturn(Set.of(chat1, chat2));
		when(this.mockParticipantService.fetchById(senderId1)).thenReturn(Optional.of(sender1));
		when(this.mockParticipantService.fetchById(recipientId1)).thenReturn(Optional.empty());
		when(this.mockParticipantService.fetchById(senderId2)).thenReturn(Optional.of(sender2));
		when(this.mockParticipantService.fetchById(recipientId2)).thenReturn(Optional.of(recipient2));
		ResponseEntity<Set<PrivateChatAPIOutbound>> response = controller.fetchChatsByUserId(mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(chatId2, response.getBody().stream().findFirst().get().getId());
		assertEquals(1, response.getBody().size());
		
	}
	
	/**
	* Tests setting the Blocked status of a Chat 
	*/
	@Test
	void testDoSetBlockedStatus() {
		
		final UUID 		chatId 	= UUID.randomUUID();
		final boolean 	blocked = true;
		
		ResponseEntity<Void> response = controller.doSetBlockedStatus(chatId, blocked, mockPrincipal);
		
		verify(this.mockPrivateChatService).setBlockedStatus(chatId, mockPrincipal, true);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests setting the last viewed time of a Chat by the 
	* authenticated User
	*/
	@Test
	void testDoSetLastViewed() {
		
		final UUID chatId = UUID.randomUUID();
		
		ResponseEntity<Void> response = controller.doSetLastViewed(chatId, mockPrincipal);
		
		verify(this.mockPrivateChatService).setLastViewed(chatId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests setting the last viewed time of a Chat by the 
	* authenticated User
	*/
	@Test
	void testDoSetKeyPressed() {
		
		final UUID chatId = UUID.randomUUID();
		
		ResponseEntity<Void> response = controller.doSetKeyPressed(chatId, mockPrincipal);
		
		verify(this.mockPrivateChatService).setLastKeyPress(chatId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests adding new message to Char
	*/
	@Test
	void testAddMessage() {
		
		final UUID chatId = UUID.randomUUID();
		
		ChatMessageAPIInbound msg = ChatMessageAPIInbound.builder().build();
		
		ResponseEntity<Void> response = controller.doAddMessageToChat(chatId, msg, mockPrincipal);
		
		verify(this.mockPrivateChatService).addMessage(chatId, msg.getMessage(), mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests marking existing message as deleted 
	*/
	void testDeleteMessage() {
		
		final UUID chatId 		= UUID.randomUUID();
		final UUID messageId 	= UUID.randomUUID();
		
		when(this.mockPrincipal.getName()).thenReturn("1234");
		
		ResponseEntity<Void> response = controller.doDeleteMessageFromChat(chatId, messageId, mockPrincipal);
		
		verify(this.mockPrivateChatService).deleteMessage(chatId, messageId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
}