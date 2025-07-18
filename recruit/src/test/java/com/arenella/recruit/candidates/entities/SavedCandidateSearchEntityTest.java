package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.SavedCandidateSearch;
import com.fasterxml.jackson.databind.JsonNode;

/**
* Unit tests for the SavedCandidateSearchEntity class
*/
class SavedCandidateSearchEntityTest {

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
	
		SavedCandidateSearchEntity search = SavedCandidateSearchEntity
				.builder()
					.id(ID)
					.userId(USER_ID)
					.emailAlert(EMAIL_ALERT)
					.searchName(SEARCH_NAME)
					.searchRequest(SEARCH_REQUEST)
				.build();
		
		assertEquals(ID, 				search.getId());
		assertEquals(USER_ID, 			search.getUserId());
		assertEquals(EMAIL_ALERT, 		search.hasEmailAlert());
		assertEquals(SEARCH_NAME, 		search.getSearchName());
		assertEquals(SEARCH_REQUEST, 	search.getSearchRequest());
		
	}
	
	/**
	* Tests conversion from Entity to Domain 
	* representation
	*/
	@Test
	void testFromEntity() {
		
		SavedCandidateSearchEntity entity = SavedCandidateSearchEntity
				.builder()
					.id(ID)
					.userId(USER_ID)
					.emailAlert(EMAIL_ALERT)
					.searchName(SEARCH_NAME)
					.searchRequest(SEARCH_REQUEST)
				.build();
		
		assertEquals(ID, 				entity.getId());
		assertEquals(USER_ID, 			entity.getUserId());
		assertEquals(EMAIL_ALERT, 		entity.hasEmailAlert());
		assertEquals(SEARCH_NAME, 		entity.getSearchName());
		assertEquals(SEARCH_REQUEST, 	entity.getSearchRequest());
		
		SavedCandidateSearch search = SavedCandidateSearchEntity.fromEntity(entity);
		
		assertEquals(ID, 				search.getId());
		assertEquals(USER_ID, 			search.getUserId());
		assertEquals(EMAIL_ALERT, 		search.hasEmailAlert());
		assertEquals(SEARCH_NAME, 		search.getSearchName());
		assertEquals(SEARCH_REQUEST, 	search.getSearchRequest());
		
	}
	
	/**
	* Tests conversion from Domain to Entity 
	* representation
	*/
	@Test
	void testToEntity() {
		
		SavedCandidateSearch search = SavedCandidateSearch
				.builder()
					.id(ID)
					.userId(USER_ID)
					.emailAlert(EMAIL_ALERT)
					.searchName(SEARCH_NAME)
					.searchRequest(SEARCH_REQUEST)
				.build();
		
		assertEquals(ID, 				search.getId());
		assertEquals(USER_ID, 			search.getUserId());
		assertEquals(EMAIL_ALERT, 		search.hasEmailAlert());
		assertEquals(SEARCH_NAME, 		search.getSearchName());
		assertEquals(SEARCH_REQUEST, 	search.getSearchRequest());
		
		SavedCandidateSearchEntity entity = SavedCandidateSearchEntity.toEntity(search);
		
		assertEquals(ID, 				entity.getId());
		assertEquals(USER_ID, 			entity.getUserId());
		assertEquals(EMAIL_ALERT, 		entity.hasEmailAlert());
		assertEquals(SEARCH_NAME, 		entity.getSearchName());
		assertEquals(SEARCH_REQUEST, 	entity.getSearchRequest());
		
	}
	
}