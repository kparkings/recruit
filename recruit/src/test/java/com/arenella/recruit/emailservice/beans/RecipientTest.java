package com.arenella.recruit.emailservice.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Recipient.RECIPIENT_TYPE;

/**
* Unit tests for the Recipient class
* @author K Parkings
*/
public class RecipientTest {

	final String 			id				= "rec1";
	final RECIPIENT_TYPE 	recipientType	= RECIPIENT_TYPE.RECRUITER;
	final String 			firstName		= "Kevin";
	final String 			email			= "kparkings@gmail.com";
	
	/**
	* Tests constructor
	*/
	@Test
	public void testConstructor() throws Exception {
		
		Recipient recipient = new Recipient(id, recipientType, firstName, email);
		
		assertEquals(id,			recipient.getId());
		assertEquals(recipientType,	recipient.getRecipientType());
		assertEquals(firstName,		recipient.getFirstName());
		assertEquals(email,			recipient.getEmail());
	}
}
