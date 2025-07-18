package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.SavedCandidateSearch;
import com.fasterxml.jackson.databind.JsonNode;

/**
* Unit tests for the SavedCandidateSearchRequestUpdateAPIInbound class 
*/
class SavedCandidateSearchRequestUpdateAPIInboundTest {

	private static final UUID 		ID				= UUID.randomUUID();
	private static final String 	USER_ID			= "recruiter1";
	private static final boolean 	EMAIL_ALERT		= true;
	private static final String 	SEARCH_NAME		= "boop search";
	private static final JsonNode 	SEARCH_REQUEST	= mock(JsonNode.class);
	
	/**
	* Tests construction via the Builder
	*/
	@Test
	void testBuilder() {
	
		SavedCandidateSearchRequestUpdateAPIInbound search = SavedCandidateSearchRequestUpdateAPIInbound
				.builder()
					.id(ID)
					.emailAlert(EMAIL_ALERT)
					.searchName(SEARCH_NAME)
					.searchRequest(SEARCH_REQUEST)
				.build();
		
		assertEquals(ID, 				search.getId());
		assertEquals(EMAIL_ALERT, 		search.hasEmailAlert());
		assertEquals(SEARCH_NAME, 		search.getSearchName());
		assertEquals(SEARCH_REQUEST, 	search.getSearchRequest());
		
	}
	
	/**
	* Tests conversion from Domain representation
	*/
	@Test
	void testToDomain() {
		
		SavedCandidateSearchRequestUpdateAPIInbound inbound = SavedCandidateSearchRequestUpdateAPIInbound
				.builder()
				.id(ID)
				.emailAlert(EMAIL_ALERT)
				.searchName(SEARCH_NAME)
				.searchRequest(SEARCH_REQUEST)
				.build();
		
		assertEquals(ID, 				inbound.getId());
		assertEquals(EMAIL_ALERT, 		inbound.hasEmailAlert());
		assertEquals(SEARCH_NAME, 		inbound.getSearchName());
		assertEquals(SEARCH_REQUEST, 	inbound.getSearchRequest());
		
		SavedCandidateSearch search = SavedCandidateSearchRequestUpdateAPIInbound.toDomain(inbound, ID, USER_ID);
		
		assertEquals(ID, 				search.getId());
		assertEquals(USER_ID, 			search.getUserId());
		assertEquals(EMAIL_ALERT, 		search.hasEmailAlert());
		assertEquals(SEARCH_NAME, 		search.getSearchName());
		assertEquals(SEARCH_REQUEST, 	search.getSearchRequest());
		
	}
	
}
