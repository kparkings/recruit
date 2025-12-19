package com.arenella.recruit.messaging.services;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.arenella.recruit.authentication.spring.filters.ClaimsUsernamePasswordAuthenticationToken;
import com.arenella.recruit.messaging.beans.ChatMessage;
import com.arenella.recruit.messaging.beans.PrivateChat;
import com.arenella.recruit.messaging.beans.PrivateChat.PrivateChatBuilder;
import com.arenella.recruit.messaging.dao.PrivateChatDao;

/**
* Services for PrivateChat's
*/
@Service
public class PrivateChatServiceImpl implements PrivateChatService{

	//TODO: [KP] Need a scheduled task to look for messages sent before x minutes after the user last logged in
	
	public static final String ERR_MSG_CANNOT_ADD_MESSAGE = "Cannot add message";
	public static final String ERR_MSG_CANNOT_DEL_MESSAGE = "Cannot delete message";
	
	private PrivateChatDao privateChatDao;

	/**
	* Constructor
	* @param privateChatDao - DAO for persisting/retrieving Chat data
	*/
	public PrivateChatServiceImpl(PrivateChatDao privateChatDao) {
		this.privateChatDao = privateChatDao;
	}
	
	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public Set<PrivateChat> getUsersChats(Principal user) {
		
		this.canChat(user, true);
		
		return this.privateChatDao.fetchUserChats(user.getName());
	}

	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public PrivateChat getChat(UUID chatId, Principal user) {
		
		this.canChat(user, true);
		
		Optional<PrivateChat> chatOpt  = this.privateChatDao.fetchChatById(chatId);
		
		if (chatOpt.isEmpty()) {
			throw new RuntimeException(PrivateChatService.ERR_MSG_UNKNOWN_CHAT);
		}
		
		PrivateChat chat = chatOpt.orElseThrow();
		
		if (!user.getName().equals(chat.getSenderId()) && !user.getName().equals(chat.getRecipientId())) {
			throw new RuntimeException(PrivateChatService.ERR_MSG_UNAUTHORISED_CHAT);
		}
		
		return chat;
		
	}

	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public UUID saveChat(PrivateChat chat, Principal user) {
		
		this.canChat(user, true);
		
		this.saveInitialChat(chat, user.getName());
		return chat.getId();
	}
	
	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public void setBlockedStatus(UUID chatId, Principal user, boolean blocked) {
		
		this.canChat(user, true);
		
		PrivateChat 		chat 		= this.fetchAndValidateChatForUpdate(chatId, user.getName());
		PrivateChatBuilder 	updatedChat = chat.cloneToBuilder();
		
		if (user.getName().equals(chat.getSenderId())) {
			updatedChat.blockedBySender(blocked);
		}
		
		if (user.getName().equals(chat.getRecipientId())) {
			updatedChat.blockedByRecipient(blocked);
		}
		
		this.privateChatDao.saveChat(updatedChat.build());
		
	}

	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public void setLastViewed(UUID chatId, Principal user) {
		
		this.canChat(user, true);
		
		PrivateChat 		chat 		= this.fetchAndValidateChatForUpdate(chatId, user.getName());
		PrivateChatBuilder 	updatedChat = chat.cloneToBuilder();
		
		if (user.getName().equals(chat.getSenderId())) {
			updatedChat.lastViewedBySender(LocalDateTime.now());
		}
		
		if (user.getName().equals(chat.getRecipientId())) {
			updatedChat.lastViewedByRecipient(LocalDateTime.now());
		}
	
		this.privateChatDao.saveChat(updatedChat.build());
		
	}
	
	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public void setLastKeyPress(UUID chatId, Principal user) {
		
		this.canChat(user, true);
		
		PrivateChat 		chat 		= this.fetchAndValidateChatForUpdate(chatId, user.getName());
		PrivateChatBuilder 	updatedChat = chat.cloneToBuilder();
		
		if (user.getName().equals(chat.getSenderId())) {
			updatedChat.lastKeyPressSender(LocalDateTime.now());
		}
		
		if (user.getName().equals(chat.getRecipientId())) {
			updatedChat.lastKeyPressRecipient(LocalDateTime.now());
		}
		
		this.privateChatDao.saveChat(updatedChat.build());
	
	}
	
	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public void deleteUserChats(Principal user) {
		
		this.canChat(user, true);
		
		this.privateChatDao.fetchUserChats(user.getName()).forEach(c -> privateChatDao.deleteById(c.getId()));
	}
	
	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public void addMessage(UUID chatId, String message, Principal user) {
		
		this.canChat(user, true);
		
		PrivateChat chat = privateChatDao.fetchChatById(chatId).orElseThrow(() -> new RuntimeException(ERR_MSG_CANNOT_ADD_MESSAGE));
		
		this.doCheckIsBlocked(chat);
		
		if (!user.getName().equals(chat.getSenderId()) && !user.getName().equals(chat.getRecipientId())) {
			throw new RuntimeException(ERR_MSG_CANNOT_ADD_MESSAGE);
		}
		
		String recipientId = chat.getSenderId().equals(user.getName()) ? chat.getRecipientId() : chat.getSenderId();
		
		chat.addReply(ChatMessage
				.builder()
					.id(UUID.randomUUID())
					.chatId(chatId)
					.created(LocalDateTime.now())
					.message(message)
					.senderId(user.getName())
					.recipientId(recipientId)
				.build());
		
		this.privateChatDao.saveChat(chat);
	}

	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public void deleteMessage(UUID chatId, UUID messageId, Principal user) {
		
		this.canChat(user, true);
		
		PrivateChat chat = privateChatDao.fetchChatById(chatId).orElseThrow(() -> new RuntimeException(ERR_MSG_CANNOT_DEL_MESSAGE));
		
		this.doCheckIsBlocked(chat);
		
		if (!chat.getReplies().keySet().contains(messageId)) {
			throw new RuntimeException(ERR_MSG_CANNOT_DEL_MESSAGE);
		}
		
		ChatMessage message = chat.getReplies().get(messageId);
		
		if (!user.getName().equals(message.getSenderId())) {
			throw new RuntimeException(ERR_MSG_CANNOT_DEL_MESSAGE);
		}
		
		chat.deleteReply(messageId);
		
		this.privateChatDao.saveChat(chat);
	
	}
	
	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public boolean canChat(Principal principal, boolean throwIfCannotChat) {
		
		ClaimsUsernamePasswordAuthenticationToken 	user 			= (ClaimsUsernamePasswordAuthenticationToken)principal;
		boolean 									isRecruiter 	= user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_RECRUITER"));
		boolean 									useCredits 		= (Boolean)user.getClaim("useCredits").get();
	
		if (isRecruiter && useCredits && throwIfCannotChat) {
			throw new RuntimeException("Nope");
		} else if (isRecruiter && useCredits) {
			return false;
		}
		
		return true;
		
	}
	
	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public Set<PrivateChat> getUnblockedChatsBeforeCuttoff(LocalDateTime cuttoff) {
		return this.privateChatDao.getUnblockedChatsBeforeCuttoff(cuttoff);
	}
	
