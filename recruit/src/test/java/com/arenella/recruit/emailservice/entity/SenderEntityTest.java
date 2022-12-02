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
public class SenderEntityTest {

	private static final String emailAddress 	= "kparkings@gmail.com";
	private static final String id				= "anId";
	private static final UUID	emailId			= UUID.randomUUID();
	private static final SenderType type		= SenderType.SYSTEM;
	
	/**
	* Test construction via a Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception {
		
		SenderEntity entity = 
				SenderEntity
					.builder()
						.emailAddress(emailAddress)
						.emailId(emailId)
						.id(id)
						.senderType(type)
					.build();
		
		assertEquals(emailAddress, 	entity.getEmailAddress());
		assertEquals(id, 			entity.getId());
		assertEquals(emailId, 		entity.getEmailId());
		assertEquals(type, 			entity.getSenderType());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception{
		
		SenderEntity entity = 
				SenderEntity
					.builder()
						.emailAddress(emailAddress)
						.emailId(emailId)
						.id(id)
						.senderType(type)
					.build();
		
		Sender<String> sender = SenderEntity.convertFromEntity(entity);
		
		assertEquals(emailAddress, 	sender.getEmail());
		assertEquals(id, 			sender.getId());
		assertEquals(type, 			sender.getSenderType());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity() throws Exception{
		
		final Sender<String> 	sender 	= new Sender<>(id, type, emailAddress); 
		final Email 			email 	= Email.builder().id(UUID.randomUUID()).build();
		
		SenderEntity entity = SenderEntity.convertToEntity(sender, email);
		
		assertEquals(emailAddress, 		entity.getEmailAddress());
		assertEquals(id, 				entity.getId());
		assertEquals(type, 				entity.getSenderType());
		assertEquals(email.getId(), 	entity.getEmailId());
	}
	
}