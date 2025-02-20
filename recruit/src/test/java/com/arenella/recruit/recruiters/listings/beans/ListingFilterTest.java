package com.arenella.recruit.recruiters.listings.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.Listing.LISTING_AGE;
import com.arenella.recruit.listings.beans.Listing.Country;
import com.arenella.recruit.listings.beans.Listing.listing_type;
import com.arenella.recruit.listings.beans.ListingFilter;

/**
* Unit tests for the ListingFilter class
* @author K Parkings
*/
public class ListingFilterTest {

	/**
	* Test construction of Filters with Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		final UUID 				listingId		= UUID.randomUUID();
		final String			searchTerm		= "Java";
		final String			ownerId			= "abc123";
		final listing_type 		type			= listing_type.CONTRACT_ROLE;
		final Country 			countryVal		= Country.BELGIUM;
		final LISTING_AGE		listingAge		= LISTING_AGE.THIS_WEEK;
		final boolean			active			= false;
		
		ListingFilter filter = ListingFilter
									.builder()
										.listingId(listingId)
										.searchTerm(searchTerm)
										.ownerId(ownerId)
										.type(type)
										.countries(Set.of(countryVal))
										.listingAge(listingAge)
										.active(active)
									.build();
		
		assertEquals(filter.getListingId().get(), 				listingId);
		assertEquals(filter.getSearchTerm().get(), 				searchTerm);
		assertEquals(filter.getOwnerId().get(), 				ownerId);
		assertEquals(filter.getType().get(), 					type);
		assertEquals(filter.getCountries().toArray()[0], 		countryVal);
		assertEquals(filter.getListingAge().get(), 				listingAge);
		assertEquals(filter.getActive().get(), 					active);
		
	}

	/**
	* Test construction of Filters with Builder
	* @throws Exception
	*/
	@Test
	public void testBuilderWithDefaultValues() throws Exception {
		
		ListingFilter filter = ListingFilter
									.builder()
									.build();
		
		assertFalse(filter.getListingId().isPresent());
		assertFalse(filter.getSearchTerm().isPresent());
		assertFalse(filter.getOwnerId().isPresent());
		assertFalse(filter.getType().isPresent());
		assertTrue(filter.getCountries().isEmpty());
		assertFalse(filter.getListingAge().isPresent());
		assertFalse(filter.getActive().isPresent());
	}
	
}