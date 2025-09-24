package com.arenella.recruit.authentication.adapters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.listings.services.ListingService;
import com.arenella.recruit.recruiters.adapters.ListingsExternalEventListener;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Unit tests for the ListingsExternalEventListener class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class ListingsExternalEventListenerTest {

	@Mock
	private ListingService	mockListingService;
	
	@InjectMocks
	private ListingsExternalEventListener listener = new ListingsExternalEventListenerImpl();
	
	/**
	* Tests handling of RecruiterOpenSubscriptionEvent event 
	*/
	@Test
	void testListenForRecruiterHasOpenSubscriptionsEvent() {
		
		final String recruiterId = "kparkings";
		
		listener.listenForSubscriptionAddedEvent(new SubscriptionAddedEvent(recruiterId, subscription_type.ONE_MONTH_SUBSCRIPTION));
		
		Mockito.verify(this.mockListingService).enableListingsForRecruiter(recruiterId);
		
	}

	/**
	* Tests handling of RecruiterNoOpenSubscriptionEvent event 
	*/
	@Test
	void testListenForRecruiterHasNoOpenSubscriptionEvent() {
		
		final String recruiterId = "kparkings";
		
		listener.listenForRecruiterNoOpenSubscriptionsEvent(new RecruiterNoOpenSubscriptionEvent(recruiterId));
	
		Mockito.verify(this.mockListingService).disableListingsForRecruiter(recruiterId);
		
	}
	
}
