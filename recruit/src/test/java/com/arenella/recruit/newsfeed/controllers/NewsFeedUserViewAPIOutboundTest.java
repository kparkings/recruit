package com.arenella.recruit.newsfeed.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.newsfeed.beans.NewsFeedUserView;

/** 
* Unit tests for the NewsFeedUserViewAPIOutbound class
* @author K Parkings
*/
public class NewsFeedUserViewAPIOutboundTest {

	private static final String 			USER_ID = "kparkings";
	private static final LocalDateTime 		LAST_VIEWED = LocalDateTime.of(2024, 2, 7, 0, 4, 5, 4);
	
	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
	
		NewsFeedUserViewAPIOutbound view = new NewsFeedUserViewAPIOutbound(USER_ID, LAST_VIEWED);
		
		assertEquals(USER_ID, 		view.getUserId());
		assertEquals(LAST_VIEWED, 	view.getLastViewed());
		
	}
	
	/**
	* Tests conversion from domain to APIOutbound representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromDomain() throws Exception{
	
		NewsFeedUserView view = new NewsFeedUserView(USER_ID, LAST_VIEWED);
		
		assertEquals(USER_ID, 		view.getUserId());
		assertEquals(LAST_VIEWED, 	view.getLastViewed());
		
		NewsFeedUserViewAPIOutbound apiOutbound = NewsFeedUserViewAPIOutbound.convertFromDomain(view);
		
		assertEquals(USER_ID, 		apiOutbound.getUserId());
		assertEquals(LAST_VIEWED, 	apiOutbound.getLastViewed());
		
	}
	
}