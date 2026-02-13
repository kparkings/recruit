package com.arenella.recruit.messaging.controllers;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.messaging.services.ParticipantService;
import com.arenella.recruit.messaging.services.PublicChatNotificationService;

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
	
	/**
	* Sets whether a given Notification has been viewed by the destination User
	* @param notificationId		- Unique id of Notification to set viewed status for
	* @param viewedStatus		- new status
	* @param authenticatedUser  - Authenticated User
	* @return ResponseEntity
	*/
	@PutMapping(path="publicchatnotification/{notificationId}/{viewedStatus}", produces="application/json")
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	public ResponseEntity<Void> setViewedStatus(@PathVariable("notificationId") UUID notificationId, @PathVariable("viewedStatus") boolean viewedStatus, Principal authenticatedUser){
		this.notificationService.setNotificationViewedStatus(notificationId, viewedStatus, authenticatedUser.getName());
		return ResponseEntity.ok().build();
	}
	
	/**
	* Deletes the notification providing the User has permissions
	* @param notificationId		- Id of Notification to delete
	* @param authenticatedUser	- Authenticated User
	* @return
	*/
	@DeleteMapping(path="publicchatnotification/{notificationId}", produces="application/json")
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	public ResponseEntity<Void> deleteNotification(@PathVariable("notificationId") UUID notificationId, Principal authenticatedUser){
		this.notificationService.deleteNotification(notificationId, authenticatedUser.getName());
		return ResponseEntity.ok().build();
	}
}