package com.arenella.recruit.recruiters.beans;

import java.time.LocalDateTime;
import java.util.UUID;

import com.arenella.recruit.recruiters.utils.RecruiterSubscriptionActionHandler;

/**
* Represents a yearly subscription that a Recruiter can take out to 
* access the system for a period of 12 months from the moment the subcription 
* is activated.
* @author K Parkings
*/
public class YearlyRecruiterSubscription implements RecruiterSubscription{

	private UUID 					subscriptionId;
	private String 					recruiterId;
	private LocalDateTime 			created;
	private LocalDateTime 			activatedDate;
	private subscription_status		status;
	private boolean					currentSubscription;
	
	/**
	* Constructor based upon a builder
	* @param builder
	*/
	public YearlyRecruiterSubscription(YearlyRecruiterSubscriptionBuilder builder) {
		
		this.subscriptionId 		= builder.subscriptionId;
		this.created				= builder.created;
		this.activatedDate			= builder.activatedDate;
		this.recruiterId			= builder.recruiterId;
		this.status					= builder.status;
		this.currentSubscription 	= builder.currentSubscription;
		
	}
	
	/**
	* Refer to the RecruiterSubscription interface for details 
	*/
	@Override
	public UUID getSubscriptionId() {
		return this.subscriptionId;
	}

	/**
	* Refer to the RecruiterSubscription interface for details 
	*/
	@Override
	public LocalDateTime getCreated() {
		return this.created;
	}

	/**
	* Refer to the RecruiterSubscription interface for details 
	*/
	@Override
	public LocalDateTime getActivatedDate() {
		return this.activatedDate;
	}

	/**
	* Refer to the RecruiterSubscription interface for details 
	*/
	@Override
	public String getRecruiterId() {
		return this.recruiterId;
	}

	/**
	* Refer to the RecruiterSubscription interface for details 
	*/
	@Override
	public boolean isCurrentSubscription() {
		return this.currentSubscription;
	}

	/**
	* Refer to the RecruiterSubscription interface for details 
	*/
	@Override
	public subscription_type getType() {
		return subscription_type.YEAR_SUBSCRIPTION;
	}

	/**
	* Refer to the RecruiterSubscription interface for details 
	*/
	@Override
	public subscription_status getStatus() {
		return this.status;
	}

	/**
	* Returns an instance of a builder for the YearlyRecruiterSubscription class
	* @return Builder
	*/
	public static YearlyRecruiterSubscriptionBuilder builder() {
		return new YearlyRecruiterSubscriptionBuilder();
	}
	
	/**
	* Builder for the YearlyRecruiterSubscription class
	* @author K Parkings
	*/
	public static class YearlyRecruiterSubscriptionBuilder {
		
		private UUID 					subscriptionId;
		private String 					recruiterId;
		private LocalDateTime 			created;
		private LocalDateTime 			activatedDate;
		private subscription_status		status;
		private boolean					currentSubscription;
		
		/**
		* Sets the Unique Id of the subscription
		* @param subscriptionId - Unique identifier of the subscription
		* @return Builder
		*/
		public YearlyRecruiterSubscriptionBuilder subscriptionId(UUID subscriptionId) {
			this.subscriptionId = subscriptionId;
			return this;
		}
		
		/**
		* Sets the unique id of the Recruiter the Subscription is associated with
		* @param recruiterId - Unique Id of the Recruiter
		* @return Builder
		*/
		public YearlyRecruiterSubscriptionBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the Date the Subscriptions was created
		* @param created - Creation date of the Subscription
		* @return Builder
		*/
		public YearlyRecruiterSubscriptionBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the Status of the Subscriptions
		* @param created - Status of the Subscription
		* @return Builder
		*/
		public YearlyRecruiterSubscriptionBuilder status(subscription_status status) {
			this.status = status;
			return this;
		}
		
		/**
		* Sets the date from which the Subscription is active
		* @param activatedDate - When the subscription becomes/became active
		* @return Builder
		*/
		public YearlyRecruiterSubscriptionBuilder activateDate(LocalDateTime activatedDate) {
			this.activatedDate = activatedDate;
			return this;
		}
		
		/**
		* Sets whether or not the Subscription is the current subscription
		* @param currentSubscription - Whether or not the subscription is the current subscription
		* @return Builder
		*/
		public YearlyRecruiterSubscriptionBuilder currentSubscription(boolean currentSubscription) {
			this.currentSubscription = currentSubscription;
			return this;
		}
		
		/**
		* Returns an initialized instance of YearlyRecruiterSubscription
		* @return new instance of YearlyRecruiterSubscription
		*/
		public YearlyRecruiterSubscription build() {
			return new YearlyRecruiterSubscription(this);
		}
	}
	
	/**
	* Returns an ActionHandler for the Subscription
	* @return
	*/
	public static ActionHandler getActionHandler() {
		return new ActionHandler();
	}
	
	/**
	* ActionHandler for the subscription
	* @author K Parkings
	*/
	public static class ActionHandler implements RecruiterSubscriptionActionHandler{

		/**
		* Refer to RecruiterSubscriptionActionHandler for details 
		*/
		@Override
		public void performAction(Recruiter recruiter, RecruiterSubscription subscription,  subscription_action action) {

			switch(action) {
				default:{
					throw new IllegalArgumentException("Unknown action " + action + " for subscription type: " + subscription_type.YEAR_SUBSCRIPTION);
				}
			}
			
		}
		
	}
	
}