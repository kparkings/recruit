package com.arenella.recruit.candidates.adapters;

import com.arenella.recruit.adapters.actions.GrantCreditCommand;
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
	* Listener for GrantCreditCommand indicating it is time to update
	* the Credits for a Recruiter
	* @param command - To update Credit's
	*/
	public void listenForGrantCreditCommand(GrantCreditCommand command);
}
