package com.arenella.recruit.curriculum.adapters;

import java.util.UUID;

import com.arenella.recruit.adapters.events.CreditsAssignedEvent;
import com.arenella.recruit.adapters.events.CreditsUsedEvent;

/**
* Defines functionality for publishing Events to external services 
* @author K Parkings
*/
public interface ExternalEventPublisher {

	/**
	* Informs other services that credits have been assigend to users
	* @param event - CreditsAssignedEvent
	*/
	public void publishCreditsAssignedEvent(CreditsAssignedEvent event);
	
	/**
	* Publishes an event when PendingCurriculum is deleted
	* @param pendingCandidateId - Unique Id of PendingCurriculum that was deleted
	*/
	public void publishPendingCurriculumDeletedEvent(UUID pendingCandidateId);

	/**
	* Publishes an event informing that a User has used a credit 
	* @param creditsUsedEvent
	*/
	public void publishCreditsUsedEvent(CreditsUsedEvent creditsUsedEvent);
	
}
