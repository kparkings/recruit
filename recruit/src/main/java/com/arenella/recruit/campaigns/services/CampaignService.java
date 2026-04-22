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
* Defines services for working with Campaigns 
*/
public interface CampaignService {

	/**
	* Returns any Campaign in which the User is a Participant
	* @param currentUserId - Id of the authenticated User
	* @return User's Campaigns
	*/
	Set<Campaign> fetchCampaignsForUser(String currentUserId);

	/**
	* Returns the Campaign based on the provided Id
	* @param campaignId 	- Id of the Campaign to return
	* @param currentUserId 	- Id of the authenticated User
	* @return Campaign
	*/
	Campaign fetchCampaignById(UUID campaignId, String currentUserId);

	/**
	* Adds a new Campaign
	* @param name			- Name of the Campaign
	* @param description	- Description of the Campaign
	* @param logo			- Logo		
	* @param currentUserId 	- Id of the authenticated User
	* @return
	*/
	void addCampaign(String name, String description, CampaignLogo logo, String currentUserId);

	/**
	* Adds a new Participation to an existing Campaign
	* @param contactId	 - Id of the Contact that will be participating in the Campaign
	* @param campaignId	 - Unique id of the Campaign
	* @param roleId		 - Unique id of the Role if provided
	* @param type		 - Type of participation the contact will have in the Campaign
	* @param currentUserId 	- Id of the authenticated User			
	*/
	void addParticipationToCampaign(String contactId, UUID campaignId, UUID roleId, ParticipantType type, String currentUserId);

	/**
	* Deletes an existing Participation from an existing Campaign
	* @param participationId -  Id of the Participation to delete
	* @param currentUserId 	 - Id of the authenticated User
	*/
	void deleteParticipation(UUID participationId, String currentUserId);

	/**
	* Adds a new Note to the Campaign
	* @param campaignId		- Id of the Campaign to add the Note to
	* @param roleId			- If Role level Note, the Id of the Role
	* @param title			- If present the title of the Note
	* @param text			- Note text body
	* @param currentUserId 	- Id of the authenticated User
	*/
	void addNoteToCampaign(UUID campaignId, UUID roleId, String title, String text, String currentUserId);

	/**
	* Updates an existing Note
	* @param noteId			- Unique Identifier of the Note
	* @param title			- title for the Note
	* @param text			- Body text of the Note
	* @param currentUser 	- Id of the authenticated User
	*/
	void updateNote(UUID noteId, String title, String text, String currentUser);

	/**
	* Delete an existing Note
	* @param noteId 		- Id of the Note to delete
	* @param currentUser 	- Id of the authenticated User
	*/
	void deleteNote(UUID noteId, String name);

	/**
	* Adds an Appointment to an existing Campaign
	* @param campaignId		- Id of the Campaign
	* @param roleId			- If the Appointment is at Role level the Id of the Role
	* @param name			- Name of the Appointment
	* @param description	- Description of the Appointment
	* @param phoneNumber	- Phone number to call for the Appointment if via Phone
	* @param videoLink		- Video link to use for the Appointment is via Video
	* @param when			- When the Appointment will take place
	* @param currentUser 	- Id of the authenticated User
	*/
	void addAppointment(UUID campaignId, UUID roleId, String name, String description, String phoneNumber, String videoLink, ZonedDateTime when, String currentUser);

	/**
	* Updates an existing appointment
	* @param appointmentId	- Id of the appointment to update
	* @param name			- name of the appointment
	* @param description	- Description of the Appointment
	* @param phoneNumber	- Phone number to call for the Appointment if via Phone
	* @param videoLink		- Video link to use for the Appointment is via Video
	* @param when			- When the Appointment will take place
	* @param currentUser 	- Id of the authenticated User
	*/
	void updateAppointment(UUID appointmentId, String name, String description, String phoneNumber, String videoLink, ZonedDateTime when, String currentUser);

	/**
	* Deletes an existing appointment
	* @param appointmentId 	- Id of the Appointment to delete
	* @param currentUser 	- Id of the authenticated User
	*/
	void deleteAppointment(UUID appointmentId, String currentUser);

	/**
	* Adds a Document to a Campaign
	* @param campaignId		- Id to associate the Document with
	* @param roleId			- If Role level document the Id of the Role to associate the Document with
	* @param title			- Title of the Document
	* @param type			- Type of the Document
	* @param bytes			- Bytes of the actual Document
	* @param currentUser	- Id of the authenticated User		
	*/
	void addDocument(UUID campaignId, UUID roleId, String title, DocumentType type, byte[] bytes, String currentUser);

	/**
	* Deletes the Document with the given Id
	* @param documentId 	- Id of the Document to delete
	* @param currentUser 	- Id of the authenticated User
	*/
	void deleteDocument(UUID documentId, String currentUser);

	/**
	* Retrieves a Document based upon its Id
	* @param documentId 	- Id of Document to retrieve
	* @param currentUser 	- Id of the authenticated User		
	*/
	Document fetchCampaignDocument(UUID documentId, String currentUser);

} 