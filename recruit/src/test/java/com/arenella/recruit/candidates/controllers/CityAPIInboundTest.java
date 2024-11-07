package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.City;

/**
* Unit tests for the CityAPIInbound class
*/
class CityAPIInboundTest {

	private static final String 	COUNTRY 	= "it";
	private static final String 	NAME 		= "Rome";
	private static final float 		LAT 		= -1.99f;
	private static final float 		LON 		= 1.99f;
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	void testConstruction() {
		
		CityAPIInbound city = CityAPIInbound.builder()
							.country(COUNTRY)
							.name(NAME)
							.lat(LAT)
							.lon(LON)
						.build();
		
		assertEquals(COUNTRY.toUpperCase(), city.getCountry());
		assertEquals(NAME.toLowerCase(), 	city.getName());
		assertEquals(LAT, 					city.getLat());
		assertEquals(LON, 					city.getLon());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation 
	* of City
	* @throws Exception
	*/
	@Test
	void testConvertFromAPIInbound() {
		
		CityAPIInbound city = CityAPIInbound.builder()
				.country(COUNTRY)
				.name(NAME)
				.lat(LAT)
				.lon(LON)
			.build();

		City entity = CityAPIInbound.convertFromAPIInbound(city);
		
		assertEquals(COUNTRY.toUpperCase(), 	entity.getCountry());
		assertEquals(NAME.toLowerCase(), 		entity.getName());
		assertEquals(LAT, 						entity.getLat());
		assertEquals(LON, 						entity.getLon());
	
	}
	
}