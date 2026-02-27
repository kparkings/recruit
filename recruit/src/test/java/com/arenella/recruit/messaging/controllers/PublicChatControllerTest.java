package com.arenella.recruit.messaging.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.PublicChat;
import com.arenella.recruit.messaging.services.ParticipantService;
import com.arenella.recruit.messaging.services.PublicChatService;

/**
* Unit tests for the PublicChatController class
*/
@ExtendWith(MockitoExtension.class)
class PublicChatControllerTest {

	private static final UUID 		ID 			= UUID.randomUUID();
	private static final UUID 		PARENT_ID 	= UUID.randomUUID();
	private static final String 	MESSAGE 	= "aMessage";
	
	@Mock
	private PublicChatService 		mockService;
	
	@Mock
	private ParticipantService 		mockParticipantService;
	
	@Mock
	private Principal 				mockPrincipal;
	
	@Mock
	private Pageable				mockPageable;
	
	@InjectMocks
	private PublicChatController 	controller;
	
	/**
	* Tests Creation of new Chat 
	*/
	@Test
	void testCreateChat() {
		
		when(mockService.createChat(PARENT_ID, MESSAGE, mockPrincipal)).thenReturn(ID);
		
		ResponseEntity<UUID> response = this.controller
				.createChat(PublicChatAPIInbound
						.builder()
							.parentChat(PARENT_ID)
							.message(MESSAGE)
						.build(), mockPrincipal);
		
		assertEquals(ID, response.getBody());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
	}
	
	/**
	* Tests update of an existing Chat 
	*/
	@Test
	void testUpdateChat() {
		
		doNothing().when(mockService).updateChat(ID, MESSAGE, mockPrincipal);
		
		ResponseEntity<Void> response = this.controller.updateChat(PublicChatUpdateAPIInbound.builder().message(MESSAGE).build(), ID, mockPrincipal);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	/**
	* Tests Deletion of Chat 
	*/
	@Test 
	void testDeleteChat() {
		
		doNothing().when(mockService).deleteChat(ID, mockPrincipal);
		
		ResponseEntity<Void> response = this.controller.deleteChat(ID, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	
	}
	
	/**
	* Test fetch of single Chat
	*/
	@Test 
	void testFetchChat() {
		
		when(this.mockService.fetchChat(ID)).thenReturn(PublicChat.builder().id(ID).ownerId("r1").build());
		when(this.mockParticipantService.fetchById(anyString())).thenReturn(Optional.of(ChatParticipant.builder().build()));
		
		ResponseEntity<PublicChatAPIOutbound> response = this.controller.fetchChat(ID);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ID, response.getBody().getId());
		
	}
	
	/**
	* Test fetch of single Chat where the owner cant be found
	*/
	@Test 
	void testFetchChatOwnerMissing() {
		
		when(this.mockService.fetchChat(ID)).thenReturn(PublicChat.builder().id(ID).ownerId("r1").build());
		when(this.mockParticipantService.fetchById(anyString())).thenReturn(Optional.empty());
		
		ResponseEntity<PublicChatAPIOutbound> response = this.controller.fetchChat(ID);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ID, response.getBody().getId());
		assertEquals("NA", response.getBody().getOwner().getId());
		
	}
	
	/**
	* Test fetch of single Chats Children
	*/
	@Test 
	void testFetchChatChildren() {
		
		when(this.mockService.fetchChatChildren(ID)).thenReturn(Set.of(PublicChat.builder().id(ID).ownerId("r1").build(),PublicChat.builder().id(ID).ownerId("r2").build()));
		when(this.mockParticipantService.fetchById(anyString())).thenReturn(Optional.of(ChatParticipant.builder().build()));
		
		ResponseEntity<Set<PublicChatAPIOutbound>> response = this.controller.fetchChatChildren(ID);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
		
	}
	
	/**
	* Test fetch of single Chats Children where the Owner is missing 
	*/
	@Test 
	void testFetchChatChildrenMissingOwner() {
		
		when(this.mockService.fetchChatChildren(ID)).thenReturn(Set.of(PublicChat.builder().id(ID).ownerId("r1").build(),PublicChat.builder().id(ID).ownerId("r2").build()));
		when(this.mockParticipantService.fetchById(anyString())).thenReturn(Optional.empty());
		
		ResponseEntity<Set<PublicChatAPIOutbound>> response = this.controller.fetchChatChildren(ID);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
		assertEquals("NA", response.getBody().stream().findAny().get().getOwner().getId());
		
	}
	
	/**
	* Test fetch of single Chats Children
	*/
	@Test 
	void testFetchTopLevelChats() {
		
		final int page = 1;
		final int maxResults = 59;
		
		when(this.mockPageable.getPageNumber()).thenReturn(page);
		when(this.mockPageable.getPageSize()).thenReturn(maxResults);
		when(this.mockParticipantService.fetchById(anyString())).thenReturn(Optional.of(ChatParticipant.builder().build()));
		
		when(this.mockService.fetchTopLevelChats(page, maxResults)).thenReturn(Set.of(PublicChat.builder().id(ID).ownerId("r1").build(),PublicChat.builder().id(ID).ownerId("r2").build()));
		
		ResponseEntity<Set<PublicChatAPIOutbound>> response = this.controller.fetchTopLevelChats(this.mockPageable);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
		
	}
	
	/**
	* Test fetch of single Chats Children
	*/
	@Test 
	void testFetchTopLevelChatsOwnerMissing() {
		
		final int page = 1;
		final int maxResults = 59;
		
		when(this.mockPageable.getPageNumber()).thenReturn(page);
		when(this.mockPageable.getPageSize()).thenReturn(maxResults);
		when(this.mockParticipantService.fetchById(anyString())).thenReturn(Optional.empty());
		
		when(this.mockService.fetchTopLevelChats(page, maxResults)).thenReturn(Set.of(PublicChat.builder().id(ID).ownerId("r1").build(),PublicChat.builder().id(ID).ownerId("r2").build()));
		
		ResponseEntity<Set<PublicChatAPIOutbound>> response = this.controller.fetchTopLevelChats(this.mockPageable);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
		assertEquals("NA", response.getBody().stream().findAny().get().getOwner().getId());
	}
	
	/**
	* Test fetch of single Chats Children
	*/
	@Test 
	void testToggleLikeForChat() {
		
		when(this.mockPrincipal.getName()).thenReturn("rec1");
		when(this.mockService.toggleLikeForChat(ID, "rec1")).thenReturn(PublicChat.builder().build());
		
		ResponseEntity<PublicChatAPIOutbound> response = this.controller.toggleLikeForChat(ID, mockPrincipal);
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Test endpoint to see if there have been new newsfeed posts since the 
	* authenticated User last viewed the newsfeed 
	*/
	@Test 
	void testIsUnreadNewsFeedItems() {
	
		when(this.mockPrincipal.getName()).thenReturn("rec1");
		
		ResponseEntity<Boolean> response = this.controller.isUnreadNewsFeedItems(mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Test endpoint to see if there have been new newsfeed posts since the 
	* authenticated User last viewed the newsfeed 
	*/
	@Test 
	void testNewsFeedViewed() {
	
		when(this.mockPrincipal.getName()).thenReturn("rec1");
		
		ResponseEntity<Void> response = this.controller.newsFeedViewed(mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
}