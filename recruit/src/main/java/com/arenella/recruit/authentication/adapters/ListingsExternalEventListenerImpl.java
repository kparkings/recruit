package com.arenella.recruit.authentication.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.listings.beans.RecruiterCredit;
import com.arenella.recruit.listings.services.ListingService;
import com.arenella.recruit.recruiters.adapters.ListingsExternalEventListener;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

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
	public void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent event) {
		this.listingService.disableListingsForRecruiter(event.geRecruiterId());
		this.listingService.updateCreditsForUser(event.geRecruiterId(), RecruiterCredit.DISABLED_CREDITS);
	}

	/**
	* Refer to the ListingsExternalEventListener interface for details
	*/
	@Override
	public void listenForSubscriptionAddedEvent(SubscriptionAddedEvent event) {
		
		if (event.getSubscriptionType() != subscription_type.CREDIT_BASED_SUBSCRIPTION) {
			this.listingService.enableListingsForRecruiter(event.getRecruiterId());
			this.listingService.updateCreditsForUser(event.getRecruiterId(), RecruiterCredit.DISABLED_CREDITS);
		} else {
			this.listingService.enableListingsForRecruiter(event.getRecruiterId());
			this.listingService.updateCreditsForUser(event.getRecruiterId(), RecruiterCredit.DEFAULT_CREDITS);
		}
		
	}

	/**
	* Refer to the ListingsExternalEventListener interface for details
	*/
	@Override
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event) {
		this.listingService.addCreditsRecordForUser(event.getRecruiterId());
	}

}
