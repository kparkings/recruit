package com.arenella.recruit.messaging.dao;

import org.springframework.data.repository.ListCrudRepository;

/**
* DAO for interacting with Chat Participants 
*/
public interface ChatParticipantEntity extends ListCrudRepository<ChatParticipantEntity, String>{

}
