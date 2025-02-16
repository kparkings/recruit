package com.arenella.recruit.listings.dao;

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
* Unit tests for the ListingAlertEntity class
* @author K Parkings
*/
public class ListingAlertEntityTest {

	private static final UUID 					ID				= UUID.randomUUID();
	private static final Long 					USER_ID			= 1L;
	private static final String 				EMAIL			= "admin@arenella-ict.com";
	private static final LocalDate 				CREATED			= LocalDate.of(2023, 12, 9);
	private static final Listing.listing_type 	CONTRACT_TYPE 	= Listing.listing_type.BOTH;
	private static final Set<Listing.Country> 	COUNTRIES 		= Set.of(Listing.Country.NETHERLANDS);
	private static final Set<Listing.TECH> 		CATEGORIES 		= Set.of(Listing.TECH.ARCHITECT);
	
	/**
	* Tests construction via builder
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		ListingAlertEntity entity = 
				ListingAlertEntity
					.builder()
						.categories(CATEGORIES)
						.contractType(CONTRACT_TYPE)
						.countries(COUNTRIES)
						.created(CREATED)
						.email(EMAIL)
						.id(ID)
						.userId(USER_ID)
					.build();
					
		assertEquals(CONTRACT_TYPE, entity.getContractType());
		assertEquals(CREATED, 		entity.getCreated());
		assertEquals(EMAIL, 		entity.getEmail());
		assertEquals(ID, 			entity.getId());
		assertEquals(USER_ID, 		entity.getUserId().get());
		
		entity.getCategories().stream().filter(c -> c == Listing.TECH.ARCHITECT).findAny().orElseThrow();
		entity.getCountries().stream().filter(c -> c == Listing.Country.NETHERLANDS).findAny().orElseThrow();
		
	}
	
	/**
	* Tests conversion from Entity
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception{
		
		ListingAlertEntity entity = 
				ListingAlertEntity
					.builder()
						.categories(CATEGORIES)
						.contractType(CONTRACT_TYPE)
						.countries(COUNTRIES)
						.created(CREATED)
						.email(EMAIL)
						.id(ID)
						.userId(USER_ID)
					.build();
					
		
		ListingAlert alert = ListingAlertEntity.convertFromEntity(entity);
	
		assertEquals(CONTRACT_TYPE, alert.getContractType());
		assertEquals(CREATED, 		alert.getCreated());
		assertEquals(EMAIL, 		alert.getEmail());
		assertEquals(ID, 			alert.getId());
		assertEquals(USER_ID, 		alert.getUserId().get());
		
		alert.getCategories().stream().filter(c -> c == Listing.TECH.ARCHITECT).findAny().orElseThrow();
		alert.getCountries().stream().filter(c -> c == Listing.Country.NETHERLANDS).findAny().orElseThrow();
		
		
	}
	
	/**
	* Tests conversion to Entity
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity() throws Exception{
	
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
					
		
		ListingAlertEntity entity = ListingAlertEntity.convertToEntity(alert);
	
		assertEquals(CONTRACT_TYPE, entity.getContractType());
		assertEquals(CREATED, 		entity.getCreated());
		assertEquals(EMAIL, 		entity.getEmail());
		assertEquals(ID, 			entity.getId());
		assertEquals(USER_ID, 		entity.getUserId().get());
		
		entity.getCategories().stream().filter(c -> c == Listing.TECH.ARCHITECT).findAny().orElseThrow();
		entity.getCountries().stream().filter(c -> c == Listing.Country.NETHERLANDS).findAny().orElseThrow();
		
	}
	
	/**
	* Tests defensive coding practices are being used
	* @throws Exception
	*/
	@Test
	public void testDefaults() throws Exception {
		
		ListingAlertEntity entity = 
				ListingAlertEntity
					.builder().build();
		
		assertNotNull(entity.getCategories());
		assertNotNull(entity.getCountries());
		assertTrue(entity.getUserId().isEmpty());
		
	}
	
}