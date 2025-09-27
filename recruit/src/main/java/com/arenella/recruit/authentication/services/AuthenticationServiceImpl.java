package com.arenella.recruit.authentication.services;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.Cookie;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.arenella.recruit.authentication.beans.AuthenticatedEvent;
import com.arenella.recruit.authentication.beans.User;
import com.arenella.recruit.authentication.dao.AuthenticatedEventDao;
import com.arenella.recruit.authentication.dao.UserDao;
import com.arenella.recruit.authentication.utils.JwtTokenUtil;

/**
* Services for User authentication
* @author K Parkings
*/
@Service
public class AuthenticationServiceImpl implements AuthenticationService{

	private AuthenticationManager 		authenticationManager;
	private JwtTokenUtil 				jwtTokenUtil;
	private UserDetailsService	 		userDetailsService;
	private AuthenticatedEventDao		authenticatedEventDao;
	private UserDao						userDao;
	
	/**
	* Constructor
	* @param authenticationManager	- Manages authentication
	* @param jwtTokenUtil			- Provides Utilities for working with JTWs
	* @param userDetailsService		- Services relating to User details
	* @param authenticatedEventDao	- DAO for authentication
	* @param userDao				- DAO dor users
	*/
	public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
									 JwtTokenUtil jwtTokenUtil,
									 UserDetailsService userDetailsService,
									 AuthenticatedEventDao authenticatedEventDao,
									 UserDao	userDao) {
	
		this.authenticationManager 	= authenticationManager;
		this.jwtTokenUtil 			= jwtTokenUtil;
		this.userDetailsService 	= userDetailsService;
		this.authenticatedEventDao 	= authenticatedEventDao;
		this.userDao			 	= userDao;
		
	}
	
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
		final UserDetails 	userDetails = this.userDetailsService.loadUserByUsername(userName);
		final User 			user 		= this.userDao.fetchUser(userName).orElseThrow();
		
		/**
		* Creates an Auth token for a User. Wraps it in a cookie and returns it. 
		*/
		final String token = this.jwtTokenUtil.generateToken(userDetails, Map.of("useCredits", user.isUseCredits()));
		
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

	/**
	* Refer to the AuthenticationService for details
	*/
	@Override
	public void logLogin(UserDetails userDetails, Set<String> roles) {
		
		final String 	userName 		= userDetails.getUsername();
		final boolean 	isRecruiter 	= roles.stream().anyMatch(r -> r.equals("ROLE_RECRUITER"));
		final boolean 	isCandidate 	= roles.stream().anyMatch(r -> r.equals("ROLE_CANDIDATE"));
		
		this.authenticatedEventDao.persitAuthenticatedEvent(new AuthenticatedEvent(userName, isRecruiter, isCandidate, LocalDateTime.now()));
		
	}

}