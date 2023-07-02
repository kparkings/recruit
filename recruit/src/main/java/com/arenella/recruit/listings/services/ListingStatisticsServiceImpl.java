package com.arenella.recruit.listings.services;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingFilter;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.controllers.ListingStatistics;
import com.arenella.recruit.listings.dao.ListingDao;
import com.arenella.recruit.listings.dao.ListingStatisticsDao;
import com.arenella.recruit.listings.dao.ListingViewedEventEntity;

/**
* Services relating to Listing statistics
* @author K Parkings
*/
@Service
public class ListingStatisticsServiceImpl implements ListingStatisticsService{

	@Autowired
	private ListingStatisticsDao listingStatsDao;
	
	@Autowired
	private ListingDao 			listingDao;
	
	/**
	* Refer to the StatisticsService interface for details 
	*/
	@Override
	public ListingStatistics fetchListingStatistics() {
		
		List<ListingViewedEvent> events = StreamSupport.stream(listingStatsDao.findAll().spliterator(), false).map(ListingViewedEventEntity::convertFromEntity).collect(Collectors.toList());
		
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
		
		Set<Listing> 				listings 	= this.listingDao.findAllListings(ListingFilter.builder().ownerId(recruiterId).build());
		Set<ListingViewedEvent> 	events 		= listingStatsDao.fetchEventsForRecruiter(recruiterId);
		
		events.stream().forEach(e -> {
			Optional<Listing> listing = listings.stream().filter(l -> l.getListingId().toString().equals(e.getListingId().toString())).findFirst();
			if (listing.isPresent()) {
				e.setTitle(listing.get().getTitle());
			}
		});
		
		return events;
	}

}