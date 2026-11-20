package com.arenella.recruit.campaigns.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.CampaignEmail;
import com.arenella.recruit.campaigns.beans.CampaignEmailAttachment;

/**
* Unit tests for the CampaignEmailEntity class 
*/
class CampaignEmailEntityTest {

	private static final UUID 								ID						= UUID.randomUUID();
	private static final UUID 								EXTERNAL_EMAIL_ID		= UUID.randomUUID();
	private static final UUID 								CAMPAIGN_ID				= UUID.randomUUID();
	private static final UUID 								ROLE_ID					= UUID.randomUUID();
	private static final String 							OWNING_RECRUITER_ID 	= "rec33";
	private static final String 							SENDING_CANDIDATE_ID 	= "123";
	private static final String 							FIRST_NAME				= "jimmy";
	private static final String 							SURNAME					= "boo";
	private static final String 							SUBJECT 				= "a Subject";
	private static final Set<CampaignEmailAttachmentEntity> ATTACHMENTS				= Set.of(new CampaignEmailAttachmentEntity());
	
	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testConstruction() {
		
		CampaignEmailEntity entity = CampaignEmailEntity
				.builder()
					.id(ID)
					.externalEmailId(EXTERNAL_EMAIL_ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.owningRecruiterId(OWNING_RECRUITER_ID)
					.sendingCandidateId(SENDING_CANDIDATE_ID)
					.firstName(FIRST_NAME)
					.surname(SURNAME)
					.subject(SUBJECT)
					.attachments(ATTACHMENTS)
				.build();
		
		assertEquals(ID, 					entity.getId());
		assertEquals(EXTERNAL_EMAIL_ID, 	entity.getExternalEmailId());
		assertEquals(CAMPAIGN_ID, 			entity.getCampaignId());
		assertEquals(ROLE_ID, 				entity.getRoleId().orElseThrow());
		assertEquals(OWNING_RECRUITER_ID, 	entity.getOwningRecruiterId());
		assertEquals(SENDING_CANDIDATE_ID, 	entity.getSendingCandidateId().orElse(null));
		assertEquals(FIRST_NAME, 			entity.getFirstName());
		assertEquals(SURNAME, 				entity.getSurname());
		assertEquals(SUBJECT, 				entity.getSubject());
		assertEquals(1, 					entity.getAttachments().size());
		
	}
	
	/**
	* Tests Defaults 
	*/
	@Test
	void testConstructionDefaults() {
		
		CampaignEmailEntity entity = CampaignEmailEntity
				.builder()
				.build();
		
		assertTrue(entity.getSendingCandidateId().isEmpty());
		assertTrue(entity.getAttachments().isEmpty());
		assertTrue(entity.getRoleId().isEmpty());
	}
	
	/**
	* Tests conversion from Entity to Domain representation
	*/
	@Test
	void testFromEntity() {
		
		CampaignEmailEntity entity = CampaignEmailEntity
				.builder()
					.id(ID)
					.externalEmailId(EXTERNAL_EMAIL_ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.owningRecruiterId(OWNING_RECRUITER_ID)
					.sendingCandidateId(SENDING_CANDIDATE_ID)
					.firstName(FIRST_NAME)
					.surname(SURNAME)
					.subject(SUBJECT)
					.attachments(ATTACHMENTS)
				.build();
		
		assertEquals(ID, 					entity.getId());
		assertEquals(EXTERNAL_EMAIL_ID, 	entity.getExternalEmailId());
		assertEquals(CAMPAIGN_ID, 			entity.getCampaignId());
		assertEquals(ROLE_ID, 				entity.getRoleId().orElseThrow());
		assertEquals(OWNING_RECRUITER_ID, 	entity.getOwningRecruiterId());
		assertEquals(SENDING_CANDIDATE_ID, 	entity.getSendingCandidateId().orElse(null));
		assertEquals(FIRST_NAME, 			entity.getFirstName());
		assertEquals(SURNAME, 				entity.getSurname());
		assertEquals(SUBJECT, 				entity.getSubject());
		assertEquals(1, 					entity.getAttachments().size());
		
		CampaignEmail email = CampaignEmailEntity.fromEntity(entity);
		
		assertEquals(ID, 					email.getId());
		assertEquals(EXTERNAL_EMAIL_ID, 	email.getExternalEmailId());
		assertEquals(CAMPAIGN_ID, 			email.getCampaignId());
		assertEquals(ROLE_ID, 				email.getRoleId().orElseThrow());
		assertEquals(OWNING_RECRUITER_ID, 	email.getOwningRecruiterId());
		assertEquals(SENDING_CANDIDATE_ID, 	email.getSendingCandidateId().orElse(null));
		assertEquals(FIRST_NAME, 			email.getFirstName());
		assertEquals(SURNAME, 				email.getSurname());
		assertEquals(SUBJECT, 				email.getSubject());
		assertEquals(1, 					email.getAttachments().size());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation
	*/
	@Test
	void testToEntity() {
		
		CampaignEmail email = CampaignEmail
				.builder()
					.id(ID)
					.externalEmailId(EXTERNAL_EMAIL_ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.owningRecruiterId(OWNING_RECRUITER_ID)
					.sendingCandidateId(SENDING_CANDIDATE_ID)
					.firstName(FIRST_NAME)
					.surname(SURNAME)
					.subject(SUBJECT)
					.attachments(Set.of(CampaignEmailAttachment.builder().build()))
				.build();
		
		assertEquals(ID, 					email.getId());
		assertEquals(EXTERNAL_EMAIL_ID, 	email.getExternalEmailId());
		assertEquals(CAMPAIGN_ID, 			email.getCampaignId());
		assertEquals(ROLE_ID, 				email.getRoleId().orElseThrow());
		assertEquals(OWNING_RECRUITER_ID, 	email.getOwningRecruiterId());
		assertEquals(SENDING_CANDIDATE_ID, 	email.getSendingCandidateId().orElse(null));
		assertEquals(FIRST_NAME, 			email.getFirstName());
		assertEquals(SURNAME, 				email.getSurname());
		assertEquals(SUBJECT, 				email.getSubject());
		assertEquals(1, 					email.getAttachments().size());
		
		CampaignEmailEntity entity = CampaignEmailEntity.toEntity(email);
		
		assertEquals(ID, 					entity.getId());
		assertEquals(EXTERNAL_EMAIL_ID, 	entity.getExternalEmailId());
		assertEquals(CAMPAIGN_ID, 			entity.getCampaignId());
		assertEquals(ROLE_ID, 				entity.getRoleId().orElseThrow());
		assertEquals(OWNING_RECRUITER_ID, 	entity.getOwningRecruiterId());
		assertEquals(SENDING_CANDIDATE_ID, 	entity.getSendingCandidateId().orElse(null));
		assertEquals(FIRST_NAME, 			entity.getFirstName());
		assertEquals(SURNAME, 				entity.getSurname());
		assertEquals(SUBJECT, 				entity.getSubject());
		assertEquals(1, 					entity.getAttachments().size());
		
	}
	
}