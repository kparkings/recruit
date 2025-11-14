package com.arenella.recruit.listings.beans;

import java.time.LocalDateTime;

/**
* Defines a basic ListingEvent 
*/
public interface ListingEvent {

	/**
	* Returns when the event was created
	* @return
	*/
	public LocalDateTime getCreated();
	
}
