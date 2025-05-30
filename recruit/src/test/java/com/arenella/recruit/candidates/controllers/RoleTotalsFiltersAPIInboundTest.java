package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.RoleTotalsFilters;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.utils.GeoZoneSearchUtil.GEO_ZONE;

/**
* Unit tests for the RoleTotalsFiltersAPIInboundTest class 
*/
class RoleTotalsFiltersAPIInboundTest {

	private static final GEO_ZONE 		ZONE 			= GEO_ZONE.BENELUX;
	private static final COUNTRY 		COUNTRY_VAL 	= COUNTRY.AUSTRIA;
	
	/**
	* Tests construction via builder 
	*/
	@Test
	void testConstruction() {
		
		RoleTotalsFiltersAPIInbound filters = RoleTotalsFiltersAPIInbound
				.builder()
					.zone(ZONE)
					.country(COUNTRY_VAL)
				.build();
		
		assertEquals(ZONE, 				filters.getZone().get());
		assertEquals(COUNTRY_VAL, 		filters.getCountry().get());
		
	}
	
	/**
	* Tests converstion form API Inbound representation to Domain
	* representation 
	*/
	@Test
	void testToDomain() {
		
		RoleTotalsFiltersAPIInbound filters = RoleTotalsFiltersAPIInbound
		 		.builder()
					.zone(ZONE)
					.country(COUNTRY_VAL)
				.build();
		
		assertEquals(ZONE, 				filters.getZone().get());
		assertEquals(COUNTRY_VAL, 		filters.getCountry().get());
		
		RoleTotalsFilters domain = RoleTotalsFiltersAPIInbound.toDomain(filters);
		
		assertEquals(ZONE, 				domain.getZone().get());
		assertEquals(COUNTRY_VAL, 		domain.getCountry().get());
		
	}
	
	/**
	* Tests conversion form API Inbound representation to Domain
	* representation with default values
	*/
	@Test
	void testToDomain_defaults() {
		
		RoleTotalsFiltersAPIInbound filters = RoleTotalsFiltersAPIInbound
		 		.builder()
				.build();
		
		assertTrue(filters.getZone().isEmpty());
		assertTrue(filters.getCountry().isEmpty());
		
		RoleTotalsFilters domain = RoleTotalsFiltersAPIInbound.toDomain(filters);
		
		assertTrue(domain.getZone().isEmpty());
		assertTrue(domain.getCountry().isEmpty());
		
	}
	
}