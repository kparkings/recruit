package com.arenella.recruit.newsfeed.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.newsfeed.beans.NewsFeedUserView;

/**
* Unit tests for the NewsFeedUserView class
* @author K Parkings
*/
public class NewsFeedUserViewEntityTest {

	private static final String 			USER_ID = "kparkings";
	private static final LocalDateTime 		LAST_VIEWED = LocalDateTime.of(2024, 2, 7, 0, 4, 5, 4);
	
	/**
	* Tests construction
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
	
		NewsFeedUserViewEntity viewEvent = new NewsFeedUserViewEntity(USER_ID, LAST_VIEWED);
		
		assertEquals(USER_ID, 		viewEvent.getUserId());
		assertEquals(LAST_VIEWED, 	viewEvent.getLastViewed());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	* @throws Exception
	*/
	@Test
	public void testConvertFromEntity() throws Exception{
	
		NewsFeedUserViewEntity entity = new NewsFeedUserViewEntity(USER_ID, LAST_VIEWED);
		
		assertEquals(USER_ID, 		entity.getUserId());
		assertEquals(LAST_VIEWED, 	entity.getLastViewed());
		
		NewsFeedUserView view = NewsFeedUserViewEntity.convertFromEntity(entity);
		
		assertEquals(USER_ID, 		view.getUserId());
		assertEquals(LAST_VIEWED, 	view.getLastViewed());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	* @throws Exception
	*/
	@Test
	public void testConvertToEntity() throws Exception{
		
		NewsFeedUserView view = new NewsFeedUserView(USER_ID, LAST_VIEWED);
		
		assertEquals(USER_ID, 		view.getUserId());
		assertEquals(LAST_VIEWED, 	view.getLastViewed());
		
		NewsFeedUserViewEntity entity = NewsFeedUserViewEntity.convertToEntity(view);
		
		assertEquals(USER_ID, 		entity.getUserId());
		assertEquals(LAST_VIEWED, 	entity.getLastViewed());
		
	}
	
}