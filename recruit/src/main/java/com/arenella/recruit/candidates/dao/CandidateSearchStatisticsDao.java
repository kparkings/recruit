package com.arenella.recruit.candidates.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.entities.CandidateSearchEventEntity;

/**
* Functionality for persisting Statistics related to Candidates
* @author K Parkings
*/
public interface CandidateSearchStatisticsDao extends CrudRepository<CandidateSearchEventEntity, UUID>{


	
}
