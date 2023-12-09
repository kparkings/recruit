package com.arenella.recruit.recruiters.listings.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingAlert;

/**
* Unit tests for the ListingAlert class
* @author K Parkings
*/
public class ListingAlertTest {

	private static final UUID 					ID				= UUID.randomUUID();
	private static final Long 					USER_ID			= 1L;
	private static final String 				EMAIL			= "kparkings@gmail.com";
	private static final LocalDate 				CREATED			= LocalDate.of(2023, 12, 9);
	private static final Listing.listing_type 	CONTRACT_TYPE 	= Listing.listing_type.BOTH;
	private static final Set<Listing.country> 	COUNTRIES 		= Set.of(Listing.country.NETHERLANDS);
	private static final Set<Listing.TECH> 		CATEGORIES 		= Set.of(Listing.TECH.ARCHITECT);
	
	/**
	* Tests construction via builder
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		ListingAlert alert = 
				ListingAlert
					.builder()
						.categories(CATEGORIES)
						.contractType(CONTRACT_TYPE)
						.countries(COUNTRIES)
						.created(CREATED)
						.email(EMAIL)
						.id(ID)
						.userId(USER_ID)
					.build();
					
		assertEquals(CONTRACT_TYPE, alert.getContractType());
		assertEquals(CREATED, 		alert.getCreated());
		assertEquals(EMAIL, 		alert.getEmail());
		assertEquals(ID, 			alert.getId());
		assertEquals(USER_ID, 		alert.getUserId().get());
		
		alert.getCategories().stream().filter(c -> c == Listing.TECH.ARCHITECT).findAny().orElseThrow();
		alert.getCountries().stream().filter(c -> c == Listing.country.NETHERLANDS).findAny().orElseThrow();
		
	}
	
	/**
	* Tests defensive coding practices are being used
	* @throws Exception
	*/
	@Test
	public void testDefaults() throws Exception {
		
		ListingAlert alert = 
				ListingAlert
					.builder().build();
		
		assertNotNull(alert.getCategories());
		assertNotNull(alert.getCountries());
		assertTrue(alert.getUserId().isEmpty());
		
	}
}
