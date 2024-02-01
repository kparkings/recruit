package com.arenella.recruit.newsfeed.beans;

import java.util.UUID;
import java.util.Set;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

/**
* Filters for fetching NewsFeedItems
* @author K Parkings
*/
public class NewsFeedItemFilters {

	private UUID 									firstId;
	private Integer									maxResults;
	private Set<NewsFeedItem.NEWSFEED_ITEM_TYPE> 	types = new HashSet<>();
	private LocalDateTime							createBefore;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization information
	*/
	public NewsFeedItemFilters(NewsFeedItemFiltersBuilder builder) {
		this.firstId 		= builder.firstId;
		this.maxResults 	= builder.maxResults;
		this.createBefore 	= builder.createBefore;
		this.types.clear();
		this.types.addAll(builder.types);
	}
	
	/**
	* Returns the first Id to be returned. If missing 
	* the newest id will be used as the starting point
	* for the results
	* @return first Id
	*/
	public Optional<UUID> getFirstId(){
		return Optional.ofNullable(this.firstId);
	}
	
	/**
	* Returns the max number of results to return
	* @return max number of results
	*/
	public Optional<Integer> getMaxResults(){
		return Optional.ofNullable(this.maxResults);
	}
	
	/**
	* Returns types to include in filters. 
	* If empty filters on all types
	* @return type filters
	*/
	public Set<NewsFeedItem.NEWSFEED_ITEM_TYPE> getTypes(){
		return this.types;
	}
	
	/**
	* Returns Date/Time the item must have been created 
	* before in order to be included in the results
	* @return before inclusion date/time
	*/
	public Optional<LocalDateTime> getCreatedBefore() {
		return Optional.ofNullable(this.createBefore);
	}
	
	/**
	* Sets the createdBefore Date
	* @param createdBefore
	*/
	public void setCreateBefore(LocalDateTime createdBefore) {
		this.createBefore = createdBefore;
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder
	*/
	public static NewsFeedItemFiltersBuilder builder() {
		return new NewsFeedItemFiltersBuilder();
	}
	
	/**
	* Builder for the class
	* @author K Parkings
	*/
	public static class NewsFeedItemFiltersBuilder{
	
		private UUID 									firstId;
		private Integer									maxResults;
		private Set<NewsFeedItem.NEWSFEED_ITEM_TYPE> 	types = new HashSet<>();
		private LocalDateTime							createBefore;
		
		/**
		* Sets firstId to filter results on. This is the starting point for 
		* the feed of items
		* @param firstId - id of the first item
		* @return Builder
		*/
		public NewsFeedItemFiltersBuilder firstId(UUID firstId) {
			this.firstId = firstId;
			return this;
		}
		
		/**
		* Sets the max number of items to return
		* @param maxResults - Max number of items
		* @return Builder
		*/
		public NewsFeedItemFiltersBuilder maxResults(Integer maxResults) {
			this.maxResults = maxResults;
			return this;
		}
		
		/**
		* Sets the the types to filter on 
		* @param types - Types to filter on
		* @return Builder
		*/
		public NewsFeedItemFiltersBuilder types(Set<NewsFeedItem.NEWSFEED_ITEM_TYPE> types) {
			this.types.clear();
			this.types.addAll(types);
			return this;
		}
		
		/**
		* Sets the date/time before which the items must have been created
		* for them to be included in the results
		* @param createdBefore - before date/time
		* @return builder
		*/
		public NewsFeedItemFiltersBuilder createdBefore(LocalDateTime createdBefore) {
			this.createBefore = createdBefore;
			return this;
		}
		
		/**
		* Returns an initialized instance
		* @return - initialized instance
		*/
		public NewsFeedItemFilters build() {
			return new NewsFeedItemFilters(this);
		}
		
	}
	
}