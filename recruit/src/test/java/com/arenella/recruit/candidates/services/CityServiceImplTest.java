package com.arenella.recruit.candidates.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.candidates.beans.City;
import com.arenella.recruit.candidates.dao.CityDao;
import com.arenella.recruit.candidates.entities.CityEntity.CityId;
import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* Unit tests for the CityServiceImpl class
*/
@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {

	@Mock
	private CityDao mockCityDao;
	
	@InjectMocks
	private CityServiceImpl service = new CityServiceImpl();
	
	/**
	* Tests fetching of Cities for a Country
	*/
	@Test
	void testFetchCitiesForCountry() {
		
		City cosenza 	= City.builder().country(COUNTRY.ITALY).name("Cosenza").lat(1f).lon(5f).active(true).build();
		City roma 		= City.builder().country(COUNTRY.ITALY).name("Rome").lat(2f).lon(6f).active(true).build();
		City firenze 	= City.builder().country(COUNTRY.ITALY).name("Florence").lat(3f).lon(7f).active(true).build();
		City napoli 	= City.builder().country(COUNTRY.ITALY).name("Naples").lat(4f).lon(8f).build();
		
		Mockito.when(this.mockCityDao.fetchCitiesForCountry(COUNTRY.ITALY)).thenReturn(Set.of(cosenza, roma, firenze, napoli));
		
		Set<City> italianCities = this.service.fetchCitiesForCountry(COUNTRY.ITALY);
		
		italianCities.stream().filter(c -> c.getCountry() == COUNTRY.ITALY && c.getName().equals("Cosenza")).findAny().orElseThrow();
		italianCities.stream().filter(c -> c.getCountry() == COUNTRY.ITALY && c.getName().equals("Rome")).findAny().orElseThrow();
		italianCities.stream().filter(c -> c.getCountry() == COUNTRY.ITALY && c.getName().equals("Florence")).findAny().orElseThrow();
		
		assertTrue(italianCities.stream().filter(c -> c.getCountry() == COUNTRY.ITALY && c.getName().equals("Naples")).findAny().isEmpty());
		
	}
	
	/**
	* Tests addition of City that already exists
	*/
	@Test
	void testAddCity_existingCity() {

		City city = City.builder().country(COUNTRY.BELGIUM).name("cosenza").lat(1f).lon(2f).build();
		
		Mockito.when(this.mockCityDao.existsById(Mockito.any(CityId.class))).thenReturn(true);
		
	
		assertThrows(IllegalArgumentException.class, () -> {
			service.addCity(city);
		});
		
	}
	
	/**
	* Tests addition of City that already exists
	*/
	@Test
	void testAddCity() {

		City city = City.builder().country(COUNTRY.BELGIUM).name("cosenza").lat(1f).lon(2f).build();
		
		Mockito.when(this.mockCityDao.existsById(Mockito.any(CityId.class))).thenReturn(false);
		
		service.addCity(city);
		
		Mockito.verify(this.mockCityDao).saveCity(city);
		
	}
	
	/**
	* Tests addition of City that already exists
	*/
	@Test
	void testUpdateCity_noExistingCity() {

		City city = City.builder().country(COUNTRY.BELGIUM).name("cosenza").lat(1f).lon(2f).build();
		
		Mockito.when(this.mockCityDao.existsById(Mockito.any(CityId.class))).thenReturn(false);
		
	
		assertThrows(IllegalArgumentException.class, () -> {
			service.updateCity(city);
		});
		
	}
	
	/**
	* Tests addition of City that already exists
	*/
	@Test
	void testUpdateCity() {

		City city = City.builder().country(COUNTRY.BELGIUM).name("cosenza").lat(1f).lon(2f).active(true).build();
		
		Mockito.when(this.mockCityDao.existsById(Mockito.any(CityId.class))).thenReturn(true);
		
		service.updateCity(city);
		
		Mockito.verify(this.mockCityDao).saveCity(city);
		
	}
	
}