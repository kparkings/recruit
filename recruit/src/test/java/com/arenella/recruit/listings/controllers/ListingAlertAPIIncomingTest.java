package com.arenella.recruit.listings.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingAlert;

/**
* Unit tests for the ListingAlertAPIIncomming class
* @author K Parkings
*/
public class ListingAlertAPIIncomingTest {

	private static final String 				EMAIL			= "admin@arenella-ict.com";
	private static final Listing.listing_type 	CONTRACT_TYPE 	= Listing.listing_type.BOTH;
	private static final Set<Listing.Country> 	COUNTRIES 		= Set.of(Listing.Country.NETHERLANDS);
	private static final Set<Listing.TECH> 		CATEGORIES 		= Set.of(Listing.TECH.ARCHITECT);
	
	/**
	* Tests construction via builder
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		ListingAlertAPIIncoming alert = 
				ListingAlertAPIIncoming
					.builder()
						.categories(CATEGORIES)
						.contractType(CONTRACT_TYPE)
						.countries(COUNTRIES)
						.email(EMAIL)
					.build();
					
		assertEquals(CONTRACT_TYPE, alert.getContractType());
		assertEquals(EMAIL, 		alert.getEmail());
		
		alert.getCategories().stream().filter(c -> c == Listing.TECH.ARCHITECT).findAny().orElseThrow();
		alert.getCountries().stream().filter(c -> c == Listing.Country.NETHERLANDS).findAny().orElseThrow();
		
	}
	
	/**
	* Tests defensive coding practices are being used
	* @throws Exception
	*/
	@Test
	public void testDefaults() throws Exception {
		
		ListingAlertAPIIncoming alert = 
				ListingAlertAPIIncoming
					.builder().build();
		
		assertNotNull(alert.getCategories());
		assertNotNull(alert.getCountries());
		
	}
	
	/**
	* Tests conversion from Incoming API to Domain 
	* representation of Alert
	* @throws Exception
	*/
	@Test
	public void testConvertToDomain() throws Exception{
	
		ListingAlertAPIIncoming alert = 
				ListingAlertAPIIncoming
					.builder()
						.categories(CATEGORIES)
						.contractType(CONTRACT_TYPE)
						.countries(COUNTRIES)
						.email(EMAIL)
					.build();
		
		ListingAlert domain = ListingAlertAPIIncoming.convertToDomain(alert);
		
		assertEquals(CONTRACT_TYPE, domain.getContractType());
		assertEquals(EMAIL, 		domain.getEmail());
		
		domain.getCategories().stream().filter(c -> c == Listing.TECH.ARCHITECT).findAny().orElseThrow();
		domain.getCountries().stream().filter(c -> c == Listing.Country.NETHERLANDS).findAny().orElseThrow();
		
		assertNull(domain.getId());
		assertTrue(domain.getUserId().isEmpty());
		assertNull(domain.getCreated());
	}
	
}