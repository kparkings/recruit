package com.arenella.recruit.authentication.services;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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
		
		this.authenticate(userName, password);
		
		final UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
		
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
		
		try {
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		}catch(DisabledException e) {
			throw new RuntimeException("USER_DISABLED", e);
		}catch(BadCredentialsException e) {
			throw new RuntimeException("INVALID_CREDENTIALS", e);
		}
		
	}

}