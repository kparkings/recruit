package com.arenella.recruit.authentication.services;

import static org.junit.Assert.assertNotNull;

import javax.servlet.http.Cookie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.arenella.recruit.authentication.utils.JwtTokenUtil;

/**
* Unit tests for the AuthenticationServiceImpl class
* @author K Parkings
*/
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceImplTest {

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
	
	/**
	* Test if user not found exception is thrown
	* @throws Exception
	*/
	@Test(expected=Exception.class)
	public void testAuthenticateUser_badAuthentication() throws Exception{
		
		final String username = "aUser";
		final String password = "aPassword";
		
		Mockito.when(this.mockUserDetailsService.loadUserByUsername(Mockito.anyString())).thenThrow(new RuntimeException());
		
		authService.authenticateUser(username, password);
		
	}
	
	/**
	* Test if login is successful the a Cookie with the Users token is returned
	* @throws Exception
	*/
	@Test
	public void testAuthenticateUser_succes() throws Exception{
		
		final String username 	= "aUser";
		final String password 	= "aPassword";
		final String token		= "=Gdsfjseifj2";
		final Cookie cookie		= new Cookie("a","b");
		
		Mockito.when(this.mockUserDetailsService.loadUserByUsername(Mockito.anyString())).thenReturn(mockUserDetails);
		Mockito.when(this.mockJwtTokenUtil.generateToken(mockUserDetails)).thenReturn(token);
		Mockito.when(this.mockJwtTokenUtil.wrapInCookie(token)).thenReturn(cookie);
		
		Cookie returnedCookie = authService.authenticateUser(username, password);
		
		assertNotNull(returnedCookie);
		
		Mockito.verify(this.mockJwtTokenUtil, Mockito.times(1)).generateToken(mockUserDetails);
		Mockito.verify(this.mockJwtTokenUtil, Mockito.times(1)).wrapInCookie(token);
		
	}

}