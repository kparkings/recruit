package com.arenella.recruit.authentication.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.arenella.recruit.authentication.services.AuthenticationService;

/**
* Unit tests for the AuthenticationController
* @author K Parkings
*/
//@RunWith(MockitoJUnitRunner.class)
public class AuthenticationControllerTest {

	//@InjectMocks
	//private AuthenticationController 	authController;
	
	//@Mock
	//private AuthenticationService 		mockAuthService;
	
	//@Spy 
	//private HttpServletResponse			spyResponse;
	
	/**
	* Happy path. Tests that the endPoint returns a Cookie in the response
	* containing the Auth Token
	* @throws Exception
	*/
	//@Test
	//public void testCookieReturnedWithToken() throws Exception{
		
	//	JwtRequest authenticationRequest = new JwtRequest();
		
	//	authenticationRequest.setUsername("userName");
	//	authenticationRequest.setPassword("password");
		
	//	Cookie cookie = new Cookie("Authorization","Bearer=32323");
		
	//	Mockito.when(mockAuthService.authenticateUser(Mockito.anyString(), Mockito.anyString())).thenReturn(cookie);
		
	//	this.authController.createAuthenticationToken(authenticationRequest, spyResponse);
		
	//	Mockito.verify(spyResponse, Mockito.times(1)).addCookie(cookie);
	//	Mockito.verify(spyResponse, Mockito.times(1)).setStatus(HttpStatus.OK.value());
	//}
	
}
