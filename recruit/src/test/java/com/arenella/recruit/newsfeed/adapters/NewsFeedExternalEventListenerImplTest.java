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
	
	/**
	* Tests handling of CandidateUpdateEvent when Candidate has been deleted. This is a special case
	* as instead of creating a NewsFeedItem we use it as a command to trigger the deletion of all 
	* NewsFeedItems relating to a Candidate that has deleted their account so we dont retain data 
	* on Users who no longer want to be in the system
	*/
	@Test
	public void testListenForEventCandidateUpdate_candidate_deleted() {
		
		final int id = 1234;
		
		CandidateUpdateEvent event = CandidateUpdateEvent.builder().candidateId(id).itemType(NewsFeedItem.NEWSFEED_ITEM_TYPE.CANDIDATE_DELETED).build();
		
		listener.listenForEventCandidateUpdate(event);
		
		Mockito.verify(this.mockService).deleteAllNewsFeedItemsForReferencedUserId(String.valueOf(id));
		Mockito.verify(this.mockService, Mockito.never()).addNewsFeedItem(Mockito.any(NewsFeedItem.class));
		
	}
	
}
