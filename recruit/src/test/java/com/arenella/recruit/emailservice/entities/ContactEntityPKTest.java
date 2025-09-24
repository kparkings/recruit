package com.arenella.recruit.emailservice.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;


/**
* Unit tests for the RecipientEntityPK class
* @author K Parkings
*/
class ContactEntityPKTest {

	private final ContactType 	contactType		= ContactType.RECRUITER;
	private final String 		contactId		= "kparkings";
	
	/**
	* Tests constructor
	* @throws Exception
	*/
	@Test
	void testConstructor() {

		ContactEntityPK pk = new ContactEntityPK(contactType, contactId);
		
		assertEquals(contactType, 	pk.getContactType());
		assertEquals(contactId, 	pk.getContactId());
		
	}
}
