package com.arenella.recruit.campaign.dao;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.campaigns.beans.Candidate;
import com.arenella.recruit.campaigns.entities.CandidateEntity;

@Repository
public interface CandidateEntityDao extends ListCrudRepository<CandidateEntity, String>{

	/**
	* Saves a Candidate
	* @param candidate - Candidate to persist
	*/
	default void saveCandidate(Candidate candidate) {
		this.save(CandidateEntity.toEntity(candidate));
	}

	/**
	* If present returns the Candidate references by the ID
	* @param candidateId - Unique id of the Candidate
	* @return
	*/
	default Optional<Candidate> findCandidateById(String candidateId){
		return this.findById(candidateId).map(CandidateEntity::fromEntity);
	}
	
}
