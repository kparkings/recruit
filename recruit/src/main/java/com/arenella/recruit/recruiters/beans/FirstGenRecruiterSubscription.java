package com.arenella.recruit.recruiters.beans;

import java.time.LocalDateTime;
import java.util.UUID;

/**
* A FirstGen subscription is for Recruiters that signed up to the service before subscriptions
* where introduced. These Recruiters have signed up on the basis that they will pay a set 
* price / hourly margin for each candidate the place but without a pre-paid subscription.
* @author K Parkings
*/
public class FirstGenRecruiterSubscription implements RecruiterSubscription{

	private UUID 					subscriptionId;
	private String 					recruiterId;
	private LocalDateTime 			created;
	private LocalDateTime 			activatedDate;
	private subscription_status		status;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public FirstGenRecruiterSubscription(FirstGenRecruiterSubscriptionBuilder builder) {
	
		this.subscriptionId 	= builder.subscriptionId;
		this.created			= builder.created;
		this.activatedDate		= builder.activatedDate;
		this.recruiterId		= builder.recruiterId;
		this.status				= builder.status;
		
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
		boolean xx = this.getStatus() != subscription_status.SUBSCRIPTION_ENDED;
		return this.getStatus() != subscription_status.SUBSCRIPTION_ENDED;
	}

	/**
	* Refer to the RecruiterSubscription interface for details 
	*/
	@Override
	public subscription_type getType() {
		return subscription_type.FIRST_GEN;
	}

	/**
	* Refer to the RecruiterSubscription interface for details 
	*/
	@Override
	public subscription_status getStatus() {
		return this.status;
	}
	
	/**
	* Ends the subscription 
	*/
	public void endSubscription() {
		this.status = subscription_status.SUBSCRIPTION_ENDED;
	}
	
	/**
	* Returns a Builder for the FirstGenRecruiterSubscription class
	* @return Builder
	*/
	public static FirstGenRecruiterSubscriptionBuilder builder() {
		return new FirstGenRecruiterSubscriptionBuilder();
	}
	
	/**
	* Builder for the FirstGenRecruiterSubscription class
	* @author K Parkings
	*/
	public static class FirstGenRecruiterSubscriptionBuilder {
		
		private UUID 					subscriptionId;
		private String 					recruiterId;
		private LocalDateTime 			created;
		private LocalDateTime 			activatedDate;
		private subscription_status		status;
		
		/**
		* Sets the Unique Id of the subscription
		* @param subscriptionId - Unique identifier of the subscription
		* @return Builder
		*/
		public FirstGenRecruiterSubscriptionBuilder subscriptionId(UUID subscriptionId) {
			this.subscriptionId = subscriptionId;
			return this;
		}
		
		/**
		* Sets the unique id of the Recruiter the Subscription is associated wiith
		* @param recruiterId - Unique Id of the Recruiter
		* @return Builder
		*/
		public FirstGenRecruiterSubscriptionBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the Date the Subscriptions was created
		* @param created - Creation date of the Subscription
		* @return Builder
		*/
		public FirstGenRecruiterSubscriptionBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the Status of the Subscriptions
		* @param created - Status of the Subscription
		* @return Builder
		*/
		public FirstGenRecruiterSubscriptionBuilder status(subscription_status status) {
			this.status = status;
			return this;
		}
		
		/**
		* Sets the date from which the Subscription is active
		* @param activatedDate - When the subscription becomes/became active
		* @return Builder
		*/
		public FirstGenRecruiterSubscriptionBuilder activateDate(LocalDateTime activatedDate) {
			this.activatedDate = activatedDate;
			return this;
		}
		
		/**
		* Returns an initialzied instance of FirstGenRecruiterSubscription
		* @return Initialzied instance of FirstGenRecruiterSubscription
		*/
		public FirstGenRecruiterSubscription build() {
			return new FirstGenRecruiterSubscription(this);
		}
		
	}

}