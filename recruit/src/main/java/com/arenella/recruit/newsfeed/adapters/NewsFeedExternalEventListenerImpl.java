package com.arenella.recruit.newsfeed.adapters;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.adapters.events.CandidateUpdateEvent;
import com.arenella.recruit.newsfeed.beans.NewsFeedItem;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemLine;
import com.arenella.recruit.newsfeed.beans.NewsFeedItemLine.NEWS_FEED_ITEM_LINE_TYPE;
import com.arenella.recruit.newsfeed.services.NewsFeedItemService;

import java.util.Set;
import java.util.LinkedHashSet;

/**
* Monolith implementation. Simply calls the appropriate service
* @author K Parkings
*/
@Service
public class NewsFeedExternalEventListenerImpl implements NewsFeedExternalEventListener{

	@Autowired
	private NewsFeedItemService service;
	
	/**
	* Refer to the NewsFeedExternalEventListener interface for details 
	*/
	@Override
	public void listenForEventCandidateUpdate(CandidateUpdateEvent event) {
		
		final UUID newsItemId = UUID.randomUUID();
		
		Set<NewsFeedItemLine> lines = new LinkedHashSet<>();
		
		lines.add(NewsFeedItemLine.builder().id(UUID.randomUUID()).newsItemId(newsItemId).type(NEWS_FEED_ITEM_LINE_TYPE.TEXT).text("C"+event.getCandidateId()).build());
		lines.add(NewsFeedItemLine.builder().id(UUID.randomUUID()).newsItemId(newsItemId).type(NEWS_FEED_ITEM_LINE_TYPE.TEXT).text(event.getFirstName() + " " + event.getSurname()).build());
		lines.add(NewsFeedItemLine.builder().id(UUID.randomUUID()).newsItemId(newsItemId).type(NEWS_FEED_ITEM_LINE_TYPE.TEXT).text(event.getRoleSought()).build());
		lines.add(NewsFeedItemLine.builder().id(UUID.randomUUID()).newsItemId(newsItemId).type(NEWS_FEED_ITEM_LINE_TYPE.INTERNAL_URL).text("/"+event.getCandidateId()).build());
		
		NewsFeedItem item = NewsFeedItem
				.builder()
					.id(newsItemId)
					.referencedUserId(String.valueOf(event.getCandidateId()))
					.created(LocalDateTime.now())
					.itemType(event.getItemType())
					.lines(lines)
				.build();
		
		this.service.addNewsFeedItem(item);
		
	}

}
