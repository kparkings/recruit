package com.arenella.recruit.recruiters.beans;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
* Represents a Subscription that the Recruiter that gives them the right
* to access features in the site
* @author K Parkings
*/
public interface RecruiterSubscription {

	public enum subscription_type 	{CREDIT_BASED_SUBSCRIPTION, ONE_MONTH_SUBSCRIPTION, THREE_MONTHS_SUBSCRIPTION, SIX_MONTHS_SUBSCRIPTION, YEAR_SUBSCRIPTION}
	public enum subscription_status {AWAITING_ACTIVATION, ACTIVE_PENDING_PAYMENT, ACTIVE_INVOICE_SENT, ACTIVE, DISABLED_PENDING_PAYMENT, SUBSCRIPTION_ENDED}
	public enum subscription_action {INVOICE_SENT, ACTIVATE_SUBSCRIPTION, REJECT_SUBSCRIPTION, DISABLE_PENDING_PAYMENT, RENEW_SUBSCRIPTION, END_SUBSCRIPTION}
	public enum INVOICE_TYPE 		{BUSINESS, PERSON}
	
	/**
	* Returns the unique Id of the Subscription
	* @return Unique Id of the subscription
	*/
	public UUID getSubscriptionId();
	
	/**
	* Returns when the Subscriptions was created
	* @return Creation Date
	*/
	public LocalDateTime getCreated();
	
	/**
	* Returns when the Subscription is considered active from
	* @return When the subscriptions becomes active
	*/
	public LocalDateTime getActivatedDate();
	
	/**
	* Returns the uniqueId of the Recruiter the Subscription is
	* associated with 
	* @return Id of Recruiter
	*/
	public String getRecruiterId();
	
	/**
	* Returns whether or not the subscription is active or not
	* @return Whether the Subscription is active
	*/
	public boolean isCurrentSubscription();
	
	/**
	* Returns the type of the Subscription
	* @return Type of Subscription
	*/
	public subscription_type getType();
	
	/**
	* What type of Invoice to send
	* @return Invoice Type
	*/
	public Optional<INVOICE_TYPE> getInvoiceType();
	
	/**
	* Returns the current status of the Subscription
	* @return
	*/
	public subscription_status getStatus();
	
}
