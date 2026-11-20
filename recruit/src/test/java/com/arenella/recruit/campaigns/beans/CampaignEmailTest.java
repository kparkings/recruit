package com.arenella.recruit.campaigns.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
* Unit tests for the CampaignEmail class 
*/
class CampaignEmailTest {

	private static final UUID 								ID						= UUID.randomUUID();
	private static final UUID 								EXTERNAL_EMAIL_ID		= UUID.randomUUID();
	private static final UUID 								CAMPAIGN_ID				= UUID.randomUUID();
	private static final UUID 								ROLE_ID					= UUID.randomUUID();
	private static final String 							OWNING_RECRUITER_ID 	= "rec33";
	private static final String 							SENDING_CANDIDATE_ID 	= "123";
	private static final String 							FIRST_NAME				= "jimmy";
	private static final String 							SURNAME					= "boo";
	private static final String 							SUBJECT 				= "a Subject";
	private static final Set<CampaignEmailAttachment> 		ATTACHMENTS				= Set.of(new CampaignEmailAttachment());
	
	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testConstruction() {
		
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
					.attachments(ATTACHMENTS)
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
		
	}
	
	/**
	* Tests Defaults 
	*/
	@Test
	void testConstructionDefaults() {
		
		CampaignEmail entity = CampaignEmail
				.builder()
				.build();
		
		assertTrue(entity.getSendingCandidateId().isEmpty());
		assertTrue(entity.getAttachments().isEmpty());
		assertTrue(entity.getRoleId().isEmpty());
		
	}
	
}