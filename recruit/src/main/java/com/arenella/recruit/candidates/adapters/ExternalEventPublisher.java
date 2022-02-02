package com.arenella.recruit.candidates.adapters;

import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.adapters.events.CandidateNoLongerAvailableEvent;

/**
* Defines functionality for publishing Events to external services 
* @author K Parkings
*/
public interface ExternalEventPublisher {

	/**
	* Posts an event containing Skills that where searched on when
	* searching for Candidates
	* @param skills - Skills used in Candidate Search
	*/
	public void publishSearchedSkillsEvent(Set<String> skills);
	
	/**
	* Publishes an event when PendingCurriculum is deleted
	* @param pendingCandidateId - Uniue Id of PendingCurriculum that was deleted
	*/
	public void publishPendingCurriculumDeletedEvent(UUID pendingCandidateId);
	
	/**
	* Publishes an event informing that a Candidate is no longer available
	* @param event
	*/
	public void publishCandidateNoLongerAvailableEvent(CandidateNoLongerAvailableEvent event);
	
}
