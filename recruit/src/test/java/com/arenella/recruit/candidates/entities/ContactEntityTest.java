package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Contact;
import com.arenella.recruit.candidates.beans.Contact.CONTACT_TYPE;

/**
* Unit tests for the ContactEntity class
* @author K Parkings
*/
class ContactEntityTest {

	private static final CONTACT_TYPE 	CONTACT_TYPE_VAL 	= CONTACT_TYPE.RECRUITER;
	private static final String 		USER_ID 			= "kparkings";
	private static final String 		EMAIL 				= "admin@arenella-ict.com";
	private static final String 		FIRSTNAME 			= "kevin";
	private static final String 		SURNAME 			= "parkings";
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		RecruiterContactEntity entity = RecruiterContactEntity
				.builder()
					.contactType(CONTACT_TYPE_VAL)
					.userId(USER_ID)
					.email(EMAIL)
					.firstname(FIRSTNAME)
					.surname(SURNAME)
				.build();
		
		assertEquals(CONTACT_TYPE_VAL, 	entity.getContactType());
		assertEquals(USER_ID, 			entity.getUserId());
		assertEquals(EMAIL, 			entity.getEmail());
		assertEquals(FIRSTNAME, 		entity.getFirstname());
		assertEquals(SURNAME, 			entity.getSurname());
	}
	
	/**
	* Tests conversion from Entity to Domain
	* @throws Exception
	*/
	@Test
	void testConvertFromEntity() {
		
		RecruiterContactEntity entity = RecruiterContactEntity
				.builder()
					.contactType(CONTACT_TYPE_VAL)
					.userId(USER_ID)
					.email(EMAIL)
					.firstname(FIRSTNAME)
					.surname(SURNAME)
				.build();
		
		assertEquals(CONTACT_TYPE_VAL, 	entity.getContactType());
		assertEquals(USER_ID, 			entity.getUserId());
		assertEquals(EMAIL, 			entity.getEmail());
		assertEquals(FIRSTNAME, 		entity.getFirstname());
		assertEquals(SURNAME, 			entity.getSurname());
		
		Contact contact = RecruiterContactEntity.convertFromEntity(entity);
		
		assertEquals(CONTACT_TYPE_VAL, 	contact.getContactType());
		assertEquals(USER_ID, 			contact.getUserId());
		assertEquals(EMAIL, 			contact.getEmail());
		assertEquals(FIRSTNAME, 		contact.getFirstname());
		assertEquals(SURNAME, 			contact.getSurname());
		
	}
	
}
