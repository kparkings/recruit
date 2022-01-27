package com.arenella.recruit.curriculum.beans;

import java.time.LocalDateTime;
import java.util.UUID;

/**
* Represents an Event where a Candidates email address was 
* requested by a Recruiter
* @author K Parkings
*/
public class CandidateEmailRequestEvent {

	final UUID				eventId;
	final LocalDateTime 	created;
	final String 			recruiterId;
	final long	 			candidateId;
	
	/**
	* Constructor for Event
	* @param eventId		- Unique Identifier for the Event
	* @param created		- Creation date/time of the event
	* @param recruiterId	- Id of recruiter that caused the event
	* @param candidateId	- Id of candidate event relates to
	*/
	public CandidateEmailRequestEvent(UUID eventId, LocalDateTime created, String recruiterId, long candidateId) {
		this.eventId 		= eventId;
		this.created 		= created;
		this.recruiterId 	= recruiterId;
		this.candidateId 	= candidateId;
	} 
	
	/**
	* Returns the unique Id of the event
	* @return event id
	*/
	public UUID getEventId() {
		return this.eventId;
	}
	
	/**
	* Returns when the event was created
	* @return creation date
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}
	
	/**
	* Returns the Id of the recruiter requesting the candidates Email
	* @return Id of Recruiter
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}

	/**
	* Returns the Id of the Candidate whose email was requested
	* @return Id of Candidate
	*/
	public long getCandidateId() {
		return this.candidateId;
	}
	
}