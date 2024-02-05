package com.arenella.recruit.newsfeed.controllers;

import java.util.Optional;

import com.arenella.recruit.newsfeed.beans.NewsFeedItemLine;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemLine.NEWS_FEED_ITEM_LINE_TYPE;

/**
* Individual line making up a NewsFeedItem
* @author K Parkings
*/
public class NewsFeedItemLineAPIOutbound {

	private NEWS_FEED_ITEM_LINE_TYPE 	type;
	private String 						text;
	private String 						url;
	private byte[] 						image;
	private int							order;
	
	/**
	* Constructor based upon a Builder
	* @param builder
	*/
	public NewsFeedItemLineAPIOutbound(NewsFeedItemLineAPIOutboundBuilder builder) {
		this.type 			= builder.type;
		this.text 			= builder.text;
		this.url 			= builder.url;
		this.image 			= builder.image;
		this.order			= builder.order;
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
	* Returns display order
	* @return order
	*/
	public int	getOrder() {
		return this.order;
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder 
	*/
	public static NewsFeedItemLineAPIOutboundBuilder builder() {
		return new NewsFeedItemLineAPIOutboundBuilder();
	}
	
	/**
	* Builder for the Class
	* @author K Parkings
	*/
	public static class NewsFeedItemLineAPIOutboundBuilder{
		
		private NEWS_FEED_ITEM_LINE_TYPE 	type;
		private String 						text;
		private String 						url;
		private byte[] 						image;
		private int							order;
		
		/**
		* Returns the type of the line
		* @param type - contents type of the line
		* @return Builder
		*/
		public NewsFeedItemLineAPIOutboundBuilder type(NEWS_FEED_ITEM_LINE_TYPE type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the text value if present
		* @param text - content
		* @return Builder
		*/
		public NewsFeedItemLineAPIOutboundBuilder text(String text) {
			this.text = text;
			return this;
		}
		
		/**
		* Sets the url value if present
		* @param url - content
		* @return Builder
		*/
		public NewsFeedItemLineAPIOutboundBuilder url(String url) {
			this.url = url;
			return this;
		}
		
		/**
		* Sets the image bytes if present
		* @param image - content
		* @return Builder
		*/
		public NewsFeedItemLineAPIOutboundBuilder image(byte[] image) {
			this.image = image;
			return this;
		}
		
		/**
		* Sets the display order
		* @param order - display order
		* @return Builder
		*/
		public NewsFeedItemLineAPIOutboundBuilder order(int order) {
			this.order = order;
			return this;
		}
		
		/**
		* Returns an Instance initialized with the values in the 
		* Builder
		* @return Initialized instance
		*/
		public NewsFeedItemLineAPIOutbound build() {
			return new NewsFeedItemLineAPIOutbound(this);
		}
		
	}
	
	/**
	* Converts from Domain to APIOutbound representation 
	* @param line - To be converted
	* @return APIOutbound representation
	*/
	public static NewsFeedItemLineAPIOutbound convertFromDomain(NewsFeedItemLine line) {
		return NewsFeedItemLineAPIOutbound
				.builder()
					.order(line.getOrder())
					.type(line.getType())
					.image(line.getImage().isEmpty()	? null : line.getImage().get())
					.text(line.getText().isEmpty() 		? null : line.getText().get())
					.url(line.getUrl().isEmpty() 		? null : line.getUrl().get())
				.build();
	}
	
}