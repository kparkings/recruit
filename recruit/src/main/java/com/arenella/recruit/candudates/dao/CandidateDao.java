package com.arenella.recruit.candudates.dao;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.entities.CandidateEntity;

/**
* Defines DAO functions for interacting with CandidateEntity objects
* @author K Parkings
*/
public interface CandidateDao extends CrudRepository<CandidateEntity, String> {

	
}
