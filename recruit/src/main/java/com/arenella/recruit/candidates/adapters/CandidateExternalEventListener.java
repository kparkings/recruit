package com.arenella.recruit.candidates.adapters;

import com.arenella.recruit.adapters.events.CreditsAssignedEvent;
import com.arenella.recruit.adapters.events.CreditsUsedEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;

/**
* Defines Listeners for incoming events from External services
* @author K parkings
*/
public interface CandidateExternalEventListener {

	/**
	* Listens for events for the creation of a new recruiter
	* @param event - Recruiter created
	*/
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event);

	/**
	* Listens for events for the updating of an existing recruiter
	* @param event - Recruiter update
	*/
	public void listenForRecruiterUpdatedEvent(RecruiterUpdatedEvent event);
	
	/**
	* Listens for CreditsAssignedEvent
	* @param event - CreditsAssignedEvent
	*/
	public void listenForCreditsAssignedEvent(CreditsAssignedEvent event);

	/**
	* Listens for CreditsAssignedEvent
	* @param event - CreditsAssignedEvent
	*/
	public void listenForCreditsUsedEvent(CreditsUsedEvent any);
	
}