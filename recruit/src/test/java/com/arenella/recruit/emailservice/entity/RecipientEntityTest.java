package com.arenella.recruit.emailservice.entity;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.Recipient;
import com.arenella.recruit.emailservice.beans.Email.Recipient.RecipientType;

/**
* Unit tests for the RecipientEntity class
* @author K Parkings
*/
public class RecipientEntityTest {

	private static final String 		emailAddress 	= "kparkings@gmail.com";
	private static final String 		id				= "anId";
	private static final UUID			emailId			= UUID.randomUUID();
	private static final RecipientType 	type			= RecipientType.SYSTEM;
	
	/**
	* Test construction via a Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		RecipientEntity entity = 
				RecipientEntity
					.builder()
						.emailAddress(emailAddress)
						.emailId(emailId)
						.id(id)
						.recipientType(type)
					.build();
		
		assertEquals(emailAddress, 	entity.getEmailAddress());
		assertEquals(id, 			entity.getId());
		assertEquals(emailId, 		entity.getEmailId());
		assertEquals(type, 			entity.getRecipientType());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception{
		
		RecipientEntity entity = 
				RecipientEntity
					.builder()
						.emailAddress(emailAddress)
						.emailId(emailId)
						.id(id)
						.recipientType(type)
					.build();
		
		@SuppressWarnings("unchecked")
		Recipient<String> recipient = (Recipient<String>)RecipientEntity.convertFromEntity(entity);
		
		assertEquals(emailAddress, 	recipient.getEmailAddress());
		assertEquals(id, 			recipient.getId());
		assertEquals(type, 			recipient.getRecipientType());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity() throws Exception{
		
		final Recipient<String> 	recipient 	= new Recipient<>(id, type, emailAddress); 
		final Email 				email 		= Email.builder().id(UUID.randomUUID()).build();
		
		RecipientEntity entity = RecipientEntity.convertToEntity(recipient, email);
		
		assertEquals(emailAddress, 		entity.getEmailAddress());
		assertEquals(id, 				entity.getId());
		assertEquals(type, 				entity.getRecipientType());
		assertEquals(email.getId(), 	entity.getEmailId());
	}
	
}
