package com.arenella.recruit.listings.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.Listing.Country;
import com.arenella.recruit.listings.beans.Listing.LISTING_AGE;
import com.arenella.recruit.listings.beans.Listing.listing_type;

/**
* Unit tests for the ListingSearchRequestAPIInbound class 
*/

public class ListingSearchRequestAPIInboundTest {

	/**
	* Tests defaults return empty
	*/
	@Test
	void testDefaults() {
			
		ListingSearchRequestAPIInbound request = ListingSearchRequestAPIInbound.builder().build();
			
		assertTrue(request.getSearchTerm().isEmpty());
		assertTrue(request.getContractType().isEmpty());
		assertTrue(request.getCountries().isEmpty());
		assertTrue(request.getMaxAgeOfPost().isEmpty());
			
	}
		
	/**
	* Test construction 
	*/
	@Test
	void testConstuctor() {
		
		final String 		searchTerm 		= "Java develoer";
		final listing_type 	contractType 	= listing_type.BOTH;
		final Country 		country			= Country.BULGARIA;
		final LISTING_AGE 	maxAgeOfPost 	= LISTING_AGE.THIS_WEEK;
		
		ListingSearchRequestAPIInbound request = 
				ListingSearchRequestAPIInbound
				.builder()
					.searchTerm(searchTerm)
					.contractType(contractType)
					.countries(Set.of(country))
					.maxAgeOfPost(maxAgeOfPost)
				.build();
		
		assertEquals(searchTerm, 	request.getSearchTerm().orElseThrow());
		assertEquals(contractType, 	request.getContractType().orElseThrow());
		assertEquals(country, 		request.getCountries().stream().filter(c -> c == country).findAny().orElseThrow());
		assertEquals(maxAgeOfPost, 	request.getMaxAgeOfPost().orElseThrow());
	}
	
}