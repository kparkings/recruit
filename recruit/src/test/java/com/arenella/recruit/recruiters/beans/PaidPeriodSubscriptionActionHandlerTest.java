package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
	
	/**
	* Test exception thrown if a non admin user attempts to end the subscription 
	* when the status of the subscription is DISABLED_PENDING_PAYMENT
	*/
	@Test
	void testEndSubscriptionNotAdminStatusDisablePendingPayment() {
		
		when(mockRecruiterSubscription.getStatus()).thenReturn(subscription_status.DISABLED_PENDING_PAYMENT);
		
		IllegalAccessException exception = assertThrows(IllegalAccessException.class, ()->{
			handler.performAction(mockRecruiter, mockRecruiterSubscription, subscription_action.END_SUBSCRIPTION, false);
		});	
		
		assertEquals("You are not authorized to carry out this action", exception.getMessage());
		
		verify(this.mockExternEventPublisher, never()).publishRecruiterNoOpenSubscriptionsEvent(any());
	}
	
	/**
	* Test exception thrown if a non admin user attempts to end the subscription 
	* when the status of the subscription is ACTIVE_INVOICE_SENT
	*/
	@Test
	void testEndSubscriptionNotAdminStatusActiveInvoiceSent() {
		
		when(mockRecruiterSubscription.getStatus()).thenReturn(subscription_status.ACTIVE_INVOICE_SENT);
		
		IllegalAccessException exception = assertThrows(IllegalAccessException.class, ()->{
			handler.performAction(mockRecruiter, mockRecruiterSubscription, subscription_action.END_SUBSCRIPTION, false);
		});	
		
		assertEquals("You are not authorized to carry out this action", exception.getMessage());
		
		verify(this.mockExternEventPublisher, never()).publishRecruiterNoOpenSubscriptionsEvent(any());
	}
	
	/**
	* Test exception thrown if a non admin user attempts to end the subscription 
	* when the status of the subscription is SUBSCRIPTION_INVOICE_UNPAID
	*/
	@Test
	void testEndSubscriptionNotAdminStatusSubscriptionEndedUnpaid() {
		
		when(mockRecruiterSubscription.getStatus()).thenReturn(subscription_status.SUBSCRIPTION_INVOICE_UNPAID);
		
		IllegalAccessException exception = assertThrows(IllegalAccessException.class, ()->{
			handler.performAction(mockRecruiter, mockRecruiterSubscription, subscription_action.END_SUBSCRIPTION, false);
		});	
		
		assertEquals("You are not authorized to carry out this action", exception.getMessage());
		
		verify(this.mockExternEventPublisher, never()).publishRecruiterNoOpenSubscriptionsEvent(any());
	}
	
	/**
	* Test exception thrown if user attempts to end the subscription 
	* when the status of the subscription is SUBSCRIPTION_ENDED
	*/
	@Test
	void testEndSubscriptionSubscriptionAlreadyEnded() {
		
		when(mockRecruiterSubscription.getStatus()).thenReturn(subscription_status.SUBSCRIPTION_ENDED);
		
		IllegalStateException exception = assertThrows(IllegalStateException.class, ()->{
			handler.performAction(mockRecruiter, mockRecruiterSubscription, subscription_action.END_SUBSCRIPTION, false);
		});	
		
		assertEquals("Subscription is already ended. Cant end a second time: " +mockRecruiterSubscription.getSubscriptionId(), exception.getMessage());
		
		verify(this.mockExternEventPublisher, never()).publishRecruiterNoOpenSubscriptionsEvent(any());
		
	}
	
	/**
	* Test case when Exception is ended when there is an outstanding invoice. In this case we want to end the 
	* subscription so the user returns to the free tier and can take out a new subscription but marks the 
	* current subscription as having an unpaid invoice to we can track the recruiter and block their account
	* if desired
	 * @throws IllegalAccessException 
	*/
	@Test
	void testEndSubscriptionSubscriptionActiveInvoiceSent() throws IllegalAccessException {
		
		PaidPeriodRecruiterSubscription sub = PaidPeriodRecruiterSubscription.builder().status(subscription_status.ACTIVE_INVOICE_SENT).build();
		
		handler.performAction(mockRecruiter, sub, subscription_action.END_SUBSCRIPTION, true);
		
		assertEquals(subscription_status.SUBSCRIPTION_INVOICE_UNPAID, sub.getStatus());
		
		verify(this.mockExternEventPublisher, never()).publishRecruiterNoOpenSubscriptionsEvent(any());
		
	}
	
	/**
	* Test case when Exception is ended when there is no outstanding invoice. In this case we want to end the 
	* subscription
	 * @throws IllegalAccessException 
	*/
	@Test
	void testEndSubscriptionSubscriptionActive() throws IllegalAccessException {
		
		PaidPeriodRecruiterSubscription sub = PaidPeriodRecruiterSubscription.builder().status(subscription_status.ACTIVE).build();
		
		handler.performAction(mockRecruiter, sub, subscription_action.END_SUBSCRIPTION, true);
		
		assertEquals(subscription_status.SUBSCRIPTION_ENDED, sub.getStatus());
		
		verify(this.mockExternEventPublisher).publishRecruiterNoOpenSubscriptionsEvent(any());
		
	}
	
}
