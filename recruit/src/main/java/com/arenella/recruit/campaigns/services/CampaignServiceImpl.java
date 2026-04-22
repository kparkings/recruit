package com.arenella.recruit.campaigns.services;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import com.arenella.recruit.campaign.dao.CampaignDao;
import com.arenella.recruit.campaign.dao.ContactEntityDao;
import com.arenella.recruit.campaign.dao.ParticipationEntityDao;
import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;
import com.arenella.recruit.campaigns.beans.Document;
import com.arenella.recruit.campaigns.beans.Participation;
import com.arenella.recruit.campaigns.beans.Document.DocumentType;
import com.arenella.recruit.campaigns.beans.Note;
import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;
import com.arenella.recruit.campaigns.beans.Role;

/**
* Services for working with Campaigns 
*/
@Service
public class CampaignServiceImpl implements CampaignService{

	public static final String ERR_MSG_UNKNOWN_CAMPAIGN 				= "Cannot retrieve unknown Campaign.";
	public static final String ERR_MSG_USER_NOT_PARTICIPANT 			= "You are not a participant in the Campaign.";
	public static final String ERR_MSG_CONTACT_NOT_FOUND 				= "Unknown Contact.";
	public static final String ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE = "Only paid subscription users can perform this action.";
	public static final String ERR_MSG_CAMPAIGN_NOT_FOUND 				= "Unknown Campaign.";
	public static final String ERR_MSG_CONTACT_ALREADY_PARTICIPANT 		= "Cannot add existing participant.";
	public static final String ERR_MSG_NO_ADMIN_ROLE_FOR_USER			= "Only Admin Users can perform this action.";
	public static final String ERR_MSG_UNKNOWN_PARTICIPATION			= "Unknown Participation.";
	public static final String ERR_MSG_UNKNOWN_ROLE						= "Unknown Role";
	public static final String ERR_MSG_NO_ADMIN_USER_WOULD_BE_LEFT		= "An Admin Participant must be present after action taken.";
	public static final String ERR_MSG_NO_ADMIN_RIGHTS					= "No rights to perform this action.";
	
	private final CampaignDao 				campaignDao;
	private final ContactEntityDao 			contactDao;
	private final ParticipationEntityDao 	participationDao;
	
