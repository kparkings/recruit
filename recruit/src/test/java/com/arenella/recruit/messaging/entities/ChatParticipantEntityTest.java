package com.arenella.recruit.messaging.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.messaging.beans.Photo;
import com.arenella.recruit.messaging.beans.ChatParticipant;
import com.arenella.recruit.messaging.beans.ChatParticipant.CHAT_PARTICIPANT_TYPE;
import com.arenella.recruit.messaging.beans.Photo.PHOTO_FORMAT;

/**
* Unit tests for the ChatParticipantEntity class 
*/
class ChatParticipantEntityTest {

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
		
		ChatParticipantEntity participant = ChatParticipantEntity
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
		
		ChatParticipantEntity participant = ChatParticipantEntity
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
				.build();
		
		assertEquals(PARTICIPANT_ID, 	entity.getParticipantId());
		assertEquals(TYPE, 				entity.getType());
		assertEquals(FIRSTNAME, 		entity.getFirstName());
		assertEquals(SURNAME, 			entity.getSurame());
		assertTrue(entity.getPhoto().isPresent());
		
		ChatParticipant participant = ChatParticipantEntity.fromEntity(entity);
		
		assertEquals(PARTICIPANT_ID, 	participant.getParticipantId());
		assertEquals(TYPE, 				participant.getType());
		assertEquals(FIRSTNAME, 		participant.getFirstName());
		assertEquals(SURNAME, 			participant.getSurame());
		assertNotNull(participant.getPhoto().get().imageBytes());
		assertNotNull(participant.getPhoto().get().format());
		
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
				.build();
		
		assertEquals(PARTICIPANT_ID, 	entity.getParticipantId());
		assertEquals(TYPE, 				entity.getType());
		assertEquals(FIRSTNAME, 		entity.getFirstName());
		assertEquals(SURNAME, 			entity.getSurame());
		
		ChatParticipant participant = ChatParticipantEntity.fromEntity(entity);
		
		assertEquals(PARTICIPANT_ID, 	participant.getParticipantId());
		assertEquals(TYPE, 				participant.getType());
		assertEquals(FIRSTNAME, 		participant.getFirstName());
		assertEquals(SURNAME, 			participant.getSurame());
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
				.build();
		
		assertEquals(PARTICIPANT_ID, 	participant.getParticipantId());
		assertEquals(TYPE, 				participant.getType());
		assertEquals(FIRSTNAME, 		participant.getFirstName());
		assertEquals(SURNAME, 			participant.getSurame());
		assertTrue(participant.getPhoto().isPresent());
		
		ChatParticipantEntity entity = ChatParticipantEntity.toEntity(participant);
		
		assertEquals(PARTICIPANT_ID, 	entity.getParticipantId());
		assertEquals(TYPE, 				entity.getType());
		assertEquals(FIRSTNAME, 		entity.getFirstName());
		assertEquals(SURNAME, 			entity.getSurame());
		assertNotNull(entity.getPhoto().get().imageBytes());
		assertNotNull(entity.getPhoto().get().format());
		
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
				.build();
		
		assertEquals(PARTICIPANT_ID, 	participant.getParticipantId());
		assertEquals(TYPE, 				participant.getType());
		assertEquals(FIRSTNAME, 		participant.getFirstName());
		assertEquals(SURNAME, 			participant.getSurame());
		
		ChatParticipantEntity entity = ChatParticipantEntity.toEntity(participant);
		
		assertEquals(PARTICIPANT_ID, 	entity.getParticipantId());
		assertEquals(TYPE, 				entity.getType());
		assertEquals(FIRSTNAME, 		entity.getFirstName());
		assertEquals(SURNAME, 			entity.getSurame());
		assertTrue(entity.getPhoto().isEmpty());
		
	}
	
}