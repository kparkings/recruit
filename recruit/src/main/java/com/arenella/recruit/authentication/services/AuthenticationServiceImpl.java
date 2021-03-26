package com.arenella.recruit.authentication.services;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.arenella.recruit.authentication.utils.JwtTokenUtil;

/**
* Services for User authentication
* @author K Parkings
*/
@Service
public class AuthenticationServiceImpl implements AuthenticationService{

	@Autowired
	private AuthenticationManager 		authenticationManager;
	
	@Autowired
	private JwtTokenUtil 				jwtTokenUtil;
	
	@Autowired
	private UserDetailsService	 		userDetailsService;
	
	/**
	* Refer to the AuthenticationService for details
	*/
	@Override
	public Cookie authenticateUser(String userName, String password) {
		
		/**
		* Perform actual authentication. Uses the UserDetails service to load a matching user. If 
		* no matching user is found it will throw an exception 
		*/
		this.authenticate(userName, password);
		
		/**
		* Loads the details of the just authenticated User
		*/
		final UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
		
		/**
		* Creates an Auth token for a User. Wraps it in a cookie and returns it. 
		*/
		final String token = this.jwtTokenUtil.generateToken(userDetails);
		
		return this.jwtTokenUtil.wrapInCookie(token);
		
	}
	
	/**
	* Performs Authentication of the User's login details.
	* @param username		- Username provided by the User
	* @param password		- password provided by the user
	* @throws Exception
	*/
	private void authenticate(String username, String password) {
		
		this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		
	}

}