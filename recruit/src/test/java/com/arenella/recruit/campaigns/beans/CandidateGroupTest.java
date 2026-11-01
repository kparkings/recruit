package com.arenella.recruit.campaigns.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.CandidateGroup.Group;

/**
* Unit tests for the CandidateGroup class 
*/
class CandidateGroupTest {

	private static final UUID 		ID 				= UUID.randomUUID();
	private static final UUID 		CAMPAIGN_ID 	= UUID.randomUUID();
	private static final UUID 		ROLE_ID 		= UUID.randomUUID();
	private static final String 	CANDIDATE_ID 	= "123";
	private static final Group 		GROUP 			= Group.GROUP0;
	
	/**
	* Tests construction via a builder 
	*/
	@Test
	void testConstruction() {
		
		CandidateGroup group = CandidateGroup.
			builder()
				.id(ID)
				.campaignId(CAMPAIGN_ID)
				.roleId(ROLE_ID)
				.candidateId(CANDIDATE_ID)
				.group(GROUP)
			.build();
		
		assertEquals(ID, 			group.getId());
		assertEquals(CAMPAIGN_ID, 	group.getCampaignId());
		assertEquals(ROLE_ID, 		group.getRoleId().get());
		assertEquals(CANDIDATE_ID,  group.getCandidateId());
		assertEquals(GROUP, 		group.getGroup());
		
	}
	
	/**
	* Tests construction via a builder using existing instance
	* to populate the Builder
	*/
	@Test
	void testConstructionFromExisting() {
		
		CandidateGroup group = CandidateGroup.
			builder()
				.id(ID)
				.campaignId(CAMPAIGN_ID)
				.roleId(ROLE_ID)
				.candidateId(CANDIDATE_ID)
				.group(GROUP)
			.build();
		
		assertEquals(ID, 			group.getId());
		assertEquals(CAMPAIGN_ID, 	group.getCampaignId());
		assertEquals(ROLE_ID, 		group.getRoleId().get());
		assertEquals(CANDIDATE_ID,  group.getCandidateId());
		assertEquals(GROUP, 		group.getGroup());
		
		CandidateGroup newGroup = CandidateGroup.
				builder()
				.from(group)
				.build();
			
			assertEquals(ID, 			newGroup.getId());
			assertEquals(CAMPAIGN_ID, 	newGroup.getCampaignId());
			assertEquals(ROLE_ID, 		newGroup.getRoleId().get());
			assertEquals(CANDIDATE_ID,  newGroup.getCandidateId());
			assertEquals(GROUP, 		newGroup.getGroup());
		
	}
	
}
