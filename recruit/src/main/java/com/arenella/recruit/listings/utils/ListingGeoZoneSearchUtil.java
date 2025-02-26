package com.arenella.recruit.listings.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.arenella.recruit.listings.beans.Listing;

/**
* Util to return supported Countries in a Geo zone 
* @author K Parkings
*/
@Component
public class ListingGeoZoneSearchUtil {

	public enum GEO_ZONE {
		WORLD, 
		EUROPE, 
		NORTH_AMERICA, 
		SOUTHERN_EUROPE, 
		EASTERN_EUROPE, 
		WESTERN_EUROPE, 
		NORTHERN_EUROPE, 
		CENTRAL_EUROPE, 
		BENELUX, 
		BRITISH_ISLES, 
		IRELAND, 
		DACH
		}
	
	private static final Set<Listing.Country> WORLD 			= new HashSet<>();
	private static final Set<Listing.Country> EUROPE 			= new HashSet<>();
	private static final Set<Listing.Country> NORTH_AMERICA 	= new HashSet<>();
	private static final Set<Listing.Country> SOUTHERN_EUROPE 	= new HashSet<>();
	private static final Set<Listing.Country> EASTERN_EUROPE 	= new HashSet<>();
	private static final Set<Listing.Country> WESTERN_EUROPE 	= new HashSet<>();
	private static final Set<Listing.Country> NORTHERN_EUROPE 	= new HashSet<>();
	private static final Set<Listing.Country> CENTRAL_EUROPE 	= new HashSet<>();
	private static final Set<Listing.Country> BENELUX  			= new HashSet<>();
	private static final Set<Listing.Country> BRITISH_ISLES  	= new HashSet<>();
	private static final Set<Listing.Country> IRELAND  			= new HashSet<>();
	private static final Set<Listing.Country> DACH  			= new HashSet<>();
	private static final Set<Listing.Country> BALTICS  			= new HashSet<>();
	
