package com.arenella.recruit.recruiters.listings.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.beans.Listing.Country;
import com.arenella.recruit.listings.beans.Listing.currency;
import com.arenella.recruit.listings.beans.Listing.language;
import com.arenella.recruit.listings.beans.Listing.listing_type;
import com.arenella.recruit.listings.controllers.ListingAPIOutboundPublic;

/**
* Unit tests for the ListingAPIOutbound class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class ListingAPIOutboundPublicTest {

	private static final 	String 			SKILL_JAVA 			= "java";
	private static final 	String 			SKILL_CSHARP 		= "c#";
	
	private final 			UUID 						listingId			= UUID.randomUUID();
	private final 			String						ownerId				= "kparking";
	private final 			LocalDateTime				created				= LocalDateTime.of(2021, 11, 24, 10, 01, 01);
	private final 			String 						title				= "aTitle";
	private final 			String 						description			= "aDesc";
	private final 			listing_type 				type		 		= listing_type.CONTRACT_ROLE;
	private final 			Country 					country				= Listing.Country.NETHERLANDS;
	private final 			String 						location			= "Den Haag";
	private final 			int 						yearsExperience		= 10;
	private final 			Set<language> 				languages			= new LinkedHashSet<>();
	private final 			Set<String>					skills			 	= Set.of(SKILL_JAVA, SKILL_CSHARP);
	private final 			String 						rate				= "115.00";
	private final 			currency					currency			= Listing.currency.EUR;
	private final 			Set<ListingViewedEvent>		views				= Set.of(ListingViewedEvent.builder().build());
	private final 			String						ownerName			= "Kevin Parkings";
	private final 			String 						ownerCompany		= "Arenella BV";
	private final 			String						ownerEmail			= "admin@arenella-ict.com";
	
	/**
	* Sets up test environment 
	*/
	@BeforeEach
	void init() {
		this.languages.add(language.DUTCH);
		this.languages.add(language.FRENCH);
	}
	
	/**
	* Tests instantiation of Instance of class via the Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		ListingAPIOutboundPublic listing = ListingAPIOutboundPublic
													.builder()
														.country(country)
														.created(created)
														.currency(currency)
														.description(description)
														.languages(languages)
														.skills(skills)
														.listingId(listingId)
														.location(location)
														.ownerName(ownerName)
														.ownerCompany(ownerCompany)
														.ownerEmail(ownerEmail)
														.rate(rate)
														.title(title)
														.type(type)
														.yearsExperience(yearsExperience)
														.ownerId(ownerId)
													.build();
		
		assertEquals(country, 			listing.getCountry());
		assertEquals(created, 			listing.getCreated());
		assertEquals(currency, 			listing.getCurrency());
		assertEquals(description, 		listing.getDescription());
		assertEquals(listingId, 		listing.getListingId());
		assertEquals(location, 			listing.getLocation());
		assertEquals(rate, 				listing.getRate());
		assertEquals(title,	 			listing.getTitle());
		assertEquals(type, 				listing.getType());
		assertEquals(yearsExperience, 	listing.getYearsExperience());
		assertEquals(ownerName, 		listing.getOwnerName());
		assertEquals(ownerCompany, 		listing.getOwnerCompany());
		assertEquals(ownerEmail, 		listing.getOwnerEmail());
		assertEquals(ownerId, 			listing.getOwnerId());
		
		assertTrue(listing.getLanguages().contains(Listing.language.DUTCH));
		assertTrue(listing.getLanguages().contains(Listing.language.FRENCH));
		assertEquals(2, listing.getLanguages().size());
		
		assertTrue(listing.getSkills().contains(SKILL_JAVA));
		assertTrue(listing.getSkills().contains(SKILL_CSHARP));
		assertEquals(2, listing.getSkills().size());
		
	}
	
	/**
	* Tests instantiation of Instance of class via the Builder with default values.
	* Expect any Collections to be instantiated but empty
	* @throws Exception
	*/
	@Test
	void testBuilder_defaults() {
		
		ListingAPIOutboundPublic listing = ListingAPIOutboundPublic.builder().build();
		
		assertTrue(listing.getLanguages().isEmpty());
		assertTrue(listing.getSkills().isEmpty());
		
	}
	
	/**
	* Tests conversion from Domain version of Listing to API 
	* Outbound representation
	* @throws Exception
	*/
	@Test
	void testConvertFromListing() {
	
		Listing listing = Listing
				.builder()
					.country(country)
					.created(created)
					.currency(currency)
					.description(description)
					.languages(languages)
					.listingId(listingId)
					.skills(skills)
					.location(location)
					.ownerId(ownerId)
					.ownerName(ownerName)
					.ownerCompany(ownerCompany)
					.ownerEmail(ownerEmail)
					.rate(rate)
					.title(title)
					.type(type)
					.yearsExperience(yearsExperience)
					.views(views)
					.ownerId(ownerId)
				.build();
		
		ListingAPIOutboundPublic outbound = ListingAPIOutboundPublic.convertFromListing(listing);
		
		assertEquals(country, 			outbound.getCountry());
		assertEquals(created, 			outbound.getCreated());
		assertEquals(currency, 			outbound.getCurrency());
		assertEquals(description, 		outbound.getDescription());
		assertEquals(listingId, 		outbound.getListingId());
		assertEquals(location, 			outbound.getLocation());
		assertEquals(rate, 				outbound.getRate());
		assertEquals(title,	 			outbound.getTitle());
		assertEquals(type, 				outbound.getType());
		assertEquals(yearsExperience, 	outbound.getYearsExperience());
		assertEquals(ownerName, 		outbound.getOwnerName());
		assertEquals(ownerCompany, 		outbound.getOwnerCompany());
		assertEquals(ownerEmail, 		outbound.getOwnerEmail());
		assertEquals(ownerId, 			outbound.getOwnerId());
		
		assertTrue(outbound.getLanguages().contains(Listing.language.DUTCH));
		assertTrue(outbound.getLanguages().contains(Listing.language.FRENCH));
		assertEquals(2, outbound.getLanguages().size());
		
		assertTrue(outbound.getSkills().contains(SKILL_JAVA));
		assertTrue(outbound.getSkills().contains(SKILL_CSHARP));
		assertEquals(2, outbound.getSkills().size());
		
	}
		
}