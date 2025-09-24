package com.arenella.recruit.newsfeed.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the NewsFeedUserView class
* @author K Parkings
*/
class NewsFeedUserViewTest {

	private static final String 		USER_ID 	= "kparkings";
	private static final LocalDateTime 	LAST_VIEWED = LocalDateTime.of(2024, 2, 7, 0, 4, 5, 4);
	
	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	void testConstructor() {
	
		NewsFeedUserView viewEvent = new NewsFeedUserView(USER_ID, LAST_VIEWED);
		
		assertEquals(USER_ID, 		viewEvent.getUserId());
		assertEquals(LAST_VIEWED, 	viewEvent.getLastViewed());
		
	}
	
}
