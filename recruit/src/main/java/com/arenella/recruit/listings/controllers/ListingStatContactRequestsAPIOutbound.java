package com.arenella.recruit.listings.controllers;

import java.util.UUID;

import com.arenella.recruit.listings.beans.ListingStatContactRequests;

/**
* API Outbound view containing information about the contact requests sent
* to a recruiter regarding a specific Listing
*/
public record ListingStatContactRequestsAPIOutbound(UUID listingId, int registeredUserRequests, int unregisteredUserRequests) {

	/**
	* Converts from Domain to API Outbound representation
	* @param stat - Stat to convert
	* @return converted stat
	*/
	public static ListingStatContactRequestsAPIOutbound fromDomain(ListingStatContactRequests stat) {
		return new ListingStatContactRequestsAPIOutbound(stat.getListingId(), stat.getRegisteredUserRequests(), stat.getUnregisteredUserRequests());
	}
	
}
