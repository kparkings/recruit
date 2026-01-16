package com.arenella.recruit.messaging.dao;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.entities.ChatParticipantEntity;

/**
* DAO for interacting with Chat Participants 
*/
public interface ChatParticipantDao extends ListCrudRepository<ChatParticipantEntity, String>{

	/**
	* Where it exists returns the ChatParticipant matching the given Id
	* @param participantId - Unique id
	* @return ChatParticipant
	*/
	default Optional<ChatParticipant> fetchChatParticipantById(String participantId){
		return this.findById(participantId).map(ChatParticipantEntity::fromEntity);
	}

	/**
	* Persists the ChatParticipant
	* @param chatParticipant - Chat participant to be persisted
	*/
	default void persistChatParticipant(ChatParticipant chatParticipant) {
		this.save(ChatParticipantEntity.toEntity(chatParticipant));
	}
	
}
