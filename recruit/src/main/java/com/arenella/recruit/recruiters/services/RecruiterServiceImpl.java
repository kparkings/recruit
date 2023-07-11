package com.arenella.recruit.recruiters.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.RecruiterPasswordUpdatedEvent;
import com.arenella.recruit.adapters.events.RecruiterUpdatedEvent;
import com.arenella.recruit.emailservice.adapters.RequestSendEmailCommand;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailTopic;
import com.arenella.recruit.emailservice.beans.Email.EmailType;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;
import com.arenella.recruit.recruiters.adapters.RecruitersExternalEventPublisher;
import com.arenella.recruit.recruiters.beans.FirstGenRecruiterSubscription;
import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;
import com.arenella.recruit.recruiters.beans.SubscriptionActionFeedback;
import com.arenella.recruit.recruiters.beans.TrialPeriodSubscription;
import com.arenella.recruit.recruiters.beans.YearlyRecruiterSubscription;
import com.arenella.recruit.recruiters.dao.RecruiterDao;
import com.arenella.recruit.recruiters.entities.RecruiterEntity;
import com.arenella.recruit.recruiters.utils.PasswordUtil;
import com.arenella.recruit.recruiters.utils.RecruiterSubscriptionActionHandler;
import com.arenella.recruit.recruiters.utils.RecruiterSubscriptionFactory;

/**
* Servcies relating to Recruiters
* @author K Parkings
*/
@Service
public class RecruiterServiceImpl implements RecruiterService{

	@Autowired
	private RecruiterDao 						recruiterDao;
	
	@Autowired
	private RecruiterSubscriptionFactory 		recruiterSubscriptionFactory;
	
	@Autowired
	private RecruitersExternalEventPublisher 	externEventPublisher;
	
	/**
	* Refer to the RecruiterService for details
	* @throws IllegalAccessException 
	*/
	@Override
	public void updateRecruiter(Recruiter recruiter) throws IllegalAccessException {
		
		//TODO: [KP] This should be the Domain version and construction of entity should take place in the DAO
		RecruiterEntity 	entity 		= this.recruiterDao.findById(recruiter.getUserId()).orElseThrow(() -> new IllegalArgumentException("Recruiter doesnt exists"));
		Recruiter 			original 	= RecruiterEntity.convertFromEntity(entity);
		
		performIsAdminOrRecruiterAccessingOwnAccountCheck(recruiter.getUserId());
		
		RecruiterUpdatedEvent event = RecruiterUpdatedEvent
				.builder()
					.companyName(recruiter.getCompanyName())
					.email(recruiter.getEmail())
					.firstName(recruiter.getFirstName())
					.language(recruiter.getLanguage())
					.recruiterId(recruiter.getUserId())
					.surname(recruiter.getSurname())
				.build();
				
		this.externEventPublisher.publishRecruiterAccountUpdatedEvent(event);
		
		try {
		
			entity.setCompanyName(recruiter.getCompanyName());
			entity.setCompanyAddress(recruiter.getCompanyAddress());
			entity.setCompanyCountry(recruiter.getCompanyCountry());
			entity.setCompanyVatNumber(recruiter.getCompanyVatNumber());
			entity.setCompanyRegistrationNumber(recruiter.getCompanyRegistrationNumber());
			entity.setEmail(recruiter.getEmail());
			entity.setFirstName(recruiter.getFirstName());
			entity.setLanguage(recruiter.getLanguage());
			entity.setSurname(recruiter.getSurname());
		
			this.recruiterDao.save(entity);
			
			this.externEventPublisher.publishRecruiterAccountUpdatedEvent(event);
		
		}catch(Exception e) {
			
			/**
			* If when the transaction is closed an exception occurs the event would already have been sent.
			* In this case we send a compensating event to revert the users details back to their original state 		
			*/
			this.externEventPublisher.publishRecruiterAccountUpdatedEvent( RecruiterUpdatedEvent
					.builder()
					.companyName(original.getCompanyName())
					.email(original.getEmail())
					.firstName(original.getFirstName())
					.language(original.getLanguage())
					.recruiterId(original.getUserId())
					.surname(original.getSurname())
				.build());
			
		}
	}

