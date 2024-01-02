package com.arenella.recruit.listings.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.controllers.RecruiterListingStatistics.WeekStatBucket;

/**
* Unit tests for the RecruiterListingStatistics class
* @author K Parkings
*/
public class RecruiterListingStatisticsTest {

	/**
	* Tests construction
	* @throws Exception
	*/
	//@Test
	public void testConstruction() throws Exception{
		
		ListingViewedEvent eventOldYear = ListingViewedEvent.builder().created(LocalDateTime.now().minusYears(2)).eventId(UUID.fromString("acde070d-8c4c-4f0d-9d8a-162843c10333")).listingId(UUID.fromString("acde070d-8c4c-4f0d-9d8a-162843c10331")).build(); 
		ListingViewedEvent eventOne = ListingViewedEvent.builder().created(LocalDateTime.now().minusWeeks(1)).eventId(UUID.fromString("acde070d-8c4c-4f0d-9d8a-162843c10444")).listingId(UUID.fromString("acde070d-8c4c-4f0d-9d8a-162843c10441")).build(); 
		ListingViewedEvent eventTwo = ListingViewedEvent.builder().created(LocalDateTime.now().minusWeeks(2)).eventId(UUID.fromString("acde070d-8c4c-4f0d-9d8a-162843c10555")).listingId(UUID.fromString("acde070d-8c4c-4f0d-9d8a-162843c10551")).build(); 
		
		Set<ListingViewedEvent> events = Set.of(eventOldYear, eventOne, eventTwo);
		
		RecruiterListingStatistics stats = new RecruiterListingStatistics(events);
		
		assertEquals(52, stats.getYearByWeekAllListing().size());
		
		Set<WeekStatBucket> buckets = stats.getYearByWeekAllListing().stream().filter(wb -> wb.getCount() > 0).collect(Collectors.toSet());
		
		assertEquals(2, buckets.size());
		
		buckets.stream().forEach(b -> assertEquals(1,b.getCount()));
		
		
	}
	
}
