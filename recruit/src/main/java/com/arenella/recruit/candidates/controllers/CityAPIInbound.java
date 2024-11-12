package com.arenella.recruit.candidates.controllers;

import com.arenella.recruit.candidates.beans.City;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* API Inbound representation of a City 
*/
@JsonDeserialize(builder=CityAPIInbound.CityAPIInboundBuilder.class)
public class CityAPIInbound {
	
	private final COUNTRY 	country;
	private final String 	name;
	private final float 	lat;
	private final float 	lon;
	private final boolean 	active;
	
	/**
	* Constructor
	* @param builder - Contains initialization values
	*/
	public CityAPIInbound(CityAPIInboundBuilder builder) {
		this.country 	= builder.country;
		this.name 		= builder.name;
		this.lat 		= builder.lat;
		this.lon 		= builder.lon;
		this.active		= builder.active;
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
	* Whether the City is active or disabled
	* @return Whether the City is active
	*/
	public boolean isActive() {
		return this.active;
	}
	
	/**
	* Returns a Builder for the City class
	* @return Builder
	*/
	public static CityAPIInboundBuilder builder() {
		return new CityAPIInboundBuilder();
	}
	
	/**
	* Builder for the City class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class CityAPIInboundBuilder{
	
		private COUNTRY country;
		private String 	name;
		private float 	lat;
		private float 	lon;
		private boolean active;
		
		/**
		* Sets the 2 digit Country code of the Country 
		* the City belongs to
		* @param country - 2 digit country code
		* @return Builder
		*/
		public CityAPIInboundBuilder country(COUNTRY country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets the name of the City
		* @param name - name of the City
		* @return Builder
		*/
		public CityAPIInboundBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		/**
		* Sets latitude of the City
		* @param lat - lat position
		* @return Builder
		*/
		public CityAPIInboundBuilder lat(float lat) {
			this.lat = lat;
			return this;
		}
		
		/**
		* Sets the longitude of the City
		* @param lon - lon position
		* @return Builder
		*/
		public CityAPIInboundBuilder lon(float lon) {
			this.lon = lon;
			return this;
		}
		
		/**
		* Sets whether or not the City is active
		* @param active - whether the city is active
		* @return Builder
		*/
		public CityAPIInboundBuilder active(boolean active) {
			this.active = active;
			return this;
		}
		
		/**
		* Reutrns initialized instance of the City
		* @return City
		*/
		public CityAPIInbound build() {
			return new CityAPIInbound(this);
		}
		
	}
	
	/**
	* Converts from domain to API Inbound representation of a Cuty
	* @param city - City to convert
	* @return API Inbound representation
	*/
	public static City convertFromAPIInbound(CityAPIInbound city) {
		return City.builder().country(city.getCountry()).name(city.getName()).lat(city.getLat()).lon(city.getLon()).active(city.isActive()).build();
	}
	
}
