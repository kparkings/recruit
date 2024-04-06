package com.arenella.recruit.candidates.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* Util to return supported Countries in a Geo zone 
* @author K Parkings
*/
@Component
public class GeoZoneSearchUtil {

	public enum GEO_ZONE {
		//WORLD, 
		EUROPE, 
		//NORTH_AMERICA, 
		//SOUTHERN_EUROPE, 
		//EASTERN_EUROPE, 
		//WESTERN_EUROPE, 
		//NORTHERN_EUROPE, 
		//CENTRAL_EUROPE, 
		BENELUX, 
		BRITISH_ISLES, 
		//IRELAND, 
		//DACH
		}
	
	private static final Set<COUNTRY> WORLD 			= new HashSet<>();
	private static final Set<COUNTRY> EUROPE 			= new HashSet<>();
	private static final Set<COUNTRY> NORTH_AMERICA 	= new HashSet<>();
	private static final Set<COUNTRY> SOUTHERN_EUROPE 	= new HashSet<>();
	private static final Set<COUNTRY> EASTERN_EUROPE 	= new HashSet<>();
	private static final Set<COUNTRY> WESTERN_EUROPE 	= new HashSet<>();
	private static final Set<COUNTRY> NORTHERN_EUROPE 	= new HashSet<>();
	private static final Set<COUNTRY> CENTRAL_EUROPE 	= new HashSet<>();
	private static final Set<COUNTRY> BENELUX  			= new HashSet<>();
	private static final Set<COUNTRY> BRITISH_ISLES  	= new HashSet<>();
	private static final Set<COUNTRY> IRELAND  			= new HashSet<>();
	private static final Set<COUNTRY> DACH  			= new HashSet<>();
	private static final Set<COUNTRY> BALTICS  			= new HashSet<>();
	
	/**
	* Returns the countries in the requested GEO_ZONES
	* @param geoZones - Geographical zones containing multiple Countries
	* @return Countries in selected GEO_ZONE's
	*/
	public static Set<COUNTRY> fetchCountriesFor(GEO_ZONE... geoZones ){
		
		Set<COUNTRY> countries = new HashSet<>();
		
		Arrays.stream(geoZones).forEach(geoZone -> {
			switch(geoZone) {
				//case WORLD 				-> countries.addAll(WORLD);
				case EUROPE 			-> countries.addAll(EUROPE);
				//case NORTH_AMERICA 		-> countries.addAll(NORTH_AMERICA);
				//case SOUTHERN_EUROPE 	-> countries.addAll(SOUTHERN_EUROPE);
				//case EASTERN_EUROPE 	-> countries.addAll(EASTERN_EUROPE);
				//case WESTERN_EUROPE 	-> countries.addAll(WESTERN_EUROPE);
				//case NORTHERN_EUROPE 	-> countries.addAll(NORTHERN_EUROPE);
				//case CENTRAL_EUROPE 	-> countries.addAll(CENTRAL_EUROPE);
				case BENELUX 			-> countries.addAll(BENELUX);
				case BRITISH_ISLES 		-> countries.addAll(BRITISH_ISLES);
				//case IRELAND 			-> countries.addAll(IRELAND);
				//case DACH				-> countries.addAll(DACH);
			}
		});
		
		return countries;
		
	}
	
	/**
	* Populates the GeoZone datasets
	*/
	@PostConstruct
	public static void initGeoZoneSearchUtil() {
		
		initializeCentralEurope();
		initializeBaltics();
		initializeDach();
		initalizeIreland();
		initializeBenelux();
		initializeNorthAmerica();
		initializeSouthernEurope();
		initializeEasternEurope();
		initializeNorthernEurope();
		initializeBritishIsles();
		initializeWesternEurope();
		initializeEurope();
		initializeWorld();
		
	}
	
	/**
	* Initializes the Southern Europe dataset 
	*/
	private static void initializeSouthernEurope() {
		
		SOUTHERN_EUROPE.add(COUNTRY.CROATIA);
		SOUTHERN_EUROPE.add(COUNTRY.GREECE);
		SOUTHERN_EUROPE.add(COUNTRY.ITALY);
		SOUTHERN_EUROPE.add(COUNTRY.MALTA);
		SOUTHERN_EUROPE.add(COUNTRY.PORTUGAL);
		SOUTHERN_EUROPE.add(COUNTRY.SLOVENIA);
		SOUTHERN_EUROPE.add(COUNTRY.SPAIN);
		SOUTHERN_EUROPE.add(COUNTRY.TURKEY);
	}
	
