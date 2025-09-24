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

	private static final String 			curriculumId 		= "anId";
	private static final String 			userId 				= "kparkings";
	private static final boolean 			isAdminUser 		= true;
	private static final LocalDateTime 		timestamp 			= LocalDateTime.of(2021, 5, 28, 12, 05);
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		CurriculumDownloadedEventEntity entity = CurriculumDownloadedEventEntity
																		.builder()
																			.curriculumId(curriculumId)
																			.userId(userId)
																			.isAdminUser(isAdminUser)
																			.timestamp(timestamp)
																		.build();
	
		assertEquals(curriculumId, 		entity.getCurriculumId());
		assertEquals(userId, 			entity.getUserId());
		assertEquals(isAdminUser, 		entity.isAdminUser());
		assertEquals(timestamp, 		entity.getTimestamp());
		
	}

	/**
	* Tests conversion from Domain representation of Event to 
	* the Entity representation
	* @throws Exception
	*/
	@Test
	void testConvertToEntity() {
		
		CurriculumDownloadedEvent event = CurriculumDownloadedEvent.manualBuilder()
																			.curriculumId(curriculumId)
																			.userId(userId)
																			.isAdminUser(isAdminUser)
																			.timestamp(timestamp)
																		.build();
		
		CurriculumDownloadedEventEntity entity = CurriculumDownloadedEventEntity.toEntity(event);
		
		assertEquals(curriculumId, 		entity.getCurriculumId());
		assertEquals(userId, 			entity.getUserId());
		assertEquals(isAdminUser, 		entity.isAdminUser());
		assertEquals(timestamp, 		entity.getTimestamp());
		
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
					.curriculumId(curriculumId)
					.userId(userId)
					.isAdminUser(isAdminUser)
					.timestamp(timestamp)
				.build();

		CurriculumDownloadedEvent event = CurriculumDownloadedEventEntity.fromEntity(entity);
		
		assertEquals(curriculumId, 		event.getCurriculumId());
		assertEquals(userId, 			event.getUserId());
		assertEquals(isAdminUser, 		event.isAdminUser());
		assertEquals(timestamp, 		event.getTimestamp());

	}
	
}