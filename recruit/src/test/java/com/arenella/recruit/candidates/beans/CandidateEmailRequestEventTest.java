package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.entities.CandidateEmailRequestEventEntity;
import com.arenella.recruit.curriculum.beans.CandidateEmailRequestEvent;

public class CandidateEmailRequestEventTest {

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
		
		CandidateEmailRequestEvent event = new CandidateEmailRequestEvent(eventId, created, recruiterId, candidateId);
	
		assertEquals(eventId, 		event.getEventId());
		assertEquals(created, 		event.getCreated());
		assertEquals(recruiterId, 	event.getRecruiterId());
		assertEquals(candidateId, 	event.getCandidateId());
		
	}
	
}
