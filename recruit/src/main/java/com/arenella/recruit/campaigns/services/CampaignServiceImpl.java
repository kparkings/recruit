package com.arenella.recruit.campaigns.services;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.campaign.dao.CampaignDao;
import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.arenella.recruit.campaigns.beans.Document;
import com.arenella.recruit.campaigns.beans.Document.DocumentType;
import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;

/**
* Services for working with Campaigns 
*/
public class CampaignServiceImpl implements CampaignService{

	private final CampaignDao campaignDao;
	
	/**
	* Constructor
	* @param campaignDao
	*/
	public CampaignServiceImpl(CampaignDao campaignDao) {
		this.campaignDao = campaignDao;
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
		return null;
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public Void addCampaign(String name, String description, CampaignLogo logo, String currentUserId) {
		return null;
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