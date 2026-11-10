package com.arenella.recruit.campaigns.entities;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.CandidateGroup;
import com.arenella.recruit.campaigns.beans.CandidateGroup.Group;

/**
* Unit tests for the CandidateGroupEntity class
*/
class CandidateGroupEntityTest {

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
		
		CandidateGroupEntity entity = CandidateGroupEntity.
			builder()
				.id(ID)
				.campaignId(CAMPAIGN_ID)
				.roleId(ROLE_ID)
				.candidateId(CANDIDATE_ID)
				.group(GROUP)
			.build();
		
		assertEquals(ID, 			entity.getId());
		assertEquals(CAMPAIGN_ID, 	entity.getCampaignId());
		assertEquals(ROLE_ID, 		entity.getRoleId().get());
		assertEquals(CANDIDATE_ID,  entity.getCandidateId());
		assertEquals(GROUP, 		entity.getGroup());
		
	}
	
	/**
	* Tests conversion from Entity to Domain representation 
	*/
	@Test
	void testFromEntity() {
		
		CandidateGroupEntity entity = CandidateGroupEntity.
				builder()
					.id(ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.candidateId(CANDIDATE_ID)
					.group(GROUP)
				.build();
			
			assertEquals(ID, 			entity.getId());
			assertEquals(CAMPAIGN_ID, 	entity.getCampaignId());
			assertEquals(ROLE_ID, 		entity.getRoleId().get());
			assertEquals(CANDIDATE_ID,  entity.getCandidateId());
			assertEquals(GROUP, 		entity.getGroup());
			
			CandidateGroup group = CandidateGroupEntity.fromEntity(entity);
			
			assertEquals(ID, 			group.getId());
			assertEquals(CAMPAIGN_ID, 	group.getCampaignId());
			assertEquals(ROLE_ID, 		group.getRoleId().get());
			assertEquals(CANDIDATE_ID,  group.getCandidateId());
			assertEquals(GROUP, 		group.getGroup());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation 
	*/
	@Test
	void testToEntity() {
		
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
			
			CandidateGroupEntity entity = CandidateGroupEntity.toEntity(group);
			
			assertEquals(ID, 			entity.getId());
			assertEquals(CAMPAIGN_ID, 	entity.getCampaignId());
			assertEquals(ROLE_ID, 		entity.getRoleId().get());
			assertEquals(CANDIDATE_ID,  entity.getCandidateId());
			assertEquals(GROUP, 		entity.getGroup());
		
	}
	
}