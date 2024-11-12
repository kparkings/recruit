package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.candidates.beans.City;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.services.CityService;

/**
* Unit tests for the CityController
*/
@ExtendWith(MockitoExtension.class)
class CityControllerTest {

	@Mock
	private CityService mockCityService;
	
	@InjectMocks
	private CityController cityController;
	
	/**
	* Tests fetching city information endpoint 
	*/
	@Test
	void testFetchCitiesForCountry() {
		
		final String cityName = "rome";
		
		Mockito.when(this.mockCityService.fetchCitiesForCountry(COUNTRY.BELGIUM)).thenReturn(Set.of(City.builder().country(COUNTRY.BELGIUM).name(cityName).build()));
		
		ResponseEntity<Set<CityAPIOutbound>> cities = this.cityController.fetchCitiesForCountry(COUNTRY.BELGIUM);
		
		assertEquals(HttpStatus.OK, cities.getStatusCode());
		
		cities.getBody().stream().filter(c -> c.getName().equals(cityName)).findAny().orElseThrow();
		
	}
	
	/**
	* Tests adding new City 
	*/
	@Test
	void testAddCoity() {
		
		ResponseEntity<Void> response = this.cityController.addCity(CityAPIInbound.builder().country(COUNTRY.BELGIUM).name("Rome").build());
		
		Mockito.verify(this.mockCityService).addCity(Mockito.any(City.class));
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
	}
	
	/**
	* Tests fetching Cities awating activation by an Admin 
	*/
	@Test
	void testFetchCitiesAwaitingActivation() {
		
		final String cityName = "rome";
		
		Mockito.when(this.mockCityService.fetchCitiesAwaitingActivation()).thenReturn(Set.of(City.builder().country(COUNTRY.BELGIUM).name(cityName).build()));
		
		ResponseEntity<Set<CityAPIOutbound>> cities = this.cityController.fetchCitiesAwaitingActivation();
		
		assertEquals(HttpStatus.OK, cities.getStatusCode());
		
		cities.getBody().stream().filter(c -> c.getName().equals(cityName)).findAny().orElseThrow();	
	}
	
	/**
	* Tests adding new City 
	*/
	@Test
	void testUpdateCity() {
		
		ResponseEntity<Void> response = this.cityController.updateCity(CityAPIInbound.builder().country(COUNTRY.BELGIUM).name("Rome").build());
		
		Mockito.verify(this.mockCityService).updateCity(Mockito.any(City.class));
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	/**
	* Tests deletion of City
	*/
	@Test
	void testDeleteCity() {
		
		ResponseEntity<Void> response = this.cityController.deleteCity(COUNTRY.BELGIUM, "Rome");
		
		Mockito.verify(this.mockCityService).deleteCity(COUNTRY.BELGIUM, "Rome");
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
}
