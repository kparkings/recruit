package com.arenella.recruit.campaigns.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the Note class 
*/
class NoteTest {

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
		
	}
	
}