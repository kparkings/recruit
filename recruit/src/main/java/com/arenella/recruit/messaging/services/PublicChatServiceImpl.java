package com.arenella.recruit.messaging.services;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.SequencedSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import com.arenella.recruit.messaging.beans.PublicChat;
import com.arenella.recruit.messaging.beans.PublicChat.AUDIENCE_TYPE;
import com.arenella.recruit.messaging.beans.PublicChatNotification;
import com.arenella.recruit.messaging.beans.PublicChatNotification.NotificationType;
import com.arenella.recruit.messaging.dao.PublicChatDao;

/**
* Chat Services for Public Chats
*/
@Service
public class PublicChatServiceImpl implements PublicChatService {

	private PublicChatDao 					chatDao;
	private PublicChatNotificationService 	notificationService;
	private ParticipantService 				participantService;
	
	/**
	* Constructor
	* @param chatDao 				- Repository for PublicChat's
	* @param notificationService 	- Services for Notification relating to Chats
	* @param participantService		- Services relating to Chat Participants
	*/
	public PublicChatServiceImpl(PublicChatDao chatDao, PublicChatNotificationService notificationService, ParticipantService participantService) {
		this.chatDao 				= chatDao;
		this.notificationService 	= notificationService;
		this.participantService		= participantService;
	}
	
	/**
	* Refer to the PublicChatService interface for details 
	*/
	@Override
	public UUID createChat(UUID parentChat, String message, Principal user) {
		
		PublicChat chat = PublicChat
				.builder()
					.id(UUID.randomUUID())
					.created(LocalDateTime.now())
					.audienceType(AUDIENCE_TYPE.ALL)
					.message(message)
					.ownerId(user.getName())
					.parentChat(parentChat)
				.build();
		
		chat.getParentChat().ifPresent(parentId -> {
			if (!this.chatDao.existsById(parentId))  {
				throw new IllegalArgumentException(PublicChatService.ERR_MSG_UNKNOWN_PARENT);
			}
		});
		
		if (!chat.getOwnerId().equals(user.getName())) {
			throw new IllegalArgumentException(PublicChatService.ERR_MSG_CANT_CREATE_CHAT_FOR_ANOTHER_USER);
		}
			
		this.chatDao.saveChat(chat);
		
		//If a reply we notify the poster of the original Chat
		chat.getParentChat().ifPresent(parentChatId -> {
			this.notificationService.persistNotification(PublicChatNotification
					.builder()
						.notificationId(UUID.randomUUID())
						.chatId(chat.getId())
						.created(LocalDateTime.now())
						.destinationUserId(this.chatDao.fetchPublicChat(parentChatId).orElseThrow().getOwnerId())
						.initiatingUserId(user.getName())
						.type(NotificationType.REPLY)
					.build());
		});
		
		return chat.getId();
		
	}
	
	/**
	* Refer to the PublicChatService interface for details 
	*/
	@Override
	public void updateChat(UUID chatId, String message, Principal user) {
		
		if (!this.chatDao.existsById(chatId)) {
			throw new IllegalArgumentException(PublicChatService.ERR_MSG_UKNOWN_CHAT);
		}
		
		this.chatDao.fetchChatById(chatId).ifPresent(existingChat -> {
			
			if (!existingChat.getOwnerId().equals(user.getName())) {
				throw new IllegalArgumentException(PublicChatService.ERR_MSG_CANT_UPDATE_CHAT_FOR_ANOTHER_USER);
			}
			
			PublicChat newChat = PublicChat
					.builder()
						.publicChat(existingChat)
						.message(message)
					.build();
			
			this.chatDao.saveChat(newChat);
			
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
			this.notificationService.deleteNotificationsForChat(chat.getId());
			this.deleteChatChildrenNotifications(chat);
			this.deleteChatChildren(chat);
			this.chatDao.deleteById(chat.getId());
		});
	}
	
	/**
	* Refer to the PublicChatService interface for details 
	*/
	@Override
	public PublicChat removeLikeForChat(UUID chatId, String name) {
		
		AtomicReference<PublicChat> atomicChat = new AtomicReference<>();
		
		this.chatDao.fetchChatById(chatId).ifPresent(chat -> {
			
			Set<String> likes = chat.getLikes();
			
			boolean deleteNotification = false;
			
			if (chat.getLikes().contains(name)) {
				likes.remove(name);
				deleteNotification = true;
				
			}
			
			PublicChat updatedChat = PublicChat.builder().publicChat(chat).likes(likes).build();
			
			//If like removed also remove the notification if it existed
			if (deleteNotification) {
				this.notificationService.fetchNotificationsForChat(chatId)
					.stream()
					.filter(notification -> notification.getType() == NotificationType.LIKE && notification.getInitiatingUserId().equals(name))
					.forEach(notificationToDelete -> this.notificationService.deleteNotification(notificationToDelete.getNotificationId(), name));
			}
			
			this.chatDao.saveChat(updatedChat);
			
		});
		
		return atomicChat.get();
		
	}
	
