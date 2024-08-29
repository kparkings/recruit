package com.arenella.recruit.curriculum.adapters;

import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.adapters.actions.RequestSkillsForCurriculumCommand;
import com.arenella.recruit.adapters.events.CandidateDeletedEvent;
import com.arenella.recruit.adapters.events.CandidateNoLongerAvailableEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.candidates.adapters.CandidateCreatedEvent;

/**
* Defines functionality for listening to Events from external Services
* @author K Parkings
*/
public interface CurriculumExternalEventListener {

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
	
	/**
	* Listens for an event informing that a Candidate is no longer 
	* available in the system 
	* @param event - Event to listen for
	*/
	public void listenForCandidateNoLongerAvailableEvent(CandidateNoLongerAvailableEvent event);

	/**
	* Listens for an event informing that a Candidate has been created
	* @param event - Event to listen for
	*/
	void listenForCandidateCreatedEvent(CandidateCreatedEvent event);

	/**
	* Listens for an event informing that a Candidate has been deleted
	* @param candidateDeletedEvent - Event to listen for
	*/
	public void listenForCandidteDeletedEvent(CandidateDeletedEvent candidateDeletedEvent);

	/**
	* Handles event of SubscriptionAddedEvent for Recruiter 
	*/
	void listenForSubscriptionAddedEvent(SubscriptionAddedEvent event);

	/**
	* Handles event of RecruiterNoOpenSubscriptionEvent for Recruiter 
	*/
	void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent event);

	/**
	* Listens for events for the creation of a new recruiter
	* @param event - Recruiter created
	*/
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event);

	/**
	* Listens for a Command requesting Skills contained in a given Curriculum
	* @param command - Specifies which Curriculum to extract skills for
	*/
	public void listenForRequestSkillsForCurriculumCommand(RequestSkillsForCurriculumCommand command);
	
}
