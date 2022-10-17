package com.arenella.recruit.recruiters.dao;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

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

	/**
	* Retrieves the OfferedCandidates
	* @return OfferedCandidates
	*/
	default Set<OfferedCandidate> findAllOfferedCandidates(){
		return  StreamSupport
			.stream(this.findAll().spliterator(), false)
			.sorted(Comparator.comparing(OfferedCandidateEntity::getCreated).reversed())
			.map(entity -> OfferedCandidateEntity.convertFromEntity(entity))
			.collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Retrieves the OfferedCandidates for a specific Recruiter
	* @return OfferedCandidates
	*/
	default Set<OfferedCandidate> findAllOfferedCandidatesByRecruiterId(String recruiterId){
		return StreamSupport
			.stream(this.findAllByRecruiterId(recruiterId).spliterator(), false)
			.sorted(Comparator.comparing(OfferedCandidateEntity::getCreated).reversed())
			.map(entity -> OfferedCandidateEntity.convertFromEntity(entity))
			.collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Returns all available OfferedCandidates for a specific recruiter
	* @return OfferedCandidates for Recruiter
	*/
	@Query("FROM OfferedCandidateEntity where recruiterId = :id")
	Set<OfferedCandidateEntity> findAllByRecruiterId(@Param("id") String recruiterId);
	
}