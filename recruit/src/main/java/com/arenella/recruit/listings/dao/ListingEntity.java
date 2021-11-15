package com.arenella.recruit.listings.dao;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
* Entity representation of a Listing
* @author K Parkings
*/
@Entity
@Table(schema="listings", name="listing")
public class ListingEntity {

	@Id
	private UUID listingId;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public ListingEntity(ListingEntityBuilder builder) {
		this.listingId = builder.listingId;
	}
	
	/**
	* Returns the unique identifier for the Listing
	* @return Unique identifier for the Listing
	*/
	public UUID getListingId() {
		return this.listingId;
	}
	
	/**
	* Returns a Builder for the ListingEntity class
	* @return Builder for ListingEntity
	*/
	public static ListingEntityBuilder builder() {
		return new ListingEntityBuilder();
	}
	
	/**
	* Builder for the ListingEntity class
	* @author K Parkings
	*/
	public static class ListingEntityBuilder{
	
		private UUID listingId;
		
		/**
		* Sets the Unique identifier of the ListingEntity
		* @param listingId - Unique Identifier
		* @return Builder
		*/
		public ListingEntityBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Returns a new Instance of ListingEntity initalized with the 
		* values in the Builder
		* @return new Instance of ListingEntity
		*/
		public ListingEntity build() {
			return new ListingEntity(this);
		}
		
	}
	
}