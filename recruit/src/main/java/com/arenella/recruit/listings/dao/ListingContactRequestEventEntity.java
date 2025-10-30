package com.arenella.recruit.listings.dao;

import java.time.LocalDateTime;
import java.util.UUID;

import com.arenella.recruit.listings.beans.ListingContactRequestEvent;
import com.arenella.recruit.listings.beans.ListingContactRequestEvent.CONTACT_USER_TYPE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* DAO Representation of an event of a User contacting a recruiter
* about a Listing
*/
@Entity
@Table(schema="listings", name="listing_contact_request_event")
public class ListingContactRequestEventEntity {

	@Id
	@Column(name="event_id")
	private UUID eventId;
	
	@Column(name="listing_id")
	private UUID listingId;
	
	@Column(name="user_type") 
	@Enumerated(EnumType.STRING)
	private CONTACT_USER_TYPE userType; 
	
	@Column(name="created")
	private LocalDateTime created;

	/**
	* Default constructor 
	*/
	public ListingContactRequestEventEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization details
	*/
	public ListingContactRequestEventEntity(ListingContactRequestEventEntityBuilder builder) {
		this.eventId 	= builder.eventId;
		this.listingId 	= builder.listingId;
		this.userType 	= builder.userType; 
		this.created 	= builder.created;
	}
	
	/**
	* Return unique identifier of the Event
	* @return unique Id
	*/
	public UUID getEventId() {
		return this.eventId;
	} 
	
	/**
	* Returns the Id of the listing the event is associated with
	* @return unique Id of the Listing
	*/
	public UUID getListingId() {
		return this.listingId;
	}
	
	/**
	* Returns the type of User that sent the contact request
	* to the recruiter relating to the LIsting
	* @return user type
	*/
	public CONTACT_USER_TYPE getUserType() {
		return this.userType;
	} 
	
	/**
	* Returns when the event was created
	* @return When the event was created
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}

	/**
	* Returns a Builder for the class
	* @return Builder for the class
	*/
	public static ListingContactRequestEventEntityBuilder builder() {
		return new ListingContactRequestEventEntityBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class ListingContactRequestEventEntityBuilder {
	
		private UUID 				eventId;
		private UUID 				listingId;
		private CONTACT_USER_TYPE 	userType; 
		private LocalDateTime 		created;
		
		/**
		* Sets the unique Id of the event
		* @param eventId - Id of the event
		* @return Builder
		*/
		public ListingContactRequestEventEntityBuilder eventId(UUID eventId) {
			this.eventId = eventId;
			return this;
		}
		
		/**
		* Sets the Unique Id of the Listing the contact request was sent for
		* @param listingId - Unique Id of the Listing
		* @return Builder
		*/
		public ListingContactRequestEventEntityBuilder listingId(UUID listingId) {
			this.listingId = listingId;
			return this;
		}
		
		/**
		* Sets the type of User that sent the contact request
		* @param userType	- User type
		* @return Builder
		*/
		public ListingContactRequestEventEntityBuilder userType(CONTACT_USER_TYPE userType) {
			this.userType = userType;
			return this;
		}
		
		/**
		* Sets when the event was created
		* @param created - When event occurred
		* @return Builder
		*/
		public ListingContactRequestEventEntityBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Returns Initialized instance of class
		* @return Initialized instance
		*/
		public ListingContactRequestEventEntity build() {
			return new ListingContactRequestEventEntity(this);
		} 
		
	}
	
	/**
	* Converts from the domain to entity representation of the Event
	* @param event - To be converted
	* @return Entity representation
	*/
	public static ListingContactRequestEventEntity toEntity( ListingContactRequestEvent event) {
		return ListingContactRequestEventEntity
				.builder()
					.eventId(event.getEventId())
					.listingId(event.getListingId())
					.userType(event.getUserType())
					.created(event.getCreated())
				.build();
	}
	
	/**
	* Converts from the entity to the domain representation of the Event
	* @param entity - To be converted
	* @return Domain representation
	*/
	public static ListingContactRequestEvent fromEntity( ListingContactRequestEventEntity entity) {
		return ListingContactRequestEvent
				.builder()
					.eventId(entity.getEventId())
					.listingId(entity.getListingId())
					.userType(entity.getUserType())
					.created(entity.getCreated())
				.build();
	}
	
}