package com.arenella.recruit.messaging.services;

import com.arenella.recruit.messaging.beans.ChatParticipant;

/**
* Services for interacting with Chat participants 
*/
public interface ParticipantService {

	/**
	* Saves / Updates a Participant
	* @param participant - Participant to persist
	*/
	void persistParticpant(ChatParticipant participant);
	
	/**
	* Deletes if existing the Participant
	* @param participantId - Id of Participant to delete
	*/
	void deletePartipant(String participantId);
	
}
