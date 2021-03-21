package com.arenella.recruit.dao;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.entities.CandidateEntity;

/**
* Defines DAO functions for interacting with CandidateEntity objects
* @author K Parkings
*/
public interface CandidateDao extends CrudRepository<CandidateEntity, String> {

	
}
