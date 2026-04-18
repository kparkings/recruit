package com.arenella.recruit.campaigns.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arenella.recruit.campaign.dao.CampaignDao;
import com.arenella.recruit.campaign.dao.ContactEntityDao;
import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.arenella.recruit.campaigns.beans.CampaignLogo.PHOTO_FORMAT;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;
import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.beans.Participation;

/**
* Unit tests for the CampaignServiceImpl class 
*/
@ExtendWith(MockitoExtension.class)
class CampaignServiceImplTest {

	@Mock
	private CampaignDao 			mockCampaignDao;
	
	@Mock
	private ContactEntityDao		mockContactDao;
	
	@InjectMocks
	private CampaignServiceImpl 	service;
	
	/**
	* Tests retrieval of campaigns for User 
	*/
	@Test
	void testFetchCampaignsForUser() {
		
		final String 	currentUserId 	= "recrutier99";

		when(this.mockCampaignDao.fetchCampaignsForUser(currentUserId)).thenReturn(Set.of(Campaign.builder().build(), Campaign.builder().build()));
		
		Set<Campaign> campaigins = this.service.fetchCampaignsForUser(currentUserId);
	
		assertEquals(2, campaigins.size());
		
	}
	
	/**
	* Tests the case where a Campaign is requested but it does
	* not exist 
	*/
	@Test
	void testFetchCampaignByIdUnknownCampaigin() {
		
		final UUID 		campaignId 		= UUID.randomUUID();
		final String 	currentUserId 	= "recrutier99";
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.empty());
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.fetchCampaignById(campaignId, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_UNKNOWN_CAMPAIGN, ex.getMessage());
		
	}
	
	/**
	* Tests the case where a Campaign is requested and it exists
	* but where the User is not a participant in the Campaign
	*/
	@Test
	void testFetchCampaignByIdUserNotParticipant() {
		
		final UUID 		campaignId 		= UUID.randomUUID();
		final String 	currentUserId 	= "recrutier99";
		
		Campaign campaign = Campaign
				.builder()
				.participation(Participation.builder().contactId("Another user").build())
				.build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.fetchCampaignById(campaignId, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_USER_NOT_PARTICIPANT, ex.getMessage());
		
		
	}
	
	/**
	* Tests the case where a Campaign is requested and it exists
	* and where the User is a participant in the Campaign
	*/
	@Test
	void testFetchCampaignByIdUserIsParticipant() {
		
		final UUID 		campaignId 		= UUID.randomUUID();
		final String 	currentUserId 	= "recrutier99";
		
		Campaign campaign = Campaign
				.builder()
					.participation(Participation.builder().contactId(currentUserId).build())
				.build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		
		Campaign result = this.service.fetchCampaignById(campaignId, currentUserId);
		
		assertNotNull(result);
		
	}
	
	/**
	* Tests that if the authenticated user doesn't have a corresponding Contact
	* record an exception is thrown
	*/
	@Test
	void testAddCampaignUnknownContact() {
		
		final String 		name 			= "ABN Zimbo";
		final String 		description 	= "Banking client";
		final CampaignLogo 	logo 			= new CampaignLogo(new byte[] {}, PHOTO_FORMAT.jpeg);
		final String 		currentUserId 	= "rec88";
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.addCampaign(name, description, logo, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_CONTACT_NOT_FOUND, ex.getMessage());
		
	}
	
	/**
	* Tests that if a user attempts to create a Campaign and they do now have an active
	* paid subscription they will not be allowed and and exception will be thrown. 
	*/
	@Test
	void testAddCampaignContactHasNoPaidSubscription() {
		
		final String 		name 			= "ABN Zimbo";
		final String 		description 	= "Banking client";
		final CampaignLogo 	logo 			= new CampaignLogo(new byte[] {}, PHOTO_FORMAT.jpeg);
		final String 		currentUserId 	= "rec88";
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.CREDIT)));
		
		RuntimeException ex = assertThrows(RuntimeException.class, () -> {
			this.service.addCampaign(name, description, logo, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE, ex.getMessage());
		
	}
	
	/**
	* Test successfully adding a new Campaign
	*/
	@Test
	void testAddCampaign() {
	
		final String 		name 			= "ABN Zimbo";
		final String 		description 	= "Banking client";
		final CampaignLogo 	logo 			= new CampaignLogo(new byte[] {}, PHOTO_FORMAT.jpeg);
		final String 		currentUserId 	= "rec88";
		
		ArgumentCaptor<Campaign> campaignArgCapt = ArgumentCaptor.forClass(Campaign.class);
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID)));
		doNothing().when(this.mockCampaignDao).saveCampaign(campaignArgCapt.capture());
		
		this.service.addCampaign(name, description, logo, currentUserId);
		
		verify(this.mockCampaignDao).saveCampaign(any(Campaign.class));
		
		Campaign campaign = campaignArgCapt.getValue();
		
		assertNotNull(campaign.getCreated());
		
		assertEquals(1, campaign.getParticipations().size());
		
		assertEquals(currentUserId, campaign.getParticipations().stream().findFirst().map(p -> p.getContactId()).get());
		
	}
	
}