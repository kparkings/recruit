package com.arenella.recruit.authentication.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.arenella.recruit.authentication.beans.AuthenticatedEvent;
import com.arenella.recruit.authentication.controllers.AuthenticationStatisticsController.LoginTrendStats;
import com.arenella.recruit.authentication.services.AuthenticationStatisticsService;

/**
* Unit tests for the AuthenticationStatisticsController class
* @author K Parkings
*/
@ExtendWith(MockitoExtension.class)
public class AuthenticationStatisticsControllerTest {
	
	@InjectMocks
	private AuthenticationStatisticsController controller = new AuthenticationStatisticsController();
	
	@Mock
	private AuthenticationStatisticsService mockAuthStatservice;

	/*
	* Tests ordering and inclusions
	*/
	@Test
	public void testEventsToday() throws Exception{
		Set<AuthenticatedEvent> events = Set.of(
			new AuthenticatedEvent("user1", true, false, LocalDateTime.now()),
			new AuthenticatedEvent("user2", false, true, LocalDateTime.now()),
			new AuthenticatedEvent("user3", false, true, LocalDateTime.now()),
			new AuthenticatedEvent("user4", true, false, LocalDateTime.now()),
			new AuthenticatedEvent("user4", true, false, LocalDateTime.now()),
			new AuthenticatedEvent("kevin3p284", true, false, LocalDateTime.now()),
			new AuthenticatedEvent("user6", true, false, LocalDateTime.now()),
			new AuthenticatedEvent("user7", false, true, LocalDateTime.now()),
			new AuthenticatedEvent("user7", false, true, LocalDateTime.now()),
			new AuthenticatedEvent("kparkings", true, false, LocalDateTime.now())
		);
		
		Mockito.when(this.mockAuthStatservice.fetchAuthenticatedEventsSince(Mockito.any(LocalDateTime.class))).thenReturn(events);
		
		ResponseEntity<LoginTrendStats> response = controller.fetchLoginTrendStats();
		
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertFalse(response.getBody().getEventsToday().stream().filter(e -> e.getUserId().equals("kparkings")).findAny().isPresent());
		Assertions.assertFalse(response.getBody().getEventsToday().stream().filter(e -> e.getUserId().equals("kevin3p284")).findAny().isPresent());
		
		Assertions.assertEquals(8, response.getBody().getEventsToday().size());
		
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsToday().toArray()[0]).isRecruiter());
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsToday().toArray()[1]).isRecruiter());
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsToday().toArray()[2]).isRecruiter());
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsToday().toArray()[3]).isRecruiter());
		
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsToday().toArray()[4]).isCandidate());
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsToday().toArray()[5]).isCandidate());
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsToday().toArray()[6]).isCandidate());
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsToday().toArray()[7]).isCandidate());
		
	}

	/*
	* Tests ordering and inclusions
	*/
	@Test
	public void testEventsWeek() throws Exception{
		Set<AuthenticatedEvent> events = Set.of(
			new AuthenticatedEvent("user1", true, false, LocalDateTime.now()),
			new AuthenticatedEvent("user2", false, true, LocalDateTime.now()),
			new AuthenticatedEvent("user3", false, true, LocalDateTime.now()),
			new AuthenticatedEvent("user4", true, false, LocalDateTime.now()),
			new AuthenticatedEvent("user4", true, false, LocalDateTime.now()),
			new AuthenticatedEvent("kevin3p284", true, false, LocalDateTime.now()),
			new AuthenticatedEvent("user6", true, false, LocalDateTime.now()),
			new AuthenticatedEvent("user7", false, true, LocalDateTime.now()),
			new AuthenticatedEvent("user7", false, true, LocalDateTime.now()),
			new AuthenticatedEvent("kparkings", true, false, LocalDateTime.now())
		);
		
		Mockito.when(this.mockAuthStatservice.fetchAuthenticatedEventsSince(Mockito.any(LocalDateTime.class))).thenReturn(events);
		
		ResponseEntity<LoginTrendStats> response = controller.fetchLoginTrendStats();
		
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertFalse(response.getBody().getEventsWeek().stream().filter(e -> e.getUserId().equals("kparkings")).findAny().isPresent());
		Assertions.assertFalse(response.getBody().getEventsWeek().stream().filter(e -> e.getUserId().equals("kevin3p284")).findAny().isPresent());
		
		Assertions.assertEquals(8, response.getBody().getEventsToday().size());
		
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsWeek().toArray()[0]).isRecruiter());
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsWeek().toArray()[1]).isRecruiter());
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsWeek().toArray()[2]).isRecruiter());
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsWeek().toArray()[3]).isRecruiter());
		
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsWeek().toArray()[4]).isCandidate());
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsWeek().toArray()[5]).isCandidate());
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsWeek().toArray()[6]).isCandidate());
		assertTrue(((AuthenticatedEvent)response.getBody().getEventsWeek().toArray()[7]).isCandidate());
		
	}
}
