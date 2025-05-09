package com.arenella.recruit.candidates.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import com.arenella.recruit.candidates.beans.CandidateProfileViewedEvent;
import com.arenella.recruit.candidates.entities.CandidateProfileViewedEventEntity;

/**
* Repository for working with persostemce store for CandidateProfileViewedEventEntity objects
*/
public interface CandidateProfileViewedEventEntityDao extends ListCrudRepository<CandidateProfileViewedEventEntity, UUID> {
	
	/**
	* Returns all Events since a certain point in time
	* @param viewedGTE - Min moment in time to return events from
	* @return events
	*/
	default Set<CandidateProfileViewedEvent> fetchCandidateProfileViewedEvents(LocalDateTime viewedGTE) {
		return this.findByViewedGreaterThanOrEqualTo(viewedGTE).stream().map(CandidateProfileViewedEventEntity::toDomain).collect(Collectors.toSet());
	}
	
	/**
	* Return all events for specific Candidate
	* @param candidateId - Unique id of the candidate
	* @return events
	*/
	default Set<CandidateProfileViewedEvent> fetchCandidateProfileViewedEventsByCandidateId(String candidateId) {
		return this.findByCandidateId(candidateId).stream().map(CandidateProfileViewedEventEntity::toDomain).collect(Collectors.toSet());
	}
	
	/**
	* Returns all events specific to a Recruiter
	* @param recruiterId - Recruiter events
	* @return events
	*/
	default Set<CandidateProfileViewedEvent> fetchCandidateProfileViewedEventsByRecruiterId(String recruiterId) {
		return this.findByRecruiterId(recruiterId).stream().map(CandidateProfileViewedEventEntity::toDomain).collect(Collectors.toSet());
	}
	
	/**
	* Do not call directly. Use domain specific version
	*/
	List<CandidateProfileViewedEventEntity> findByCandidateId(String candidateId);
	
	/**
	* Do not call directly. Use domain specific version
	*/
	List<CandidateProfileViewedEventEntity> findByRecruiterId(String recruiterId);
	
	/**
	* Do not call directly. Use domain specific version
	*/
	
	@Query("from CandidateProfileViewedEventEntity where viewed >= :since")
	List<CandidateProfileViewedEventEntity> findByViewedGreaterThanOrEqualTo(LocalDateTime since);

	/**
	* Persists an event
	* @param event - Event to be persisted
	*/
	default void save(CandidateProfileViewedEvent event) {
		this.save(CandidateProfileViewedEventEntity.fromDomain(event));
	}
	
}