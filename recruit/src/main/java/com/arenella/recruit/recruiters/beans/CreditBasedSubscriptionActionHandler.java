package com.arenella.recruit.recruiters.beans;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arenella.recruit.adapters.events.RecruiterCreatedEvent;
import com.arenella.recruit.adapters.events.SubscriptionAddedEvent;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.recruiters.adapters.RecruitersExternalEventPublisher;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;
import com.arenella.recruit.recruiters.utils.PasswordUtil;
import com.arenella.recruit.recruiters.utils.RecruiterSubscriptionActionHandler;

/**
* ActionHandler for the CreditBasedSubscription
* @author K Parkings
*/
@Component
public class CreditBasedSubscriptionActionHandler implements RecruiterSubscriptionActionHandler{

	@Autowired
	private RecruitersExternalEventPublisher 	externEventPublisher;
	
	/**
	* Refer to the RecruiterSubscriptionActionHandler interface for details 
	*/
	@Override
	public Optional<SubscriptionActionFeedback> performAction(Recruiter recruiter, RecruiterSubscription subscription,
			subscription_action action, Boolean isAdminUser) throws IllegalAccessException {
		// TODO Auto-generated method stub
		
		CreditBasedSubscription currentSubscription = ((CreditBasedSubscription)subscription);
		
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
					
					String password 			= PasswordUtil.generatePassword();
					String encryptedPassword 	= PasswordUtil.encryptPassword(password);
					
					externEventPublisher
						.publishRecruiterAccountCreatedEvent(RecruiterCreatedEvent
																				.builder()
																				.recruiterId(recruiter.getUserId())
																				.encryptedPassword(encryptedPassword)
																				.email(recruiter.getEmail())
																				.firstName(recruiter.getFirstName())
																				.surname(recruiter.getSurname())
																				.build());

					//TODO: [KP] Currently these are in separate services. If we split this into microservices
					//			 this will break
					int cvDownloads 		= com.arenella.recruit.curriculum.beans.RecruiterCredit.DEFAULT_CREDITS;
					int jobBoardPosts 		= com.arenella.recruit.listings.beans.RecruiterCredit.DEFAULT_CREDITS;
					int marketPlacePosts 	= com.arenella.recruit.recruiters.beans.RecruiterCredit.DEFAULT_CREDITS;
					
					RequestSendEmailCommand command = RequestSendEmailCommand
							.builder()
								.emailType(EmailType.EXTERN)
								.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(),recruiter.getUserId(), ContactType.RECRUITER)))
								.sender(new Sender<>(UUID.randomUUID(), "", SenderType.SYSTEM, "no-reply@arenella-ict.com"))
								.title("Arenella-ICT - Recruiter Account Approved")
								.topic(EmailTopic.ACCOUNT_CREATED)
								.model(Map.of(
										"firstname",		recruiter.getFirstName(),
										"userid",			recruiter.getUserId(),
										"password",			password,
										"cvDownloads", 		cvDownloads, 
										"jobBoardPosts", 	jobBoardPosts,
										"marketPlacePosts",	marketPlacePosts))
								.persistable(false)
							.build();
					
					this.externEventPublisher.publishSubscriptionAddedEvent(new SubscriptionAddedEvent(recruiter.getUserId(), subscription_type.CREDIT_BASED_SUBSCRIPTION));
					this.externEventPublisher.publishSendEmailCommand(command);

					return Optional.empty();
					
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
			
		}
		
		throw new IllegalArgumentException("Unable to perform selected action " + action + " on subscription : " + subscription.getSubscriptionId());
	}
}
