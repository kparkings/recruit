package com.arenella.recruit.messaging.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

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
	public Set<PrivateChat> getUsersChats(String userId) {
		return this.privateChatDao.fetchUserChats(userId);
	}

	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public PrivateChat getChat(UUID chatId, String userId) {
		
		Optional<PrivateChat> chatOpt  = this.privateChatDao.fetchChatById(chatId);
		
		if (chatOpt.isEmpty()) {
			throw new RuntimeException(PrivateChatService.ERR_MSG_UNKNOWN_CHAT);
		}
		
		PrivateChat chat = chatOpt.orElseThrow();
		
		if (!userId.equals(chat.getSenderId()) && !userId.equals(chat.getRecipientId())) {
			throw new RuntimeException(PrivateChatService.ERR_MSG_UNAUTHORISED_CHAT);
		}
		
		return chat;
		
	}

	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public UUID saveChat(PrivateChat chat, String userId) {
		this.saveInitialChat(chat, userId);
		return chat.getId();
	}
	
	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public void setBlockedStatus(UUID chatId, String userId, boolean blocked) {
		
		PrivateChat 		chat 		= this.fetchAndValidateChatForUpdate(chatId, userId);
		PrivateChatBuilder 	updatedChat = chat.cloneToBuilder();
		
		if (userId.equals(chat.getSenderId())) {
			updatedChat.blockedBySender(blocked);
		}
		
		if (userId.equals(chat.getRecipientId())) {
			updatedChat.blockedByRecipient(blocked);
		}
		
		this.privateChatDao.saveChat(updatedChat.build());
		
	}

	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public void setLastViewed(UUID chatId, String userId) {
		
		PrivateChat 		chat 		= this.fetchAndValidateChatForUpdate(chatId, userId);
		PrivateChatBuilder 	updatedChat = chat.cloneToBuilder();
		
		if (userId.equals(chat.getSenderId())) {
			updatedChat.lastViewedBySender(LocalDateTime.now());
		}
		
		if (userId.equals(chat.getRecipientId())) {
			updatedChat.lastViewedByRecipient(LocalDateTime.now());
		}
	
		this.privateChatDao.saveChat(updatedChat.build());
		
	}
	
	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public void setLastKeyPress(UUID chatId, String userId) {
		
		PrivateChat 		chat 		= this.fetchAndValidateChatForUpdate(chatId, userId);
		PrivateChatBuilder 	updatedChat = chat.cloneToBuilder();
		
		if (userId.equals(chat.getSenderId())) {
			updatedChat.lastKeyPressSender(LocalDateTime.now());
		}
		
		if (userId.equals(chat.getRecipientId())) {
			updatedChat.lastKeyPressRecipient(LocalDateTime.now());
		}
		
		this.privateChatDao.saveChat(updatedChat.build());
	
	}
	
	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public void deleteUserChats(String userId) {
		this.privateChatDao.fetchUserChats(userId).forEach(c -> privateChatDao.deleteById(c.getId()));
	}
	
	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public void addMessage(UUID chatId, String message, String userId) {
		
		PrivateChat chat = privateChatDao.fetchChatById(chatId).orElseThrow(() -> new RuntimeException(ERR_MSG_CANNOT_ADD_MESSAGE));
		
		if (!userId.equals(chat.getSenderId()) && !userId.equals(chat.getRecipientId())) {
			throw new RuntimeException(ERR_MSG_CANNOT_ADD_MESSAGE);
		}
		
		String recipientId = chat.getSenderId().equals(userId) ? chat.getRecipientId() : chat.getSenderId();
		
		chat.addReply(ChatMessage
				.builder()
					.id(UUID.randomUUID())
					.chatId(chatId)
					.created(LocalDateTime.now())
					.message(message)
					.senderId(userId)
					.recipientId(recipientId)
				.build());
		
		this.privateChatDao.saveChat(chat);
	}

	/**
	* Refer to the PrivateChatService for details 
	*/
	@Override
	public void deleteMessage(UUID chatId, UUID messageId, String userId) {
		
		PrivateChat chat = privateChatDao.fetchChatById(chatId).orElseThrow(() -> new RuntimeException(ERR_MSG_CANNOT_DEL_MESSAGE));
		
		if (!chat.getReplies().keySet().contains(messageId)) {
			throw new RuntimeException(ERR_MSG_CANNOT_DEL_MESSAGE);
		}
		
		ChatMessage message = chat.getReplies().get(messageId);
		
		if (!userId.equals(message.getSenderId())) {
			throw new RuntimeException(ERR_MSG_CANNOT_DEL_MESSAGE);
		}
		
		chat.deleteReply(messageId);
		
		this.privateChatDao.saveChat(chat);
	
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
				.build();
		
		this.privateChatDao.saveChat(newChat);
		
	}
		
}