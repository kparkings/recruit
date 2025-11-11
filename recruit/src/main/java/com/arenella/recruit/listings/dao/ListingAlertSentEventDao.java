package com.arenella.recruit.listings.dao;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.repository.ListCrudRepository;

import com.arenella.recruit.listings.beans.ListingAlertSentEvent;

public interface ListingAlertSentEventDao extends ListCrudRepository<ListingAlertSentEventEntity, UUID> {

	/**
	* Returns events relating to alerts being sent to users for a specified
	* group of listings
	* @param listingIds - Listings to return events for
	* @return Events for listings
	*/
	List<ListingAlertSentEventEntity> findByListingIdIn(Set<UUID> listingIds);
	
	/**
	* Returns events relating to alerts being sent to users for a specified
	* group of listings
	* @param listingIds - Listings to return events for
	* @return Events for listings
	*/
	default Set<ListingAlertSentEvent> fetchEventForListings(Set<UUID> listingIds) {
		return this.findByListingIdIn(listingIds).stream().map(ListingAlertSentEventEntity::fromEntity).collect(Collectors.toSet());
	}

	/**
	* Persists an event
	* @param event - persists event
	*/
	default void saveEvent(ListingAlertSentEvent event) {
		this.save(ListingAlertSentEventEntity.toEntity(event));
	};
	
}
