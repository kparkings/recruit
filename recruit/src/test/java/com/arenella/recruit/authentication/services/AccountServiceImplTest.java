package com.arenella.recruit.authentication.services;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
class AccountServiceImplTest {
	
	@Mock
	private UserDao 			mockUserDao;
	
	@InjectMocks
	private AccountServiceImpl 	service = new AccountServiceImpl(mockUserDao);
	
	/**
	* Tests Exception is thrown if User is unknown
	* @throws Exception
	*/
	@Test
	void testreplaceRolesForUser_unknownUser() {
		
		final String userId = "kparkings";
		
		Mockito.when(this.mockUserDao.fetchUser(userId)).thenReturn(Optional.empty());
		
		Set<USER_ROLE> roles = Set.of();
		assertThrows(IllegalArgumentException.class, () -> 
			this.service.replaceRolesForUser(userId, roles)
		);
		
	}
	
	/**
	* Tests Happy path. Old roles removed and new roles added
	* @throws Exception
	*/
	@Test
	void testreplaceRolesForUser() {
		
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
		assertEquals(1, userEntity.getRoles().size());
		
	}
	
	/**
	* Test exception thrown if User can not be found
	* @throws Exception
	*/
	@Test
	void testUpdateUserPassword_unknownUser() {
		
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
	void testUpdateUserPassword() {
		
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
	void testDeleteAccount() {
		
		final String userId = "11";
	
		Mockito.when(this.mockUserDao.existsById(userId)).thenReturn(true);
		
		this.service.deleteAccount(userId);
		
		Mockito.verify(this.mockUserDao).deleteById(userId);
		
	}
	
	/**
	* Tests deletion of a User account that doesn't exist
	* @throws Exception
	*/
	@Test
	void testDeleteAccount_non_existent() {
		
		final String userId = "11";
	
		Mockito.when(this.mockUserDao.existsById(userId)).thenReturn(false);
		
		this.service.deleteAccount(userId);
		
		Mockito.verify(this.mockUserDao, Mockito.never()).deleteById(userId);
		
	}

	
	
	
	
	
	
	/**
	* Test exception thrown if User can not be found
	* @throws Exception
	*/
	@Test
	void testUpdateUsersCreditStatus_unknownUser() {
		
		final String userId 	= "kparkings";
		
		Mockito.when(this.mockUserDao.fetchUser(userId)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.service.updateUsersCreditStatus(userId, false);
		});
		
	}
	
	/**
	* Test exception thrown if User can not be found
	* @throws Exception
	*/
	@Test
	void testupdateUsersCreditStatus() {
		
		final String	userId 		= "kparkings";
		final User		user		= User.builder().username(userId).useCredits(true).build();
		
		ArgumentCaptor<User> userArgCapt = ArgumentCaptor.forClass(User.class);
		
		Mockito.when(this.mockUserDao.fetchUser(userId)).thenReturn(Optional.of(user));
		Mockito.doNothing().when(this.mockUserDao).updateUser(userArgCapt.capture());
		
		this.service.updateUsersCreditStatus(userId, false);
		
		Mockito.verify(this.mockUserDao).updateUser(user);
		
		assertFalse(userArgCapt.getValue().isUseCredits());
		
	}
	
}