	/**
	* Returns the countries in the requested GEO_ZONES
	* @param geoZones - Geographical zones containing multiple Countries
	* @return Countries in selected GEO_ZONE's
	*/
	public static Set<Listing.Country> fetchCountriesFor(GEO_ZONE... geoZones ){
		
		Set<Listing.Country> countries = new HashSet<>();
		
		Arrays.stream(geoZones).forEach(geoZone -> {
			switch(geoZone) {
				case WORLD 				-> countries.addAll(WORLD);
				case EUROPE 			-> countries.addAll(EUROPE);
				case NORTH_AMERICA 		-> countries.addAll(NORTH_AMERICA);
				case SOUTHERN_EUROPE 	-> countries.addAll(SOUTHERN_EUROPE);
				case EASTERN_EUROPE 	-> countries.addAll(EASTERN_EUROPE);
				case WESTERN_EUROPE 	-> countries.addAll(WESTERN_EUROPE);
				case NORTHERN_EUROPE 	-> countries.addAll(NORTHERN_EUROPE);
				case CENTRAL_EUROPE 	-> countries.addAll(CENTRAL_EUROPE);
				case BENELUX 			-> countries.addAll(BENELUX);
				case BRITISH_ISLES 		-> countries.addAll(BRITISH_ISLES);
				case IRELAND 			-> countries.addAll(IRELAND);
				case DACH				-> countries.addAll(DACH);
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
		
		SOUTHERN_EUROPE.add(Listing.Country.CROATIA);
		SOUTHERN_EUROPE.add(Listing.Country.GREECE);
		SOUTHERN_EUROPE.add(Listing.Country.ITALY);
		SOUTHERN_EUROPE.add(Listing.Country.MALTA);
		SOUTHERN_EUROPE.add(Listing.Country.PORTUGAL);
		SOUTHERN_EUROPE.add(Listing.Country.SLOVENIA);
		SOUTHERN_EUROPE.add(Listing.Country.SPAIN);
		SOUTHERN_EUROPE.add(Listing.Country.TURKEY);
	}
	
	/**
	* Initializes the Eastern Europe dataset 
	*/
	private static void initializeEasternEurope() {
		
		EASTERN_EUROPE.add(Listing.Country.BULGARIA);
		EASTERN_EUROPE.add(Listing.Country.CZECH_REPUBLIC);
		EASTERN_EUROPE.add(Listing.Country.HUNGARY);
		EASTERN_EUROPE.add(Listing.Country.POLAND);
		EASTERN_EUROPE.add(Listing.Country.ROMANIA);
		EASTERN_EUROPE.add(Listing.Country.SLOVAKIA);
		EASTERN_EUROPE.add(Listing.Country.UKRAINE);
		
	}
	
	/**
	* Initializes the Western Europe dataset 
	*/
	private static void initializeWesternEurope() {
		WESTERN_EUROPE.add(Listing.Country.BELGIUM);
		WESTERN_EUROPE.add(Listing.Country.FRANCE);
		WESTERN_EUROPE.addAll(BRITISH_ISLES);
		WESTERN_EUROPE.addAll(BENELUX);
	}
	
	/**
	* Initializes the Northern Europe dataset 
	*/
	private static void initializeNorthernEurope() {
		NORTHERN_EUROPE.add(Listing.Country.SWEDEN);
		NORTHERN_EUROPE.add(Listing.Country.FINLAND);
		NORTHERN_EUROPE.add(Listing.Country.LATVIA);
		NORTHERN_EUROPE.add(Listing.Country.LITHUANIA);
		NORTHERN_EUROPE.add(Listing.Country.ESTONIA);
		NORTHERN_EUROPE.add(Listing.Country.NORWAY);
		NORTHERN_EUROPE.add(Listing.Country.DENMARK);
		
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
		NORTH_AMERICA.add(Listing.Country.US);
		NORTH_AMERICA.add(Listing.Country.CANADA);
	}
	
	/**
	* Initializes the World dataset 
	*/
	private static void initializeWorld() {
		WORLD.addAll(EUROPE);
		WORLD.addAll(NORTH_AMERICA);
		WORLD.add(Listing.Country.INDIA);
		WORLD.add(Listing.Country.PAKISTAN);
	}
	
	/**
	* Initializes the Benelux dataset 
	*/
	private static void initializeBenelux() {
		BENELUX.add(Listing.Country.NETHERLANDS);
		BENELUX.add(Listing.Country.BELGIUM);
		BENELUX.add(Listing.Country.LUXEMBOURG);
	}
	
	/**
	* Initializes the British Isles dataset 
	*/
	private static void initializeBritishIsles() {
		BRITISH_ISLES.addAll(IRELAND);
		BRITISH_ISLES.add(Listing.Country.UK);
	}
	
	/**
	* Initializes the island of Ireland dataset 
	*/
	private static void initalizeIreland() {
		IRELAND.add(Listing.Country.NORTHERN_IRELAND);
		IRELAND.add(Listing.Country.REPUBLIC_OF_IRELAND);
	}
	
	/**
	* Initializes the island of Ireland dataset 
	*/
	private static void initializeDach() {
		DACH.add(Listing.Country.GERMANY);
		DACH.add(Listing.Country.AUSTRIA);
		DACH.add(Listing.Country.SWITZERLAND);
	}
	
	/**
	* Initializes the island of Ireland dataset 
	*/
	private static void initializeBaltics() {
		BALTICS.add(Listing.Country.ESTONIA);
		BALTICS.add(Listing.Country.LATVIA);
		BALTICS.add(Listing.Country.LITHUANIA);
	}
	
	/**
	* Initializes the island of Ireland dataset 
	*/
	private static void initializeCentralEurope() {
		CENTRAL_EUROPE.add(Listing.Country.AUSTRIA);
		CENTRAL_EUROPE.add(Listing.Country.CROATIA);
		CENTRAL_EUROPE.add(Listing.Country.CZECH_REPUBLIC);
		CENTRAL_EUROPE.add(Listing.Country.GERMANY);
		CENTRAL_EUROPE.add(Listing.Country.HUNGARY);
		CENTRAL_EUROPE.add(Listing.Country.LIECHTENSTEIN);
		CENTRAL_EUROPE.add(Listing.Country.LITHUANIA);
		CENTRAL_EUROPE.add(Listing.Country.POLAND);
		CENTRAL_EUROPE.add(Listing.Country.SLOVAKIA);
		CENTRAL_EUROPE.add(Listing.Country.SLOVENIA);
		CENTRAL_EUROPE.add(Listing.Country.SWITZERLAND);
	}

}