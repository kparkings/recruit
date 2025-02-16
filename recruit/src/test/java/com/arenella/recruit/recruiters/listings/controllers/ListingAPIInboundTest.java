package com.arenella.recruit.recruiters.listings.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.Listing.Country;
import com.arenella.recruit.listings.beans.Listing.currency;
import com.arenella.recruit.listings.beans.Listing.language;
import com.arenella.recruit.listings.beans.Listing.listing_type;
import com.arenella.recruit.listings.controllers.ListingAPIInbound;

/**
* Unit tests for the ListingAPIInbound class
* @author K Parkings
*/
public class ListingAPIInboundTest {

	private static final 	String 			SKILL_JAVA 			= "java";
	private static final 	String 			SKILL_CSHARP 		= "c#";
	
	private final 			UUID 			listingId			= UUID.randomUUID();
	private final 			String 			title				= "aTitle";
	private final 			String 			description			= "aDesc";
	private final 			listing_type 	type		 		= listing_type.CONTRACT_ROLE;
	private final 			Country 		country				= Listing.Country.NETHERLANDS;
	private final 			String 			location			= "Den Haag";
	private final 			int 			yearsExperience		= 10;
	private final 			Set<language> 	languages			= new LinkedHashSet<>();
	private final 			String 			rate				= "115.00";
	private final 			currency		currency			= Listing.currency.EUR;
	private final 			String			ownerName			= "Kevin Parkings";
	private final 			String 			ownerCompany		= "Arenella BV";
	private final 			String			ownerEmail			= "admin@arenella-ict.com";
	private final 			Set<String>		skills			 	= Set.of(SKILL_JAVA, SKILL_CSHARP);
	
	/**
	* Sets up test environment 
	*/
	@BeforeEach
	public void init() {
		this.languages.add(language.DUTCH);
		this.languages.add(language.FRENCH);
	}
	
	/**
	* Tests instantiation of Instance of class via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		ListingAPIInbound listing = ListingAPIInbound
							.builder()
								.country(country)
								.currency(currency)
								.description(description)
								.languages(languages)
								.skills(skills)
								.listingId(listingId)
								.location(location)
								.rate(rate)
								.title(title)
								.type(type)
								.yearsExperience(yearsExperience)
								.postToSocialMedia(true)
								.ownerName(ownerName)
								.ownerCompany(ownerCompany)
								.ownerEmail(ownerEmail)
							.build();
		
		assertEquals(country, 			listing.getCountry());
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
		
		assertTrue(listing.getLanguages().contains(Listing.language.DUTCH));
		assertTrue(listing.getLanguages().contains(Listing.language.FRENCH));
		assertEquals(listing.getLanguages().size(), 2);
		
		assertTrue(listing.getSkills().contains(SKILL_JAVA));
		assertTrue(listing.getSkills().contains(SKILL_CSHARP));
		assertEquals(listing.getSkills().size(), 2);
		
		assertTrue(listing.isPostToSocialMedia());
	
	}
	
	/**
	* Tests instantiation of Instance of class via the Builder with default values.
	* Expect any Collections to be instantiated but empty
	* @throws Exception
	*/
	@Test
	public void testBuilder_defaults() throws Exception {
		
		ListingAPIInbound listing = ListingAPIInbound.builder().build();
		
		assertTrue(listing.getLanguages().isEmpty());
		assertTrue(listing.getSkills().isEmpty());
			
		assertFalse(listing.isPostToSocialMedia());
		
	}
	
	/**
	* Tests conversion from API Inbound version of Listing to Domain representaion
	* @throws Exception
	*/
	@Test
	public void testConvertFromListing() throws Exception {
	
		ListingAPIInbound inbound = ListingAPIInbound
												.builder()
													.country(country)
													.currency(currency)
													.description(description)
													.languages(languages)
													.skills(skills)
													.listingId(listingId)
													.location(location)
													.rate(rate)
													.title(title)
													.type(type)
													.yearsExperience(yearsExperience)
													.ownerName(ownerName)
													.ownerCompany(ownerCompany)
													.ownerEmail(ownerEmail)
												.build();
		
		Listing listing = ListingAPIInbound.convertToListing(inbound);
		
		assertEquals(country, 			listing.getCountry());
		assertEquals(null,	 			listing.getCreated());
		assertEquals(currency, 			listing.getCurrency());
		assertEquals(description, 		listing.getDescription());
		assertEquals(listingId, 		listing.getListingId());
		assertEquals(location, 			listing.getLocation());
		assertEquals(null,	 			listing.getOwnerId());
		assertEquals(rate, 				listing.getRate());
		assertEquals(title,	 			listing.getTitle());
		assertEquals(type, 				listing.getType());
		assertEquals(yearsExperience, 	listing.getYearsExperience());
		assertEquals(0, 				listing.getViews().size());
		assertEquals(ownerName, 		listing.getOwnerName());
		assertEquals(ownerCompany, 		listing.getOwnerCompany());
		assertEquals(ownerEmail, 		listing.getOwnerEmail());
		
		assertTrue(listing.getLanguages().contains(Listing.language.DUTCH));
		assertTrue(listing.getLanguages().contains(Listing.language.FRENCH));
		assertEquals(listing.getLanguages().size(), 2);
		
		assertTrue(listing.getSkills().contains(SKILL_JAVA));
		assertTrue(listing.getSkills().contains(SKILL_CSHARP));
		assertEquals(listing.getSkills().size(), 2);
		
	}
	
}