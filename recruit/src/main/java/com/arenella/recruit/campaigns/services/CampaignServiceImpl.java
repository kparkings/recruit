package com.arenella.recruit.campaigns.services;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.arenella.recruit.campaign.dao.AppointmentEntityDao;
import com.arenella.recruit.campaign.dao.CampaignDao;
import com.arenella.recruit.campaign.dao.CandidateEntityDao;
import com.arenella.recruit.campaign.dao.ContactEntityDao;
import com.arenella.recruit.campaign.dao.DocumentEntityDao;
import com.arenella.recruit.campaign.dao.NoteEntityDao;
import com.arenella.recruit.campaign.dao.ParticipationEntityDao;
import com.arenella.recruit.campaign.dao.RoleDao;
import com.arenella.recruit.campaigns.adapters.CampaignExternalEventPublisher;
import com.arenella.recruit.campaigns.adapters.ExternalCandiateAddedToSystemSendEmailCommand;
import com.arenella.recruit.campaigns.adapters.ExternalCandiateMessageSendEmailCommand;
import com.arenella.recruit.campaigns.beans.Appointment;
import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.arenella.recruit.campaigns.beans.Candidate;
import com.arenella.recruit.campaigns.beans.Contact;
import com.arenella.recruit.campaigns.beans.Contact.SubscriptionType;
import com.arenella.recruit.campaigns.beans.Document;
import com.arenella.recruit.campaigns.beans.Participation;
import com.arenella.recruit.campaigns.beans.Document.DocumentType;
import com.arenella.recruit.campaigns.beans.Note;
import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;
import com.arenella.recruit.campaigns.beans.Role;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient;
import com.arenella.recruit.emailservice.beans.Email.EmailRecipient.ContactType;
import com.arenella.recruit.campaigns.beans.Candidate.Type;

/**
* Services for working with Campaigns 
*/
@Service
public class CampaignServiceImpl implements CampaignService{

	public static final String ERR_MSG_UNKNOWN_CAMPAIGN 					= "Cannot retrieve unknown Campaign.";
	public static final String ERR_MSG_USER_NOT_PARTICIPANT 				= "You are not a participant in the Campaign.";
	public static final String ERR_MSG_CONTACT_NOT_FOUND 					= "Unknown Contact.";
	public static final String ERR_MSG_ADD_CAMPAIGN_FEATURE_UNAVAILABLE 	= "Only paid subscription users can perform this action.";
	public static final String ERR_MSG_CAMPAIGN_NOT_FOUND 					= "Unknown Campaign.";
	public static final String ERR_MSG_UNKNOWN_CANDIDATE					= "Unknown Candidate.";
	public static final String ERR_MSG_CONTACT_ALREADY_PARTICIPANT 			= "Cannot add existing participant.";
	public static final String ERR_MSG_NO_ADMIN_ROLE_FOR_USER				= "Only Admin Users can perform this action.";
	public static final String ERR_MSG_UNKNOWN_PARTICIPATION				= "Unknown Participation.";
	public static final String ERR_MSG_UNKNOWN_ROLE							= "Unknown Role";
	public static final String ERR_MSG_NO_ADMIN_USER_WOULD_BE_LEFT			= "An Admin Participant must be present after action taken.";
	public static final String ERR_MSG_NO_ADMIN_RIGHTS						= "No rights to perform this action.";
	public static final String ERR_MSG_UNKNOWN_NOTE 						= "Cannot retrieve unknown Note.";
	public static final String ERR_MSG_UNKNOWN_APPOINTMENT 					= "Cannot retrieve unknown Appointment.";
	public static final String ERR_MSG_UNKNOWN_DOCUMENT 					= "Cannot retrieve unknown Document.";
	public static final String ERR_MSG_UNSUPPORTED_DOC_TYPE 				= "Document type not supported.";
	public static final String ERR_MSG_UNASSOCIATED_CANDIDATE_CAMPAIGN 		= "Cant send message to External User not associated with the Campaign";
	public static final String ERR_MSG_UNASSOCIATED_CANDIDATE_ROLE	 		= "Cant send message to External User not associated with the Role";
	