	/**
	* Refer to the RecruiterService for details
	*/
	@Override
	public Set<Recruiter> fetchRecruiters() {
		return StreamSupport.stream(recruiterDao.findAll().spliterator(), false).map(RecruiterEntity::convertFromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	* Refer to the RecruiterService for details
	 * @throws IllegalAccessException 
	*/
	@Override
	public Recruiter fetchRecruiter(String recruiterId) throws IllegalAccessException {
		
		performIsAdminOrRecruiterAccessingOwnAccountCheck(recruiterId);
		
		RecruiterEntity entity =  this.recruiterDao.findByUserIdIgnoreCase(recruiterId).orElseThrow(()-> new IllegalArgumentException("Unknown Recruiter"));
		return RecruiterEntity.convertFromEntity(entity);
	}

	/**
	* Refer to the RecruiterService for details
	 * @throws IllegalAccessException 
	*/
	@Override
	public Recruiter fetchRecruiterOwnAccount()  throws IllegalAccessException {
		
		String recruiterId = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return fetchRecruiter(recruiterId);
		
	}

	/**
	* Refer to the RecruiterService for details
	* @throws IllegalAccessException 
	*/
	@Override
	public void addRecruiterAccountRequest(Recruiter recruiter) {
		
		//TODO: Do something to stop DOS attaches by sending multiple
		//      requests with same account details.
		
		String temporaryRecruiterId = PasswordUtil.generateUsername(recruiter);
		
		recruiter.setUserId(temporaryRecruiterId.toString());
		recruiter.activateAccount();
		
		int counter = 1;
		
		while (this.recruiterDao.existsById(recruiter.getUserId())) {
			String nextId = recruiter.getUserId() + counter;
			counter++;
			recruiter.setUserId(nextId);
		}
	
		TrialPeriodSubscription subscription = TrialPeriodSubscription
																.builder()
																	.created(LocalDateTime.now())
																	.recruiterId(recruiter.getUserId())
																	.status(RecruiterSubscription.subscription_status.AWAITING_ACTIVATION)
																	.subscriptionId(UUID.randomUUID())
																	.currentSubscription(true)
																.build();
		
		recruiter.addSubscription(subscription); 
		
		this.recruiterDao.save(RecruiterEntity.convertToEntity(recruiter, Optional.empty()));
		
	}
	
	/**
	* Refer to the RecruiterService for details
	* @throws IllegalAccessException 
	*/
	@Override
	public Optional<SubscriptionActionFeedback> performSubscriptionAction(String recruiterId, UUID subscriptionId, subscription_action action) throws IllegalAccessException {
		
		//0
		performIsAdminOrRecruiterAccessingOwnAccountCheck(recruiterId);
		
		//1. Check recruiter exists
		Recruiter recruiter = this.recruiterDao.findRecruiterById(recruiterId).orElseThrow(() -> new IllegalArgumentException("Unable to retrieve recruiter: " + recruiterId));
		
		//2. Check subscription exists
		RecruiterSubscription subscription = recruiter.getSubscriptions().stream().filter(sub -> sub.getSubscriptionId().equals(subscriptionId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown Subscription: " + subscriptionId.toString()));
		
		//3. Pass to Subscription to validate and handle action
		RecruiterSubscriptionActionHandler actionHandler = this.recruiterSubscriptionFactory.getActionHandlerByType(subscription.getType());
		
		Optional<SubscriptionActionFeedback> feedback = actionHandler.performAction(recruiter, subscription, action, isAdmin());
		
		//4. Check that after action maximum 1 subscriptions is active
		if (recruiter.getSubscriptions().stream().filter(s -> s.isCurrentSubscription()).count() > 1) {
			throw new IllegalStateException("Max 1 subscription can be active at any one time. recruiter: " + recruiterId);
		}
		
		RecruiterEntity entity = RecruiterEntity.convertToEntity(recruiter, this.recruiterDao.findById(recruiterId));
		
		this.recruiterDao.save(entity);
		
		return feedback;
	}
	
	/**
	* Refer to the RecruiterService for details
	* @throws IllegalAccessException 
	*/
	@Override
	public void addSubscription(String recruiterId, subscription_type type) throws IllegalAccessException{
		
		this.performIsAdminOrRecruiterAccessingOwnAccountCheck(recruiterId);
		
		Recruiter recruiter = this.recruiterDao.findRecruiterById(recruiterId).orElseThrow(() -> new IllegalArgumentException("Unable to retrieve recruiter: " + recruiterId));
		
		/**
		* Handle case YEAR_SUBSCRIPTION 
		*/
		if (type == subscription_type.YEAR_SUBSCRIPTION) {
			switchToYearSubscription(recruiter);
		}
		
	}
	
	/**
	* Refer to the RecruiterService for details
	* @throws IllegalAccessException 
	*/
	@Override
	public void resetPassword(String emailAddress) {
		
		Set<Recruiter> recruiters = this.recruiterDao.findRecruitersByEmail(emailAddress);
		
		/**
		* Can only reset if exactly one recruiter with the email address 
		*/
		if (recruiters.size() != 1) {
			throw new IllegalArgumentException("Cannot reset password");
		}
		
		Recruiter recruiter = recruiters.stream().findFirst().get();
		
		String rawPassword = PasswordUtil.generatePassword();
		String encPassword = PasswordUtil.encryptPassword(rawPassword);
		
		this.externEventPublisher.publishRecruiterPasswordUpdated(new RecruiterPasswordUpdatedEvent(recruiter.getUserId(), encPassword));
		
		RequestSendEmailCommand command = RequestSendEmailCommand
				.builder()
					.emailType(EmailType.EXTERN)
					.recipients(Set.of(new EmailRecipient<UUID>(UUID.randomUUID(),recruiter.getUserId(), ContactType.RECRUITER)))
					.sender(new Sender<>(UUID.randomUUID(), "", SenderType.SYSTEM, "kparkings@gmail.com"))
					.title("Arenella-ICT - Password Reset")
					.topic(EmailTopic.PASSWORD_RESET)
					.model(Map.of("userId", recruiter.getUserId(), "firstname",recruiter.getFirstName(),"password",rawPassword))
					.persistable(false)
				.build();
		
		this.externEventPublisher.publishSendEmailCommand(command);
		
	}
	
	/**
	* Attemts to switch the Recruiters subscription to 
	* a YEAR_SUBSCRIPTION
	* @param recruiterId
	* @param type
	*/
	private void switchToYearSubscription(Recruiter recruiter) {
		
		/**
		* Ensure that at max only non ended YEAR_SUBSCRIPTION exists per recruiter 
		*/
		if (recruiter.getSubscriptions().stream().filter(s -> s.getType() == subscription_type.YEAR_SUBSCRIPTION && s.getStatus() != subscription_status.SUBSCRIPTION_ENDED).findAny().isPresent()) {
			throw new IllegalStateException("Subscription already exists. Cannot add a second time.");
		}
		 
		/**
		* Can't have both FIRST_GEN_SUBSCRIPTION and YEAR_SUBSCRIPTION so end any currently open
		* FirstGen subscriptions
		*/
		recruiter.getSubscriptions()
								.stream()
								.filter(s -> s.getType() == subscription_type.FIRST_GEN)
								.collect(Collectors.toSet())
								.stream().forEach(s -> {
									((FirstGenRecruiterSubscription) s).endSubscription();
								});
		
		/**
		* If TRIAL_SUBSCRIPTION is not finished then make the start date of the subscription todays date plus the outstanding number 
		* of days from the Trial subscription. Otherwise use todays date
		*/
		Optional<RecruiterSubscription> trialSubscriptionOpt = recruiter.getSubscriptions().stream().filter(s -> s.getType() == subscription_type.TRIAL_PERIOD && s.getStatus() != subscription_status.SUBSCRIPTION_ENDED).findFirst();
		
		LocalDateTime activationDate 	= LocalDateTime.now();
		LocalDateTime createdDate		= activationDate;
		
		if (trialSubscriptionOpt.isPresent()) {
			TrialPeriodSubscription subscription = (TrialPeriodSubscription)trialSubscriptionOpt.get();
			subscription.endSubscription();
			subscription.setCurrentSubscription(false);
			
			long secondsRemainingFromTrialPeriod = ChronoUnit.SECONDS.between(LocalDateTime.now(),subscription.getActivatedDate().plusDays(TrialPeriodSubscription.trialPeriodInDays));
			
			activationDate = activationDate.plusSeconds(secondsRemainingFromTrialPeriod);
			
		}
		
		YearlyRecruiterSubscription yearlySubscription = YearlyRecruiterSubscription
																			.builder()
																				.activateDate(activationDate)
																				.created(createdDate)
																				.currentSubscription(true)
																				.recruiterId(recruiter.getUserId())
																				.status(subscription_status.ACTIVE_PENDING_PAYMENT)
																				.subscriptionId(UUID.randomUUID())
																			.build();
														
		recruiter.addSubscription(yearlySubscription);
		
		this.recruiterDao.save(RecruiterEntity.convertToEntity(recruiter, this.recruiterDao.findById(recruiter.getUserId())));
		
		this.externEventPublisher.publishRecruiterHasOpenSubscriptionEvent(recruiter.getUserId());
		
	}
	
	
	
	/**
	* Checks that where an endpoint can be used by both Admin and Recuiter users Admin can access anything but 
	* the Recruiters can only access their own information
	* @param recruiterId
	* @throws IllegalAccessException
	*/
	private void performIsAdminOrRecruiterAccessingOwnAccountCheck(String recruiterId) throws IllegalAccessException{
		
		if (!isAdmin() && !recruiterId.equalsIgnoreCase(SecurityContextHolder.getContext().getAuthentication().getName())) {
			throw new IllegalAccessException("A Recruiter can only view their own accout");
		}
		
	}
	
	/**
	* Whether or not the User is an Admin User
	* @return Whether or not the current User is an Admin
	*/
	private boolean isAdmin() {
		
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(role -> role.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
		
		
	}

}