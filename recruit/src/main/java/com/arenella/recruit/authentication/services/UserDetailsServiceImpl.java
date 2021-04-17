package com.arenella.recruit.authentication.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.arenella.recruit.authentication.beans.User;
import com.arenella.recruit.authentication.dao.UserDao;

/**
* Service for UserDetails
* @author K Parkings
*/
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserDao userDao;
	
	/**
	* Refer to UserDetailsService for details 
	*/
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		User user = userDao.fetchUser(username).orElseThrow(() -> new UsernameNotFoundException("Unknown User"));
		
		//User user = User.builder().username("javainuse").password("$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6").enabled(true).roles(roles).build();
		
		List<GrantedAuthority> roles = user.getRoles()
											.stream()
											.map(r -> new SimpleGrantedAuthority("ROLE_".concat(r.toString().toUpperCase())))
											.collect(Collectors.toList());
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),roles);
		 
	}
	
}