	private final CampaignDao 						campaignDao;
	private final ContactEntityDao 					contactDao;
	private final ParticipationEntityDao 			participationDao;
	private final NoteEntityDao						noteDao;
	private final AppointmentEntityDao				appointmentDao;
	private final DocumentEntityDao					documentDao;
	private final CampaignFileSecurityParser		fileSecurityParser;
	private final RoleDao							roleDao;
	private final CandidateEntityDao				candidateDao;
	private final CampaignExternalEventPublisher 	eventPublisher;
	
	/**
	* Constructor
	* @param campaignDao		- For working with Campaigns
	* @param contactDao			- For working with Contacts
	* @param participationDao	- For working with Paricipation's
	* @param appointmentDao		- For working with Appointments
	* @param documentDao		- For working with Documents
	* @param fileSecurityParser	- To check file is of type specified
	* @param roleDao			- For working with Roles
	* @param candidateDao		- For working with Candidates
	* @param eventPublisher		- For publishing events/commands to external services
	*/
	public CampaignServiceImpl(
			CampaignDao 					campaignDao, 
			ContactEntityDao 				contactDao, 
			ParticipationEntityDao 			participationDao, 
			NoteEntityDao 					noteDao, 
			AppointmentEntityDao 			appointmentDao,
			DocumentEntityDao				documentDao,
			CampaignFileSecurityParser		fileSecurityParser,
			RoleDao							roleDao,
			CandidateEntityDao				candidateDao,
			CampaignExternalEventPublisher 	eventPublisher) {
		this.campaignDao 		= campaignDao;
		this.contactDao 		= contactDao;
		this.participationDao 	= participationDao;
		this.noteDao 			= noteDao;
		this.appointmentDao		= appointmentDao;
		this.documentDao  		= documentDao;
		this.fileSecurityParser = fileSecurityParser;
		this.roleDao			= roleDao;
		this.candidateDao		= candidateDao;
		this.eventPublisher		= eventPublisher;
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
		
		if (Optional.ofNullable(roleId).isEmpty()) {
			campaign.getParticipations().stream().filter(p -> p.getContactId().equals(contactId) && p.getRoleId().isEmpty()).findAny().ifPresent(_ -> {
				throw new IllegalArgumentException(ERR_MSG_CONTACT_ALREADY_PARTICIPANT);
			});
		} else {
			campaign.getParticipations().stream().filter(p -> p.getContactId().equals(contactId) && p.getRoleId().isPresent() && p.getRoleId().get().equals(roleId)).findAny().ifPresent(_ -> {
				throw new IllegalArgumentException(ERR_MSG_CONTACT_ALREADY_PARTICIPANT);
			});
		}
		
		this.participationDao.saveParticipation(Participation
						.builder()
							.participationId(UUID.randomUUID())
							.campaignId(campaignId)
							.contactId(contactId)
							.roleId(roleId)
							.type(type)
						.build());
		
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
		
		if (participation.getRoleId().isEmpty()) {
			campaign = Campaign
				.builder()
				.from(campaign)
				.participants(campaign.getParticipations().stream().filter(p -> !p.getParticipationId().equals(participationId)).collect(Collectors.toSet()))
				.build();
			
			this.campaignDao.saveCampaign(campaign);
			
		} else {
			
			Role role = this.roleDao.fetchRoleById(participation.getRoleId().get()).get();
			
			role = Role.builder().from(role).participants(role.getParticipations().stream().filter(p -> !p.getParticipationId().equals(participationId)).collect(Collectors.toSet())).build();
			
			this.roleDao.saveRole(role, campaign.getId());
			
		}
	
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
		
		if (note.getRoleId().isPresent()) {
			note.getRoleId().ifPresent(roleId -> {
				
				Role role = this.roleDao.fetchRoleById(roleId).get();
				
				role = Role.builder().from(role).notes(role.getNotes().stream().filter(n -> !n.getId().equals(noteId)).collect(Collectors.toSet())).build();
				
				this.roleDao.saveRole(role, campaign.getId());
				
			});
		} else {
			this.campaignDao.saveCampaign(Campaign
				.builder()
					.from(campaign)
					.notes(campaign.getNotes().stream().filter(n -> n.getId() != noteId).collect(Collectors.toSet()))
				.build());
		}
		
		
		
		
		//this.noteDao.deleteById(noteId);
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
		Role role = null;
		
		if (document.getRoleId().isPresent()) {
			role = campaign.getRoles().stream().filter(aRole -> aRole.getId().equals(document.getRoleId().get())).findFirst().get();
		}
		
		this.checkLoggedInUserIsAdminOrEditForCampaignOrRole(campaign, document.getRoleId().orElse(null), currentUserId);
		
		if (Optional.ofNullable(role).isEmpty()) {
			this.campaignDao.saveCampaign(Campaign.builder().from(campaign).documents(campaign.getDocuments().stream().filter(d -> !d.getDocumentId().equals(documentId)).collect(Collectors.toSet())).build());
		}else {
			this.roleDao.saveRole(Role.builder().from(role).documents(role.getDocuments().stream().filter(d -> !d.getDocumentId().equals(documentId)).collect(Collectors.toSet())).build(), campaign.getId());
		}
		
	}
	
