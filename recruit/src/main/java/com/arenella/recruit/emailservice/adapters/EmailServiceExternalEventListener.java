package com.arenella.recruit.emailservice.adapters;

import com.arenella.recruit.adapters.events.CandidateUpdatedEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.candidates.adapters.CandidateCreatedEvent;

/**
* Defines functionality for listening to Events from external Services
* @author K Parkings
*/
public interface EmailServiceExternalEventListener {

	/**
	* Listens for events requesting an email is sent
	* @param command - Command to request sending of email
	*/
	public void listenForSendEmailCommand(RequestSendEmailCommand command);

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
	* Listens for events for the creation of a new Candidate
	* @param event - Candidate created
	*/
	public void listenForCandidateCreatedEvent(CandidateCreatedEvent event);

	/**
	* Listens for events for the updating of an existing Candidate
	* @param event - Candidate update
	*/
	public void listenForCandidateUpdatedEvent(CandidateUpdatedEvent event);

}
