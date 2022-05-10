package com.arenella.recruit.authentication.services;

import static org.junit.Assert.assertEquals;
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
	
}