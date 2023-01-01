package com.arenella.recruit.candidates.dao;

import java.util.UUID;

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
	
}
