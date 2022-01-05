package com.arenella.recruit.recruiters.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.entities.RecruiterEntity;

/**
* Repository for working with Recruiter Entities
* @author K Parkings
*/
public interface RecruiterDao extends CrudRepository<RecruiterEntity, String>{

	default Optional<Recruiter> findRecruiterById(String recruiterId) {
		
		Optional<RecruiterEntity> entityOpt = this.findByUserIdIgnoreCase(recruiterId);
		
		if (entityOpt.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(RecruiterEntity.convertFromEntity(entityOpt.get()));
		
	}
	
	Optional<RecruiterEntity> findByUserIdIgnoreCase(String userId);
}
