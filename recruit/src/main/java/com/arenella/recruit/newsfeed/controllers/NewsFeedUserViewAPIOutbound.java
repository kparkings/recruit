package com.arenella.recruit.newsfeed.controllers;

import java.time.LocalDateTime;

import com.arenella.recruit.newsfeed.beans.NewsFeedUserView;

/**
* API Outbound representation of NewsFeedUserView
* @author K Parkings
*/
public class NewsFeedUserViewAPIOutbound {

	private String 			userId;
	private LocalDateTime 	lastViewed;
	
	/**
	* Constructor
	* @param userId		 - Id of user that viewed the NewsFeed
	* @param lastViewed	 - When the User viewed the NewsFeed
	*/
	public NewsFeedUserViewAPIOutbound(String userId, LocalDateTime lastViewed) {
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
	
	/**
	* Converts from domain representation to APIOutbound representation
	* @param view - Domain representation
	* @return APIOutboud representation
	*/
	public static NewsFeedUserViewAPIOutbound convertFromDomain(NewsFeedUserView view){
		return new NewsFeedUserViewAPIOutbound(view.getUserId(), view.getLastViewed());
	}
	
}
