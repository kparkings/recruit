package com.arenella.recruit.recruiters.dao;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent;
import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent.EventType;
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
	
	/**
	* Returns entities representation of Events  
	* @param after 	- Date >= which the event must have been created
	* @param type	- Type of Event  
	* @return
	*/
	@Query("from SupplyAndDemandEventEntity where created >= :after and type = :type")
	public Set<SupplyAndDemandEventEntity> fetchEvents(LocalDateTime after, EventType type);
	
	/**
	* Returns events from a given period of a specified type
	* @param after - Time after which the Event must have been created
	* @param type  - Type of Event
	* @return Events in period
	*/
	default Set<SupplyAndDemandEvent> fetchThisWeeksEvents(LocalDateTime after, EventType type){
		return this.fetchEvents(after, type).stream().map(e -> SupplyAndDemandEventEntity.convertFromEntity(e)).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	
}