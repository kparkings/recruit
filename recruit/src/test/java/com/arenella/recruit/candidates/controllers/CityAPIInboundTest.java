package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.City;
import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* Unit tests for the CityAPIInbound class
*/
class CityAPIInboundTest {

	private static final COUNTRY 	COUNTRY_VAL 	= COUNTRY.BELGIUM;
	private static final String 	NAME 			= "Rome";
	private static final float 		LAT 			= -1.99f;
	private static final float 		LON 			= 1.99f;
	private static final boolean	ACTIVE			= true;
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	void testConstruction() {
		
		CityAPIInbound city = CityAPIInbound.builder()
							.country(COUNTRY_VAL)
							.name(NAME)
							.lat(LAT)
							.lon(LON)
							.active(ACTIVE)
						.build();
		
		assertEquals(COUNTRY_VAL, 	city.getCountry());
		assertEquals(NAME, 			city.getName());
		assertEquals(LAT, 			city.getLat());
		assertEquals(LON, 			city.getLon());
		assertEquals(ACTIVE, 		city.isActive());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation 
	* of City
	* @throws Exception
	*/
	@Test
	void testConvertFromAPIInbound() {
		
		CityAPIInbound city = CityAPIInbound.builder()
				.country(COUNTRY_VAL)
				.name(NAME)
				.lat(LAT)
				.lon(LON)
				.active(ACTIVE)
			.build();

		City entity = CityAPIInbound.convertFromAPIInbound(city);
		
		assertEquals(COUNTRY_VAL, 	entity.getCountry());
		assertEquals(NAME, 			entity.getName());
		assertEquals(LAT, 			entity.getLat());
		assertEquals(LON, 			entity.getLon());
		assertEquals(ACTIVE, 			entity.isActive());
	}
	
}