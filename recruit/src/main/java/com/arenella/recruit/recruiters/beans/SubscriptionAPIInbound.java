package com.arenella.recruit.recruiters.beans;

import com.arenella.recruit.recruiters.beans.RecruiterSubscription.INVOICE_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Class represents a request for a Subscription of 
* a given type for a Recruiter
* @author K Parkings
*/
@JsonDeserialize(builder=SubscriptionAPIInbound.SubscriptionAPIInboundBuilder.class)
public class SubscriptionAPIInbound {

	public subscription_type 	type;
	public INVOICE_TYPE 		invoiceType;
	
	/**
	* Constructor based upon a Buider
	* @param builder - Contains initialization values
	*/
	public SubscriptionAPIInbound(SubscriptionAPIInboundBuilder builder) {
		this.type 			= builder.type;
		this.invoiceType 	= builder.invoiceType;
	}
	
	/**
	* Returns the type of Subscription being requested
	* @return type of subscription 
	*/
	public subscription_type getType() {
		return type;
	}
	
	/**
	* Returns the type of Invoice to send to the Recruiter
	* @return Invoice type
	*/
	public INVOICE_TYPE getInvoiceType() {
		return this.invoiceType;
	}
	
	/**
	* Returns a Builder for the SubscriptionAPIInbound class
	* @return Builder for the class
	*/
	public static SubscriptionAPIInboundBuilder builder() {
		return new SubscriptionAPIInboundBuilder();
	}
	
	/**
	* Builder for the SubscriptionAPIInbound class
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class SubscriptionAPIInboundBuilder {
	
		public subscription_type 	type;
		public INVOICE_TYPE 		invoiceType;
		
		/**
		* Sets the type of Subscription being requested
		* @param type - type requested
		* @return Builder
		*/
		public SubscriptionAPIInboundBuilder type(subscription_type type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the type of Invoice to send to the Recruiter
		* @param invoiceType - Invoice type
		* @return Builder
		*/
		public SubscriptionAPIInboundBuilder invoiceType(INVOICE_TYPE invoiceType) {
			this.invoiceType = invoiceType;
			return this;
		}
		
		/**
		* Returns an initialized instance of SubscriptionAPIInbound
		* @return
		*/
		public SubscriptionAPIInbound build() {
			return new SubscriptionAPIInbound(this);
	
		}
	}
	
}