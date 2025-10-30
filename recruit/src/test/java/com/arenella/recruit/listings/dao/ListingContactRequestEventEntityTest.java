package com.arenella.recruit.listings.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.ListingContactRequestEvent;
import com.arenella.recruit.listings.beans.ListingContactRequestEvent.CONTACT_USER_TYPE;

/**
* Unit tests for the ListingContactRequestEventEntity class 
*/
class ListingContactRequestEventEntityTest {

	private static final UUID 					EVENT_ID 		= UUID.randomUUID();
	private static final UUID 					LISTING_ID 		= UUID.randomUUID();
	private static final CONTACT_USER_TYPE 		USER_TYPE	 	= CONTACT_USER_TYPE.REGISTERED;
	private static final LocalDateTime 			CREATED 		= LocalDateTime.of(2025, 10, 30, 17, 37, 1);
	
	/**
	* Tests construction via Builder 
	*/
	@Test
	void testConstructor() {
		
		ListingContactRequestEventEntity entity = ListingContactRequestEventEntity
				.builder()
					.eventId(EVENT_ID)
					.listingId(LISTING_ID)
					.userType(USER_TYPE)
					.created(CREATED)
				.build();
		
		assertEquals(EVENT_ID, 		entity.getEventId());
		assertEquals(LISTING_ID, 	entity.getListingId());
		assertEquals(USER_TYPE, 	entity.getUserType());
		assertEquals(CREATED, 		entity.getCreated());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation 
	*/
	@Test
	void testFromEntity() {
		
		ListingContactRequestEventEntity entity = ListingContactRequestEventEntity
				.builder()
					.eventId(EVENT_ID)
					.listingId(LISTING_ID)
					.userType(USER_TYPE)
					.created(CREATED)
				.build();
		
		assertEquals(EVENT_ID, 		entity.getEventId());
		assertEquals(LISTING_ID, 	entity.getListingId());
		assertEquals(USER_TYPE, 	entity.getUserType());
		assertEquals(CREATED, 		entity.getCreated());
		
		ListingContactRequestEvent event = ListingContactRequestEventEntity.fromEntity(entity);
		
		assertEquals(EVENT_ID, 		event.getEventId());
		assertEquals(LISTING_ID, 	event.getListingId());
		assertEquals(USER_TYPE, 	event.getUserType());
		assertEquals(CREATED, 		event.getCreated());
		
	}
	
	/**
	* Tests conversion from domain to Entity representation 
	*/
	@Test
	void testToEntity() {
		
		ListingContactRequestEvent event = ListingContactRequestEvent
				.builder()
					.eventId(EVENT_ID)
					.listingId(LISTING_ID)
					.userType(USER_TYPE)
					.created(CREATED)
				.build();
		
		assertEquals(EVENT_ID, 		event.getEventId());
		assertEquals(LISTING_ID, 	event.getListingId());
		assertEquals(USER_TYPE, 	event.getUserType());
		assertEquals(CREATED, 		event.getCreated());
		
		ListingContactRequestEventEntity entity = ListingContactRequestEventEntity.toEntity(event);
		
		assertEquals(EVENT_ID, 		entity.getEventId());
		assertEquals(LISTING_ID, 	entity.getListingId());
		assertEquals(USER_TYPE, 	entity.getUserType());
		assertEquals(CREATED, 		entity.getCreated());
		
	}
	
}