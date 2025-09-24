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
class RecruiterEntityTest {

	private static final String 				USER_ID						= "kparkings";
	private static final String 				FIRST_NAME					= "kevin";
	private static final String 				SURNAME						= "parkings";
	private static final String 				EMAIL						= "admin@arenella-ict.com";
	private static final String					COMPANY_NAME				= "arenella";
	private static final String 				COMPANY_ADDRESS				= "Julianastraat 16, Noordwijk, 2202KD";
	private static final String 				COMPANY_COUNTRY				= "Nederland";
	private static final String 				COMPANY_VAT_NUMBER			= "123214";
	private static final String 				COMPANY_REGISTRATION_NUMBER = "AAFF23";
	private static final boolean 				ACTIVE						= false;
	private static final language 				LANGUAGE					= Recruiter.language.DUTCH;
	private static final LocalDate				ACCOUNT_CREATED				= LocalDate.of(2021, 5, 1);
	
	private static final LocalDateTime 			CREATED 					= LocalDateTime.of(2021, 12, 18, 10, 10);
	private static final LocalDateTime 			ACTIVATED_DATE 				= LocalDateTime.of(2021, 12, 24, 10, 10);
	private static final String					RECRUITER_ID				= "kparkings";
	private static final UUID					SUBSCRIPTION_ID				= UUID.randomUUID();
	private static final subscription_status 	STATUS 						= subscription_status.ACTIVE;
	private static final subscription_type		TYPE						= subscription_type.ONE_MONTH_SUBSCRIPTION;
	
