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
import com.arenella.recruit.listings.utils.ListingGeoZoneSearchUtil.GEO_ZONE;

/**
* Unit tests for the ListingFilter class
* @author K Parkings
*/
class ListingFilterTest {

	/**
	* Test construction of Filters with Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		final UUID 				listingId		= UUID.randomUUID();
		final String			searchTerm		= "Java";
		final String			ownerId			= "abc123";
		final listing_type 		type			= listing_type.CONTRACT_ROLE;
		final GEO_ZONE			geoZone			= GEO_ZONE.BENELUX;
		final Country 			countryVal		= Country.BELGIUM;
		final LISTING_AGE		listingAge		= LISTING_AGE.THIS_WEEK;
		final boolean			active			= false;
		
		ListingFilter filter = ListingFilter
									.builder()
										.listingId(listingId)
										.searchTerms(Set.of(searchTerm))
										.ownerId(ownerId)
										.type(type)
										.geoZones(Set.of(geoZone))
										.countries(Set.of(countryVal))
										.listingAge(listingAge)
										.active(active)
									.build();
		
		assertEquals(filter.getListingId().get(), 				listingId);
		assertEquals(filter.getSearchTerms().toArray()[0], 		searchTerm);
		assertEquals(filter.getOwnerId().get(), 				ownerId);
		assertEquals(filter.getType().get(), 					type);
		assertEquals(filter.getGeoZones().toArray()[0], 		geoZone);
		assertEquals(filter.getCountries().toArray()[0], 		countryVal);
		assertEquals(filter.getListingAge().get(), 				listingAge);
		assertEquals(filter.getActive().get(), 					active);
		
		filter.addCountry(Country.CYPRUS);
		
		assertTrue(filter.getCountries().stream().filter(c -> c == countryVal).findAny().isPresent());
		assertTrue(filter.getCountries().stream().filter(c -> c == Country.CYPRUS).findAny().isPresent());
	}

	/**
	* Tests adding of search terms to filers
	*/
	@Test
	void testAddSearchTerms() {
		
		ListingFilter filter = ListingFilter
				.builder()
				.build();
		
		filter.addSearchTerm("java");
		filter.addSearchTerm("C#");
		
		assertEquals(2, filter.getSearchTerms().size());
		assertTrue(filter.getSearchTerms().contains("java"));
		assertTrue(filter.getSearchTerms().contains("C#"));
		
	}
	
	/**
	* Test construction of Filters with Builder
	* @throws Exception
	*/
	@Test
	void testBuilderWithDefaultValues() {
		
		ListingFilter filter = ListingFilter
									.builder()
									.build();
		
		assertFalse(filter.getListingId().isPresent());
		assertTrue(filter.getSearchTerms().isEmpty());
		assertFalse(filter.getOwnerId().isPresent());
		assertFalse(filter.getType().isPresent());
		assertTrue(filter.getGeoZones().isEmpty());
		assertTrue(filter.getCountries().isEmpty());
		assertFalse(filter.getListingAge().isPresent());
		assertFalse(filter.getActive().isPresent());
	}
	
}