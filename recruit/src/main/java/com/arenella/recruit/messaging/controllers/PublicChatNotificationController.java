package com.arenella.recruit.messaging.controllers;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.messaging.services.ParticipantService;
import com.arenella.recruit.messaging.services.PublicChatNotificationService;
import com.arenella.recruit.messaging.services.PublicChatService;

/**
* REST API for PublicChatNotifications 
*/
@RestController
public class PublicChatNotificationController {

	private PublicChatNotificationService notificationService;
	private ParticipantService 	participantService;
	
	/**
	* Constructor 
	* @param notificationService 	- Services for PublicChat Notifications
	* @param participantService   	- Services related to PublicChat Participant's
	*/
	public PublicChatNotificationController(PublicChatNotificationService notificationService, ParticipantService participantService) {
		this.notificationService 	= notificationService;
		this.participantService 	= participantService;
	}
	
	/**
	* Retrieves the notifications for the authenticated User
	* @param authenticatedUser - Authenticated User
	* @return Notification for Use
	*/
	@GetMapping(path="publicchatnotification", produces="application/json")
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	public ResponseEntity<Set<PublicChatNotificationAPIOutbound>> fetchNotificationsForUser(Principal authenticatedUser){
		return ResponseEntity.ok(this.notificationService.fetchNotificationsForUser(authenticatedUser.getName())
				.stream()
				.map(notification -> PublicChatNotificationAPIOutbound
						.builder()
							.publicChatNotification(notification,participantService.fetchById(notification.getInitiatingUserId()).map(cp -> ChatParticipantAPIOutbound.builder().chatParticipant(cp).build()).orElse(null))
						.build())
				.collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
}
