package com.arenella.recruit.newsfeed.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the NewsFeedItemFilters class
* @author K Parkings
*/
public class NewsFeedItemFiltersTest {

	private static final Integer								MAX_RESULTS = 40;
	private static final Set<NewsFeedItem.NEWSFEED_ITEM_TYPE> 	types 		= Set.of(NewsFeedItem.NEWSFEED_ITEM_TYPE.CANDIDATE_BECAME_AVAILABLE, 
																					 NewsFeedItem.NEWSFEED_ITEM_TYPE.CANDIDATE_ADDED);
	private static final LocalDateTime							BEFORE		= LocalDateTime.of(2024, 02, 01, 23, 46);
	
	/**
	* Test construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		NewsFeedItemFilters filters = NewsFeedItemFilters.builder().maxResults(MAX_RESULTS).types(types).createdBefore(BEFORE).build();
		
		assertEquals(MAX_RESULTS, 	filters.getMaxResults().get());
		assertEquals(BEFORE, 		filters.getCreatedBefore().get());
		
		filters.getTypes().stream().filter(t -> t == NewsFeedItem.NEWSFEED_ITEM_TYPE.CANDIDATE_BECAME_AVAILABLE).findAny().orElseThrow();
		filters.getTypes().stream().filter(t -> t == NewsFeedItem.NEWSFEED_ITEM_TYPE.CANDIDATE_ADDED).findAny().orElseThrow();
		
	}

	/**
	* Test construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testBuilder_defaults() throws Exception{
		
		NewsFeedItemFilters filters = NewsFeedItemFilters.builder().build();
		
		assertTrue(filters.getMaxResults().isEmpty());
		assertTrue(filters.getTypes().isEmpty());
		assertTrue(filters.getCreatedBefore().isEmpty());
		
	}

	/**
	* Test Setters
	* @throws Exception
	*/
	@Test
	public void testSetters() throws Exception{
		
		NewsFeedItemFilters filters = NewsFeedItemFilters.builder().build();
		
		filters.setCreateBefore(BEFORE);
		
		assertEquals(BEFORE, filters.getCreatedBefore().get());
		
	}
}
