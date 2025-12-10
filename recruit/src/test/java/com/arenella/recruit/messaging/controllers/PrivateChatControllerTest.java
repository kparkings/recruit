package com.arenella.recruit.messaging.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
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

import com.arenella.recruit.messaging.beans.PrivateChat;
import com.arenella.recruit.messaging.services.PrivateChatService;

/**
* Unit tests for the PrivateChatController Class 
*/
@ExtendWith(MockitoExtension.class)
class PrivateChatControllerTest {

	@Mock
	private PrivateChatService 		mockPrivateChatService;
	
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
		
		final UUID chatId = UUID.randomUUID();
		final PrivateChat chat = PrivateChat.builder().id(chatId).build();
		
		when(this.mockPrivateChatService.getChat(chatId, mockPrincipal)).thenReturn(chat);
		
		ResponseEntity<PrivateChatAPIOutbound> response = controller.fetchPrivateChatById(chatId, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(chatId, response.getBody().getId());
		
	}
	
	/**
	* Tests retrieval of Chat's for a specific User 
	*/
	@Test
	void testFetchChatsByUserId() {
		
		final UUID chatId = UUID.randomUUID();
		final PrivateChat chat = PrivateChat.builder().id(chatId).build();
		
		when(this.mockPrivateChatService.getUsersChats(mockPrincipal)).thenReturn(Set.of(chat));
		
		ResponseEntity<Set<PrivateChatAPIOutbound>> response = controller.fetchChatsByUserId(mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(chatId, response.getBody().stream().findFirst().get().getId());
		
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