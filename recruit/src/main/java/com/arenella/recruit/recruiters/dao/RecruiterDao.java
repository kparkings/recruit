package com.arenella.recruit.recruiters.dao;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.recruiters.beans.OfferedCandidateAPIOutbound.RecruiterDetails;
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
	* Returns Recruiters matching the provided Id's
	* @param ids - Id's of Recruiters to fetch
	* @return Recruiters
	*/
	default Set<Recruiter> fetchRecruiterByIds(Set<String> ids){
		return this.findByIds(ids).stream().map(RecruiterEntity::convertFromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	@Query("FROM RecruiterEntity where userId in :ids " )
	List<RecruiterEntity> findByIds(Set<String> ids);
	
	/**
	* Attempts to find a Recruiter by its Id regardless of the case 
	* used
	* @param userId - Unique id of recruiter to search for
	* @return If found the associated Recruiter
	*/
	Optional<RecruiterEntity> findByUserIdIgnoreCase(String userId);
	
	/**
	* Attempts to find a Recruiter by its email address
	* @param email - email of the recruiter
	* @return If found the associated Recruiter
	*/
	Set<RecruiterEntity> findByEmail(String email);
	
	/**
	* Attempts to find a Recruiter by its SubscriptionId
	* @param id - id of the Subscription
	* @return If found the associated Recruiter
	*/
	@Query("SELECT RecruiterEntity FROM RecruiterEntity e left join e.subscriptions s where s.subscriptionId = :subscriptionId " )
	Optional<RecruiterEntity> getSubscriptionById(UUID subscriptionId);
	

	/**
	* Retrieves Recruiter details based upon the id of the Recruiter
	* @param recruiterId - Unique identifier of the Recruiter
	* @return recruiter Details
	*/
	default RecruiterDetails findRecruiterDetailsById(String recruiterId) {
	
		RecruiterEntity entity = this.findById(recruiterId).orElseThrow(()-> new RuntimeException("Unknown Recruiter " + recruiterId));
	
		return new RecruiterDetails(entity.getUserId(), entity.getFirstName() + " " + entity.getSurname(), entity.getCompanyName(), entity.getEmail());
		
	}
	
	/**
	* Retrieves recruiters with a matching email address
	* @param email - email address of the user
	* @return Recruiters
	*/
	default Set<Recruiter> findRecruitersByEmail(String email){
		return this.findByEmail(email).stream().map(RecruiterEntity::convertFromEntity).collect(Collectors.toSet());
	}
	
	/**
	* If present returns the Recruiter who owns the given subscription
	* @param subscriptionId - Unique Id of Subscription
	* @return owning Recruiter
	*/
	default Optional<Recruiter> findRecruitersBySubscriptionId(UUID subscriptionId){
		
		Optional<RecruiterEntity> entity = this.getSubscriptionById(subscriptionId);
		
		if(entity.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(RecruiterEntity.convertFromEntity(entity.get()));
	}
	
	
	default void saveRecruiter(Recruiter recruiter) {
		this.save(RecruiterEntity.convertToEntity(recruiter, Optional.empty()));
	}
	
	default void deleteRecruiter(Recruiter recruiter) {
		RecruiterEntity entity = RecruiterEntity.convertToEntity(recruiter, Optional.empty());
		this.save(entity);
		this.delete(entity);
		
	}
	
}
