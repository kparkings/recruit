package com.arenella.recruit.candidates.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.entities.PendingCandidateEntity;

/**
* Repository for PendingCandidate entites
* @author K Parkings
*/
public interface PendingCandidateDao extends CrudRepository<PendingCandidateEntity, UUID>{

}
