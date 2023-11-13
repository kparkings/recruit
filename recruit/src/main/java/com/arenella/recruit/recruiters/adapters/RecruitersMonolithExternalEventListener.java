package com.arenella.recruit.recruiters.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterUserAccountCreatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.recruiters.beans.RecruiterCredit;
import com.arenella.recruit.recruiters.services.SupplyAndDemandService;

/**
* Implementation of ExternalEventListener optimised to 
* work for when the services are physically located in 
* the same Monolith.
* @author K Parkings
*/
@Service
public class RecruitersMonolithExternalEventListener implements RecruitersExternalEventListener{
	
	@Autowired
	private SupplyAndDemandService supplyAndDemandService;
	
	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterAccountCreatedEvent(RecruiterUserAccountCreatedEvent event) {
		// TODO Auto-generated method stub
		
	}

	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterNoOpenSubscriptionsEvent(RecruiterNoOpenSubscriptionEvent event) {
		this.supplyAndDemandService.disableSupplyAndDemandPostsForRecruiter(event.geRecruiterId());
		this.supplyAndDemandService.updateCreditsForUser(event.geRecruiterId(), RecruiterCredit.DISABLED_CREDITS);
	}

	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForSubscriptionAddedEvent(SubscriptionAddedEvent event) {
		this.supplyAndDemandService.enableSupplyAndDemandPostsForRecruiter(event.getRecruiterId());
		this.supplyAndDemandService.updateCreditsForUser(event.getRecruiterId(), RecruiterCredit.DISABLED_CREDITS);
	}

}