package com.arenella.recruit.listings.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.listings.services.ListingStatisticsService;

/**
* REST API for obtaining Statistics relating to Listings
* @author K Parkings
*/
@RestController
public class ListingStatisticsController {

	@Autowired
	private ListingStatisticsService statisticsService;
	
	/**
	* Returns a summary of Listing statistics
	* @return stats of daily downloads
	*/
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value="/listings/stats/")
	public ResponseEntity<ListingStatistics> fetchDownloadStats(){
		return ResponseEntity.ok(statisticsService.fetchListingStatistics());
	}
	
	
}
