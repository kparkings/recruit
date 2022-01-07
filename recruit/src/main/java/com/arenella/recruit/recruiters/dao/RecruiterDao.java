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
	
	/**
	* Attempts to find a Recruiter but its Id regardless of the case 
	* used
	* @param userId - Unique id of recruiter to search for
	* @return If found the associated Recruiter
	*/
	Optional<RecruiterEntity> findByUserIdIgnoreCase(String userId);
}
