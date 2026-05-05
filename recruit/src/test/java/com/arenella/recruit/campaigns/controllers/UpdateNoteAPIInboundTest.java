package com.arenella.recruit.campaigns.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the UpdateNoteAPIInbound class 
*/
class UpdateNoteAPIInboundTest {

	private static final String 		TITLE 			= "Meeting Notes";
	private static final String 		TEXT 			= "We had a meeting.";
	
	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testBuilder() {
		
		UpdateNoteAPIInbound note = UpdateNoteAPIInbound
				.builder()
					.title(TITLE)
					.text(TEXT)
				.build();
		
		assertEquals(TITLE, 		note.getTitle().get());
		assertEquals(TEXT, 			note.getText());
		
	}
	
	/**
	* Tests construction via a Builder where the non mandatory 
	* values are not provided
	*/
	@Test
	void testBuilderDefaultValues() {
		
		UpdateNoteAPIInbound note = UpdateNoteAPIInbound
				.builder()
					.text(TEXT)
				.build();
		
		assertEquals(TEXT, 			note.getText());
		
		assertTrue(note.getTitle().isEmpty());
		
	}
	
}