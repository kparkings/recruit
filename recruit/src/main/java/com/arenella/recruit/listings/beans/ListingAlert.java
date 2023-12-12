package com.arenella.recruit.listings.beans;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
* Represents an Alert configuration specifying what types of 
* Listings a User wants to be alerted about
* @author K Parkings
*/
public class ListingAlert {

	private UUID id;
	private Long userId;
	private String email;
	private LocalDate created;
	private Listing.listing_type contractType;
	private Set<Listing.country> countries = new LinkedHashSet<>();
	private Set<Listing.TECH> categories = new LinkedHashSet<>();
	
	/**
	* Builder for the class
	* @param builder - Contains initialization information
	*/
	public ListingAlert(ListingAlertBuilder builder) {
		this.id 			= builder.id;
		this.userId 		= builder.userId;
		this.email 			= builder.email;
		this.created 		= builder.created;
		this.contractType 	= builder.contractType;
		this.countries 		= builder.countries;
		this.categories 	= builder.categories;
		
	}
	
	/**
	* Initialzies the attributes that need to be set when the object is first created
	*/
	public void initializeAlert() {
		if(Optional.ofNullable(id).isPresent() || Optional.ofNullable(created).isPresent()) {
			throw new IllegalStateException("You cand initalize the ListingAlert twice");
		}
		this.id 		= UUID.randomUUID();
		this.created 	= LocalDate.now();
	}
	
	/**
	* Sets the unique id of the User who owns the Alert
	* @param userId - Unique id of the User
	*/
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	
	/**
	* Returns the unique id of the alert
	* @return id of the alert
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns unique if of the user if known.
	* @return id of the user who owns the alert
	*/
	public Optional<Long> getUserId() {
		return Optional.ofNullable(this.userId);
	}
	
	/**
	* Returns the email of the owner of the Alert
	* @return owner of alerts email address
	*/
	public String getEmail() {
		return this.email;
	}
	
	/**
	* Returns the Date the Alert was created
	* @return when the Alter was created
	*/
	public LocalDate getCreated() {
		return this.created;
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
	public Set<Listing.country> getCountries() {
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
	public static ListingAlertBuilder builder() {
		return new ListingAlertBuilder();
	}

	/**
	* Builder for the class
	* @author K Parkings
	*/
	public static class ListingAlertBuilder {
	
		private UUID 					id;
		private Long 					userId;
		private String 					email;
		private LocalDate 				created;
		private Listing.listing_type 	contractType;
		private Set<Listing.country> 	countries 		= new LinkedHashSet<>();
		private Set<Listing.TECH> 		categories 		= new LinkedHashSet<>();
		
		/**
		* Sets the unique if of the Alert
		* @param id - unique id
		* @return Builder
		*/
		public ListingAlertBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the unique if of the user who owns the Alter
		* @param userId - id of the user
		* @return Builder
		*/
		public ListingAlertBuilder userId(Long userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets the email of the USer
		* @param email - email of the owner of the Alert
		* @return Builder
		*/
		public ListingAlertBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets when the Alter was created
		* @param created - when Alert was created
		* @return Builder
		*/
		public ListingAlertBuilder created(LocalDate created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the contract types the User in interested in receiving Alerts for
		* @param contractType - contract types of interest
		* @return Builder
		*/
		public ListingAlertBuilder contractType(Listing.listing_type contractType) {
			this.contractType = contractType;
			return this;
		}
		
		/**
		* Sets the countries of interest
		* @param countries - countries to receive Alerts for
		* @return Builder
		*/
		public ListingAlertBuilder countries(Set<Listing.country> countries) {
			this.countries.clear();
			this.countries.addAll(countries);
			return this;
		}
		
		/**
		* Sets the categories of interest
		* @param categories - Categories to receive alerts for
		* @return Builder
		*/
		public ListingAlertBuilder categories(Set<Listing.TECH> categories) {
			this.categories.clear();
			this.categories.addAll(categories);
			return this;
		}
		
		/**
		* Returns initialized instance
		* @return Initialized instance
		*/
		public ListingAlert build() {
			return new ListingAlert(this);
		}
		
	}

}