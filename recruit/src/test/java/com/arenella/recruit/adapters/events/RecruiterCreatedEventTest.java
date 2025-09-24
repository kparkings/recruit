package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the RecruiterCreatedEvent class
* @author K Parkings
*/
class RecruiterCreatedEventTest {

	/**
	* Tests creation via Builder 
	*/
	@Test
	void testBuilder() {
		
		final String recruiterId 		= "kparkings";
		final String encryptedPassword 	= "3sefes##@@1!f";
		final String email				= "admin@arenella-ict.com";
		final String firstName			= "Kevin";
		final String surname			= "Parkings";
		
		RecruiterCreatedEvent event = RecruiterCreatedEvent
				.builder()
					.recruiterId(recruiterId)
					.encryptedPassword(encryptedPassword)
					.email(email)
					.firstName(firstName)
					.surname(surname)
				.build();
		
		assertEquals(recruiterId, 		event.getRecruiterId());
		assertEquals(encryptedPassword, event.getEncryptedPassord());
		assertEquals(email, 			event.getEmail());
		assertEquals(firstName, 		event.getFirstName());
		assertEquals(surname, 			event.getSurname());
	}
	
}
