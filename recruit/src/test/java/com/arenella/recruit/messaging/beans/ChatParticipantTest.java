package com.arenella.recruit.messaging.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.messaging.beans.ChatParticipant.CHAT_PARTICIPANT_TYPE;
import com.arenella.recruit.messaging.beans.Photo.PHOTO_FORMAT;

/**
* Unit tests for the ChatParticipant class
*/
class ChatParticipantTest {

	private static final String 				PARTICIPANT_ID 			= "a123";
	private static final CHAT_PARTICIPANT_TYPE 	TYPE					= CHAT_PARTICIPANT_TYPE.CANDIDATE;
	private static final String					FIRSTNAME 				= "kevin";
	private static final String					SURNAME 				= "Parkings";
	private static final Photo 					PHOTO 					= new Photo(new byte[] {}, PHOTO_FORMAT.PNG);
	private static final boolean				DISABLE_EMAIL			= true;
	private static final LocalDateTime			LAST_EMAIL				= LocalDateTime.of(2026, 2, 14, 10, 11 , 12);
	private static final LocalDateTime			LAST_NEWSFEED_VIEW		= LocalDateTime.of(2026, 2, 25, 18, 07 , 00);
	
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
					.disableNotificationEmails(DISABLE_EMAIL)
					.lastNotificationEmailSent(LAST_EMAIL)
					.lastTimeNewsFeedViewed(LAST_NEWSFEED_VIEW)
				.build();
		
		assertEquals(PARTICIPANT_ID, 		participant.getParticipantId());
		assertEquals(TYPE, 					participant.getType());
		assertEquals(FIRSTNAME, 			participant.getFirstName());
		assertEquals(SURNAME, 				participant.getSurame());
		assertEquals(DISABLE_EMAIL, 		participant.isDisableNotificationEmails());
		assertEquals(LAST_EMAIL, 			participant.getLastNotificationEmailSent().get());
		assertEquals(LAST_NEWSFEED_VIEW, 	participant.getLastTimeNewsFeedViewed().get());
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
					.disableNotificationEmails(DISABLE_EMAIL)
					.lastNotificationEmailSent(LAST_EMAIL)
					.lastTimeNewsFeedViewed(LAST_NEWSFEED_VIEW)
				.build();
		
		assertEquals(PARTICIPANT_ID, 	participant.getParticipantId());
		assertEquals(TYPE, 				participant.getType());
		assertEquals(FIRSTNAME, 		participant.getFirstName());
		assertEquals(SURNAME, 			participant.getSurame());
		assertEquals(DISABLE_EMAIL, 	participant.isDisableNotificationEmails());
		assertEquals(LAST_EMAIL, 		participant.getLastNotificationEmailSent().get());
		assertEquals(LAST_NEWSFEED_VIEW, 	participant.getLastTimeNewsFeedViewed().get());
		assertTrue(participant.getPhoto().isEmpty());
		
	}
	
}