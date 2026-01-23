package com.arenella.recruit.messaging.services;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.arenella.recruit.messaging.beans.PublicChat;
import com.arenella.recruit.messaging.dao.PublicChatDao;

/**
* Chat Services for Public Chats
*/
@Service
public class PublicChatServiceImpl implements PublicChatService {

	private PublicChatDao chatDao;
	
	/**
	* Constructor
	* @param chatDao - Repository for PublicChat's
	*/
	public PublicChatServiceImpl(PublicChatDao chatDao) {
		this.chatDao = chatDao;
	}
	
	/**
	* Refer to the PublicChatService interface for details 
	*/
	@Override
	public UUID saveChat(PublicChat chat, Principal user) {
		
		if (this.chatDao.existsById(chat.getId())) {
			
			this.chatDao.fetchChatById(chat.getId()).ifPresent(existingChat -> {
				
				if ( !chat.getOwnerId().equals(user.getName())) {
					throw new IllegalArgumentException(PublicChatService.ERR_MSG_CANT_UPDATE_CHAT_FOR_ANOTHER_USER);
				}
				
				PublicChat newChat = PublicChat
						.builder()
							.publicChat(existingChat)
							.message(chat.getMessage()).build();
				
				this.chatDao.saveChat(newChat);
				
			});
			
		} else {
			
			chat.getParentChat().ifPresent(parentId -> {
				if (!this.chatDao.existsById(parentId))  {
					throw new IllegalArgumentException(PublicChatService.ERR_MSG_UNKNOWN_PARENT);
				}
			});
			
			if (!chat.getOwnerId().equals(user.getName())) {
				throw new IllegalArgumentException(PublicChatService.ERR_MSG_CANT_CREATE_CHAT_FOR_ANOTHER_USER);
			}
				
			this.chatDao.saveChat(chat);
			
		}
		
		return chat.getId();
		
	}

	/**
	* Refer to the PublicChatService interface for details 
	*/
	@Override
	public void deleteChat(UUID chatId, Principal user) {
		
		if (!this.chatDao.existsById(chatId)) {
			throw new IllegalArgumentException("Cannot delete non existent Chat");
		}
		
		this.chatDao.fetchChatById(chatId).ifPresent(chat -> {
			
			if (!chat.getOwnerId().equals(user.getName())) {
				throw new IllegalArgumentException("Cannot delete other users Chat");
			}
			
			this.deleteChatChildren(chat);
			this.chatDao.deleteById(chat.getId());
		});
		
	}

	/**
	* Refer to the PublicChatService interface for details 
	*/
	@Override
	public PublicChat fetchChat(UUID chatId) {
		return this.chatDao.fetchChatById(chatId).orElseThrow();
	}

	/**
	* Refer to the PublicChatService interface for details 
	*/
	@Override
	public Set<PublicChat> fetchChatChildren(UUID chatId) {
		return this.chatDao.fetchChatChildren(chatId);
	}

	/**
	* Refer to the PublicChatService interface for details 
	*/
	@Override
	public Set<PublicChat> fetchTopLevelChats(int page, int maxResults) {
		return this.chatDao.fetchTopLevelChats(page, maxResults);
	}

	/**
	* Refer to the PublicChatService interface for details 
	*/
	@Override
	public void deleteChatsOlderThan(LocalDate date) {
		this.chatDao.fetchTopLevelChatsOlderThanOrEqualTo(date).stream().forEach(chat -> {
			this.deleteChatChildren(chat);
			this.chatDao.deleteById(chat.getId());
		});
		
	}
	
	/**
	* Recursive function that will delete all child Chats to n levels
	* and then delete the current Chat
	* @param Chat - Chat to delete
	*/
	private void deleteChatChildren(PublicChat chat) {
		this.fetchChatChildren(chat.getId()).stream().forEach(child -> {
			this.deleteChatChildren(child);
			this.chatDao.deleteById(child.getId());
		});
	}
	
}