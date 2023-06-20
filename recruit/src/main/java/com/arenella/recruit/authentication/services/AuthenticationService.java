package com.arenella.recruit.authentication.services;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.springframework.security.core.userdetails.UserDetails;


/**
* Defines authentication services 
* @author K Parkings
*/
public interface AuthenticationService {
	
	/**
	* Authenticates User and returns a Cookie 
	* containing an Authentication token
	* @param userName - Username provided by the User
	* @param password - Password provided by the User
	* @return Cookie containing Auth token
	*/
	public Cookie authenticateUser(String userName, String password);

	/**
	* Logs an event for a user that has successfully authenticated with 
	* the system
	* @param userDetails - Details of the User
	* @param roles		 - Roles granted to the User
	*/
	public void logLogin(UserDetails userDetails, Set<String> roles);
	
}
