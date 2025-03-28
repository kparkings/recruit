package com.arenella.recruit.listings.services;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingFilter;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.controllers.ListingStatistics;
import com.arenella.recruit.listings.repos.ListingRepository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

/**
* Services relating to Listing statistics
* @author K Parkings
*/
@Service
public class ListingStatisticsServiceImpl implements ListingStatisticsService{

	@Autowired 
	private ListingRepository	listingRepo;
	
	@Autowired
	private ElasticsearchClient	esClient;
	/**
	* Refer to the StatisticsService interface for details 
	*/
	@Override
	public ListingStatistics fetchListingStatistics() {
		
		
		List<ListingViewedEvent> events = this.listingRepo.findAllListings(ListingFilter.builder().build(), this.esClient)
		.stream()
		.map(Listing::getViews).flatMap(Collection::stream).toList();
		
		return new ListingStatistics(events);
		
	}

	/**
	* Refer to the StatisticsService interface for details 
	*/
	@Override
	public Set<ListingViewedEvent> fetchListingStatsForRecruiter(String recruiterId, Principal principal) {
		
		if (!principal.getName().equals(recruiterId)) {
			throw new IllegalArgumentException("You cannot view another Recruiters Statistic's");
		}
		
		return this.listingRepo.findAllListings(ListingFilter.builder().ownerId(recruiterId).build(), this.esClient)
				.stream()
				.map(Listing::getViews).flatMap(Collection::stream).collect(Collectors.toSet());
		
	}

}