package com.arenella.recruit.authentication.controllers;

import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
	
	@Autowired
	private UserDetailsService			userDetailsService;
	
	/**
	* EndPoint validated users login credentials and returns a JWT token wrapped 
	* in a Cookie which can be used to access other endpoints
	* @param authenticationRequest 	- Contains login details
	* @param response				- Cookie containing JWT token
	* @throws Exception
	*/
	@PostMapping(path="authenticate", consumes="application/json", produces="application/json")
	public Set<String> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, HttpServletResponse response) throws Exception{
		
		Cookie 			tokenCookie 	= authenticationService.authenticateUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		UserDetails 	userDetails 	= userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		Set<String> 	roles 			= userDetails.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
		
		response.addCookie(tokenCookie);
		response.setStatus(HttpStatus.OK.value());
		
		return roles;
		
	}
	
}