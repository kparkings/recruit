package com.arenella.recruit.authentication.controllers;

import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arenella.recruit.authentication.services.AuthenticationService;

/**
* Controller contains endpoints for authenticating with the system and
* obtaining a JWT token for accessing other endpoints
* @author K Parkings
*/
@CrossOrigin(origins = {	"https://api-arenella-ict.wosah.nl/authenticate"	
		,	"https://arenella-ict.wosah.nl"
		, 	"http://arenella-ict.wosah.nl"
		,	"https://arenella-ict.wosah.nl/"
		, 	"http://arenella-ict.wosah.nl/"
		,  	"http://api-arenella-ict.wosah.nl/"
		, 	"https://api-arenella-ict.wosah.nl/"
		, 	"http://api-arenella-ict.wosah.nl"
		, 	"https://api-arenella-ict.wosah.nl"
		,	"http://api.arenella-ict.com/"
		, 	"htts://api.arenella-ict.com/"
		, 	"http://127.0.0.1:4200"
		, 	"http://127.0.0.1:8080"
		, 	"http://localhost:4200"
		, 	"http://localhost:8080"
		, 	"http://127.0.0.1:9090"
		,	"https://www.arenella-ict.com"
		, 	"https://www.arenella-ict.com:4200"
		, 	"https://www.arenella-ict.com:8080"}, allowedHeaders = "*")
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
	public Set<String> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, HttpServletResponse response) {
		
		Cookie 			tokenCookie 	= authenticationService.authenticateUser(authenticationRequest.getUsername().trim(), authenticationRequest.getPassword().trim());
		UserDetails 	userDetails 	= userDetailsService.loadUserByUsername(authenticationRequest.getUsername().trim());
		Set<String> 	roles 			= userDetails.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
		
		response.addCookie(tokenCookie);
		response.setStatus(HttpStatus.OK.value());
		
		authenticationService.logLogin(userDetails, roles);
		
		return roles;
		
	}
	
}