package com.arenella.recruit.listings.dao;

import java.time.LocalDate;
import java.util.UUID;

import com.arenella.recruit.listings.beans.ListingAlertSentEvent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Represents and email alert having been sent to a user 
* 
*/
@Entity
@Table(schema="listings", name="listing_alert_sent_event")
public class ListingAlertSentEventEntity {

	@Id
	@Column(name="id")
	private UUID id;
	
	@Column(name="listing_id")
	private UUID listingId;
	
	@Column(name="alert_sent")
	private LocalDate alertSent;
	
	/**
	* Default constructor
	*/
	public ListingAlertSentEventEntity() {
		//Hibernate
	}
	
	/**
	* Constructor
	* @param id				- Unique Id of the event
	* @param listingId		- Listing event was for
	* @param alertSent		- When alert was sent to user
	*/
	ListingAlertSentEventEntity(UUID id, UUID listingId, LocalDate alertSent) {
		this.id 		= id;
		this.listingId 	= listingId;
		this.alertSent 	= alertSent;
	}
	
	/**
	* Returns the uniqueId of the event
	* @return id of the event
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns the unique Id of the Listing
	* @return
	*/
	public UUID getListingId() {
		return this.listingId;
	}
	
	/**
	* Returns when the alert was sent to the 
	* candidates
	* @return
	*/
	public LocalDate getAlertSent() {
		return this.alertSent;
	}
	
	/**
	* Converts for Entity to Domain representation
	* @param entity - To be converted
	* @return converted
	*/
	public static ListingAlertSentEvent fromEntity(ListingAlertSentEventEntity entity) {
		return new ListingAlertSentEvent(entity.id, entity.listingId, entity.alertSent);
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param event - To be converted
	* @return converted
	*/
	public static ListingAlertSentEventEntity toEntity(ListingAlertSentEvent event) {
		return new ListingAlertSentEventEntity(event.id(), event.listingId(), event.alertSent());
	}
	
}