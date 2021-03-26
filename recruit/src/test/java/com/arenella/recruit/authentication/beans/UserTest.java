package com.arenella.recruit.authentication.beans;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.arenella.recruit.authentication.beans.User.USER_ROLE;

/**
* Unit tests for the User class
* @author K Parkings
*/
public class UserTest {

	/**
	* Test User is created with the values set in the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		final String 			username 	= "aUser";
		final String 			password 	= "aPassword";
		final boolean 			enabled 	= true;
		final Set<USER_ROLE> 	roles 	= new LinkedHashSet<>();
		
		roles.add(USER_ROLE.admin);
		
		User user = User.builder().username(username).password(password).enabled(enabled).roles(roles).build();
		
		assertEquals(user.getUsername(), 	username);
		assertEquals(user.getPassword(), 	password);
		assertEquals(user.isEnabled(), 		enabled);
		
		user.getRoles().stream().filter(r -> r == USER_ROLE.admin).findAny().orElseThrow(() -> new RuntimeException("Expected Role"));
		
	}
	
}
