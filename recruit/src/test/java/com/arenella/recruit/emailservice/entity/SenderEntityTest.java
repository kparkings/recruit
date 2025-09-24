package com.arenella.recruit.emailservice.entity;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email;
import com.arenella.recruit.emailservice.beans.Email.Sender;
import com.arenella.recruit.emailservice.beans.Email.Sender.SenderType;

/**
* Unit tests for the SenderEntity class
* @author K Parkings
*/
class SenderEntityTest {

	private static final String EMAIL_ADDRESS 	= "admin@arenella-ict.com";
	private static final String ID				= "anId";
	private static final String CONTACT_ID		= "na";
	private static final UUID	EMAIL_ID		= UUID.randomUUID();
	private static final SenderType TYPE		= SenderType.SYSTEM;
	
	/**
	* Test construction via a Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		SenderEntity entity = 
				SenderEntity
					.builder()
						.emailAddress(EMAIL_ADDRESS)
						.emailId(EMAIL_ID)
						.id(ID)
						.contactType(TYPE)
						.contactId(CONTACT_ID)
					.build();
		
		assertEquals(EMAIL_ADDRESS, entity.getEmailAddress());
		assertEquals(ID, 			entity.getId());
		assertEquals(EMAIL_ID, 		entity.getEmailId());
		assertEquals(TYPE, 			entity.getContactType());
		assertEquals(CONTACT_ID, 	entity.getContactId());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	void testConvertFromEntity() {
		
		SenderEntity entity = 
				SenderEntity
					.builder()
						.emailAddress(EMAIL_ADDRESS)
						.emailId(EMAIL_ID)
						.id(ID)
						.contactType(TYPE)
						.contactId(CONTACT_ID)
					.build();
		
		Sender<String> sender = SenderEntity.convertFromEntity(entity);
		
		assertEquals(EMAIL_ADDRESS, sender.getEmail());
		assertEquals(ID, 			sender.getId());
		assertEquals(TYPE, 			sender.getContactType());
		assertEquals(CONTACT_ID, 	sender.getContactId());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	void testConvertToEntity() {
		
		final Sender<String> 	sender 	= new Sender<>(ID, CONTACT_ID, TYPE, EMAIL_ADDRESS); 
		final Email 			email 	= Email.builder().id(UUID.randomUUID()).build();
		
		SenderEntity entity = SenderEntity.convertToEntity(sender, email);
		
		assertEquals(EMAIL_ADDRESS, 	entity.getEmailAddress());
		assertEquals(ID, 				entity.getId());
		assertEquals(TYPE, 				entity.getContactType());
		assertEquals(CONTACT_ID, 		entity.getContactId());
		assertEquals(email.getId(), 	entity.getEmailId());
	}
	
}