package com.arenella.recruit.campaigns.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.campaigns.beans.Participation;
import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;

/**
* Unit tests for the ParticipationEntity class 
*/
class ParticipationEntityTest {

	private static final UUID				PARTICIPATION_ID 	= UUID.randomUUID();
	private static final String 			CONTACT_ID 			= "rec332";
	private static final UUID 				CAMPAIGN_ID 		= UUID.randomUUID();
	private static final UUID 				ROLE_ID 			= UUID.randomUUID();
	private static final ParticipantType	 TYPE 				= ParticipantType.EDIT;
	
	/**
	* Tests construction via a Builder 
	*/
	@Test
	void testConstruction() {
		
		ParticipationEntity entity = ParticipationEntity
				.builder()
					.participationId(PARTICIPATION_ID)
					.contactId(CONTACT_ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.type(TYPE)
				.build();
		
		assertEquals(PARTICIPATION_ID, 	entity.getParticipationId());
		assertEquals(CONTACT_ID, 		entity.getContactId());
		assertEquals(CAMPAIGN_ID, 		entity.getCampaignId());
		assertEquals(ROLE_ID, 			entity.getRoleId().get());
		assertEquals(TYPE, 				entity.getType());
		
	}
	
	/**
	* Tests conversion from the Entity to Domain representation 
	* of a Participation 
	*/
	@Test
	void testFromEntity() {
		
		ParticipationEntity entity = ParticipationEntity
				.builder()
					.participationId(PARTICIPATION_ID)
					.contactId(CONTACT_ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.type(TYPE)
				.build();
		
		assertEquals(PARTICIPATION_ID, 	entity.getParticipationId());
		assertEquals(CONTACT_ID, 		entity.getContactId());
		assertEquals(CAMPAIGN_ID, 		entity.getCampaignId());
		assertEquals(ROLE_ID, 			entity.getRoleId().get());
		assertEquals(TYPE, 				entity.getType());
		
		Participation participation = ParticipationEntity.fromEntity(entity);
		
		assertEquals(PARTICIPATION_ID, 	participation.getParticipationId());
		assertEquals(CONTACT_ID, 		participation.getContactId());
		assertEquals(CAMPAIGN_ID, 		participation.getCampaignId());
		assertEquals(ROLE_ID, 			participation.getRoleId().get());
		assertEquals(TYPE, 				participation.getType());
		
	}
	
	/**
	* Tests conversion from Domain to Entity representation 
	* of a Participation 
	*/
	@Test
	void testToEntity() {
		
		Participation participation = Participation
				.builder()
					.participationId(PARTICIPATION_ID)
					.contactId(CONTACT_ID)
					.campaignId(CAMPAIGN_ID)
					.roleId(ROLE_ID)
					.type(TYPE)
				.build();
		
		assertEquals(PARTICIPATION_ID, 	participation.getParticipationId());
		assertEquals(CONTACT_ID, 		participation.getContactId());
		assertEquals(CAMPAIGN_ID, 		participation.getCampaignId());
		assertEquals(ROLE_ID, 			participation.getRoleId().get());
		assertEquals(TYPE, 				participation.getType());
		
		ParticipationEntity entity = ParticipationEntity.toEntity(participation);
		
		assertEquals(PARTICIPATION_ID, 	entity.getParticipationId());
		assertEquals(CONTACT_ID, 		entity.getContactId());
		assertEquals(CAMPAIGN_ID, 		entity.getCampaignId());
		assertEquals(ROLE_ID, 			entity.getRoleId().get());
		assertEquals(TYPE, 				entity.getType());
		
	}
	
}