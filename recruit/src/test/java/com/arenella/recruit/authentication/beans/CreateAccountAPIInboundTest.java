package com.arenella.recruit.authentication.beans;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.authentication.enums.AccountType;

/**
* Unit tests for the CreateAccountAPIInbound class
* @author K Parkings
*/
class CreateAccountAPIInboundTest {

	/**
	* Test creation via Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		final String 		proposedUsername 	= "kparkings";
		final AccountType 	accountType 		= AccountType.RECRUITER;
		
		CreateAccountAPIInbound createRequest = CreateAccountAPIInbound.builder().proposedUsername(proposedUsername).accountType(accountType).build();
		
		assertEquals(proposedUsername, 	createRequest.getProposedUsername());
		assertEquals(accountType, 		createRequest.getAccountType());
		
	}
	
}
