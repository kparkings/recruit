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

	public static final String ERR_MSG_UNKNOWN_CAMPAIGN = "Cannot retrieve unknown Campaign.";
	public static final String ERR_MSG_USER_NOT_PARTICIPANT = "You are not a participant in the Campaign.";
	public static final String ERR_MSG_CONTACT_NOT_FOUND = "Unknown Contact.";
	public static final String ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE = "Only paid subscription users can create Campaigns";
	
	
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
		Contact currentUser = this.contactDao.fetchContact(currentUserId).orElseThrow(() ->new IllegalArgumentException(ERR_MSG_CONTACT_NOT_FOUND));
		
		if (currentUser.subscriptionType() == SubscriptionType.CREDIT) {
			throw new RuntimeException(ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE);
		}
		
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

}