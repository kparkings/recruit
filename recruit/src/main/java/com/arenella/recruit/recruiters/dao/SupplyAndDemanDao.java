package com.arenella.recruit.recruiters.dao;

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
		this.save(OpenPositionEntity.convertToEntity(openPosition));
	}
	
}
