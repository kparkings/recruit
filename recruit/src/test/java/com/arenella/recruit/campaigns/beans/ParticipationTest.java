package com.arenella.recruit.campaigns.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;

/**
* Unit tests for the Participation class 
*/
class ParticipationTest {


	private static final UUID				PARTICIPATION_ID = UUID.randomUUID();
	private static final String 			CONTACT_ID = "rec22";
	private static final UUID 				CAMPAIGN_ID = UUID.randomUUID(); 
	private static final UUID 				ROLE_ID = UUID.randomUUID();
	private static final ParticipantType 	TYPE = ParticipantType.VIEW;
	
	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testBuilder() {
		
		Participation participation = Participation
				.builder()
					.participationId(PARTICIPATION_ID)
					.contactId(CONTACT_ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.type(TYPE)
				.build();
		
		assertEquals(PARTICIPATION_ID, 	participation.getParticipanttionId());
		assertEquals(CONTACT_ID, 		participation.getContactId());
		assertEquals(CAMPAIGN_ID, 		participation.getCampaignId());
		assertEquals(ROLE_ID, 			participation.getRoleId().get());
		assertEquals(TYPE,			 	participation.getType());
	}
	
}