package com.arenella.recruit.messaging.adapters;

import com.arenella.recruit.adapters.events.CandidateDeletedEvent;
import com.arenella.recruit.adapters.events.CandidateUpdateEvent;
import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterDeletedEvent;
import com.arenella.recruit.adapters.events.RecruiterProfileCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterProfileUpdatedEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.candidates.adapters.CandidateCreatedEvent;

/**
* Defines functionality for listening to Events from external Services
* @author K Parkings
*/
public interface MessagingMessagingServiceExternalEventListener {

	/**
	* Listens for event informing new Candidate created
	* @param event - details of Candidate
	*/
	public void listenForCandidateCreatedEvent(CandidateCreatedEvent event);

	/**
	* Listens for event informing Candidate details updated
	* @param event - details of Candidate
	*/
	public void listenForCandidateUpdateEvent(CandidateUpdateEvent event);
	
	/**
	* Listens for event informing new Recruiter created
	* @param event - details of Recruiter
	*/
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event);
	
	/**
	* Listens for event informing Recruiter details updated
	* @param event - details of Recruiter
	*/
	public void listenForRecruiterUpdatedEvent(RecruiterUpdatedEvent event);
	
	/**
	* Listens for event informing Candidate Deleted from System
	* @param event - details of Candidate
	*/
	public void listenForCandidateDeletedEvent(CandidateDeletedEvent event);
	
	/**
	* Listens for event informing Recruiter Deleted from System
	* @param event - details of Recruiter
	*/
	public void listenForRecruiterDeletedEvent(RecruiterDeletedEvent event);
	
	/**
	* Listens for event informing that a recruiters profile has been created
	* @param event - details of recruiter profile
	*/
	public void listenForRecruiterProfileCreatedEvent(RecruiterProfileCreatedEvent event);
	
	/**
	* Listens for event informing that a recruiters profile has been updated
	* @param event - details of recruiter profile
	*/
	public void listenForRecruiterProfileUpdatedEvent(RecruiterProfileUpdatedEvent event);
	
}