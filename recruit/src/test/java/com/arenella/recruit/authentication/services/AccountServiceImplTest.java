package com.arenella.recruit.authentication.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
	
}