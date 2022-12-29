package com.arenella.recruit.candidates.dao;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
	* @return
	*/
	public default Set<CandidateSearchAlert> fetchAlertsByRecruiterId(String recruiterId){
		return fetchAlertEntitiesByRecruiterId(recruiterId)
				.stream()
				.map(ae -> CandidateSearchAlertEntity.convertFromEntity(ae))
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
