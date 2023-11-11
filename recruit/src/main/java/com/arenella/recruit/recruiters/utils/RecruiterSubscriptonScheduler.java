package com.arenella.recruit.recruiters.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;
import com.arenella.recruit.recruiters.beans.PaidPeriodRecruiterSubscription;
import com.arenella.recruit.recruiters.beans.TrialPeriodSubscription;
import com.arenella.recruit.recruiters.dao.RecruiterDao;
import com.arenella.recruit.recruiters.entities.RecruiterEntity;
import com.arenella.recruit.recruiters.services.RecruiterService;

/**
* Scheduler looks at the state of the subcriptions and where a subscription 
* requires a state change updates the state of the Subscription.
* 
* This can be for example
* 
*  - Trial has expired and the status need to change to SUBSCRIPTION_ENDED
*  - Year subscription has passed a year and status needs to change to ACTIVE_PENDING_PAYMENT
* 
* 
* @author K Parkings
*/
@Component
public class RecruiterSubscriptonScheduler {
	
	@Autowired
	private RecruiterService 				recruiterService;
	
	@Autowired
	private RecruiterSubscriptionFactory 	subscriptionFactory;
	
	@Autowired
	private RecruiterDao					recruiterDao;
	
	private final ScheduledExecutorService 	scheduler 			= Executors.newSingleThreadScheduledExecutor();
	
	/**
	* Initiates the schedulting
	* @throws Exception
	*/
	@PostConstruct
	public void runTasks() {
		
		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				
				try {
				
						recruiterService.fetchRecruiters().stream().forEach(recruiter -> 
							
							recruiter.getSubscriptions().stream().filter(a -> a.getStatus() == subscription_status.ACTIVE).collect(Collectors.toSet()).stream().forEach(subscription -> {
								
								
								switch(subscription.getType()) {
									case TRIAL_PERIOD -> {
										
										/**
										* Test if Trial has expired. If so end the subscription
										*/
										if (TrialPeriodSubscription.isTrialPeriodExpired((TrialPeriodSubscription)subscription)) {
											
											RecruiterSubscriptionActionHandler actionHandler = subscriptionFactory.getActionHandlerByType(subscription_type.TRIAL_PERIOD);
											
											try {
												actionHandler.performAction(recruiter, subscription, subscription_action.END_SUBSCRIPTION, true);
												
												recruiterDao.save(RecruiterEntity.convertToEntity(recruiter, recruiterDao.findById(recruiter.getUserId())));
												
											} catch (IllegalAccessException e) {
												throw new RuntimeException(e);
											}
										}
										
										return;
									}
									case YEAR_SUBSCRIPTION, ONE_MONTH_SUBSCRIPTION, THREE_MONTHS_SUBSCRIPTION, SIX_MONTHS_SUBSCRIPTION ->  {
										
										/**
										* If a year or year multiple has elapsed then sets the status to ACTIVE_PENDING_PAYMENT so the 
										* Recruiter can be sent a new invoice
										*/
										if (PaidPeriodRecruiterSubscription.hasPeriodElapsedSinceActivation((PaidPeriodRecruiterSubscription) subscription)) {
											
											RecruiterSubscriptionActionHandler actionHandler = subscriptionFactory.getActionHandlerByType(subscription_type.YEAR_SUBSCRIPTION);
											
											try {
												
												actionHandler.performAction(recruiter, subscription, subscription_action.END_SUBSCRIPTION, true);
												
												recruiterDao.save(RecruiterEntity.convertToEntity(recruiter, recruiterDao.findById(recruiter.getUserId())));
												
											} catch (IllegalAccessException e) {
												throw new RuntimeException(e);
											}
											
										}
										
										return;
									}
									default ->{
										//No expiry so nothing to do here
									}
									
								}
								
							})
							
						);
					
				} catch(Exception e) {
					e.printStackTrace();
					System.out.println("Issue with the scheduler: " + e.getStackTrace().toString()); 
				}
				
			}
			
		}, 0, 1, TimeUnit.MINUTES);
		
	}
	
}