package com.arenella.recruit.messaging.controllers;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
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

import com.arenella.recruit.messaging.services.PublicChatService;

/**
* REST API For PublicChat's 
*/
@RestController
public class PublicChatController {

	private PublicChatService publicChatService;
	
	/**
	* Constructor
	* @param publicChatService - Services for working with PublicChat's
	*/
	public PublicChatController(PublicChatService publicChatService) {
		this.publicChatService = publicChatService;
	}
	
	/**
	* Creates a Chat
	* @param chat - Chat to persist
	* @param user - Authenticated User
	* @return ID of Chat
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@PostMapping(path="publicchat", produces="application/json")
	public ResponseEntity<UUID> createChat(@RequestBody PublicChatAPIInbound chat, Principal user) {
		return new ResponseEntity<>(this.publicChatService.createChat(chat.getParentChat().orElse(null), chat.getMessage(), user), HttpStatus.CREATED);
	}
	
	/**
	* Updates a Chat
	* @param chat - Chat to persist
	* @param user - Authenticated User
	* @return ID of Chat
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@PutMapping(path="publicchat/{chatId}", produces="application/json")
	public ResponseEntity<Void> updateChat(@RequestBody PublicChatUpdateAPIInbound chat, @PathVariable("chatId") UUID chatId, Principal user) {
		this.publicChatService.updateChat(chatId, chat.getMessage(), user);
		return ResponseEntity.ok().build();
	} 
	
	/**
	* Deletes a Chat and all of its Children
	* @param chatId - Id of Chat to delete
	* @param user 	- Authenticated User
	* @return Void
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@DeleteMapping(path="publicchat/{chatId}", produces="application/json")
	public ResponseEntity<Void> deleteChat(@PathVariable UUID chatId, Principal user) {
		this.publicChatService.deleteChat(chatId, user);
		return ResponseEntity.ok().build();
	}
	
	/**
	* Fetches a specific Chat
	* @param chatId - Id of Chat to return 
	* @return Chat
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@GetMapping(path="publicchat/{chatId}", produces="application/json")
	public ResponseEntity<PublicChatAPIOutbound> fetchChat(@PathVariable UUID chatId) {
		
		PublicChatAPIOutbound outbound = PublicChatAPIOutbound
			.builder()
				.publicChat(this.publicChatService.fetchChat(chatId))
				.build();
		
		return ResponseEntity.ok(outbound);
	}
	
	/**
	* Fetches the Child Chats ( replies ) to a given CHat
	* @param chatId - Id of Chat to return Children for
	* @return Child Chats
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@PostMapping(path="publicchat/{chatId}/children", produces="application/json")
	public ResponseEntity<Set<PublicChatAPIOutbound>> fetchChatChildren(@PathVariable UUID chatId) {
		
		Set<PublicChatAPIOutbound> outbound = this.publicChatService.fetchChatChildren(chatId)
				.stream()
				.map(chat -> PublicChatAPIOutbound.builder().publicChat(chat).build()).collect(Collectors.toCollection(LinkedHashSet::new));
		
		return ResponseEntity.ok(outbound);
	}
	
	/**
	* Returns top level Chats. There are the Chats that are not replies to other
	* Chats
	* @param pageable - Defines which page or results to return
	* @return Page of top level Chat's
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@PostMapping(path="publicchat/toplevel", produces="application/json")
	public ResponseEntity<Set<PublicChatAPIOutbound>>fetchTopLevelChats(Pageable pageable) {
		
		Set<PublicChatAPIOutbound> outbound = this.publicChatService.fetchTopLevelChats(pageable.getPageNumber(), pageable.getPageSize())
				.stream()
				.map(chat -> PublicChatAPIOutbound.builder().publicChat(chat).build()).collect(Collectors.toCollection(LinkedHashSet::new));
		
		return ResponseEntity.ok(outbound);
	}
	
	/**
	* Toggles the authenticated Users like/unlike for a given PublicChat
	* @param chatId		- Id of Chat to like / unlike
	* @param principal	- Currently authenticated User
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@PutMapping(path="publicchat/like", produces="application/json")
	public ResponseEntity<Void> toggleLikeForChat(UUID chatId, Principal principal) {
		this.publicChatService.toggleLikeForChat(chatId, principal.getName());
		return ResponseEntity.ok().build();
	}
	
}