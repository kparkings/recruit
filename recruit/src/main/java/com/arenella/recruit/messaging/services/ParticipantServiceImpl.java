package com.arenella.recruit.messaging.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.dao.ChatParticipantDao;

/**
* Services for interacting with ChatParticipants 
*/
@Service
public class ParticipantServiceImpl implements ParticipantService{

	private ChatParticipantDao chatParticipantDao;
	
	/**
	* Constructor
	* @param participantDao - Dao for Chat Participants
	*/
	public ParticipantServiceImpl(ChatParticipantDao chatParticipantDao) {
		this.chatParticipantDao = chatParticipantDao;
	}
	
	/**
	* Refer to the ParticipantService for details 
	*/
	@Override
	public void persistParticpant(ChatParticipant participant) {
		this.chatParticipantDao.persistChatParticipant(participant);
	}

	/**
	* Refer to the ParticipantService for details 
	*/
	@Override
	public void deletePartipant(String participantId) {
		this.chatParticipantDao.fetchChatParticipantById(participantId).ifPresent(_ -> 
			this.chatParticipantDao.deleteById(participantId)
		);
	}

	/**
	* Refer to the ParticipantService for details 
	*/
	@Override
	public Optional<ChatParticipant> fetchById(String participantId) {	
		return this.chatParticipantDao.fetchChatParticipantById(participantId);
	}

}