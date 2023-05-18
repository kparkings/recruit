package com.arenella.recruit.authentication.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.RecruiterHasOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.listings.services.ListingService;
import com.arenella.recruit.recruiters.adapters.ListingsExternalEventListener;

/**
* Event listener for Listings
* @author K Parkings
*/
@Service
public class ListingsExternalEventListenerImpl implements ListingsExternalEventListener{

	@Autowired
	private ListingService listingService;
	
	/**
	* Refer to the ListingsExternalEventListener interface for details
	*/
	@Override
	public void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent recruiterNoOpenSubscriptionEvent) {
		this.listingService.disableListingsForRecruiter(recruiterNoOpenSubscriptionEvent.geRecruiterId());
	}

	/**
	* Refer to the ListingsExternalEventListener interface for details
	*/
	@Override
	public void listenForRecruiterHasOpenSubscriptionsEvent(RecruiterHasOpenSubscriptionEvent recruiterHasOpenSubscriptionEvent) {
		this.listingService.enableListingsForRecruiter(recruiterHasOpenSubscriptionEvent.geRecruiterId());
		
	}

}
