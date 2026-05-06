package com.arenella.recruit.campaign.dao;

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
	
}
