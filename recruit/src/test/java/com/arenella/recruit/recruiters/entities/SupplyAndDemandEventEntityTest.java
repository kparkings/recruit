package com.arenella.recruit.recruiters.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent;
import com.arenella.recruit.recruiters.beans.SupplyAndDemandEvent.EventType;

/**
* Unit tests for the SupplyAndDemandEventEntity class
* @author K Parkings
*/
class SupplyAndDemandEventEntityTest {

	private static final LocalDateTime 		CREATED 		= LocalDateTime.of(2022, 12, 8, 21, 33);
	private static final UUID				ID				= UUID.randomUUID();
	private static final String				RECRUITER_ID 	= "kparkings";
	private static final EventType 			TYPE 			= EventType.OFFERED_CANDIDATE;
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		SupplyAndDemandEventEntity entity = 
				SupplyAndDemandEventEntity
				.builder()
					.created(CREATED)
					.eventId(ID)
					.recruiterId(RECRUITER_ID)
					.type(TYPE)
				.build();
		
		assertEquals(CREATED, 		entity.getCreated());
		assertEquals(ID, 			entity.getId().getEventId());
		assertEquals(RECRUITER_ID, 	entity.getId().getRecruiterId());
		assertEquals(TYPE, 			entity.getType());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test 
	void testConvertToEntity() {
		
		SupplyAndDemandEventEntity entity = 
				SupplyAndDemandEventEntity
				.builder()
					.created(CREATED)
					.eventId(ID)
					.recruiterId(RECRUITER_ID)
					.type(TYPE)
				.build();
		
		SupplyAndDemandEvent event = SupplyAndDemandEventEntity.convertFromEntity(entity);
		
		assertEquals(CREATED, 		event.getCreated());
		assertEquals(ID, 			event.getEventId());
		assertEquals(RECRUITER_ID, 	event.getRecruiterId());
		assertEquals(TYPE, 			event.getType());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* @throws Exception
	*/
	@Test 
	void testConvertFromEntity() {
		
		SupplyAndDemandEvent event = 
				SupplyAndDemandEvent
				.builder()
					.created(CREATED)
					.eventId(ID)
					.recruiterId(RECRUITER_ID)
					.type(TYPE)
				.build();
		
		SupplyAndDemandEventEntity entity = SupplyAndDemandEventEntity.convertToEntity(event);
		
		assertEquals(CREATED, 		entity.getCreated());
		assertEquals(ID, 			entity.getId().getEventId());
		assertEquals(RECRUITER_ID, 	entity.getId().getRecruiterId());
		assertEquals(TYPE, 			entity.getType());
		
	}
	
}