	/**
	* Refer to the PublicChatService interface for details 
	*/
	@Override
	public PublicChat toggleLikeForChat(UUID chatId, String name) {
		
		AtomicReference<PublicChat> atomicChat = new AtomicReference<>();
		
		this.chatDao.fetchChatById(chatId).ifPresent(chat -> {
			
			Set<String> likes = chat.getLikes();
			
			boolean createNotification = false;
			boolean deleteNotification = false;
			
			if (chat.getLikes().contains(name)) {
				likes.remove(name);
				deleteNotification = true;
				
			} else {
				likes.add(name);
				createNotification = true;
			}
			
			PublicChat updatedChat = PublicChat.builder().publicChat(chat).likes(likes).build();
			
			//If like removed also remove the notification if it existed
			if (deleteNotification) {
				this.notificationService.fetchNotificationsForChat(chatId)
					.stream()
					.filter(notification -> notification.getType() == NotificationType.LIKE && notification.getInitiatingUserId().equals(name))
					.forEach(notificationToDelete -> this.notificationService.deleteNotification(notificationToDelete.getNotificationId(), name));
			}
			
			this.chatDao.saveChat(updatedChat);
			
			atomicChat.set(updatedChat);
			
			//If a like we notify the poster of the original Chat
			if (createNotification) {
				this.notificationService.persistNotification(PublicChatNotification
						.builder()
							.notificationId(UUID.randomUUID())
							.chatId(chat.getId())
							.created(LocalDateTime.now())
							.destinationUserId(this.chatDao.fetchPublicChat(chatId).orElseThrow().getOwnerId())
							.initiatingUserId(name)
							.type(NotificationType.LIKE)
						.build());
			}
			
		});
		
		return atomicChat.get();
		
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
			
			this.notificationService.deleteNotificationsForChat(chat.getId());
			this.deleteChatChildrenNotifications(chat);
			this.deleteChatChildren(chat);
			this.chatDao.deleteById(chat.getId());
			
		});
		
	}
	
	/**
	* Refer to the PublicChatService interface for details 
	*/
	@Override
	public void systemDeleteChatsForUser(String userId) {
		this.chatDao.fetchChatsForUser(userId).stream().forEach(chat -> {
			this.notificationService.deleteNotificationsForChat(chat.getId());
			this.deleteChatChildrenNotifications(chat);
			this.deleteChatChildren(chat);
			this.chatDao.saveChat(PublicChat.builder().publicChat(chat).likes(Set.of()).build());
			this.chatDao.deleteById(chat.getId());
		});
		
	}
	
	public void removeLikesForUser(String userId) {
		
		this.chatDao.fetchChatsLikedByUser(userId).forEach(chat -> {
			this.removeLikeForChat(chat.getId(), userId);
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
			this.chatDao.saveChat(PublicChat.builder().publicChat(chat).likes(Set.of()).build());
			this.chatDao.deleteById(child.getId());
		});
	}
	
	/**
	* Recursive delete of Chat's children's notifications 
	* @param chat - Chat to delete notification for
	*/
	private void deleteChatChildrenNotifications(PublicChat chat) {
		this.notificationService.deleteNotificationsForChat(chat.getId());
		this.fetchChatChildren(chat.getId()).stream().forEach(child -> {
			deleteChatChildrenNotifications(child);
			this.notificationService.deleteNotificationsForChat(chat.getId());
		});
	}

	public SequencedSet<UUID> fetchPathToTopLevelChat(UUID chatId){
		
		LinkedHashSet<UUID> pathFromChiltToTopLevelParent = new LinkedHashSet<>();
		
		PublicChat chat = this.chatDao.fetchChatById(chatId).orElseThrow();
		
		while (Optional.ofNullable(chat.getParentChat()).isPresent()) {
		
			pathFromChiltToTopLevelParent.add(chat.getId());
			
			if(chat.getParentChat().isEmpty()) {
				break;
			}
			
			chat = this.chatDao.fetchChatById(chat.getParentChat().orElseThrow()).orElseThrow();
		}
		
		return pathFromChiltToTopLevelParent.reversed();
	}

	/**
	* Refer to the PublicChatService interface for details 
	*/
	@Override
	public boolean isUnreadNewsFeedItems(String authenticatedUserId) {
		AtomicBoolean unreadItems = new AtomicBoolean(false);
		this.fetchTopLevelChats(0, 1).stream().findFirst().ifPresent(chat -> {
			this.participantService.fetchById(authenticatedUserId).ifPresent(chatParticipant -> {
				if (chatParticipant.getLastTimeNewsFeedViewed().isEmpty()) {
					unreadItems.set(true);
				}
				
				chatParticipant.getLastTimeNewsFeedViewed().ifPresent(lastView ->{
					if (chat.getCreated().isAfter(lastView)) {
						unreadItems.set(true);
					}
				});
			});
		});
		return unreadItems.get();
	}
	
}