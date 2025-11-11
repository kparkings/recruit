package com.arenella.recruit.listings.services;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.listings.beans.ListingAlert;
import com.arenella.recruit.listings.beans.ListingAlertFilterOptions;

/**
* Defines services relating to interactions with ListingAlerts
* @author K Parkings
*/
public interface ListingAlertService {

	/**
	* Adds a new LisitingAlert
	* @param alert 			- ListingAlert to be persisted
	* @param currentUser 	- Current logged in User 
	*/
	public void addListingAlert(ListingAlert alert, Principal currentUser);
	
	/**
	* Returns all ListingAlerts matching the filters
	* @param filters - Config to Filter on
	* @return Matching ListingAlerts
	*/
	public Set<ListingAlert> fetchListingAlerts(ListingAlertFilterOptions filters);
	
	/**
	* Deletes the ListingAlert matching the Id
	* @param id - Unique id of the ListingAlert
	*/
	public void deleteListingAlert(UUID id);

	/**
	* Registers an event stating that an email alert was triggered
	* and the user receiver an alert informing them that a new 
	* listing matching their requirements has been added to the 
	* job board
	* @param listingId
	*/
	public void registerListingAlertEmailSent(UUID listingId);
	
}
