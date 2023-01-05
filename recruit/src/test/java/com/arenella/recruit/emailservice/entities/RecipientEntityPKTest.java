package com.arenella.recruit.emailservice.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.RecipientType;

/**
* Unit tests for the RecipientEntityPK class
* @author K Parkings
*/
public class RecipientEntityPKTest {

	private final RecipientType recipientType	= RecipientType.RECRUITER;
	private final String 		recipientId		= "kparkings";
	
	/**
	* Tests constructor
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception {

		RecipientEntityPK pk = new RecipientEntityPK(recipientType, recipientId);
		
		assertEquals(recipientType, pk.getRecipientType());
		assertEquals(recipientId, 	pk.getRecipientId());
		
	}
}
