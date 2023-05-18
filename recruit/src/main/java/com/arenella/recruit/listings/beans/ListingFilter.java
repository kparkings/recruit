package com.arenella.recruit.listings.beans;

import java.util.Optional;
import java.util.UUID;

import com.arenella.recruit.listings.beans.Listing.LISTING_AGE;
import com.arenella.recruit.listings.beans.Listing.country;
import com.arenella.recruit.listings.beans.Listing.listing_type;

/**
* Filters to apply to a Search for Listing objects
* @author K Parkings
*/
public class ListingFilter {

	private UUID 				listingId;
	private String				ownerId;
	private listing_type 		type;
	private country 			country;
	private LISTING_AGE			listingAge;
	private Boolean				active;
	
	/**
	* Constructor Based upon a Builder
	* @param builder - Contains initialization values
	*/
	public ListingFilter(ListingFilterBuilder builder) {
		this.listingId 		= builder.listingId;
		this.ownerId 		= builder.ownerId;
		this.type 			= builder.type;
		this.country 		= builder.country;
		this.listingAge		= builder.listingAge;
		this.active			= builder.active;
	}
	
	/**
	* Returns listingId to filter on
	* @return listing Id to filter on
	*/
	public Optional<UUID> getListingId(){
		return Optional.ofNullable(this.listingId);
	}
	
	/**
	* Returns the ownerId to filter on
	* @return owner Id to filter on
	*/
	public Optional<String> getOwnerId(){
		return Optional.ofNullable(this.ownerId);
	}
	
	/**
	* Returns the listing type to filter on
	* @return listing type to filter on
	*/
	public Optional<listing_type> getType() {
		return Optional.ofNullable(this.type);
	}
	
	/**
	* Returns the country to filter on
	* @return country to filter on
	*/
	public Optional<country> getCountry(){
		return Optional.ofNullable(country);
	}
	
	/**
	* Returns the age to filter on
	* @return period the listing was posted in
	*/
	public Optional<LISTING_AGE> getListingAge(){
		return Optional.ofNullable(this.listingAge);
	}
	
	/**
	* Returns the active value to filter on
	* @return if selected to filter on active nor active
	*/
	public Optional<Boolean> getActive(){
		return Optional.ofNullable(this.active);
	}
	
	/**
	* Returns a Builder for the ListingFilter class
	* @return
	*/
	public static ListingFilterBuilder builder() {
		return new ListingFilterBuilder();
	}
	
	/**
	* Builder for ListingFiler
	* @author K Parkings
	*/
	public static class ListingFilterBuilder {
	
		private UUID 				listingId;
		private String				ownerId;
		private listing_type 		type;
		private country 			country;
		private LISTING_AGE			listingAge;
		private Boolean				active;
		
		/**
		* Unique listingId to filter on
		* @param listingId - Unique identifier of the Listing
		* @return Builder
		*/
		public ListingFilterBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Unique id of the listing owner to filter on
		* @param ownerId - Unique identifier of the owner
		* @return Builder
		*/
		public ListingFilterBuilder ownerId(String ownerId) {
			this.ownerId = ownerId;
			return this;
		}
		
		/**
		* Type of listing to filter on
		* @param type - listing type
		* @return Builder
		*/
		public ListingFilterBuilder type(listing_type type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the country to filter on 
		* @param country - country to filter on
		* @return Builder
		*/
		public ListingFilterBuilder country(country country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets the period the listing was posted in to filter on
		* @param listingAge - when the listing was posted
		* @return Builder
		*/
		public ListingFilterBuilder listingAge(LISTING_AGE listingAge) {
			this.listingAge = listingAge;
			return this;
		}
		
		/**
		* Sets whether to filter on the active state
		* @param active - to filter on active / not active
		* @return Builder
		*/
		public ListingFilterBuilder active(boolean active) {
			this.active = active;
			return this;
		}
		
		/**
		* Returns an instance of ListingFilter initialized
		* with the values in the Builder
		* @return initialized instance of ListingFilter
		*/
		public ListingFilter build() {
			return new ListingFilter(this);
		}
		
	}
	
}