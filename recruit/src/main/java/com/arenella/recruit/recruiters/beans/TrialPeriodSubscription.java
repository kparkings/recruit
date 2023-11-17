package com.arenella.recruit.recruiters.beans;

import java.time.LocalDateTime;
import java.util.UUID;

/**
* 30 Day trial period subscription
* @author K Parkings
*/
public class TrialPeriodSubscription implements RecruiterSubscription{

	public static final int trialPeriodInDays = 30;
	
	private UUID 					subscriptionId;
	private String 					recruiterId;
	private LocalDateTime 			created;
	private LocalDateTime 			activatedDate;
	private subscription_status		status					= subscription_status.AWAITING_ACTIVATION;
	private boolean					currentSubscription;
	
	/**
	* Constructor based upon a builder
	* @param builder
	*/
	public TrialPeriodSubscription(TrialPeriodSubscriptionBuilder builder) {
		
		this.subscriptionId 		= builder.subscriptionId;
		this.created				= builder.created;
		this.activatedDate			= builder.activatedDate;
		this.recruiterId			= builder.recruiterId;
		this.status					= builder.status;
		this.currentSubscription	= builder.currentSubscription;
	}
	
	/**
	* Refer to RecruiterSubscription for details 
	*/
	@Override
	public UUID getSubscriptionId() {
		return this.subscriptionId;
	}

	/**
	* Refer to RecruiterSubscription for details 
	*/
	@Override
	public LocalDateTime getCreated() {
		return this.created;
	}

	/**
	* Refer to RecruiterSubscription for details 
	*/
	@Override
	public LocalDateTime getActivatedDate() {
		return this.activatedDate;
	}

	/**
	* Refer to RecruiterSubscription for details 
	*/
	@Override
	public String getRecruiterId() {
		return this.recruiterId;
	}

	/**
	* Refer to RecruiterSubscription for details 
	*/
	@Override
	public boolean isCurrentSubscription() {
		return this.currentSubscription;
	}

	/**
	* Refer to RecruiterSubscription for details 
	*/
	@Override
	public subscription_type getType() {
		return subscription_type.TRIAL_PERIOD;
	}

	/**
	* Refer to RecruiterSubscription for details 
	*/
	@Override
	public subscription_status getStatus() {
		return this.status;
	}
	
	/**
	* Ends the Subscription
	*/
	public void endSubscription() {
		this.status = subscription_status.SUBSCRIPTION_ENDED;
	}
	
	/**
	* Activates the Subscription 
	*/
	public void activateSubscription() {
		
		if (this.status != null && this.status != subscription_status.AWAITING_ACTIVATION) {
			throw new IllegalStateException("You cant activate a Subscription more than once");
		}
		
		this.currentSubscription	= true;
		this.status 				= subscription_status.ACTIVE;
		this.activatedDate 			= LocalDateTime.now();
		
	}
	
	/**
	* Sets whether or not the Subscription is the current subscription
	* @param currentSubscription - Whether or not this is the current subscription
	*/
	public void setCurrentSubscription(boolean currentSubscription) {
		this.currentSubscription = currentSubscription;
	}
	
	/**
	* Returns an instance of a builder for the TrialPeriodSubscription class
	* @return Builder
	*/
	public static TrialPeriodSubscriptionBuilder builder() {
		return new TrialPeriodSubscriptionBuilder();
	}
	
	/**
	* Builder for the TrialPeriodSubscription class
	* @author K Parkings
	*/
	public static class TrialPeriodSubscriptionBuilder {
		
		private UUID 					subscriptionId;
		private String 					recruiterId;
		private LocalDateTime 			created;
		private LocalDateTime 			activatedDate;
		private subscription_status		status					= subscription_status.AWAITING_ACTIVATION;
		private boolean					currentSubscription;
		
		/**
		* Sets the Unique Id of the subscription
		* @param subscriptionId - Unique identifier of the subscription
		* @return Builder
		*/
		public TrialPeriodSubscriptionBuilder subscriptionId(UUID subscriptionId) {
			this.subscriptionId = subscriptionId;
			return this;
		}
		
		/**
		* Sets the unique id of the Recruiter the Subscription is associated with
		* @param recruiterId - Unique Id of the Recruiter
		* @return Builder
		*/
		public TrialPeriodSubscriptionBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the Date the Subscriptions was created
		* @param created - Creation date of the Subscription
		* @return Builder
		*/
		public TrialPeriodSubscriptionBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the Status of the Subscriptions
		* @param created - Status of the Subscription
		* @return Builder
		*/
		public TrialPeriodSubscriptionBuilder status(subscription_status status) {
			this.status = status;
			return this;
		}
		
		/**
		* Sets the date from which the Subscription is active
		* @param activatedDate - When the subscription becomes/became active
		* @return Builder
		*/
		public TrialPeriodSubscriptionBuilder activateDate(LocalDateTime activatedDate) {
			this.activatedDate = activatedDate;
			return this;
		}
		
		/**
		* Sets whether or not the Subscription is the current subscription
		* @param currentSubscription - Whether or not the subscription is the current subscription
		* @return Builder
		*/
		public TrialPeriodSubscriptionBuilder currentSubscription(boolean currentSubscription) {
			this.currentSubscription = currentSubscription;
			return this;
		}
		
		/**
		* Returns an initialized instance of YearlyRecruiterSubscription
		* @return new instance of YearlyRecruiterSubscription
		*/
		public TrialPeriodSubscription build() {
			return new TrialPeriodSubscription(this);
		}
	}
	
	/**
	* Performs check to determine whether the Subscription has expired. TrialPeriodSubscriptions
	* expire automatically after 30 days.
	* @param subscription - Subscription to check
	* @return
	*/
	public static boolean isTrialPeriodExpired(TrialPeriodSubscription subscription) {
		
		final int lengthOfTrialPeriodInDays = 30;
		
		LocalDateTime 	now 		= LocalDateTime.now();
		LocalDateTime 	expiryDate 	= subscription.getActivatedDate().plusDays(lengthOfTrialPeriodInDays);
		
		return expiryDate.isBefore(now);
		
	}
	
}