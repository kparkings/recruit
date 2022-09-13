package com.arenella.recruit.recruiters.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.recruiters.beans.OpenPosition;
import com.arenella.recruit.recruiters.entities.OpenPositionEntity;

/**
* Defines behaviour relating to Suppy and Demand Entities at the 
* Persistence level
* @author K Parkings
*/
public interface SupplyAndDemanDao extends CrudRepository<OpenPositionEntity, UUID>{

	/**
	* Persists an OpenPosition
	* @param openPosition - Open Position to persist
	*/
	default void persistOpenPositiion(OpenPosition openPosition) {
		this.save(OpenPositionEntity.convertToEntity(openPosition, Optional.empty()));
	}
	
	/**
	* Returns an existing OpenPosition
	* @param openPositionId - Unique identifier of OpenPosition to return
	* @return OpenPosition
	*/
	default OpenPosition findByOpenPositionId(UUID openPositionId) {
		
		OpenPositionEntity entity = this.findById(openPositionId).orElseThrow(() -> new RuntimeException("Unknown Open Position"));
		
		return OpenPositionEntity.convertFromEntity(entity);
		
	}

	/**
	* Updates an existing OpenPosition
	* @param openPositionId - Unique identifier of OpenPosition to update
	* @param openPosition	- New version of the OpenPosition
	*/
	default void updateExistingOpenPosition(UUID openPositionId, OpenPosition openPosition) {
	
		OpenPositionEntity originalEntity 	= this.findById(openPositionId).orElseThrow(() -> new RuntimeException("Unknown Open Position"));
		OpenPositionEntity updatedEntity 	= OpenPositionEntity.convertToEntity(openPosition, Optional.of(originalEntity));
		
		this.save(updatedEntity);
		
	}
	
}