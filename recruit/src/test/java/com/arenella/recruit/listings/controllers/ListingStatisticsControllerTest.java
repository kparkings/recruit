package com.arenella.recruit.listings.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.services.ListingStatisticsService;

/**
* Unit tests for the ListingStatisticsController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class ListingStatisticsControllerTest {

	@Mock
	private Principal						mockPrincipal;
	
	@Mock
	private ListingStatisticsService 		mockStatisticsService;
	
	@InjectMocks
	private ListingStatisticsController 	controller = new ListingStatisticsController();
	
	/**
	* Tests endpoint for obtaining stat's for a Recruiter relating to how often their Listings were viewed
	* @throws Exception
	*/
	//@Test
	void testFetchListingStatsForRecruiter() {
		
		ListingViewedEvent eventOldYear = ListingViewedEvent.builder().created(LocalDateTime.now().minusYears(2)).eventId(UUID.fromString("acde070d-8c4c-4f0d-9d8a-162843c10333")).listingId(UUID.fromString("acde070d-8c4c-4f0d-9d8a-162843c10331")).build(); 
		ListingViewedEvent eventOne 	= ListingViewedEvent.builder().created(LocalDateTime.now().minusWeeks(1)).eventId(UUID.fromString("acde070d-8c4c-4f0d-9d8a-162843c10444")).listingId(UUID.fromString("acde070d-8c4c-4f0d-9d8a-162843c10441")).build(); 
		ListingViewedEvent eventTwo 	= ListingViewedEvent.builder().created(LocalDateTime.now().minusWeeks(2)).eventId(UUID.fromString("acde070d-8c4c-4f0d-9d8a-162843c10555")).listingId(UUID.fromString("acde070d-8c4c-4f0d-9d8a-162843c10551")).build(); 
		
		Set<ListingViewedEvent> events = Set.of(eventOldYear, eventOne, eventTwo);
		
		Mockito.when(this.mockStatisticsService.fetchListingStatsForRecruiter("recruiter1", mockPrincipal)).thenReturn(events);
		
		ResponseEntity<RecruiterListingStatistics> response = controller.fetchListingStatsForRecruiter("recruiter1", mockPrincipal);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody() instanceof RecruiterListingStatistics);
		
		
	}
	
}
