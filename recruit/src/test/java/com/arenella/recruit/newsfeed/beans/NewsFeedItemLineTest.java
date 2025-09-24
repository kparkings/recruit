package com.arenella.recruit.newsfeed.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.newsfeed.beans.NewsFeedItemLine.NEWS_FEED_ITEM_LINE_TYPE;

/**
* Unit tests for the NewsFeedItemLine class
* @author K Parkings
*/
class NewsFeedItemLineTest {

	private static final UUID 						ID				= UUID.randomUUID();
	private static final UUID 						NEWS_ITEM_ID	= UUID.randomUUID();
	private static final NEWS_FEED_ITEM_LINE_TYPE 	TYPE			= NEWS_FEED_ITEM_LINE_TYPE.TEXT;
	private static final String 					TEXT			= "text";
	private static final String 					URL				= "/candidate/4045";
	private static final byte[] 					IMAGE			= new byte[] {};
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		NewsFeedItemLine line = NewsFeedItemLine
				.builder()
					.id(ID)
					.newsItemId(NEWS_ITEM_ID)
					.type(TYPE)
					.text(TEXT)
					.url(URL)
					.image(IMAGE)
				.build();
		
		assertEquals(ID, 			line.getId());
		assertEquals(NEWS_ITEM_ID, 	line.getNewsItemId());
		assertEquals(TYPE, 			line.getType());
		assertEquals(TEXT, 			line.getText().get());
		assertEquals(URL, 			line.getUrl().get());
		assertEquals(IMAGE, 		line.getImage().get());
	}
	
	/**
	* Tests construction with defaults to ensure
	* Optional is returned
	* @throws Exception
	*/
	@Test
	void testBuilder_defaults() {
		
		NewsFeedItemLine line = NewsFeedItemLine
				.builder()
					.id(ID)
					.newsItemId(NEWS_ITEM_ID)
					.type(TYPE)
					
				.build();
		
		assertTrue(line.getText().isEmpty());
		assertTrue(line.getUrl().isEmpty());
		assertTrue(line.getImage().isEmpty());
		
	}
	
}