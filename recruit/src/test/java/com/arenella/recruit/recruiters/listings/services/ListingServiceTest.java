package com.arenella.recruit.recruiters.listings.services;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.dao.ListingDao;
import com.arenella.recruit.listings.services.ListingServiceImpl;

/**
* Unit tests for the ListingService class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class ListingServiceTest {

	@InjectMocks
	private ListingServiceImpl 	service = new ListingServiceImpl();
	
	@Mock
	private ListingDao 			mockListingDao;
	
	/**
	* Tests happy path for Listing creation
	* @throws Exception
	*/
	@Test
	public void testAddListing() throws Exception {
		
		final Listing 			listing 		= null;
		
		UUID listingId = service.addListing(listing);
		
		if (!(listingId instanceof UUID)) {
			throw new RuntimeException();
		}
		
	}
	
}