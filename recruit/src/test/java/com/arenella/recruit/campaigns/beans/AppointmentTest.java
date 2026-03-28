package com.arenella.recruit.campaigns.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the Appointment Class
*/
class AppointmentTest {

	private static final String 			NAME 			= "Call with Candidate K Parkings";
	private static final String 			DESCRIPTION 	= "Initial introduction meeting with Kevin Parkings";
	private static final String 			VIDEO_LINK 		= "https://wwww.fakevidomeeting.com?asaasa22131";
	private static final String 			PHONE_NUMBER 	= "0031 643 220 866";
	private static final ZonedDateTime 		WHEN 			= ZonedDateTime.of(LocalDateTime.of(2026, 3, 28, 13, 35, 10), ZoneId.systemDefault());
	
	/**
	* Tests construction via the Builder 
	*/
	@Test
	void testBuilder() {
		
		Appointment appointment = Appointment
				.builder()
					.name(NAME)
					.description(DESCRIPTION)
					.videoLink(VIDEO_LINK)
					.phoneNumber(PHONE_NUMBER)
					.when(WHEN)
				.build();
		
		assertEquals(NAME, 			appointment.getName());
		assertEquals(DESCRIPTION, 	appointment.getDescription());
		assertEquals(VIDEO_LINK, 	appointment.getVideoLink().get());
		assertEquals(PHONE_NUMBER, 	appointment.getPhoneNumber().get());
		assertEquals(WHEN,		 	appointment.getWhen());
		
	}
	
	/**
	* Tests construction via the builder with no optional's specified
	*/
	@Test
	void testBuilderDefailts() {
		
		Appointment appointment = Appointment
				.builder()
					.name(NAME)
					.description(DESCRIPTION)
					.when(WHEN)
				.build();
		
		assertTrue(appointment.getVideoLink().isEmpty());
		assertTrue(appointment.getPhoneNumber().isEmpty());
		
	}
	
}