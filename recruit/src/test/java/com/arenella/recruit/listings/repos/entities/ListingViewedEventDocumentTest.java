package com.arenella.recruit.listings.repos.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.ListingViewedEvent;

/**
* Unit tests for the ListingViewedEventDocument class 
*/
class ListingViewedEventDocumentTest {

	private static final UUID 			ID 			= UUID.randomUUID();
	private static final LocalDateTime 	CREATED 	= LocalDateTime.of(2025, 3, 1, 19, 13);
	
	/**
	* Test construction via Builder
	*/
	@Test
	void testCreationViaBuilder() {
	
		ListingViewedEventDocument document = ListingViewedEventDocument
				.builder()
					.eventId(ID)
					.created(CREATED)
				.build();
		
		assertEquals(ID, document.getEventId());
		assertEquals(CREATED, document.getCreated());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* of a ListingViewedEvent
	*/
	@Test
	void testToEntity() {
		
		ListingViewedEvent event = ListingViewedEvent
				.builder()
					.eventId(ID)
					.created(CREATED)
				.build();
		
		assertEquals(ID, event.getEventId());
		assertEquals(CREATED, event.getCreated());
		
		ListingViewedEventDocument document = ListingViewedEventDocument.toDocument(event);
		
		assertEquals(ID, document.getEventId());
		assertEquals(CREATED, document.getCreated());
			
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* of a ListingViewedEvent
	*/
	@Test
	void testFromEntity() {
		
		ListingViewedEventDocument document = ListingViewedEventDocument
				.builder()
					.eventId(ID)
					.created(CREATED)
				.build();
		
		assertEquals(ID, document.getEventId());
		assertEquals(CREATED, document.getCreated());
		
		ListingViewedEvent event = ListingViewedEventDocument.fromEntity(document); 
		
		assertEquals(ID, event.getEventId());
		assertEquals(CREATED, event.getCreated());
		
	}
	
}