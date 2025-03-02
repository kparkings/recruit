package com.arenella.recruit.listings.repos.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.Listing.Country;
import com.arenella.recruit.listings.beans.Listing.currency;
import com.arenella.recruit.listings.beans.Listing.language;
import com.arenella.recruit.listings.beans.Listing.listing_type;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.dao.ListingViewedEventEntity;

/**
* Unit tests for the ListingDocument class 
*/
public class ListingDocumentTest {

	private static final UUID 							LISTING_ID			= UUID.randomUUID();
	private static final String							OWNER_ID			= "123";
	private static final String							OWNER_NAME			= "Kevin Parkings";
	private static final String 						OWNER_COMPANY		= "Arenella BV";
	private static final String							OWNER_EMAIL			= "kparkings@gmail.com";
	private static final LocalDateTime					CREATED				= LocalDateTime.of(2025, 3, 2, 17, 17);
	private static final String 						TITLE				= "Java Developer";
	private static final String 						DESCRIPTION			= "Senior Jav Developer needed";
	private static final listing_type 					TYPE				= listing_type.CONTRACT_ROLE;
	private static final Country 						COUNTRY				= Country.BELGIUM;
	private static final String 						LOCATION			= "Brussels";
	private static final int 							YEARS_EXPERIENCE	= 10;
	private static final boolean						ACTIVE				= true; 
	private static final String 						RATE				= "100 p/d ex tax";
	private static final currency						CURRENCY			= currency.EUR;
	private static final Set<String> 					SKILLS				= Set.of("JAVA");
	private static final Set<language> 					LANGUAGES			= Set.of(language.CZECH);
	private static final ListingViewedEventEntity		VIEW_ENTITY			= ListingViewedEventEntity.builder().build();
	private static final Set<ListingViewedEventEntity> 	VIEWS				= Set.of(VIEW_ENTITY);
	
	/**
	* Tests construction via a Builder
	*/
	@Test
	void testConstruction() {
		
		ListingDocument listing = ListingDocument
			.builder()
				.active(ACTIVE)
				.country(COUNTRY)
				.created(CREATED)
				.currency(CURRENCY)
				.description(DESCRIPTION)
				.languages(LANGUAGES)
				.listingId(LISTING_ID)
				.location(LOCATION)
				.ownerCompany(OWNER_COMPANY)
				.ownerEmail(OWNER_EMAIL)
				.ownerId(OWNER_ID)
				.ownerName(OWNER_NAME)
				.rate(RATE)
				.skills(SKILLS)
				.title(TITLE)
				.type(TYPE)
				.views(VIEWS)
				.yearsExperience(YEARS_EXPERIENCE)
			.build();
		
		assertEquals(ACTIVE, listing.isActive());
		assertEquals(COUNTRY, listing.getCountry());
		assertEquals(CREATED, listing.getCreated());
		assertEquals(CURRENCY, listing.getCurrency());
		assertEquals(DESCRIPTION, listing.getDescription());
		assertEquals(LISTING_ID, listing.getListingId());
		assertEquals(LOCATION, listing.getLocation());
		assertEquals(OWNER_COMPANY, listing.getOwnerCompany());
		assertEquals(OWNER_EMAIL, listing.getOwnerEmail());
		assertEquals(OWNER_ID, listing.getOwnerId());
		assertEquals(OWNER_NAME, listing.getOwnerName());
		assertEquals(RATE, listing.getRate());
		
		assertEquals(TITLE, listing.getTitle());
		assertEquals(TYPE, listing.getType());
		assertEquals(YEARS_EXPERIENCE, listing.getYearsExperience());
		
		assertTrue(listing.getSkills().contains("JAVA"));
		assertTrue(listing.getViews().contains(VIEW_ENTITY));
		assertTrue(listing.getLanguages().contains(language.CZECH));
		
	}
	
