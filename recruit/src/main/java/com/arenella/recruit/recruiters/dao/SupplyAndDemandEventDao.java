package com.arenella.recruit.recruiters.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent;
import com.arenella.recruit.recruiters.entities.SupplyAndDemandEventEntity;

/**
* Repository for SupplyAndDemandEventEntity 
* @author K Parkings
*/
@Repository
public interface SupplyAndDemandEventDao extends CrudRepository<SupplyAndDemandEventEntity,UUID>{

	/**
	* Persists an Event
	* @param event - Event to Persist
	*/
	default void persistEvent(SupplyAndDemandEvent event) {
		
		this.save(SupplyAndDemandEventEntity.convertToEntity(event));
		
	}
	
}
