package com.arenella.recruit.authentication.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.authentication.beans.User;
import com.arenella.recruit.authentication.beans.User.USER_ROLE;
import com.arenella.recruit.authentication.dao.UserDao;
import com.arenella.recruit.authentication.entity.UserEntity;

/**
* Unit tests for the AccountServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
	
	@Mock
	private UserDao 			mockUserDao;
	
	@InjectMocks
	private AccountServiceImpl 	service 		= new AccountServiceImpl();
	
	
	/**
	* Tests Exception is thrown if User is unknown
	* @throws Exception
	*/
	@Test
	public void testreplaceRolesForUser_unknownUser() throws Exception {
		
		final String userId = "kparkings";
		
		Mockito.when(this.mockUserDao.fetchUser(userId)).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.service.replaceRolesForUser(userId, Set.of());
		});
		
	}
	
	/**
	* Tests Happy path. Old roles removed and new roles added
	* @throws Exception
	*/
	@Test
	public void testreplaceRolesForUser() throws Exception {
		
		final String 		userId 							= "kparkings";
		final USER_ROLE 	recruiterRole 					= USER_ROLE.recruiter;
		final USER_ROLE		recruiterNoSubscriptionRole 	= USER_ROLE.recruiterNoSubscrition; 
		
		User user = User.builder().username(userId).roles(Set.of(recruiterRole)).build();
		
		Mockito.when(this.mockUserDao.fetchUser(userId)).thenReturn(Optional.of(user));
		
		ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
		
		this.service.replaceRolesForUser(userId, Set.of(recruiterNoSubscriptionRole));
		
		Mockito.verify(this.mockUserDao).save(userEntityCaptor.capture());
		
		UserEntity userEntity = userEntityCaptor.getValue();
		
		assertEquals(userId, userEntity.getUsername());
		assertEquals(recruiterNoSubscriptionRole, userEntity.getRoles().stream().findFirst().get());
		assertTrue(userEntity.getRoles().size() == 1);
		
	}
	
	/**
	* Test exception thrown if User can not be found
	* @throws Exception
	*/
	@Test
	public void testUpdateUserPassword_unknownUser() throws Exception{
		
		final String userId 	= "kparkings";
		final String password 	= "dad##@&!3";
		
		Mockito.when(this.mockUserDao.fetchUser(userId)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateUserPassword(userId, password);
		});
		
	}
	
	/**
	* Test exception thrown if User can not be found
	* @throws Exception
	*/
	@Test
	public void testUpdateUserPassword() throws Exception{
		
		final String	userId 		= "kparkings";
		final String 	password 	= "dad##@&!3";
		final User		user		= User.builder().username(userId).password("oldPassword").build();
		
		ArgumentCaptor<User> userArgCapt = ArgumentCaptor.forClass(User.class);
		
		Mockito.when(this.mockUserDao.fetchUser(userId)).thenReturn(Optional.of(user));
		Mockito.doNothing().when(this.mockUserDao).updateUser(userArgCapt.capture());
		
		this.service.updateUserPassword(userId, password);
		
		Mockito.verify(this.mockUserDao).updateUser(user);
		
		assertEquals(password, userArgCapt.getValue().getPassword());
		
	}
	
	/**
	* Tests deletion of a User account
	* @throws Exception
	*/
	@Test
	public void testDeleteAccount() throws Exception{
		
		final String userId = "11";
		
		this.service.deleteAccount(userId);
		
		Mockito.verify(this.mockUserDao).deleteById(userId);
		
	}
	
}