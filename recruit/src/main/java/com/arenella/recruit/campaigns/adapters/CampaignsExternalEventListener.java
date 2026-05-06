package com.arenella.recruit.campaigns.adapters;

import com.arenella.recruit.adapters.events.CandidateDeletedEvent;
import com.arenella.recruit.adapters.events.CandidateUpdateEvent;
import com.arenella.recruit.adapters.events.CandidateUpdatedEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterDeletedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.candidates.adapters.CandidateCreatedEvent;

/**
* Defines Listeners for incoming events from External services
* @author K parkings
*/
public interface CampaignsExternalEventListener {

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
	* Listens for SubscriptionAddedEvent 
	* @param event - SubscriptionAddedEvent
	*/
	public void listenForSubscriptionAddedEvent(SubscriptionAddedEvent event);

	/**
	* Listens for RecruiterNoOpenSubscriptionEvent 
	* @param event - RecruiterNoOpenSubscriptionEvent
	*/
	public void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent recruiterNoOpenSubscriptionEvent);

	/**
	* Listens for Event informing that a Recruiter has been deleted
	* @param event
	*/
	public void listenForRecruiterAccountDeletedEvent(RecruiterDeletedEvent recruiterDeletedEvent);
	
	/**
	* Listens for Event informing that a Recruiter has been deleted
	* @param event
	*/
	public void listenForRecruiterDeletedEvent(RecruiterDeletedEvent event);

	/**
	* Listens for events stating a Candidate has been updated
	* @param candidateUpdateEvent
	*/
	public void listenFor(CandidateUpdateEvent candidateUpdateEvent);

	/**
	* Listens for events stating candidate has been deleted
	* @param candidateDeletedEvent
	*/
	public void listenFor(CandidateDeletedEvent candidateDeletedEvent);

	/**
	* Listens for event stating candidate has been updated
	* @param candidateUpdatedEvent
	*/
	public void listenFor(CandidateUpdatedEvent candidateUpdatedEvent);

	/**
	* Listens for event stating candidate has been created
	* @param candidateCreatedEvent
	*/
	public void listenFor(CandidateCreatedEvent candidateCreatedEvent);
}