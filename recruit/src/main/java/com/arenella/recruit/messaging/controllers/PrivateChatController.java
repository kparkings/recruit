package com.arenella.recruit.messaging.controllers;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.PrivateChat;
import com.arenella.recruit.messaging.services.ParticipantService;
import com.arenella.recruit.messaging.services.PrivateChatService;

/**
* REST API for PrivateChat's 
*/
@RestController
public class PrivateChatController {

	private PrivateChatService privateChatService;
	
	private ParticipantService participantService;
	
	/**
	* Constructor
	* @param privateChatService - Provides services related to private chat's
	* @param ParticipantService - Services for participants of Chat's
	*/
	public PrivateChatController(PrivateChatService privateChatService, ParticipantService participantService) {
		this.privateChatService = privateChatService;
		this.participantService = participantService;
	}
	
	/**
	* Creates a new Chat between a Sender and a Receiver
	* @param chat 		- Details of new Chat
	* @param principal 	- Authenticated User
	* @return ID of new Chat
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER')")
	@PostMapping(path="privatechat", produces="application/json")
	public ResponseEntity<UUID> createPrivateChat(@RequestBody PrivateChatAPIInbound chat, Principal principal) {
		return new ResponseEntity<>(this.privateChatService.saveChat(PrivateChatAPIInbound.toDomain(chat), principal), HttpStatus.CREATED);
	}
	
	/**
	* Retrieves an individual chat based upon its ID
	* @param id			- Id of the Chat to return
	* @param principal 	- Authenticated User
	* @return Requested Chat
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@GetMapping(path="privatechat/{chatId}", produces="application/json")
	public ResponseEntity<PrivateChatAPIOutbound> fetchPrivateChatById(@PathVariable("chatId") UUID chatId, Principal principal) {
		
		PrivateChat 		chat		= this.privateChatService.getChat(chatId, principal);
		ChatParticipant 	sender 		= this.participantService.fetchById(chat.getSenderId()).orElseThrow(()    -> new IllegalStateException());
		ChatParticipant 	recipient 	= this.participantService.fetchById(chat.getRecipientId()).orElseThrow(() -> new IllegalStateException());
		
		return ResponseEntity.ok(PrivateChatAPIOutbound.fromDomain(chat, sender, recipient));
		
	}
	
	/**
	* Retrieves all Chats where the the authenticated User is either the Sender or Receiver
	* @param principal - Authenticated User
	* @return Chats
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@GetMapping(path="privatechat", produces="application/json")
	public ResponseEntity<Set<PrivateChatAPIOutbound>> fetchChatsByUserId(Principal principal) {
		
		Set<PrivateChatAPIOutbound> chats = new LinkedHashSet<>();
		
		this.privateChatService.getUsersChats(principal).stream().forEach(chat -> {
			this.participantService.fetchById(chat.getSenderId()).ifPresent(sender -> {
				this.participantService.fetchById(chat.getRecipientId()).ifPresent(recipient -> {
					chats.add(PrivateChatAPIOutbound.fromDomain(chat, sender, recipient));
				});
			});
		});
		
		return ResponseEntity.ok(chats);
	}
	
	/**
	* Sets whether the User has blocked the Chat
	* @return Status Code
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@PutMapping(path="privatechat/{chatId}/block/{blocked}", produces="application/json")
	public ResponseEntity<Void> doSetBlockedStatus(@PathVariable("chatId") UUID chatId, @PathVariable("blocked") boolean blocked, Principal principal) {
		this.privateChatService.setBlockedStatus(chatId, principal, blocked);
		return ResponseEntity.ok().build();
	}
	
	/**
	* Sets whether the User has blocked the Chat
	* @return Status Code
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@PutMapping(path="privatechat/{chatId}/viewed", produces="application/json")
	public ResponseEntity<Void> doSetLastViewed(@PathVariable("chatId") UUID chatId, Principal principal) {
		this.privateChatService.setLastViewed(chatId, principal);
		return ResponseEntity.ok().build();
	}
	
	/**
	* Sets whether the User has blocked the Chat
	* @return Status Code
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@PutMapping(path="privatechat/{chatId}/keypressed", produces="application/json")
	public ResponseEntity<Void> doSetKeyPressed(@PathVariable("chatId") UUID chatId, Principal principal) {
		this.privateChatService.setLastKeyPress(chatId, principal);
		return ResponseEntity.ok().build();
	}
	
	/**
	* Adds a new Message to the chat
	* @param chatId 	- Id of Chat to add Message to
	* @param principal  - Authenticated User
	* @return
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@PutMapping(path="privatechat/{chatId}/message", produces="application/json")
	public ResponseEntity<Void> doAddMessageToChat(@PathVariable("chatId") UUID chatId, @RequestBody ChatMessageAPIInbound message, Principal principal) {
		this.privateChatService.addMessage(chatId, message.getMessage(), principal);
		return ResponseEntity.ok().build();
	}
	
	/**
	* Marks the Message as deleted
	* @param chatId 	- Id of Chat to delete Message from
	* @param messageId	- Id of Message to delete
	* @param principal  - Authenticated User
	* @return Status
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@DeleteMapping(path="privatechat/{chatId}/message/{messageId}", produces="application/json")
	public ResponseEntity<Void> doDeleteMessageFromChat(@PathVariable("chatId") UUID chatId, @PathVariable("messageId") UUID messageId, Principal principal) {
		this.privateChatService.deleteMessage(chatId, messageId, principal);
		return ResponseEntity.ok().build();
	}
	
	//TODO: [KP] 1. Add endpoint for canChat so we can display message to user in FE if they have no access
	//TODO: [KP] 2. use this function to throw exception if User attempts to access chat directly via API
	
	/**
	* Determines if User has access to the Chat's
	* @param principal - currently logged in user
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@GetMapping(path="privatechat/hasAccess", produces="application/json")
	public ResponseEntity<Boolean> hasChatAccess(Principal principal) {
		return ResponseEntity.ok(this.privateChatService.canChat(principal, false));
	}
	
	
}