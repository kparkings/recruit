package com.arenella.recruit.candidate.dao;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidate.entities.CandidateEntity;

public interface CandidateDao extends CrudRepository<CandidateEntity, String> {

	
}
