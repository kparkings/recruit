package com.arenella.recruit.listings.utils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingAlertFilterOptions;
import com.arenella.recruit.listings.services.ListingAlertService;

/**
* Utility for testing an new listing against the Listing alerts already setup in the
* system.
* @author K Parkings
*/
@Service
public class ListingAlertHitTesterUtil {

	@Autowired
	private ListingAlertService listingAlertService;
	
	@Autowired
	private CategoryExtractorUtil categoryExtractor;
	
	private final HashSet<Listing> pendingListings = new HashSet<>();
	
	/**
	* Registers a new Listing to be tested against existing Alerts to 
	* see if the Listing is a match for one or more Alerts
	* @param listing - Listing to be tested against the Alerts
	*/
	public void registerListing(Listing listing) {
		this.pendingListings.add(listing);
	}
	
	/**
	* Task to periodically test pending Listings against the existing
	* Alerts to ascertain if there is a Hit
	*/
	@Scheduled(fixedRate=10000)
	private void pollForHits() {
		System.out.println("RUNNING ALERT TESTER");
		Optional<Listing> listing = this.pendingListings.stream().findFirst();
		
		if (listing.isEmpty()) {
			return;
		}
		
		this.pendingListings.remove(listing.get());
		
		ListingAlertFilterOptions filters = extractListingAlertFilters(listing.get());
		
		this.listingAlertService.fetchListingAlerts(filters).stream().forEach(hit -> {
			//Send email command with link to Listing
			System.out.println("RUNNING LISTING ALERT : Listing " + listing.get().getListingId() + " Alert: " + hit.getId());
		});
		
		System.out.println("RUNNING ALERT TESTER - DONE");
		
	}
	
	/**
	* Constructs the filters based upon the contents of the Listing
	* @param listing - Listing to extract filters from
	* @return Filters
	*/
	private ListingAlertFilterOptions extractListingAlertFilters(Listing listing) {
		//1. extract filters ( country / contractType / categories )
		
		listing.getType();
		
		ListingAlertFilterOptions.ListingAlertFilterOptionsBuilder filters = ListingAlertFilterOptions.builder();
		
		if (Optional.ofNullable(listing.getCountry()).isPresent()) {
			filters.countries(Set.of(listing.getCountry()));
		}
		
		if (Optional.ofNullable(listing.getType()).isPresent()) {
			filters.contractType(listing.getType());
		}
		
		this.categoryExtractor.extractFilters(listing.getTitle(), filters);
		
		//TODO: [KP] Need to add logic to determine category from title
		
		return filters.build();
	
	}
	
}