package com.arenella.recruit.campaigns.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
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
import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;
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
	
	/**
	* Tests case attempt is made to update a non existent campaign 
	*/
	@Test
	void testAddParticipationToCampaignUnknownCampaign() {
		
		final UUID 		campaignId 		= UUID.randomUUID();
		final String 	contactId 		= "rec22";
		final String 	loggedInUserId 	= "rec88";
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.empty());
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.addParticipationToCampaign(contactId, campaignId, null, ParticipantType.VIEW, loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_CAMPAIGN_NOT_FOUND, ex.getMessage());
		
		verify(this.mockCampaignDao, never()).saveCampaign(any(Campaign.class));
		
	}
	
	/**
	* Tests case where the logged in User is not a know contact 
	*/
	@Test
	void testAddParticipationToCampaignUnknownUser() {
		
		final UUID 		campaignId 		= UUID.randomUUID();
		final String 	contactId 		= "rec22";
		final String 	loggedInUserId 	= "rec88";
		
		Campaign campaign = Campaign.builder().build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.empty());
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.addParticipationToCampaign(contactId, campaignId, null, ParticipantType.VIEW, loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_CONTACT_NOT_FOUND, ex.getMessage());
		
		verify(this.mockCampaignDao, never()).saveCampaign(any(Campaign.class));
		
	}
	
	/**
	* Tests case where the logged in User is a know contact
	* but does not have a paid subscription and can't administer
	* campaigns 
	*/
	@Test
	void testAddParticipationToCampaignNonPaidUser() {
		
		final UUID 		campaignId 		= UUID.randomUUID();
		final String 	contactId 		= "rec22";
		final String 	loggedInUserId 	= "rec88";
		
		Campaign campaign = Campaign.builder().build();
		Contact loggedInUserContact = new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.CREDIT);
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		
		RuntimeException ex = assertThrows(RuntimeException.class, () -> {
			this.service.addParticipationToCampaign(contactId, campaignId, null, ParticipantType.VIEW, loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE, ex.getMessage());
		
		verify(this.mockCampaignDao, never()).saveCampaign(any(Campaign.class));
		
	}
	
	/**
	* Test case when the new Participant is not a known contact in the 
	* system
	*/
	@Test
	void testAddParticipationToCampaignNonExistentNewParticipant() {
		
		final UUID 		campaignId 		= UUID.randomUUID();
		final String 	contactId 		= "rec22";
		final String 	loggedInUserId 	= "rec88";
		
		Campaign campaign = Campaign.builder().build();
		Contact loggedInUserContact = new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.empty());
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.addParticipationToCampaign(contactId, campaignId, null, ParticipantType.VIEW, loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_CONTACT_NOT_FOUND, ex.getMessage());
		
		verify(this.mockCampaignDao, never()).saveCampaign(any(Campaign.class));
		
	}
	
	/**
	* Test case when the Current user attempts to add a new Participant but is not an 
	* admin user for the Campaign
	*/
	@Test
	void testAddParticipationToCampaignLoggedInUserNotAdmin() {
		
		final UUID 		campaignId 		= UUID.randomUUID();
		final String 	contactId 		= "rec22";
		final String 	loggedInUserId 	= "rec88";
		
		Contact loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		Contact newParticipantContact 	= new Contact("rec3", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.CREDIT);
		
		Campaign campaign = Campaign.builder().participation(Participation
				.builder()
				.contactId(loggedInUserId)
				.type(ParticipantType.VIEW)
		.build()).build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.of(newParticipantContact));
		
		RuntimeException ex = assertThrows(RuntimeException.class, () -> {
			this.service.addParticipationToCampaign(contactId, campaignId, null, ParticipantType.VIEW, loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_NO_ADMIN_ROLE_FOR_USER, ex.getMessage());
		
		verify(this.mockCampaignDao, never()).saveCampaign(any(Campaign.class));
		
	}
	

	/**
	* Test case when the new Participant is already a participant in the 
	* Campaign
	*/
	@Test
	void testAddParticipationToCampaignExistentNewParticipant() {
		
		final UUID 		campaignId 		= UUID.randomUUID();
		final String 	contactId 		= "rec22";
		final String 	loggedInUserId 	= "rec88";
		
		Contact loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		Contact newParticipantContact 	= new Contact("rec3", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.CREDIT);
		
		Campaign campaign = Campaign.builder().participation(Participation
				.builder()
					.contactId(loggedInUserId)
					.type(ParticipantType.ADMIN)
				.build())
				.participation(Participation
						.builder()
							.contactId(contactId)
							.type(ParticipantType.VIEW)
						.build()
				).build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.of(newParticipantContact));
		
		RuntimeException ex = assertThrows(RuntimeException.class, () -> {
			this.service.addParticipationToCampaign(contactId, campaignId, null, ParticipantType.ADMIN, loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_CONTACT_ALREADY_PARTICIPANT, ex.getMessage());
		
		verify(this.mockCampaignDao, never()).saveCampaign(any(Campaign.class));
		
	}
	
	/**
	* Test happy path
	*/
	@Test
	void testAddParticipationToCampaign() {
		
		final UUID 		campaignId 		= UUID.randomUUID();
		final String 	contactId 		= "rec22";
		final String 	loggedInUserId 	= "rec88";
		
		ArgumentCaptor<Campaign> campaignArgCapt = ArgumentCaptor.forClass(Campaign.class);
		
		Contact loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		Contact newParticipantContact 	= new Contact("rec3", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.CREDIT);
		
		Campaign campaign = Campaign.builder().participation(Participation
				.builder()
					.contactId(loggedInUserId)
					.type(ParticipantType.ADMIN)
				.build())
				.build();
		
		doNothing().when(this.mockCampaignDao).saveCampaign(campaignArgCapt.capture());
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		when(this.mockContactDao.fetchContact(contactId)).thenReturn(Optional.of(newParticipantContact));
		
		this.service.addParticipationToCampaign(contactId, campaignId, null, ParticipantType.EDIT, loggedInUserId);
		
		verify(this.mockCampaignDao).saveCampaign(any(Campaign.class));
		
		Campaign saved = campaignArgCapt.getValue();
		
		saved.getParticipations().stream().filter(p -> p.getType() == ParticipantType.EDIT && p.getContactId().equals(contactId)).findAny().orElseThrow();
		
	}
	
}