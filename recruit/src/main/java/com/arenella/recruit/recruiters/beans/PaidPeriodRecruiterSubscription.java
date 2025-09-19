package com.arenella.recruit.recruiters.beans;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
* Represents a yearly subscription that a Recruiter can take out to 
* access the system for a period of 12 months from the moment the subscription 
* is activated.
* @author K Parkings
*/
public class PaidPeriodRecruiterSubscription implements RecruiterSubscription{

	private UUID 					subscriptionId;
	private String 					recruiterId;
	private LocalDateTime 			created;
	private LocalDateTime 			activatedDate;
	private subscription_status		status;
	private subscription_type		type;
	private INVOICE_TYPE			invoiceType;
	private boolean					currentSubscription;
	
	/**
	* Constructor based upon a builder
	* @param builder
	*/
	public PaidPeriodRecruiterSubscription(PaidPeriodRecruiterSubscriptionBuilder builder) {
		
		this.subscriptionId 		= builder.subscriptionId;
		this.created				= builder.created;
		this.activatedDate			= builder.activatedDate;
		this.recruiterId			= builder.recruiterId;
		this.status					= builder.status;
		this.type					= builder.type;
		this.invoiceType			= builder.invoiceType;
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
	* Activates the subscription
	*/
	public void activateSubscription() {
		this.activatedDate 	= LocalDateTime.now();
		this.status 		= subscription_status.ACTIVE;
	}
	
	/**
	* Activates the existing subscription
	*/
	public void activateExistingSubscription() {
		this.status 		= subscription_status.ACTIVE;
	}
	
	/**
	* Disables the subscription due to unpaid invoice 
	*/
	public void disablePendingPayment() {
		this.status 		= subscription_status.DISABLED_PENDING_PAYMENT;
	}

	/**
	* Ends the current Subscription 
	*/
	public void endSubscription() {
		this.currentSubscription 	= false;
		this.status 				= subscription_status.SUBSCRIPTION_ENDED;
	}
	
	/**
	* Ends the current Subscription but marks it as having an unpaid invoice. 
	*/
	public void endSubscriptionWithUnpaidInvoice() {
		this.status 				= subscription_status.SUBSCRIPTION_INVOICE_UNPAID;
		this.currentSubscription 	= true;
	}
	
	/**
	* Renews the subscription. This involves setting the activation date 
	* to a year from the previous activation and changing the status to 
	* indicate that a new invoice for the new period needs to be sent 
	*/
	public void renewSubscription() {
		this.status 		= subscription_status.ACTIVE_PENDING_PAYMENT;
		this.activatedDate 	= this.activatedDate.plusYears(1);
	}
	
	/**
	* Marks subscription as being active and indicates that the invoice
	* was sent to the recruiter
	*/
	public void invoiceSent() {
		this.status = subscription_status.ACTIVE_INVOICE_SENT;
	}
	
	/**
	* Returns an instance of a builder for the YearlyRecruiterSubscription class
	* @return Builder
	*/
	public static PaidPeriodRecruiterSubscriptionBuilder builder() {
		return new PaidPeriodRecruiterSubscriptionBuilder();
	}
	
	/**
	* Builder for the YearlyRecruiterSubscription class
	* @author K Parkings
	*/
	public static class PaidPeriodRecruiterSubscriptionBuilder {
		
		private UUID 					subscriptionId;
		private String 					recruiterId;
		private LocalDateTime 			created;
		private LocalDateTime 			activatedDate;
		private subscription_status		status;
		private subscription_type		type;
		private INVOICE_TYPE			invoiceType;
		private boolean					currentSubscription;
		
		/**
		* Sets the Unique Id of the subscription
		* @param subscriptionId - Unique identifier of the subscription
		* @return Builder
		*/
		public PaidPeriodRecruiterSubscriptionBuilder subscriptionId(UUID subscriptionId) {
			this.subscriptionId = subscriptionId;
			return this;
		}
		
		/**
		* Sets the unique id of the Recruiter the Subscription is associated with
		* @param recruiterId - Unique Id of the Recruiter
		* @return Builder
		*/
		public PaidPeriodRecruiterSubscriptionBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the Date the Subscriptions was created
		* @param created - Creation date of the Subscription
		* @return Builder
		*/
		public PaidPeriodRecruiterSubscriptionBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the Status of the Subscriptions
		* @param created - Status of the Subscription
		* @return Builder
		*/
		public PaidPeriodRecruiterSubscriptionBuilder status(subscription_status status) {
			this.status = status;
			return this;
		}
		
		/**
		* Sets the type of the Subscription
		* @param type - type of Subscription
		* @return Builder
		*/
		public PaidPeriodRecruiterSubscriptionBuilder type(subscription_type type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets Invoice type to send to Recruiter
		* @param type - type of invoice
		* @return Builder
		*/
		public PaidPeriodRecruiterSubscriptionBuilder invoiceType(INVOICE_TYPE invoiceType) {
			this.invoiceType = invoiceType;
			return this;
		}
		
		/**
		* Sets the date from which the Subscription is active
		* @param activatedDate - When the subscription becomes/became active
		* @return Builder
		*/
		public PaidPeriodRecruiterSubscriptionBuilder activateDate(LocalDateTime activatedDate) {
			this.activatedDate = activatedDate;
			return this;
		}
		
		/**
		* Sets whether or not the Subscription is the current subscription
		* @param currentSubscription - Whether or not the subscription is the current subscription
		* @return Builder
		*/
		public PaidPeriodRecruiterSubscriptionBuilder currentSubscription(boolean currentSubscription) {
			this.currentSubscription = currentSubscription;
			return this;
		}
		
		/**
		* Returns an initialized instance of YearlyRecruiterSubscription
		* @return new instance of YearlyRecruiterSubscription
		*/
		public PaidPeriodRecruiterSubscription build() {
			return new PaidPeriodRecruiterSubscription(this);
		}
	}
	
	/**
	* Returns true if the subscription period has passed since the activation date. This signals the end of the 
	* subscription period and that a new Invoice must be sent to the Recruiter
	* @param subscription - Subscription to test
	* @return whether a year has passed since the subscription was activated
	*/
	public static boolean hasPeriodElapsedSinceActivation(PaidPeriodRecruiterSubscription subscription) {
		
		//final int lengthOfSubcriptionPeriodInYears = 1;
		
		LocalDateTime 	now 		= LocalDateTime.now();
		LocalDateTime 	expiryDate 	= calculateExpiryDate(subscription.getType(), subscription.getActivatedDate());
		
		return expiryDate.isBefore(now);
		
	}
	
	/**
	* Returns whether or not a month has passed since the subscription was activated.
	* @param subscription
	* @return
	*/
	public static boolean hasOneMonthElapsedSinceActivation(PaidPeriodRecruiterSubscription subscription) {
		
		LocalDateTime 	now 		= LocalDateTime.now();
		LocalDateTime 	expiryDate 	= subscription.getActivatedDate().plusMonths(1);
		
		return expiryDate.isBefore(now);
		
	}
	
	/**
	* Calculated when the subscription is due to expire
	* @param type
	* @param activationDate
	* @return
	*/
	private static LocalDateTime calculateExpiryDate(subscription_type type, LocalDateTime activationDate) {
		
		return switch(type) {
			case YEAR_SUBSCRIPTION -> activationDate.plusYears(1);
			case ONE_MONTH_SUBSCRIPTION -> activationDate.plusMonths(1);
			case THREE_MONTHS_SUBSCRIPTION -> activationDate.plusMonths(3);
			case SIX_MONTHS_SUBSCRIPTION -> activationDate.plusMonths(6);
			default -> throw new IllegalArgumentException("Unexpected value: " + type);	
		};
		
	}

}