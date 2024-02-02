package com.arenella.recruit.adapters.events;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.newsfeed.beans.NewsFeedItem.NEWSFEED_ITEM_TYPE;

/**
* Unit tests for the CandidateUpdateEvent class 
* @author K Parkings
*/
public class CandidateUpdateEventTest {

	private static final NEWSFEED_ITEM_TYPE 	ITEM_TYPE 		= NEWSFEED_ITEM_TYPE.CANDIDATE_ADDED;
	private static final int					CANDIDATE_ID 	= 1001;;
	private static final String 				FIRST_NAME		= "Kevin";
	private static final String					SURNAME			= "Parkings";
	private static final String					ROLE_SOUGHT		= "Java Developer";
	
	/**
	* Unit tests for the CandidateUpdateEvent class
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		CandidateUpdateEvent event = CandidateUpdateEvent
				.builder()
					.itemType(ITEM_TYPE)
					.candidateId(CANDIDATE_ID)
					.firstName(FIRST_NAME)
					.surname(SURNAME)
					.roleSought(ROLE_SOUGHT)
				.build();
		
		assertEquals(ITEM_TYPE, 	event.getItemType());
		assertEquals(CANDIDATE_ID, 	event.getCandidateId());
		assertEquals(FIRST_NAME, 	event.getFirstName());
		assertEquals(SURNAME, 		event.getSurname());
		assertEquals(ROLE_SOUGHT, 	event.getRoleSought());
		
	}
	
}