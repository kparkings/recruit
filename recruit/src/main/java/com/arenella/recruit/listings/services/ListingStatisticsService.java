package com.arenella.recruit.listings.services;

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
	
}
