package com.arenella.recruit.candidates.dao;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.beans.PendingCandidate;
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
	
	/**
	* 
	* @param emailId
	* @return
	*/
	default Optional<PendingCandidate> fetchPendingCandidateByEmail(String emailId){
		
		Set<PendingCandidateEntity> matches = this.fetchByEmail(emailId);
		
		if (matches.size() > 1) {
			throw new IllegalStateException("Cant have two Candidates with the same Email");
		} 
		
		if (matches.isEmpty()) {
			return Optional.empty();
		}
		
		return matches.stream().findFirst().map(e -> PendingCandidateEntity.convertFromEntity(e));
		
	}
	
}
