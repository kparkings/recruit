package com.arenella.recruit.listings.dao;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.listings.beans.ListingViewedEvent;

/**
* Defines repository for working with ListingViewedEventEntity
* @author K Parkings
*/
public interface ListingStatisticsDao extends CrudRepository<ListingViewedEventEntity,String>{

	/**
	* Events for recruiter - ONLY FOR INTERNAL USE IN PERSISTENCE LAYER
	* @param recruiterId - Id of recruiter
	* @return Recruiters Events
	*/
	@Query("from ListingViewedEventEntity evt inner join ListingEntity le on evt.listingId = le.listingId where le.ownerId = :recruiterId")
	Set<ListingViewedEventEntity> fetchEventsEntitiesForRecruiter(String recruiterId);
	
	/**
	* Events for recruiter
	* @param recruiterId - Id of recruiter
	* @return Recruiters Events
	*/
	default Set<ListingViewedEvent> fetchEventsForRecruiter(String recruiterId){
		return this.fetchEventsEntitiesForRecruiter(recruiterId)
				.stream()
				.map(ListingViewedEventEntity::convertFromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	
}
