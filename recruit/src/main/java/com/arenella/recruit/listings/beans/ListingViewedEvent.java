package com.arenella.recruit.listings.beans;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
* Event representing the viewing of a Listing
* by a User.
* @author K Parkings
*/
public class ListingViewedEvent {

	private UUID 			eventId;
	private UUID 			listingId;
	private LocalDateTime 	created;
	private String			title;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains the initialization values
	*/
	public ListingViewedEvent(ListingViewedEventBuilder builder) {
		
		this.eventId 		= builder.eventId;
		this.listingId 		= builder.listingId;
		this.created 		= builder.created;
		
	}
	
	/**
	* Returns the unique identifier of the Event
	* @return Unique id for the Event
	*/
	public UUID getEventId() {
		return this.eventId;
	}
	
	/**
	* Returns the unique identifier of the Listing that 
	* was viewed
	* @return
	*/
	public UUID getListingId() {
		return this.listingId;
	}
	
	/**
	* Returns when the Listing was viewed
	* @return when the Listing was viewed
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}
	
	/**
	* Will return the title of the Listing where available or the
	* listing id where it is not available
	* @return title for the Listing
	*/
	public String getTitleOrLisingId(){
		return Optional.ofNullable(this.title).isPresent() ? this.title : this.listingId.toString();
	}
	
	/**
	* Sets the human readable title for the Listing
	* @param title - Human readable title
	*/
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	* Returns a Builder for the ListingViewedEvent class
	* @return Builder for the Event
	*/
	public static ListingViewedEventBuilder builder() {
		return new ListingViewedEventBuilder();
	}
	
	/**
	* Builder for the ListingViewedEvent class
	* @author K Parkings
	*/
	public static class ListingViewedEventBuilder {
	
		private UUID 			eventId			= UUID.randomUUID();
		private UUID 			listingId;
		private LocalDateTime 	created			= LocalDateTime.now();
	
		/**
		* Sets the unique Id of the Event 
		* @param eventId - Unique Id of the Event
		* @return Builder
		*/
		public ListingViewedEventBuilder eventId(UUID eventId) {
			this.eventId = eventId;
			return this;
		}
		
		/**
		* Sets the unique id of the Listing that was viewed
		* @param listingId - id of viewed Listing
		* @return Builder
		*/
		public ListingViewedEventBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets when the Event was created 
		* @param created - When event was created
		* @return Builder
		*/
		public ListingViewedEventBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Returns an instance of ListingViewedEvent instantiated with the 
		* values in the builder
		* @return instance of ListingViewedEvent
		*/
		public ListingViewedEvent build() {
			return new ListingViewedEvent(this);
		}
		
	}
		
}