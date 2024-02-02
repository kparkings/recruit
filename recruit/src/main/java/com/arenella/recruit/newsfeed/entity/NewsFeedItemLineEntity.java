package com.arenella.recruit.newsfeed.entity;

import java.util.Optional;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.newsfeed.beans.NewsFeedItemLine;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemLine.NEWS_FEED_ITEM_LINE_TYPE;

/**
* Individual line making up a NewsFeedItem
* @author K Parkings
*/
@Entity
@Table(schema="newsfeed", name="news_feed_item_lines")
public class NewsFeedItemLineEntity {

	@Id
	@Column(name="id")
	private UUID 						id;
	
	@Column(name="newsfeed_item_id")
	private UUID 						newsItemId;
	
	@Column(name="type")
	@Enumerated(EnumType.STRING)
	private NEWS_FEED_ITEM_LINE_TYPE 	type;
	
	@Column(name="text_value")
	private String 						text;
	
	@Column(name="url_value")
	private String 						url;
	
	@Column(name="image_bytes")
	private byte[] 						image;
	
	/**
	* Constructor 
	*/
	public NewsFeedItemLineEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder
	*/
	public NewsFeedItemLineEntity(NewsFeedItemLineEntityBuilder builder) {
		this.id 			= builder.id;
		this.newsItemId 	= builder.newsItemId;
		this.type 			= builder.type;
		this.text 			= builder.text;
		this.url 			= builder.url;
		this.image 			= builder.image;
	}
	
	/**
	* Returns the unique id of the line
	* @return id of the line
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns the unique id of the Item the line
	* belongs to
	* @return parent id
	*/
	public UUID getNewsItemId() {
		return this.newsItemId;
	}
	
	/**
	* Returns the content type to use
	* @return type of content to use
	*/
	public NEWS_FEED_ITEM_LINE_TYPE getType() {
		return this.type;
	}
	
	/**
	* Returns optional Text content
	* @return content
	*/
	public Optional<String> getText() {
		return Optional.ofNullable(this.text);
	}
	
	/**
	* Returns optional URL content
	* @return url content
	*/
	public Optional<String> getUrl() {
		return Optional.ofNullable(this.url);
	}
	
	/**
	* Returns optional image content
	* @return
	*/
	public Optional<byte[]>	getImage() {
		return Optional.ofNullable(this.image);
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder 
	*/
	public static NewsFeedItemLineEntityBuilder builder() {
		return new NewsFeedItemLineEntityBuilder();
	}
	
	/**
	* Builder for the Class
	* @author K Parkings
	*/
	public static class NewsFeedItemLineEntityBuilder{
		
		private UUID 						id;
		private UUID 						newsItemId;
		private NEWS_FEED_ITEM_LINE_TYPE 	type;
		private String 						text;
		private String 						url;
		private byte[] 						image;
		
		/**
		* Sets the unique Id of the line
		* @param id - Id of the line
		* @return Builder
		*/
		public NewsFeedItemLineEntityBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Unique id of the Item the line belongs to
		* @param newsItemId - id of parent
		* @return Builder
		*/
		public NewsFeedItemLineEntityBuilder newsItemId(UUID newsItemId) {
			this.newsItemId = newsItemId;
			return this;
		}
		
		/**
		* Returns the type of the line
		* @param type - contents type of the line
		* @return Builder
		*/
		public NewsFeedItemLineEntityBuilder type(NEWS_FEED_ITEM_LINE_TYPE type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the text value if present
		* @param text - content
		* @return Builder
		*/
		public NewsFeedItemLineEntityBuilder text(String text) {
			this.text = text;
			return this;
		}
		
		/**
		* Sets the url value if present
		* @param url - content
		* @return Builder
		*/
		public NewsFeedItemLineEntityBuilder url(String url) {
			this.url = url;
			return this;
		}
		
		/**
		* Sets the image bytes if present
		* @param image - content
		* @return Builder
		*/
		public NewsFeedItemLineEntityBuilder image(byte[] image) {
			this.image = image;
			return this;
		}
		
		/**
		* Returns an Instance initialized with the values in the 
		* Builder
		* @return Initialized instance
		*/
		public NewsFeedItemLineEntity build() {
			return new NewsFeedItemLineEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity to Domain representation
	* @param entity - Entity to convert
	* @return Domain representation
	*/
	public static NewsFeedItemLine convertFromEntity(NewsFeedItemLineEntity entity) {
		return NewsFeedItemLine
				.builder()
					.id(entity.getId())
					.newsItemId(entity.getNewsItemId())
					.type(entity.getType())
					.text(entity.getText().isPresent() 		? entity.getText().get() 	: null)
					.url(entity.getUrl().isPresent() 		? entity.getUrl().get() 	: null)
					.image(entity.getImage().isPresent()	? entity.getImage().get()	: null)
				.build();
	}

	/**
	* Converts from Domain to Entity representation
	* @param item - Domain representation to convert
	* @return Entity representation
	*/
	public static NewsFeedItemLineEntity convertToEntity(NewsFeedItemLine item) {
		return NewsFeedItemLineEntity
				.builder()
					.id(item.getId())
					.newsItemId(item.getNewsItemId())
					.type(item.getType())
					.text(item.getText().isPresent() 	? item.getText().get() 	: null)
					.url(item.getUrl().isPresent() 		? item.getUrl().get() 	: null)
					.image(item.getImage().isPresent()	? item.getImage().get()	: null)
				.build();
	}
}