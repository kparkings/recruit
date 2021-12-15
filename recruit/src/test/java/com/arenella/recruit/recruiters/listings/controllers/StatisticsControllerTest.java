package com.arenella.recruit.recruiters.listings.controllers;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.listings.controllers.ListingStatistics;
import com.arenella.recruit.listings.controllers.ListingStatisticsController;
import com.arenella.recruit.listings.services.ListingStatisticsService;

/**
* Unit tests for the StatisticsController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class StatisticsControllerTest {

	@InjectMocks
	private ListingStatisticsController controller = new ListingStatisticsController();
	
	@Mock
	private ListingStatisticsService statisticsService;
	
	/**
	* Tests returns a ListingStatistics object
	* @throws Exception
	*/
	@Test
	public void testFetchDownloadStats() throws Exception {
		
		ListingStatistics stats = new ListingStatistics(List.of());
		
		Mockito.when(this.statisticsService.fetchListingStatistics()).thenReturn(stats);
		
		ResponseEntity<ListingStatistics> result = controller.fetchDownloadStats();
		
		assertEquals(result.getBody(), stats);
		assertEquals(result.getStatusCode(), HttpStatus.OK);
		
	}
	
}