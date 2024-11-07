package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.entities.CityEntity;

/**
* Unit tests for the City class
*/
class CityTest {

	private static final String 	COUNTRY 	= "it";
	private static final String 	NAME 		= "Rome";
	private static final float 		LAT 		= -1.99f;
	private static final float 		LON 		= 1.99f;
	private static final boolean	ACTIVE		= true;
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	void testConstruction() {
		
		City city = City.builder()
							.country(COUNTRY)
							.name(NAME)
							.lat(LAT)
							.lon(LON)
							.active(ACTIVE)
						.build();
		
		assertEquals(COUNTRY.toUpperCase(), city.getCountry());
		assertEquals(NAME.toLowerCase(), 	city.getName());
		assertEquals(LAT, 					city.getLat());
		assertEquals(LON, 					city.getLon());
		
		assertTrue(city.isActive());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation 
	* of City
	* @throws Exception
	*/
	@Test
	void testToEntity() {
		
		City city = City.builder()
				.country(COUNTRY)
				.name(NAME)
				.lat(LAT)
				.lon(LON)
				.active(ACTIVE)
			.build();
		
		assertTrue(city.isActive());

		CityEntity entity = CityEntity.toEntity(city);
		
		assertEquals(COUNTRY.toUpperCase(), 	entity.getCountry());
		assertEquals(NAME.toLowerCase(), 		entity.getName());
		assertEquals(LAT, 						entity.getLat());
		assertEquals(LON, 						entity.getLon());
	
		assertTrue(entity.isActive());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation 
	* of City
	* @throws Exception
	*/
	@Test
	void testFromEntity() {
		
		CityEntity entity = new CityEntity(COUNTRY, NAME, LAT, LON, ACTIVE);
		
		City city = CityEntity.fromEntity(entity);
		
		assertEquals(COUNTRY.toUpperCase(), 	city.getCountry());
		assertEquals(NAME.toLowerCase(), 		city.getName());
		assertEquals(LAT, 						city.getLat());
		assertEquals(LON, 						city.getLon());
		
		assertTrue(city.isActive());
		
	}
	
}