	/**
	* Constructor
	* @param campaignDao		- For working with Campaigns
	* @param contactDao			- For working with Contacts
	* @param participationDao	- For working with Paricipation's
	*/
	public CampaignServiceImpl(CampaignDao campaignDao, ContactEntityDao contactDao, ParticipationEntityDao participationDao) {
		this.campaignDao 		= campaignDao;
		this.contactDao 		= contactDao;
		this.participationDao 	= participationDao;
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
		
		this.fetchAndValidateContactForCurrentUser(currentUserId);
		
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
		
		//TODO: [KP] Think we need to add the case for Admin at Role level but not Campaign level who can add other Admin users to the Role
		
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void deleteParticipation(UUID participationId, String currentUserId) {
	
		this.fetchAndValidateContactForCurrentUser(currentUserId);
		
		Participation 	participation 	= this.participationDao.fetchParticipationById(participationId).orElseThrow(()-> new IllegalArgumentException(ERR_MSG_UNKNOWN_PARTICIPATION));
		Campaign 		campaign 		= this.campaignDao.fetchCampaign(participation.getCampaignId()).orElseThrow(()-> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		this.checkLoggedInUserIsAdminForParticipation(campaign, participation, currentUserId);
		this.checkAtLeastOneAdminUserLeftAfterAction(campaign, participation);
		
		this.participationDao.deleteById(participationId);
	}
	
	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void addNoteToCampaign(UUID campaignId, UUID roleId, String title, String text, String currentUserId) {
		
		Campaign campaign = this.campaignDao.fetchCampaign(campaignId).orElseThrow(()-> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		this.fetchAndValidateContactForCurrentUser(currentUserId);
		this.checkLoggedInUserIsAdminOrEditForCampaignOrRole(campaign, roleId, currentUserId);
		
		this.campaignDao.saveCampaign(Campaign
				.builder()
				.from(campaign)
				.note(Note
						.builder()
							.campaignId(campaignId)
							.created(LocalDateTime.now())
							.id(UUID.randomUUID())
							.roleId(roleId)
							.text(text)
							.title(title)
						.build())
				.build());
		
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
		return this.contactDao.fetchContact(currentUserId).orElseThrow(() ->new IllegalArgumentException(ERR_MSG_CONTACT_NOT_FOUND));
	}
	
	/**
	* Checks that loggedIn user is Admin or Edit either at the Role or Campaign level
	* If not throws an Exception
	*/
	private void checkLoggedInUserIsAdminOrEditForCampaignOrRole(Campaign campaign, UUID roleId, String currentUserId) {
		
		AtomicBoolean adminAtCampaignLevel 	= new AtomicBoolean(campaign.getParticipations().stream().anyMatch(p -> p.getContactId().equals(currentUserId) && (p.getType() == ParticipantType.ADMIN || p.getType() == ParticipantType.EDIT)));
		AtomicBoolean adminAtRoleLevel 		= new AtomicBoolean(false);
	
		Optional.ofNullable(roleId).ifPresent(rId -> 
			campaign.getRoles().stream().filter(r -> r.getId() == rId).findAny().ifPresent(matchingRole ->
				adminAtRoleLevel.set(matchingRole.getParticipations().stream().anyMatch(p -> p.getContactId().equals(currentUserId) && (p.getType() == ParticipantType.ADMIN || p.getType() == ParticipantType.EDIT)))
			)
		);
	
		if (!adminAtCampaignLevel.get() && !adminAtRoleLevel.get()) {
			throw new IllegalArgumentException(ERR_MSG_NO_ADMIN_RIGHTS);
		}
		
	}
	
	/**
	* Checks that loggedIn user is Admin either at the Role or Campaign level
	* If not throws an Exception
	*/
	private void checkLoggedInUserIsAdminForParticipation(Campaign campaign, Participation participation, String currentUserId) {
		
		AtomicBoolean adminAtCampaignLevel 	= new AtomicBoolean(campaign.getParticipations().stream().anyMatch(p -> p.getContactId().equals(currentUserId) && p.getType() == ParticipantType.ADMIN));
		AtomicBoolean adminAtRoleLevel 		= new AtomicBoolean(false);
	
		participation.getRoleId().ifPresent(roleId -> {
			Role role = campaign.getRoles().stream().filter(r -> r.getId() == roleId).findAny().orElseThrow(()-> new IllegalArgumentException(ERR_MSG_UNKNOWN_ROLE));
			adminAtRoleLevel.set(role.getParticipations().stream().anyMatch(p -> p.getContactId().equals(currentUserId) && p.getType() == ParticipantType.ADMIN));
		});
		
		if (!adminAtCampaignLevel.get() && !adminAtRoleLevel.get()) {
			throw new IllegalArgumentException(ERR_MSG_NO_ADMIN_RIGHTS);
		}
		
	}
	
	/**
	* Validates that if removing the participation then there would still be at least on Admin user at 
	* either the Role or the Campaign Level. Otherwise throws an exception
	* @param campaign
	* @param participation
	*/
	private void checkAtLeastOneAdminUserLeftAfterAction(Campaign campaign, Participation participation) {
	
		AtomicBoolean adminAtCampaignLevelIfParticipantRemoved 	= new AtomicBoolean(campaign.getParticipations().stream().anyMatch(p -> (p.getParticipationId() != participation.getParticipationId()) && p.getType() == ParticipantType.ADMIN));
		AtomicBoolean adminAtRoleLevelIfParticipantRemoved 		= new AtomicBoolean(false);
		
		participation.getRoleId().ifPresent(roleId -> {
			Role role = campaign.getRoles().stream().filter(r -> r.getId() == roleId).findAny().orElseThrow(()-> new IllegalArgumentException(ERR_MSG_UNKNOWN_ROLE));
			adminAtRoleLevelIfParticipantRemoved.set(role.getParticipations().stream().anyMatch(p -> p.getParticipationId() != participation.getParticipationId() && p.getType() == ParticipantType.ADMIN));
		});
		
		if (!adminAtRoleLevelIfParticipantRemoved.get() && !adminAtCampaignLevelIfParticipantRemoved.get()) {
			throw new IllegalStateException(ERR_MSG_NO_ADMIN_USER_WOULD_BE_LEFT);
		}
		
		if (!adminAtCampaignLevelIfParticipantRemoved.get()) {
			throw new IllegalStateException(ERR_MSG_NO_ADMIN_USER_WOULD_BE_LEFT);
		}
		
	}

}