package com.arenella.recruit.listings.dao;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.ListingAlertSentEvent;

/**
* Unit tests for the ListingAlertSentEventEntity class
*/
class ListingAlertSentEventEntityTest {

	private static final UUID 			ID 			= UUID.randomUUID();
	private static final UUID 			LISTING_ID 	= UUID.randomUUID();
	private static final LocalDate 		ALERT_SENT 	= LocalDate.of(2025, 11, 7);
	
	/**
	* Tests construction
	*/
	@Test
	void testConstructor() {
		
		ListingAlertSentEventEntity event = new ListingAlertSentEventEntity(ID, LISTING_ID, ALERT_SENT);
		
		assertEquals(ID, 			event.getId());
		assertEquals(LISTING_ID, 	event.getListingId());
		assertEquals(ALERT_SENT, 	event.getAlertSent());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	*/
	@Test
	void testConversionFromEntity() {
		
		ListingAlertSentEventEntity entity = new ListingAlertSentEventEntity(ID, LISTING_ID, ALERT_SENT);
		
		ListingAlertSentEvent event = ListingAlertSentEventEntity.fromEntity(entity);
		
		assertEquals(ID, 			event.id());
		assertEquals(LISTING_ID, 	event.listingId());
		assertEquals(ALERT_SENT, 	event.alertSent());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	*/
	@Test
	void testConversionToEntity() {
		
		ListingAlertSentEvent event = new ListingAlertSentEvent(ID, LISTING_ID, ALERT_SENT);
		
		ListingAlertSentEventEntity entity = ListingAlertSentEventEntity.toEntity(event);
		
		assertEquals(ID, 			entity.getId());
		assertEquals(LISTING_ID, 	entity.getListingId());
		assertEquals(ALERT_SENT, 	entity.getAlertSent());
		
	}
	
}