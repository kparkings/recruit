package com.arenella.recruit.candidates.entities;

import java.io.Serializable;

import com.arenella.recruit.candidates.beans.City;
import com.arenella.recruit.candidates.enums.COUNTRY;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/**
* Entity representation of a City in a Country 
*/
@Entity
@Table(schema="candidate", name="city")
public class CityEntity {
	
	@EmbeddedId
	private CityId id;
	
	@Column(name="lat")
	private float 	lat;
	
	@Column(name="lon")
	private float 	lon;
	
	@Column(name="active")
	public boolean active;
	
	/**
	* Default Constructor 
	*/
	public CityEntity() {
		//Hibernate
	}
	
	/**
	* Constructor
	* @param country - Name of the country the City belongs to
	* @param name	 - Name of the City
	* @param lat	 - Latitude position to 2 decimal places 
	* @param lon	 - Longitude position to 2 decimal places
	* @param active	 - If the City has been activated or is awaiting activation
	*/
	public CityEntity(COUNTRY country, String name, float lat, float lon, boolean active) {
		this.id 			= new CityId(country, name);
		this.lat 			= lat;
		this.lon 			= lon;
		this.active			= active;
	}

	/**
	* Returns the name of the Country the City belongs to
	* @return Name of the Country
	*/
	public COUNTRY getCountry() {
		return this.id.country;
	}
	
	/**
	* Returns the name of the City
	* @return name of the City
	*/
	public String getName() {
		return this.id.name;
	}
	
	/**
	* latitude position of the City. 2 decimal points
	* @return lat position
	*/
	public float getLat() {
		return this.lat;
	}
	
	/**
	* Longitude position of the City. 2 decimal points
	* @return lon position
	*/
	public float getLon() {
		return this.lon;
	}
	
	/**
	* Returns whether the City is active in the system
	* @return whether city has been activated by the Admin
	*/
	public boolean isActive() {
		return this.active;
	}
	
	/**
	* Id for the City Entity
	*/
	@Embeddable
	public static class CityId implements Serializable{

		private static final long serialVersionUID = 615419116493228418L;
		
		@Column(name="country")
		@Enumerated(EnumType.STRING)
		private COUNTRY country;
		
		@Column(name="city")
		private String name;
		
		/**
		* Default constructor 
		*/
		public CityId() {
			//Hibernate
		}
		
		/**
		* Constructor
		* @param country - country
		* @param name	 - City name
		*/
		public CityId(COUNTRY country, String name) {
			this.country 	= country;
			this.name 		= name;
		}
		
		/**
		* Returns the country code
		* @return country code
		*/
		public COUNTRY getCountry() {
			return this.country;
		}
		
		/**
		* Returns the name of the city
		* @return city name
		*/
		public String getName() {
			return this.name;
		}
		
		/**
		* Sets the country
		* @param country - country code
		*/
		public void setCountry(COUNTRY country) {
			this.country = country;
		}
		
		/**
		* sets the city name
		* @param city - name of the city
		*/
		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	/**
	* Converts from Entity to Domain representation
	* @param entity - To convert
	* @return Domain representation of City
	*/
	public static City fromEntity(CityEntity entity) {
		return City
				.builder()
					.country(entity.getCountry())
					.name(entity.getName())
					.lat(entity.getLat())
					.lon(entity.getLon())
					.active(entity.isActive())
				.build();
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param city - To convert
	* @return Entity representation of City
	*/
	public static CityEntity toEntity(City city) {
		return new CityEntity(city.getCountry(), city.getName(), city.getLat(), city.getLon(), city.isActive());
	}
	
}