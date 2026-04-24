package com.arenella.recruit.campaigns.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
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
import com.arenella.recruit.campaign.dao.NoteEntityDao;
import com.arenella.recruit.campaign.dao.ParticipationEntityDao;
import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.arenella.recruit.campaigns.beans.CampaignLogo.PHOTO_FORMAT;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;
import com.arenella.recruit.campaigns.beans.Document.DocumentType;
import com.arenella.recruit.campaigns.beans.Note;
import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;
import com.arenella.recruit.campaigns.beans.Role;
import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.beans.Participation;

/**
* Unit tests for the CampaignServiceImpl class 
*/
@ExtendWith(MockitoExtension.class)
class CampaignServiceImplTest {

	@Mock
	private CampaignDao 				mockCampaignDao;
	
	@Mock
	private ParticipationEntityDao 		mockParticipationDao;
	
	@Mock
	private ContactEntityDao			mockContactDao;
	
	@Mock
	private NoteEntityDao				mockNoteDao;
	
	@InjectMocks
	private CampaignServiceImpl 		service;
	
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
		
		final UUID 		campaignId 				= UUID.randomUUID();
		final String 	contactId 				= "rec22";
		final String 	loggedInUserId 			= "rec88";
		final Contact 	loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
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
		
		final UUID 		campaignId 				= UUID.randomUUID();
		final String 	contactId 				= "rec22";
		final String 	loggedInUserId 			= "rec88";
	
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
		
