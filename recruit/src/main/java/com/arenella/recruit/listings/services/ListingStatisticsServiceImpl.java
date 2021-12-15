package com.arenella.recruit.listings.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.controllers.ListingStatistics;
import com.arenella.recruit.listings.dao.ListingStatisticsDao;
import com.arenella.recruit.listings.dao.ListingViewedEventEntity;

/**
* 
* @author K Parkings
*/
@Service
public class ListingStatisticsServiceImpl implements ListingStatisticsService{

	@Autowired
	private ListingStatisticsDao listingStatsDao;
	
	/**
	* Refer to the StatisticsService interface for details 
	*/
	@Override
	public ListingStatistics fetchListingStatistics() {
		
		List<ListingViewedEvent> events = StreamSupport.stream(listingStatsDao.findAll().spliterator(), false).map(event -> ListingViewedEventEntity.convertFromEntity(event)).collect(Collectors.toList());
		
		return new ListingStatistics(events);
		
	}

}
