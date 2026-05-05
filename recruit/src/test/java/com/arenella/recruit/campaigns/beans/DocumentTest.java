package com.arenella.recruit.campaigns.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Document.DocumentType;

/**
* Unit tests for the Document class 
*/
class DocumentTest {
	
	public static final UUID 			DOCUMENT_ID 		= UUID.randomUUID(); 
	public static final UUID 			CAMPAIGN_ID 		= UUID.randomUUID();
	public static final UUID 			ROLE_ID 			= UUID.randomUUID();
	public static final String 			TITLE 				= "A title"; 
	public static final DocumentType 	TYPE 				= DocumentType.DOC; 
	public static final byte[] 			BYTES 				= new byte[] {};
	public static final LocalDateTime 	CREATED 			= LocalDateTime.of(2026, 4, 8, 22, 11, 10);
	
	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testConstuction() {
		
		Document document = Document
				.builder()
					.documentId(DOCUMENT_ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.title(TITLE)
					.type(TYPE)
					.bytes(BYTES)
					.created(CREATED)
				.build();
		
		assertEquals(DOCUMENT_ID, 	document.getDocumentId());
		assertEquals(CAMPAIGN_ID, 	document.getCampaignId());
		assertEquals(ROLE_ID, 		document.getRoleId().get());
		assertEquals(TITLE, 		document.getTitle());
		assertEquals(TYPE, 			document.getType());
		assertEquals(BYTES, 		document.getBytes());
		assertEquals(CREATED, 		document.getCreated());
		
	}
	
	/**
	* Tests construction via a Builder with default values
	*/
	@Test
	void testConstuctionDefaults() {
		
		Document document = Document
				.builder()
				.build();
		
		assertTrue(document.getRoleId().isEmpty());
		
	}
	
}