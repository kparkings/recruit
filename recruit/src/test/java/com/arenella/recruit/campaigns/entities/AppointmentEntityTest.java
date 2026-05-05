package com.arenella.recruit.campaigns.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Appointment;

/**
* Unit tests for the AppointmentEntity class 
*/
class AppointmentEntityTest {

	private static final UUID				ID				= UUID.randomUUID();
	private static final UUID				CAMPAIGN_ID		= UUID.randomUUID();
	private static final UUID				ROLE_ID			= UUID.randomUUID();
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
		
		AppointmentEntity appointment = AppointmentEntity
				.builder()
					.appointmentId(ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.name(NAME)
					.description(DESCRIPTION)
					.videoLink(VIDEO_LINK)
					.phoneNumber(PHONE_NUMBER)
					.when(WHEN)
				.build();
		
		assertEquals(ID, 			appointment.getAppointmentId());
		assertEquals(CAMPAIGN_ID, 	appointment.getCampaignId());
		assertEquals(ROLE_ID, 		appointment.getRoleId().get());
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
		
		AppointmentEntity appointment = AppointmentEntity
				.builder()
					.name(NAME)
					.description(DESCRIPTION)
					.when(WHEN)
				.build();
		
		assertTrue(appointment.getRoleId().isEmpty());
		assertTrue(appointment.getVideoLink().isEmpty());
		assertTrue(appointment.getPhoneNumber().isEmpty());
		
	}
	
	/**
	* Tests conversion from the Entity to Domain representation 
	* of an Appointment 
	*/
	@Test
	void testFromEntity() {
		
		AppointmentEntity entity = AppointmentEntity
				.builder()
					.appointmentId(ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.name(NAME)
					.description(DESCRIPTION)
					.videoLink(VIDEO_LINK)
					.phoneNumber(PHONE_NUMBER)
					.when(WHEN)
				.build();
		
		Appointment appointment = AppointmentEntity.fromEntity(entity);
		
		assertEquals(ID, 			appointment.getAppointmentId());
		assertEquals(CAMPAIGN_ID, 	appointment.getCampaignId());
		assertEquals(ROLE_ID, 		appointment.getRoleId().get());
		assertEquals(NAME, 			appointment.getName());
		assertEquals(DESCRIPTION, 	appointment.getDescription());
		assertEquals(VIDEO_LINK, 	appointment.getVideoLink().get());
		assertEquals(PHONE_NUMBER, 	appointment.getPhoneNumber().get());
		assertEquals(WHEN,		 	appointment.getWhen());
		
	}
	
	/**
	* Tests conversion from the Domain to the Entity representation 
	* of an Appointment 
	*/
	@Test
	void testToEntity() {
		
		Appointment appointment = Appointment
				.builder()
					.appointmentId(ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.name(NAME)
					.description(DESCRIPTION)
					.videoLink(VIDEO_LINK)
					.phoneNumber(PHONE_NUMBER)
					.when(WHEN)
				.build();
		
		AppointmentEntity entity = AppointmentEntity.toEntity(appointment);
		
		assertEquals(ID, 			entity.getAppointmentId());
		assertEquals(CAMPAIGN_ID, 	entity.getCampaignId());
		assertEquals(ROLE_ID, 		entity.getRoleId().get());
		assertEquals(NAME, 			entity.getName());
		assertEquals(DESCRIPTION, 	entity.getDescription());
		assertEquals(VIDEO_LINK, 	entity.getVideoLink().get());
		assertEquals(PHONE_NUMBER, 	entity.getPhoneNumber().get());
		assertEquals(WHEN,		 	entity.getWhen());
		
	}
	
}