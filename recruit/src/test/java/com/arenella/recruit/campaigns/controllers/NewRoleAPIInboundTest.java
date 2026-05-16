package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the NewRoleAPIInbound class 
*/
class NewRoleAPIInboundTest {

	private static final String 		NAME			= "ING";
	private static final String 		DESCRIPTION		= "ING Bank IT roles";
	
	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testBuilder() {
		
		NewRoleAPIInbound campaign = NewRoleAPIInbound
				.builder()
					.name(NAME)
					.description(DESCRIPTION)
				.build();
		
		assertEquals(NAME, 			campaign.getName());
		assertEquals(DESCRIPTION, 	campaign.getDescription());
		
	}
	
}