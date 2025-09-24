package com.arenella.recruit.newsfeed.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.newsfeed.beans.NewsFeedItem.NEWSFEED_ITEM_TYPE;

/**
* Unit tests for the NewsFeedItem class
* @author K Parkings
*/
class NewsFeedItemTest {

	private static final UUID 					ID 				= UUID.randomUUID();
	private static final LocalDateTime 			CREATED 		= LocalDateTime.of(2024, 1, 31, 23, 06);
	private static final NEWSFEED_ITEM_TYPE 	ITEM_TYPE 		= NEWSFEED_ITEM_TYPE.CANDIDATE_BECAME_AVAILABLE;
	private static final Set<NewsFeedItemLine> 	LINES 			= new LinkedHashSet<>();
	
	/**
	* Tests construction via Builder 
	*/
	@Test
	void testBuilder() {
		
		NewsFeedItem item = NewsFeedItem.builder().id(ID).created(CREATED).itemType(ITEM_TYPE).lines(LINES).build();
		
		assertEquals(ID, 		item.getId());
		assertEquals(CREATED, 	item.getCreated());
		assertEquals(ITEM_TYPE, item.getItemType());
		assertEquals(LINES, 	item.getLines());
	}
	
}
