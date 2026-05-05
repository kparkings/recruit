package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Note;

/**
* Unit tests for the NoteAPIOutbound class 
*/
class NoteAPIOutboundTest {

	private static final UUID			ID 			= UUID.randomUUID();
	private static final LocalDateTime 	CREATED 	= LocalDateTime.of(2026, 3, 28, 19, 8, 9);
	private static final String 		TITLE 		= "Meeting Notes";
	private static final String 		TEXT 		= "Client wants Quarkus experience";

	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testBuilder() {
		
		NoteAPIOutbound note = NoteAPIOutbound
				.builder()
					.id(ID)
					.created(CREATED)
					.title(TITLE)
					.text(TEXT)
				.build();
		
		assertEquals(ID, note.getId());
		assertEquals(CREATED, note.getCreated());
		assertEquals(TITLE, note.getTitle().get());
		assertEquals(TEXT, note.getText());
		
	}
	
	/**
	* Tests construction via a Builder using Domain 
	* representation of a Note
	*/
	@Test
	void testBuilderFromDomain() {
		
		Note noteDomain = Note
				.builder()
					.id(ID)
					.created(CREATED)
					.title(TITLE)
					.text(TEXT)
				.build();
		
		NoteAPIOutbound note = NoteAPIOutbound
				.builder()
					.from(noteDomain)
				.build();
		
		assertEquals(ID, note.getId());
		assertEquals(CREATED, note.getCreated());
		assertEquals(TITLE, note.getTitle().get());
		assertEquals(TEXT, note.getText());
		
	}
	
}