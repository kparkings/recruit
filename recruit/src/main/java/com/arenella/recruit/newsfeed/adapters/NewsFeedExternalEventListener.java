package com.arenella.recruit.newsfeed.adapters;

import com.arenella.recruit.adapters.events.CandidateUpdateEvent;

/**
* Defines Events that the service listens for from external sources
* @author K Parkings
*/
public interface NewsFeedExternalEventListener {

	/**
	* Listens for incoming events of type CandidateUpdateEvent
	* @param event
	*/
	public void listenForEventCandidateUpdate(CandidateUpdateEvent event);
	
}
