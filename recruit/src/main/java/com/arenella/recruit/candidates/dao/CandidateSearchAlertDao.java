package com.arenella.recruit.candidates.dao;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.beans.CandidateSearchAlert;
import com.arenella.recruit.candidates.entities.CandidateSearchAlertEntity;

/**
* Repository for interating with CandidateSearchAlert's
* @author K Parkings
*/
public interface CandidateSearchAlertDao extends CrudRepository<CandidateSearchAlertEntity, UUID>{

	/**
	* Saves an Alert
	* @param alert
	*/
	public default void saveAlert(CandidateSearchAlert alert) {
		this.save(CandidateSearchAlertEntity.convertToEntity(alert));
	}

	@Query("from CandidateSearchAlertEntity where recruiterId = :recruiterId")
	Set<CandidateSearchAlertEntity> fetchAlertEntitiesByRecruiterId(String recruiterId);
	
	@Query("from CandidateSearchAlertEntity where alertId = :alertId")
	Optional<CandidateSearchAlertEntity> fetchAlertEntityById(UUID alertId);
	
	/**
	* Returns alerts for a specific recruiter
	* @return All alerts for a specific recruiter
	*/
	public default Set<CandidateSearchAlert> fetchAlertsByRecruiterId(String recruiterId){
		return fetchAlertEntitiesByRecruiterId(recruiterId)
				.stream()
				.map(CandidateSearchAlertEntity::convertFromEntity)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Returns all alerts [ NOTE: Need to page is this becomes popular to avoid speed/memory issues)
	* @return All alerts
	*/
	public default Set<CandidateSearchAlert> fetchAlerts(){
		return StreamSupport.stream(this.findAll().spliterator(), false)
				.map(CandidateSearchAlertEntity::convertFromEntity)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Returns a Search Alert matching the id
	* @param id - unique id of the SearchAlert
	* @return SearchAlert with requested Id
	*/
	public default Optional<CandidateSearchAlert> getchAlertById(UUID id) {
		
		Optional<CandidateSearchAlertEntity> entity = this.fetchAlertEntityById(id);
		
		if ( entity.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(CandidateSearchAlertEntity.convertFromEntity(entity.get()));
	}
	
}
