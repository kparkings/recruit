package com.arenella.recruit.candidates.controllers;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.services.CityService;

/**
* REST API for working with City static data
*/
@RestController
public class CityController {

	@Autowired
	private CityService cityService;

	/**
	* Returns City object for a given Country
	* @param countryCode - country code
	* @return Cities for Country
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_RECRUITER')")
	@GetMapping("/city/{country}")
	public ResponseEntity<Set<CityAPIOutbound>>  fetchCitiesForCountry(@PathVariable("country") COUNTRY country){
		return ResponseEntity.ok(this.cityService.fetchCitiesForCountry(country).stream().map(CityAPIOutbound::convertToAPIOutbound).collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	/**
	* Returns all City's awaiting activation
	* @return cities awaiting activation
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/city/awaiting-activation")
	public ResponseEntity<Set<CityAPIOutbound>>  fetchCitiesAwaitingActivation(){
		return ResponseEntity.ok(this.cityService.fetchCitiesAwaitingActivation().stream().map(CityAPIOutbound::convertToAPIOutbound).collect(Collectors.toCollection(LinkedHashSet::new)));
	}
	
	/**
	* Adds a new City 
	* @param cityAPIInbound - Details of City to be added
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/city")
	public ResponseEntity<Void> addCity(@RequestBody CityAPIInbound cityAPIInbound) {
		
		this.cityService.addCity(CityAPIInbound.convertFromAPIInbound(cityAPIInbound));
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	/**
	* Adds a new City 
	* @param cityAPIInbound - Details of City to be added
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/city")
	public ResponseEntity<Void> updateCity(@RequestBody CityAPIInbound cityAPIInbound) {
		
		this.cityService.updateCity(CityAPIInbound.convertFromAPIInbound(cityAPIInbound));
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
		
}