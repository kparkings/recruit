package com.arenella.recruit.campaign.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.IMNotification;

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

	/**
	* Tests conversion from Entity to Domain representation 
	*/
	@Test
	void testFromEntity() {
		
		IMNotificationEntity entity = IMNotificationEntity
				.builder()
					.notificationId(NOTIFICATION_ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.recruiterId(RECRUITER_ID)
					.candidateId(CANDIDATE_ID)
				.build();
		
		assertEquals(NOTIFICATION_ID, 	entity.getNotificationId());
		assertEquals(CAMPAIGN_ID, 		entity.getCampaignId());
		assertEquals(ROLE_ID, 			entity.getRoleId().orElseThrow());
		assertEquals(RECRUITER_ID, 		entity.getRecruiterId());
		assertEquals(CANDIDATE_ID, 		entity.getCandidateId());
		
		IMNotification notification = IMNotificationEntity.fromEntity(entity);
		
		assertEquals(NOTIFICATION_ID, 	notification.getNotificationId());
		assertEquals(CAMPAIGN_ID, 		notification.getCampaignId());
		assertEquals(ROLE_ID, 			notification.getRoleId().orElseThrow());
		assertEquals(RECRUITER_ID, 		notification.getRecruiterId());
		assertEquals(CANDIDATE_ID, 		notification.getCandidateId());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation 
	*/
	@Test
	void testToEntity() {
		
		IMNotification notification = IMNotification
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
		
		IMNotificationEntity entity = IMNotificationEntity.toEntity(notification);
		
		assertEquals(NOTIFICATION_ID, 	entity.getNotificationId());
		assertEquals(CAMPAIGN_ID, 		entity.getCampaignId());
		assertEquals(ROLE_ID, 			entity.getRoleId().orElseThrow());
		assertEquals(RECRUITER_ID, 		entity.getRecruiterId());
		assertEquals(CANDIDATE_ID, 		entity.getCandidateId());
		
	}

}