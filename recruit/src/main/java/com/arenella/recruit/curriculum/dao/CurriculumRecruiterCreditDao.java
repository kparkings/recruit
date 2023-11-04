package com.arenella.recruit.curriculum.dao;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.curriculum.beans.RecruiterCredit;
import com.arenella.recruit.curriculum.entity.CurriculumRecruiterCreditEntity;

/**
* Respository for RecruiterCredit Entities
* @author K Parkings
*/
public interface CurriculumRecruiterCreditDao extends CrudRepository<CurriculumRecruiterCreditEntity, String>{

	/**
	* If found returns the Credit information for the Recruiter
	* @param recruiterId - Unique id of the Recruiter
	* @return 
	*/
	default Optional<RecruiterCredit> getByRecruiterId(String recruiterId){
		Optional<CurriculumRecruiterCreditEntity> entity =  this.findById(recruiterId);
		
		if (entity.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(CurriculumRecruiterCreditEntity.convertFromEntity(entity.get()));
	}
	
	/**
	* Saves/Updates the RecruiterCredit
	* @param recruiterCredit - RecruiterCredit to be persisted
	*/
	default void persist(RecruiterCredit recruiterCredit) {
		CurriculumRecruiterCreditEntity entity = CurriculumRecruiterCreditEntity.convertToEntity(recruiterCredit);
		
		this.save(entity);
	}
	
	/**
	* Saves/Updates the RecruiterCredit
	* @param recruiterCredit - RecruiterCredit to be persisted
	*/
	default Set<RecruiterCredit> fetchRecruiterCredits() {
		return StreamSupport.stream(this.findAll().spliterator(), false).map(CurriculumRecruiterCreditEntity::convertFromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	* Persit's a collection of RecruiterCredits
	* @param credits
	*/
	default void saveAll(Set<RecruiterCredit> credits) {
		credits.stream().map(recruiterCredit -> CurriculumRecruiterCreditEntity.convertToEntity(recruiterCredit)).forEach(entity -> this.save(entity));
	}
	
}