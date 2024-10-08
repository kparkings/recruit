package com.arenella.recruit.recruiters.beans;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
* API specific representation of a Subscription
* @author K Parkings
*/
public class RecruiterSubscriptionAPIOutbound implements RecruiterSubscription{

	private UUID 					subscriptionId;
	private String 					recruiterId;
	private LocalDateTime 			created;
	private LocalDateTime 			activatedDate;
	private subscription_status		status;
	private boolean					currentSubscription;
	private subscription_type		type;
	private INVOICE_TYPE			invoiceType;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public RecruiterSubscriptionAPIOutbound(RecruiterSubscriptionAPIOutboundBuilder builder) {
	
		this.subscriptionId 		= builder.subscriptionId;
		this.created				= builder.created;
		this.activatedDate			= builder.activatedDate;
		this.recruiterId			= builder.recruiterId;
		this.status					= builder.status;
		this.currentSubscription	= builder.currentSubscription;
		this.type					= builder.type;
		this.invoiceType			= builder.invoiceType;
		
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
		return this.type;
	}
	
	/**
	* Refer to the RecruiterSubscription interface for details 
	*/
	@Override
	public Optional<INVOICE_TYPE> getInvoiceType() {
		return Optional.ofNullable(this.invoiceType);
	}

	/**
	* Refer to the RecruiterSubscription interface for details 
	*/
	@Override
	public subscription_status getStatus() {
		return this.status;
	}
	
	/**
	* Returns a Builder for the RecruiterSubscriptionAPIOutboud class
	* @return
	*/
	public static RecruiterSubscriptionAPIOutboundBuilder builder() {
		return new RecruiterSubscriptionAPIOutboundBuilder();
	}
	
	/**
	* Builder for the FirstGenRecruiterSubscription class
	* @author K Parkings
	*/
	public static class RecruiterSubscriptionAPIOutboundBuilder {
		
		private UUID 					subscriptionId;
		private String 					recruiterId;
		private LocalDateTime 			created;
		private LocalDateTime 			activatedDate;
		private subscription_status		status;
		private boolean					currentSubscription;
		private subscription_type		type;
		private INVOICE_TYPE			invoiceType;
		
		/**
		* Sets the Unique Id of the subscription
		* @param subscriptionId - Unique identifier of the subscription
		* @return Builder
		*/
		public RecruiterSubscriptionAPIOutboundBuilder subscriptionId(UUID subscriptionId) {
			this.subscriptionId = subscriptionId;
			return this;
		}
		
		/**
		* Sets the unique id of the Recruiter the Subscription is associated with
		* @param recruiterId - Unique Id of the Recruiter
		* @return Builder
		*/
		public RecruiterSubscriptionAPIOutboundBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the Date the Subscriptions was created
		* @param created - Creation date of the Subscription
		* @return Builder
		*/
		public RecruiterSubscriptionAPIOutboundBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the Status of the Subscriptions
		* @param created - Status of the Subscription
		* @return Builder
		*/
		public RecruiterSubscriptionAPIOutboundBuilder status(subscription_status status) {
			this.status = status;
			return this;
		}
		
		/**
		* Sets the date from which the Subscription is active
		* @param activatedDate - When the subscription becomes/became active
		* @return Builder
		*/
		public RecruiterSubscriptionAPIOutboundBuilder activateDate(LocalDateTime activatedDate) {
			this.activatedDate = activatedDate;
			return this;
		}
		
		/**
		* Sets the type of the Subscription
		* @param type - subscription type
		* @return Builder
		*/
		public RecruiterSubscriptionAPIOutboundBuilder type(subscription_type type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the type of Invoice to send the Recruiter
		* @param invoiceType - type of Invoice
		* @return Builder
		*/
		public RecruiterSubscriptionAPIOutboundBuilder invoiceType(INVOICE_TYPE invoiceType) {
			this.invoiceType = invoiceType;
			return this;
		}
		
		/**
		* Sets whether or not the subscription is the current Subscription
		* @param currentSubscrition -  whether or not the subscription is the current Subscription
		* @return Builder
		*/
		public RecruiterSubscriptionAPIOutboundBuilder currentSubscription(boolean currentSubscription) {
			this.currentSubscription = currentSubscription;
			return this;
		}
		
		/**
		* Returns an initialzied instance of FirstGenRecruiterSubscription
		* @return Initialzied instance of FirstGenRecruiterSubscription
		*/
		public RecruiterSubscriptionAPIOutbound build() {
			return new RecruiterSubscriptionAPIOutbound(this);
		}
		
	}
	
	/**
	* Converts domain representation of a RecruiterSubscription to an API outbound specific 
	* representation
	* @param subscription - to convert
	* @return converted subscription
	*/
	public static RecruiterSubscriptionAPIOutbound convertFromSubscription(RecruiterSubscription subscription) {
		
		return RecruiterSubscriptionAPIOutbound
										.builder()
											.activateDate(subscription.getActivatedDate())
											.created(subscription.getCreated())
											.recruiterId(subscription.getRecruiterId())
											.status(subscription.getStatus())
											.subscriptionId(subscription.getSubscriptionId())
											.type(subscription.getType())
											.currentSubscription(subscription.isCurrentSubscription())
											.invoiceType(subscription.getInvoiceType().isEmpty() ? null : subscription.getInvoiceType().get())
										.build();
		
	}

}