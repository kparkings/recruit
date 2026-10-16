package com.arenella.recruit.campaign.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the IMNotificationEntity class 
*/
class IMNotificationEntityTest {

	private static final UUID 		NOTIFICATION_ID		= UUID.randomUUID();
	private static final UUID 		CAMPAIGN_ID			= UUID.randomUUID();
	private static final UUID 		ROLE_ID				= UUID.randomUUID();
	private static final String 	RECRUITER_ID		= "rec1";
	private static final String 	CANDIDATE_ID		= "1234";

	/**
	* Tests construction via the Builder 
	*/
	@Test
	void testBuilder() {
		
		IMNotificationEntity notification = IMNotificationEntity
				.builder()
					.notificationId(NOTIFICATION_ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.recruiterId(RECRUITER_ID)
					.candidateId(CANDIDATE_ID)
				.build();
		
		assertEquals(NOTIFICATION_ID, 	notification.getNotificationId());
		assertEquals(CAMPAIGN_ID, 		notification.getCampaignId());
		assertEquals(ROLE_ID, 			notification.getRoleId().orElseThrow());
		assertEquals(RECRUITER_ID, 		notification.getRecruiterId());
		assertEquals(CANDIDATE_ID, 		notification.getCandidateId());
		
	}
	
}
