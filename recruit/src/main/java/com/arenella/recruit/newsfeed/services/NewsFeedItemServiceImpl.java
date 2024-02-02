package com.arenella.recruit.newsfeed.services;

import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.newsfeed.beans.NewsFeedItem;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemFilters;
import com.arenella.recruit.newsfeed.dao.NewsFeedItemDao;

/**
* Services for interacting with NewsFeedItems
* @author K Parkings
*/
@Service
public class NewsFeedItemServiceImpl implements NewsFeedItemService{

	@Autowired
	private NewsFeedItemDao newsFeedItemDao;
	
	/**
	* Refer to the NewsFeedItemService interface for details 
	*/
	@Override
	public void addNewsFeedItem(NewsFeedItem newsFeedItem) {
		
		if(this.newsFeedItemDao.existsById(newsFeedItem.getId())) {
			throw new IllegalArgumentException("NewsFeedItem already exists " + newsFeedItem.getId());
		}
		
		this.newsFeedItemDao.saveNewsFeedItem(newsFeedItem);
		
	}

	/**
	* Refer to the NewsFeedItemService interface for details 
	*/
	@Override
	public Set<NewsFeedItem> fetchNewsFeedItems(NewsFeedItemFilters filters) {
		return this.newsFeedItemDao.fetchNewsFeedItem(filters);
	}

	/**
	* Refer to the NewsFeedItemService interface for details 
	*/
	@Override
	public void deleteNewsFeedItem(UUID id) {
		this.newsFeedItemDao.deleteById(id);
		
	}

	/**
	* Refer to the NewsFeedItemService interface for details 
	*/
	@Override
	public void deleteAllNewsFeedItemsForReferencedUserId(String id) {
		this.newsFeedItemDao.deleteAllNewsFeedItemsForReferencedUserId(id);
	}
	

}
