package com.arenella.recruit.authentication.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.authentication.beans.AuthenticatedEvent;

/**
* Unit tests for the AuthenticatedEventEntity class
* @author Hp
*/
class AuthenticatedEventEntityTest {

	private static final UUID 			ID 				= UUID.randomUUID();
	private static final String 		USER_ID 		= "kparkings";
	private static final boolean 		RECRUITER 		= true;
	private static final boolean 		CANDIDATE 		= false;
	private static final LocalDateTime	LOGGED_IN_AT	= LocalDateTime.of(2023, 6, 20, 10, 11, 12);
	
	/**
	* Test construction via the Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		AuthenticatedEventEntity entity = 
				AuthenticatedEventEntity
					.builder()
						.id(ID)
						.userId(USER_ID)
						.recruiter(RECRUITER)
						.candidate(CANDIDATE)
						.loggedInAt(LOGGED_IN_AT)
					.build();
		
		assertEquals(ID, 			entity.getId());
		assertEquals(USER_ID, 		entity.getUserId());
		assertEquals(LOGGED_IN_AT, 	entity.getLoggedInAt());
		assertTrue(entity.isRecruiter());
		assertFalse(entity.isCandidate());
		
	}
	
	/**
	* Tests conversion from Domain to Entity
	* @throws Exception
	*/
	@Test
	void testConvertFromDomain() {
		
		AuthenticatedEvent event = new AuthenticatedEvent(USER_ID, RECRUITER, CANDIDATE, LOGGED_IN_AT);
		
		AuthenticatedEventEntity entity = AuthenticatedEventEntity.convertToEntity(event, ID);

		assertEquals(ID, 			entity.getId());
		assertEquals(USER_ID, 		entity.getUserId());
		assertEquals(LOGGED_IN_AT, 	entity.getLoggedInAt());
		assertTrue(entity.isRecruiter());
		assertFalse(entity.isCandidate());
		
	}

	/**
	* Tests conversion from Domain to Entity
	* @throws Exception
	*/
	@Test
	void testConvertToFromDomain() {
		
		AuthenticatedEventEntity entity = 
				AuthenticatedEventEntity
					.builder()
						.id(ID)
						.userId(USER_ID)
						.recruiter(RECRUITER)
						.candidate(CANDIDATE)
						.loggedInAt(LOGGED_IN_AT)
					.build();
		
		
		AuthenticatedEvent event = AuthenticatedEventEntity.convertFromEntity(entity);
		
		assertEquals(USER_ID, 		event.getUserId());
		assertEquals(LOGGED_IN_AT, 	event.getLoggedInAt());
		assertTrue(event.isRecruiter());
		assertFalse(event.isCandidate());
		
	}
	
}