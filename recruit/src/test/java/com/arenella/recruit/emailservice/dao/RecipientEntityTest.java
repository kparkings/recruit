package com.arenella.recruit.emailservice.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.RecipientType;
import com.arenella.recruit.emailservice.beans.Recipient;
import com.arenella.recruit.emailservice.entities.RecipientEntity;
import com.arenella.recruit.emailservice.entities.RecipientEntityPK;

/**
* Unit tests for the RecipientEntity class
* @author K Parkings
*/
public class RecipientEntityTest {

	private final RecipientType 	type 			= RecipientType.RECRUITER;
	private final String 			recipientId 	= "1234";
	private final String 			email 			= "kparkings@gmail.com";
	private final String 			firstName 		= "Kevin";
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		RecipientEntity entity = 
				RecipientEntity
					.builder()
						.email(email)
						.firstName(firstName)
						.id(new RecipientEntityPK(type, recipientId))
					.build();
		
		assertEquals(type, 			entity.getId().getRecipientType());
		assertEquals(recipientId, 	entity.getId().getRecipientId());
		assertEquals(firstName, 	entity.getFirstName());
		assertEquals(email, 		entity.getEmail());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	public void convertToEntity() throws Exception{
		
		Recipient 		recipient 	= new Recipient(recipientId, type, firstName, email);
		RecipientEntity entity 		= RecipientEntity.convertToEntity(recipient);
	
		assertEquals(type, 			entity.getId().getRecipientType());
		assertEquals(recipientId, 	entity.getId().getRecipientId());
		assertEquals(firstName, 	entity.getFirstName());
		assertEquals(email, 		entity.getEmail());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* @throws Exception
	*/
	@Test
	public void convertFromEntity() throws Exception{
		
		RecipientEntity entity = 
				RecipientEntity
					.builder()
						.email(email)
						.firstName(firstName)
						.id(new RecipientEntityPK(type, recipientId))
					.build();
		
		Recipient recipient = RecipientEntity.convertFromEntity(entity);
		
		assertEquals(type, 			recipient.getRecipientType());
		assertEquals(recipientId, 	recipient.getId());
		assertEquals(firstName, 	recipient.getFirstName());
		assertEquals(email, 		recipient.getEmail());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* @throws Exception
	*/
	@Test
	public void convertFromEntity_updateMethods() throws Exception{
		
		final String updtFirstName 	= "updtFirstName";
		final String updtEmail 		= "updtEmail";
		
		RecipientEntity entity = 
				RecipientEntity
					.builder()
						.email(email)
						.firstName(firstName)
						.id(new RecipientEntityPK(type, recipientId))
					.build();
		
		assertEquals(recipientId, 	entity.getId().getRecipientId());
		assertEquals(firstName, 	entity.getFirstName());
		assertEquals(email, 		entity.getEmail());
		
		entity.setEmail(updtEmail);
		entity.setFirstName(updtFirstName);
		
		assertEquals(recipientId, 	entity.getId().getRecipientId());
		assertEquals(updtFirstName, entity.getFirstName());
		assertEquals(updtEmail, 	entity.getEmail());
		
	}
	
}