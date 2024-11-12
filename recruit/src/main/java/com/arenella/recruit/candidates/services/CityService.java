package com.arenella.recruit.candidates.services;

import java.util.Set;

import com.arenella.recruit.candidates.beans.City;
import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* Defines Services related to City objets 
*/
public interface CityService {

	/**
	* Returns a list of Cities for a particular Country
	* @param country - country to filter on
	* @return Cities for country
	*/
	Set<City> fetchCitiesForCountry(COUNTRY country);
	
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

	/**
	* Performs a check to see if the combination of Country and City is 
	* known. If you adds it to the System for review by an admin who 
	* can add the GeoLocation information and activate the City
	* @param country - Country the City belongs to
	* @param city	 - Name of the City
	*/
	void performNewCityCheck(COUNTRY country, String city);
	
	/**
	* Deletes a City from the system
	* @param country - Country the City belongs to
	* @param city	 - name of the City
	*/
	void deleteCity(COUNTRY country, String city);
	
}