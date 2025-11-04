package com.arenella.recruit.listings.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the ListingStatContactRequests class 
*/
class ListingStatContactRequestsTest {

	private static final UUID	LISTING_ID 		= UUID.randomUUID();
	private static final int 	REGISTERED 		= 22;
	private static final int 	UNREGISTERED 	= 33;
	/**
	* Tests construction via builder 
	*/
	@Test
	void testConstructionViaBuilder() {
		
		ListingStatContactRequests stat = ListingStatContactRequests
				.builder()
					.listingId(LISTING_ID)
					.registeredUserRequests(REGISTERED)
					.unregisteredUserRequests(UNREGISTERED)
				.build();
		
		assertEquals(LISTING_ID, 	stat.getListingId());
		assertEquals(REGISTERED, 	stat.getRegisteredUserRequests());
		assertEquals(UNREGISTERED, 	stat.getUnregisteredUserRequests());
		
	}
	
	/**
	* Tests incrementing contact request counts
	*/
	@Test
	void testIncrementContactRequests() {
		
		ListingStatContactRequests stat = ListingStatContactRequests
				.builder()
					.listingId(LISTING_ID)
					.registeredUserRequests(REGISTERED)
					.unregisteredUserRequests(UNREGISTERED)
				.build();
		
		assertEquals(LISTING_ID, 	stat.getListingId());
		assertEquals(REGISTERED, 	stat.getRegisteredUserRequests());
		assertEquals(UNREGISTERED, 	stat.getUnregisteredUserRequests());
		
		stat.increaseRegisteredCount();
		stat.increaseRegisteredCount();
		stat.increaseRegisteredCount();
		
		stat.increaseUnregisteredCount();
		stat.increaseUnregisteredCount();
		stat.increaseUnregisteredCount();
		stat.increaseUnregisteredCount();
		stat.increaseUnregisteredCount();
		
		assertEquals(REGISTERED + 3, 	stat.getRegisteredUserRequests());
		assertEquals(UNREGISTERED + 5, 	stat.getUnregisteredUserRequests());
		
	}
	
}
