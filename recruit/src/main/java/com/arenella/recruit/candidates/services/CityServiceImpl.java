package com.arenella.recruit.candidates.services;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.candidates.beans.City;
import com.arenella.recruit.candidates.dao.CityDao;
import com.arenella.recruit.candidates.entities.CityEntity.CityId;
import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* Services for interaction with City objects. A City represents a City where a Candidate
* is based or looking for work 
*/
@Service
public class CityServiceImpl implements CityService {

	@Autowired
	CityDao cityDao;
	
	/**
	* Refer to the CityService interface
	*/
	@Override
	public Set<City> fetchCitiesForCountry(COUNTRY country) {
		return cityDao.fetchCitiesForCountry(country).stream().filter(City::isActive).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	* Refer to the CityService interface
	*/
	@Override
	public Set<City> fetchCitiesAwaitingActivation() {
		return cityDao.fetchCitiesAwaitingActivation();	
	}
	
	
	/**
	* Refer to the CityService interface
	*/
	@Override
	public void addCity(City city) {
		
		if (this.cityDao.existsById(new CityId(city.getCountry(), city.getName()))) {
			throw new IllegalArgumentException("Unable to add city to country " + city.getCountry() + " " + city.getName());
		}
		
		this.cityDao.saveCity(city);

	}

	/**
	* Refer to the CityService interface
	*/
	@Override
	public void updateCity(City city) {
		
		if (!this.cityDao.existsById(new CityId(city.getCountry(), city.getName()))) {
			throw new IllegalArgumentException("Unable to update city " + city.getCountry() + " " + city.getName());
		}
		
		this.cityDao.saveCity(city);
		
	}

	/**
	* Refer to the CityService interface
	*/
	@Override
	public void performNewCityCheck(COUNTRY country, String city) {
		
		if (this.cityDao.existsById(new CityId(country, city))) {
			return;
		}
		
		this.cityDao.saveCity(City.builder().country(country).name(this.cleanCityName(city)).build());
		
	}

	/**
	* Refer to the CityService interface
	*/
	@Override
	public void deleteCity(COUNTRY  country, String city) {
		// TODO Auto-generated method stub
		
	}

	/**
	* Formats incoming City nameString
	* @param name - unformatted City name
	* @return formatted City name
	*/
	private String cleanCityName(String name) {
		
		
		String cleanedString = ""+name.trim();
		cleanedString = cleanedString.toLowerCase();
		
		if (cleanedString.length() > 0) {
			cleanedString = cleanedString.substring(0,1).toUpperCase() + cleanedString.substring(1);
		}
		
		return cleanedString;
		
	}
	
}