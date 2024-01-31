package com.arenella.recruit.newsfeed.beans;

import java.util.Optional;
import java.util.UUID;

/**
* Individual line making up a NewsFeedItem
* @author K Parkings
*/
public class NewsFeedItemLine {

	public enum NEWS_FEED_ITEM_LINE_TYPE {TEXT, INTERNAL_URL, JPEG}
	
	private UUID 						id;
	private UUID 						newsItemId;
	private NEWS_FEED_ITEM_LINE_TYPE 	type;
	private String 						text;
	private String 						url;
	private byte[] 						image;
	
	/**
	* Constructor based upon a Builder
	* @param builder
	*/
	public NewsFeedItemLine(NewsFeedItemLineBuilder builder) {
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
	public static NewsFeedItemLineBuilder builder() {
		return new NewsFeedItemLineBuilder();
	}
	
	/**
	* Builder for the Class
	* @author K Parkings
	*/
	public static class NewsFeedItemLineBuilder{
		
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
		public NewsFeedItemLineBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Unique id of the Item the line belongs to
		* @param newsItemId - id of parent
		* @return Builder
		*/
		public NewsFeedItemLineBuilder newsItemId(UUID newsItemId) {
			this.newsItemId = newsItemId;
			return this;
		}
		
		/**
		* Returns the type of the line
		* @param type - contents type of the line
		* @return Builder
		*/
		public NewsFeedItemLineBuilder type(NEWS_FEED_ITEM_LINE_TYPE type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the text value if present
		* @param text - content
		* @return Builder
		*/
		public NewsFeedItemLineBuilder text(String text) {
			this.text = text;
			return this;
		}
		
		/**
		* Sets the url value if present
		* @param url - content
		* @return Builder
		*/
		public NewsFeedItemLineBuilder url(String url) {
			this.url = url;
			return this;
		}
		
		/**
		* Sets the image bytes if present
		* @param image - content
		* @return Builder
		*/
		public NewsFeedItemLineBuilder image(byte[] image) {
			this.image = image;
			return this;
		}
		
		/**
		* Returns an Instance initialized with the values in the 
		* Builder
		* @return Initialized instance
		*/
		public NewsFeedItemLine build() {
			return new NewsFeedItemLine(this);
		}
		
	}
	
}