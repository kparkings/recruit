package com.arenella.recruit.messaging.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.messaging.beans.PublicChatNotification;
import com.arenella.recruit.messaging.services.ParticipantService;
import com.arenella.recruit.messaging.services.PublicChatNotificationService;

/**
* Unit tests for the PublicChatNotificationController class 
*/
@ExtendWith(MockitoExtension.class)
class PublicChatNotificationControllerTest {

	@Mock
	private PublicChatNotificationService 	mockNotificationService;
	
	@Mock
	private ParticipantService 				mockParticipantService;
	
	@Mock
	private Principal 						mockPrincipal;
	
	@InjectMocks
	private PublicChatNotificationController controller;
	
	/**
	* Tests endpoint for retrieving Notifications 
	*/
	@Test
	void testFetchNotificationsForUser() {
		
		final String username = "authenticatedUser";
		
		when(this.mockPrincipal.getName()).thenReturn(username);
		when(this.mockNotificationService.fetchNotificationsForUser(username)).thenReturn(Set.of(PublicChatNotification.builder().build(), PublicChatNotification.builder().build()));
		
		ResponseEntity<Set<PublicChatNotificationAPIOutbound>> response = this.controller.fetchNotificationsForUser(mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
		
	}
	
	/**
	* Tests endpoint for updating Notifications viewed status
	*/
	@Test
	void testSetViewedStatus() {
		
		final UUID 		notificationID 	= UUID.randomUUID();
		final boolean 	viewed 			= true;
		final String 	username 		= "authenticatedUser";
		
		when(this.mockPrincipal.getName()).thenReturn(username); 
		
		ResponseEntity<Void> response = this.controller.setViewedStatus(notificationID, viewed, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockNotificationService).setNotificationViewedStatus(notificationID, viewed, username);
		
	}
	
	/**
	* Tests endpoint for deleting an existing notification 
	*/
	@Test 
	void testDeleteNotification() {
		
		final UUID 		notificationID 	= UUID.randomUUID();
		final String 	username 		= "authenticatedUser";
		
		when(this.mockPrincipal.getName()).thenReturn(username); 
		
		ResponseEntity<Void> response = this.controller.deleteNotification(notificationID, mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		verify(this.mockNotificationService).deleteNotification(notificationID, username);
		
	}
	
}
