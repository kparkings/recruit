package com.arenella.recruit.messaging.dao;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import com.arenella.recruit.messaging.entities.ChatMessageEntity;

/**
* Dao for interacting with ChatMessage's 
*/
public interface ChatMessageDao extends ListCrudRepository<ChatMessageEntity,UUID>{

}
