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

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.beans.ListingViewedEvent;
import com.arenella.recruit.listings.repos.ListingRepository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

/**
* Unit tests for the ListingStatisticsServiceImpl class 
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class ListingStatisticsServiceImplTest {

	@Mock
	private Principal						mockPrincipal;
	
	@Mock 
	private ListingRepository				mockListingRepo;
	
	@Mock
	private ElasticsearchClient				esClient;
	
	@InjectMocks
	private ListingStatisticsServiceImpl 	service = new ListingStatisticsServiceImpl();
	
	/**
	* Test that Recruiter can only obtain their own statistics
	* @throws Exception
	*/
	@Test
	void testFetchListingStatsForRecruiter_otherRecruitersEvents() {
		
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
	void testFetchListingStatsForRecruiter() {
		
		final String recruiter = "recruiter1";
		
		Mockito.when(mockPrincipal.getName()).thenReturn(recruiter);
		Mockito.when(this.mockListingRepo
				.findAllListings(Mockito.any(),Mockito.any()))
			.thenReturn(Set.of(
					Listing.builder().views(Set.of(ListingViewedEvent.builder().build())).build(),
					Listing.builder().views(Set.of(ListingViewedEvent.builder().build())).build()
					));	
		
		Set<ListingViewedEvent> results = this.service.fetchListingStatsForRecruiter(recruiter, mockPrincipal);
		
		assertEquals(2, results.size());
		
	}
	
}
