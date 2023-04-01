package com.arenella.recruit.candidates.dao;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.entities.PendingCandidateEntity;

/**
* Repository for PendingCandidate entities
* @author K Parkings
*/
public interface PendingCandidateDao extends CrudRepository<PendingCandidateEntity, UUID>{

	@Query("from PendingCandidateEntity where email = :emailId")
	public Set<PendingCandidateEntity> fetchByEmail(String emailId);
	
	/**
	* Returns whether or not Email is already used for the Candidate
	* @param email - email address to filter on
	* @return If Email already used for the Candidate
	*/
	default boolean emailInUse(String email) {
		return !this.fetchByEmail(email).isEmpty();
	}
	
}
