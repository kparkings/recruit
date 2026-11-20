package com.arenella.recruit.campaigns.services;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;

import com.arenella.recruit.campaign.dao.ContactEntityDao;
import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.beans.Participation;
import com.arenella.recruit.campaigns.beans.Role;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;
import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;

/**
* There are various services related to Campaigns but the security rules are shared.
* This utility defines the rules and allows them to be shared between relevant services
*/
@Component
public class CampaignAuthUtility {

	public static final String ERR_MSG_UNKNOWN_CAMPAIGN 					= "Cannot retrieve unknown Campaign.";
	public static final String ERR_MSG_NO_ADMIN_USER_WOULD_BE_LEFT			= "An Admin Participant must be present after action taken.";
	public static final String ERR_MSG_CONTACT_NOT_FOUND 					= "Unknown Contact.";
	public static final String ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE 	= "Only paid subscription users can perform this action.";
	public static final String ERR_MSG_NO_ADMIN_RIGHTS						= "No rights to perform this action.";
	public static final String ERR_MSG_UNKNOWN_ROLE							= "Unknown Role";
	
	private final ContactEntityDao contactDao;
	
	public CampaignAuthUtility(ContactEntityDao contactDao) {
		this.contactDao = contactDao;
	}
	
	/**
	* Attempts to retrieve the Contact of the current user and validate that
	* they have access to Campaigns
	* @param currentUserId - Id of currently logged in User
	* @return Contact for User
	*/
	public Contact fetchAndValidateContactForCurrentUser(String currentUserId) {
		
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
	public Contact fetchContactFor(String currentUserId) {
		return this.contactDao.fetchContact(currentUserId).orElseThrow(() ->new IllegalArgumentException(ERR_MSG_CONTACT_NOT_FOUND));
	}
	
	/**
	* Checks that loggedIn user is Admin or Edit either at the Role or Campaign level
	* If not throws an Exception
	*/
	public void checkLoggedInUserIsAdminOrEditForCampaignOrRole(Campaign campaign, UUID roleId, String currentUserId) {
		
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
	* Checks that loggedIn user is Participant at the Role or Campaign level
	* If not throws an Exception
	*/
	public void checkLoggedInUserIsParticipantForCampaignOrRole(Campaign campaign, UUID roleId, String currentUserId) {
		
		AtomicBoolean adminAtCampaignLevel 	= new AtomicBoolean(campaign.getParticipations().stream().anyMatch(p -> p.getContactId().equals(currentUserId) && (p.getType() == ParticipantType.ADMIN || p.getType() == ParticipantType.EDIT || p.getType() == ParticipantType.VIEW)));
		AtomicBoolean adminAtRoleLevel 		= new AtomicBoolean(false);
	
		Optional.ofNullable(roleId).ifPresent(rId -> 
			campaign.getRoles().stream().filter(r -> r.getId() == rId).findAny().ifPresent(matchingRole ->
				adminAtRoleLevel.set(matchingRole.getParticipations().stream().anyMatch(p -> p.getContactId().equals(currentUserId) && (p.getType() == ParticipantType.ADMIN || p.getType() == ParticipantType.EDIT|| p.getType() == ParticipantType.VIEW)))
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
	public void checkLoggedInUserIsAdminForParticipation(Campaign campaign, Participation participation, String currentUserId) {
		
		AtomicBoolean adminAtCampaignLevel 	= new AtomicBoolean(campaign.getParticipations().stream().anyMatch(p -> p.getContactId().equals(currentUserId) && p.getType() == ParticipantType.ADMIN));
		AtomicBoolean adminAtRoleLevel 		= new AtomicBoolean(false);
	
		participation.getRoleId().ifPresent(roleId -> {
			Role role = campaign.getRoles().stream().filter(r -> r.getId() .equals(roleId)).findAny().orElseThrow(()-> new IllegalArgumentException(ERR_MSG_UNKNOWN_ROLE));
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
	public void checkAtLeastOneAdminUserLeftAfterAction(Campaign campaign, Participation participation) {
	
		AtomicBoolean adminAtCampaignLevelIfParticipantRemoved 	= new AtomicBoolean(campaign.getParticipations().stream().filter(p -> p.getRoleId().isEmpty()).anyMatch(p -> (p.getParticipationId() != participation.getParticipationId()) && p.getType() == ParticipantType.ADMIN));
		AtomicBoolean adminAtRoleLevelIfParticipantRemoved 		= new AtomicBoolean(false);
		
		participation.getRoleId().ifPresent(roleId -> {
			Role role = campaign.getRoles().stream().filter(r -> r.getId().equals(roleId)).findAny().orElseThrow(()-> new IllegalArgumentException(ERR_MSG_UNKNOWN_ROLE));
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