	/**
	* Tests Builder and Getters
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		RecruiterEntity entity = RecruiterEntity
										.builder()
											.accountCreated(ACCOUNT_CREATED)
											.active(ACTIVE)
											.companyName(COMPANY_NAME)
											.companyAddress(COMPANY_ADDRESS)
											.companyCountry(COMPANY_COUNTRY)
											.companyVatNumber(COMPANY_VAT_NUMBER)
											.companyRegistrationNumber(COMPANY_REGISTRATION_NUMBER)
											.email(EMAIL)
											.firstName(FIRST_NAME)
											.language(LANGUAGE)
											.surname(SURNAME)
											.userId(USER_ID)
										.build();
		
		assertEquals(ACCOUNT_CREATED,				entity.getAccountCreated());
		assertEquals(COMPANY_NAME, 					entity.getCompanyName());
		assertEquals(COMPANY_ADDRESS, 				entity.getCompanyAddress());
		assertEquals(COMPANY_COUNTRY, 				entity.getCompanyCountry());
		assertEquals(COMPANY_VAT_NUMBER, 			entity.getCompanyVatNumber());
		assertEquals(COMPANY_REGISTRATION_NUMBER, 	entity.getCompanyRegistrationNumber());
		assertEquals(EMAIL, 						entity.getEmail());
		assertEquals(FIRST_NAME, 					entity.getFirstName());
		assertEquals(ACTIVE, 						entity.isActive());
		assertEquals(LANGUAGE, 						entity.getLanguage());
		assertEquals(SURNAME, 						entity.getSurname());
		assertEquals(USER_ID, 						entity.getUserId());
		
	}
	
	/**
	* Tests setters 
	* @throws Exception
	*/
	@Test
	void testSetters() {
		
		RecruiterEntity entity = RecruiterEntity
										.builder()
										.build();
		
		entity.setCompanyName(COMPANY_NAME);
		entity.setCompanyAddress(COMPANY_ADDRESS);
		entity.setCompanyCountry(COMPANY_COUNTRY);
		entity.setCompanyVatNumber(COMPANY_VAT_NUMBER);
		entity.setCompanyRegistrationNumber(COMPANY_REGISTRATION_NUMBER);
		entity.setEmail(EMAIL);
		entity.setFirstName(FIRST_NAME);
		entity.setLanguage(LANGUAGE);
		entity.setSurname(SURNAME);
		entity.setUserId(USER_ID);
		
		assertEquals(COMPANY_NAME, 					entity.getCompanyName());
		assertEquals(EMAIL, 						entity.getEmail());
		assertEquals(FIRST_NAME, 					entity.getFirstName());
		assertEquals(LANGUAGE, 						entity.getLanguage());
		assertEquals(SURNAME, 						entity.getSurname());
		assertEquals(USER_ID, 						entity.getUserId());
		assertEquals(COMPANY_ADDRESS, 				entity.getCompanyAddress());
		assertEquals(COMPANY_COUNTRY, 				entity.getCompanyCountry());
		assertEquals(COMPANY_VAT_NUMBER, 			entity.getCompanyVatNumber());
		assertEquals(COMPANY_REGISTRATION_NUMBER, 	entity.getCompanyRegistrationNumber());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation of the Recruiter
	* @throws Exception
	*/
	@Test
	void testConvertFromEntity() {
		
		RecruiterSubscriptionEntity subscriptionEntity = RecruiterSubscriptionEntity
				.builder()
					.activateDate(ACTIVATED_DATE)
					.created(CREATED)
					.recruiterId(RECRUITER_ID)
					.subscriptionId(SUBSCRIPTION_ID)
					.status(STATUS)
					.type(TYPE)
				.build();
		
		RecruiterEntity entity = RecruiterEntity
				.builder()
					.accountCreated(ACCOUNT_CREATED)
					.active(ACTIVE)
					.companyName(COMPANY_NAME)
					.companyAddress(COMPANY_ADDRESS)
					.companyCountry(COMPANY_COUNTRY)
					.companyVatNumber(COMPANY_VAT_NUMBER)
					.companyRegistrationNumber(COMPANY_REGISTRATION_NUMBER)
					.email(EMAIL)
					.firstName(FIRST_NAME)
					.language(LANGUAGE)
					.surname(SURNAME)
					.userId(USER_ID)
					.subscriptions(Set.of(subscriptionEntity))
				.build();
		
		Recruiter recruiter = RecruiterEntity.convertFromEntity(entity);

		assertEquals(ACCOUNT_CREATED, 				recruiter.getAccountCreated());
		assertEquals(COMPANY_NAME, 					recruiter.getCompanyName());
		assertEquals(COMPANY_ADDRESS, 				entity.getCompanyAddress());
		assertEquals(COMPANY_COUNTRY, 				entity.getCompanyCountry());
		assertEquals(COMPANY_VAT_NUMBER,			entity.getCompanyVatNumber());
		assertEquals(COMPANY_REGISTRATION_NUMBER, 	entity.getCompanyRegistrationNumber());
		assertEquals(EMAIL, 						recruiter.getEmail());
		assertEquals(FIRST_NAME, 					recruiter.getFirstName());
		assertEquals(ACTIVE, 						recruiter.isActive());
		assertEquals(LANGUAGE, 						recruiter.getLanguage());
		assertEquals(SURNAME, 						recruiter.getSurname());
		assertEquals(USER_ID, 						recruiter.getUserId());
		
		RecruiterSubscription subscription = recruiter.getSubscriptions().stream().findFirst().get();
		
		assertEquals(ACTIVATED_DATE, 													subscription.getActivatedDate());
		assertEquals(CREATED, 															subscription.getCreated());
		assertEquals(RECRUITER_ID, 														subscription.getRecruiterId());
		assertEquals(STATUS, 															subscription.getStatus());
		assertEquals(SUBSCRIPTION_ID, 													subscription.getSubscriptionId());
		assertEquals(RecruiterSubscription.subscription_type.ONE_MONTH_SUBSCRIPTION, 	subscription.getType());

	}

	/**
	* Tests conversion from Domain to Entity representation of the Recruiter
	* @throws Exception
	*/
	@Test
	void testConvertToEntity() {
		
		Recruiter recruiter = Recruiter
								.builder()
									.accountCreated(ACCOUNT_CREATED)
									.active(ACTIVE)
									.companyName(COMPANY_NAME)
									.companyAddress(COMPANY_ADDRESS)
									.companyCountry(COMPANY_COUNTRY)
									.companyVatNumber(COMPANY_VAT_NUMBER)
									.companyRegistrationNumber(COMPANY_REGISTRATION_NUMBER)
									.email(EMAIL)
									.firstName(FIRST_NAME)
									.language(LANGUAGE)
									.surname(SURNAME)
									.userId(USER_ID)
								.build();
						
		RecruiterEntity entity = RecruiterEntity.convertToEntity(recruiter, Optional.empty());

		assertEquals(ACCOUNT_CREATED,				entity.getAccountCreated());
		assertEquals(COMPANY_NAME, 					entity.getCompanyName());
		assertEquals(COMPANY_ADDRESS, 				entity.getCompanyAddress());
		assertEquals(COMPANY_COUNTRY, 				entity.getCompanyCountry());
		assertEquals(COMPANY_VAT_NUMBER, 			entity.getCompanyVatNumber());
		assertEquals(COMPANY_REGISTRATION_NUMBER, 	entity.getCompanyRegistrationNumber());
		assertEquals(EMAIL, 						entity.getEmail());
		assertEquals(FIRST_NAME, 					entity.getFirstName());
		assertEquals(ACTIVE, 						entity.isActive());
		assertEquals(LANGUAGE, 						entity.getLanguage());
		assertEquals(SURNAME, 						entity.getSurname());
		assertEquals(USER_ID, 						entity.getUserId());
		
	}
	
}