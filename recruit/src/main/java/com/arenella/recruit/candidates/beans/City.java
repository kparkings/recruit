package com.arenella.recruit.candidates.beans;

/**
* Class to represent a City in a Country 
*/
public class City {

	private final String 	country;
	private final String 	name;
	private final float 	lat;
	private final float 	lon;
	private final boolean 	active;
	
	/**
	* Constructor
	* @param builder - Contains initialization values
	*/
	public City(CityBuilder builder) {
		this.country 	= builder.country.toUpperCase();
		this.name 		= builder.name.toLowerCase();
		this.lat 		= builder.lat;
		this.lon 		= builder.lon;
		this.active		= builder.active;
	}
	
	/**
	* Returns 2 digit code for coutry
	* @return country city belongs to
	*/
	public String getCountry() {
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
	* Returns whether the City has been activated 
	* for use in the system by the Admin
	* @return whether the City is activated
	*/
	public boolean isActive() {
		return this.active;
	}
	
	/**
	* Returns a Builder for the City class
	* @return Builder
	*/
	public static CityBuilder builder() {
		return new CityBuilder();
	}
	
	/**
	* Builder for the City class 
	*/
	public static class CityBuilder{
	
		private String 	country;
		private String 	name;
		private float 	lat;
		private float 	lon;
		private boolean  active;
		
		/**
		* Sets the 2 digit Country code of the Country 
		* the City belongs to
		* @param country - 2 digit country code
		* @return Builder
		*/
		public CityBuilder country(String country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets the name of the City
		* @param name - name of the City
		* @return Builder
		*/
		public CityBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		/**
		* Sets latitude of the City
		* @param lat - lat position
		* @return Builder
		*/
		public CityBuilder lat(float lat) {
			this.lat = lat;
			return this;
		}
		
		/**
		* Sets the longitude of the City
		* @param lon - lon position
		* @return Builder
		*/
		public CityBuilder lon(float lon) {
			this.lon = lon;
			return this;
		}
		
		/**
		* Sets whether the City has been activated and available 
		* for use in the system
		* @param active - Whether the City has been activated
		* @return Builder
		*/
		public CityBuilder active(boolean active) {
			this.active = active;
			return this;
		}
		
		/**
		* Reutrns initialized instance of the City
		* @return City
		*/
		public City build() {
			return new City(this);
		}
		
	}
	
}