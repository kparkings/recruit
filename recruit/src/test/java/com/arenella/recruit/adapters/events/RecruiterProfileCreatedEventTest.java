package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the RecruiterProfileCreatedEvent class
*/
class RecruiterProfileCreatedEventTest {

	private static final String RECRUITER_ID = "steveTheRecruiter";
	private static final Photo 	PROFILE_IMAGE = new Photo(PHOTO_FORMAT.png, new byte[] {});
	
	/**
	* Test construction via builder 
	*/
	@Test
	void testConstructionViaBuilder() {
		
		RecruiterProfileCreatedEvent event = RecruiterProfileCreatedEvent
				.builder()
					.recruiterId(RECRUITER_ID)
					.profileImage(PROFILE_IMAGE)
				.build();
		
		assertEquals(RECRUITER_ID, event.getRecruiterId());
		assertTrue(event.getProfileImage().isPresent());
		
	}
	
	/**
	* Tests construction via builder when there is 
	* no profileImage
	*/
	@Test
	void testConstructionViaBuilderNoProfileImage() {
		
		RecruiterProfileCreatedEvent event = RecruiterProfileCreatedEvent
				.builder()
					.recruiterId(RECRUITER_ID)
				.build();
		
		assertEquals(RECRUITER_ID, event.getRecruiterId());
		assertFalse(event.getProfileImage().isPresent());
	}
	
}
