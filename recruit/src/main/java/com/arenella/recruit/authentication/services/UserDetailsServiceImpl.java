package com.arenella.recruit.authentication.services;

import java.util.ArrayList;

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
	
		if ("javainuse".equals(username)) {
			return new User("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",new ArrayList<>());
			
			
		}
		 
		throw new UsernameNotFoundException("Auth failed for Arenella recruiting");
		
	}

}
