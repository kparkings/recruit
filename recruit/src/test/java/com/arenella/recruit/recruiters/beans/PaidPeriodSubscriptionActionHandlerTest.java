package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.recruiters.adapters.RecruitersExternalEventPublisher;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;

/**
* Unit tests for the PaidPeriodSubscriptionActionHandler class
*/
@ExtendWith(MockitoExtension.class)
class PaidPeriodSubscriptionActionHandlerTest {

	@InjectMocks
	private PaidPeriodSubscriptionActionHandler handler;
	
	@Mock
	private Recruiter 							mockRecruiter;
	
	@Mock
	private PaidPeriodRecruiterSubscription		mockRecruiterSubscription;
	
	@Mock
	private RecruitersExternalEventPublisher 	mockExternEventPublisher;
	
	/**
	* Tests exception is thrown if a non admin user attempts to update the status 
	*/
	@Test
	void testInvoiceSentNotAdmin() {
		
		IllegalAccessException exception = assertThrows(IllegalAccessException.class, ()->{
			handler.performAction(mockRecruiter, mockRecruiterSubscription, subscription_action.INVOICE_SENT, false);
		});
		
		assertEquals("You are not authorized to carry out this action", exception.getMessage());
		
	}
	
	/**
	* Tests exception is thrown current subscription status is not active pending payment
	*/
	@Test
	void testInvoiceSentCurrentStatusNotActivePeningPayment() {
		
		when(mockRecruiterSubscription.getStatus()).thenReturn(subscription_status.AWAITING_ACTIVATION);
		when(mockRecruiterSubscription.getSubscriptionId()).thenReturn(UUID.randomUUID());
		
		
		IllegalStateException exception = assertThrows(IllegalStateException.class, ()->{
			handler.performAction(mockRecruiter, mockRecruiterSubscription, subscription_action.INVOICE_SENT, true);
		});
		
		assertEquals("Can only activate subscritions in state ACTIVE_PENDING_PAYMENT: " + mockRecruiterSubscription.getSubscriptionId(), exception.getMessage());
		
	}
	
	/**
	* Tests happy path for moving from status ACTIVE_PENDING_PAYMENT to INVOICE_SENT
	 * @throws IllegalAccessException 
	*/
	@Test
	void testInvoiceSentCurrentStatusHappyPath() throws IllegalAccessException {
		
		when(mockRecruiterSubscription.getStatus()).thenReturn(subscription_status.ACTIVE_PENDING_PAYMENT);
		
		handler.performAction(mockRecruiter, mockRecruiterSubscription, subscription_action.INVOICE_SENT, true);
		
		verify(mockExternEventPublisher).publishSubscriptionAddedEvent(any(SubscriptionAddedEvent.class));
		
	}
}
