package com.arenella.recruit.emailservice.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Contact;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.emailservice.entities.ContactEntity;
import com.arenella.recruit.emailservice.entities.ContactEntityPK;

/**
* Unit tests for the ContactEntity class
* @author K Parkings
*/
public class ContactEntityTest {

	private final ContactType 		type 			= ContactType.RECRUITER;
	private final String 			contactId 		= "1234";
	private final String 			email 			= "admin@arenella-ict.com";
	private final String 			firstName 		= "Kevin";
	private final String 			surname 		= "Parkings";
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		ContactEntity entity = 
				ContactEntity
					.builder()
						.email(email)
						.firstName(firstName)
						.surname(surname)
						.id(new ContactEntityPK(type, contactId))
					.build();
		
		assertEquals(type, 			entity.getId().getContactType());
		assertEquals(contactId, 	entity.getId().getContactId());
		assertEquals(firstName, 	entity.getFirstName());
		assertEquals(surname, 		entity.getSurname());
		assertEquals(email, 		entity.getEmail());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	public void convertToEntity() throws Exception{
		
		Contact 		contact 	= new Contact(contactId, type, firstName, surname, email);
		ContactEntity 	entity 		= ContactEntity.convertToEntity(contact);
	
		assertEquals(type, 			entity.getId().getContactType());
		assertEquals(contactId, 	entity.getId().getContactId());
		assertEquals(firstName, 	entity.getFirstName());
		assertEquals(surname, 		entity.getSurname());
		assertEquals(email, 		entity.getEmail());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* @throws Exception
	*/
	@Test
	public void convertFromEntity() throws Exception{
		
		ContactEntity entity = 
				ContactEntity
					.builder()
						.email(email)
						.firstName(firstName)
						.surname(surname)
						.id(new ContactEntityPK(type, contactId))
					.build();
		
		Contact contact = ContactEntity.convertFromEntity(entity);
		
		assertEquals(type, 			contact.getContactType());
		assertEquals(contactId, 	contact.getId());
		assertEquals(firstName, 	contact.getFirstName());
		assertEquals(surname, 		contact.getSurname());
		assertEquals(email, 		contact.getEmail());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* @throws Exception
	*/
	@Test
	public void convertFromEntity_updateMethods() throws Exception{
		
		final String updtFirstName 	= "updtFirstName";
		final String updtSurname 	= "updtSurname";
		final String updtEmail 		= "updtEmail";
		
		ContactEntity entity = 
				ContactEntity
					.builder()
						.email(email)
						.firstName(firstName)
						.surname(surname)
						.id(new ContactEntityPK(type, contactId))
					.build();
		
		assertEquals(contactId, 	entity.getId().getContactId());
		assertEquals(firstName, 	entity.getFirstName());
		assertEquals(surname, 		entity.getSurname());
		assertEquals(email, 		entity.getEmail());
		
		entity.setEmail(updtEmail);
		entity.setFirstName(updtFirstName);
		entity.setSurname(updtSurname);
		
		assertEquals(contactId, 	entity.getId().getContactId());
		assertEquals(updtFirstName, entity.getFirstName());
		assertEquals(updtSurname, 	entity.getSurname());
		assertEquals(updtEmail, 	entity.getEmail());
		
	}
	
}