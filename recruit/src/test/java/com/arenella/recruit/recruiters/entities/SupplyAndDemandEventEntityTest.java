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
public class SupplyAndDemandEventEntityTest {

	private static final LocalDateTime 		created 		= LocalDateTime.of(2022, 12, 8, 21, 33);
	private static final UUID				id				= UUID.randomUUID();
	private static final String				recruiterId 	= "kparkings";
	private static final EventType 			type 			= EventType.OFFERED_CANDIDATE;
	
	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		SupplyAndDemandEventEntity entity = 
				SupplyAndDemandEventEntity
				.builder()
					.created(created)
					.eventId(id)
					.recruiterId(recruiterId)
					.type(type)
				.build();
		
		assertEquals(created, 		entity.getCreated());
		assertEquals(id, 			entity.getEventId());
		assertEquals(recruiterId, 	entity.getRecruiterId());
		assertEquals(type, 			entity.getType());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test 
	public void testConvertToEntity() throws Exception{
		
		SupplyAndDemandEventEntity entity = 
				SupplyAndDemandEventEntity
				.builder()
					.created(created)
					.eventId(id)
					.recruiterId(recruiterId)
					.type(type)
				.build();
		
		SupplyAndDemandEvent event = SupplyAndDemandEventEntity.convertFromEntity(entity);
		
		assertEquals(created, 		event.getCreated());
		assertEquals(id, 			event.getEventId());
		assertEquals(recruiterId, 	event.getRecruiterId());
		assertEquals(type, 			event.getType());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* @throws Exception
	*/
	@Test 
	public void testConvertFromEntity() throws Exception{
		
		SupplyAndDemandEvent event = 
				SupplyAndDemandEvent
				.builder()
					.created(created)
					.eventId(id)
					.recruiterId(recruiterId)
					.type(type)
				.build();
		
		SupplyAndDemandEventEntity entity = SupplyAndDemandEventEntity.convertToEntity(event);
		
		assertEquals(created, 		entity.getCreated());
		assertEquals(id, 			entity.getEventId());
		assertEquals(recruiterId, 	entity.getRecruiterId());
		assertEquals(type, 			entity.getType());
		
	}
	
}