	/**
	* Refer to the CampaignService interface for details 
	*/
	@Override
	public void addRole(UUID campaignId, String name, String description, String currentUserId) {
		
		final UUID roleId = UUID.randomUUID();
		
		Campaign campaign = this.campaignDao.fetchCampaign(campaignId).orElseThrow(()-> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		this.fetchAndValidateContactForCurrentUser(currentUserId);
		this.checkLoggedInUserIsAdminOrEditForCampaignOrRole(campaign, null, currentUserId);
		
		this.roleDao.saveRole(Role
			.builder()
				.id(roleId)
				.name(name)
				.description(description)
				.created(LocalDateTime.now())
				.participation(Participation
						.builder()
							.campaignId(campaignId)
							.roleId(roleId)
							.contactId(currentUserId)
							.participationId(UUID.randomUUID())
							.type(Participation.ParticipantType.ADMIN)
						.build())
			.build(), campaignId);
		
	}
	
	/**
	* Refer to the CampaignService interface for details
	*/
	@Override
	public void deleteCampaign(UUID campaignId, String name) {
		
		Campaign campaign = this.campaignDao.fetchCampaign(campaignId).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		campaign.getParticipations().stream().filter(p -> p.getContactId().equals(name) && p.getType() == ParticipantType.ADMIN).findFirst().orElseThrow(()-> new IllegalArgumentException(ERR_MSG_NO_ADMIN_ROLE_FOR_USER));
		
		this.campaignDao.deleteById(campaignId);
		
	}

	/**
	* Refer to the CampaignService interface for details
	*/
	@Override
	public void deleteRole(UUID roleId, String name) {
		
		Role role = this.roleDao.fetchRoleById(roleId).orElseThrow(()-> new IllegalArgumentException(ERR_MSG_UNKNOWN_ROLE));
		
		role.getParticipations().stream().filter(p -> p.getContactId().equals(name) && p.getType() == ParticipantType.ADMIN).findFirst().orElseThrow(()-> new IllegalArgumentException(ERR_MSG_NO_ADMIN_ROLE_FOR_USER));
	
		this.roleDao.deleteById(roleId);
		
	}
	
	/**
	* Refer to the CampaignService interface for details
	*/
	@Override
	public void addCandidateToCampaign(UUID campaignId, UUID roleId, String candidateId, String currentUser) {
		
		Campaign 	campaign;
		Role 		role = null;
		Candidate 	candidate;
		
		//0. Check user has access to Campaigns
		this.fetchAndValidateContactForCurrentUser(currentUser);
		
		// 1. Check campaign exists
		campaign = this.campaignDao.fetchCampaign(campaignId).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		// 2. Check Role exists if Role level		
		if (Optional.ofNullable(roleId).isPresent()) {
			role = this.roleDao.fetchRoleById(roleId).orElseThrow(()-> new IllegalArgumentException(ERR_MSG_UNKNOWN_ROLE));
		}

		// 3. Check candidate exists
		candidate = this.candidateDao.findCandidateById(candidateId).orElseThrow(()-> new IllegalArgumentException(ERR_MSG_UNKNOWN_CANDIDATE));
		
		// 4. Check is admin or edit participant 
		this.checkLoggedInUserIsAdminOrEditForCampaignOrRole(campaign, roleId, currentUser);
		
		//5. Add Candidate to Campaign or role
		
		if (Optional.ofNullable(role).isEmpty()) {
			this.campaignDao.saveCampaign(Campaign.builder().from(campaign).candidate(candidate).build());
		}else {
			this.roleDao.saveRole(Role.builder().from(role).candidate(candidate).build(), campaignId);
		}
		
	}
	
	/**
	* Refer to the CampaignService interface for details
	*/
	@Override
	public void deleteCandidateFromCampaign(UUID campaignId, UUID roleId, String candidateId, String currentUser) {
		
		Campaign 	campaign;
		Role 		role = null;
		
		//0. Check user has access to Campaigns
		this.fetchAndValidateContactForCurrentUser(currentUser);
		
		// 1. Check campaign exists
		campaign = this.campaignDao.fetchCampaign(campaignId).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		// 2. Check Role exists if Role level		
		if (Optional.ofNullable(roleId).isPresent()) {
			role = this.roleDao.fetchRoleById(roleId).orElseThrow(()-> new IllegalArgumentException(ERR_MSG_UNKNOWN_ROLE));
		}
	
		// 3. Check is admin or edit participant 
		this.checkLoggedInUserIsAdminOrEditForCampaignOrRole(campaign, roleId, currentUser);
		
		AtomicBoolean isExternalCandiadte = new AtomicBoolean();
		//4. Add Candidate to Campaign or role
		if (Optional.ofNullable(role).isEmpty()) {
			campaign.getCandidates().stream().filter(c -> c.getId().equals(candidateId) && c.getType() == Type.EXTERNAL).findAny().ifPresent(_ -> 
				isExternalCandiadte.set(true)
			);
			this.campaignDao.saveCampaign(Campaign.builder().from(campaign).candidates(campaign.getCandidates().stream().filter(c -> !c.getId().equals(candidateId)).collect(Collectors.toSet())).build());
		} else {
			role.getCandidates().stream().filter(c -> c.getId().equals(candidateId) && c.getType() == Type.EXTERNAL).findAny().ifPresent(_ -> 
				isExternalCandiadte.set(true)
			);
			this.roleDao.saveRole(Role.builder().from(role).candidates(role.getCandidates().stream().filter(c -> !c.getId().equals(candidateId)).collect(Collectors.toSet())).build(), campaignId);
		}
		
		if (isExternalCandiadte.get()) {
			this.candidateDao.deleteById(candidateId);
		}
		
	}
	
	/**
	* Refer to the CampaignService interface for details
	*/
	@Override
	public Document fetchDocumentById(UUID documentId, String currentUser) {
		
		this.fetchAndValidateContactForCurrentUser(currentUser);
		
		Document document = this.documentDao.fetchDocumentById(documentId).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_DOCUMENT));
		Campaign campaign = this.campaignDao.fetchCampaign(document.getCampaignId()).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		this.checkLoggedInUserIsParticipantForCampaignOrRole(campaign, document.getRoleId().orElse(null), currentUser);
		
