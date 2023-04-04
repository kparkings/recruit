package com.arenella.recruit.candidates.adapters;

import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.adapters.events.CandidateAccountCreatedEvent;
import com.arenella.recruit.adapters.events.CandidateNoLongerAvailableEvent;
import com.arenella.recruit.adapters.events.CandidatePasswordUpdatedEvent;
import com.arenella.recruit.adapters.events.CandidateUpdatedEvent;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;

/**
* Defines functionality for publishing Events to external services 
* @author K Parkings
*/
public interface ExternalEventPublisher {

	/**
	* Posts an event containing Skills that where searched on when
	* searching for Candidates
	* @param skills - Skills used in Candidate Search
	*/
	public void publishSearchedSkillsEvent(Set<String> skills);
	
	/**
	* Publishes an event when PendingCurriculum is deleted
	* @param pendingCandidateId - Unique Id of PendingCurriculum that was deleted
	*/
	public void publishPendingCurriculumDeletedEvent(UUID pendingCandidateId);
	
	/**
	* Publishes an event informing that a Candidate is no longer available
	* @param event
	*/
	public void publishCandidateNoLongerAvailableEvent(CandidateNoLongerAvailableEvent event);
	
	/**
	* Publishes an event informing that a new Candidate has been added
	* @param event - Details of new Candidate
	*/
	public void publishCandidateCreatedEvent(CandidateCreatedEvent event);
	
	/**
	* Publishes command requesting email is sent to recruiter with details of 
	* todays Candidate alert matches
	* @param command - Contains details of Alert matches for a specific Recruiter
	*/
	public void publishRequestSendAlertDailySummaryEmailCommand(RequestSendAlertDailySummaryEmailCommand command);
	
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
	
	/**
	* Published a command to send an email
	* @param command - Contains details of email to be sent
	*/
	public void publishSendEmailCommand(RequestSendEmailCommand command);
	
}
