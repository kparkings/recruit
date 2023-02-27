package com.arenella.recruit.emailservice.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;

/**
* Unit tests for the Contact class
* @author K Parkings
*/
public class RecipientTest {

	final String 			id				= "rec1";
	final ContactType 		contactType		= ContactType.RECRUITER;
	final String 			firstName		= "Kevin";
	final String 			email			= "kparkings@gmail.com";
	
	/**
	* Tests constructor
	*/
	@Test
	public void testConstructor() throws Exception {
		
		Contact contact = new Contact(id, contactType, firstName, email);
		
		assertEquals(id,			contact.getId());
		assertEquals(contactType,	contact.getContactType());
		assertEquals(firstName,		contact.getFirstName());
		assertEquals(email,			contact.getEmail());
	}
}
