package com.arenella.recruit.listings.repos.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.arenella.recruit.listings.beans.ListingViewedEvent;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
* Representation of an event where a User has viewed the 
* Listing. 
*/
public class ListingViewedEventDocument {

	@Field(type = FieldType.Keyword)
	private UUID 			eventId;
	
	@Field(type = FieldType.Date)
	@Enumerated(EnumType.STRING)
	private LocalDateTime 	created;
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization information
	*/
	public ListingViewedEventDocument(ListingViewedEventDocumentBuilder builder) {
		this.eventId = builder.eventId;
		this.created = builder.created;
	}
	
	/**
	* Returns the unique id of the event
	* @return unique Id
	*/
	public UUID getEventId() {
		return this.eventId;
	}
	
	/**
	* Returns when the Event occurred
	* @return When the Event was created
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}
	
	/**
	* Returns a builder for the class 
	* @return BUuilder
	*/
	public static ListingViewedEventDocumentBuilder builder() {
		return new ListingViewedEventDocumentBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class ListingViewedEventDocumentBuilder {
	
		private UUID 			eventId;
		private LocalDateTime 	created;
		
		/**
		* Sets the unique id of the event
		* @param eventId - Unique id of the event
		* @return Builder
		*/
		public ListingViewedEventDocumentBuilder eventId(UUID eventId) {
			this.eventId = eventId;
			return this;
		}
		
		/**
		* Sets when the event was created
		* @param created - create date time
		* @return Builder
		*/
		public ListingViewedEventDocumentBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Returns a new initialized instance
		* @return initialized instance
		*/
		public ListingViewedEventDocument build() {
			return new ListingViewedEventDocument(this);
		}
		
	}
	
	/**
	* Converts from Entity representation to domain representation of an event
	* @param document - To be converted
	* @return Domain representation
	*/
	public static ListingViewedEvent fromEntity(ListingViewedEventDocument document) {
		return ListingViewedEvent
				.builder()
					.eventId(document.getEventId())
					.created(document.getCreated())
				.build();
	}
	
	/**
	* Converts from Domain representation to Entity representation of an event
	* @param event - To be converted
	* @return Entity representation
	*/
	public static ListingViewedEventDocument toDocument(ListingViewedEvent event) {
		return ListingViewedEventDocument
				.builder()
					.eventId(event.getEventId())
					.created(event.getCreated())
				.build();
	}
	
}