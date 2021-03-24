package com.arenella.recruit.authentication.services;

import java.util.ArrayList;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
* Service for 
* @author K Parkings
*/
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	/**
	* Refer to UserDetailsService for details 
	*/
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		//TODO: Setup DB for Users
		
		if ("javainuse".equals(username)) {
			return new User("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",new ArrayList<>());
			
			
		}
		 
		throw new UsernameNotFoundException("Auth failed for Arenella recruiting");
		
	}
}