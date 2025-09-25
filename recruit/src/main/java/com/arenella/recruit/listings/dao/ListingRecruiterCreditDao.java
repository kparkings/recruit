package com.arenella.recruit.listings.dao;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.listings.beans.RecruiterCredit;

/**
* Respository for RecruiterCredit Entities
* @author K Parkings
*/
public interface ListingRecruiterCreditDao extends CrudRepository<ListingRecruiterCreditEntity, String>{

	/**
	* If found returns the Credit information for the Recruiter
	* @param recruiterId - Unique id of the Recruiter
	* @return 
	*/
	default Optional<RecruiterCredit> getByRecruiterId(String recruiterId){
		Optional<ListingRecruiterCreditEntity> entity =  this.findById(recruiterId);
		
		if (entity.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(ListingRecruiterCreditEntity.convertFromEntity(entity.get()));
	}
	
	/**
	* Saves/Updates the RecruiterCredit
	* @param recruiterCredit - RecruiterCredit to be persisted
	*/
	default void persist(RecruiterCredit recruiterCredit) {
		ListingRecruiterCreditEntity entity = ListingRecruiterCreditEntity.convertToEntity(recruiterCredit);
		
		this.save(entity);
	}
	
	/**
	* Saves/Updates the RecruiterCredit
	* @param recruiterCredit - RecruiterCredit to be persisted
	*/
	default Set<RecruiterCredit> fetchRecruiterCredits() {
		return StreamSupport.stream(this.findAll().spliterator(), false).map(ListingRecruiterCreditEntity::convertFromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	* Persit's a collection of RecruiterCredits
	* @param credits
	*/
	default void saveAll(Set<RecruiterCredit> credits) {
		credits.stream().map(ListingRecruiterCreditEntity::convertToEntity).forEach(this::save);
	}
	
}
