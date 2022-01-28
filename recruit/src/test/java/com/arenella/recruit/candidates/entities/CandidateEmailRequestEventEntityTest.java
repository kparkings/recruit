package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.curriculum.beans.CandidateEmailRequestEvent;

/**
* Unit tests for the CandidateEmailRequestEventEntity class
* @author K Parkings
*/
public class CandidateEmailRequestEventEntityTest {

	private static final UUID 				eventId 		= UUID.randomUUID();
	private static final LocalDateTime 		created 		= LocalDateTime.of(2022, 01, 28, 16,35,00);
	private static final String 			recruiterId 	= "kparkings";
	private static final Long 				candidateId 	= 433L;
	
	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception {
		
		CandidateEmailRequestEventEntity entity = new CandidateEmailRequestEventEntity(eventId, created, recruiterId, candidateId);
	
		assertEquals(eventId, 		entity.getEventId());
		assertEquals(created, 		entity.getCreated());
		assertEquals(recruiterId, 	entity.getRecruiterId());
		assertEquals(candidateId, 	entity.getCandidateId());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception {
	
		CandidateEmailRequestEventEntity 	entity = new CandidateEmailRequestEventEntity(eventId, created, recruiterId, candidateId);
		CandidateEmailRequestEvent 			event = CandidateEmailRequestEventEntity.convertFromEntity(entity);
		
		assertEquals(eventId, 		event.getEventId());
		assertEquals(created, 		event.getCreated());
		assertEquals(recruiterId, 	event.getRecruiterId());
		assertEquals(candidateId, 	event.getCandidateId());
		
	}
	
}