package com.arenella.recruit.newsfeed.adapters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.adapters.events.CandidateUpdateEvent;
import com.arenella.recruit.newsfeed.beans.NewsFeedItem;
import com.arenella.recruit.newsfeed.services.NewsFeedItemService;

/**
* Unit tests for the NewsFeedExternalEventListenerImpl class
* @author Hp
*/
@ExtendWith(MockitoExtension.class)
public class NewsFeedExternalEventListenerImplTest {

	@Mock
	private NewsFeedItemService mockService;
	
	@InjectMocks
	private NewsFeedExternalEventListenerImpl listener = new NewsFeedExternalEventListenerImpl();
	
	/**
	* Tests handling of CandidateUpdateEvent 
	*/
	@Test
	public void testListenForEventCandidateUpdate() {
		
		CandidateUpdateEvent event = CandidateUpdateEvent.builder().build();
		
		listener.listenForEventCandidateUpdate(event);
		
		Mockito.verify(this.mockService).addNewsFeedItem(Mockito.any(NewsFeedItem.class));
		
	}
	
}
