package com.arenella.recruit.campaigns.services;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.arenella.recruit.campaigns.beans.Document;
import com.arenella.recruit.campaigns.beans.Document.DocumentType;
import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;

/**
* Services for working with Campaigns 
*/
public class CampaignServiceImpl implements CampaignService{

	@Override
	public Set<Campaign> fetchCampaignsForUser(String currentUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Campaign fetchCampaignById(UUID campaignId, String currentUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void addCampaign(String name, String description, CampaignLogo logo, String currentUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addParticipationToCampaign(String contactId, UUID campaignId, UUID roleId, ParticipantType type,
			String currentUserId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteParticipation(UUID participationId, String currentUserId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNotToCampaign(UUID campaignId, UUID roleId, String title, String text, String currentUserId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNote(UUID noteId, String title, String text, String currentUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteNote(UUID noteId, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAppointment(UUID campaignId, UUID roleId, String name, String description, String phoneNumber,
			String videoLink, ZonedDateTime when, String currentUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAppointment(UUID appointmentId, String name, String description, String phoneNumber,
			String videoLink, ZonedDateTime when, String currentUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAppointment(UUID appointmentId, String currentUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDocument(UUID campaignId, UUID roleId, String title, DocumentType type, byte[] bytes,
			String currentUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteDocument(UUID documentId, String currentUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Document fetchCampaignDocument(UUID documentId, String currentUser) {
		// TODO Auto-generated method stub
		return null;
	}

}
