package com.arenella.recruit.emailservice.entity;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;

/**
* Unit tests for the RecipientEntity class
* @author K Parkings
*/
class EmailRecipientEntityTest {

	private static final String 		EMAIL_ADDRESS 	= "admin@arenella-ict.com";
	private static final UUID			ID 				= UUID.randomUUID();
	private static final String 		CONTACT_ID		= "anId";
	private static final UUID			EMAIL_ID			= UUID.randomUUID();
	private static final String			FIRST_NAME		= "Kevin";
	private static final ContactType 	TYPE			= ContactType.SYSTEM;
	
	/**
	* Test construction via a Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		EmailRecipientEntity entity = 
				EmailRecipientEntity
					.builder()
						.emailAddress(EMAIL_ADDRESS)
						.emailId(EMAIL_ID)
						.id(ID)
						.contactType(TYPE)
						.contactId(CONTACT_ID)
						.firstName(FIRST_NAME)
					.build();
		
		assertEquals(EMAIL_ADDRESS, entity.getEmailAddress());
		assertEquals(ID, 			entity.getId());
		assertEquals(EMAIL_ID, 		entity.getEmailId());
		assertEquals(TYPE, 			entity.getContactType());
		assertEquals(CONTACT_ID, 	entity.getContactId());
		assertEquals(FIRST_NAME, 	entity.getFirstName());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	void testConvertFromEntity() {
		
		EmailRecipientEntity entity = 
				EmailRecipientEntity
					.builder()
						.emailAddress(EMAIL_ADDRESS)
						.emailId(EMAIL_ID)
						.id(ID)
						.contactType(TYPE)
						.contactId(CONTACT_ID)
						.firstName(FIRST_NAME)
					.build();
		
		EmailRecipient<UUID> recipient = (EmailRecipient<UUID>)EmailRecipientEntity.convertFromEntity(entity);
		
		assertEquals(EMAIL_ADDRESS, recipient.getEmailAddress());
		assertEquals(ID, 			recipient.getId());
		assertEquals(TYPE, 			recipient.getContactType());
		assertEquals(CONTACT_ID, 	recipient.getContactId());
		assertEquals(FIRST_NAME, 	recipient.getFirstName());
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	void testConvertToEntity() {
		
		final EmailRecipient<UUID> 		recipient 	= new EmailRecipient<>(ID, CONTACT_ID, TYPE); 
		final Email 					email 		= Email.builder().id(UUID.randomUUID()).build();
		
		recipient.setEmail(EMAIL_ADDRESS);
		recipient.setFirstName(FIRST_NAME);
		
		EmailRecipientEntity 			entity 		= EmailRecipientEntity.convertToEntity(recipient, email);
		
		assertEquals(EMAIL_ADDRESS, 	entity.getEmailAddress());
		assertEquals(ID, 				entity.getId());
		assertEquals(TYPE, 				entity.getContactType());
		assertEquals(CONTACT_ID, 		entity.getContactId());
		assertEquals(email.getId(), 	entity.getEmailId());
		assertEquals(FIRST_NAME, 		entity.getFirstName());
		
	}
	
}