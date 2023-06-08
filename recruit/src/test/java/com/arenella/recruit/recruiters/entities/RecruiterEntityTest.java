package com.arenella.recruit.recruiters.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.Recruiter.language;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_status;
import com.arenella.recruit.recruiters.beans.RecruiterSubscription.subscription_type;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
* Unit tests for the RecruiterEntity class
* @author K Parkings
*/
public class RecruiterEntityTest {

	private static final String 				userId				= "kparkings";
	private static final String 				firstName			= "kevin";
	private static final String 				surname				= "parkings";
	private static final String 				email				= "kparkings@gmail.com";
	private static final String					companyName			= "arenella";
	private static final boolean 				active				= false;
	private static final language 				language			= Recruiter.language.DUTCH;
	private static final LocalDate				accountCreated		= LocalDate.of(2021, 5, 1);
	
	private static final LocalDateTime 			created 			= LocalDateTime.of(2021, 12, 18, 10, 10);
	private static final LocalDateTime 			activatedDate 		= LocalDateTime.of(2021, 12, 24, 10, 10);
	private static final String					recruiterId			= "kparkings";
	private static final UUID					subscriptionId		= UUID.randomUUID();
	private static final subscription_status 	status 				= subscription_status.ACTIVE;
	private static final subscription_type		type				= subscription_type.FIRST_GEN;
	
	/**
	* Tests Builder and Getters
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		RecruiterEntity entity = RecruiterEntity
										.builder()
											.accountCreated(accountCreated)
											.active(active)
											.companyName(companyName)
											.email(email)
											.firstName(firstName)
											.language(language)
											.surname(surname)
											.userId(userId)
										.build();
		
		assertEquals(accountCreated,	entity.getAccountCreated());
		assertEquals(companyName, 		entity.getCompanyName());
		assertEquals(email, 			entity.getEmail());
		assertEquals(firstName, 		entity.getFirstName());
		assertEquals(active, 			entity.isActive());
		assertEquals(language, 			entity.getLanguage());
		assertEquals(surname, 			entity.getSurname());
		assertEquals(userId, 			entity.getUserId());
		
	}
	
	/**
	* Tests setters 
	* @throws Exception
	*/
	@Test
	public void testSetters() throws Exception {
		
		RecruiterEntity entity = RecruiterEntity
										.builder()
										.build();
		
		entity.setCompanyName(companyName);
		entity.setEmail(email);
		entity.setFirstName(firstName);
		entity.setLanguage(language);
		entity.setSurname(surname);
		entity.setUserId(userId);
		
		assertEquals(companyName, 	entity.getCompanyName());
		assertEquals(email, 		entity.getEmail());
		assertEquals(firstName, 	entity.getFirstName());
		assertEquals(language, 		entity.getLanguage());
		assertEquals(surname, 		entity.getSurname());
		assertEquals(userId, 		entity.getUserId());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation of the Recruiter
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception {
		
		RecruiterSubscriptionEntity subscriptionEntity = RecruiterSubscriptionEntity
				.builder()
					.activateDate(activatedDate)
					.created(created)
					.recruiterId(recruiterId)
					.subscriptionId(subscriptionId)
					.status(status)
					.type(type)
				.build();
		
		RecruiterEntity entity = RecruiterEntity
				.builder()
					.accountCreated(accountCreated)
					.active(active)
					.companyName(companyName)
					.email(email)
					.firstName(firstName)
					.language(language)
					.surname(surname)
					.userId(userId)
					.subscriptions(Set.of(subscriptionEntity))
				.build();
		
		Recruiter recruiter = RecruiterEntity.convertFromEntity(entity);

		assertEquals(accountCreated, 	recruiter.getAccountCreated());
		assertEquals(companyName, 		recruiter.getCompanyName());
		assertEquals(email, 			recruiter.getEmail());
		assertEquals(firstName, 		recruiter.getFirstName());
		assertEquals(active, 			recruiter.isActive());
		assertEquals(language, 			recruiter.getLanguage());
		assertEquals(surname, 			recruiter.getSurname());
		assertEquals(userId, 			recruiter.getUserId());
		
		RecruiterSubscription subscription = recruiter.getSubscriptions().stream().findFirst().get();
		
		assertEquals(activatedDate, 									subscription.getActivatedDate());
		assertEquals(created, 											subscription.getCreated());
		assertEquals(recruiterId, 										subscription.getRecruiterId());
		assertEquals(status, 											subscription.getStatus());
		assertEquals(subscriptionId, 									subscription.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.FIRST_GEN, subscription.getType());

	}

	/**
	* Tests conversion from Domain to Entity representation of the Recruiter
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity() throws Exception {
		
		Recruiter recruiter = Recruiter
								.builder()
									.accountCreated(accountCreated)
									.active(active)
									.companyName(companyName)
									.email(email)
									.firstName(firstName)
									.language(language)
									.surname(surname)
									.userId(userId)
								.build();
						
		RecruiterEntity entity = RecruiterEntity.convertToEntity(recruiter, Optional.empty());

		assertEquals(accountCreated,	entity.getAccountCreated());
		assertEquals(companyName, 		entity.getCompanyName());
		assertEquals(email, 			entity.getEmail());
		assertEquals(firstName, 		entity.getFirstName());
		assertEquals(active, 			entity.isActive());
		assertEquals(language, 			entity.getLanguage());
		assertEquals(surname, 			entity.getSurname());
		assertEquals(userId, 			entity.getUserId());
		
	}
	
}