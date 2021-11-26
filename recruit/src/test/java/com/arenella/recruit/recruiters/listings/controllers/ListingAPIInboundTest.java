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
import com.arenella.recruit.listings.beans.Listing.country;
import com.arenella.recruit.listings.beans.Listing.currency;
import com.arenella.recruit.listings.beans.Listing.language;
import com.arenella.recruit.listings.beans.Listing.listing_type;
import com.arenella.recruit.listings.controllers.ListingAPIInbound;

/**
* Unit tests for the ListingAPIInbound class
* @author K Parkings
*/
public class ListingAPIInboundTest {

	private final UUID 				listingId			= UUID.randomUUID();
	private final String 			title				= "aTitle";
	private final String 			description			= "aDesc";
	private final listing_type 		type		 		= listing_type.CONTRACT_ROLE;
	private final country 			country				= Listing.country.NETHERLANDS;
	private final String 			location			= "Den Haag";
	private final int 				yearsExperience		= 10;
	private final Set<language> 	languages			= new LinkedHashSet<>();
	private final float 			rate				= 115.0f;
	private final currency			currency			= Listing.currency.EUR;
	
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
								.listingId(listingId)
								.location(location)
								.rate(rate)
								.title(title)
								.type(type)
								.yearsExperience(yearsExperience)
								.postToSocialMedia(true)
							.build();
		
		assertEquals(country, 			listing.getCountry());
		assertEquals(currency, 			listing.getCurrency());
		assertEquals(description, 		listing.getDescription());
		assertEquals(listingId, 		listing.getListingId());
		assertEquals(location, 			listing.getLocation());
		assertEquals(rate, 				listing.getRate(), 0);
		assertEquals(title,	 			listing.getTitle());
		assertEquals(type, 				listing.getType());
		assertEquals(yearsExperience, 	listing.getYearsExperience());
		
		assertTrue(listing.getLanguages().contains(Listing.language.DUTCH));
		assertTrue(listing.getLanguages().contains(Listing.language.FRENCH));
		assertTrue(listing.isPostToSocialMedia());
		assertEquals(listing.getLanguages().size(), 2);
		
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
		
		assertFalse(listing.isPostToSocialMedia());
		
	}
	
}