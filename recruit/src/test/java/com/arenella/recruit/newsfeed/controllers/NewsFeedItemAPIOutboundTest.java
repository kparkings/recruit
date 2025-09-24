package com.arenella.recruit.newsfeed.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.newsfeed.beans.NewsFeedItem;
import com.arenella.recruit.newsfeed.beans.NewsFeedItem.NEWSFEED_ITEM_TYPE;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemLine;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemLine.NEWS_FEED_ITEM_LINE_TYPE;

/**
* Unit tests for the NewsFeedItemAPIOutbound class
* @author K Parkings
*/
class NewsFeedItemAPIOutboundTest {

	private static final LocalDateTime 						CREATED 		= LocalDateTime.of(2024, 2, 3, 17, 14);
	private static final NEWSFEED_ITEM_TYPE 				ITEM_TYPE 		= NEWSFEED_ITEM_TYPE.CANDIDATE_ADDED;
	private static final Set<NewsFeedItemLineAPIOutbound> 	LINES 			= new LinkedHashSet<>();
	
	/**
	* Test construction via Builder
	* @throws Exception
	*/
	@Test
	void testConstruction() {
		
		NewsFeedItemAPIOutbound item = NewsFeedItemAPIOutbound.builder().itemType(ITEM_TYPE).created(CREATED).lines(LINES).build();
		
		assertEquals(CREATED, 		item.getCreated());
		assertEquals(ITEM_TYPE, 	item.getItemType());
		assertEquals(LINES, 		item.getLines());
				
	}
	
	/**
	* Test construction via Builder
	* Tests defensive programming. Empty Set to avoid null
	* @throws Exception
	*/
	@Test
	void testConstruction_defaults() {
		
		NewsFeedItemAPIOutbound item = NewsFeedItemAPIOutbound.builder().build();
		
		assertTrue(item.getLines().isEmpty());
		
	}

	/**
	* Tests conversion
	* @throws Exception
	*/
	@Test
	void testConvertFromDomain() {
		
		final Set<NewsFeedItemLine> 	lines 			= Set.of(NewsFeedItemLine.builder().text("").type(NEWS_FEED_ITEM_LINE_TYPE.TEXT).build(),
																 NewsFeedItemLine.builder().image(new byte[] {}).type(NEWS_FEED_ITEM_LINE_TYPE.TEXT).build(),
																 NewsFeedItemLine.builder().url("").type(NEWS_FEED_ITEM_LINE_TYPE.INTERNAL_URL).build());
		
		NewsFeedItem item = NewsFeedItem.builder().itemType(ITEM_TYPE).created(CREATED).lines(lines).build();
		
		NewsFeedItemAPIOutbound outbound = NewsFeedItemAPIOutbound.convertFromDomain(item);
		
		assertEquals(CREATED, 		outbound.getCreated());
		assertEquals(ITEM_TYPE, 	outbound.getItemType());
		
		assertEquals(lines.size(), outbound.getLines().size());
		
	}
	
}