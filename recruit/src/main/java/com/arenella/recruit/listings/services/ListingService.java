package com.arenella.recruit.listings.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingFilter;

/**
* Defines services relating to Listings
* @author K Parkings
*/
public interface ListingService {

	/**
	* Adds a new Listing
	* @param listing - Details of new Listing
	* @return Unique Id for new Listing
	*/
	public UUID addListing(Listing listing);
	
	/**
	* Updates an Existing Listing
	* @param listingId 	- Unique Id of Listing to update
	* @param listing 	- Updated details of the Listing
	*/
	public void updateListing(UUID listingId, Listing listing);

	/**
	* Deletes an existing Listing
	* @param listingId
	*/
	public void deleteListing(UUID listingId);
	
	/**
	* Retrieves a Page of listings matching the filters
	* @return Page of Matching Listings
	*/
	public Page<Listing> fetchListings(ListingFilter filters, Pageable pageable);

}