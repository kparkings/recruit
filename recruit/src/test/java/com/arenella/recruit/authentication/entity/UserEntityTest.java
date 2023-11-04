package com.arenella.recruit.authentication.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.authentication.beans.User;
import com.arenella.recruit.authentication.beans.User.USER_ROLE;

/**
* Unit tests for the UserEntity class 
* @author K Parkings
*/
public class UserEntityTest {

	private static final String 			USERNAME 	= "username";
	private static final String 			PASSWORD 	= "password";
	private static final boolean 			ENABLED 	= true;
	private static final boolean			USE_CREDITS	= true;
	private static final Set<USER_ROLE> 	ROLES 		= new LinkedHashSet<>();
	
	/**
	* Constructor 
	*/
	public UserEntityTest() {
		ROLES.add(USER_ROLE.admin);
	}
	
	/**
	* Tests Instantiation of UserEntity with values set in the Builder
	* @throws Exception
	*/
	@Test
	public void testUserEntityBuilder() throws Exception{
		
		UserEntity entity = UserEntity
								.builder()
									.username(USERNAME)
									.password(PASSWORD)
									.enabled(ENABLED)
									.roles(ROLES)
									.useCredits(USE_CREDITS)
									.build();
		
		assertEquals(USERNAME, 		entity.getUsername());
		assertEquals(PASSWORD, 		entity.getPassword());
		assertEquals(ENABLED, 		entity.isEnabled());
		assertEquals(USE_CREDITS, 	entity.isEnabled());
		
		assertTrue(entity.getRoles().contains(USER_ROLE.admin));
		
	}

	/**
	* Tests the conversion of Domain version of User to 
	* the Entity version 
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity() throws Exception {
		
		User user = User
				.builder()
					.username(USERNAME)
					.password(PASSWORD)
					.enabled(ENABLED)
					.roles(ROLES)
					.useCredits(USE_CREDITS)
					.build();

		UserEntity userEntity = UserEntity.convertToEntity(user);
		
		assertEquals(USERNAME, 		userEntity.getUsername());
		assertEquals(PASSWORD, 		userEntity.getPassword());
		assertEquals(ENABLED, 		userEntity.isEnabled());
		assertEquals(USE_CREDITS, 	userEntity.isEnabled());
		
		assertTrue(userEntity.getRoles().contains(USER_ROLE.admin));
		
	}
	
	/**
	* Tests the conversion of Entity version of User to 
	* the Domain version
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception {
		
		UserEntity entity = UserEntity
				.builder()
					.username(USERNAME)
					.password(PASSWORD)
					.enabled(ENABLED)
					.roles(ROLES)
					.useCredits(USE_CREDITS)
					.build();

		User user = UserEntity.convertFromEntity(entity);
		
		assertEquals(USERNAME, 		user.getUsername());
		assertEquals(PASSWORD, 		user.getPassword());
		assertEquals(ENABLED, 		user.isEnabled());
		assertEquals(USE_CREDITS, 	user.isEnabled());
		
		assertTrue(user.getRoles().contains(USER_ROLE.admin));
		
	}
	
	/**
	* Tests the Update of a the Password
	* @throws Exception
	*/
	@Test
	public void testUpdatePassword() throws Exception{
		
		final String newPassword = "newPass1";
		
		UserEntity entity = UserEntity
				.builder()
					.username(USERNAME)
					.password(PASSWORD)
					.enabled(ENABLED)
					.roles(ROLES)
					.build();
		
		assertEquals(PASSWORD, entity.getPassword());
		
		entity.setPassword(newPassword);
		
		assertEquals(newPassword, entity.getPassword());
		
	}
	
}