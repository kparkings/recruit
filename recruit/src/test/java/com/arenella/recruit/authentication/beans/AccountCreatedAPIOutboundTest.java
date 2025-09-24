package com.arenella.recruit.authentication.beans;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the AccountCreatedAPIOutbound class
* @author K Parkings
*/
class AccountCreatedAPIOutboundTest {

	/**
	* Tests creation via Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		final String username = "kparkings";
		final String password = "aPassword";
		
		AccountCreatedAPIOutbound accountCreated = AccountCreatedAPIOutbound
																		.builder()
																			.username(username)
																			.password(password)
																		.build();
		
		assertEquals(username, accountCreated.getUsername());
		assertEquals(password, accountCreated.getPassword());
		
	}
	
	/**
	* Tests conversion from User (domain) to AccountCreatedAPIOutbound (api outbound) 
	* @throws Exception
	*/
	@Test
	void testConverFromUser() {
		
		final String username = "kparkings";
		final String password = "aPassword";
		
		User user = User.builder().username(username).password(password).build();
		
		AccountCreatedAPIOutbound accountCreated = AccountCreatedAPIOutbound.convertFromUser(user);
		
		assertEquals(username, accountCreated.getUsername());
		assertEquals(password, accountCreated.getPassword());
		
	}
	
}