package com.arenella.recruit.campaigns.entities;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.arenella.recruit.campaigns.beans.CampaignLogo.PHOTO_FORMAT;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
* Entity representation of a Campaign 
*/
@Entity
@Table(schema="campaigns", name="campaigns")
public class CampaignEntity {

	@Id
	@Column(name="id")
	private UUID 				id;
	
	@Column(name="name")
	private String 				name;
	
	@Column(name="description")
	private String 				description;
	
	@Column(name="created")
	private LocalDateTime		created;
	
	@Column(name="logo_bytes")
	private byte[] 				logoBytes;
	
	@Column(name="logo_format")
	@Enumerated(EnumType.STRING)
	private PHOTO_FORMAT 		logoFormat;
	
	@OneToMany(mappedBy = "campaignId", cascade = CascadeType.DETACH, orphanRemoval=true, fetch=FetchType.LAZY)
	private Set<RoleEntity>		roles		= new LinkedHashSet<>();
	
	@ElementCollection(targetClass=CandidateEntity.class, fetch=FetchType.LAZY)
	@CollectionTable(schema="campaigns", name="campaign_candidates", joinColumns=@JoinColumn(name="campaign_id"))
	@Column(name="id")
	private Set<CandidateEntity>		candidates		= new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "campaignId", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private Set<ParticipationEntity> 	participations	= new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "campaignId", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private Set<NoteEntity> 			notes			= new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "campaignId", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private Set<AppointmentEntity> 	appointments	= new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "campaignId", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private Set<DocumentEntity> 		documents		= new LinkedHashSet<>();
	
	/**
	* Default constructor 
	*/
	public CampaignEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public CampaignEntity(CampaignEntityBuilder builder) {
		this.id 			= builder.id;
		this.name 			= builder.name;
		this.description 	= builder.description;
		this.logoBytes 		= builder.logoBytes;
		this.logoFormat 	= builder.logoFormat;
		this.created		= builder.created;
		
		this.roles.clear();
		this.candidates.clear();
		this.participations.clear();
		this.notes.clear();
		this.appointments.clear();
		this.documents.clear();
		
		this.roles.addAll(builder.roles);
		this.candidates.addAll(builder.candidates);
		this.participations.addAll(builder.participations);
		this.notes.addAll(builder.notes);
		this.appointments.addAll(builder.appointments);
		this.documents.addAll(builder.documents);
	}
	
	
	/**
	* Returns the unique Id of the Campaign
	* @return Id of the Campaign
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns the name of the Campaign
	* @return name of the Campaign
	*/
	public String getName() {
		return this.name;
	}
	
	/**
	* Returns a description of the purpose of the Campaign 
	* @return description of the Campaign
	*/
	public String getDescription() {
		return this.description;
	}
	
	/**
	* Returns when the Campaign was created
	* @return creation date/time
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}
	
	/**
	* Returns the bytes of the Optional Logo for the Campaign
	* @return bytes
	*/
	public Optional<byte[]> getLogoBytes() {
		return Optional.ofNullable(this.logoBytes);
	}
	
	/**
	* Returns the format of the Optional logo
	* @return format of logo image
	*/
	public Optional<PHOTO_FORMAT> getLogoFormat() {
		return Optional.ofNullable(this.logoFormat);
	}
	
	/**
	* Returns the Roles associated with the Campaign
	* @return Roles
	*/
	public Set<RoleEntity> getRoles(){
		return this.roles;
	}
	
	/**
	* Returns the Campaign level Candidates. These are Candidates that are potentially interesting 
	* for multiple roles in the Campaign
	* @return candidates
	*/
	public Set<CandidateEntity> getCandidates(){
		return this.candidates;
	}
	
	/**
	* Returns the Participants in the Campaign. These are the Recruiters that can 
	* work on the Campaign / Roles
	* @return Participants
	*/
	public Set<ParticipationEntity> getParticipations(){
		return this.participations;
	}
	
	/**
	* Returns Notes relevant to the campaign level
	* @return Notes
	*/
	public Set<NoteEntity> getNotes(){
		return this.notes;
	}
	
	/**
	* Returns Campaign level appointments. For example a meeting that relates to 
	* multiple Roles in the Campaign
	* @return Appointments
	*/
	public Set<AppointmentEntity> getAppointments(){
		return this.appointments;
	}
	
	/**
	* Returns Documents relevant at the Campaign level. 
	* @return Campaign documents
	*/
	public Set<DocumentEntity> getDocuments(){
		return this.documents;
	}
	
