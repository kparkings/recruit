package com.arenella.recruit.newsfeed.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.newsfeed.beans.NewsFeedUserView;

/**
* Represents when a User last viewed the NewsFeed
* @author K Parkings
*/
@Entity
@Table(schema="newsfeed", name="news_feed_user_views")
public class NewsFeedUserViewEntity {

	@Id
	@Column(name="user_id")
	private String 			userId;
	
	@Column(name="last_viewed_newsfeed")
	private LocalDateTime 	lastViewed;
	
	/**
	* Default Constructor 
	*/
	public NewsFeedUserViewEntity() {
		//Hibernate
	}
	/**
	* Constructor
	* @param userId		 - Id of user that viewed the NewsFeed
	* @param lastViewed	 - When the User viewed the NewsFeed
	*/
	public NewsFeedUserViewEntity(String userId, LocalDateTime lastViewed) {
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
	* Converts from Entity representation to Domain representation
	* @param entity - Entity representation
	* @return Domain representation
	*/
	public static NewsFeedUserView convertFromEntity(NewsFeedUserViewEntity entity){
		return new NewsFeedUserView(entity.getUserId(), entity.getLastViewed());
	}

	/**
	* Converts from Domain representation to Entity representation
	* @param event - Domain representation
	* @return Entity representation
	*/
	public static NewsFeedUserViewEntity convertToEntity(NewsFeedUserView event){
		return new NewsFeedUserViewEntity(event.getUserId(), event.getLastViewed());
	}
	
}