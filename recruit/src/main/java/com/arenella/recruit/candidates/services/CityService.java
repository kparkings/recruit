package com.arenella.recruit.candidates.services;

import java.util.Set;

import com.arenella.recruit.candidates.beans.City;

/**
* Defines Services related to City objets 
*/
public interface CityService {

	/**
	* Returns a list of Cities for a particular Country
	* @param country - 2 digit country code
	* @return Cities for country
	*/
	Set<City> fetchCitiesForCountry(String country);
	
	/**
	* Returns City's awaiting activation
	* @return All City's awaiting activation
	*/
	public Set<City> fetchCitiesAwaitingActivation();
	
	/**
	* Adds a City
	* @param city	 - City to persist
	*/
	void addCity(City city);

	/**
	* Updates an existing City
	* @param city - city to update 
	*/
	void updateCity(City city);
	
	
	
}
