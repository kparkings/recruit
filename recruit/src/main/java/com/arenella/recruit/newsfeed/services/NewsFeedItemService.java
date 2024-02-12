package com.arenella.recruit.newsfeed.services;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.newsfeed.beans.NewsFeedItem;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemFilters;
import com.arenella.recruit.newsfeed.beans.NewsFeedUserView;

/**
* Defines services for NewsFeedItems
* @author K Parkings
*/
public interface NewsFeedItemService {

	/**
	* Adds a NewsFeedItem
	* @param newsFeedItem - Item to add
	*/
	public void addNewsFeedItem(NewsFeedItem newsFeedItem);
	
	/**
	* Returns NewsFeedItems macthing the filters
	* @param filters - Filter options
	* @return Matching Items
	*/
	public Set<NewsFeedItem> fetchNewsFeedItems(NewsFeedItemFilters filters);
	
	/**
	* Deletes the NewsFeedItem matching the id
	* @param id - Unique if of NewsFeedItem
	*/
	public void deleteNewsFeedItem(UUID id);
	
	/**
	* Used to clean u NewsFeedItems when a User has deleted their account. In this case
	* any NewsFeedItem matching the Id will be deleted
	* @param id - UniqueId of the User referenced in NewsFeedItems
	*/
	public void deleteAllNewsFeedItemsForReferencedUserId(String id);
	
	/**
	* Adds/Updates a Users record showing the last time they viewed the newsfeed
	* @param newsFeedUserView - Users last view
	*/
	public void saveNewsFeedUserView(Principal principal);
	
	/**
	* Returns information relating to the last time the User viewed
	* the Newsfedd
	* @param userId - Unique id of the User
	* @return Information relating to the last time the User viewed
	*/
	public NewsFeedUserView getNewsFeedUserView(String userId);
	
	/**
	* Deletes the information about when a Use last viewed the Newsfeed
	* @param userId - Unique id of the User
	*/
	void deleteNewsFeedUserView(String userId);

}