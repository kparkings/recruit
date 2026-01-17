package com.arenella.recruit.messaging.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.messaging.beans.ChatParticipant;

/**
* Unit tests for the ChatParticipantAPIOutbound class 
*/
class ChatParticipantAPIOutboundTest {

	/**
	* Tests construction via Builder 
	*/
	@Test
	void testConstruction() {
		
		final String id 		= "pId1";
		final String firstName 	= "Bob";
		final String surname 	= "Parkings";
		
		ChatParticipant chatParticipant = ChatParticipant
				.builder()
					.participantId(id)
					.firstName(firstName)
					.surname(surname)
				.build();
		
		ChatParticipantAPIOutbound outbound = ChatParticipantAPIOutbound
				.builder()
					.chatParticipant(chatParticipant)
				.build();
		
		assertEquals(id, 			outbound.getId());
		assertEquals(firstName, 	outbound.getFirstName());
		assertEquals(surname, 		outbound.getSurname());
		
	}
	
}