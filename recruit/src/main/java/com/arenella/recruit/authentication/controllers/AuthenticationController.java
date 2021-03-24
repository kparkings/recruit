package com.arenella.recruit.authentication.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.authentication.services.AuthenticationService;

/**
* Controller contains endpoints for authenticating with the system and
* obtaining a JWT token for accessing other endpoints
* @author K Parkings
*/
@RestController
public class AuthenticationController {

	@Autowired
	private AuthenticationService		authenticationService;
	
	/**
	* EndPoint validated users login credentials and returns a JWT token wrapped 
	* in a Cookie which can be used to access other endpoints
	* @param authenticationRequest 	- Contains login details
	* @param response				- Cookie containing JWT token
	* @throws Exception
	*/
	@PostMapping(path="authenticate", consumes="application/json", produces="application/json")
	public void createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, HttpServletResponse response) throws Exception{
		
		Cookie tokenCookie = authenticationService.authenticateUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		
		response.addCookie(tokenCookie);
		response.setStatus(HttpStatus.OK.value());
		
	}
	
}