		Contact loggedInUserContact = new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.CREDIT);
		
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
	
	/**
	* Tests case an attempt is made to delete a Participation but the Participation
	* doesn't exist
	*/
	@Test
	void testDeleteParticipationNoMatchingParticipant() {
		
		final UUID 		participationId 		= UUID.randomUUID();
		final String 	loggedInUserId 			= "rec88";
		final Contact 	loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		when(this.mockParticipationDao.fetchParticipationById(participationId)).thenReturn(Optional.empty());
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteParticipation(participationId, loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_UNKNOWN_PARTICIPATION, ex.getMessage());
		
	}
	
	/**
	* Tests case an attempt is made to delete a Participation but the 
	* associated Campaign doesn't exist
	*/
	@Test
	void testDeleteParticipationNoMatchingCampaign() {
		
		final UUID 		participationId 		= UUID.randomUUID();
		final UUID 		campaignId 				= UUID.randomUUID();
		final String 	loggedInUserId 			= "rec88";
		final Contact 	loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		when(this.mockParticipationDao.fetchParticipationById(participationId)).thenReturn(Optional.of(Participation.builder().participationId(participationId).campaignId(campaignId).build()));
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.empty());
		
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteParticipation(participationId, loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_UNKNOWN_CAMPAIGN, ex.getMessage());
		
	}
	
	/**
	* Tests case an attempt is made to delete a Participation but the 
	* associated Role doesn't exist
	*/
	@Test
	void testDeleteParticipationNoMatchingRole() {
		
		final UUID 		participationId 		= UUID.randomUUID();
		final UUID 		campaignId 				= UUID.randomUUID();
		final UUID 		roleId 					= UUID.randomUUID();
		final String 	loggedInUserId 			= "rec88";
		final Contact 	loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		when(this.mockParticipationDao.fetchParticipationById(participationId)).thenReturn(Optional.of(Participation.builder().participationId(participationId).campaignId(campaignId).roleId(roleId).build()));
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(Campaign.builder().build()));
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteParticipation(participationId, loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_UNKNOWN_ROLE, ex.getMessage());
		
	}
	
	/**
	* Tests it is not possible to delete a participation if that would mean
	* no Admin user would exist at either the campaign level or the role level 
	*/
	@Test
	void testDeleteParticipationNoAdminLeftCampaignLevel() {
		
		final UUID 		participationId 		= UUID.randomUUID();
		final UUID 		campaignId 				= UUID.randomUUID();
		final String 	loggedInUserId 			= "rec88";
		final Contact 	loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		when(this.mockParticipationDao.fetchParticipationById(participationId)).thenReturn(Optional.of(Participation.builder().participationId(participationId).campaignId(campaignId).build()));
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(Campaign.builder().participation(Participation.builder().participationId(participationId).contactId(loggedInUserId).type(ParticipantType.ADMIN).build()).build()));
		
		IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
			this.service.deleteParticipation(participationId, loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_NO_ADMIN_USER_WOULD_BE_LEFT, ex.getMessage());
		
	}
	
	/**
	* Tests it is not possible to delete a participation if that would mean
	* no Admin user would exist at campaign level even if there is an admin 
	* participation at the role level
	*/
	@Test
	void testDeleteParticipationNoAdminLeftAtCampaignLevel() {
		
		final UUID 		participationId 	= UUID.randomUUID();
		final UUID 		campaignId 			= UUID.randomUUID();
		final UUID 		roleId 				= UUID.randomUUID();
		final String	contactIdCampaign	= "rec99";
		final String	contactIdRole1		= "rec77";
		final String 	loggedInUserId 		= "rec88";
		
		Contact loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		when(this.mockParticipationDao.fetchParticipationById(participationId)).thenReturn(Optional.of(Participation.builder().participationId(participationId).campaignId(campaignId).roleId(roleId).build()));
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(Campaign
				.builder()
					.participation(Participation.builder().contactId(contactIdCampaign).build())
					.role(Role.builder()
							.id(roleId)
							.participation(Participation.builder().participationId(participationId).contactId(contactIdRole1).build())
							.participation(Participation.builder().participationId(UUID.randomUUID()).contactId(loggedInUserId).type(ParticipantType.ADMIN).build())
							.build())
							
					.build()));
		
		
		IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
			this.service.deleteParticipation(participationId, loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_NO_ADMIN_USER_WOULD_BE_LEFT, ex.getMessage());
		
	}
	
	/**
	* Tests it is possible to delete a Participant who is the last Role level admin providing there is 
	* still an Admin at the campaign level.
	*/
	@Test
	void testDeleteParticipationAdminLeftAtRoleLevel() {
		
		final UUID 		participationId 	= UUID.randomUUID();
		final UUID 		campaignId 			= UUID.randomUUID();
		final UUID 		roleId 				= UUID.randomUUID();
		final String	contactIdCampaign	= "rec99";
		final String	contactIdRole1		= "rec77";
		final String	contactIdRole2		= "rec11";
		final String 	loggedInUserId 		= "rec88";
		
		Contact loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		when(this.mockParticipationDao.fetchParticipationById(participationId)).thenReturn(Optional.of(Participation.builder().participationId(participationId).campaignId(campaignId).roleId(roleId).build()));
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(Campaign
				.builder()
					.participation(Participation.builder().contactId(contactIdCampaign).type(ParticipantType.ADMIN).build())
					.participation(Participation.builder().contactId(loggedInUserId).type(ParticipantType.ADMIN).build())
					.role(Role.builder()
							.id(roleId)
							.participation(Participation.builder().participationId(participationId).contactId(contactIdRole1).build())
							.participation(Participation.builder().participationId(UUID.randomUUID()).contactId(contactIdRole2).build())
							.build())
							
					.build()));
		
		
		this.service.deleteParticipation(participationId, loggedInUserId);
		
		verify(this.mockParticipationDao).deleteById(participationId);
		
	}
	
	/**
	* Tests it is possible to delete a Participant who is admin at Campaign level providing there is 
	* another Campaign level Admin
	*/
	@Test
	void testDeleteParticipationAdminLeftAtCampaignLevel() {
		
		final UUID 		participationId 		= UUID.randomUUID();
		final UUID 		campaignId 				= UUID.randomUUID();
		final String	contactIdCampaign		= "rec99";
		final String 	loggedInUserId 			= "rec88";
		final Contact 	loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		when(this.mockParticipationDao.fetchParticipationById(participationId)).thenReturn(Optional.of(Participation.builder().participationId(participationId).campaignId(campaignId).build()));
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(Campaign
				.builder()
					.participation(Participation.builder().participationId(participationId).contactId(contactIdCampaign).type(ParticipantType.ADMIN).build())
					.participation(Participation.builder().participationId(UUID.randomUUID()).contactId(loggedInUserId).type(ParticipantType.ADMIN).build())		
					.build()));
		
		
		this.service.deleteParticipation(participationId, loggedInUserId);
		
		verify(this.mockParticipationDao).deleteById(participationId);
		
	}
	
	/**
	* Tests it is possible to delete a Participant who is admin at Campaign level providing there is 
	* another Campaign level Admin
	*/
	@Test
	void testDeleteParticipationLoggedInUserIsNotAdminAtCampaignOrRoleLevel() {
		
		final UUID 		participationId 		= UUID.randomUUID();
		final UUID 		campaignId 				= UUID.randomUUID();
		final String	contactIdCampaign		= "rec99";
		final String 	loggedInUserId 			= "rec88";
		final Contact 	loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		when(this.mockParticipationDao.fetchParticipationById(participationId)).thenReturn(Optional.of(Participation.builder().participationId(participationId).campaignId(campaignId).build()));
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(Campaign
				.builder()
					.participation(Participation.builder().participationId(participationId).contactId(contactIdCampaign).type(ParticipantType.ADMIN).build())
					.participation(Participation.builder().participationId(UUID.randomUUID()).contactId("notAdmin").type(ParticipantType.ADMIN).build())		
					.build()));
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.deleteParticipation(participationId, loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_NO_ADMIN_RIGHTS, ex.getMessage());
		
	}
	
	/**
	* Tests a User without a paid subscription cannot delete a Partition
	*/
	@Test
	void testDeleteParticipationLoggedInUserNotPaidUSer() {
		
		final UUID 		participationId 		= UUID.randomUUID();
		final String 	loggedInUserId 			= "rec88";
		final Contact 	loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.CREDIT);
		
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		
		RuntimeException ex = assertThrows(RuntimeException.class, () -> {
			this.service.deleteParticipation(participationId, loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE, ex.getMessage());
		
	}

	/**
	* Test case where loggedInUser does not have a paid subscription and cannot
	* add a new Note to a Campaigin  
	*/
	@Test
	void testAddNoteToCampaignLoggedInUserNotPaidUser() {
		
		final UUID		campaignId				= UUID.randomUUID();
		final String 	loggedInUserId 			= "rec2";
		final Contact 	loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.CREDIT);
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(Campaign.builder().build()));
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		
		RuntimeException ex = assertThrows(RuntimeException.class, () -> {
			this.service.addNoteToCampaign(campaignId, null, "title", "text", loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE, ex.getMessage());
		
	}
	
	/**
	* Tests case where an attempt is made to add a Note to a Campaign
	* that doesn't exist 
	*/
	@Test
	void testAddNoteToCampaignUnknownCampaign() {
		
		final UUID		campaignId				= UUID.randomUUID();
		final String 	loggedInUserId 			= "rec2";
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.empty());
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.addNoteToCampaign(campaignId, null, "title", "text", loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_UNKNOWN_CAMPAIGN, ex.getMessage());
		
	}
	
	/**
	* Tests case where attempt is made to add a Note to a Campaign 
	* but the loggedIn User is nether and Admin or Edit user 
	*/
	@Test
	void testAddNoteToCampaignUserNotAdminOrEditForCampaign() {
		
		final UUID		campaignId				= UUID.randomUUID();
		final String 	loggedInUserId 			= "rec2";
		final Contact 	loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Campaign	campaign				= Campaign
				.builder()
					.participation(Participation.builder().contactId(loggedInUserId).type(ParticipantType.VIEW).build())
				.build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.addNoteToCampaign(campaignId, null, "title", "text", loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_NO_ADMIN_RIGHTS, ex.getMessage());
		
	}
	
	/**
	* Tests case where attempt is made to add a Note to a Campaign 
	* and the User is an Admin at Campaign level 
	*/
	@Test
	void testAddNoteToCampaignUserIsAdminAdminCampaign() {
		
		final ArgumentCaptor<Campaign>	argCaptCampaign = ArgumentCaptor.forClass(Campaign.class);
		
		final UUID		campaignId				= UUID.randomUUID();
		final String 	loggedInUserId 			= "rec2";
		final Contact 	loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final String 	title					= "title";
		final String 	text					= "some text";
		final Campaign	campaign				= Campaign
				.builder()
					.participation(Participation.builder().contactId(loggedInUserId).type(ParticipantType.ADMIN).build())
				.build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		doNothing().when(this.mockCampaignDao).saveCampaign(argCaptCampaign.capture());
		
		this.service.addNoteToCampaign(campaignId, null, title, text, loggedInUserId);
		
		verify(this.mockCampaignDao).saveCampaign(any(Campaign.class));
		
		Note savedNote = argCaptCampaign.getValue().getNotes().stream().findFirst().orElseThrow();
		
		assertEquals(title, 		savedNote.getTitle().get());
		assertEquals(text, 			savedNote.getText());
		assertTrue(savedNote.getRoleId().isEmpty());
		assertEquals(campaignId, 	savedNote.getCampaignId());
		
	}
	
	/**
	* Tests case where attempt is made to add a Note to a Campaign 
	* and the User is an Edit at Campaign level 
	*/
	@Test
	void testAddNoteToCampaignUserIsEditCampaign() {
		
		final ArgumentCaptor<Campaign>	argCaptCampaign = ArgumentCaptor.forClass(Campaign.class);
		
		final UUID		campaignId				= UUID.randomUUID();
		final String 	loggedInUserId 			= "rec2";
		final Contact 	loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final String 	title					= "title";
		final String 	text					= "some text";
		final Campaign	campaign				= Campaign
				.builder()
					.participation(Participation.builder().contactId(loggedInUserId).type(ParticipantType.EDIT).build())
				.build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		doNothing().when(this.mockCampaignDao).saveCampaign(argCaptCampaign.capture());
		
		this.service.addNoteToCampaign(campaignId, null, title, text, loggedInUserId);
		
		verify(this.mockCampaignDao).saveCampaign(any(Campaign.class));
		
		Note savedNote = argCaptCampaign.getValue().getNotes().stream().findFirst().orElseThrow();
		
		assertEquals(title, 		savedNote.getTitle().get());
		assertEquals(text, 			savedNote.getText());
		assertTrue(savedNote.getRoleId().isEmpty());
		assertEquals(campaignId, 	savedNote.getCampaignId());
		
	}
	
	/**
	* Tests case where attempt is made to add a Note to a Role 
	* but the loggedIn User is nether and Admin or Edit user 
	*/
	@Test
	void testAddNoteToCampaignUserNotAdminOrEditForRole() {
		
		final UUID		campaignId				= UUID.randomUUID();
		final UUID		roleId					= UUID.randomUUID();
		final String 	loggedInUserId 			= "rec2";
		final Contact 	loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Campaign	campaign				= Campaign
				.builder()
					.participation(Participation.builder().roleId(roleId).contactId(loggedInUserId).type(ParticipantType.VIEW).build())
				.build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			this.service.addNoteToCampaign(campaignId, null, "title", "text", loggedInUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_NO_ADMIN_RIGHTS, ex.getMessage());
		
	}
	
	/**
	* Tests case where attempt is made to add a Note to a Role 
	* and the loggedIn User is Admin at Role level
	*/
	@Test
	void testAddNoteToCampaignUserIsAdminAtRoleLevel() {
		
		final ArgumentCaptor<Campaign>	argCaptCampaign = ArgumentCaptor.forClass(Campaign.class);
		
		final UUID		campaignId				= UUID.randomUUID();
		final UUID		roleId					= UUID.randomUUID();
		final String 	loggedInUserId 			= "rec2";
		final Contact 	loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final String 	title					= "title";
		final String 	text					= "some text";
		final Campaign	campaign				= Campaign
				.builder()
					.role(Role
						.builder()
							.id(roleId)
							.participation(Participation.builder().contactId(loggedInUserId).type(ParticipantType.ADMIN).build())
						.build())
				.build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		doNothing().when(this.mockCampaignDao).saveCampaign(argCaptCampaign.capture());
		
		this.service.addNoteToCampaign(campaignId, roleId, title, text, loggedInUserId);
		
		verify(this.mockCampaignDao).saveCampaign(any(Campaign.class));
		
		Note savedNote = argCaptCampaign.getValue().getNotes().stream().findFirst().orElseThrow();
		
		assertEquals(title, 		savedNote.getTitle().get());
		assertEquals(text, 			savedNote.getText());
		assertEquals(roleId, 		savedNote.getRoleId().orElseThrow());
		assertEquals(campaignId, 	savedNote.getCampaignId());
		
	}
	
	/**
	* Tests case where attempt is made to add a Note to a Role 
	* and the loggedIn User is Edit at Role level
	*/
	@Test
	void testAddNoteToCampaignUserIsEditAtRoleLevel() {
		
		final ArgumentCaptor<Campaign>	argCaptCampaign = ArgumentCaptor.forClass(Campaign.class);
		
		final UUID		campaignId				= UUID.randomUUID();
		final UUID		roleId					= UUID.randomUUID();
		final String 	loggedInUserId 			= "rec2";
		final Contact 	loggedInUserContact 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final String 	title					= "title";
		final String 	text					= "some text";
		final Campaign	campaign				= Campaign
				.builder()
					.role(Role
						.builder()
							.id(roleId)
							.participation(Participation.builder().contactId(loggedInUserId).type(ParticipantType.EDIT).build())
						.build())
				.build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(loggedInUserId)).thenReturn(Optional.of(loggedInUserContact));
		doNothing().when(this.mockCampaignDao).saveCampaign(argCaptCampaign.capture());
		
		this.service.addNoteToCampaign(campaignId, roleId, title, text, loggedInUserId);
		
		verify(this.mockCampaignDao).saveCampaign(any(Campaign.class));
		
		Note savedNote = argCaptCampaign.getValue().getNotes().stream().findFirst().orElseThrow();
		
		assertEquals(title, 		savedNote.getTitle().get());
		assertEquals(text, 			savedNote.getText());
		assertEquals(roleId, 		savedNote.getRoleId().orElseThrow());
		assertEquals(campaignId, 	savedNote.getCampaignId());
		
	}
	
	/**
	* Tests it is not possible to Update a Note if the User 
	* does not have an active paid subscription
	*/
	@Test
	void testUpdateNoteUserNotPaidUser() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final String 	title 			= "a title";
		final String 	body 			= "note text";
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.CREDIT);
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.updateNote(noteId, title, body, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE, ex.getMessage());
		
	}
	
	/**
	* Tests it is not possible to Delete a Note if the User 
	* does not have an active paid subscription
	*/
	@Test
	void testDeleteNoteUserNotPaidUser() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.CREDIT);
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.deleteNote(noteId, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE, ex.getMessage());
		
	}
	
	/**
	* Tests it is not possible to Delete a Note if the User 
	* does not have an active paid subscription
	*/
	@Test
	void testAddAppointmentNotPaidUser() {
		
		final UUID 				campaignId		= UUID.randomUUID();
		final UUID 				roleId			= UUID.randomUUID();
		final String 			name			= "Appointment name";
		final String 			description		= "appointment desc";
		final String 			phoneNumber		= "0031 643 220 866";
		final String 			videoLink		= "https:www.vidapp1.com/dsad11";
		final ZonedDateTime 	when 			= ZonedDateTime.now();
		final String 			currentUserId 	= "rec35";
		final Contact 			currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.CREDIT);
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.addAppointment(campaignId, roleId, name, description, phoneNumber, videoLink, when, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE, ex.getMessage());
		
	}
	
	/**
	* Tests it is not possible to Delete a Note if the User 
	* does not have an active paid subscription
	*/
	@Test
	void testUpdateAppointmentNotPaidUser() {
		
		final UUID 				appointmentId	= UUID.randomUUID();
		final String 			name			= "Appointment name";
		final String 			description		= "appointment desc";
		final String 			phoneNumber		= "0031 643 220 866";
		final String 			videoLink		= "https:www.vidapp1.com/dsad11";
		final ZonedDateTime 	when 			= ZonedDateTime.now();
		final String 			currentUserId 	= "rec35";
		final Contact 			currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.CREDIT);
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.updateAppointment(appointmentId, name, description, phoneNumber, videoLink, when, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE, ex.getMessage());
		
	}
	
	/**
	* Tests it is not possible to Delete a Note if the User 
	* does not have an active paid subscription
	*/
	@Test
	void testDeleteAppointmentNotPaidUser() {
		
		final UUID 				appointmentId	= UUID.randomUUID();
		final String 			currentUserId 	= "rec35";
		final Contact 			currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.CREDIT);
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.deleteAppointment(appointmentId, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE, ex.getMessage());
		
	}
	
	/**
	* Tests it is not possible to add a Document if the User 
	* does not have an active paid subscription
	*/
	@Test
	void testAddDocumentNotPaidUser() {
		
		final UUID 				campaignId		= UUID.randomUUID();
		final UUID 				roleId			= UUID.randomUUID();
		final String 			title			= "doc title";
		final DocumentType 		type			= DocumentType.PDF;
		final byte[] 			bytes			= new byte[] {};
		final String 			currentUserId 	= "rec35";
		final Contact 			currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.CREDIT);
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.addDocument(campaignId, roleId, title, type, bytes, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE, ex.getMessage());
		
	}
	
	/**
	* Tests it is not possible to delete a Document if the User 
	* does not have an active paid subscription
	*/
	@Test
	void testDeleteDocumentNotPaidUser() {
		
		final UUID 				documentId		= UUID.randomUUID();
		final String 			currentUserId 	= "rec35";
		final Contact 			currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.CREDIT);
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		
		RuntimeException ex = assertThrows(RuntimeException.class, ()-> {
			this.service.deleteDocument(documentId, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE, ex.getMessage());
		
	}
	
	/**
	* Tests exception is thrown if Note if not found 
	*/
	@Test
	void testUpdateNoteUnknownNote() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final String 	title 			= "a title";
		final String 	body 			= "note text";
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.empty());
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()-> {
			this.service.updateNote(noteId, title, body, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_UNKNOWN_NOTE, ex.getMessage());
		
	}
	
	/**
	* Tests exception is thrown if Note if not found 
	*/
	@Test
	void testDeleteNoteUnknownNote() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.empty());
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()-> {
			this.service.deleteNote(noteId, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_UNKNOWN_NOTE, ex.getMessage());
		
	}
	
	/**
	* Tests exception is thrown if an attempt is made to update a Note and 
	* its associated Campaign is not found 
	*/
	@Test
	void testUpdateNoteUnknownCampaign() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final String 	title 			= "a title";
		final String 	body 			= "note text";
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Note		note			= Note.builder().campaignId(UUID.randomUUID()).build();
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.of(note));
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()-> {
			this.service.updateNote(noteId, title, body, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_UNKNOWN_CAMPAIGN, ex.getMessage());
		
	}
	
	/**
	* Tests exception is thrown if an attempt is made to delete a Note and 
	* its associated Campaign is not found 
	*/
	@Test
	void testDeleteNoteUnknownCampaign() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Note		note			= Note.builder().campaignId(UUID.randomUUID()).build();
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.of(note));
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()-> {
			this.service.deleteNote(noteId, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_UNKNOWN_CAMPAIGN, ex.getMessage());
		
	}

	/**
	* Tests exception is thrown an attempt is made to update 
	* a Note by a User that has no participation for the Note 
	* at any level
	*/
	@Test
	void testUpdateNoteUserNotParticipantAtAnyLevel() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final UUID		campaignId		= UUID.randomUUID();
		final String 	title 			= "a title";
		final String 	body 			= "note text";
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Note		note			= Note.builder().campaignId(campaignId).build();
		final Campaign	campaign		= Campaign.builder().build();
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.of(note));
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()-> {
			this.service.updateNote(noteId, title, body, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_NO_ADMIN_RIGHTS, ex.getMessage());
		
	}
	
	/**
	* Tests exception is thrown an attempt is made to delete 
	* a Note by a User that has no participation for the Note 
	* at any level
	*/
	@Test
	void testDeleteNoteUserNotParticipantAtAnyLevel() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final UUID		campaignId		= UUID.randomUUID();
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Note		note			= Note.builder().campaignId(campaignId).build();
		final Campaign	campaign		= Campaign.builder().build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.of(note));
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()-> {
			this.service.deleteNote(noteId, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_NO_ADMIN_RIGHTS, ex.getMessage());
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	* Tests exception is thrown if attempt made to update a 
	* Note by a User who does not have an Admin or Edit 
	* Participation level for that Note
	*/
	@Test
	void testUpdateNoteUserNotParticipantAtAdminOrEditLevel() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final UUID		campaignId		= UUID.randomUUID();
		final String 	title 			= "a title";
		final String 	body 			= "note text";
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Note		note			= Note.builder().campaignId(campaignId).build();
		final Campaign	campaign		= Campaign.builder().participation(Participation.builder().contactId(currentUserId).type(ParticipantType.VIEW).build()).build();
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.of(note));
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()-> {
			this.service.updateNote(noteId, title, body, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_NO_ADMIN_RIGHTS, ex.getMessage());
		
	}
	
	/**
	* Tests exception is thrown if attempt made to delete a 
	* Note by a User who does not have an Admin or Edit 
	* Participation level for that Note
	*/
	@Test
	void testDeleteNoteUserNotParticipantAtAdminOrEditLevel() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final UUID		campaignId		= UUID.randomUUID();
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Note		note			= Note.builder().campaignId(campaignId).build();
		final Campaign	campaign		= Campaign.builder().participation(Participation.builder().contactId(currentUserId).type(ParticipantType.VIEW).build()).build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.of(note));
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()-> {
			this.service.deleteNote(noteId, currentUserId);
		});
		
		assertEquals(CampaignServiceImpl.ERR_MSG_NO_ADMIN_RIGHTS, ex.getMessage());
		
	}
	
	/**
	* Tests happy path for Update where User has Admin level participation at Campaign level
	*/
	@Test
	void testUpdateNoteUserParticipantAtCampaignAdminLevel() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final UUID		campaignId		= UUID.randomUUID();
		final String 	title 			= "a title";
		final String 	body 			= "note text";
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Note		note			= Note.builder().campaignId(campaignId).id(noteId).title("oldTitle").text("oldBody").build();
		final Campaign	campaign		= Campaign.builder().note(note).participation(Participation.builder().contactId(currentUserId).type(ParticipantType.ADMIN).build()).build();
		
		ArgumentCaptor<Campaign> capaignArgCapt = ArgumentCaptor.forClass(Campaign.class);
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.of(note));
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		doNothing().when(mockCampaignDao).saveCampaign(capaignArgCapt.capture());
		
		this.service.updateNote(noteId, title, body, currentUserId);
		
		verify(this.mockCampaignDao).saveCampaign(any(Campaign.class));
		
		assertEquals(1, capaignArgCapt.getValue().getNotes().size());
		
		Note updatedNote = capaignArgCapt.getValue().getNotes().stream().findFirst().orElseThrow();
		
		assertEquals(noteId, updatedNote.getId());
		assertEquals(title, updatedNote.getTitle().orElseThrow());
		assertEquals(body, updatedNote.getText());
		
	}
	
	/**
	* Tests happy path for Delete where User has Admin level participation at Campaign level
	*/
	@Test
	void testDeleteNoteUserNotParticipantAtCampaignAdminLevel() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final UUID		campaignId		= UUID.randomUUID();
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Note		note			= Note.builder().campaignId(campaignId).build();
		final Campaign	campaign		= Campaign.builder().participation(Participation.builder().contactId(currentUserId).type(ParticipantType.ADMIN).build()).build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.of(note));
		
		this.service.deleteNote(noteId, currentUserId);
		
		verify(this.mockNoteDao).deleteById(noteId);
		
	}
	
	/**
	* Tests happy path for Update where User has Admin level participation at Campaign level
	*/
	@Test
	void testUpdateNoteUserParticipantAtCampaignEditLevel() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final UUID		campaignId		= UUID.randomUUID();
		final String 	title 			= "a title";
		final String 	body 			= "note text";
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Note		note			= Note.builder().campaignId(campaignId).id(noteId).title("oldTitle").text("oldBody").build();
		final Campaign	campaign		= Campaign.builder().note(note).participation(Participation.builder().contactId(currentUserId).type(ParticipantType.EDIT).build()).build();
		
		ArgumentCaptor<Campaign> capaignArgCapt = ArgumentCaptor.forClass(Campaign.class);
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.of(note));
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		doNothing().when(mockCampaignDao).saveCampaign(capaignArgCapt.capture());
		
		this.service.updateNote(noteId, title, body, currentUserId);
		
		verify(this.mockCampaignDao).saveCampaign(any(Campaign.class));
		
		assertEquals(1, capaignArgCapt.getValue().getNotes().size());
		
		Note updatedNote = capaignArgCapt.getValue().getNotes().stream().findFirst().orElseThrow();
		
		assertEquals(noteId, updatedNote.getId());
		assertEquals(title, updatedNote.getTitle().orElseThrow());
		assertEquals(body, updatedNote.getText());
		
	}
	
	/**
	* Tests happy path for Delete where User has Admin level participation at Campaign level
	*/
	@Test
	void testDeleteNoteUserNotParticipantAtCampaignEditLevel() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final UUID		campaignId		= UUID.randomUUID();
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Note		note			= Note.builder().campaignId(campaignId).build();
		final Campaign	campaign		= Campaign.builder().participation(Participation.builder().contactId(currentUserId).type(ParticipantType.EDIT).build()).build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.of(note));
		
		this.service.deleteNote(noteId, currentUserId);
		
		verify(this.mockNoteDao).deleteById(noteId);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	* Tests happy path for Update where User has Admin level participation at Role level
	*/
	@Test
	void testUpdateNoteUserParticipantAtRoleAdminLevel() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final UUID		campaignId		= UUID.randomUUID();
		final UUID		roleId			= UUID.randomUUID();
		final String 	title 			= "a title";
		final String 	body 			= "note text";
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Note		note			= Note.builder().campaignId(campaignId).roleId(roleId).id(noteId).title("oldTitle").text("oldBody").build();
		final Role		role			= Role.builder().id(roleId).participation(Participation.builder().contactId(currentUserId).type(ParticipantType.ADMIN).build()).build();
		final Campaign	campaign		= Campaign.builder().role(role).note(note).build();
		
		ArgumentCaptor<Campaign> capaignArgCapt = ArgumentCaptor.forClass(Campaign.class);
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.of(note));
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		doNothing().when(mockCampaignDao).saveCampaign(capaignArgCapt.capture());
		
		this.service.updateNote(noteId, title, body, currentUserId);
		
		verify(this.mockCampaignDao).saveCampaign(any(Campaign.class));
		
		assertEquals(1, capaignArgCapt.getValue().getNotes().size());
		
		Note updatedNote = capaignArgCapt.getValue().getNotes().stream().findFirst().orElseThrow();
		
		assertEquals(noteId, updatedNote.getId());
		assertEquals(title, updatedNote.getTitle().orElseThrow());
		assertEquals(body, updatedNote.getText());
		
	}
	
	/**
	* Tests happy path for Delete where User has Admin level participation at Role level
	*/
	@Test
	void testDeleteNoteUserNotParticipantAtRoleAdminLevel() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final UUID		campaignId		= UUID.randomUUID();
		final UUID		roleId			= UUID.randomUUID();
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Note		note			= Note.builder().campaignId(campaignId).roleId(roleId).build();
		final Role		role			= Role.builder().id(roleId).participation(Participation.builder().contactId(currentUserId).type(ParticipantType.ADMIN).build()).build();
		final Campaign	campaign		= Campaign.builder().role(role).build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.of(note));
		
		this.service.deleteNote(noteId, currentUserId);
		
		verify(this.mockNoteDao).deleteById(noteId);
		
	}
	
	/**
	* Tests happy path for Update where User has Admin level participation at Campaign level
	*/
	@Test
	void testUpdateNoteUserParticipantAtRoleEditLevel() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final UUID		campaignId		= UUID.randomUUID();
		final UUID		roleId			= UUID.randomUUID();
		final String 	title 			= "a title";
		final String 	body 			= "note text";
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Note		note			= Note.builder().campaignId(campaignId).roleId(roleId).id(noteId).title("oldTitle").text("oldBody").build();
		final Role		role			= Role.builder().id(roleId).participation(Participation.builder().contactId(currentUserId).type(ParticipantType.EDIT).build()).build();
		final Campaign	campaign		= Campaign.builder().role(role).build();
		
		ArgumentCaptor<Campaign> capaignArgCapt = ArgumentCaptor.forClass(Campaign.class);
		
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.of(note));
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		doNothing().when(mockCampaignDao).saveCampaign(capaignArgCapt.capture());
		
		this.service.updateNote(noteId, title, body, currentUserId);
		
		verify(this.mockCampaignDao).saveCampaign(any(Campaign.class));
		
		assertEquals(1, capaignArgCapt.getValue().getNotes().size());
		
		Note updatedNote = capaignArgCapt.getValue().getNotes().stream().findFirst().orElseThrow();
		
		assertEquals(noteId, updatedNote.getId());
		assertEquals(title, updatedNote.getTitle().orElseThrow());
		assertEquals(body, updatedNote.getText());
		
	}
	
	/**
	* Tests happy path for Delete where User has Admin level participation at Campaign level
	*/
	@Test
	void testDeleteNoteUserNotParticipantAtRoleEditLevel() {
		
		final UUID 		noteId 			= UUID.randomUUID();
		final UUID		campaignId		= UUID.randomUUID();
		final UUID		roleId			= UUID.randomUUID();
		final String 	currentUserId 	= "rec35";
		final Contact 	currentUser 	= new Contact("rec2", "bilbo", "baggins", "bibo@bag.nl", SubscriptionType.PAID);
		final Note		note			= Note.builder().campaignId(campaignId).roleId(roleId).build();
		final Role		role			= Role.builder().id(roleId).participation(Participation.builder().contactId(currentUserId).type(ParticipantType.EDIT).build()).build();
		final Campaign	campaign		= Campaign.builder().role(role).build();
		
		when(this.mockCampaignDao.fetchCampaign(campaignId)).thenReturn(Optional.of(campaign));
		when(this.mockContactDao.fetchContact(currentUserId)).thenReturn(Optional.of(currentUser));
		when(this.mockNoteDao.fetchNoteById(noteId)).thenReturn(Optional.of(note));
		
		this.service.deleteNote(noteId, currentUserId);
		
		verify(this.mockNoteDao).deleteById(noteId);
		
	}
	
	
}