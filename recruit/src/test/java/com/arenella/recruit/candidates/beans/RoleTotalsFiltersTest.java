package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.utils.GeoZoneSearchUtil.GEO_ZONE;

/**
* Unit tests for the RoleTotalsFilters class 
*/
class RoleTotalsFiltersTest {

	private static final GEO_ZONE 		ZONE 			= GEO_ZONE.BENELUX;
	private static final COUNTRY 		COUNTRY_VAL 	= COUNTRY.AUSTRIA;
	
	/**
	* Tests construction via builder 
	*/
	@Test
	void testConstruction() {
		
		RoleTotalsFilters filters = RoleTotalsFilters
				.builder()
					.zone(ZONE)
					.country(COUNTRY_VAL)
				.build();
		
		assertEquals(ZONE, 				filters.getZone().get());
		assertEquals(COUNTRY_VAL, 		filters.getCountry().get());

	}
	
}