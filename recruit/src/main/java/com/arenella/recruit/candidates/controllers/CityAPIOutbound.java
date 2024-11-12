package com.arenella.recruit.candidates.controllers;

import com.arenella.recruit.candidates.beans.City;
import com.arenella.recruit.candidates.enums.COUNTRY;

/**
* API Outbound representation of a City 
*/
public class CityAPIOutbound {
	
	private final COUNTRY country;
	private final String name;
	private final float lat;
	private final float lon;
	
	/**
	* Constructor
	* @param builder - Contains initialization values
	*/
	public CityAPIOutbound(CityAPIOutboundBuilder builder) {
		this.country 	= builder.country;
		this.name 		= builder.name;
		this.lat 		= builder.lat;
		this.lon 		= builder.lon;
	}
	
	/**
	* Returns 2 digit code for coutry
	* @return country city belongs to
	*/
	public COUNTRY getCountry() {
		return this.country;
	}
	
	/**
	* Returns the name of the City
	* @return name of the City
	*/
	public String getName() {
		return this.name;
	}
	
	/**
	* Returns the latitude of the city
	* @return lat
	*/
	public float getLat() {
		return this.lat;
	}
	
	/**
	* Returns the longitude of the City
	* @return lon
	*/
	public float getLon() {
		return this.lon;
	}
	
	/**
	* Returns a Builder for the City class
	* @return Builder
	*/
	public static CityAPIOutboundBuilder builder() {
		return new CityAPIOutboundBuilder();
	}
	
	/**
	* Builder for the City class 
	*/
	public static class CityAPIOutboundBuilder{
	
		private COUNTRY 	country;
		private String 		name;
		private float 		lat;
		private float 		lon;
		
		/**
		* Sets the 2 digit Country code of the Country 
		* the City belongs to
		* @param country - 2 digit country code
		* @return Builder
		*/
		public CityAPIOutboundBuilder country(COUNTRY country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets the name of the City
		* @param name - name of the City
		* @return Builder
		*/
		public CityAPIOutboundBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		/**
		* Sets latitude of the City
		* @param lat - lat position
		* @return Builder
		*/
		public CityAPIOutboundBuilder lat(float lat) {
			this.lat = lat;
			return this;
		}
		
		/**
		* Sets the longitude of the City
		* @param lon - lon position
		* @return Builder
		*/
		public CityAPIOutboundBuilder lon(float lon) {
			this.lon = lon;
			return this;
		}
		
		/**
		* Reutrns initialized instance of the City
		* @return City
		*/
		public CityAPIOutbound build() {
			return new CityAPIOutbound(this);
		}
		
	}
	
	/**
	* Converts from domain to API Outbound represenation of a Cuty
	* @param city - City to convert
	* @return API Outbound representation
	*/
	public static CityAPIOutbound convertToAPIOutbound(City city) {
		return CityAPIOutbound.builder().country(city.getCountry()).name(city.getName()).lat(city.getLat()).lon(city.getLon()).build();
	}
	
}
