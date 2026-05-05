package com.arenella.recruit.campaigns.adapters;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterDeletedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;

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
}