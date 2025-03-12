package com.arenella.recruit.recruiters.listings.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.ListingViewedEvent;

/**
* Unit tests for the ListingViewedEventClass
* @author K Parkings
*/
class ListingViewedEventTest {

	/**
	* Tests that by default if not eventId is provided 
	* one is provided by the Builder
	* @throws Exception
	*/
	@Test
	void testBuilder_defaults() {
		
		ListingViewedEvent event = ListingViewedEvent.builder().build();
		
		assertTrue(event.getEventId() instanceof UUID);
		assertTrue(event.getCreated() instanceof LocalDateTime);
		
	}
	
	/**
	* Tests instantiation via Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		final UUID 				eventId 		= UUID.randomUUID();
		final UUID 				listingId 		= UUID.randomUUID();
		final LocalDateTime 	created 		= LocalDateTime.of(2021,12,12, 00,00,01);
		final String			title			= "aTitle";
		ListingViewedEvent event = ListingViewedEvent
												.builder()
													.eventId(eventId)
													.listingId(listingId)
													.title(title)
													.created(created)
												.build();
		
		assertTrue(event.getEventId() instanceof UUID);
		
		assertEquals(eventId, 		event.getEventId());
		assertEquals(listingId, 	event.getListingId());
		assertEquals(created, 		event.getCreated());
		assertEquals(title, 		event.getTitleOrLisingId());
			
	}
	
	/**
	* Tests title is returned or where not present 
	* the id
	* @throws Exception
	*/
	@Test
	void testGetTitle() {
		
		final String 			title 			= "Java Developer";
		final UUID 				eventId 		= UUID.randomUUID();
		final UUID 				listingId 		= UUID.randomUUID();
		final LocalDateTime 	created 		= LocalDateTime.of(2021,12,12, 00,00,01);
		
		ListingViewedEvent event = ListingViewedEvent
												.builder()
													.eventId(eventId)
													.listingId(listingId)
													.created(created)
												.build();
		
		assertEquals(listingId.toString(), event.getTitleOrLisingId());
		
		event.setTitle(title);
		
		assertEquals(title, event.getTitleOrLisingId());
		
	}
	
}