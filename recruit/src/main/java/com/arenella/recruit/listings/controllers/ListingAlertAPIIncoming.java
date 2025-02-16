package com.arenella.recruit.listings.controllers;

import java.util.LinkedHashSet;
import java.util.Set;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingAlert;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* API Incomming representation of a ListingAlert
* @author K Parkings
*/
@JsonDeserialize(builder=ListingAlertAPIIncoming.ListingAlertAPIIncomingBuilder.class)
public class ListingAlertAPIIncoming {

	private String email;
	private Listing.listing_type contractType;
	private Set<Listing.Country> countries = new LinkedHashSet<>();
	private Set<Listing.TECH> categories = new LinkedHashSet<>();
	
	/**
	* Builder for the class
	* @param builder - Contains initialization information
	*/
	public ListingAlertAPIIncoming(ListingAlertAPIIncomingBuilder builder) {
		this.email 			= builder.email;
		this.contractType 	= builder.contractType;
		this.countries 		= builder.countries;
		this.categories 	= builder.categories;
	}
	
	/**
	* Returns the email of the owner of the Alert
	* @return owner of alerts email address
	*/
	public String getEmail() {
		return this.email;
	}
	
	/**
	* Returns the types of contract the owner of the Alert is
	* interested in hearing about
	* @return types of interest
	*/
	public Listing.listing_type getContractType() {
		return this.contractType;
	}
	
	/**
	* Returns the countries the owner of the Alert is interested in 
	* hearing about
	* @return countries of interest
	*/
	public Set<Listing.Country> getCountries() {
		return this.countries;
	}
	
	/**
	* Returns the categories the owner of the Alert is interested in 
	* hearing about
	* @return categoties of interest
	*/
	public Set<Listing.TECH> getCategories() {
		return this.categories;
	}
	
	/**
	* Returns a Builder for the class
	* @return Builder
	*/
	public static ListingAlertAPIIncomingBuilder builder() {
		return new ListingAlertAPIIncomingBuilder();
	}

	/**
	* Builder for the class
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class ListingAlertAPIIncomingBuilder {
	
		private String 					email;
		private Listing.listing_type 	contractType;
		private Set<Listing.Country> 	countries 		= new LinkedHashSet<>();
		private Set<Listing.TECH> 		categories 		= new LinkedHashSet<>();
		
		/**
		* Sets the email of the USer
		* @param email - email of the owner of the Alert
		* @return Builder
		*/
		public ListingAlertAPIIncomingBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the contract types the User in interested in receiving Alerts for
		* @param contractType - contract types of interest
		* @return Builder
		*/
		public ListingAlertAPIIncomingBuilder contractType(Listing.listing_type contractType) {
			this.contractType = contractType;
			return this;
		}
		
		/**
		* Sets the countries of interest
		* @param countries - countries to receive Alerts for
		* @return Builder
		*/
		public ListingAlertAPIIncomingBuilder countries(Set<Listing.Country> countries) {
			this.countries.clear();
			this.countries.addAll(countries);
			return this;
		}
		
		/**
		* Sets the categories of interest
		* @param categories - Categories to receive alerts for
		* @return Builder
		*/
		public ListingAlertAPIIncomingBuilder categories(Set<Listing.TECH> categories) {
			this.categories.clear();
			this.categories.addAll(categories);
			return this;
		}
		
		/**
		* Returns initialized instance
		* @return Initialized instance
		*/
		public ListingAlertAPIIncoming build() {
			return new ListingAlertAPIIncoming(this);
		}
		
	}
	
	/**
	* Converts from Incoming API representation of the ListingAltert to 
	* the Domain representation
	* @param alert - Alert to convert
	* @return Domain representation
	*/
	public static ListingAlert convertToDomain(ListingAlertAPIIncoming alert) {
		return ListingAlert
				.builder()
					.categories(alert.getCategories())
					.contractType(alert.getContractType())
					.countries(alert.getCountries())
					.email(alert.getEmail())
				.build();
	}
	
}