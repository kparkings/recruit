package com.arenella.recruit.curriculum.adapters;

import java.util.Set;
import java.util.UUID;

/**
* Defines functionality for listening to Events from external Services
* @author K Parkings
*/
public interface ExternalEventListener {

	/**
	* Listens for Events containing information relating to Skills 
	* Searched that have been searched for
	* @param skills - Skills searched for
	*/
	public void listenForSearchedSkillsEvent(Set<String> skills);
	
	/**
	* Listerner for CurriculumDeletedEvent
	* @param pendingCandidateId - Id of Deleted PendingCurriculum
	*/
	public void listenForPendingCurriculumDeletedEvent(UUID pendingCandidateId);
	
}
