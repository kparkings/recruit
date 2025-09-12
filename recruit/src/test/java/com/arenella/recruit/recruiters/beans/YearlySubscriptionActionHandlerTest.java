package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.recruiters.adapters.RecruitersExternalEventPublisher;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;
import com.arenella.recruit.recruiters.utils.RecruiterSubscriptionActionHandler;

/**
* Unit tests for the YearlySubscriptionActionHandler class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class YearlySubscriptionActionHandlerTest {
	
	@InjectMocks
	private RecruiterSubscriptionActionHandler actionHandler = new PaidPeriodSubscriptionActionHandler();
	
	@Mock
	private RecruitersExternalEventPublisher mockRecruitersExternalEventPublisher;
	
	private static final Recruiter recruiter = Recruiter.builder().userId("kparkings").build();
	
	/**
	* Tests if a non admin user attempts to activate the subscription an exception
	* is thrown
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_activateSubscription_nonAdmin() throws Exception {
		
		assertThrows(IllegalAccessException.class, () -> {
			actionHandler.performAction(recruiter, null, subscription_action.ACTIVATE_SUBSCRIPTION, false);
		});
		
	}
	
	/**
	* Tests if a non admin user attempts to disable the subscription an exception
	* is thrown
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_disableSubscription_nonAdmin() throws Exception {
		
		assertThrows(IllegalAccessException.class, () -> {
			actionHandler.performAction(recruiter, null, subscription_action.DISABLE_PENDING_PAYMENT, false);
		});
		
	}
	
	/**
	* Tests if an admin user attempts to activate but the subscription is in an invalid state for 
	* this actions
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_activateSubscription_admin_invalidState() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.ACTIVE;
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.type(subscription_type.YEAR_SUBSCRIPTION)
																			.currentSubscription(true)
																		.build();
		
		assertThrows(IllegalStateException.class, () -> {
			actionHandler.performAction(recruiter, subscription, subscription_action.ACTIVATE_SUBSCRIPTION, true);
		});
		
	}
	
	/**
	* Happy Path
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_activateSubscriptionInActivePendingPayement_admin() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.ACTIVE_PENDING_PAYMENT;
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																			.type(subscription_type.YEAR_SUBSCRIPTION)
																		.build();
		
		actionHandler.performAction(recruiter, subscription, subscription_action.ACTIVATE_SUBSCRIPTION, true);
		
		assertNotNull(subscription.getActivatedDate());
		assertEquals(subscription_status.ACTIVE, subscription.getStatus());
		
		Mockito.verify(this.mockRecruitersExternalEventPublisher).publishSubscriptionAddedEvent(Mockito.any(SubscriptionAddedEvent.class));
		
	}

	/**
	* Happy Path
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_activateSubscriptionInDisabledPendingPayement_admin() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.DISABLED_PENDING_PAYMENT;
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																			.type(subscription_type.YEAR_SUBSCRIPTION)
																		.build();
		
		actionHandler.performAction(recruiter, subscription, subscription_action.ACTIVATE_SUBSCRIPTION, true);
		
		assertNotNull(subscription.getActivatedDate());
		assertEquals(subscription_status.ACTIVE, subscription.getStatus());
		
		Mockito.verify(this.mockRecruitersExternalEventPublisher).publishSubscriptionAddedEvent(Mockito.any(SubscriptionAddedEvent.class));
		
	}
	
	/**
	* Tests if an admin user attempts to disable pending payment but the subscription is in an invalid state for 
	* this actions
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_disablePendingPayment_admin_invalidState() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.ACTIVE;
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																			.type(subscription_type.YEAR_SUBSCRIPTION)
																		.build();
		
		assertThrows(IllegalStateException.class, () -> {
			actionHandler.performAction(recruiter, subscription, subscription_action.DISABLE_PENDING_PAYMENT, true);
		});
		
	}
	
	/**
	* Happy Path
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_disableSubscriptionInActiveInvoiceSent_admin() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.ACTIVE_INVOICE_SENT;
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																			.type(subscription_type.YEAR_SUBSCRIPTION)
																		.build();
		
		actionHandler.performAction(recruiter, subscription, subscription_action.DISABLE_PENDING_PAYMENT, true);
		
		assertEquals(subscription_status.DISABLED_PENDING_PAYMENT, subscription.getStatus());
		
		Mockito.verify(this.mockRecruitersExternalEventPublisher).publishRecruiterNoOpenSubscriptionsEvent(recruiter.getUserId());
		
	}
	
	/**
	* Tests non admin users cannot end a subscription that is disabled because of an unpaid invoice
	* @throws Exception
	*/
	@Test
	public void testEndSubscription_nonAdmin_disabledPendingPayment() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.DISABLED_PENDING_PAYMENT;
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																			.type(subscription_type.YEAR_SUBSCRIPTION)
																		.build();
		
		assertThrows(IllegalAccessException.class, () -> {
			actionHandler.performAction(recruiter, subscription, subscription_action.END_SUBSCRIPTION, false);
		});
		
	}
	
	/**
	* Tests that it is not possible to end a subscriptions which is already ended
	* @throws Exception
	*/
	@Test
	public void testEndSubscription_alreadyEndedt() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.SUBSCRIPTION_ENDED;
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																			.type(subscription_type.YEAR_SUBSCRIPTION)
																		.build();
		
		assertThrows(IllegalStateException.class, () -> {
			actionHandler.performAction(recruiter, subscription, subscription_action.END_SUBSCRIPTION, false);
		});
		
	}

	/**
	* Happy Path
	* @throws Exception
	*/
	@Test
	public void testActionHAndler_endSubscription_admin() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.ACTIVE;
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																			.type(subscription_type.YEAR_SUBSCRIPTION)
																		.build();
		
		actionHandler.performAction(recruiter, subscription, subscription_action.END_SUBSCRIPTION, true);
		
		assertEquals(subscription_status.SUBSCRIPTION_ENDED, subscription.getStatus());
		assertFalse(subscription.isCurrentSubscription());
		
		Mockito.verify(this.mockRecruitersExternalEventPublisher).publishRecruiterNoOpenSubscriptionsEvent(recruiter.getUserId());
		
	}
	
	/**
	* Tests a non admin user cannot renew the Subscription. In reality this is the system via 
	* the scheduler but could also be done manually by an admin user
	* @throws Exception
	*/
	@Test
	public void testActionHandler_renewSubscription_nonAdmin() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.ACTIVE;
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																			.type(subscription_type.YEAR_SUBSCRIPTION)
																		.build();
		
		assertThrows(IllegalAccessException.class, () -> {
			actionHandler.performAction(recruiter, subscription, subscription_action.RENEW_SUBSCRIPTION, false);
		});
		
	}
	
	/**
	* Test only an active subscription can be renewed
	* @throws Exception
	*/
	@Test
	public void testActionHandler_renewSubscription_notActive() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.ACTIVE_PENDING_PAYMENT;
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.currentSubscription(true)
																			.type(subscription_type.YEAR_SUBSCRIPTION)
																		.build();
		
		assertThrows(IllegalStateException.class, () -> {
			actionHandler.performAction(recruiter, subscription, subscription_action.RENEW_SUBSCRIPTION, true);
		});
		
	}
	
	/**
	* Happy path
	* @throws Exception
	*/
	@Test
	public void testActionHandler_renewSubscription() throws Exception {
		
		final LocalDateTime 		created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
		final LocalDateTime 		activateDate		= LocalDateTime.of(2021, 12, 18, 10, 11);
		final String				recruiterId			= "kparkings";
		final UUID					subscriptionId		= UUID.randomUUID();
		final subscription_status 	status 				= subscription_status.ACTIVE;
		
		PaidPeriodRecruiterSubscription subscription = PaidPeriodRecruiterSubscription
																		.builder()
																			.created(created)
																			.recruiterId(recruiterId)
																			.subscriptionId(subscriptionId)
																			.status(status)
																			.activateDate(activateDate)
																			.currentSubscription(true)
																			.type(subscription_type.YEAR_SUBSCRIPTION)
																		.build();
		
		actionHandler.performAction(recruiter, subscription, subscription_action.RENEW_SUBSCRIPTION, true);
		
		assertEquals(subscription_status.ACTIVE_PENDING_PAYMENT, 	subscription.getStatus());
		assertEquals(activateDate.plusYears(1), 					subscription.getActivatedDate());
		
	}
	
}