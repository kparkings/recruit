package com.arenella.recruit.emailservice.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;

/**
* Unit tests for the Contact class
* @author K Parkings
*/
class RecipientTest {

	final String 			id				= "rec1";
	final ContactType 		contactType		= ContactType.RECRUITER;
	final String 			firstName		= "Kevin";
	final String 			surname			= "Parkings";
	final String 			email			= "admin@arenella-ict.com";
	
	/**
	* Tests constructor
	*/
	@Test
	void testConstructor() {
		
		Contact contact = new Contact(id, contactType, firstName, surname, email);
		
		assertEquals(id,			contact.getId());
		assertEquals(contactType,	contact.getContactType());
		assertEquals(firstName,		contact.getFirstName());
		assertEquals(surname,		contact.getSurname());
		assertEquals(email,			contact.getEmail());
	}
}
