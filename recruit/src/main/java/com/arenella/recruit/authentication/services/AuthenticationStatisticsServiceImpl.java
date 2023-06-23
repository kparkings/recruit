package com.arenella.recruit.authentication.services;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.authentication.beans.AuthenticatedEvent;
import com.arenella.recruit.authentication.dao.AuthenticatedEventDao;

/**
* Services related to Authentication statistics
* @author K Parkings
*/
@Service
public class AuthenticationStatisticsServiceImpl implements AuthenticationStatisticsService {

	@Autowired
	private AuthenticatedEventDao authenticatedEventDao;

	/**
	* Refer to AuthenticationStatisticsService for details 
	*/
	@Override
	public Set<AuthenticatedEvent> fetchAuthenticatedEventsSince(LocalDateTime since) {
		
		if (!isAdmin()) {
			return this.authenticatedEventDao.fetchEventsSince(since).stream().filter(e -> e.getUserId().equals(this.getUserId())).collect(Collectors.toCollection(LinkedHashSet::new));
		}
		
		return this.authenticatedEventDao.fetchEventsSince(since);
	}
	
	/**
	* Whether or not the User is an Admin User
	* @return Whether or not the current User is an Admin
	*/
	private boolean isAdmin() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
	}
	
	/**
	* Returns the unique user id of the Authenticated use
	* @return id of user
	*/
	private String getUserId() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
}
