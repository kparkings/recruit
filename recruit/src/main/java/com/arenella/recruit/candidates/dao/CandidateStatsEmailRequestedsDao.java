package com.arenella.recruit.candidates.dao;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.entities.CandidateEmailRequestEventEntity;

/**
* Functionality for persisting Statistics related to Candidates
* @author K Parkings
*/
public interface CandidateStatsEmailRequestedsDao extends CrudRepository<CandidateEmailRequestEventEntity, UUID>{
	
	/**
	* Persists an Event for a Recruiter requesting the email address of a Candidate
	* @param eventId		- Unique Id of Event
	* @param created		- When Event occurred
	* @param recruiterId	- Id of the Recruiter
	* @param candidateId	- Id of the Candidate
	*/
	default void persistEmailRequestedEvent(UUID eventId, LocalDateTime created, String recruiterId, long candidateId) {
		
		CandidateEmailRequestEventEntity event = new CandidateEmailRequestEventEntity(eventId, created, recruiterId, candidateId);
	
		this.save(event);
	}
	
	
}
