package com.arenella.recruit.campaigns.services;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.arenella.recruit.campaign.dao.AppointmentEntityDao;
import com.arenella.recruit.campaign.dao.CampaignDao;
import com.arenella.recruit.campaign.dao.ContactEntityDao;
import com.arenella.recruit.campaign.dao.DocumentEntityDao;
import com.arenella.recruit.campaign.dao.NoteEntityDao;
import com.arenella.recruit.campaign.dao.ParticipationEntityDao;
import com.arenella.recruit.campaigns.beans.Appointment;
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
	public static final String ERR_MSG_UNKNOWN_NOTE 					= "Cannot retrieve unknown Note.";
	public static final String ERR_MSG_UNKNOWN_APPOINTMENT 				= "Cannot retrieve unknown Appointment.";
	public static final String ERR_MSG_UNKNOWN_DOCUMENT 				= "Cannot retrieve unknown Document.";
	public static final String ERR_MSG_UNSUPPORTED_DOC_TYPE 			= "Document type not supported.";
	
	
	private final CampaignDao 					campaignDao;
	private final ContactEntityDao 				contactDao;
	private final ParticipationEntityDao 		participationDao;
	private final NoteEntityDao					noteDao;
	private final AppointmentEntityDao			appointmentDao;
	private final DocumentEntityDao				documentDao;
	private final CampaignFileSecurityParser	fileSecurityParser;
	
	/**
	* Constructor
	* @param campaignDao		- For working with Campaigns
	* @param contactDao			- For working with Contacts
	* @param participationDao	- For working with Paricipation's
	* @param appointmentDao		- For working with Appointments
	* @param documentDao		- For working with Documents
	* @param fileSecurityParser	- To check file is of type specified
	*/
	public CampaignServiceImpl(
			CampaignDao 				campaignDao, 
			ContactEntityDao 			contactDao, 
			ParticipationEntityDao 		participationDao, 
			NoteEntityDao 				noteDao, 
			AppointmentEntityDao 		appointmentDao,
			DocumentEntityDao			documentDao,
			CampaignFileSecurityParser	fileSecurityParser) {
		this.campaignDao 		= campaignDao;
		this.contactDao 		= contactDao;
		this.participationDao 	= participationDao;
		this.noteDao 			= noteDao;
		this.appointmentDao		= appointmentDao;
		this.documentDao  		= documentDao;
		this.fileSecurityParser = fileSecurityParser;
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
		
		Campaign campaign = this.campaignDao.fetchCampaign(campaignId).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_CAMPAIGN_NOT_FOUND));
		
		this.fetchContactFor(contactId);
		
		AtomicBoolean isAdminAtRoleLevel = new AtomicBoolean(false);
		
		Optional.ofNullable(roleId).ifPresent(rId -> 
			campaign.getRoles().stream().filter(r -> r.getId() == rId).findAny().ifPresent(role -> 
				role.getParticipations().stream().filter(p -> p.getType() == ParticipantType.ADMIN && p.getContactId().equals(currentUserId)).findAny().ifPresent(_ -> 
					isAdminAtRoleLevel.set(true)
				)
			)
		);
		
		if (!isAdminAtRoleLevel.get()) {
			campaign.getParticipations()
				.stream()
				.filter(p -> p.getContactId().equals(currentUserId) && p.getType() == ParticipantType.ADMIN)
				.findAny().orElseThrow(() -> new RuntimeException(ERR_MSG_NO_ADMIN_ROLE_FOR_USER));
		}
		
		Optional.ofNullable(roleId).ifPresent(rId -> 
			campaign.getRoles().stream().filter(r -> r.getId() == rId).findAny().ifPresent(role -> 
				role.getParticipations().stream().filter(p -> p.getContactId().equals(contactId)).findAny().ifPresent(_ -> {
					throw new IllegalArgumentException(ERR_MSG_CONTACT_ALREADY_PARTICIPANT);
				})
			)
		);
		
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
	public void updateNote(UUID noteId, String title, String text, String currentUserId) {
		
		this.fetchAndValidateContactForCurrentUser(currentUserId);
		
		Note 		note 		= this.noteDao.fetchNoteById(noteId).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_NOTE));
		Campaign 	campaign 	= this.campaignDao.fetchCampaign(note.getCampaignId()).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		this.checkLoggedInUserIsAdminOrEditForCampaignOrRole(campaign, note.getRoleId().orElse(null), currentUserId);
		
		this.campaignDao.saveCampaign(Campaign
				.builder()
					.from(campaign)
					.notes(campaign.getNotes().stream().filter(n -> n.getId() != noteId).collect(Collectors.toSet()))
					.note(Note
							.builder()
								.from(note)
								.title(title)
								.text(text)
							.build())
				.build());
		
	}
	
	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void deleteNote(UUID noteId, String currentUserId) {
		
		this.fetchAndValidateContactForCurrentUser(currentUserId);
		
		Note 		note 		= this.noteDao.fetchNoteById(noteId).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_NOTE));
		Campaign 	campaign 	= this.campaignDao.fetchCampaign(note.getCampaignId()).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		
		this.checkLoggedInUserIsAdminOrEditForCampaignOrRole(campaign, note.getRoleId().orElse(null), currentUserId);
		
		this.noteDao.deleteById(noteId);
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void addAppointment(UUID campaignId, UUID roleId, String name, String description, String phoneNumber, String videoLink, ZonedDateTime when, String currentUserId) {
	
		this.fetchAndValidateContactForCurrentUser(currentUserId);
		
		Campaign 	campaign 	= this.campaignDao.fetchCampaign(campaignId).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
	
		this.checkLoggedInUserIsAdminOrEditForCampaignOrRole(campaign, roleId, currentUserId);
		
		Appointment appointment = Appointment
				.builder()
					.campaignId(campaignId)
					.appointmentId(UUID.randomUUID())
					.description(description)
					.name(name)
					.phoneNumber(phoneNumber)
					.roleId(roleId)
					.videoLink(videoLink)
					.when(when)
				.build();
		
		this.campaignDao.saveCampaign(Campaign
				.builder()
					.from(campaign)
					.appointment(appointment)
				.build());
		
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void updateAppointment(UUID appointmentId, String name, String description, String phoneNumber, String videoLink, ZonedDateTime when, String currentUserId) {
		
		this.fetchAndValidateContactForCurrentUser(currentUserId);
		
		Appointment appointment = this.appointmentDao.fetchAppointmentById(appointmentId).orElseThrow(() 	 -> new IllegalArgumentException(ERR_MSG_UNKNOWN_APPOINTMENT));
		Campaign 	campaign 	= this.campaignDao.fetchCampaign(appointment.getCampaignId()).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		this.checkLoggedInUserIsAdminOrEditForCampaignOrRole(campaign, appointment.getRoleId().orElse(null), currentUserId);
		
		Appointment updatedAppointment = Appointment
				.builder()
					.from(appointment)
					.description(description)
					.name(name)
					.phoneNumber(phoneNumber)
					.roleId(appointment.getRoleId().orElse(null))
					.videoLink(videoLink)
					.when(when)
				.build();
		
		this.campaignDao.saveCampaign(Campaign
				.builder()
					.from(campaign)
					.appointments(campaign.getAppointments().stream().filter(a -> a.getAppointmentId() != appointmentId).collect(Collectors.toSet()))
					.appointment(updatedAppointment)
				.build());
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void deleteAppointment(UUID appointmentId, String currentUserId) {
		
		this.fetchAndValidateContactForCurrentUser(currentUserId);
		
		Appointment appointment = this.appointmentDao.fetchAppointmentById(appointmentId).orElseThrow(() 	 -> new IllegalArgumentException(ERR_MSG_UNKNOWN_APPOINTMENT));
		Campaign 	campaign 	= this.campaignDao.fetchCampaign(appointment.getCampaignId()).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		this.checkLoggedInUserIsAdminOrEditForCampaignOrRole(campaign, appointment.getRoleId().orElse(null), currentUserId);
		
		this.appointmentDao.deleteById(appointmentId);
		
	}

	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void addDocument(UUID campaignId, UUID roleId, String title, DocumentType type, byte[] bytes, String currentUserId) {	
		
		this.fetchAndValidateContactForCurrentUser(currentUserId);
		
		Campaign campaign = this.campaignDao.fetchCampaign(campaignId).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		this.checkLoggedInUserIsAdminOrEditForCampaignOrRole(campaign, roleId, currentUserId);
		
		if (!this.fileSecurityParser.isSafe(bytes)) {		
			throw new IllegalArgumentException(ERR_MSG_UNSUPPORTED_DOC_TYPE);
		}
		
		this.documentDao.saveDocument(Document
				.builder()
					.bytes(bytes)
					.campaignId(campaignId)
					.created(LocalDateTime.now())
					.documentId(UUID.randomUUID())
					.roleId(roleId)
					.title(title)
					.type(type)
				.build());
		
	}
	
	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void deleteDocument(UUID documentId, String currentUserId) {
		
		this.fetchAndValidateContactForCurrentUser(currentUserId);
		
		Document document = this.documentDao.fetchDocumentById(documentId).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_DOCUMENT));
		Campaign campaign = this.campaignDao.fetchCampaign(document.getCampaignId()).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		this.checkLoggedInUserIsAdminOrEditForCampaignOrRole(campaign, document.getRoleId().orElse(null), currentUserId);
		
		this.documentDao.deleteById(documentId);
		
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