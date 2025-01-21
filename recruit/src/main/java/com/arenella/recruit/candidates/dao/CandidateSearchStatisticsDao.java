package com.arenella.recruit.candidates.dao;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.beans.CandidateSearchEvent;
import com.arenella.recruit.candidates.entities.CandidateSearchEventEntity;

/**
* Functionality for persisting Statistics related to Candidates
* @author K Parkings
*/
public interface CandidateSearchStatisticsDao extends CrudRepository<CandidateSearchEventEntity, UUID>{
	
	@Query("from CandidateSearchEventEntity where userId = :recruiterId and timestamp >= :since")
	Set<CandidateSearchEventEntity> fetchEventForRecruiterAsEntities(String recruiterId, LocalDate since);

	@Query("from CandidateSearchEventEntity where timestamp >= :since")
	Set<CandidateSearchEventEntity> fetchEventSinceAsEntities(LocalDate since);

	/**
	* Returns the Search events of a Recruiter
	* @param recruiterId - Unique id of the recruiter
	* @param since 		 - Return events from the date provided
	* @return Events for Recruiter
	*/
	default Set<CandidateSearchEvent> fetchEventForRecruiter(String recruiterId, LocalDate since){
		return this.fetchEventForRecruiterAsEntities(recruiterId, since).stream().map(CandidateSearchEventEntity::fromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Returns events created since a specified date
	* @param since - Date events need to have occurred after
	* @return matching events
	*/
	default Set<CandidateSearchEvent> fetchEventsSince(LocalDate since) {
		return this.fetchEventSinceAsEntities(since).stream().map(CandidateSearchEventEntity::fromEntity).collect(Collectors.toSet());
	}
	
}
