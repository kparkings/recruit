package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.City;

/**
* Unit test for the CityEntity class 
*/
class CityEntityTest {

	private static final String 	COUNTRY 	= "IT";
	private static final String 	NAME		= "rome";
	private static final float 		LAT 		=  19.9f;
	private static final float 		LON 		= -5.78f;
	private static final boolean 	ACTIVE	 	= true;
	
	@Test
	void testConstructor() {
		
		CityEntity entity = new CityEntity(COUNTRY, NAME, LAT, LON, ACTIVE);
		
		assertEquals(COUNTRY, 	entity.getCountry());
		assertEquals(NAME, 		entity.getName());
		assertEquals(LAT, 		entity.getLat());
		assertEquals(LON, 		entity.getLon());
		
		assertTrue(entity.isActive());
		
	}
	
	/**
	* Tests conversion from Entity representation to 
	* Domain representation
	*/
	@Test
	void testFromEntity() {
		
		CityEntity entity = new CityEntity(COUNTRY, NAME, LAT, LON, ACTIVE);

		assertEquals(COUNTRY, 	entity.getCountry());
		assertEquals(NAME, 		entity.getName());
		assertEquals(LAT, 		entity.getLat());
		assertEquals(LON, 		entity.getLon());
		
		assertTrue(entity.isActive());
		
		City city = CityEntity.fromEntity(entity);
		
		assertEquals(COUNTRY, 	city.getCountry());
		assertEquals(NAME, 		city.getName());
		assertEquals(LAT, 		city.getLat());
		assertEquals(LON, 		city.getLon());
		
		assertTrue(entity.isActive());
		
	}
	
	/**
	* Tests conversion from Domain representation to 
	* Entity representation
	*/
	@Test
	void testToEntity() {
		
		City city = City.builder().country(COUNTRY).name(NAME).lat(LAT).lon(LON).active(true).build();

		assertEquals(COUNTRY, 	city.getCountry());
		assertEquals(NAME, 		city.getName());
		assertEquals(LAT, 		city.getLat());
		assertEquals(LON, 		city.getLon());
		
		assertTrue(city.isActive());
		
		CityEntity entity = CityEntity.toEntity(city);
		
		assertEquals(COUNTRY, 	entity.getCountry());
		assertEquals(NAME, 		entity.getName());
		assertEquals(LAT, 		entity.getLat());
		assertEquals(LON, 		entity.getLon());
		
		assertTrue(entity.isActive());
		
	}
	
}