package com.arenella.recruit.authentication.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Set;

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
import com.arenella.recruit.authentication.enums.AccountType;

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
	* Test creation of new User account where the proposed username does not already 
	* exist and is used for the account
	* @throws Exception
	*/
	@Test
	public void testCreateRecruiterAccount_proposedUsername_accepted() throws Exception {
	
		final String 		proposedUsername 	= "kparkings";
		final AccountType 	accountType 		= AccountType.RECRUITER;
		
		Mockito.when(mockUserDao.existsById(proposedUsername)).thenReturn(false);
		
		User user = this.service.createAccount(proposedUsername, accountType);
		
		assertEquals(proposedUsername, user.getUsername());
		assertTrue(user.getRoles().contains(USER_ROLE.recruiter));
		assertNotNull(user.getPassword());
		
	}
	
	/**
	* Test creation of new User account where the proposed username already 
	* exist and an alternative username is assigned
	* @throws Exception
	*/
	@Test
	public void testCreateRecruiterAccount_proposedUsername_exists() throws Exception {
		
		final String 		proposedUsername 	= "kparkings";
		final String		expectedUsername	= "kparkings1";
		final AccountType 	accountType 		= AccountType.RECRUITER;
		
		Mockito.when(mockUserDao.existsById(proposedUsername)).thenReturn(true).thenReturn(false);
		Mockito.when(mockUserDao.save(Mockito.any(UserEntity.class))).thenReturn(null);
		
		User user = this.service.createAccount(proposedUsername, accountType);
		
		Mockito.verify(mockUserDao).save(Mockito.any(UserEntity.class));
		
		assertEquals(expectedUsername, user.getUsername());
		assertTrue(user.getRoles().contains(USER_ROLE.recruiter));
		assertNotNull(user.getPassword());
		
	}
	
	/**
	* Test creation of new User account where the proposed username already 
	* exist and an alternative username is assigned but is also already in use 
	* and the next available username is assigned
	* @throws Exception
	*/
	@Test
	public void testCreateRecruiterAccount_proposedUsername_exists_alternative_exists() throws Exception {
		
		final String 		proposedUsername 	= "kparkings";
		final String		expectedUsername	= "kparkings2";
		final AccountType 	accountType 		= AccountType.RECRUITER;
		
		Mockito.when(mockUserDao.existsById(Mockito.anyString())).thenReturn(true).thenReturn(true).thenReturn(false);
		Mockito.when(mockUserDao.save(Mockito.any(UserEntity.class))).thenReturn(null);
		
		User user = this.service.createAccount(proposedUsername, accountType);
		
		Mockito.verify(mockUserDao).save(Mockito.any(UserEntity.class));
		
		assertEquals(expectedUsername, user.getUsername());
		assertTrue(user.getRoles().contains(USER_ROLE.recruiter));
		assertNotNull(user.getPassword());
		
	}
	
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
	
}