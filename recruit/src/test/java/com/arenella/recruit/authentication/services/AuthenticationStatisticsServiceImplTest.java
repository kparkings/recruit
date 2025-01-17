package com.arenella.recruit.authentication.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.arenella.recruit.authentication.beans.AuthenticatedEvent;
import com.arenella.recruit.authentication.controllers.UserLoginSummary;
import com.arenella.recruit.authentication.dao.AuthenticatedEventDao;

/**
* Unit tests for the AuthenticationStatisticsServiceImpl class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class AuthenticationStatisticsServiceImplTest {

	@InjectMocks 
	private AuthenticationStatisticsServiceImpl service						= new AuthenticationStatisticsServiceImpl();
	
	@Mock
	private SecurityContext 					mockSecurityContext;
	
	@Mock
	private Authentication						mockAuthentication;
	
	@Mock
	private AuthenticatedEventDao 				mockAuthenticatedEventDao;
	
	private static final String 				userId1 		= "recruiter1";
	private static final boolean 				recruiter1 		= true;
	private static final boolean 				candidate1 		= false;
	private static final LocalDateTime 			loggedInAt1 	= LocalDateTime.now();
	private static final AuthenticatedEvent 	event1 			= new AuthenticatedEvent(userId1, recruiter1, candidate1, loggedInAt1);
	private static final String 				userId2 		= "candidate1";
	private static final boolean 				recruiter2 		= false;
	private static final boolean 				candidate2 		= true;
	private static final LocalDateTime 			loggedInAt2 	= LocalDateTime.now();
	private static final AuthenticatedEvent 	event2 			= new AuthenticatedEvent(userId2, recruiter2, candidate2, loggedInAt2);
	
	/**
	* Sets up test environment 
	*/
	@BeforeEach
	public void init() throws Exception{
		SecurityContextHolder.setContext(mockSecurityContext);
	}
	
	/**
	* Tests Candidates can only see their own events
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testFetchAuthenticatedEventsSince_candidate() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_CANDIDATE"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getName()).thenReturn("candidate1");
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthenticatedEventDao.fetchEventsSince(Mockito.any())).thenReturn(Set.of(event1,event2));
		
		Set<AuthenticatedEvent> events = this.service.fetchAuthenticatedEventsSince(LocalDateTime.of(2023, 6, 20, 10, 11, 12));

		assertTrue(events.stream().anyMatch(e -> e.getUserId().equals("candidate1")));
		assertFalse(events.stream().anyMatch(e -> e.getUserId().equals("recruiter1")));
		
	}
	
	/**
	* Tests Recruiters can only see their own events
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testFetchAuthenticatedEventsSince_recruiter() throws Exception{

		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getName()).thenReturn("recruiter1");
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthenticatedEventDao.fetchEventsSince(Mockito.any())).thenReturn(Set.of(event1,event2));
		
		Set<AuthenticatedEvent> events = this.service.fetchAuthenticatedEventsSince(LocalDateTime.of(2023, 6, 20, 10, 11, 12));
		
		assertFalse(events.stream().anyMatch(e -> e.getUserId().equals("candidate1")));
		assertTrue(events.stream().anyMatch(e -> e.getUserId().equals("recruiter1")));
		
	} 
	
	/**
	* Tests Admin can see both Candidate and Recruiter events
	* @throws Exception
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testFetchAuthenticatedEventsSince_admin() throws Exception{
		
		Collection authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getAuthorities()).thenReturn(authorities);
		Mockito.when(mockAuthenticatedEventDao.fetchEventsSince(Mockito.any())).thenReturn(Set.of(event1,event2));
		
		Set<AuthenticatedEvent> events = this.service.fetchAuthenticatedEventsSince(LocalDateTime.of(2023, 6, 20, 10, 11, 12));
		
		assertTrue(events.stream().anyMatch(e -> e.getUserId().equals("candidate1")));
		assertTrue(events.stream().anyMatch(e -> e.getUserId().equals("recruiter1")));
		
	} 
	
	/**
	* Tests service returns the correct login totals based upon the
	* events used as input 
	*/
	@Test
	void testFetchUserLoginSummary() {
		
		LocalDateTime baseTime = LocalDateTime.now();
		
		LocalDateTime endOfToday 		= baseTime.withHour(23).withMinute(59).withSecond(59);
		LocalDateTime startOfWeek 		= endOfToday.minusDays(7).withHour(0).withMinute(0).withSecond(0);
		LocalDateTime startOf30DaysAgo 	= endOfToday.minusDays(30).withHour(0).withMinute(0).withSecond(0);
		LocalDateTime startOf60DaysAgo 	= endOfToday.minusDays(60).withHour(0).withMinute(0).withSecond(0);
		LocalDateTime startOf90DaysAgo 	= endOfToday.minusDays(90).withHour(0).withMinute(0).withSecond(0);
		
		Mockito.when(this.mockAuthenticatedEventDao.fetchUserLoginEvents("user1"))
			.thenReturn(Set.of(
					new AuthenticatedEvent("user1", true, false, endOfToday.minusSeconds(1)),
					new AuthenticatedEvent("user1", true, false, startOfWeek.plusDays(2)),
					new AuthenticatedEvent("user1", true, false, startOf30DaysAgo),
					new AuthenticatedEvent("user1", true, false, startOf30DaysAgo.plusDays(1)),
					new AuthenticatedEvent("user1", true, false, startOf60DaysAgo.plusHours(15)),
					new AuthenticatedEvent("user1", true, false, startOf60DaysAgo.plusDays(6)),
					new AuthenticatedEvent("user1", true, false, startOf90DaysAgo.plusSeconds(1)),
					new AuthenticatedEvent("user1", true, false, startOf90DaysAgo.plusHours(1)),
					new AuthenticatedEvent("user1", true, false, startOf90DaysAgo.plusDays(1))
				));
		
		UserLoginSummary summary = this.service.fetchUserLoginSummary("user1");
		
		assertEquals(2, summary.loginsThisWeeek());
		assertEquals(4, summary.loginsLast30Days());
		assertEquals(6, summary.loginsLast60Days());
		assertEquals(9, summary.loginsLats90Days());
		
	}
	
}
