package com.arenella.recruit.recruiters.listings.services;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.listings.beans.Listing;
import com.arenella.recruit.listings.dao.ListingDao;
import com.arenella.recruit.listings.dao.ListingEntity;
import com.arenella.recruit.listings.services.ListingServiceImpl;

/**
* Unit tests for the ListingService class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class ListingServiceImplTest {

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
		
		Mockito.verify(mockListingDao).save(Mockito.any(ListingEntity.class));
		
	}
	
	/**
	* Asserts Exception is thrown if the Listing being updated does not exist
	* @throws Exception
	*/
	@Test
	public void testUpdateListing_unknownListing() throws Exception{
		
		final UUID listingId = UUID.randomUUID();
		
		Mockito.when(this.mockListingDao.findById(listingId)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateListing(listingId, null);
		});
		
	}

	/**
	* Asserts Exception is thrown if the Listing being deleted does not exist
	* @throws Exception
	*/
	@Test
	public void testDeleteListing_unknownListing() throws Exception{
		
		final UUID listingId = UUID.randomUUID();
		
		Mockito.when(this.mockListingDao.findById(listingId)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteListing(listingId);
		});
		
	}

}