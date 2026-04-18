package com.arenella.recruit.campaigns.services;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.arenella.recruit.campaign.dao.CampaignDao;
import com.arenella.recruit.campaign.dao.ContactEntityDao;
import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;
import com.arenella.recruit.campaigns.beans.Document;
import com.arenella.recruit.campaigns.beans.Participation;
import com.arenella.recruit.campaigns.beans.Document.DocumentType;
import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;

/**
* Services for working with Campaigns 
*/
@Service
public class CampaignServiceImpl implements CampaignService{

	public static final String ERR_MSG_UNKNOWN_CAMPAIGN 				= "Cannot retrieve unknown Campaign.";
	public static final String ERR_MSG_USER_NOT_PARTICIPANT 			= "You are not a participant in the Campaign.";
	public static final String ERR_MSG_CONTACT_NOT_FOUND 				= "Unknown Contact.";
	public static final String ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE = "Only paid subscription users can create Campaigns";
	public static final String ERR_MSG_CAMPAIGN_NOT_FOUND 				= "Unknown Campaign.";
	public static final String ERR_MSG_CONTACT_ALREADY_PARTICIPANT 		= "Cannot add existing participant.";
	public static final String ERR_MSG_NO_ADMIN_ROLE_FOR_USER			= "Only Admin Users can perform this action.";
	
	
	private final CampaignDao 		campaignDao;
	private final ContactEntityDao 	contactDao;
	
	/**
	* Constructor
	* @param campaignDao
	*/
	public CampaignServiceImpl(CampaignDao campaignDao, ContactEntityDao contactDao) {
		this.campaignDao 	= campaignDao;
		this.contactDao 	= contactDao;
	}
	
	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public Set<Campaign> fetchCampaignsForUser(String currentUserId) {
		return this.campaignDao.fetchCampaignsForUser(currentUserId);
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public Campaign fetchCampaignById(UUID campaignId, String currentUserId) {
		
		Campaign campaign = this.campaignDao.fetchCampaign(campaignId).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		campaign.getParticipations().stream().filter(p -> p.getContactId().equals(currentUserId)).findAny().orElseThrow(() -> new IllegalArgumentException(ERR_MSG_USER_NOT_PARTICIPANT));
		
		return campaign;
	}
	
	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void addCampaign(String name, String description, CampaignLogo logo, String currentUserId) {
		
		UUID 	campaignId 	= UUID.randomUUID();
		
		fetchAndValidateContactForCurrentUser(currentUserId);
		
		Campaign campaign = Campaign
				.builder()
					.created(LocalDateTime.now())
					.description(description)
					.id(campaignId)
					.logo(logo)
					.name(name)
					.participation(Participation
							.builder()
								.campaignId(campaignId)
								.contactId(currentUserId)
								.participationId(UUID.randomUUID())
								.type(Participation.ParticipantType.ADMIN)
							.build())
				.build();
		
		this.campaignDao.saveCampaign(campaign);
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void addParticipationToCampaign(String contactId, UUID campaignId, UUID roleId, ParticipantType type, String currentUserId) {
		
		Campaign 	campaign 			= this.campaignDao.fetchCampaign(campaignId).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_CAMPAIGN_NOT_FOUND));
		
		this.fetchAndValidateContactForCurrentUser(currentUserId);
		this.fetchContactFor(contactId);
		
		campaign.getParticipations()
			.stream()
			.filter(p -> p.getContactId().equals(currentUserId) && p.getType() == ParticipantType.ADMIN)
			.findAny().orElseThrow(() -> new RuntimeException(ERR_MSG_NO_ADMIN_ROLE_FOR_USER));
		
		campaign.getParticipations().stream().filter(p -> p.getContactId().equals(contactId)).findAny().ifPresent(_ -> {
			throw new IllegalArgumentException(ERR_MSG_CONTACT_ALREADY_PARTICIPANT);
		});
		
		this.campaignDao.saveCampaign(Campaign.builder().from(campaign)
				.participation(Participation
						.builder()
							.participationId(UUID.randomUUID())
							.campaignId(campaignId)
							.contactId(contactId)
							.roleId(roleId)
							.type(type)
						.build()).build());
		
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void deleteParticipation(UUID participationId, String currentUserId) {
		
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void addNotToCampaign(UUID campaignId, UUID roleId, String title, String text, String currentUserId) {
		
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void updateNote(UUID noteId, String title, String text, String currentUser) {
		
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void deleteNote(UUID noteId, String name) {
		
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void addAppointment(UUID campaignId, UUID roleId, String name, String description, String phoneNumber, String videoLink, ZonedDateTime when, String currentUser) {
		
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void updateAppointment(UUID appointmentId, String name, String description, String phoneNumber, String videoLink, ZonedDateTime when, String currentUser) {
		
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void deleteAppointment(UUID appointmentId, String currentUser) {
		
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void addDocument(UUID campaignId, UUID roleId, String title, DocumentType type, byte[] bytes, String currentUser) {	
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void deleteDocument(UUID documentId, String currentUser) {
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public Document fetchCampaignDocument(UUID documentId, String currentUser) {
		return null;
	}
	
	/**
	* Attempts to retrieve the Contact of the current user and validate that
	* they have access to Campaigns
	* @param currentUserId - Id of currently logged in User
	* @return Contact for User
	*/
	private Contact fetchAndValidateContactForCurrentUser(String currentUserId) {
		
		Contact currentUser = this.contactDao.fetchContact(currentUserId).orElseThrow(() ->new IllegalArgumentException(ERR_MSG_CONTACT_NOT_FOUND));
		
		if (currentUser.subscriptionType() == SubscriptionType.CREDIT) {
			throw new RuntimeException(ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE);
		}
		
		return currentUser;
		
	}
	
	/**
	* Attempts to retrieve the Contact 
	* @param contactId - Id of contact to retrieve
	* @return Contact for User
	*/
	private Contact fetchContactFor(String currentUserId) {
		
		Contact currentUser = this.contactDao.fetchContact(currentUserId).orElseThrow(() ->new IllegalArgumentException(ERR_MSG_CONTACT_NOT_FOUND));
		
		return currentUser;
		
	}

}