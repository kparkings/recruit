package com.arenella.recruit.newsfeed.beans;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
* An Object as being something that can be displayed
* in a newsfeed
* @author K Parkings
*/
public class NewsFeedItem {
	
	public enum NEWSFEED_ITEM_TYPE {CANDIDATE_CV_UPDATED, 
									CANDIDATE_PROFILE_UPDATED, 
									TODAYS_CANDIDATES, 
									CANDIDATE_ADDED, 
									CANDIDATE_DELETED,
									CANDIDATE_BECAME_UNAVAILABLE, 
									CANDIDATE_BECAME_AVAILABLE,
									USER_POST}
	
	private UUID 					id;
	private String 					referencedUserId;
	private LocalDateTime 			created;
	private NEWSFEED_ITEM_TYPE 		itemType;
	private Set<NewsFeedItemLine> 	lines 					= new LinkedHashSet<>();
	
	/**
	* Constructor based upon a builder
	* @param builder
	*/
	public NewsFeedItem(NewsFeedItemBuilder builder) {
		this.id 					= builder.id;
		this.referencedUserId 		= builder.referencedUserId;
		this.created 				= builder.created;
		this.itemType 				= builder.itemType;
		this.lines.clear();
		this.lines.addAll(builder.lines);
	}
	
	/**
	* Returns the unique Id of the NewsFeedItem
	* @return
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns the unique Id of the User the NewsItem related to 
	* where the NewsItem related to a User
	* @return userId
	*/
	public Optional<String> getReferencedUserId(){
		return Optional.ofNullable(this.referencedUserId);
	}
	
	/**
	* Returns the date/time that the NewsItem was created
	* @return
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}

	/**
	* Returns the Type/Category of the NewsItem so it can be 
	* grouped with similar types of Items if desired
	* @return
	*/
	public NEWSFEED_ITEM_TYPE getItemType() {
		return this.itemType;
	}
	
	/**
	* Returns the lines making up the NewsFeedItem
	* @return lines
	*/
	public Set<NewsFeedItemLine> getLines(){
		return this.lines;
	}
	
	/**
	* Returns a builder for the class
	* @return Builder
	*/
	public static NewsFeedItemBuilder builder() {
		return new NewsFeedItemBuilder();
	}
	
	/**
	* Builder for the class
	* @author K Parkings
	*/
	public static class NewsFeedItemBuilder{
		
		private UUID 					id;
		private String 					referencedUserId;
		private LocalDateTime 			created;
		private NEWSFEED_ITEM_TYPE 		itemType;
		private Set<NewsFeedItemLine> 	lines 			= new LinkedHashSet<>();
		
		/**
		* Sets the unique Id
		* @param id - Unique Id
		* @return Builder
		*/
		public NewsFeedItemBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the referenced users Id. This is when for example the item related to a 
		* Candidate or Recruiter. The id is the Candidate/Recruiter id
		* @param referencedUserId - Unique id of User
		* @return Builder
		*/
		public NewsFeedItemBuilder referencedUserId(String referencedUserId) {
			this.referencedUserId = referencedUserId;
			return this;
		}
		
		/**
		* Sets creation date/time
		* @param created - when it was created
		* @return Builder
		*/
		public NewsFeedItemBuilder	created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the type so similar items can be grouped
		* @param itemType - type of the item
		* @return Builder
		*/
		public NewsFeedItemBuilder itemType(NEWSFEED_ITEM_TYPE itemType) {
			this.itemType = itemType;
			return this;
		}
		
		/**
		* Sets the lines that make up the post
		* @param lines - lines of content
		* @return Builder
		*/
		public NewsFeedItemBuilder lines(Set<NewsFeedItemLine> lines) {
			this.lines.clear();
			this.lines.addAll(lines);
			return this;
		}
		
		/**
		* Returns a instance initialized with the values in the Builder
		* @return Initialized instance
		*/
		public NewsFeedItem build() {
			return new NewsFeedItem(this);
		}
		
	}
	
}