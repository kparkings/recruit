package com.arenella.recruit.recruiters.beans;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arenella.recruit.recruiters.adapters.RecruitersExternalEventPublisher;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;
import com.arenella.recruit.recruiters.utils.RecruiterSubscriptionActionHandler;

/**
* ActionHandler for the TrialPeriodSubscription
* @author K Parkings
*/
@Component
public class YearlySubscriptionActionHandler implements RecruiterSubscriptionActionHandler{
	
	@Autowired
	private RecruitersExternalEventPublisher 	externEventPublisher;

	/**
	* Refer to RecruiterSubscriptionActionHandler for details 
	*/
	@Override
	public Optional<SubscriptionActionFeedback> performAction(Recruiter recruiter, RecruiterSubscription subscription, subscription_action action, Boolean isAdminUser) throws IllegalAccessException {
		
		switch(action) {
			case ACTIVATE_SUBSCRIPTION:{
			
				if (!isAdminUser) {
					throw new IllegalAccessException("You are not authorized to carry out this action");
				}
				
				if (subscription.getStatus() != subscription_status.ACTIVE_PENDING_PAYMENT && subscription.getStatus() != subscription_status.DISABLED_PENDING_PAYMENT) {
					throw new IllegalStateException("Can only activate subscritions in state ACTIVE_PENDING_PAYMENT or DISABLED_PENDING_PAYMENT: " + subscription.getSubscriptionId());
				}
				
				((YearlyRecruiterSubscription)subscription).activateSubscription();
				
				this.externEventPublisher.publishRecruiterHasOpenSubscriptionEvent(recruiter.getUserId());
				
				return Optional.empty();
			}
			case DISABLE_PENDING_PAYMENT: {
				
				if (!isAdminUser) {
					throw new IllegalAccessException("You are not authorized to carry out this action");
				}
				
				if (subscription.getStatus() != subscription_status.ACTIVE_PENDING_PAYMENT) {
					throw new IllegalStateException("Can only activate subscritions in state ACTIVE_PENDING_PAYMENT: " + subscription.getSubscriptionId());
				}
				
				((YearlyRecruiterSubscription)subscription).disablePendingPayment();
				
				this.externEventPublisher.publishRecruiterNoOpenSubscriptionsEvent(recruiter.getUserId());
				
				return Optional.empty();
			}
			case END_SUBSCRIPTION: {
				
				if (!isAdminUser && subscription.getStatus() == subscription_status.DISABLED_PENDING_PAYMENT) {
					throw new IllegalAccessException("You are not authorized to carry out this action");
				}
				
				if (subscription.getStatus() == subscription_status.SUBSCRIPTION_ENDED) {
					throw new IllegalStateException("Subscription is already ended. Cant end a second time: " + subscription.getSubscriptionId());
				}
				
				((YearlyRecruiterSubscription)subscription).endSubscription();
				
				this.externEventPublisher.publishRecruiterNoOpenSubscriptionsEvent(recruiter.getUserId());
				
				return Optional.empty();
				
			}
			case RENEW_SUBSCRIPTION: {
				
				if (!isAdminUser) {
					throw new IllegalAccessException("You are not authorized to carry out this action");
				}
				
				if (subscription.getStatus() != subscription_status.ACTIVE) {
					throw new IllegalStateException("Subscription is already ended. Cant end a second time: " + subscription.getSubscriptionId());
				}
				
				((YearlyRecruiterSubscription)subscription).renewSubscription();
				
				return Optional.empty();
				
			}
			default:{
				throw new IllegalArgumentException("Unknown action " + action + " for subscription type: " + subscription_type.YEAR_SUBSCRIPTION);
			}
		}
		
	}
		
}