	/**
	* Performs common validations for all Chat updates
	* @param chatId - Id of Chat 
	* @param userId - Id of User
	*/
	private PrivateChat fetchAndValidateChatForUpdate(UUID chatId, String userId) {
		
		Optional<PrivateChat> chat = this.privateChatDao.fetchChatById(chatId);
		
		if (chat.isEmpty()) {
			throw new RuntimeException("You cannot Update an unknown Chat.");
		}
		
		return chat.orElseThrow();
		
	}
	
	/**
	* Performs validation and Saves an initial chat
	* Validations:
	*  - The User must be the Sender on a new Chat
	*  - Only 1 chat can be between a Sender and Recipient. Regardless of who is Sender and who is Recipient
	*  - Only basic fields can be populated ( No validation needed. Method will prevent other fields being populated)
	* @param chat
	* @param userId
	*/
	private void saveInitialChat(PrivateChat chat, String userId) {
		
		if (this.privateChatDao.existsById(chat.getId())){
			throw new RuntimeException(ERR_MSG_CANNOT_CREATE_CHAT);
		}
		
		if (!userId.equals(chat.getSenderId())){
			throw new RuntimeException(ERR_MSG_CANNOT_CREATE_CHAT_FOR_ANOTHER_USER);
		}
		
		if (chat.getSenderId().equals(chat.getRecipientId())){
			throw new RuntimeException(ERR_MSG_CANNOT_CREATE_CHAT_WITH_YOURSELF);
		}
		
		this.privateChatDao.fetchUserChats(userId)
			.stream()
			.filter(c -> c.getSenderId().equals(chat.getSenderId()) && c.getRecipientId().equals(chat.getRecipientId()))
			.findAny()
			.ifPresent(m -> {throw new RuntimeException("Invalid attempt to create chat for User combination. Combination already exists.");});
			
		this.privateChatDao.fetchUserChats(userId)
			.stream()
			.filter(c -> c.getSenderId().equals(chat.getRecipientId()) && c.getRecipientId().equals(chat.getSenderId()))
			.findAny()
			.ifPresent(m -> {throw new RuntimeException("Invalid attempt to create chat for User combination. Combination already exists.");});
		
		LocalDateTime currentTime = LocalDateTime.now();
		
		PrivateChat newChat = PrivateChat
				.builder()
					.id(chat.getId())
					.senderId(chat.getSenderId())
					.recipientId(chat.getRecipientId())
					.created(currentTime)
					.lastUpdate(currentTime)
					.lastViewedBySender(currentTime)
					.lastViewedByRecipient(currentTime)
				.build();
		
		this.privateChatDao.saveChat(newChat);
		
	}
	
	/**
	* Throws Exception if an attempt is made to perform an action 
	* that is not permitted when a Chat is blocked
	*/
	private void doCheckIsBlocked(PrivateChat chat) {
		if (chat.isBlockedByRecipient() || chat.isBlockedBySender()) {
			throw new IllegalStateException("Blocked");
		}
	}
		
}