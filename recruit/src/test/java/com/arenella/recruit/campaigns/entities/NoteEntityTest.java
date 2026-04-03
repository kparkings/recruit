package com.arenella.recruit.campaigns.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Note;

/**
* Unit tests for the NoteEntity class 
*/
class NoteEntityTest {
	
	private static UUID				ID				= UUID.randomUUID();
	private static UUID 			CAMPAIGN_ID 	= UUID.randomUUID();
	private static UUID 			ROLE_ID 		= UUID.randomUUID();
	private static LocalDateTime 	CREATED 		= LocalDateTime.of(2026,3, 27, 20, 17, 9);
	private static String 			TITLE 			= "Phone Numbers";
	private static String 			TEXT			 = "Steve M : 0032 223 223122";
	
	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testBuilder() {

		NoteEntity note = NoteEntity
				.builder()
					.id(ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.created(CREATED)
					.title(TITLE)
					.text(TEXT)
				.build();
		
		assertEquals(ID, 			note.getId());
		assertEquals(CAMPAIGN_ID, 	note.getCampaignId());
		assertEquals(ROLE_ID, 		note.getRoleId().get());
		assertEquals(CREATED, 		note.getCreated());
		assertEquals(TITLE, 		note.getTitle().get());
		assertEquals(TEXT, 			note.getText());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation 
	*/
	@Test
	void testFromEntity() {
		
		NoteEntity entity = NoteEntity
				.builder()
					.id(ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.created(CREATED)
					.title(TITLE)
					.text(TEXT)
				.build();
		
		assertEquals(ID, 			entity.getId());
		assertEquals(CAMPAIGN_ID, 	entity.getCampaignId());
		assertEquals(ROLE_ID, 		entity.getRoleId().get());
		assertEquals(CREATED, 		entity.getCreated());
		assertEquals(TITLE, 		entity.getTitle().get());
		assertEquals(TEXT, 			entity.getText());
		
		Note note = NoteEntity.fromEntity(entity);
		
		assertEquals(ID, 			note.getId());
		assertEquals(CAMPAIGN_ID, 	note.getCampaignId());
		assertEquals(ROLE_ID, 		note.getRoleId().get());
		assertEquals(CREATED, 		note.getCreated());
		assertEquals(TITLE, 		note.getTitle().get());
		assertEquals(TEXT, 			note.getText());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	*/
	@Test
	void testToEntity() {
		
		Note note = Note
				.builder()
					.id(ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.created(CREATED)
					.title(TITLE)
					.text(TEXT)
				.build();
		
		assertEquals(ID, 			note.getId());
		assertEquals(CAMPAIGN_ID, 	note.getCampaignId());
		assertEquals(ROLE_ID, 		note.getRoleId().get());
		assertEquals(CREATED, 		note.getCreated());
		assertEquals(TITLE, 		note.getTitle().get());
		assertEquals(TEXT, 			note.getText());
		
		NoteEntity entity = NoteEntity.toEntity(note);
		
		assertEquals(ID, 			entity.getId());
		assertEquals(CAMPAIGN_ID, 	entity.getCampaignId());
		assertEquals(ROLE_ID, 		entity.getRoleId().get());
		assertEquals(CREATED, 		entity.getCreated());
		assertEquals(TITLE, 		entity.getTitle().get());
		assertEquals(TEXT, 			entity.getText());
		
	}
	
}