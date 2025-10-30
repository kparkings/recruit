package com.arenella.recruit.listings.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.ListingContactRequestEvent.CONTACT_USER_TYPE;

/**
* Unit tests for the ListingContactRequestEvent class 
*/
class ListingContactRequestEventTest {

	private static final UUID 					EVENT_ID 		= UUID.randomUUID();
	private static final UUID 					LISTING_ID 		= UUID.randomUUID();
	private static final CONTACT_USER_TYPE 		USER_TYPE	 	= CONTACT_USER_TYPE.REGISTERED;
	private static final LocalDateTime 			CREATED 		= LocalDateTime.of(2025, 10, 30, 17, 37, 1);
	
	/**
	* Tests construction via Builder 
	*/
	@Test
	void testConstruction() {
		
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
		
	}
	
}