	/**
	* Tests that by default Collections exist but are empty 
	* to guard against null pointer exceptions
	*/
	@Test
	void testDefaults() {
		
		ListingDocument listing = ListingDocument.builder().build();
		
		assertTrue(listing.getSkills().isEmpty());
		assertTrue(listing.getLanguages().isEmpty());
		assertTrue(listing.getViews().isEmpty());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* of Listing
	*/
	@Test
	void tesFromEntity() {
		
		ListingDocument document = ListingDocument
				.builder()
					.active(ACTIVE)
					.country(COUNTRY)
					.created(CREATED)
					.currency(CURRENCY)
					.description(DESCRIPTION)
					.languages(LANGUAGES)
					.listingId(LISTING_ID)
					.location(LOCATION)
					.ownerCompany(OWNER_COMPANY)
					.ownerEmail(OWNER_EMAIL)
					.ownerId(OWNER_ID)
					.ownerName(OWNER_NAME)
					.rate(RATE)
					.skills(SKILLS)
					.title(TITLE)
					.type(TYPE)
					.views(VIEWS)
					.yearsExperience(YEARS_EXPERIENCE)
				.build();
			
			assertEquals(ACTIVE, 			document.isActive());
			assertEquals(COUNTRY, 			document.getCountry());
			assertEquals(CREATED, 			document.getCreated());
			assertEquals(CURRENCY, 			document.getCurrency());
			assertEquals(DESCRIPTION, 		document.getDescription());
			assertEquals(LISTING_ID, 		document.getListingId());
			assertEquals(LOCATION, 			document.getLocation());
			assertEquals(OWNER_COMPANY, 	document.getOwnerCompany());
			assertEquals(OWNER_EMAIL, 		document.getOwnerEmail());
			assertEquals(OWNER_ID, 			document.getOwnerId());
			assertEquals(OWNER_NAME, 		document.getOwnerName());
			assertEquals(RATE, 				document.getRate());
			
			assertEquals(TITLE, 			document.getTitle());
			assertEquals(TYPE, 				document.getType());
			assertEquals(YEARS_EXPERIENCE, 	document.getYearsExperience());
			
			assertTrue(document.getSkills().contains("JAVA"));
			assertTrue(document.getViews().contains(VIEW_ENTITY));
			assertTrue(document.getLanguages().contains(language.CZECH));
			
			Listing listing = ListingDocument.fromEntity(document);
			
			assertEquals(ACTIVE, 			listing.isActive());
			assertEquals(COUNTRY, 			listing.getCountry());
			assertEquals(CREATED, 			listing.getCreated());
			assertEquals(CURRENCY, 			listing.getCurrency());
			assertEquals(DESCRIPTION, 		listing.getDescription());
			assertEquals(LISTING_ID, 		listing.getListingId());
			assertEquals(LOCATION, 			listing.getLocation());
			assertEquals(OWNER_COMPANY, 	listing.getOwnerCompany());
			assertEquals(OWNER_EMAIL, 		listing.getOwnerEmail());
			assertEquals(OWNER_ID, 			listing.getOwnerId());
			assertEquals(OWNER_NAME, 		listing.getOwnerName());
			assertEquals(RATE, 				listing.getRate());
			
			assertEquals(TITLE, 			listing.getTitle());
			assertEquals(TYPE, 				listing.getType());
			assertEquals(YEARS_EXPERIENCE, 	listing.getYearsExperience());
			
			assertTrue(listing.getSkills().contains("JAVA"));
			assertFalse(listing.getViews().isEmpty());
			assertTrue(listing.getLanguages().contains(language.CZECH));
			
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* of Listing
	*/
	@Test
	void tesToEntity() {
		
		Listing listing = Listing
				.builder()
					.active(ACTIVE)
					.country(COUNTRY)
					.created(CREATED)
					.currency(CURRENCY)
					.description(DESCRIPTION)
					.languages(LANGUAGES)
					.listingId(LISTING_ID)
					.location(LOCATION)
					.ownerCompany(OWNER_COMPANY)
					.ownerEmail(OWNER_EMAIL)
					.ownerId(OWNER_ID)
					.ownerName(OWNER_NAME)
					.rate(RATE)
					.skills(SKILLS)
					.title(TITLE)
					.type(TYPE)
					.views(Set.of(ListingViewedEvent.builder().build()))
					.yearsExperience(YEARS_EXPERIENCE)
				.build();
			
			assertEquals(ACTIVE, 			listing.isActive());
			assertEquals(COUNTRY, 			listing.getCountry());
			assertEquals(CREATED, 			listing.getCreated());
			assertEquals(CURRENCY, 			listing.getCurrency());
			assertEquals(DESCRIPTION, 		listing.getDescription());
			assertEquals(LISTING_ID, 		listing.getListingId());
			assertEquals(LOCATION, 			listing.getLocation());
			assertEquals(OWNER_COMPANY, 	listing.getOwnerCompany());
			assertEquals(OWNER_EMAIL, 		listing.getOwnerEmail());
			assertEquals(OWNER_ID, 			listing.getOwnerId());
			assertEquals(OWNER_NAME, 		listing.getOwnerName());
			assertEquals(RATE, 				listing.getRate());
			
			assertEquals(TITLE, 			listing.getTitle());
			assertEquals(TYPE, 				listing.getType());
			assertEquals(YEARS_EXPERIENCE, 	listing.getYearsExperience());
			
			assertTrue(listing.getSkills().contains("JAVA"));
			assertFalse(listing.getViews().isEmpty());
			assertTrue(listing.getLanguages().contains(language.CZECH));
			
			ListingDocument document = ListingDocument.toEntity(listing);
			
			assertEquals(ACTIVE, 			document.isActive());
			assertEquals(COUNTRY, 			document.getCountry());
			assertEquals(CREATED, 			document.getCreated());
			assertEquals(CURRENCY, 			document.getCurrency());
			assertEquals(DESCRIPTION, 		document.getDescription());
			assertEquals(LISTING_ID, 		document.getListingId());
			assertEquals(LOCATION, 			document.getLocation());
			assertEquals(OWNER_COMPANY, 	document.getOwnerCompany());
			assertEquals(OWNER_EMAIL, 		document.getOwnerEmail());
			assertEquals(OWNER_ID, 			document.getOwnerId());
			assertEquals(OWNER_NAME, 		document.getOwnerName());
			assertEquals(RATE, 				document.getRate());
			
			assertEquals(TITLE, 			document.getTitle());
			assertEquals(TYPE, 				document.getType());
			assertEquals(YEARS_EXPERIENCE, 	document.getYearsExperience());
			
			assertTrue(document.getSkills().contains("JAVA"));
			assertFalse(document.getViews().isEmpty());
			assertTrue(document.getLanguages().contains(language.CZECH));
		
	}
	
}