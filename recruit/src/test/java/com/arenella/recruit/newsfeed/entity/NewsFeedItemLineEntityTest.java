package com.arenella.recruit.newsfeed.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.newsfeed.beans.NewsFeedItemLine;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemLine.NEWS_FEED_ITEM_LINE_TYPE;

/**
* Unit tests for the NewsFeedItemLine class
* @author K Parkings
*/
class NewsFeedItemLineEntityTest {

	private static final UUID 						ID				= UUID.randomUUID();
	private static final UUID 						NEWS_ITEM_ID	= UUID.randomUUID();
	private static final NEWS_FEED_ITEM_LINE_TYPE 	TYPE			= NEWS_FEED_ITEM_LINE_TYPE.TEXT;
	private static final String 					TEXT			= "text";
	private static final String 					URL				= "/candidate/4045";
	private static final byte[] 					IMAGE			= new byte[] {};
	private static final int 						ORDER			= 2;
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		NewsFeedItemLineEntity line = NewsFeedItemLineEntity
				.builder()
					.id(ID)
					.newsItemId(NEWS_ITEM_ID)
					.type(TYPE)
					.text(TEXT)
					.url(URL)
					.image(IMAGE)
					.order(ORDER)
				.build();
		
		assertEquals(ID, 			line.getId());
		assertEquals(NEWS_ITEM_ID, 	line.getNewsItemId());
		assertEquals(TYPE, 			line.getType());
		assertEquals(TEXT, 			line.getText().get());
		assertEquals(URL, 			line.getUrl().get());
		assertEquals(IMAGE, 		line.getImage().get());
		assertEquals(ORDER, 		line.getOrder());
	}
	
	/**
	* Tests construction with defaults to ensure
	* Optional is returned
	* @throws Exception
	*/
	@Test
	void testBuilder_defaults() {
		
		NewsFeedItemLineEntity line = NewsFeedItemLineEntity
				.builder()
					.id(ID)
					.newsItemId(NEWS_ITEM_ID)
					.type(TYPE)
				.build();
		
		assertTrue(line.getText().isEmpty());
		assertTrue(line.getUrl().isEmpty());
		assertTrue(line.getImage().isEmpty());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* @throws Exception
	*/
	@Test
	void testConvertFromEntity() {
		
		NewsFeedItemLineEntity entity = NewsFeedItemLineEntity
				.builder()
					.id(ID)
					.newsItemId(NEWS_ITEM_ID)
					.type(TYPE)
					.text(TEXT)
					.url(URL)
					.image(IMAGE)
					.order(ORDER)
				.build();
		
		NewsFeedItemLine line = NewsFeedItemLineEntity.convertFromEntity(entity);
		
		assertEquals(ID, 			line.getId());
		assertEquals(NEWS_ITEM_ID, 	line.getNewsItemId());
		assertEquals(TYPE, 			line.getType());
		assertEquals(TEXT, 			line.getText().get());
		assertEquals(URL, 			line.getUrl().get());
		assertEquals(IMAGE, 		line.getImage().get());
		assertEquals(ORDER, 		line.getOrder());
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* @throws Exception
	*/
	@Test
	void testConvertFromEntity_default_optional_value() {
		
		NewsFeedItemLineEntity entity = NewsFeedItemLineEntity
				.builder()
					.id(ID)
					.newsItemId(NEWS_ITEM_ID)
					.type(TYPE)
				.build();
		
		NewsFeedItemLine line = NewsFeedItemLineEntity.convertFromEntity(entity);
		
		assertEquals(ID, 			line.getId());
		assertEquals(NEWS_ITEM_ID, 	line.getNewsItemId());
		assertEquals(TYPE, 			line.getType());
		assertTrue(line.getText().isEmpty());
		assertTrue(line.getUrl().isEmpty());
		assertTrue(line.getImage().isEmpty());
		
	}
	
	/**
	* Tests conversion to Entity from Domain representation
	* @throws Exception
	*/
	@Test
	void testConvertToEntity() {
		
		NewsFeedItemLine line = NewsFeedItemLine
				.builder()
					.id(ID)
					.newsItemId(NEWS_ITEM_ID)
					.type(TYPE)
					.text(TEXT)
					.url(URL)
					.image(IMAGE)
					.order(ORDER)
				.build();
		
		NewsFeedItemLineEntity entity = NewsFeedItemLineEntity.convertToEntity(line);
		
		assertEquals(ID, 			entity.getId());
		assertEquals(NEWS_ITEM_ID, 	entity.getNewsItemId());
		assertEquals(TYPE, 			entity.getType());
		assertEquals(TEXT, 			entity.getText().get());
		assertEquals(URL, 			entity.getUrl().get());
		assertEquals(IMAGE, 		entity.getImage().get());
		assertEquals(ORDER, 		entity.getOrder());
	}
	
	/**
	* Tests conversion to Entity from Domain representation
	* @throws Exception
	*/
	@Test
	void testConvertToEntity_default_optional_value() {
		
		NewsFeedItemLineEntity line = NewsFeedItemLineEntity
				.builder()
					.id(ID)
					.newsItemId(NEWS_ITEM_ID)
					.type(TYPE)
				.build();
		
		NewsFeedItemLine entity = NewsFeedItemLineEntity.convertFromEntity(line);
		
		assertEquals(ID, 			entity.getId());
		assertEquals(NEWS_ITEM_ID, 	entity.getNewsItemId());
		assertEquals(TYPE, 			entity.getType());
		assertTrue(entity.getText().isEmpty());
		assertTrue(entity.getUrl().isEmpty());
		assertTrue(entity.getImage().isEmpty());
		
	}
	
}