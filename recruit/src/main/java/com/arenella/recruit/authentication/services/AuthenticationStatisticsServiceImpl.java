package com.arenella.recruit.authentication.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.arenella.recruit.authentication.beans.AuthenticatedEvent;
import com.arenella.recruit.authentication.controllers.UserLoginStats;
import com.arenella.recruit.authentication.controllers.UserLoginSummary;
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

	/**
	* Refer to AuthenticationStatisticsService for details 
	*/
	@Override
	public UserLoginStats fetchRecruiterLoginStats() {
		
		Set<UserLoginSummary> 	userLoginSummaries 	= new HashSet<>();
		Set<AuthenticatedEvent> loginEvents 		= authenticatedEventDao.fetchLoginEventsForAllRecruiter();
	
		LocalDateTime endOfToday 		= LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).minusSeconds(1);
		LocalDateTime startOfWeek 		= endOfToday.minusDays(7).withHour(0).withMinute(0).withSecond(0).minusSeconds(1);
		LocalDateTime startOf30DaysAgo 	= endOfToday.minusDays(30).withHour(0).withMinute(0).withSecond(0).minusSeconds(1);
		LocalDateTime startOf60DaysAgo 	= endOfToday.minusDays(60).withHour(0).withMinute(0).withSecond(0).minusSeconds(1);
		LocalDateTime startOf90DaysAgo 	= endOfToday.minusDays(90).withHour(0).withMinute(0).withSecond(0).minusSeconds(1);

		loginEvents
				.stream()
				.map(event -> event.getUserId())
				.distinct()
				.toList()
				.stream()
				.forEach(rec -> {
					
				List<AuthenticatedEvent> recEvents = loginEvents.stream()
						.filter(e -> e.getUserId().equals(rec))
						.toList();
					
					long loginsThisWeek 	= recEvents.stream().filter(e -> e.getLoggedInAt().isAfter(startOfWeek)).count();
					long loginsLast30Days 	= recEvents.stream().filter(e -> e.getLoggedInAt().isAfter(startOf30DaysAgo)).count();
					long loginsLast60Days 	= recEvents.stream().filter(e -> e.getLoggedInAt().isAfter(startOf60DaysAgo)).count();
					long loginsLast90Days 	= recEvents.stream().filter(e -> e.getLoggedInAt().isAfter(startOf90DaysAgo)).count();
					
					userLoginSummaries.add(new UserLoginSummary(rec, loginsThisWeek, loginsLast30Days, loginsLast60Days, loginsLast90Days));
				});
		
		return UserLoginStats.builder().loginSummaries(userLoginSummaries).build();
	
	}
	
}
