package com.arenella.recruit.listings.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingFilter;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.controllers.CandidateListingContactRequest;
import com.arenella.recruit.listings.controllers.ListingContactRequest;

/**
* Defines services relating to Listings
* @author K Parkings
*/
public interface ListingService {

	/**
	* Adds a new Listing
	* @param listing 			- Details of new Listing
	* @param postToSocialMedia 	- Whether or not to post the Listing on Social Media
	* @return Unique Id for new Listing
	*/
	public UUID addListing(Listing listing, boolean postToSocialMedia);
	
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

	/**
	* Registers that a Listing was Viewed
	* @param event - Contains details of the viewing of the Listing
	*/
	public void registerListingViewedEvent(ListingViewedEvent event);

	/**
	* Sends a Contact request for a Listing to the owner of the Listing
	* @param contactRequest - Details of contactRequest to be Sent
	*/
	public void sendContactRequestToListingOwner(ListingContactRequest contactRequest);

	/**
	* Sends a Contact request for a Listing from an existing Candidate to the owner of the Listing
	* @param contactRequest - Details of contactRequest to be Sent
	*/
	public void sendContactRequestFomCandidateToListingOwner(CandidateListingContactRequest contactRequest);
}