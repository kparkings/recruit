package com.arenella.recruit.recruiters.listings.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.listings.beans.Listing.country;
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
		final String			ownerId			= "abc123";
		final listing_type 		type			= listing_type.CONTRACT_ROLE;
		final country 			countryVal		= country.BELGIUM;
		
		ListingFilter filter = ListingFilter
									.builder()
										.listingId(listingId)
										.ownerId(ownerId)
										.type(type)
										.country(countryVal)
									.build();
		
		assertEquals(filter.getListingId().get(), listingId);
		assertEquals(filter.getOwnerId().get(), ownerId);
		assertEquals(filter.getType().get(), type);
		assertEquals(filter.getCountry().get(), countryVal);
		
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
		assertFalse(filter.getOwnerId().isPresent());
		assertFalse(filter.getType().isPresent());
		assertFalse(filter.getCountry().isPresent());
		
	}
	
}