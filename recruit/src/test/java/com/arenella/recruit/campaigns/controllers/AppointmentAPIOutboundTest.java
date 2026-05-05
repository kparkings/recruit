package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Appointment;

/**
* Unit tests for the AppointmentAPIOutbound class 
*/
class AppointmentAPIOutboundTest {

	private static final String 		NAME			= "Client meeting";
	private static final String 		DESCRIPTION		= "Meeting to discuss new C# role";
	private static final String 		VIDEO_LINK		= "https://gmeet.com/1818D22";
	private static final String 		PHONE_NUMBER	= "0031 645 888 555";
	private static final ZonedDateTime 	WHEN			= ZonedDateTime.of(LocalDateTime.of(2026, 3,28,19,27,11), ZoneId.systemDefault());
	
	/**
	* Tests construction via a Builder
	*/
	@Test
	void testConstruction() {
		
		AppointmentAPIOutbound appointment = AppointmentAPIOutbound
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
		assertEquals(WHEN, 			appointment.getWhen());
		
	}
	
	/**
	* Tests construction via a Builder when no video link or
	* phone number has been provided
	*/
	@Test
	void testConstructionNoLinks() {
		
		AppointmentAPIOutbound appointment = AppointmentAPIOutbound
				.builder()
					.name(NAME)
					.description(DESCRIPTION)
					.when(WHEN)
				.build();
		
		assertEquals(NAME, 			appointment.getName());
		assertEquals(DESCRIPTION, 	appointment.getDescription());
		assertEquals(WHEN, 			appointment.getWhen());
		
		assertTrue(appointment.getVideoLink().isEmpty());
		assertTrue(appointment.getPhoneNumber().isEmpty());
		
	}
	
	/**
	* Tests construction using the Domain representation of an Appointment 
	* to populate the Builder 
	*/
	@Test
	void testConstructionFromDomainObjects() {
		
		Appointment appointmentDomain = Appointment
				.builder()
					.name(NAME)
					.description(DESCRIPTION)
					.videoLink(VIDEO_LINK)
					.phoneNumber(PHONE_NUMBER)
					.when(WHEN)
				.build();
		
		AppointmentAPIOutbound appointment = AppointmentAPIOutbound
				.builder()
					.from(appointmentDomain)
				.build();
		
		assertEquals(NAME, 			appointment.getName());
		assertEquals(DESCRIPTION, 	appointment.getDescription());
		assertEquals(VIDEO_LINK, 	appointment.getVideoLink().get());
		assertEquals(PHONE_NUMBER, 	appointment.getPhoneNumber().get());
		assertEquals(WHEN, 			appointment.getWhen());
		
	}
	
	/**
	* Tests construction using the Domain representation of an Appointment 
	* to populate the Builder 
	*/
	@Test
	void testConstructionFromDomainObjectsNoLinks() {
		
		Appointment appointmentDomain = Appointment
				.builder()
					.name(NAME)
					.description(DESCRIPTION)
					.when(WHEN)
				.build();
		
		AppointmentAPIOutbound appointment = AppointmentAPIOutbound
				.builder()
					.from(appointmentDomain)
				.build();
		
		assertEquals(NAME, 			appointment.getName());
		assertEquals(DESCRIPTION, 	appointment.getDescription());
		assertEquals(WHEN, 			appointment.getWhen());
		
		assertTrue(appointment.getVideoLink().isEmpty());
		assertTrue(appointment.getPhoneNumber().isEmpty());
		
	}
	
}