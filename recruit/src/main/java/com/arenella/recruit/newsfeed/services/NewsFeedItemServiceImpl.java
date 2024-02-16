package com.arenella.recruit.newsfeed.services;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.newsfeed.beans.NewsFeedItem;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemFilters;
import com.arenella.recruit.newsfeed.beans.NewsFeedUserView;
import com.arenella.recruit.newsfeed.dao.NewsFeedItemDao;
import com.arenella.recruit.newsfeed.dao.NewsFeedUserViewsDao;

/**
* Services for interacting with NewsFeedItems
* @author K Parkings
*/
@Service
public class NewsFeedItemServiceImpl implements NewsFeedItemService{

	@Autowired
	private NewsFeedItemDao 		newsFeedItemDao;
	
	@Autowired
	private NewsFeedUserViewsDao 	newsFeedItemViewDao;
	
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
	/**
	* Refer to the NewsFeedItemService interface for details 
	*/
	@Override
	public void saveNewsFeedUserView(Principal principal) {
		this.newsFeedItemViewDao.saveNewsFeedUserView(new NewsFeedUserView(principal.getName(), LocalDateTime.now()));
	}

	/**
	* Refer to the NewsFeedItemService interface for details 
	*/
	@Override
	public NewsFeedUserView getNewsFeedUserView(String userId) {
		
		
		if (!this.newsFeedItemViewDao.existsById(userId)) {
			this.newsFeedItemViewDao.saveNewsFeedUserView(new NewsFeedUserView(userId, LocalDateTime.now()));
		}
		
		return this.newsFeedItemViewDao.getNewsFeedUserView(userId).get();
	}

	/**
	* Refer to the NewsFeedItemService interface for details 
	*/
	@Override
	public void deleteNewsFeedUserView(String userId) {
		
		if (this.newsFeedItemViewDao.existsById(userId)) {
			this.newsFeedItemViewDao.deleteNewsFeedUserView(userId);
		}
		
		
		
	}
	
}