package com.arenella.recruit.authentication.adapters;

import com.arenella.recruit.adapters.events.CandidateAccountCreatedEvent;
import com.arenella.recruit.adapters.events.CandidateDeletedEvent;
import com.arenella.recruit.adapters.events.CandidatePasswordUpdatedEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterHasOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterPasswordUpdatedEvent;

/**
* Defines functionality for listening to Events from external Services
* @author K Parkings
*/
public interface AuthenticationExternalEventListener {

	/**
	* Listens for events relating to the creation of a Recruiter
	* @param event - RecruiterCreatedEvent
	*/
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event);
	
	/**
	* Listens for events relating to the creation of a Candidate
	* @param event - CandidateCreatedEvent
	*/
	public void listenForCandidateAccountCreatedEvent(CandidateAccountCreatedEvent event);
	
	/**
	* Listens for event informing that a Recruiter has no 
	* Open subscriptions
	*/
	public void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent event);
	
	/**
	* Listens for event informing that a Recruiter has received 
	* an open Subscription
	*/
	public void listenForRecruiterHasOpenSubscriptionEvent(RecruiterHasOpenSubscriptionEvent event);

	/**
	* Listens for Event informing that a Recruiters Password has been updated
	* @param event
	*/
	public void listenForRecruiterPasswordUpdatedEvent(RecruiterPasswordUpdatedEvent event);

	/**
	* Listens for Event informing that a Recruiters Password has been updated
	* @param event
	*/
	public void listenForCandidatePasswordUpdatedEvent(CandidatePasswordUpdatedEvent event);

	/**
	* Listens for Event informing that a Candidate has been deleted
	* @param event
	*/
	public void listenForCandidteDeletedEvent(CandidateDeletedEvent candidateDeletedEvent);

}