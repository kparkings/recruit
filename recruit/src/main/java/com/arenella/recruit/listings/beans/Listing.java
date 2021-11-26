package com.arenella.recruit.listings.beans;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
* Class represents a Listing made by a recruiter for some type of 
* adevertised role
* @author K Parkings
*/
public class Listing {

	public static enum listing_type {CONTRACT_ROLE, PERM_ROLE};
	public static enum country 		{NETHERLANDS, BELGIUM, UK, IRELAND, EU};
	public static enum language 	{DUTCH, FRENCH, ENGLISH};
	public static enum currency		{EUR, GBP};
	
	private UUID 				listingId;
	private String				ownerId;
	private LocalDate 			created;
	private String 				title;
	private String 				description;
	private listing_type 		type;
	private country 			country;
	private String 				location;
	private int 				yearsExperience;
	private Set<language> 		languages			= new LinkedHashSet<>();
	private float 				rate;
	private currency			currency;
	//private int views;
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains details to populate instance of Listing
	*/
	public Listing(ListingBuilder builder) {
		
		this.listingId 			= builder.listingId;
		this.ownerId 			= builder.ownerId;
		this.created 			= builder.created;
		this.title 				= builder.title;
		this.description 		= builder.description;
		this.type 				= builder.type;
		this.country 			= builder.country;
		this.location 			= builder.location;
		this.yearsExperience 	= builder.yearsExperience;
		this.rate 				= builder.rate;
		this.currency 			= builder.currency;
		
		this.languages.addAll(builder.languages);
		
	}
	
	/**
	* Returns the unique Identifier of the Listing
	* @return Unique Id of the Listing
	*/
	public UUID getListingId() {
		return this.listingId;
	}
	
	/**
	* Returns the Unique Id of the Owner
	* @return Unique Id of the owner
	*/
	public String getOwnerId() {
		return this.ownerId;
	}
	
	/**
	* Returns the Date the Listing was posted
	* @return Date Listing was posted
	*/
	public LocalDate getCreated() {
		return this.created;
	}
	
	/**
	* Returns the Title for the Listing
	* @return Descriptive Title for the Listing
	*/
	public String getTitle() {
		return this.title;
	}
	
	/**
	* Returns the full description of the Listing
	* @return Body of Listing
	*/
	public String getDescription() {
		return this.description;
	}
	
	/**
	* Returns the Type of Listing. For example if it is a listing
	* for a perm or contract role
	* @return Type of Listing
	*/
	public listing_type getType() {
		return this.type;
	}
	
	/**
	* Returns the Country the Listing is for
	* @return Country location of the Listing
	*/
	public country getCountry() {
		return this.country;
	}
	
	/**
	* Returns the Location within the country for the Listing
	* @return Probably the City name 
	*/
	public String getLocation() {
		return this.location;
	}
	
	/**
	* Returns the number of years experience the Candidate must have
	* @return Years of experience
	*/
	public int getYearsExperience() {
		return this.yearsExperience;
	}
	
	/**
	* Returns the Languages requested
	* @return Requested Languages for the Listing
	*/
	public Set<language> getLanguages() {
		return this.languages;
	}
	
	/**
	* Returns the rate being offered
	* @return rate offered for the Listing
	*/
	public float getRate() {
		return this.rate;
	}
	
	/**
	* Currency being used for the rate in the Listing
	* @return Currency for the Rate
	*/
	public currency	getCurrency() {
		return this.currency;
	}
	
	/**
	* Generates a unique Id for the Listing
	*/
	public void generateListingId() {
		this.listingId = UUID.randomUUID();
	}
	
	/**
	* Sets the unique id of the owner of the Listing
	* @param ownerId - Unique Id of the Owner
	*/
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	/**
	* Returns an instance of a Builder for the Listing class
	* @return Builder
	*/
	public static ListingBuilder builder() {
		return new ListingBuilder();
	}
	
	/**
	* Builder for the Listing class
	* @author K Parkings
	*/
	public static class ListingBuilder{

		private UUID 				listingId;
		private String				ownerId;
		private LocalDate 			created;
		private String 				title;
		private String 				description;
		private listing_type 		type;
		private country 			country;
		private String 				location;
		private int 				yearsExperience;
		private Set<language> 		languages			= new LinkedHashSet<>();
		private float 				rate;
		private currency			currency;
		
		/**
		* Sets the Unique Identifier for the Listing
		* @param listingId - Unique Id of the Listing
		* @return Builder
		*/
		public ListingBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets the Unique Id of the Owner of the Listing
		* @param ownerId - Unique Id of the Owner
		* @return Builder
		*/
		public ListingBuilder ownerId(String ownerId) {
			this.ownerId = ownerId;
			return this;
		}
		
		/**
		* Sets the Date the Listing was created
		* @param created - Date of creation 
		* @return Builder
		*/
		public ListingBuilder created(LocalDate created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets a title for the Listing
		* @param title - Title for the Listing
		* @return Builder
		*/
		public ListingBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		/**
		* Sets the full description / body for the Listing
		* @param description - Information about the listing
		* @return Builder
		*/
		public ListingBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		/**
		* Sets the type of the Listing
		* @param type - contract/perm etc
		* @return Builder
		*/
		public ListingBuilder type(listing_type type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the Country the Listing is associated with
		* @param country - Where the Listing is for
		* @return Builder
		*/
		public ListingBuilder country(country country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets location the listing is for (i.e City / Remote etc)
		* @param location - Location where the Listing is based
		* @return Builder
		*/
		public ListingBuilder location(String location) {
			this.location = location;
			return this;
		}
		
		/**
		* Sets the years experiences required for the Listing
		* @param yearsExperience - Years experience expected from the candidate
		* @return Builder
		*/
		public ListingBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets the language required for the Listing
		* @param languages - Desired languages for the Listing
		* @return Builder
		*/
		public ListingBuilder languages(Set<language> languages) {
			this.languages.clear();
			this.languages.addAll(languages);
			return this;
			
		}
		
		/**
		* Sets the Rate being offered for the Listing
		* @param rate - Rate offered for the Listing
		* @return Builder
		*/
		public ListingBuilder rate(float rate) {
			this.rate = rate;
			return this;
		}
		
		/**
		* Sets the Currency associated with the Rate
		* @param currency - Currency associated with the Rate
		* @return Builder
		*/
		public ListingBuilder currency(currency currency) {
			this.currency = currency;
			return this;
		}
		
		/**
		* Returns an instantiated instance of the Listing class
		* @return instance of Listing
		*/
		public Listing build() {
			return new Listing(this);
		}
		
	}
	
}