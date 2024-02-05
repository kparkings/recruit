package com.arenella.recruit.newsfeed.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.newsfeed.beans.NewsFeedItemLine;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemLine.NEWS_FEED_ITEM_LINE_TYPE;

/**
* Unit tests for the NewsFeedItemLineAPIOutbound class
* @author K Parkings
*/
public class NewsFeedItemLineAPIOutboundTest {

	private static final NEWS_FEED_ITEM_LINE_TYPE 	TYPE 	= NEWS_FEED_ITEM_LINE_TYPE.TEXT;
	private static final String 					TEXT	= "text";
	private static final String 					URL		= "/1234";
	private static final byte[] 					IMAGE	= new byte[] {};
	private static final int 						ORDER	= 4;
	
	/**
	* Tests construction via Builder
	* @throws Exception
	*/
	@Test
	public void testConstruction() throws Exception{
		
		NewsFeedItemLineAPIOutbound line = NewsFeedItemLineAPIOutbound.builder().order(ORDER).type(TYPE).text(TEXT).image(IMAGE).url(URL).build();
		
		assertEquals(ORDER, 	line.getOrder());
		assertEquals(TYPE, 	line.getType());
		assertEquals(TEXT, 	line.getText().get());
		assertEquals(URL, 	line.getUrl().get());
		assertEquals(IMAGE, line.getImage().get());
	}
	
	/**
	* Tests construction via Builder with default values
	* @throws Exception
	*/
	@Test
	public void testConstruction_defaults() throws Exception{
		
		NewsFeedItemLineAPIOutbound line = NewsFeedItemLineAPIOutbound.builder().build();
		
		assertTrue(line.getText().isEmpty());
		assertTrue(line.getUrl().isEmpty());
		assertTrue(line.getImage().isEmpty());
		
	}

	/**
	* Tests conversion from Domain to APIOutbound
	* @throws Exception
	*/
	@Test
	public void testConvertFromDomain() throws Exception{
	
		NewsFeedItemLine line = NewsFeedItemLine.builder().order(ORDER).type(TYPE).text(TEXT).image(IMAGE).url(URL).build();
		
		NewsFeedItemLineAPIOutbound outbound = NewsFeedItemLineAPIOutbound.convertFromDomain(line);
		
		assertEquals(ORDER, outbound.getOrder());
		assertEquals(TYPE, 	outbound.getType());
		assertEquals(TEXT, 	outbound.getText().get());
		assertEquals(URL, 	outbound.getUrl().get());
		assertEquals(IMAGE, outbound.getImage().get());
		
	}
	
	/**
	* Tests conversion from Domain to APIOutbound with defaults
	* @throws Exception
	*/
	@Test
	public void testConvertFromDomain_defaults() throws Exception{
	
		NewsFeedItemLine line = NewsFeedItemLine.builder().type(TYPE).build();
		
		NewsFeedItemLineAPIOutbound outbound = NewsFeedItemLineAPIOutbound.convertFromDomain(line);
		
		assertEquals(TYPE, 	outbound.getType());
		
		assertTrue(outbound.getText().isEmpty());
		assertTrue(outbound.getUrl().isEmpty());
		assertTrue(outbound.getImage().isEmpty());
		
	}
	
}