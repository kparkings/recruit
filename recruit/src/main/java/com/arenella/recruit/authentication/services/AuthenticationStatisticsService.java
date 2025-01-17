package com.arenella.recruit.authentication.services;

import java.time.LocalDateTime;
import java.util.Set;

import com.arenella.recruit.authentication.beans.AuthenticatedEvent;
import com.arenella.recruit.authentication.controllers.UserLoginSummary;

/**
* Defines services for Authentication statistics
* @author K Parkings
*/
public interface AuthenticationStatisticsService {

	/**
	* Returns the AuthenticatedEvent since a specific period
	* @param since - Moment from when events should be retrieved
	* @return Events
	*/
	public Set<AuthenticatedEvent> fetchAuthenticatedEventsSince(LocalDateTime since);
	
	/**
	* Returns a summary of the Users login behaviour
	* @return
	*/
	public UserLoginSummary fetchUserLoginSummary(String userId);
	
}
