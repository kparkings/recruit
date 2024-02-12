package com.arenella.recruit.newsfeed.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.newsfeed.beans.NewsFeedUserView;
import com.arenella.recruit.newsfeed.entity.NewsFeedUserViewEntity;

/**
* Repository for interacting with NewsFeedRecruiterViewEntity
* @author K Parkings
*/
public interface NewsFeedUserViewsDao extends CrudRepository<NewsFeedUserViewEntity, String>{

	/**
	* Adds / Updates record showing when the User last viewed the Newsfeed 
	*/
	public default void saveNewsFeedUserView(NewsFeedUserView newsFeedUserView) {
		this.save(NewsFeedUserViewEntity.convertToEntity(newsFeedUserView));
	}
	
	/**
	* Returns details of when the User last viewed the Newsfeed
	* @param userId - Unique id of the User
	* @return If found the user matching the id
	*/
	public default Optional<NewsFeedUserView> getNewsFeedUserView(String userId) {
	
		Optional<NewsFeedUserViewEntity> result = this.findById(userId);
		
		return result.isEmpty() ? Optional.empty() : Optional.of(NewsFeedUserViewEntity.convertFromEntity(result.get()));
	}
	
	/**
	* Deletes the NewsFeedUserView record for the User specified
	* @param userId - Unique id of the User
	*/
	public default void deleteNewsFeedUserView(String userId) {
		this.deleteById(userId);
	}
	
}
