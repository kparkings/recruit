package com.arenella.recruit.recruiters.beans;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.recruiters.adapters.RecruitersExternalEventPublisher;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;
import com.arenella.recruit.recruiters.utils.RecruiterSubscriptionActionHandler;

/**
* ActionHandler for the TrialPeriodSubscription
* @author K Parkings
*/
@Component
public class TrialPeriodSubscriptionActionHandler implements RecruiterSubscriptionActionHandler{
	
	@Autowired
	private RecruitersExternalEventPublisher 	externEventPublisher;
	
	/**
	* Refer to RecruiterSubscriptionActionHandler for details 
	* @throws IllegalAccessException 
	*/
	@Override
	public Optional<SubscriptionActionFeedback> performAction(Recruiter recruiter, RecruiterSubscription subscription,  subscription_action action, Boolean isAdminUser) throws IllegalAccessException {

		TrialPeriodSubscription currentSubscription = ((TrialPeriodSubscription)subscription);
		
		/**
		* Actions available for Status AWAITING_ACTIVATION
		*/
		if (currentSubscription.getStatus() == subscription_status.AWAITING_ACTIVATION ) {
			
			switch (action) {
				case ACTIVATE_SUBSCRIPTION:{
					
					if (!isAdminUser) {
						throw new IllegalAccessException("You are not authorized to carry out this action");
					}
					
					currentSubscription.activateSubscription();
					
					
					String password = this.generatePassword(recruiter.getUserId());
					String encryptedPassword = this.encryptPassword(password);
					
					externEventPublisher
						.publishRecruiterAccountCreatedEvent(RecruiterCreatedEvent
																				.builder()
																				.recruiterId(recruiter.getUserId())
																				.encryptedPassword(encryptedPassword)
																				.build());

					return Optional.of(new AccountActivatedFeedback(recruiter.getUserId(), password));
					
				}
				case REJECT_SUBSCRIPTION:{
					
					if (!isAdminUser) {
						throw new IllegalAccessException("You are not authorized to carry out this action");
					}
					
					if (subscription.getStatus() == subscription_status.AWAITING_ACTIVATION) {
						currentSubscription.endSubscription();
						currentSubscription.setCurrentSubscription(false);
					}
					
					return Optional.empty();
				}
				default:{}
			}
			
			throw new IllegalArgumentException("Unable to perform selected action " + action + " on subscription : " + subscription.getSubscriptionId());
			
		} 
		
		/**
		* Actions available for Statuses ACTIVE
		*/
		if (currentSubscription.getStatus() == subscription_status.ACTIVE ) {
			
			if (action == subscription_action.END_SUBSCRIPTION) {
				
				currentSubscription.endSubscription();
				currentSubscription.setCurrentSubscription(false);
				
				this.externEventPublisher.publishRecruiterNoOpenSubscriptionsEvent(recruiter.getUserId());
				
				return Optional.empty();
			}
		
			throw new IllegalArgumentException("Unable to perform selected action " + action + " on subscription : " + subscription.getSubscriptionId());
			
		}
		
		/**
		* Invalid combination of STATUS and ACTION 
		*/
		throw new IllegalArgumentException("Unable to perform selected action " + action + " on subscription : " + subscription.getSubscriptionId());
		
	}
	
	/**
	* Generates a password for the user
	* @param username - username of the user
	* @return password for the user
	*/
	private String generatePassword(String username) {
		
		Random x = new Random();
		
		final String 	code 		=	String.valueOf(x.nextInt()).substring(1,5);
		
		String passwordUnencoded = username + "!" + code;
		
		return passwordUnencoded;
		
		
	}
	
	private String encryptPassword(String passwordUnencoded) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String password = encoder.encode(passwordUnencoded);
		
		return password;
		
	}
	
	/**
	* Feedback to provide Recruiter with their login details once their 
	* account has been activated by an Admin user
	* @author K Parkings
	*/
	public static class AccountActivatedFeedback implements SubscriptionActionFeedback{
		
		private final String userId;
		private final String password;
		
		/**
		* Constructor
		* @param userId				- Username for the Recruiter
		* @param encryptedPassword	- Plain text password for the Recruiter
		*/
		public AccountActivatedFeedback(String userId, String password) {
			this.userId 	= userId;
			this.password 	= password;
		}
		
		/**
		* Returns the Recruiters userId
		* @return - Recruiters userId
		*/
		public String getUserId() {
			return this.userId;
		}
		
		/**
		* Returns the Password of the Recruite r
		* @return returns plain text password for the Recruiter
		*/
		public String getPassword() {
			return password;
		}
		
	}
	
}