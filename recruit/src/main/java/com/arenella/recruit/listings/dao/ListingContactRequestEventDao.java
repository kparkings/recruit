package com.arenella.recruit.listings.dao;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import com.arenella.recruit.listings.beans.ListingContactRequestEvent;

/**
* Repository for ListingContactRequestEvent objects
*/
public interface ListingContactRequestEventDao extends ListCrudRepository<ListingContactRequestEventEntity, UUID> {

	List<ListingContactRequestEventEntity> findByListingIdIn(Set<UUID> listingIds);
	
	/**
	* Persists a new Event
	* @param event - Event to persist
	*/
	default void persistEvent(ListingContactRequestEvent event) {
		this.save(ListingContactRequestEventEntity.toEntity(event));
	}
	
	/**
	* Retrieves Events associated with any of the Listings specified
	* @param listingIds - Id's to Listings to return events for 
	* @return Events associated with the Listings
	*/
	default Set<ListingContactRequestEvent> fetchEventForListings(Set<UUID> listingIds) {
		return this.findByListingIdIn(listingIds)
				.stream()
				.map(ListingContactRequestEventEntity::fromEntity)
				.collect(Collectors.toSet());
	}
	
}