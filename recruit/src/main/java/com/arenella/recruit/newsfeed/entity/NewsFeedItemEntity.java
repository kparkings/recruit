package com.arenella.recruit.newsfeed.entity;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.arenella.recruit.newsfeed.beans.NewsFeedItem;
import com.arenella.recruit.newsfeed.beans.NewsFeedItem.NEWSFEED_ITEM_TYPE;

/**
* An Object as being something that can be displayed
* in a newsfeed
* @author K Parkings
*/
@Entity
@Table(schema="newsfeed", name="news_feed_items")
public class NewsFeedItemEntity {
	
	@Id
	@Column(name="id")
	private UUID id;
	
	@Column(name="created")
	private LocalDateTime created;
	
	@Column(name="references_user_id")
	private String referencedUserId;
	
	@Column(name="item_type")
	@Enumerated(EnumType.STRING)
	private NEWSFEED_ITEM_TYPE itemType;
	
	@OneToMany(mappedBy = "newsItemId", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	private Set<NewsFeedItemLineEntity> lines = new LinkedHashSet<>();
	
	/**
	* Constructor 
	*/
	public NewsFeedItemEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a builder
	* @param builder
	*/
	public NewsFeedItemEntity(NewsFeedItemEntityBuilder builder) {
		this.id 				= builder.id;
		this.referencedUserId 	= builder.referencedUserId;
		this.created 			= builder.created;
		this.itemType 			= builder.itemType;
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
	public Set<NewsFeedItemLineEntity> getLines(){
		return this.lines;
	}
	
	/**
	* Returns a builder for the class
	* @return Builder
	*/
	public static NewsFeedItemEntityBuilder builder() {
		return new NewsFeedItemEntityBuilder();
	}
	
	/**
	* Builder for the class
	* @author K Parkings
	*/
	public static class NewsFeedItemEntityBuilder{
		
		private UUID 					id;
		private String					referencedUserId;
		private LocalDateTime 			created;
		private NEWSFEED_ITEM_TYPE 		itemType;
		private Set<NewsFeedItemLineEntity> 	lines 			= new LinkedHashSet<>();
		
		/**
		* Sets the unique Id
		* @param id - Unique Id
		* @return Builder
		*/
		public NewsFeedItemEntityBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the referenced users Id. This is when for example the item related to a 
		* Candidate or Recruiter. The id is the Candidate/Recruiter id
		* @param referencedUserId - Unique id of User
		* @return Builder
		*/
		public NewsFeedItemEntityBuilder referencedUserId(String referencedUserId) {
			this.referencedUserId = referencedUserId;
			return this;
		}
		
		/**
		* Sets creation date/time
		* @param created - when it was created
		* @return Builder
		*/
		public NewsFeedItemEntityBuilder	created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the type so similar items can be grouped
		* @param itemType - type of the item
		* @return Builder
		*/
		public NewsFeedItemEntityBuilder itemType(NEWSFEED_ITEM_TYPE itemType) {
			this.itemType = itemType;
			return this;
		}
		
		/**
		* Sets the lines that make up the post
		* @param lines - lines of content
		* @return Builder
		*/
		public NewsFeedItemEntityBuilder lines(Set<NewsFeedItemLineEntity> lines) {
			this.lines.clear();
			this.lines.addAll(lines);
			return this;
		}
		
		/**
		* Returns a instance initialized with the values in the Builder
		* @return Initialized instance
		*/
		public NewsFeedItemEntity build() {
			return new NewsFeedItemEntity(this);
		}
		
	}
	
	/**
	* Converts form Entity to Domain representation
	* @param entity - Entity representation
	* @return Domain representation
	*/
	public static NewsFeedItem convertFromEntity(NewsFeedItemEntity entity) {
		return NewsFeedItem
				.builder()
					.id(entity.getId())
					.referencedUserId(entity.getReferencedUserId().isEmpty() ? null : entity.getReferencedUserId().get())
					.created(entity.getCreated())
					.itemType(entity.getItemType())
					.lines(entity.getLines().stream().map( NewsFeedItemLineEntity::convertFromEntity).collect(Collectors.toCollection(LinkedHashSet::new)))
				.build();
	}

	/**
	* Converts form Domain to Entity representation
	* @param domain - Domain representation
	* @return entity representation
	*/
	public static NewsFeedItemEntity convertToEntity(NewsFeedItem item) {
		return NewsFeedItemEntity
				.builder()
					.id(item.getId())
					.referencedUserId(item.getReferencedUserId().isEmpty() ? null : item.getReferencedUserId().get())
					.created(item.getCreated())
					.itemType(item.getItemType())
					.lines(item.getLines().stream().map(NewsFeedItemLineEntity::convertToEntity).collect(Collectors.toCollection(LinkedHashSet::new)))
				.build();
	}

}