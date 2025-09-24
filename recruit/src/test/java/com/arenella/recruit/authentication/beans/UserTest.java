package com.arenella.recruit.authentication.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.authentication.beans.User.USER_ROLE;

/**
* Unit tests for the User class
* @author K Parkings
*/
class UserTest {

	private static final String 			USERNAME 		= "aUser";
	private static final String 			PASSWORD 		= "aPassword";
	private static final boolean 			ENABLED 		= true;
	private static final boolean 			USE_CREDITS 	= true;
	private static final Set<USER_ROLE> 	ROLES 			= new LinkedHashSet<>();
	
	/**
	* Test User is created with the values set in the Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() throws Exception{
		
		ROLES.add(USER_ROLE.admin);
		
		User user = User.builder().username(USERNAME).password(PASSWORD).enabled(ENABLED).roles(ROLES).build();
		
		assertEquals(USERNAME,	user.getUsername());
		assertEquals(PASSWORD, 	user.getPassword());
		assertEquals(ENABLED, 	user.isEnabled());
		
		user.getRoles().stream().filter(r -> r == USER_ROLE.admin).findAny().orElseThrow(() -> new RuntimeException("Expected Role"));
		
	}
	 
	/**
	* Tests replacement of old roles with new roles
	* @throws Exception
	*/
	@Test
	void testReplaceRoles() {
		
		final String 			username 	= "aUser";
		final String 			password 	= "aPassword";
		final boolean 			enabled 	= true;
		final Set<USER_ROLE> 	roles 		= Set.of(USER_ROLE.admin);
		final Set<USER_ROLE> 	rolesNew 	= Set.of(USER_ROLE.recruiter);
		
		User user = User.builder().username(username).password(password).enabled(enabled).roles(roles).build();
		
		assertEquals(1,user.getRoles().size());
		
		assertEquals(USER_ROLE.admin, user.getRoles().stream().findFirst().get());
		
		user.replaceRoles(rolesNew);
		
		assertEquals(1, user.getRoles().size());
	
		assertEquals(USER_ROLE.recruiter, user.getRoles().stream().findFirst().get());
		
	}
	
	/**
	* Tests the Update of a the Password
	* @throws Exception
	*/
	@Test
	void testUpdatePassword() {
		
		final String newPassword = "newPass1";
		
		User user = User
				.builder()
					.username(USERNAME)
					.password(PASSWORD)
					.enabled(ENABLED)
					.roles(ROLES)
					.build();
		
		assertEquals(PASSWORD, user.getPassword());
		
		user.setPassword(newPassword);
		
		assertEquals(newPassword, user.getPassword());
		
	}
	
	/**
	* Tests the useCredit of a the Password
	* @throws Exception
	*/
	@Test
	void testUseCredits() {
		
		User user = User
				.builder()
					.useCredits(USE_CREDITS)
				.build();
		
		assertTrue(user.isUseCredits());
		
		user.setUseCredits(!USE_CREDITS);
		
		assertFalse(user.isUseCredits());
		
	}
	
}