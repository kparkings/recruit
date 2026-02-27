package com.arenella.recruit.messaging.controllers;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.SequencedSet;
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

import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.ChatParticipant.CHAT_PARTICIPANT_TYPE;
import com.arenella.recruit.messaging.beans.PublicChat;
import com.arenella.recruit.messaging.services.ParticipantService;
import com.arenella.recruit.messaging.services.PublicChatService;

/**
* REST API For PublicChat's 
*/
@RestController
public class PublicChatController {

	public static final ChatParticipant MISSING_PARTICIPANT = ChatParticipant.builder().firstName("Unknown").surname("Unknown").participantId("NA").type(CHAT_PARTICIPANT_TYPE.SYSTEM).build();
	
	private PublicChatService 	publicChatService;
	private ParticipantService 	participantService;
	
	/**
	* Constructor
	* @param publicChatService - Services for working with PublicChat's
	* @param participantService - Services for working with ChatParticipants
	*/
	public PublicChatController(PublicChatService publicChatService, ParticipantService participantService) {
		this.publicChatService = publicChatService;
		this.participantService = participantService;
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
		
		PublicChat 		chat 	= this.publicChatService.fetchChat(chatId);
		ChatParticipant owner 	= this.participantService.fetchById(chat.getOwnerId()).orElse(MISSING_PARTICIPANT);
		
		PublicChatAPIOutbound outbound = PublicChatAPIOutbound
			.builder()
				.publicChat(chat, owner)
				.build();
		
		return ResponseEntity.ok(outbound);
	}
	
	/**
	* Fetches the Child Chats ( replies ) to a given CHat
	* @param chatId - Id of Chat to return Children for
	* @return Child Chats
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@GetMapping(path="publicchat/{chatId}/children", produces="application/json")
	public ResponseEntity<Set<PublicChatAPIOutbound>> fetchChatChildren(@PathVariable UUID chatId) {
		
		Set<PublicChatAPIOutbound> outbound = this.publicChatService.fetchChatChildren(chatId)
				.stream()
				.map(chat -> PublicChatAPIOutbound.builder().publicChat(chat, this.participantService.fetchById(chat.getOwnerId()).orElse(MISSING_PARTICIPANT)).build()).collect(Collectors.toCollection(LinkedHashSet::new));
		
		return ResponseEntity.ok(outbound);
	}
	
	/**
	* Returns top level Chats. There are the Chats that are not replies to other
	* Chats
	* @param pageable - Defines which page or results to return
	* @return Page of top level Chat's
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@GetMapping(path="publicchat/toplevel", produces="application/json")
	public ResponseEntity<Set<PublicChatAPIOutbound>>fetchTopLevelChats(Pageable pageable) {
		
		Set<PublicChatAPIOutbound> outbound = this.publicChatService.fetchTopLevelChats(pageable.getPageNumber(), pageable.getPageSize())
				.stream()
				.map(chat -> PublicChatAPIOutbound.builder().publicChat(chat, this.participantService.fetchById(chat.getOwnerId()).orElse(MISSING_PARTICIPANT)).build()).collect(Collectors.toCollection(LinkedHashSet::new));
		
		return ResponseEntity.ok(outbound);
	}
	
	/**
	* Toggles the authenticated Users like/unlike for a given PublicChat
	* @param chatId		- Id of Chat to like / unlike
	* @param principal	- Currently authenticated User
	* @return ResponseEntity
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@PutMapping(path="publicchat/{chatId}/like", produces="application/json")
	public ResponseEntity<PublicChatAPIOutbound> toggleLikeForChat(@PathVariable("chatId") UUID chatId, Principal principal) {
	
		PublicChat 		chat 	= this.publicChatService.toggleLikeForChat(chatId, principal.getName());
		ChatParticipant owner 	= this.participantService.fetchById(chat.getOwnerId()).orElse(MISSING_PARTICIPANT);
		
		PublicChatAPIOutbound outbound = PublicChatAPIOutbound
			.builder()
				.publicChat(chat, owner)
				.build();
		
		return ResponseEntity.ok(outbound);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@GetMapping(path="publicchat/{chatId}/likes", produces="application/json")
	public ResponseEntity<Set<ChatParticipantAPIOutbound>> fetchLIikes(@PathVariable("chatId")UUID chatId) {
		
		PublicChat 						chat 				= this.publicChatService.fetchChat(chatId);
		Set<ChatParticipantAPIOutbound> likeParticipants 	= new LinkedHashSet<>();
		
		chat.getLikes().stream().forEach(participant -> {
			likeParticipants.add(ChatParticipantAPIOutbound.builder().chatParticipant(this.participantService.fetchById(participant).orElse(MISSING_PARTICIPANT)).build());
		});
		
		return ResponseEntity.ok(likeParticipants);
	}
	
	/**
	* Takes a Chat and returns all direct parents up until and including the top level Chat.
	* Effectively this is to find the top level chat of a notification.
	* @param chatId - Id of Chat to return top level Chat for
	* @return Chain of Chats from given Chat up to its top level Chat
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@GetMapping(path="publicchat/{chatId}/path", produces="application/json")
	public ResponseEntity<SequencedSet<UUID>> fetchPathToChat(@PathVariable("chatId") UUID chatId) {
		
		SequencedSet<UUID> path = this.publicChatService.fetchPathToTopLevelChat(chatId);
		
		
		return ResponseEntity.ok(path);
		
	}

	/**
	* Returns if there are posts on the newsfeed that the Authenticated user has 
	* not yet seen. These will be messages added since they last viewed the newsfeed
	* @param principal - Authenticated user
	* @return if there are unread posts
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@GetMapping(path="publicchat/hasUnreadNewsfeedItems", produces="application/json")
	public ResponseEntity<Boolean> isUnreadNewsFeedItems(Principal principal) {
		return ResponseEntity.ok(this.publicChatService.isUnreadNewsFeedItems(principal.getName()));
	}
	
	/**
	* Event that the authenticated user viewed the newsfeed
	* @param principal - Authenticated User
	* @return Void
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('RECRUITER') OR hasRole('CANDIDATE')")
	@PutMapping(path="publicchat/newsfeedViewed", produces="application/json")
	public ResponseEntity<Void> newsFeedViewed(Principal principal) {
		
		this.participantService.markNewsFeedViewd(principal.getName());
		
		return ResponseEntity.ok().build();
		
	}
	
}