package com.arenella.recruit.newsfeed.controllers;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.arenella.recruit.newsfeed.beans.NewsFeedItem;
import com.arenella.recruit.newsfeed.beans.NewsFeedItem.NEWSFEED_ITEM_TYPE;

/**
* An Object as being something that can be displayed
* in a newsfeed
* @author K Parkings
*/
public class NewsFeedItemAPIOutbound {
	
	private LocalDateTime 						created;
	private NEWSFEED_ITEM_TYPE 					itemType;
	private Set<NewsFeedItemLineAPIOutbound> 	lines 					= new LinkedHashSet<>();
	
	/**
	* Constructor based upon a builder
	* @param builder
	*/
	public NewsFeedItemAPIOutbound(NewsFeedItemAPIOutboundBuilder builder) {
		this.created 				= builder.created;
		this.itemType 				= builder.itemType;
		this.lines.clear();
		this.lines.addAll(builder.lines);
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
	public Set<NewsFeedItemLineAPIOutbound> getLines(){
		return this.lines;
	}
	
	/**
	* Returns a builder for the class
	* @return Builder
	*/
	public static NewsFeedItemAPIOutboundBuilder builder() {
		return new NewsFeedItemAPIOutboundBuilder();
	}
	
	/**
	* Builder for the class
	* @author K Parkings
	*/
	public static class NewsFeedItemAPIOutboundBuilder{
		
		private LocalDateTime 						created;
		private NEWSFEED_ITEM_TYPE 					itemType;
		private Set<NewsFeedItemLineAPIOutbound> 	lines 				= new LinkedHashSet<>();
		
		/**
		* Sets creation date/time
		* @param created - when it was created
		* @return Builder
		*/
		public NewsFeedItemAPIOutboundBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the type so similar items can be grouped
		* @param itemType - type of the item
		* @return Builder
		*/
		public NewsFeedItemAPIOutboundBuilder itemType(NEWSFEED_ITEM_TYPE itemType) {
			this.itemType = itemType;
			return this;
		}
		
		/**
		* Sets the lines that make up the post
		* @param lines - lines of content
		* @return Builder
		*/
		public NewsFeedItemAPIOutboundBuilder lines(Set<NewsFeedItemLineAPIOutbound> lines) {
			this.lines.clear();
			this.lines.addAll(lines);
			return this;
		}
		
		/**
		* Returns a instance initialized with the values in the Builder
		* @return Initialized instance
		*/
		public NewsFeedItemAPIOutbound build() {
			return new NewsFeedItemAPIOutbound(this);
		}
		
	}
	
	/**
	* Converts from Domain to APIOutbound representation
	* @param item - To be converted
	* @return APIOutbound representation
	*/
	public static NewsFeedItemAPIOutbound convertFromDomain(NewsFeedItem item) {
		return NewsFeedItemAPIOutbound
				.builder()
					.created(item.getCreated())
					.itemType(item.getItemType())
					.lines(item.getLines().stream().map(NewsFeedItemLineAPIOutbound::convertFromDomain).collect(Collectors.toCollection(LinkedHashSet::new)))
				.build();
	}
	
}