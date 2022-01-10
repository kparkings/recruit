package com.arenella.recruit.authentication.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

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
	 
	/**
	* Tests replacement of old roles with new roles
	* @throws Exception
	*/
	@Test
	public void testReplaceRoles() throws Exception {
		
		final String 			username 	= "aUser";
		final String 			password 	= "aPassword";
		final boolean 			enabled 	= true;
		final Set<USER_ROLE> 	roles 		= Set.of(USER_ROLE.admin);
		final Set<USER_ROLE> 	rolesNew 	= Set.of(USER_ROLE.recruiter);
		
		User user = User.builder().username(username).password(password).enabled(enabled).roles(roles).build();
		
		assertTrue(user.getRoles().size() == 1);
		
		assertEquals(USER_ROLE.admin, user.getRoles().stream().findFirst().get());
		
		user.replaceRoles(rolesNew);
		
		assertTrue(user.getRoles().size() == 1);
	
		assertEquals(USER_ROLE.recruiter, user.getRoles().stream().findFirst().get());
		
	}
	
}