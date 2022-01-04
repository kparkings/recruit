package com.arenella.recruit.recruiters.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_action;
import com.arenella.recruit.recruiters.beans.TrialPeriodSubscription;
import com.arenella.recruit.recruiters.dao.RecruiterDao;
import com.arenella.recruit.recruiters.entities.RecruiterEntity;
import com.arenella.recruit.recruiters.utils.RecruiterSubscriptionActionHandler;
import com.arenella.recruit.recruiters.utils.RecruiterSubscriptionFactory;

/**
* Servcies relating to Recruiters
* @author K Parkings
*/
@Service
public class RecruiterServiceImpl implements RecruiterService{

	@Autowired
	private RecruiterDao 					recruiterDao;
	
	@Autowired
	private RecruiterSubscriptionFactory 	recruiterSubscriptionFactory;
	
	/**
	* Refer to the RecruiterService for details
	*/
	@Override
	public void addRecruiter(Recruiter recruiter) {
		
		if (this.recruiterDao.existsById(recruiter.getUserId())) {
			throw new IllegalArgumentException("Recruiter already exists");
		}
		
		recruiter.activateAccount();
		
		this.recruiterDao.save(RecruiterEntity.convertToEntity(recruiter, Optional.empty()));
	}

	/**
	* Refer to the RecruiterService for details
	*/
	@Override
	public void updateRecruiter(Recruiter recruiter) {
		
		RecruiterEntity entity = this.recruiterDao.findById(recruiter.getUserId()).orElseThrow(() -> new IllegalArgumentException("Recruiter doesnt exists"));
		
		entity.setCompanyName(recruiter.getCompanyName());
		entity.setEmail(recruiter.getEmail());
		entity.setFirstName(recruiter.getFirstName());
		entity.setLanguage(recruiter.getLanguage());
		entity.setSurname(recruiter.getSurname());
		
		this.recruiterDao.save(entity);
		
	}

	/**
	* Refer to the RecruiterService for details
	*/
	@Override
	public Set<Recruiter> fetchRecruiters() {
		return StreamSupport.stream(recruiterDao.findAll().spliterator(), false).map(re -> RecruiterEntity.convertFromEntity(re)).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	* Refer to the RecruiterService for details
	 * @throws IllegalAccessException 
	*/
	@Override
	public Recruiter fetchRecruiter(String recruiterId) throws IllegalAccessException {
		
		boolean isAdminUser	= SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(role -> role.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
		
		if (!isAdminUser && !recruiterId.equalsIgnoreCase(SecurityContextHolder.getContext().getAuthentication().getName())) {
			throw new IllegalAccessException("A Recruiter can only view their own accout");
		}
		
		RecruiterEntity entity =  this.recruiterDao.findById(recruiterId).orElseThrow(()-> new IllegalArgumentException("Unknown Recruiter"));
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
		
		String temporaryRecruiterId = generateUsername(recruiter);
		
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
		
		recruiter.addInitialSubscription(subscription); //Change to 30 Day trial subscription
		
		this.recruiterDao.save(RecruiterEntity.convertToEntity(recruiter, Optional.empty()));
		
	}
	
	/**
	* Refer to the RecruiterService for details
	*/
	@Override
	public void performSubscriptionAction(String recruiterId, UUID subscriptionId, subscription_action action) {
		
		//1. Check recruiter exists
		Recruiter recruiter = this.recruiterDao.findRecruiterById(recruiterId).orElseThrow(() -> new IllegalArgumentException("Unable to retrieve recruiter: " + recruiterId));;
		
		//2. Check subscription exists
		RecruiterSubscription subscription = recruiter.getSubscriptions().stream().filter(sub -> sub.getSubscriptionId() == subscriptionId).findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown Subscription: " + subscriptionId.toString()));
		
		//3. Pass to Subscription to validate and handle action
		RecruiterSubscriptionActionHandler actionHandler = this.recruiterSubscriptionFactory.getActionHandlerByType(subscription.getType());
		
		actionHandler.performAction(recruiter, subscription, action);
		
		//4. Check that after action maximum 1 subscriptions is active
		if (recruiter.getSubscriptions().stream().filter(s -> s.isCurrentSubscription()).count() > 1) {
			throw new IllegalStateException("Max 1 subscription can be active at any one time. recruiter: " + recruiterId);
		}
		
	}
	
	/**
	* Generates a username for the recruiter
	* @param recruiter - Contains information about the recruiter
	* @return username for the recruiter
	*/
	private String generateUsername(Recruiter recruiter) {
		
		LocalDateTime 	startOfyear 	= LocalDateTime.of(LocalDateTime.now().getYear(), 1,1,0,0);
		long 			hours 			= ChronoUnit.HOURS.between(startOfyear, LocalDateTime.now());
		WeekFields 		weekFields 		= WeekFields.of(Locale.getDefault());
		int 			weekNumber 		= LocalDate.now().get(weekFields.weekOfYear());
		
		String userName = "";
		
		if (recruiter.getFirstName().length() > 5) {
			userName = recruiter.getFirstName().substring(0,5);
		} else {
			userName = recruiter.getFirstName();
		}
		
		userName = userName + weekNumber + recruiter.getSurname().substring(0,1).toUpperCase() + hours;
		
		return userName;
		
	}

}