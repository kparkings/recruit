package com.arenella.recruit.emailservice.entity;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.RecipientType;

/**
* Unit tests for the RecipientEntity class
* @author K Parkings
*/
public class EmailRecipientEntityTest {

	private static final String 		emailAddress 	= "kparkings@gmail.com";
	private static final String 		id				= "anId";
	private static final UUID			emailId			= UUID.randomUUID();
	private static final String			firstName		= "Kevin";
	private static final RecipientType 	type			= RecipientType.SYSTEM;
	
	/**
	* Test construction via a Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		EmailRecipientEntity entity = 
				EmailRecipientEntity
					.builder()
						.emailAddress(emailAddress)
						.emailId(emailId)
						.id(id)
						.recipientType(type)
						.firstName(firstName)
					.build();
		
		assertEquals(emailAddress, 	entity.getEmailAddress());
		assertEquals(id, 			entity.getId());
		assertEquals(emailId, 		entity.getEmailId());
		assertEquals(type, 			entity.getRecipientType());
		assertEquals(firstName, 	entity.getFirstName());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception{
		
		EmailRecipientEntity entity = 
				EmailRecipientEntity
					.builder()
						.emailAddress(emailAddress)
						.emailId(emailId)
						.id(id)
						.recipientType(type)
						.firstName(firstName)
					.build();
		
		@SuppressWarnings("unchecked")
		EmailRecipient<String> recipient = (EmailRecipient<String>)EmailRecipientEntity.convertFromEntity(entity);
		
		assertEquals(emailAddress, 	recipient.getEmailAddress());
		assertEquals(id, 			recipient.getId());
		assertEquals(type, 			recipient.getRecipientType());
		assertEquals(firstName, 	recipient.getFirstName());
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity() throws Exception{
		
		final EmailRecipient<String> 	recipient 	= new EmailRecipient<>(id, type); 
		final Email 					email 		= Email.builder().id(UUID.randomUUID()).build();
		
		recipient.setEmail(emailAddress);
		recipient.setFirstName(firstName);
		
		EmailRecipientEntity 			entity 		= EmailRecipientEntity.convertToEntity(recipient, email);
		
		assertEquals(emailAddress, 		entity.getEmailAddress());
		assertEquals(id, 				entity.getId());
		assertEquals(type, 				entity.getRecipientType());
		assertEquals(email.getId(), 	entity.getEmailId());
		assertEquals(firstName, 		entity.getFirstName());
		
	}
	
}
