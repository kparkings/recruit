package com.arenella.recruit.authentication.services;
import javax.servlet.http.Cookie;

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
	
}
