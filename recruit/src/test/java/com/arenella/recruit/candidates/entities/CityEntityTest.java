package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.arenella.recruit.candidates.enums.COUNTRY;
import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.City;

/**
* Unit test for the CityEntity class 
*/
class CityEntityTest {

	private static final COUNTRY 	COUNTRY_VAL = null;
	private static final String 	NAME		= "rome";
	private static final float 		LAT 		=  19.9f;
	private static final float 		LON 		= -5.78f;
	private static final boolean 	ACTIVE	 	= true;
	private static final boolean 	IGNORE		= true;
	
	@Test
	void testConstructor() {
		
		CityEntity entity = new CityEntity(COUNTRY_VAL, NAME, LAT, LON, ACTIVE, IGNORE);
		
		assertEquals(COUNTRY_VAL, 	entity.getCountry());
		assertEquals(NAME, 		entity.getName());
		assertEquals(LAT, 		entity.getLat());
		assertEquals(LON, 		entity.getLon());
		
		assertTrue(entity.isActive());
		assertTrue(entity.isIgnore());
	
	}
	
	/**
	* Tests conversion from Entity representation to 
	* Domain representation
	*/
	@Test
	void testFromEntity() {
		
		CityEntity entity = new CityEntity(COUNTRY_VAL, NAME, LAT, LON, ACTIVE, IGNORE);

		assertEquals(COUNTRY_VAL, 	entity.getCountry());
		assertEquals(NAME, 		entity.getName());
		assertEquals(LAT, 		entity.getLat());
		assertEquals(LON, 		entity.getLon());
		
		assertTrue(entity.isActive());
		assertTrue(entity.isIgnore());
		
		City city = CityEntity.fromEntity(entity);
		
		assertEquals(COUNTRY_VAL, 	city.getCountry());
		assertEquals(NAME, 		city.getName());
		assertEquals(LAT, 		city.getLat());
		assertEquals(LON, 		city.getLon());
		
		assertTrue(entity.isActive());
		assertTrue(entity.isIgnore());
		
	}
	
	/**
	* Tests conversion from Domain representation to 
	* Entity representation
	*/
	@Test
	void testToEntity() {
		
		City city = City.builder().country(COUNTRY_VAL).name(NAME).lat(LAT).lon(LON).active(true).ignore(true).build();

		assertEquals(COUNTRY_VAL, 	city.getCountry());
		assertEquals(NAME, 		city.getName());
		assertEquals(LAT, 		city.getLat());
		assertEquals(LON, 		city.getLon());
		
		assertTrue(city.isActive());
		assertTrue(city.isIgnore());
		
		CityEntity entity = CityEntity.toEntity(city);
		
		assertEquals(COUNTRY_VAL, 	entity.getCountry());
		assertEquals(NAME, 		entity.getName());
		assertEquals(LAT, 		entity.getLat());
		assertEquals(LON, 		entity.getLon());
		
		assertTrue(entity.isActive());
		assertTrue(city.isIgnore());
	}
	
	/**
	* Test marking a City as ignored
	*/
	@Test
	void testMAsIgnored() {
		
		City city = City.builder().country(COUNTRY_VAL).name(NAME).lat(LAT).lon(LON).active(true).build();
		
		assertFalse(city.isIgnore());
		
		city.markAsIgnored();
		
		assertTrue(city.isIgnore());
		
	}
	
}