package com.arenella.recruit.recruiters.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.dao.ListingViewedEventEntity;

/**
* Unit tests for the ListingViewedEventEntity Class
* @author K Parkings
*/
class ListingViewedEventEntityTest {

	/**
	* Tests instantiation via Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		final UUID 				eventId 		= UUID.randomUUID();
		final UUID 				listingId 		= UUID.randomUUID();
		final LocalDateTime 	created 		= LocalDateTime.of(2021,12,12, 00,00,01);
		
		ListingViewedEventEntity entity = ListingViewedEventEntity
												.builder()
													.eventId(eventId)
													.listingId(listingId)
													.created(created)
												.build();
		
		assertEquals(eventId, 		entity.getEventId());
		assertEquals(listingId, 	entity.getListingId());
		assertEquals(created, 		entity.getCreated());
		
	}
	
	/**
	* Tests conversion of Domain representation to Entity representation
	* @throws Exception
	*/
	@Test
	void testConvertToEntity() {
		
		final UUID 				eventId 		= UUID.randomUUID();
		final UUID 				listingId 		= UUID.randomUUID();
		final LocalDateTime 	created 		= LocalDateTime.of(2021,12,12, 00,00,01);
		
		ListingViewedEvent event = ListingViewedEvent
												.builder()
													.eventId(eventId)
													.listingId(listingId)
													.created(created)
												.build();

		ListingViewedEventEntity entity = ListingViewedEventEntity.convertToEntity(event);
		
		assertEquals(eventId, 		entity.getEventId());
		assertEquals(listingId, 	entity.getListingId());
		assertEquals(created, 		entity.getCreated());
		
	}

	/**
	* Tests conversion of Entity representation to Domain representation
	* @throws Exception
	*/
	@Test
	void testConvertFromEntity() {
		
		final UUID 				eventId 		= UUID.randomUUID();
		final UUID 				listingId 		= UUID.randomUUID();
		final LocalDateTime 	created 		= LocalDateTime.of(2021,12,12, 00,00,01);
		
		ListingViewedEventEntity entity = ListingViewedEventEntity
															.builder()
																.eventId(eventId)
																.listingId(listingId)
																.created(created)
															.build();

		ListingViewedEvent event = ListingViewedEventEntity.convertFromEntity(entity);
		
		assertEquals(eventId, 		event.getEventId());
		assertEquals(listingId, 	event.getListingId());
		assertEquals(created, 		event.getCreated());
		
	}
	
}