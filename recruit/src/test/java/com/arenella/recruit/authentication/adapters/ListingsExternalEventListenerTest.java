package com.arenella.recruit.authentication.adapters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.events.RecruiterHasOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.listings.services.ListingService;
import com.arenella.recruit.recruiters.adapters.ListingsExternalEventListener;

/**
* Unit tests for the ListingsExternalEventListener class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class ListingsExternalEventListenerTest {

	@Mock
	private ListingService	mockListingService;
	
	@InjectMocks
	private ListingsExternalEventListener listener = new ListingsExternalEventListenerImpl();
	
	/**
	* Tests handling of RecruiterOpenSubscriptionEvent event 
	*/
	@Test
	public void testListenForRecruiterHasOpenSubscriptionsEvent() {
		
		final String recruiterId = "kparkings";
		
		listener.listenForRecruiterHasOpenSubscriptionsEvent(new RecruiterHasOpenSubscriptionEvent(recruiterId));
		
		Mockito.verify(this.mockListingService).enableListingsForRecruiter(recruiterId);
		
	}

	/**
	* Tests handling of RecruiterNoOpenSubscriptionEvent event 
	*/
	@Test
	public void testListenForRecruiterHasNoOpenSubscriptionEvent() {
		
		final String recruiterId = "kparkings";
		
		listener.listenForRecruiterNoOpenSubscriptionsEvent(new RecruiterNoOpenSubscriptionEvent(recruiterId));
	
		Mockito.verify(this.mockListingService).disableListingsForRecruiter(recruiterId);
		
	}
	
}
