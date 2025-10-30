package com.arenella.recruit.listings.beans;

import java.time.LocalDateTime;
import java.util.UUID;

/**
* Class represents an Event that occurred when 
* a User sent a contact request to a Recruiter 
* relating to a Listing on the job board. 
*/
public class ListingContactRequestEvent {

	public enum CONTACT_USER_TYPE {REGISTERED, UNREGISTERED}
	
	private UUID 				eventId;
	private UUID 				listingId;
	private CONTACT_USER_TYPE 	userType; 
	private LocalDateTime 		created;
	
	/**
	* Default constructor
	*/
	private ListingContactRequestEvent() {
		//Hide default constructor
	}
	
	/**
	* Constructor based upon builder
	*/
	public ListingContactRequestEvent(ListingContactRequestEventBuilder builder) {
		this.eventId	= builder.eventId;
		this.listingId	= builder.listingId;
		this.userType	= builder.userType; 
		this.created	= builder.created;
	}
	
	/**
	* Returns the unique id of the event
	* @return unique id
	*/
	public UUID getEventId() {
		return this.eventId;
	}

	/**
	* Return the id of the listing the event related to
	* @return Id of the Listing the contract request was for
	*/
	public UUID getListingId() {
		return this.listingId;
	}
	
	/**
	* Returns the type of User that made the 
	* contact request
	* @return User type
	*/
	public CONTACT_USER_TYPE getUserType() {
		return this.userType;
	}
	
	/**
	* Returns when the event was created
	* @return When contact request occurred
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}
	
	/**
	* Returns a Builder for the class
	* @return Builder for the class
	*/
	public static ListingContactRequestEventBuilder builder() {
		return new ListingContactRequestEventBuilder();
	}
	
	/**
	* Builder for the class
	*/
	public static class ListingContactRequestEventBuilder {
		
		private UUID 				eventId;
		private UUID 				listingId;
		private CONTACT_USER_TYPE 	userType; 
		private LocalDateTime 		created;
		
		/**
		* Sets the unique id of the event
		* @param eventId - unique Id
		* @return Builder
		*/
		public ListingContactRequestEventBuilder eventId(UUID eventId) {
			this.eventId = eventId;
			return this;
		}
		
		/**
		* Sets the unique id of the Listing the contact request
		* relates to
		* @param listingId - Unique id of the Listing
		* @return Builder
		*/
		public ListingContactRequestEventBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets the type of User making the contact request to the Recruiter
		* @param userType - Type of User
		* @return Builder
		*/
		public ListingContactRequestEventBuilder userType(CONTACT_USER_TYPE userType) {
			this.userType = userType;
			return this;
		} 
		
		/**
		* Sets when the event was created
		* @param created - When the even occurred
		* @return Builder
		*/
		public ListingContactRequestEventBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Returns initialized instance of the class
		* @return initialized instance
		*/
		public ListingContactRequestEvent build() {
			return new ListingContactRequestEvent(this);
		}
		
	}
	
}