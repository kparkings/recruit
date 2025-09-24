package com.arenella.recruit.recruiters.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent.EventType;

/**
* Unit tests for the SupplyAndDemandEvent class
* @author K Parkings
*/
class SupplyAndDemandEventTest {

	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		final LocalDateTime 	created 		= LocalDateTime.of(2022, 12, 8, 21, 33);
		final UUID				id				= UUID.randomUUID();
		final String			recruiterId 	= "kparkings";
		final EventType 		type 			= EventType.OFFERED_CANDIDATE;
		
		SupplyAndDemandEvent event = 
				SupplyAndDemandEvent
				.builder()
					.created(created)
					.eventId(id)
					.recruiterId(recruiterId)
					.type(type)
				.build();
		
		assertEquals(created, 		event.getCreated());
		assertEquals(id, 			event.getEventId());
		assertEquals(recruiterId, 	event.getRecruiterId());
		assertEquals(type, 			event.getType());
		
	}
	
}