package com.arenella.recruit.candidates.dao;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import com.arenella.recruit.candidates.beans.City;
import com.arenella.recruit.candidates.entities.CityEntity;

/**
* Repository for City Entities
*/
public interface CityDao extends ListCrudRepository<CityEntity, CityEntity.CityId>{

	@Query("from CityEntity where id.country = :countryId")
	List<CityEntity> fetchCountries(String country);
	
	/**
	* Persists / updates a City 
	* @param city
	*/
	default void saveCity(City city) {
		this.save(CityEntity.toEntity(city));
	}
	
	/**
	* Returns all Cities for a given Country
	* @param country - country of interest
	* @return Cities
	*/
	default Set<City> fetchCitiesForCountry(String country){
		return this.fetchCountries(country).stream().map(CityEntity::fromEntity).collect(Collectors.toSet());
	}
	
	/**
	* Returns all Cities that are still awaiting activation
	* @return
	*/
	default Set<City> fetchCitiesAwaitingActivation() {
		return this.findAll().stream().filter(c -> !c.active).map(CityEntity::fromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
}
