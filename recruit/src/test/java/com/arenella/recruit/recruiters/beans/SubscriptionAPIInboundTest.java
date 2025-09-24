package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.RecruiterSubscription.INVOICE_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

/**
* Unit tests for the SubscriptionAPIInbound class
* @author K Parkings
*/
class SubscriptionAPIInboundTest {

	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		final subscription_type type 		= subscription_type.CREDIT_BASED_SUBSCRIPTION;
		final INVOICE_TYPE 		invoiceType = INVOICE_TYPE.BUSINESS;
		
		SubscriptionAPIInbound subscription = SubscriptionAPIInbound.builder().type(type).invoiceType(invoiceType).build();
		
		assertEquals(type, 			subscription.getType());
		assertEquals(invoiceType, 	subscription.getInvoiceType());
		
	}
	
}
