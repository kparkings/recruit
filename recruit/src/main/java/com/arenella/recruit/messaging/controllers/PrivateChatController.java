package com.arenella.recruit.messaging.controllers;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

import com.arenella.recruit.messaging.services.PrivateChatService;

/**
* REST API for PrivateChat's 
*/
@RestController
public class PrivateChatController {

	private PrivateChatService privateChatService;
	
	/**
	* Constructor
	* @param privateChatService - Provides services related to private chat's
	*/
	public PrivateChatController(PrivateChatService privateChatService) {
		this.privateChatService = privateChatService;
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
		return new ResponseEntity<>(this.privateChatService.saveChat(PrivateChatAPIInbound.toDomain(chat), principal.getName()), HttpStatus.CREATED);
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
		return ResponseEntity.ok(PrivateChatAPIOutbound.fromDomain(this.privateChatService.getChat(chatId, principal.getName())));
	}
	
	/**
	* Retrieves all Chats where the the authenticated User is either the Sender or Receiver
	* @param principal - Authenticated User
	* @return Chats
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@GetMapping(path="privatechat", produces="application/json")
	public ResponseEntity<Set<PrivateChatAPIOutbound>> fetchChatsByUserId(Principal principal) {
		return ResponseEntity.ok(this.privateChatService.getUsersChats(principal.getName()).stream().map(PrivateChatAPIOutbound::fromDomain).collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	/**
	* Sets whether the User has blocked the Chat
	* @return Status Code
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@PutMapping(path="privatechat/{chatId}/block/{blocked}", produces="application/json")
	public ResponseEntity<Void> doSetBlockedStatus(@PathVariable("chatId") UUID chatId, @PathVariable("blocked") boolean blocked, Principal principal) {
		this.privateChatService.setBlockedStatus(chatId, principal.getName(), blocked);
		return ResponseEntity.ok().build();
	}
	
	/**
	* Sets whether the User has blocked the Chat
	* @return Status Code
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@PutMapping(path="privatechat/{chatId}/viewed", produces="application/json")
	public ResponseEntity<Void> doSetLastViewed(@PathVariable("chatId") UUID chatId, Principal principal) {
		this.privateChatService.setLastViewed(chatId, principal.getName());
		return ResponseEntity.ok().build();
	}
	
	/**
	* Sets whether the User has blocked the Chat
	* @return Status Code
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@PutMapping(path="privatechat/{chatId}/keypressed", produces="application/json")
	public ResponseEntity<Void> doSetKeyPressed(@PathVariable("chatId") UUID chatId, Principal principal) {
		this.privateChatService.setLastKeyPress(chatId, principal.getName());
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
		this.privateChatService.addMessage(chatId, message.getMessage(), principal.getName());
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
		this.privateChatService.deleteMessage(chatId, messageId, principal.getName());
		return ResponseEntity.ok().build();
	}
	
}