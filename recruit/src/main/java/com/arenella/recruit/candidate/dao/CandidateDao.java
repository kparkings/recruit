package com.arenella.recruit.candidate.dao;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidate.entities.CandidateEntity;

/**
* Defines DAO functions for interacting with CandidateEntity objects
* @author K Parkings
*/
public interface CandidateDao extends CrudRepository<CandidateEntity, String> {

	
}
