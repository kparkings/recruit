package com.arenella.recruit.newsfeed.beans;

import java.time.LocalDateTime;

/**
* Information about when a User viewed the NewsFeed 
* @author K Parkings
*/
public class NewsFeedUserView {

	private String 			userId;
	private LocalDateTime 	lastViewed;
	
	/**
	* Constructor
	* @param userId		 - Id of user that viewed the NewsFeed
	* @param lastViewed	 - When the User viewed the NewsFeed
	*/
	public NewsFeedUserView(String userId, LocalDateTime lastViewed) {
		this.userId 		= userId;
		this.lastViewed 	= lastViewed;
	}
	
	/**
	* Returns the id of the User who viewed the NewsFeed
	* @return - Id of User
	*/
	public String getUserId() {
		return this.userId;
	}
	
	/**
	* Returns when the User last viewed the NewsFeed
	* @return when the User last viewed the NewsFeed
	*/
	public LocalDateTime getLastViewed() {
		return this.lastViewed;
	}
	
}