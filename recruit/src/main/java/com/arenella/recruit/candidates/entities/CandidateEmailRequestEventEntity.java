package com.arenella.recruit.candidates.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.curriculum.beans.CandidateEmailRequestEvent;

/**
* Event represents a request by a Recruiter for a specific Candidates
* email address
* @author K Parkings
*/
@Entity
@Table(schema="candidate", name="event_candidate_email_request")
public class CandidateEmailRequestEventEntity {

	@Id
	@Column(name="event_id")
	UUID				eventId;
	
	@Column(name="created")
	LocalDateTime 	created;
	
	@Column(name="recruiter_id")
	String 			recruiterId;
	
	@Column(name="candidate_id")
	long	 			candidateId;
	
	/**
	* For Hibernate
	*/
	@SuppressWarnings("unused")
	private CandidateEmailRequestEventEntity() {
		//For Hibernate
	}
	
	/**
	* Constructor for Event
	* @param eventId		- Unique Identifier for the Event
	* @param created		- Creation date/time of the event
	* @param recruiterId	- Id of recruiter that caused the event
	* @param candidateId	- Id of candidate event relates to
	*/
	public CandidateEmailRequestEventEntity(UUID eventId, LocalDateTime created, String recruiterId, long candidateId) {
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
	
	/**
	* Converts an Entity representation of the event to a Domain representation
	* @param entity - Entity to convert
	* @return Domain representation
	*/
	public static CandidateEmailRequestEvent convertFromEntity(CandidateEmailRequestEventEntity entity) {
		return new CandidateEmailRequestEvent(entity.getEventId(), entity.getCreated(), entity.getRecruiterId(), entity.getCandidateId());
	}
	
}