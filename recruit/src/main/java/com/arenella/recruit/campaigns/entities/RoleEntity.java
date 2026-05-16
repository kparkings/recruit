package com.arenella.recruit.campaigns.entities;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.arenella.recruit.campaigns.beans.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
* Entity representation of a Role. That is a Job / Function that needs to be
* filled by a Candidate as part of a Campaign 
*/
@Entity
@Table(schema="campaigns", name="roles")
public class RoleEntity {

	@Id
	@Column(name="id")
	private UUID 						id;
	
	@Column(name="campaign_id")
	private UUID 						campaignId;
	
	@Column(name="name")
	private String 						name;
	
	@Column(name="description")
	private String 						description;
	
	@Column(name="created")
	private LocalDateTime				created;
	
	@ElementCollection(targetClass=CandidateEntity.class, fetch=FetchType.LAZY)
	@CollectionTable(schema="campaigns", name="role_candidates", joinColumns=@JoinColumn(name="role_id"))
	@Column(name="id")
	private Set<CandidateEntity>		candidates		= new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "roleId", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private Set<ParticipationEntity> 	participations	= new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "roleId", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private Set<NoteEntity> 			notes			= new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "roleId", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private Set<AppointmentEntity> 		appointments	= new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "roleId", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	private Set<DocumentEntity> 		documents		= new LinkedHashSet<>();
	
	/**
	* Default constructor 
	*/
	public RoleEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public RoleEntity(RoleEntityBuilder builder) {
		
		this.id				= builder.id;
		this.campaignId		= builder.campaignId;
		this.name 			= builder.name;
		this.description 	= builder.description;
		this.created 		= builder.created;
		
		this.candidates.clear();
		this.participations.clear();
		this.notes.clear();
		this.appointments.clear();
		this.documents.clear();

		this.candidates.addAll(builder.candidates);
		this.participations.addAll(builder.participations);
		this.notes.addAll(builder.notes);
		this.appointments.addAll(builder.appointments);
		this.documents.addAll(builder.documents);
	
	}

	/**
	* Returns the unique Id of the Campaign
	* @return Id
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns the id of the Campaign the Role 
	* is a part of
	* @return Id of the associated Campaign
	*/
	public UUID getCampaignId() {
		return this.campaignId;
	}
	
	/**
	* Returns the name of the Campaign
	* @return name of the Campaign
	*/
	public String getName() {
		return this.name;
	}
	
	/**
	* Returns a short description describing the campaign
	* @return Description of the Campaign
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
	* Returns Campaign participants. These are the Recruiters that can participate in the
	* Campaign
	* @return Campaign Participants
	*/
	public Set<ParticipationEntity> getParticipations() {
		return this.participations;
	}
	
	/**
	* Returns Notes related to the Campaign
	* @return notes
	*/
	public Set<NoteEntity> getNotes() {
		return this.notes;
	}
	
	/**
	* Returns the Candidates at the Campaign level
	* @return Candidates associated with the Campaign
	*/
	public Set<CandidateEntity> getCandidates() {
		return this.candidates;
	}
	
	/**
	* Returns appointments that are specific to the Campaign such as 
	* meetings with the clients or calls with a Candidate
	* @return appointments
	*/
	public Set<AppointmentEntity> getAppointments() {
		return this.appointments;
	}
	
	/**
	* Returns the documents related to the Campaign
	* @return Documents
	*/
	public Set<DocumentEntity> getDocuments() {
		return this.documents;
	}
	
