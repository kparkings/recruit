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

	private static final String 			username 	= "username";
	private static final String 			password 	= "password";
	private static final boolean 			enabled 	= true;
	private static final Set<USER_ROLE> 	roles 		= new LinkedHashSet<>();
	
	/**
	* Constructor 
	*/
	public UserEntityTest() {
		roles.add(USER_ROLE.admin);
	}
	
	/**
	* Tests Instantiation of UserEntity with values set in the Builder
	* @throws Exception
	*/
	@Test
	public void testUserEntityBuilder() throws Exception{
		
		UserEntity entity = UserEntity
								.builder()
									.username(username)
									.password(password)
									.enabled(enabled)
									.roles(roles)
									.build();
		
		assertEquals(entity.getUsername(), username);
		assertEquals(entity.getPassword(), password);
		assertEquals(entity.isEnabled(), enabled);
		
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
					.username(username)
					.password(password)
					.enabled(enabled)
					.roles(roles)
					.build();

		UserEntity userEntity = UserEntity.convertToEntity(user);
		
		assertEquals(userEntity.getUsername(), username);
		assertEquals(userEntity.getPassword(), password);
		assertEquals(userEntity.isEnabled(), enabled);
		
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
					.username(username)
					.password(password)
					.enabled(enabled)
					.roles(roles)
					.build();

		User user = UserEntity.convertFromEntity(entity);
		
		assertEquals(user.getUsername(), username);
		assertEquals(user.getPassword(), password);
		assertEquals(user.isEnabled(), enabled);
		
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
					.username(username)
					.password(password)
					.enabled(enabled)
					.roles(roles)
					.build();
		
		assertEquals(password, entity.getPassword());
		
		entity.setPassword(newPassword);
		
		assertEquals(newPassword, entity.getPassword());
		
	}
	
}