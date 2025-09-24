package com.arenella.recruit.authentication.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.arenella.recruit.authentication.beans.AuthenticatedEvent;
import com.arenella.recruit.authentication.beans.User;
import com.arenella.recruit.authentication.dao.AuthenticatedEventDao;
import com.arenella.recruit.authentication.dao.UserDao;
import com.arenella.recruit.authentication.utils.JwtTokenUtil;

/**
* Unit tests for the AuthenticationServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

	@InjectMocks
	private AuthenticationServiceImpl 	authService;
	
	@Mock
	private AuthenticationManager 		mockAuthenticationManager;
	
	@Mock
	private JwtTokenUtil 				mockJwtTokenUtil;
	
	@Mock
	private UserDetailsService	 		mockUserDetailsService;
	
	@Mock
	private UserDetails					mockUserDetails;
	
	@Mock
	private AuthenticatedEventDao		mockAuthenticatedEventDao;
	
	@Mock
	private UserDao						mockUserDao;
	
	/**
	* Test if user not found exception is thrown
	* @throws Exception
	*/
	@Test
	void testAuthenticateUser_badAuthentication() throws Exception{
		
		final String username = "aUser";
		final String password = "aPassword";
		
		Mockito.when(this.mockUserDetailsService.loadUserByUsername(Mockito.anyString())).thenThrow(new RuntimeException());
	
		assertThrows(Exception.class, () -> authService.authenticateUser(username, password));
		
	}
	
	/**
	* Test if login is successful the a Cookie with the Users token is returned
	* @throws Exception
	*/
	@Test
	void testAuthenticateUser_succes() throws Exception{
		
		final String username 	= "aUser";
		final String password 	= "aPassword";
		final String token		= "=Gdsfjseifj2";
		final Cookie cookie		= new Cookie("a","b");
		
		Mockito.when(this.mockUserDetailsService.loadUserByUsername(Mockito.anyString())).thenReturn(mockUserDetails);
		Mockito.when(this.mockJwtTokenUtil.generateToken(Mockito.any(), Mockito.any())).thenReturn(token);
		Mockito.when(this.mockJwtTokenUtil.wrapInCookie(token)).thenReturn(cookie);
		Mockito.when(this.mockUserDao.fetchUser(username)).thenReturn(Optional.of(User.builder().build()));
		
		Cookie returnedCookie = authService.authenticateUser(username, password);
		
		assertNotNull(returnedCookie);
		
		Mockito.verify(this.mockJwtTokenUtil, Mockito.times(1)).generateToken(Mockito.any(), Mockito.any());
		Mockito.verify(this.mockJwtTokenUtil, Mockito.times(1)).wrapInCookie(token);
		
	}
	
	/**
	* Tests event is passed to persistence layer
	* @throws Exception
	*/
	@Test
	void testLogLogin() {
		
		final Set<String> roles = Set.of("ROLE_RECRUITER");
		
		ArgumentCaptor<AuthenticatedEvent> capt = ArgumentCaptor.forClass(AuthenticatedEvent.class);
		
		Mockito.doNothing().when(this.mockAuthenticatedEventDao).persitAuthenticatedEvent(capt.capture());
		
		this.authService.logLogin(mockUserDetails, roles);
		
		Mockito.verify(this.mockAuthenticatedEventDao).persitAuthenticatedEvent(Mockito.any());
		
		AuthenticatedEvent event = capt.getValue();
		
		assertTrue(event.isRecruiter());
		assertFalse(event.isCandidate());
		assertEquals(this.mockUserDetails.getUsername(), event.getUserId());
		
	}

}