package com.arenella.recruit.candidates.dao;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.beans.CandidateSearchAlertMatch;
import com.arenella.recruit.candidates.entities.CandidateSearchAlertMatchEntity;

/**
* Repository for CandidateSearchAlertMatch's
* @author K Parkings
*/
public interface CandidateSearchAlertMatchDao extends CrudRepository<CandidateSearchAlertMatchEntity, UUID>{

	/**
	* Persists a CandidateSearchAlertMatch
	* @param match - Match to persist
	*/
	public default void saveMatch(CandidateSearchAlertMatch match) {
		this.save(CandidateSearchAlertMatchEntity.convertToEntity(match));
	}
	
	@Query("select distinct recruiterId from CandidateSearchAlertMatchEntity")
	public Set<String> getRecruitersWithMatches();
	
	@Query("from CandidateSearchAlertMatchEntity e where e.recruiterId = :recruiterId")
	public Set<CandidateSearchAlertMatchEntity> getMatchesForRecruitersAsEntities(String recruiterId);

	/**
	* Returns Matches for a specific recruiter
	* @param recruiterId
	* @return
	*/
	public default Set<CandidateSearchAlertMatch> getMatchesForRecruiters(String recruiterId){
		return this.getMatchesForRecruitersAsEntities(recruiterId)
			.stream()
			.map(CandidateSearchAlertMatchEntity::convertFromEntity)
			.collect(Collectors.toCollection(LinkedHashSet::new));
	}

}
