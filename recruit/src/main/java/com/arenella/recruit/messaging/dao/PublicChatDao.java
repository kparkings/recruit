package com.arenella.recruit.messaging.dao;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.messaging.beans.PublicChat;
import com.arenella.recruit.messaging.entities.PublicChatEntity;

/**
* Repository for PublicChatEntities
*/
@Repository
public interface PublicChatDao extends ListCrudRepository<PublicChatEntity, UUID>{
	
	@Query("from PublicChatEntity where parentChat is null")
	List<PublicChatEntity> findTopLevelChats(Pageable pageable);
	
	@Query("from PublicChatEntity where parentChat = :parentId")
	List<PublicChatEntity> findChatChildren(UUID parentId);
	
	@Query("from PublicChatEntity where parentChat is null and created <= :olderThanOrEqualTo")
	List<PublicChatEntity> findChatsOlderThanOrEqualTo(LocalDate olderThanOrEqualTo);
	
	/**
	* If available returns a specific Chat
	* @param chatId - UniqueId of the chat
	* @return Chat
	*/
	default Optional<PublicChat> fetchChatById(UUID chatId) {
		return this.findById(chatId).map(PublicChatEntity::fromEntity);
	}
	
	/**
	* Saves a chat
	* @param chat
	*/
	default void saveChat(PublicChat chat) {
		this.save(PublicChatEntity.toEntity(chat));
	}
	
	/**
	* Returns the Chat with the given id
	* @param chatId - Unique Id of chat to return
	* @return Requested CHat
	*/
	default Optional<PublicChat> fetchPublicChat(UUID chatId){
		return this.findById(chatId).map(PublicChatEntity::fromEntity);
	}
	
	/**
	* Returns top level chats. That is, chats that have no parent chat.
	* @return Top level chats
	*/
	default Set<PublicChat> fetchTopLevelChats(int page, int maxResults) {
		return this.findTopLevelChats(PageRequest.of(page, maxResults)).stream().map(PublicChatEntity::fromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Returns all the direct children of a given Chat
	* @param chatId - id of the Chat to fetch direct children for
	* @return direct children of the Chat
	*/
	default Set<PublicChat> fetchChatChildren(UUID chatId){
		return this.findChatChildren(chatId).stream().map(PublicChatEntity::fromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	} 
	
	/**
	* Returns top level chats created before the cuttoff
	* @param olderThanOrEqualTo - cuttoff
	* @return Top level chats older than the cuttoff
	*/
	default Set<PublicChat> fetchTopLevelChatsOlderThanOrEqualTo(LocalDate olderThanOrEqualTo) {
		return this.findChatsOlderThanOrEqualTo(olderThanOrEqualTo).stream().map(PublicChatEntity::fromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
}