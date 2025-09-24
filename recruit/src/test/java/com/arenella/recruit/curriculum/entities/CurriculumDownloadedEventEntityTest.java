package com.arenella.recruit.curriculum.entities;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.curriculum.beans.CurriculumDownloadedEvent;
import com.arenella.recruit.curriculum.entity.CurriculumDownloadedEventEntity;

/**
* Unit tests for the CurriculumDownloadedEventEntity class
* @author K Parkings
*/
class CurriculumDownloadedEventEntityTest {

	private static final String 			CURRICULUM_ID 		= "anId";
	private static final String 			USER_ID 				= "kparkings";
	private static final boolean 			IS_ADMIN_USER 		= true;
	private static final LocalDateTime 		TIMESTAMP 			= LocalDateTime.of(2021, 5, 28, 12, 05);
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		CurriculumDownloadedEventEntity entity = CurriculumDownloadedEventEntity
																		.builder()
																			.curriculumId(CURRICULUM_ID)
																			.userId(USER_ID)
																			.isAdminUser(IS_ADMIN_USER)
																			.timestamp(TIMESTAMP)
																		.build();
	
		assertEquals(CURRICULUM_ID, 	entity.getCurriculumId());
		assertEquals(USER_ID, 			entity.getUserId());
		assertEquals(IS_ADMIN_USER, 	entity.isAdminUser());
		assertEquals(TIMESTAMP, 		entity.getTimestamp());
		
	}

	/**
	* Tests conversion from Domain representation of Event to 
	* the Entity representation
	* @throws Exception
	*/
	@Test
	void testConvertToEntity() {
		
		CurriculumDownloadedEvent event = CurriculumDownloadedEvent.manualBuilder()
																			.curriculumId(CURRICULUM_ID)
																			.userId(USER_ID)
																			.isAdminUser(IS_ADMIN_USER)
																			.timestamp(TIMESTAMP)
																		.build();
		
		CurriculumDownloadedEventEntity entity = CurriculumDownloadedEventEntity.toEntity(event);
		
		assertEquals(CURRICULUM_ID, 	entity.getCurriculumId());
		assertEquals(USER_ID, 			entity.getUserId());
		assertEquals(IS_ADMIN_USER, 	entity.isAdminUser());
		assertEquals(TIMESTAMP, 		entity.getTimestamp());
		
	}
	
	/**
	* Tests conversion from Entity representation of Event to 
	* the Domain representation
	* @throws Exception
	*/
	@Test
	void testConvertFromEntity() {
		
		CurriculumDownloadedEventEntity entity = CurriculumDownloadedEventEntity
				.builder()
					.curriculumId(CURRICULUM_ID)
					.userId(USER_ID)
					.isAdminUser(IS_ADMIN_USER)
					.timestamp(TIMESTAMP)
				.build();

		CurriculumDownloadedEvent event = CurriculumDownloadedEventEntity.fromEntity(entity);
		
		assertEquals(CURRICULUM_ID, 	event.getCurriculumId());
		assertEquals(USER_ID, 			event.getUserId());
		assertEquals(IS_ADMIN_USER, 	event.isAdminUser());
		assertEquals(TIMESTAMP, 		event.getTimestamp());

	}
	
}