package com.arenella.recruit.messaging.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.messaging.beans.Photo;
import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.ChatParticipant.CHAT_PARTICIPANT_TYPE;
import com.arenella.recruit.messaging.beans.Photo.PHOTO_FORMAT;

/**
* Unit tests for the ChatParticipantEntity class 
*/
class ChatParticipantEntityTest {

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
		
		ChatParticipantEntity participant = ChatParticipantEntity
				.builder()
					.participantId(PARTICIPANT_ID)
					.type(TYPE)
					.firstName(FIRSTNAME)
					.surname(SURNAME)
					.photo(PHOTO)
					.disableNotificationEmails(DISABLE_EMAIL)
					.lastTimeNewsFeedViewed(LAST_NEWSFEED_VIEW)
					.lastNotificationEmailSent(LAST_EMAIL)
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
		
		ChatParticipantEntity participant = ChatParticipantEntity
				.builder()
					.participantId(PARTICIPANT_ID)
					.type(TYPE)
					.firstName(FIRSTNAME)
					.surname(SURNAME)
					.disableNotificationEmails(DISABLE_EMAIL)
					.lastTimeNewsFeedViewed(LAST_NEWSFEED_VIEW)
					.lastNotificationEmailSent(LAST_EMAIL)
				.build();
		
		assertEquals(PARTICIPANT_ID, 		participant.getParticipantId());
		assertEquals(TYPE, 					participant.getType());
		assertEquals(FIRSTNAME, 			participant.getFirstName());
		assertEquals(SURNAME, 				participant.getSurame());
		assertEquals(LAST_NEWSFEED_VIEW, 	participant.getLastTimeNewsFeedViewed().get());
		assertTrue(participant.getPhoto().isEmpty());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	*/
	@Test
	void testFromEntity() {
		
		ChatParticipantEntity entity = ChatParticipantEntity
				.builder()
					.participantId(PARTICIPANT_ID)
					.type(TYPE)
					.firstName(FIRSTNAME)
					.surname(SURNAME)
					.photo(PHOTO)
					.disableNotificationEmails(DISABLE_EMAIL)
					.lastTimeNewsFeedViewed(LAST_NEWSFEED_VIEW)
					.lastNotificationEmailSent(LAST_EMAIL)
				.build();
		
		assertEquals(PARTICIPANT_ID, 		entity.getParticipantId());
		assertEquals(TYPE, 					entity.getType());
		assertEquals(FIRSTNAME, 			entity.getFirstName());
		assertEquals(SURNAME, 				entity.getSurame());
		assertEquals(DISABLE_EMAIL, 		entity.isDisableNotificationEmails());
		assertEquals(LAST_NEWSFEED_VIEW, 	entity.getLastTimeNewsFeedViewed().get());
		assertEquals(LAST_EMAIL, 			entity.getLastNotificationEmailSent().get());
		assertTrue(entity.getPhoto().isPresent());
		
		ChatParticipant participant = ChatParticipantEntity.fromEntity(entity);
		
		assertEquals(PARTICIPANT_ID, 		participant.getParticipantId());
		assertEquals(TYPE, 					participant.getType());
		assertEquals(FIRSTNAME, 			participant.getFirstName());
		assertEquals(SURNAME, 				participant.getSurame());
		assertNotNull(participant.getPhoto().get().imageBytes());
		assertNotNull(participant.getPhoto().get().format());
		assertEquals(DISABLE_EMAIL, 		participant.isDisableNotificationEmails());
		assertEquals(LAST_NEWSFEED_VIEW, 	participant.getLastTimeNewsFeedViewed().get());
		assertEquals(LAST_EMAIL, 			participant.getLastNotificationEmailSent().get());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* when no Photo is present
	*/
	@Test
	void testFromEntityNoPhoto() {
		
		ChatParticipantEntity entity = ChatParticipantEntity
				.builder()
					.participantId(PARTICIPANT_ID)
					.type(TYPE)
					.firstName(FIRSTNAME)
					.surname(SURNAME)
					.disableNotificationEmails(DISABLE_EMAIL)
					.lastTimeNewsFeedViewed(LAST_NEWSFEED_VIEW)
					.lastNotificationEmailSent(LAST_EMAIL)
				.build();
		
		assertEquals(PARTICIPANT_ID, 		entity.getParticipantId());
		assertEquals(TYPE, 					entity.getType());
		assertEquals(FIRSTNAME, 			entity.getFirstName());
		assertEquals(SURNAME, 				entity.getSurame());
		assertEquals(DISABLE_EMAIL, 		entity.isDisableNotificationEmails());
		assertEquals(LAST_NEWSFEED_VIEW, 	entity.getLastTimeNewsFeedViewed().get());
		assertEquals(LAST_EMAIL, 			entity.getLastNotificationEmailSent().get());
		
		ChatParticipant participant = ChatParticipantEntity.fromEntity(entity);
		
		assertEquals(PARTICIPANT_ID, 		participant.getParticipantId());
		assertEquals(TYPE, 					participant.getType());
		assertEquals(FIRSTNAME, 			participant.getFirstName());
		assertEquals(SURNAME, 				participant.getSurame());
		assertEquals(DISABLE_EMAIL, 		participant.isDisableNotificationEmails());
		assertEquals(LAST_NEWSFEED_VIEW, 	participant.getLastTimeNewsFeedViewed().get());
		assertEquals(LAST_EMAIL, 			participant.getLastNotificationEmailSent().get());
		
		assertTrue(participant.getPhoto().isEmpty());
		
	}
	
	/**
	* Tests conversion to Entity from Domain representation
	*/
	@Test
	void testToEntity() {
		
		ChatParticipant participant = ChatParticipant
				.builder()
					.participantId(PARTICIPANT_ID)
					.type(TYPE)
					.firstName(FIRSTNAME)
					.surname(SURNAME)
					.photo(PHOTO)
					.disableNotificationEmails(DISABLE_EMAIL)
					.lastTimeNewsFeedViewed(LAST_NEWSFEED_VIEW)
					.lastNotificationEmailSent(LAST_EMAIL)
				.build();
		
		assertEquals(PARTICIPANT_ID, 		participant.getParticipantId());
		assertEquals(TYPE, 					participant.getType());
		assertEquals(FIRSTNAME, 			participant.getFirstName());
		assertEquals(SURNAME, 				participant.getSurame());
		assertEquals(DISABLE_EMAIL, 		participant.isDisableNotificationEmails());
		assertEquals(LAST_NEWSFEED_VIEW, 	participant.getLastTimeNewsFeedViewed().get());
		assertEquals(LAST_EMAIL, 			participant.getLastNotificationEmailSent().get());
		
		assertTrue(participant.getPhoto().isPresent());
		
		ChatParticipantEntity entity = ChatParticipantEntity.toEntity(participant);
		
		assertEquals(PARTICIPANT_ID, 		entity.getParticipantId());
		assertEquals(TYPE, 					entity.getType());
		assertEquals(FIRSTNAME, 			entity.getFirstName());
		assertEquals(SURNAME, 				entity.getSurame());
		assertNotNull(entity.getPhoto().get().imageBytes());
		assertNotNull(entity.getPhoto().get().format());
		assertEquals(DISABLE_EMAIL, 		entity.isDisableNotificationEmails());
		assertEquals(LAST_NEWSFEED_VIEW, 	entity.getLastTimeNewsFeedViewed().get());
		assertEquals(LAST_EMAIL, 			entity.getLastNotificationEmailSent().get());
		
	}

	/**
	* Tests conversion from Domain to Entity representation
	* when no Photo is present
	*/
	@Test
	void testToEntityNoPhoto() {
		
		ChatParticipant participant = ChatParticipant
				.builder()
					.participantId(PARTICIPANT_ID)
					.type(TYPE)
					.firstName(FIRSTNAME)
					.surname(SURNAME)
					.disableNotificationEmails(DISABLE_EMAIL)
					.lastTimeNewsFeedViewed(LAST_NEWSFEED_VIEW)
					.lastNotificationEmailSent(LAST_EMAIL)
				.build();
		
		assertEquals(PARTICIPANT_ID, 		participant.getParticipantId());
		assertEquals(TYPE, 					participant.getType());
		assertEquals(FIRSTNAME, 			participant.getFirstName());
		assertEquals(SURNAME, 				participant.getSurame());
		assertEquals(DISABLE_EMAIL, 		participant.isDisableNotificationEmails());
		assertEquals(LAST_NEWSFEED_VIEW, 	participant.getLastTimeNewsFeedViewed().get());
		assertEquals(LAST_EMAIL, 			participant.getLastNotificationEmailSent().get());
		
		ChatParticipantEntity entity = ChatParticipantEntity.toEntity(participant);
		
		assertEquals(PARTICIPANT_ID, 		entity.getParticipantId());
		assertEquals(TYPE, 					entity.getType());
		assertEquals(FIRSTNAME, 			entity.getFirstName());
		assertEquals(SURNAME, 				entity.getSurame());
		assertEquals(DISABLE_EMAIL, 		entity.isDisableNotificationEmails());
		assertEquals(LAST_NEWSFEED_VIEW, 	entity.getLastTimeNewsFeedViewed().get());
		assertEquals(LAST_EMAIL, 			entity.getLastNotificationEmailSent().get());
		assertTrue(entity.getPhoto().isEmpty());
		
	}
	
}