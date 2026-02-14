package com.arenella.recruit.messaging.services;

import java.util.Optional;

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
	
	/**
	* If exists returns the participant matching the given id
	* @param participantId - unique Id
	* @return ChatParticipant if available
	*/
	Optional<ChatParticipant> fetchById(String participantId);

	/**
	* Toggles the email notification preference for the user
	* @param authenticatedUserId - Id of currently authenticated user
	*/
	Optional<ChatParticipant> toggleReceiveNotificationEmails(String authenticatedUserId);
	
}
