package com.arenella.recruit.authentication.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.arenella.recruit.authentication.utils.JwtTokenUtil;

/**
* Controller contains endpoints for authenticating with the system and
* obtaining a JWT token for accessing other endpoints
* @author K Parkings
*/
@RestController
public class AuthenticationController {

	@Autowired
	private AuthenticationManager 		authenticationManager;
	
	@Autowired
	private JwtTokenUtil 				jwtTokenUtil;
	
	@Autowired
	private UserDetailsService	 		userDetailsService;
	
	/**
	* Endpoint validated users login credentials and returns a JWT token wrapped 
	* in a Cookie which can be used to access other endpoints
	* @param authenticationRequest 	- Contains login details
	* @param response				- Cookie containing JWT token
	* @throws Exception
	*/
	@PostMapping(path="authenticate", consumes="application/json", produces="application/json")
	public void createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, HttpServletResponse response) throws Exception{
		
		this.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		
		final UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		final String token = this.jwtTokenUtil.generateToken(userDetails);
		
		Cookie tokenCookie = this.jwtTokenUtil.wrapInCookie(token);
		
		response.setHeader("set-cookie", "AA=BB");
		response.setHeader("set-cookie", "Authorization=Bearer "+token);
		
		response.addCookie(tokenCookie);
		response.setStatus(HttpStatus.OK.value());
		
	}

	/**
	* Performs Authentication of the User's login details.
	* @param username		- Username provided by the User
	* @param password		- password provided by the user
	* @throws Exception
	*/
	private void authenticate(String username, String password) throws Exception{
		
		try {
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		}catch(DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		}catch(BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
}