	/**
	* Initializes the Eastern Europe dataset 
	*/
	private static void initializeEasternEurope() {
		
		EASTERN_EUROPE.add(COUNTRY.BULGARIA);
		EASTERN_EUROPE.add(COUNTRY.CZECH_REPUBLIC);
		EASTERN_EUROPE.add(COUNTRY.HUNGARY);
		EASTERN_EUROPE.add(COUNTRY.POLAND);
		EASTERN_EUROPE.add(COUNTRY.ROMANIA);
		EASTERN_EUROPE.add(COUNTRY.SLOVAKIA);
		EASTERN_EUROPE.add(COUNTRY.UKRAINE);
		
	}
	
	/**
	* Initializes the Western Europe dataset 
	*/
	private static void initializeWesternEurope() {
		WESTERN_EUROPE.add(COUNTRY.BELGIUM);
		WESTERN_EUROPE.add(COUNTRY.FRANCE);
		WESTERN_EUROPE.addAll(BRITISH_ISLES);
		WESTERN_EUROPE.addAll(BENELUX);
	}
	
	/**
	* Initializes the Northern Europe dataset 
	*/
	private static void initializeNorthernEurope() {
		NORTHERN_EUROPE.add(COUNTRY.SWEDEN);
		NORTHERN_EUROPE.add(COUNTRY.FINLAND);
		NORTHERN_EUROPE.add(COUNTRY.LATVIA);
		NORTHERN_EUROPE.add(COUNTRY.LITHUANIA);
		NORTHERN_EUROPE.add(COUNTRY.ESTONIA);
		NORTHERN_EUROPE.add(COUNTRY.NORWAY);
		NORTHERN_EUROPE.add(COUNTRY.DENMARK);
		
	}
	
	/**
	* Initializes the Europe dataset 
	*/
	private static void initializeEurope() {
		EUROPE.addAll(BRITISH_ISLES);
		EUROPE.addAll(BENELUX);
		EUROPE.addAll(NORTHERN_EUROPE);
		EUROPE.addAll(EASTERN_EUROPE);
		EUROPE.addAll(SOUTHERN_EUROPE);
		EUROPE.addAll(WESTERN_EUROPE);
		EUROPE.addAll(CENTRAL_EUROPE);
	}
	
	/**
	* Initializes the North America dataset 
	*/
	private static void initializeNorthAmerica() {
		NORTH_AMERICA.add(COUNTRY.US);
		NORTH_AMERICA.add(COUNTRY.CANADA);
	}
	
	/**
	* Initializes the World dataset 
	*/
	private static void initializeWorld() {
		WORLD.addAll(EUROPE);
		WORLD.addAll(NORTH_AMERICA);
		WORLD.add(COUNTRY.INDIA);
		WORLD.add(COUNTRY.PAKISTAN);
	}
	
	/**
	* Initializes the Benelux dataset 
	*/
	private static void initializeBenelux() {
		BENELUX.add(COUNTRY.NETHERLANDS);
		BENELUX.add(COUNTRY.BELGIUM);
		BENELUX.add(COUNTRY.LUXEMBOURG);
	}
	
	/**
	* Initializes the British Isles dataset 
	*/
	private static void initializeBritishIsles() {
		BRITISH_ISLES.addAll(IRELAND);
		BRITISH_ISLES.add(COUNTRY.UK);
	}
	
	/**
	* Initializes the island of Ireland dataset 
	*/
	private static void initalizeIreland() {
		IRELAND.add(COUNTRY.NORTHERN_IRELAND);
		IRELAND.add(COUNTRY.REPUBLIC_OF_IRELAND);
	}
	
	/**
	* Initializes the island of Ireland dataset 
	*/
	private static void initializeDach() {
		DACH.add(COUNTRY.GERMANY);
		DACH.add(COUNTRY.AUSTRIA);
		DACH.add(COUNTRY.SWITZERLAND);
	}
	
	/**
	* Initializes the island of Ireland dataset 
	*/
	private static void initializeBaltics() {
		BALTICS.add(COUNTRY.ESTONIA);
		BALTICS.add(COUNTRY.LATVIA);
		BALTICS.add(COUNTRY.LITHUANIA);
	}
	
	/**
	* Initializes the island of Ireland dataset 
	*/
	private static void initializeCentralEurope() {
		CENTRAL_EUROPE.add(COUNTRY.AUSTRIA);
		CENTRAL_EUROPE.add(COUNTRY.CROATIA);
		CENTRAL_EUROPE.add(COUNTRY.CZECH_REPUBLIC);
		CENTRAL_EUROPE.add(COUNTRY.GERMANY);
		CENTRAL_EUROPE.add(COUNTRY.HUNGARY);
		CENTRAL_EUROPE.add(COUNTRY.LIECHTENSTEIN);
		CENTRAL_EUROPE.add(COUNTRY.LITHUANIA);
		CENTRAL_EUROPE.add(COUNTRY.POLAND);
		CENTRAL_EUROPE.add(COUNTRY.SLOVAKIA);
		CENTRAL_EUROPE.add(COUNTRY.SLOVENIA);
		CENTRAL_EUROPE.add(COUNTRY.SWITZERLAND);
	}

}