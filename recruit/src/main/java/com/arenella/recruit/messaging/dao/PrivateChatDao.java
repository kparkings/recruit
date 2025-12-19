package com.arenella.recruit.messaging.dao;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import com.arenella.recruit.messaging.beans.PrivateChat;
import com.arenella.recruit.messaging.entities.PrivateChatEntity;

/**
* Dao for interacting with PrivateChats 
*/
public interface PrivateChatDao extends ListCrudRepository<PrivateChatEntity, UUID>{

	@Query("from PrivateChatEntity where senderId = :userId OR recipientId = :userId")
	List<PrivateChatEntity> findUserChats(String userId);
	
	@Query("from PrivateChatEntity ")
			//+ "where "
			//+ "blockedBySender = false "
			//+ "and blockedByRecipient = false ")
			//+ "and ("
			//+ "(lastMissedMessageAlertSender <= :reminderDueTime and lastViewedBySender <= lastUpdated)"
			//+ "OR "
			//+ "(lastMissedMessageAlertRecipient <= :reminderDueTime and lastViewedByRecipient <= lastUpdated)"
			//+ ")")
	List<PrivateChatEntity> findUnblokedChatsRequiringMissingMessageAlert(LocalDateTime reminderDueTime);
	
	/**
	* Returns all Chats where the User is either the Sender or the 
	* Recipient
	* @param userId - Unique ID of the User
	* @return Users Chats
	*/
	default Set<PrivateChat> fetchUserChats(String userId) {
		return this.findUserChats(userId).stream().map(PrivateChatEntity::fromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	} 
	
	/**
	* If available returns a specific Chat
	* @param chatId - UniqueId of the chat
	* @return Chat
	*/
	default Optional<PrivateChat> fetchChatById(UUID chatId) {
		return this.findById(chatId).map(PrivateChatEntity::fromEntity);
	}
	
	/**
	* Saves a user chat
	* @param chat
	*/
	default void saveChat(PrivateChat chat) {
		this.save(PrivateChatEntity.toEntity(chat));
	}
	
	/**
	* Returns any Chats that have not been disabled by either the Sender or Recipient
	* @param cuttoff - Last reminder for either the Sender or Recipient must be before this datetime
	* @return Chats with messages potentially requiring a reminder
	*/
	default Set<PrivateChat> getUnblockedChatsBeforeCuttoff(LocalDateTime cuttoff){
		return this.findUnblokedChatsRequiringMissingMessageAlert(cuttoff).stream().map(PrivateChatEntity::fromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
}