	/**
	* Return a Builder for the class
	* @return Builder
	*/
	public static RoleEntityBuilder builder() {
		return new RoleEntityBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class RoleEntityBuilder{
		
		private UUID 						id;
		private UUID 						campaignId;
		private String 						name;
		private String 						description;
		private LocalDateTime				created;
		private Set<CandidateEntity>		candidates		= new LinkedHashSet<>();
		private Set<ParticipationEntity>  	participations	= new LinkedHashSet<>();
		private Set<NoteEntity> 			notes			= new LinkedHashSet<>();
		private Set<AppointmentEntity> 		appointments	= new LinkedHashSet<>();
		private Set<DocumentEntity> 		documents		= new LinkedHashSet<>();

		/**
		* Populates the Builder with the values from an existing Role
		* @param role - Contains initialization values
		* @return Builder
		*/
		public RoleEntityBuilder from(RoleEntity role) {
			this.id 			= role.id;
			this.campaignId		= role.campaignId;
			this.name 			= role.name;
			this.description 	= role.description;
			this.created	 	= role.created;
			
			this.candidates.clear();
			this.participations.clear();
			this.notes.clear();
			this.appointments.clear();
			this.documents.clear();
			
			this.candidates.addAll(role.candidates);
			this.participations.addAll(role.participations);
			this.notes.addAll(role.notes);
			this.appointments.addAll(role.appointments);
			this.documents.addAll(role.documents);
			
			return this;
		}
		
		/**
		* Sets the unique Id of the Role
		* @param id - Unique Id of the Role
		* @return Builder
		*/
		public RoleEntityBuilder id (UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the Id of the Campaign the Role is associated with
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public RoleEntityBuilder campaignId (UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* Sets the name of the Role
		* @param name - Name of the Role
		* @return Builder
		*/
		public RoleEntityBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		/**
		* Sets a description of the Role
		* @param description - Role description
		* @return Builder
		*/
		public RoleEntityBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		/**
		* Sets when the Role was created
		* @param created - Creation date/time
		* @return Builder
		*/
		public RoleEntityBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Adds Role level Candidates. These are candidates that are 
		* potentially interesting for this specific Role
		* @param candidates - Campaign level Candidates
		* @return Builder
		*/
		public RoleEntityBuilder candidates(Set<CandidateEntity> candidates) {
			this.candidates.clear();
			this.candidates.addAll(candidates);
			return this;
		}
		
		/**
		* Adds an additional Candidate to the Role level 
		* @param candidate - Candidate 
		* @return Builder
		*/
		public RoleEntityBuilder candidate(CandidateEntity candidate) {
			this.candidates.add(candidate);
			return this;
		}
		
		/**
		* Sets the Participations which are able to view and interact with the 
		* Role
		* @param participations - Role Participations
		* @return Builder:
		*/
		public RoleEntityBuilder participants(Set<ParticipationEntity> participations) {
			this.participations.clear();
			this.participations.addAll(participations);
			return this;
		}
		
		/**
		* Adds an additional Participation to the existing Participations
		* @param participation - Additional Participation
		* @return Builder
		*/
		public RoleEntityBuilder participation(ParticipationEntity participation) {
			this.participations.add(participation);
			return this;
		}
		
		/**
		* Sets ant notes relating to the Role
		* @param notes - Role level Notes
		* @return Builder
		*/
		public RoleEntityBuilder notes(Set<NoteEntity> notes) {
			this.notes.clear();
			this.notes.addAll(notes);
			return this;
		}
		
		/**
		* Adds an additional Note to the existing Notes
		* @param note - Additional Note
		* @return Builder
		*/
		public RoleEntityBuilder note(NoteEntity note) {
			this.notes.add(note);
			return this;
		}
		
		/**
		* Sets any appointments relating to the Role
		* @param appointments - Appointments such as client meetings or Candidate calls
		* @return Builder
		*/
		public RoleEntityBuilder appointments(Set<AppointmentEntity> appointments) {
			this.appointments.clear();
			this.appointments.addAll(appointments);
			return this;
		}
		
		/**
		* Adds an additional Appointment to the existing Appointments
		* @param appointment - Additional Appointment
		* @return Builder
		*/
		public RoleEntityBuilder appointment(AppointmentEntity appointment) {
			this.appointments.add(appointment);
			return this;
		}
		
		/**
		* Sets any documents associated with the Role
		* @param documents - Campaign Documents
		* @return Builder
		*/
		public RoleEntityBuilder documents(Set<DocumentEntity> documents) {
			this.documents.clear();
			this.documents.addAll(documents);
			return this;
		}
		
		/**
		* Adds an additional Document to the existing Documents
		* @param document - Additional document
		* @return Builder
		*/
		public RoleEntityBuilder document(DocumentEntity document) {
			this.documents.add(document);
			return this;
		}
		
		public RoleEntity build() {
			return new RoleEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity to Domain representation
	* @param entity - To be converted
	* @return converted
	*/
	public static Role fromEntity(RoleEntity entity) {
		return Role
				.builder()
				.id(entity.getId())
				.name(entity.getName())
				.description(entity.getDescription())
				.created(entity.getCreated())
				.candidates(entity.getCandidates().stream().map(CandidateEntity::fromEntity).collect(Collectors.toSet()))
				.participants(entity.getParticipations().stream().map(ParticipationEntity::fromEntity).collect(Collectors.toSet()))
				.notes(entity.getNotes().stream().map(NoteEntity::fromEntity).collect(Collectors.toSet()))
				.appointments(entity.getAppointments().stream().map(AppointmentEntity::fromEntity).collect(Collectors.toSet()))
				.documents(entity.getDocuments().stream().map(DocumentEntity::fromEntity).collect(Collectors.toSet()))
				.build();
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param role 		- To be converted
	* @param campaignId - Id of the Campaign the Role belongs to
	* @return converted
	*/
	public static RoleEntity toEntity(Role role, UUID campaignId) {
		return RoleEntity
				.builder()
				.id(role.getId())
				.campaignId(campaignId)
				.name(role.getName())
				.description(role.getDescription())
				.created(role.getCreated())
				.candidates(role.getCandidates().stream().map(CandidateEntity::toEntity).collect(Collectors.toSet()))
				.participants(role.getParticipations().stream().map(ParticipationEntity::toEntity).collect(Collectors.toSet()))
				.notes(role.getNotes().stream().map(NoteEntity::toEntity).collect(Collectors.toSet()))
				.appointments(role.getAppointments().stream().map(AppointmentEntity::toEntity).collect(Collectors.toSet()))
				.documents(role.getDocuments().stream().map(DocumentEntity::toEntity).collect(Collectors.toSet()))
				.build();
	}
	
}