package com.arenella.recruit.messaging.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.dao.ChatParticipantDao;
import com.arenella.recruit.messaging.utils.MessagingCandidateImageManipulator;

/**
* Services for interacting with ChatParticipants 
*/
@Service
public class ParticipantServiceImpl implements ParticipantService{

	private ChatParticipantDao 					chatParticipantDao;
	private MessagingCandidateImageManipulator 	imageManipulator;
	
	/**
	* Constructor
	* @param participantDao - Dao for Chat Participants
	*/
	public ParticipantServiceImpl(ChatParticipantDao chatParticipantDao, MessagingCandidateImageManipulator imageManipulator) {
		this.chatParticipantDao = chatParticipantDao;
		this.imageManipulator 	= imageManipulator;
	}
	
	/**
	* Refer to the ParticipantService for details 
	*/
	@Override
	public void persistParticpant(ChatParticipant participant) {
		
		ChatParticipant chatParticipant = ChatParticipant
				.builder()
				.chatParticipant(participant)
				.photo(imageManipulator.resizeToThumbnail(participant.getPhoto().orElse(null)))
				.build();
		
		this.chatParticipantDao.persistChatParticipant(chatParticipant);
		
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