package com.arenella.recruit.listings.dao;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.arenella.recruit.listings.beans.ListingViewedEvent;

/**
* Entity representation of a ListingViewedEvent
* @author K Parkings
*/
@Entity
@Table(schema="listings", name="listing_viewed_event")
public class ListingViewedEventEntity {

	@Id
	@Column(name="event_id")
	private UUID 			eventId;
	
	@Column(name="listing_id")
	private UUID 			listingId;
	
	@Column(name="created")
	private LocalDateTime 	created;
	
	/**
	* Required for Hibernate 
	*/
	public ListingViewedEventEntity() {
		
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains the initialization values
	*/
	public ListingViewedEventEntity(ListingViewedEventEntityBuilder builder) {
		
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
	* Returns a Builder for the ListingViewedEvent class
	* @return Builder for the Event
	*/
	public static ListingViewedEventEntityBuilder builder() {
		return new ListingViewedEventEntityBuilder();
	}
	
	/**
	* Builder for the ListingViewedEvent class
	* @author K Parkings
	*/
	public static class ListingViewedEventEntityBuilder {
	
		private UUID 			eventId;
		private UUID 			listingId;
		private LocalDateTime 	created;
	
		/**
		* Sets the unique Id of the Event 
		* @param eventId - Unique Id of the Event
		* @return Builder
		*/
		public ListingViewedEventEntityBuilder eventId(UUID eventId) {
			this.eventId = eventId;
			return this;
		}
		
		/**
		* Sets the unique id of the Listing that was viewed
		* @param listingId - id of viewed Listing
		* @return Builder
		*/
		public ListingViewedEventEntityBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets when the Event was created 
		* @param created - When event was created
		* @return Builder
		*/
		public ListingViewedEventEntityBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Returns an instance of ListingViewedEvent instantiated with the 
		* values in the builder
		* @return instance of ListingViewedEvent
		*/
		public ListingViewedEventEntity build() {
			return new ListingViewedEventEntity(this);
		}
		
	}
	
	/**
	* Converts from Domain representation of the Event to the Entity representation
	* @param event - To be converted
	* @return Entity representation
	*/
	public static ListingViewedEventEntity convertToEntity(ListingViewedEvent event) {
		return ListingViewedEventEntity
								.builder()
									.eventId(event.getEventId())
									.listingId(event.getListingId())
									.created(event.getCreated())
								.build();
	}
	
	/**
	* Converts from Entity representation of the Event to the Domain representation
	* @param entity - To be converted
	* @return Domain representation
	*/
	public static ListingViewedEvent convertFromEntity(ListingViewedEventEntity entity) {
		return ListingViewedEvent
				.builder()
					.eventId(entity.getEventId())
					.listingId(entity.getListingId())
					.created(entity.getCreated())
				.build();
	}
	
}