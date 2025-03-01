package com.arenella.recruit.listings.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.utils.ListingGeoZoneSearchUtil.GEO_ZONE;

/**
* Unit tests for the ListingGeoZoneSearchUtil class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class ListingGeoZoneSearchUtilTest {

	/**
	* Tests retrieving countries for multiple GeoZones
	* @throws Exception
	*/
	@Test
	public void testFetchBeneluxAndBritishIsles() throws Exception{
	
		final int expectedCountryCount = 6;
		
		ListingGeoZoneSearchUtil.initGeoZoneSearchUtil();
		
		Set<Listing.Country> countries = ListingGeoZoneSearchUtil.fetchCountriesFor(GEO_ZONE.BENELUX, GEO_ZONE.BRITISH_ISLES);
		
		assertTrue(countries.contains(Listing.Country.NETHERLANDS));
		assertTrue(countries.contains(Listing.Country.BELGIUM));
		assertTrue(countries.contains(Listing.Country.LUXEMBOURG));
		assertTrue(countries.contains(Listing.Country.UK));
		assertTrue(countries.contains(Listing.Country.NORTHERN_IRELAND));
		assertTrue(countries.contains(Listing.Country.REPUBLIC_OF_IRELAND));
		
		assertEquals(expectedCountryCount, countries.size());
		
	}
	
}