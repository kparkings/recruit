package com.arenella.recruit.campaigns.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the IMNotification class 
*/
class IMNotificationTest {

	private static final UUID 		CAMPAIGN_ID		= UUID.randomUUID();
	private static final UUID 		ROLE_ID			= UUID.randomUUID();
	private static final String 	RECRUITER_ID	= "rec1";
	private static final String 	CANDIDATE_ID	= "1234";

	/**
	* Tests construction via the Builder 
	*/
	@Test
	void testBuilder() {
		
		IMNotification notification = IMNotification
				.builder()
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.recruiterId(RECRUITER_ID)
					.candidateId(CANDIDATE_ID)
				.build();
		
		assertEquals(CAMPAIGN_ID, 	notification.getCampaignId());
		assertEquals(ROLE_ID, 		notification.getRoleId().orElseThrow());
		assertEquals(RECRUITER_ID, 	notification.getRecruiterId());
		assertEquals(CANDIDATE_ID, 	notification.getCandidateId());
		
	}
	
}
