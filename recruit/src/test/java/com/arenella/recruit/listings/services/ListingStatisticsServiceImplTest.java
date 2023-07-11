package com.arenella.recruit.listings.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.Principal;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.dao.ListingDao;
import com.arenella.recruit.listings.dao.ListingStatisticsDao;

/**
* Unit tests for the ListingStatisticsServiceImpl class 
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class ListingStatisticsServiceImplTest {

	@Mock
	private Principal				mockPrincipal;
	
	@Mock
	private ListingStatisticsDao 	mockListingStatsDao;
	
	@Mock
	private ListingDao 				listingDao;
	
	@InjectMocks
	private ListingStatisticsServiceImpl service = new ListingStatisticsServiceImpl();
	
	/**
	* Test that Recruiter can only ontain their own statistics
	* @throws Exception
	*/
	@Test
	public void testFetchListingStatsForRecruiter_otherRecruitersEvents() throws Exception {
		
		Mockito.when(mockPrincipal.getName()).thenReturn("recruiter1");
		
		assertThrows(IllegalArgumentException.class, () ->{
			this.service.fetchListingStatsForRecruiter("recruiter2", mockPrincipal);
		});
		
	}
	
	/**
	* Test that Recruiter can only obtain their own statistics
	* @throws Exception
	*/
	@Test
	public void testFetchListingStatsForRecruiter() throws Exception {
		
		final String recruiter = "recruiter1";
		
		Mockito.when(mockPrincipal.getName()).thenReturn(recruiter);
		Mockito.when(this.mockListingStatsDao.fetchEventsForRecruiter(recruiter)).thenReturn(Set.of(ListingViewedEvent.builder().build(), ListingViewedEvent.builder().build()));
		
		Set<ListingViewedEvent> results = this.service.fetchListingStatsForRecruiter(recruiter, mockPrincipal);
		
		assertEquals(2, results.size());
		
	}
	
}