	/**
	* Returns a builder for the Class
	* @return
	*/
	public static CampaignEntityBuilder builder() {
		return new CampaignEntityBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class CampaignEntityBuilder{
		
		private UUID 						id;
		private String 						name;
		private String 						description;
		private byte[] 						logoBytes;
		private PHOTO_FORMAT 				logoFormat;
		private LocalDateTime				created;
		private Set<RoleEntity>				roles			= new LinkedHashSet<>();
		private Set<CandidateEntity>		candidates		= new LinkedHashSet<>();
		private Set<ParticipationEntity>  	participations	= new LinkedHashSet<>();
		private Set<NoteEntity> 			notes			= new LinkedHashSet<>();
		private Set<AppointmentEntity> 		appointments	= new LinkedHashSet<>();
		private Set<DocumentEntity> 		documents		= new LinkedHashSet<>();

		/**
		* Populates the Builder with the values from an existing Campaign
		* @param campaign - Contains initialization values
		* @return Builder
		*/
		public CampaignEntityBuilder from(Campaign campaign) {
			
			this.id 			= campaign.getId();
			this.name 			= campaign.getName();
			this.description 	= campaign.getDescription();
			this.created		= campaign.getCreated();
			
			campaign.getLogo().ifPresent(logo -> {
				this.logoBytes = logo.imageBytes();
				this.logoFormat = logo.format();
			});
		
			this.created	 	= campaign.getCreated();
			
			this.roles.clear();
			this.candidates.clear();
			this.participations.clear();
			this.notes.clear();
			this.appointments.clear();
			this.documents.clear();
			
			this.roles.addAll(campaign.getRoles().stream().map(role -> RoleEntity.toEntity(role, campaign.getId())).collect(Collectors.toSet()));
			this.candidates.addAll(campaign.getCandidates().stream().map(CandidateEntity::toEntity).collect(Collectors.toSet()));
			this.participations.addAll(campaign.getParticipations().stream().map(ParticipationEntity::toEntity).collect(Collectors.toSet()));
			this.notes.addAll(campaign.getNotes().stream().map(NoteEntity::toEntity).collect(Collectors.toSet()));
			this.appointments.addAll(campaign.getAppointments().stream().map(AppointmentEntity::toEntity).collect(Collectors.toSet()));
			this.documents.addAll(campaign.getDocuments().stream().map(DocumentEntity::toEntity).collect(Collectors.toSet()));
			
			return this;
		}
		
		/**
		* Sets the unique Id of the Campaign
		* @param id - Unique Id of the Campaign
		* @return Builder
		*/
		public CampaignEntityBuilder id (UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the name of the Campaign
		* @param name - Name of the Campaign
		* @return Builder
		*/
		public CampaignEntityBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		/**
		* Sets a description of the Campaign
		* @param description - Campaign description
		* @return Builder
		*/
		public CampaignEntityBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		/**
		* Sets the Logo or the Campaign
		* @param logo - Campaign Logo
		* @return Builder
		*/
		public CampaignEntityBuilder logo(CampaignLogo logo) {
			
			if (logo == null) {
				return this;
			}
			
			this.logoBytes  = logo.imageBytes();
			this.logoFormat = logo.format();
			return this;
		}
		
		/**
		* Sets when the Campaign was created
		* @param created - Creation date/time
		* @return Builder
		*/
		public CampaignEntityBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}

		/**
		* Adds Roles. These are Roles that are relevant to the Campaign
		* @param roles - Roles relevant to the Campaign
		* @return Builder
		*/
		public CampaignEntityBuilder roles(Set<RoleEntity> roles) {
			this.roles.clear();
			this.roles.addAll(roles);
			return this;
		}
		
		/**
		* Adds an additional Role to the Campaign 
		* @param role - Role 
		* @return Builder
		*/
		public CampaignEntityBuilder role(RoleEntity role) {
			this.roles.add(role);
			return this;
		}
		
		/**
		* Adds Campaign level Candidates. These are candidates that are 
		* potentially interesting to multiple roles in the Campaign
		* @param candidates - Campaign level Candidates
		* @return Builder
		*/
		public CampaignEntityBuilder candidates(Set<CandidateEntity> candidates) {
			this.candidates.clear();
			this.candidates.addAll(candidates);
			return this;
		}
		
		/**
		* Adds an additional Candidate to the Campaign level 
		* @param candidate - Candidate 
		* @return Builder
		*/
		public CampaignEntityBuilder candidate(CandidateEntity candidate) {
			this.candidates.add(candidate);
			return this;
		}
		
		/**
		* Sets the Participations which are able to view and interact with the 
		* Campaign
		* @param participations - Campaign Participations
		* @return Builder:
		*/
		public CampaignEntityBuilder participants(Set<ParticipationEntity> participations) {
			this.participations.clear();
			this.participations.addAll(participations);
			return this;
		}
		
		/**
		* Adds an additional Participation to the existing Participations
		* @param participation - Additional Participation
		* @return Builder
		*/
		public CampaignEntityBuilder participation(ParticipationEntity participation) {
			this.participations.add(participation);
			return this;
		}
		
		/**
		* Sets ant notes relating to the Campaign
		* @param notes - Campaign level Notes
		* @return Builder
		*/
		public CampaignEntityBuilder notes(Set<NoteEntity> notes) {
			this.notes.clear();
			this.notes.addAll(notes);
			return this;
		}
		
		/**
		* Adds an additional Note to the existing Notes
		* @param note - Additional Note
		* @return Builder
		*/
		public CampaignEntityBuilder note(NoteEntity note) {
			this.notes.add(note);
			return this;
		}
		
		/**
		* Sets any appointments relating to the Campaign
		* @param appointments - Appointments such as client meetings or Candidate calls
		* @return Builder
		*/
		public CampaignEntityBuilder appointments(Set<AppointmentEntity> appointments) {
			this.appointments.clear();
			this.appointments.addAll(appointments);
			return this;
		}
		
		/**
		* Adds an additional Appointment to the existing Appointments
		* @param appointment - Additional Appointment
		* @return Builder
		*/
		public CampaignEntityBuilder appointment(AppointmentEntity appointment) {
			this.appointments.add(appointment);
			return this;
		}
		
		/**
		* Sets any documents associated with the Campaign
		* @param documents - Campaign Documents
		* @return Builder
		*/
		public CampaignEntityBuilder documents(Set<DocumentEntity> documents) {
			this.documents.clear();
			this.documents.addAll(documents);
			return this;
		}
		
		/**
		* Adds an additional Document to the existing Documents
		* @param document - Additional document
		* @return Builder
		*/
		public CampaignEntityBuilder document(DocumentEntity document) {
			this.documents.add(document);
			return this;
		}
		
		public CampaignEntity build() {
			return new CampaignEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity to Domain representation but does not initialize
	* the Collections of related Objects-
 	* @return Lazily initialized instance of a Campaign
	*/
	public static Campaign fromEntityLazy(CampaignEntity entity) {
		return Campaign
				.builder()
					.id(entity.id)
					.name(entity.name)
					.description(entity.description)
					.created(entity.created)
					.logo(new CampaignLogo(entity.logoBytes, entity.logoFormat))
				.build();
	}
	
	/**
	* Converts from Domain to Entity representation
	* @return Entity representation
	*/
	public static CampaignEntity toEntity(Campaign campaign) {
		return CampaignEntity
				.builder()
					.appointments(campaign.getAppointments().stream().map(AppointmentEntity::toEntity).collect(Collectors.toSet()))
					.candidates(campaign.getCandidates().stream().map(CandidateEntity::toEntity).collect(Collectors.toSet()))
					.created(campaign.getCreated())
					.description(campaign.getDescription())
					.documents(campaign.getDocuments().stream().map(DocumentEntity::toEntity).collect(Collectors.toSet()))
					.id(campaign.getId())
					.logo(campaign.getLogo().orElse(null))
					.name(campaign.getName())
					.notes(campaign.getNotes().stream().map(NoteEntity::toEntity).collect(Collectors.toSet()))
					.participants(campaign.getParticipations().stream().map(ParticipationEntity::toEntity).collect(Collectors.toSet()))
					.roles(campaign.getRoles().stream().map(r -> RoleEntity.toEntity(r, campaign.getId())).collect(Collectors.toSet()))
				.build();
	}
	
	/**
	* Converts from Domain to Entity representation
	* @return Entity representation
	*/
	public static Campaign fromEntity(CampaignEntity campaign) {
		
		AtomicReference<CampaignLogo> logo = new AtomicReference<>(null);
		
		campaign.getLogoBytes().ifPresent(b -> 
			campaign.getLogoFormat().ifPresent(f -> 
				logo.set(new CampaignLogo(b,f))
			)
		);
		
		return Campaign
				.builder()
					.appointments(campaign.getAppointments().stream().map(AppointmentEntity::fromEntity).collect(Collectors.toSet()))
					.candidates(campaign.getCandidates().stream().map(CandidateEntity::fromEntity).collect(Collectors.toSet()))
					.created(campaign.getCreated())
					.description(campaign.getDescription())
					.documents(campaign.getDocuments().stream().map(DocumentEntity::fromEntity).collect(Collectors.toSet()))
					.id(campaign.getId())
					.logo(logo.get())
					.name(campaign.getName())
					.notes(campaign.getNotes().stream().map(NoteEntity::fromEntity).collect(Collectors.toSet()))
					.participants(campaign.getParticipations().stream().map(ParticipationEntity::fromEntity).collect(Collectors.toSet()))
					.roles(campaign.getRoles().stream().map(RoleEntity::fromEntity).collect(Collectors.toSet()))
				.build();
	}
	
}