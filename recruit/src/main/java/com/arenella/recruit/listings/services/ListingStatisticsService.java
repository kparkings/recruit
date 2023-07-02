package com.arenella.recruit.listings.services;

import java.security.Principal;
import java.util.Set;

import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.controllers.ListingStatistics;

/**
* Defines Statistics functionality relating to Listings 
* @author K Parkings
*/
public interface ListingStatisticsService {

	/**
	* Returns statistics relating to Listings
	* @return
	*/
	public ListingStatistics fetchListingStatistics();
	
	/**
	* Returns ListingViewedEvents for a specific Recruiter
	* @param recruiterId - Unique id of the Recruiter
	* @param principal	 - Currently authorized recruiter
	* @return Events where recruiters Listings have been viewed
	*/
	public Set<ListingViewedEvent> fetchListingStatsForRecruiter(String recruiterId, Principal principal);
	
}
