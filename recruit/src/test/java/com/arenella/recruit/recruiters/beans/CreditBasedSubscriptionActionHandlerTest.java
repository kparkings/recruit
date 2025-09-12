package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.recruiters.adapters.RecruitersExternalEventPublisher;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;

/**
* Unit tests for the CreditBasedSubscriptionActionHandler class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class CreditBasedSubscriptionActionHandlerTest {

	@InjectMocks
	private CreditBasedSubscriptionActionHandler 	handler 					= new CreditBasedSubscriptionActionHandler();
	
	@Mock
	private RecruitersExternalEventPublisher 		mockExternEventPublisher;
	
	/**
	* Tests if Action provided is not supported an Exception is thrown
	* @throws Exception
	*/
	@Test
	void testPermformAction_unsupportedAction() {
		
		Recruiter 				recruiter 		= Recruiter.builder().build();
		RecruiterSubscription 	subscription 	= CreditBasedSubscription.builder().build();
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			handler.performAction(recruiter, subscription, subscription_action.DISABLE_PENDING_PAYMENT, true);
		});
		
	}
	
	/**
	* Tests if Action non Admin user attempts a reject subscription exception is thrown
	* @throws Exception
	*/
	@Test
	void testPermformAction_rejectSubscription_notAdmin() {
		
		Recruiter 				recruiter 		= Recruiter.builder().build();
		RecruiterSubscription 	subscription 	= CreditBasedSubscription.builder().status(subscription_status.AWAITING_ACTIVATION).build();
		
		Assertions.assertThrows(IllegalAccessException.class, () -> {
			handler.performAction(recruiter, subscription, subscription_action.REJECT_SUBSCRIPTION, false);
		});
		
	}
	
	/**
	* Tests no Invoice type for credit based subscription type
	*/
	@Test
	void testGetInvoiceType() {
		
		RecruiterSubscription 	subscription 	= CreditBasedSubscription.builder().status(subscription_status.AWAITING_ACTIVATION).build();
		assertTrue(subscription.getInvoiceType().isEmpty());
	}
	
	/**
	* Tests if Action non Admin user attempts an activate subscription exception is thrown
	* @throws Exception
	*/
	@Test
	void testPermformAction_acceptSubscription_notAdmin() {
		
		Recruiter 				recruiter 		= Recruiter.builder().build();
		RecruiterSubscription 	subscription 	= CreditBasedSubscription.builder().status(subscription_status.AWAITING_ACTIVATION).build();
		
		Assertions.assertThrows(IllegalAccessException.class, () -> {
			handler.performAction(recruiter, subscription, subscription_action.ACTIVATE_SUBSCRIPTION, false);
		});
		
	}
	
	/**
	* Tests Success for Activate subscription
	 * @throws IllegalAccessException 
	* @throws Exception
	*/
	@Test
	void testPermformAction_activareSubscription_success() throws IllegalAccessException {
		
		Recruiter 				recruiter 		= Recruiter.builder().firstName("").surname("").userId("").build();
		RecruiterSubscription 	subscription 	= CreditBasedSubscription.builder().status(subscription_status.AWAITING_ACTIVATION).build();
		
		handler.performAction(recruiter, subscription, subscription_action.ACTIVATE_SUBSCRIPTION, true);
		
		Mockito.verify(this.mockExternEventPublisher).publishSubscriptionAddedEvent(Mockito.any());
		Mockito.verify(this.mockExternEventPublisher).publishSendEmailCommand(Mockito.any());
		
	}
	
	/**
	* Tests Success for Activate subscription
	 * @throws IllegalAccessException 
	* @throws Exception
	*/
	@Test
	void testPermformAction_rejectSubscription_success() throws IllegalAccessException {
		
		Recruiter 				recruiter 		= Recruiter.builder().build();
		RecruiterSubscription 	subscription 	= CreditBasedSubscription.builder().status(subscription_status.AWAITING_ACTIVATION).build();
		
		handler.performAction(recruiter, subscription, subscription_action.REJECT_SUBSCRIPTION, true);
		
		Mockito.verify(this.mockExternEventPublisher, Mockito.never()).publishSubscriptionAddedEvent(Mockito.any());
		Mockito.verify(this.mockExternEventPublisher, Mockito.never()).publishSendEmailCommand(Mockito.any());
		
		assertEquals(subscription_status.SUBSCRIPTION_ENDED, subscription.getStatus());
		
	}
	
}