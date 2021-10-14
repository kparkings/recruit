package com.arenella.recruit.recruiters.entities;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.Recruiter.language;

/**
* Unit tests for the RecruiterEntity class
* @author K Parkings
*/
public class RecruiterEntityTest {

	final String 		userId			= "kparkings";
	final String 		firstName		= "kevin";
	final String 		surname			= "parkings";
	final String 		email			= "kparkings@gmail.com";
	final String		companyName		= "arenella";
	final boolean 		active			= false;
	final language 		language		= Recruiter.language.DUTCH;
	final LocalDate		accountCreated	= LocalDate.of(2021, 5, 1);
	
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
		
		assertEquals(entity.getAccountCreated(), 	accountCreated);
		assertEquals(entity.getCompanyName(), 		companyName);
		assertEquals(entity.getEmail(), 			email);
		assertEquals(entity.getFirstName(), 		firstName);
		assertEquals(entity.isActive(), 			active);
		assertEquals(entity.getLanguage(), 			language);
		assertEquals(entity.getSurname(), 			surname);
		assertEquals(entity.getUserId(), 			userId);
		
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
		
		assertEquals(entity.getCompanyName(), 		companyName);
		assertEquals(entity.getEmail(), 			email);
		assertEquals(entity.getFirstName(), 		firstName);
		assertEquals(entity.getLanguage(), 			language);
		assertEquals(entity.getSurname(), 			surname);
		assertEquals(entity.getUserId(), 			userId);
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation of the Recruiter
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception {
		
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
		
		Recruiter recruiter = RecruiterEntity.convertFromEntity(entity);

		assertEquals(recruiter.getAccountCreated(), 	accountCreated);
		assertEquals(recruiter.getCompanyName(), 		companyName);
		assertEquals(recruiter.getEmail(), 				email);
		assertEquals(recruiter.getFirstName(), 			firstName);
		assertEquals(recruiter.isActive(), 				active);
		assertEquals(recruiter.getLanguage(), 			language);
		assertEquals(recruiter.getSurname(), 			surname);
		assertEquals(recruiter.getUserId(), 			userId);

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
						
		RecruiterEntity entity = RecruiterEntity.convertToEntity(recruiter);

		assertEquals(entity.getAccountCreated(), 	accountCreated);
		assertEquals(entity.getCompanyName(), 		companyName);
		assertEquals(entity.getEmail(), 			email);
		assertEquals(entity.getFirstName(), 		firstName);
		assertEquals(entity.isActive(), 			active);
		assertEquals(entity.getLanguage(), 			language);
		assertEquals(entity.getSurname(), 			surname);
		assertEquals(entity.getUserId(), 			userId);
		
	}
	
}