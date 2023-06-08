package com.arenella.recruit.candidates.dao;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.controllers.SavedCandidate;
import com.arenella.recruit.candidates.entities.SavedCandidateEntity;
import com.arenella.recruit.candidates.entities.SavedCandidateEntityId;

/**
* Repository for SavedCandidateEntity
* @author K Parkings
*/
public interface SavedCandidateDao extends CrudRepository<SavedCandidateEntity, SavedCandidateEntityId>{

	@Query("FROM SavedCandidateEntity WHERE id.userId = :userId")
	Set<SavedCandidateEntity> fetchByUserId(String userId);
	
	@Query("FROM SavedCandidateEntity WHERE id.candidateId = :userId")
	Set<SavedCandidateEntity> fetchByCandidateId(long userId);
	
	
	/**
	* Returns the SavedCandidates for a User
	* @param userId - Unique Id of the User
	* @return Users Saved Candidates
	*/
	public default Set<SavedCandidate> fetchSavedCandidatesByUserId(String userId) {
		return this.fetchByUserId(userId)
				.stream()
				.map(SavedCandidateEntity::convertFromEntity)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Returns is SavedCandidate already exists
	* @param userId
	* @param candidateId
	* @return
	*/
	public default boolean exists(String userId, long candidateId) {
		
		SavedCandidateEntityId id = new SavedCandidateEntityId();
		
		id.setCandidateId(candidateId);
		id.setUserId(userId);
		
		return this.existsById(id);
		
	}
	
	/**
	* Delete Saved canidate by Id
	* @param userId			- Id of user
	* @param candidateId	- id of candidate to be removed
	*/
	public default void delete(String userId, long candidateId) {
		
		SavedCandidateEntityId id = new SavedCandidateEntityId();
		
		id.setCandidateId(candidateId);
		id.setUserId(userId);
		
		this.deleteById(id);
		
	}

	/**
	* Persists a SavedCandidate 
	*/
	public default void persistSavedCandidate(SavedCandidate savedCandidate) {
		
		this.save(SavedCandidateEntity.convertToEntity(savedCandidate));
		
	}

	/**
	* Updates a SavedCandidate 
	*/
	public default void updateSavedCandidate(SavedCandidate savedCandidate) {
		this.save(SavedCandidateEntity.convertToEntity(savedCandidate));
	}
	
	/**
	* Deletes all the SavedCandidates associated with a specific Candidate
	* @param candidateId - id of candidate
	*/
	public default void deleteByCandidateId(long candidateId) {
		this.deleteAll(fetchByCandidateId(candidateId));
	}
	
}
