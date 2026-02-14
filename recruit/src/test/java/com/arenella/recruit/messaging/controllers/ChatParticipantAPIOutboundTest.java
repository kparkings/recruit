package com.arenella.recruit.messaging.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.ChatParticipant.CHAT_PARTICIPANT_TYPE;

/**
* Unit tests for the ChatParticipantAPIOutbound class 
*/
class ChatParticipantAPIOutboundTest {

	/**
	* Tests construction via Builder 
	*/
	@Test
	void testConstruction() {
		
		final String 					id 					= "pId1";
		final String 					firstName 			= "Bob";
		final String 					surname 			= "Parkings";
		final CHAT_PARTICIPANT_TYPE 	type 				= CHAT_PARTICIPANT_TYPE.CANDIDATE;
		final boolean 					disableEmails 		= true;
		
		ChatParticipant chatParticipant = ChatParticipant
				.builder()
					.participantId(id)
					.type(type)
					.firstName(firstName)
					.surname(surname)
					.disableNotificationEmails(disableEmails)
				.build();
		
		ChatParticipantAPIOutbound outbound = ChatParticipantAPIOutbound
				.builder()
					.chatParticipant(chatParticipant)
				.build();
		
		assertEquals(id, 			outbound.getId());
		assertEquals(type, 			outbound.getType());
		assertEquals(firstName, 	outbound.getFirstName());
		assertEquals(surname, 		outbound.getSurname());
		assertEquals(disableEmails, outbound.isDisableNotificationEmails());
		
	}
	
}