		return document;
		
	}
	
	/**
	* Refer to the CampaignService interface for details
	*/
	@Override
	public void addExternalCandidateToCampaiginOrRole(UUID campaignId, UUID roleId, Candidate candidate, String currentUser) {
		
		Campaign 	campaign;
		Role 		role = null;
		
		//0. Check user has access to Campaigns
		this.fetchAndValidateContactForCurrentUser(currentUser);
		
		// 1. Check campaign exists
		campaign = this.campaignDao.fetchCampaign(campaignId).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		// 2. Check Role exists if Role level		
		if (Optional.ofNullable(roleId).isPresent()) {
			role = this.roleDao.fetchRoleById(roleId).orElseThrow(()-> new IllegalArgumentException(ERR_MSG_UNKNOWN_ROLE));
		}

		// 4. Check is admin or edit participant 
		this.checkLoggedInUserIsAdminOrEditForCampaignOrRole(campaign, roleId, currentUser);
		
		candidate = Candidate.builder()
				.from(candidate)
				.id(UUID.randomUUID().toString())
				.type(Type.EXTERNAL).build();
		
		this.candidateDao.saveCandidate(candidate);
		
		Contact recruiter = fetchContactFor(currentUser);
		
		EmailRecipient<UUID> recipient = new EmailRecipient<>(UUID.fromString(candidate.getId()), candidate.getId(), ContactType.EXTERNAL_CANDIDATE);
		recipient.setFirstName(candidate.getFirstName());
		recipient.setEmail(candidate.getEmail());
		
		ExternalCandiateAddedToSystemSendEmailCommand command = ExternalCandiateAddedToSystemSendEmailCommand
				.builder()
					.recipients(Set.of(recipient))
					.campaignOrRole(Optional.ofNullable(role).isPresent() ? role.getName() : campaign.getName())
					.recruiterName(recruiter.firstName() + " " + recruiter.surname())
					.recruiterEmail(recruiter.email())
				.build();
		
		this.eventPublisher.publishExternalCandiateMessageSendEmailCommand(command);
		
		//5. Add Candidate to Campaign or role
		
		if (Optional.ofNullable(role).isEmpty()) {
			this.campaignDao.saveCampaign(Campaign.builder().from(campaign).candidate(candidate).build());
		}else {
			this.roleDao.saveRole(Role.builder().from(role).candidate(candidate).build(), campaignId);
		}
		
	}
	
	/**
	* Sends a message related to either a Campaign or a Role to external candidates. That being. Candidates with no 
	* profile but that have been added to the Campaign/Role by the Recruiter. 
	*/
	@Override
	public void messageExternalCampaignCandidates(UUID campaignId, UUID roleId, Set<String> candidateIds, String message, String currentUser) {
		
		Campaign 			campaign;
		Role 				role 		= null;
		Set<Candidate> 		candidates 	= new HashSet<>();
		
		//0. Check user has access to Campaigns
		Contact contact = this.fetchAndValidateContactForCurrentUser(currentUser);
		
		// 1. Check campaign exists
		campaign = this.campaignDao.fetchCampaign(campaignId).orElseThrow(() -> new IllegalArgumentException(ERR_MSG_UNKNOWN_CAMPAIGN));
		
		// 2. Check Role exists if Role level		
		if (Optional.ofNullable(roleId).isPresent()) {
			role = this.roleDao.fetchRoleById(roleId).orElseThrow(()-> new IllegalArgumentException(ERR_MSG_UNKNOWN_ROLE));
		}

		// 4. Check is admin or edit participant 
		this.checkLoggedInUserIsAdminOrEditForCampaignOrRole(campaign, roleId, currentUser);
		
		//5. Check candidateIds are associated with specified campaign or role
		if (Optional.ofNullable(roleId).isPresent()) {
			Set<String> ids = role.getCandidates().stream().filter(c -> c.getType() == Type.EXTERNAL).map(c -> c.getId()).collect(Collectors.toSet());
			candidates.addAll(role.getCandidates().stream().filter(c -> c.getType() == Type.EXTERNAL).collect(Collectors.toSet()));
			candidateIds.stream().filter(candidateId -> !ids.contains(candidateId)).collect(Collectors.toSet()).stream().findAny().ifPresent(_ -> {
				throw new IllegalArgumentException(ERR_MSG_UNASSOCIATED_CANDIDATE_ROLE);
			});
		} else {
			Set<String> ids = campaign.getCandidates().stream().filter(c -> c.getType() == Type.EXTERNAL).map(c -> c.getId()).collect(Collectors.toSet());
			candidates.addAll(campaign.getCandidates().stream().filter(c -> c.getType() == Type.EXTERNAL).collect(Collectors.toSet()));
			candidateIds.stream().filter(candidateId -> !ids.contains(candidateId)).collect(Collectors.toSet()).stream().findAny().ifPresent(_ -> {
				throw new IllegalArgumentException(ERR_MSG_UNASSOCIATED_CANDIDATE_CAMPAIGN);
			});
		}
		
		Set<EmailRecipient<UUID>> recipients = new HashSet<>();
		
		candidateIds.stream().forEach(extCandidate -> {
			
			candidates.stream().filter(c -> c.getId().equals(extCandidate)).findAny().ifPresent(candidate -> {
				if (!candidate.isDeleteFromSystem()) {
					EmailRecipient<UUID> recipient = new EmailRecipient<>(UUID.fromString(candidate.getId()), extCandidate, ContactType.EXTERNAL_CANDIDATE);
					recipient.setFirstName(candidate.getFirstName());
					recipient.setEmail(candidate.getEmail());
					recipients.add(recipient);
				}
			});
		});
		
		ExternalCandiateMessageSendEmailCommand command = ExternalCandiateMessageSendEmailCommand
				.builder()
					.recipients(recipients)
					.message(message)
					.campaignOrRole(Optional.ofNullable(role).isPresent() ? role.getName() : campaign.getName())
					.recruiterName(contact.firstName() + " " + contact.surname())
					.recruiterEmail(contact.email())
				.build();
		
		this.eventPublisher.publishSendEmailCommand(command);
		
	}
	
	/**
	* Refer to the CampaignService interface for details
	*/
	@Override
	public void rejectExternalCandidateConnectionRequest(UUID externalCandidateId) {
		
		final String anonymizedDataItem = "-";
		
		this.candidateDao.findCandidateById(externalCandidateId.toString()).ifPresent(candidate -> {
			
			Candidate deletedCandidate = Candidate
					.builder()
					.from(candidate)
					.surname("")
					.jobTitle(anonymizedDataItem)
					.email(anonymizedDataItem)
					.deletedFromSystem(true)
					.lastDataRetentionConfirmation(null)
					.build();
			
			this.candidateDao.saveCandidate(deletedCandidate);
			
		});
		
	}
	
	/**
	* Refer to the CampaignService interface for details
	*/
	@Override
	public void acceptExternalCandidateConnectionRequest(UUID externalCandidateId) {
		
		this.candidateDao.findCandidateById(externalCandidateId.toString()).ifPresent(candidate -> 
			this.candidateDao.saveCandidate(Candidate.builder().from(candidate).deletedFromSystem(false).lastDataRetentionConfirmation(LocalDateTime.now()).build())
		);
		
		// TODO 
		// 
		// Need email to ask for 1 year renewal
		// Need scheduled job to
			// 1. check for 7 day with no response ( delete external candidates ) using request_sent field
			// 2. Send renewal email after 1 year and set request_sent field
	}
	
	/**
	* Refer to the CampaignService interface for details
	*/
	@Override
	public boolean hasAccess(String currentUserId) {
		
		Contact currentUser = this.contactDao.fetchContact(currentUserId).orElseThrow(() ->new IllegalArgumentException(ERR_MSG_CONTACT_NOT_FOUND));
		
		return currentUser.subscriptionType() != SubscriptionType.CREDIT;
		
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
	* Checks that loggedIn user is Participant at the Role or Campaign level
	* If not throws an Exception
	*/
	private void checkLoggedInUserIsParticipantForCampaignOrRole(Campaign campaign, UUID roleId, String currentUserId) {
		
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
	private void checkLoggedInUserIsAdminForParticipation(Campaign campaign, Participation participation, String currentUserId) {
		
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
	private void checkAtLeastOneAdminUserLeftAfterAction(Campaign campaign, Participation participation) {
	
		AtomicBoolean adminAtCampaignLevelIfParticipantRemoved 	= new AtomicBoolean(campaign.getParticipations().stream().anyMatch(p -> (p.getParticipationId() != participation.getParticipationId()) && p.getType() == ParticipantType.ADMIN));
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