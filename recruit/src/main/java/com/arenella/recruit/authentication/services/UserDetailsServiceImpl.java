package com.arenella.recruit.authentication.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.arenella.recruit.authentication.beans.User;
import com.arenella.recruit.authentication.beans.User.USER_ROLE;

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
	
		User user = this.fetchUser(username).orElseThrow(() -> new UsernameNotFoundException("Auth failed for Arenella recruiting"));
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),new ArrayList<>());
		 
	}
	
	//TODO: move logic to DAO
	public Optional<User> fetchUser(String username) {
		
		Set<User> users = new HashSet<>();
		
		Set<USER_ROLE> roles = new LinkedHashSet<>();
		roles.add(USER_ROLE.admin);
		User user = User.builder().username("javainuse").password("$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6").enabled(true).roles(roles).build();
		users.add(user);
		
		return users.stream().filter(u -> u.getUsername().toLowerCase().contentEquals(username.toLowerCase())).findAny();
		
	}
	
}