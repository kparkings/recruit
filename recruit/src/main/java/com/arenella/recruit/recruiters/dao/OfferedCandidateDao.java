package com.arenella.recruit.recruiters.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.recruiters.beans.OfferedCandidate;
import com.arenella.recruit.recruiters.entities.OfferedCandidateEntity;

public interface OfferedCandidateDao extends CrudRepository<OfferedCandidateEntity, UUID>{
	
	/**
	* Returns an existing OfferedCandidate
	* @param offeredCandidateId - Unique identifier of OfferedCandidate to return
	* @return offeredCandidate
	*/
	default OfferedCandidate findByOfferedCandidateId(UUID offeredCandidateId) {
		
		OfferedCandidateEntity entity = this.findById(offeredCandidateId).orElseThrow(() -> new RuntimeException("Unknown Offered Candidate"));
		
		return OfferedCandidateEntity.convertFromEntity(entity);
		
	}
	
	/**
	* Persists an OfferedCandidate
	* @param offeredCandidate - OfferedCandidate to be persisted
	*/
	default void persistOfferedCandidate(OfferedCandidate offeredCandidate) {
		
		this.save(OfferedCandidateEntity.convertToEntity(offeredCandidate, Optional.empty()));
	}

	/**
	* Updates an existing OfferedCandidate
	* @param offeredCandidateId - Unique Id of the OfferedCandidate
	* @param offeredCandidate   - OfferedCandidate to be updated
	*/
	default void updateExistingOfferedCandidate(UUID offeredCandidateId, OfferedCandidate offeredCandidate) {
		
		OfferedCandidateEntity originalEntity 	= this.findById(offeredCandidateId).orElseThrow(() -> new RuntimeException("Unknown Offered Candidate"));
		OfferedCandidateEntity updatedEntity 	= OfferedCandidateEntity.convertToEntity(offeredCandidate, Optional.of(originalEntity));
		
		this.save(updatedEntity);
	}
	
}
