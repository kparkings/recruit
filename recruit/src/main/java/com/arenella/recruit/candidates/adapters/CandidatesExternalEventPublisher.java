package com.arenella.recruit.candidates.adapters;

import com.arenella.recruit.adapters.events.CandidateAccountCreatedEvent;
import com.arenella.recruit.adapters.events.CandidatePasswordUpdatedEvent;
import com.arenella.recruit.adapters.events.CandidateUpdatedEvent;

/**
* Defines functionality for publishing Events to external services 
* @author K Parkings
*/
public interface CandidatesExternalEventPublisher {

	/**
	* Posts an event informing the world that a Candidate account was 
	* created
	* @param event - Event advertising the fact a new User account for a Candidate was created
	*/
	public void publishCandidateAccountCreatedEvent(CandidateAccountCreatedEvent event);
	
	/**
	* Publishes an event informing that a Candidate's password has been updates
	* @param event - Event informing that a Canidate's password has been updates
	*/
	public void publishCandidatePasswordUpdated(CandidatePasswordUpdatedEvent event);

	/**
	* Publishes an event information that a Candidate's details have been updated
	* @param event - Event information that a Candiate's detail's have been updated
	*/
	public void publishCandidateAccountUpdatedEvent(CandidateUpdatedEvent event);
	

	
}