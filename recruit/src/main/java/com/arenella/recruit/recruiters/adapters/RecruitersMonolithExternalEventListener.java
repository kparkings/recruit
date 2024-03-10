package com.arenella.recruit.recruiters.adapters;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.RecruiterNoOpenSubscriptionEvent;
import com.arenella.recruit.adapters.events.RecruiterUserAccountCreatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.recruiters.beans.RecruiterCredit;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;
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
		
		if (event.getSubscriptionType() != subscription_type.CREDIT_BASED_SUBSCRIPTION) {
			this.supplyAndDemandService.enableSupplyAndDemandPostsForRecruiter(event.getRecruiterId());
			this.supplyAndDemandService.updateCreditsForUser(event.getRecruiterId(), RecruiterCredit.DISABLED_CREDITS, Optional.of(this.isPaidSubscription(event)));
		
		} else {
			this.supplyAndDemandService.enableSupplyAndDemandPostsForRecruiter(event.getRecruiterId());
			this.supplyAndDemandService.updateCreditsForUser(event.getRecruiterId(), RecruiterCredit.DEFAULT_CREDITS, Optional.of(false));
		
		}
	}

	/**
	* Refer to ExternalEventListener interface for details 
	*/
	@Override
	public void listenForRecruiterCreatedEvent(RecruiterCreatedEvent event) {
		this.supplyAndDemandService.addCreditsRecordForUser(event.getRecruiterId());
	}
	
	/**
	* Returns whether the subscription is a paid subscription
	* @param event - Contains info about the subscription
	* @return whether the subscription is a paid subscription
	*/
	private boolean isPaidSubscription(SubscriptionAddedEvent event) {
		
		return switch(event.getSubscriptionType()) {
			case ONE_MONTH_SUBSCRIPTION, 
				 THREE_MONTHS_SUBSCRIPTION, 
				 SIX_MONTHS_SUBSCRIPTION, 
				 YEAR_SUBSCRIPTION -> true;
			default -> false;
		};
		
	}
	

}