package com.arenella.recruit.listings.beans;

import java.util.UUID;

/**
* Statistic for a specific Listing related
* to the number of contact requests that
* have been sent to the recruiter
*/
public class ListingStatContactRequests {
	
	private UUID 	listingId; 
	private int 	registeredUserRequests; 
	private int 	unregisteredUserRequests;

	/**
	* Default constructor
	*/
	private ListingStatContactRequests() {
		//Only via builder
	}
	
	/**
	* Constructor based upon a builder
	* @param builder - contains initialization values
	*/
	private ListingStatContactRequests(ListingStatContactRequestsBuilder builder) {
		this.listingId 					= builder.listingId; 
		this.registeredUserRequests 	= builder.registeredUserRequests; 
		this.unregisteredUserRequests 	= builder.unregisteredUserRequests;
	}
	
	/**
	* Returns the Id of the Listing the stat's relate to
	* @return Unique Id of the Listing
	*/
	public UUID getListingId() {
		return this.listingId;
	}
	
	/**
	* Returns number of contact requests from registers/logged in candidates
	* @return number of contact requests
	*/
	public int 	getRegisteredUserRequests() {
		return this.registeredUserRequests;
	} 
	
	/**
	* Returns number of contact requests from unregisters/ not logged in candidates
	* @return number of contact requests
	*/
	public int 	getUnregisteredUserRequests() {
		return this.unregisteredUserRequests;
	}

	/**
	* Increase the number of contact requests from a 
	* registered user
	*/
	public void increaseRegisteredCount() {
		this.registeredUserRequests ++;
	}
	
	/**
	* Increase the number of contact requests from a 
	* registered user 
	*/
	public void increaseUnregisteredCount() {
		this.unregisteredUserRequests ++;
	}
	
	/**
	* Returns a builder for the class
	* @return
	*/
	public static ListingStatContactRequestsBuilder builder() {
		return new ListingStatContactRequestsBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class ListingStatContactRequestsBuilder {

		private UUID 	listingId; 
		private int 	registeredUserRequests; 
		private int 	unregisteredUserRequests;
		
		/**
		* Sets the Id of the Listing the stats relate to
		* @param listingId - Unique Id of the Listing
		* @return Builder
		*/
		public ListingStatContactRequestsBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets the number of contact requests sent by registered users 
		* to the recruiter for the Listing
		* @param registeredUserRequests
		* @return Builder
		*/
		public ListingStatContactRequestsBuilder registeredUserRequests(int registeredUserRequests) {
			this.registeredUserRequests = registeredUserRequests;
			return this;
		}
		
		/**
		* Sets the number of contact requests sent by unregistered users 
		* to the recruiter for the Listing
		* @param unregisteredUserRequests
		* @return Builder
		*/
		public ListingStatContactRequestsBuilder unregisteredUserRequests(int unregisteredUserRequests) {
			this.unregisteredUserRequests = unregisteredUserRequests;
			return this;
		}
		
		/**
		* Returns an initialized instance of the class
		* @return initalized instance of the class
		*/
		public ListingStatContactRequests build() {
			return new ListingStatContactRequests(this);
		} 
		
	}
	
}