package com.arenella.recruit.messaging.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.messaging.beans.ChatParticipant.CHAT_PARTICIPANT_TYPE;
import com.arenella.recruit.messaging.beans.Photo.PHOTO_FORMAT;

/**
* Unit tests for the ChatParticipant class
*/
class ChatParticipantTest {

	private static final String 				PARTICIPANT_ID 	= "a123";
	private static final CHAT_PARTICIPANT_TYPE 	TYPE			= CHAT_PARTICIPANT_TYPE.CANDIDATE;
	private static final String					FIRSTNAME 		= "kevin";
	private static final String					SURNAME 		= "Parkings";
	private static final Photo 					PHOTO 			= new Photo(new byte[] {}, PHOTO_FORMAT.PNG);
	
	/**
	* Tests construction via builder 
	*/
	@Test
	void testBuilder() {
		
		ChatParticipant participant = ChatParticipant
				.builder()
					.participantId(PARTICIPANT_ID)
					.type(TYPE)
					.firstName(FIRSTNAME)
					.surname(SURNAME)
					.photo(PHOTO)
				.build();
		
		assertEquals(PARTICIPANT_ID, 	participant.getParticipantId());
		assertEquals(TYPE, 				participant.getType());
		assertEquals(FIRSTNAME, 		participant.getFirstName());
		assertEquals(SURNAME, 			participant.getSurame());
		assertTrue(participant.getPhoto().isPresent());
		
	}
	
	/**
	* Tests construction when no Photo has been provided 
	*/
	@Test
	void testBuilderNoPhoto() {
		
		ChatParticipant participant = ChatParticipant
				.builder()
					.participantId(PARTICIPANT_ID)
					.type(TYPE)
					.firstName(FIRSTNAME)
					.surname(SURNAME)
				.build();
		
		assertEquals(PARTICIPANT_ID, 	participant.getParticipantId());
		assertEquals(TYPE, 				participant.getType());
		assertEquals(FIRSTNAME, 		participant.getFirstName());
		assertEquals(SURNAME, 			participant.getSurame());
		assertTrue(participant.getPhoto().isEmpty());
		
	}
	
}