package com.arenella.recruit.campaigns.beans;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
* Class represents an individual Role within a Campaign. For example an open 
* Role for a Java developer that forms part of a Campaigin for the Recruiters 
* banking client 
*/
public class Role {

	private UUID 				id;
	private String 				name;
	private String 				description;
	private LocalDateTime		created;
	private Set<Candidate>		candidates		= new LinkedHashSet<>();
	private Set<Participation> 	participations	= new LinkedHashSet<>();
	private Set<Note> 			notes			= new LinkedHashSet<>();
	private Set<Appointment> 	appointments	= new LinkedHashSet<>();
	private Set<Document> 		documents		= new LinkedHashSet<>();
	//Role level should also have ListingIds ?? Possibly as part of its attribute list. 1 listing per role.

	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public Role(RoleBuilder builder) {
		
		this.id				= builder.id;
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
	public Set<Participation> getParticipations() {
		return this.participations;
	}
	
	/**
	* Returns Notes related to the Campaign
	* @return notes
	*/
	public Set<Note> getNotes() {
		return this.notes;
	}
	
	/**
	* Returns the Candidates at the Campaign level
	* @return Candidates associated with the Campaign
	*/
	public Set<Candidate> getCandidates() {
		return this.candidates;
	}
	
	/**
	* Returns appointments that are specific to the Campaign such as 
	* meetings with the clients or calls with a Candidate
	* @return appointments
	*/
	public Set<Appointment> getAppointments() {
		return this.appointments;
	}
	
	/**
	* Returns the documents related to the Campaign
	* @return Documents
	*/
	public Set<Document> getDocuments() {
		return this.documents;
	}
	
	/**
	* Return a Builder for the class
	* @return Builder
	*/
	public static RoleBuilder builder() {
		return new RoleBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class RoleBuilder{
		
		private UUID 				id;
		private String 				name;
		private String 				description;
		private LocalDateTime		created;
		private Set<Candidate>		candidates		= new LinkedHashSet<>();
		private Set<Participation>  participations	= new LinkedHashSet<>();
		private Set<Note> 			notes			= new LinkedHashSet<>();
		private Set<Appointment> 	appointments	= new LinkedHashSet<>();
		private Set<Document> 		documents		= new LinkedHashSet<>();

		/**
		* Populates the Builder with the values from an existing Role
		* @param role - Contains initialization values
		* @return Builder
		*/
		public RoleBuilder from(Role role) {
			this.id 			= role.id;
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
		public RoleBuilder id (UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the name of the Role
		* @param name - Name of the Role
		* @return Builder
		*/
		public RoleBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		/**
		* Sets a description of the Role
		* @param description - Role description
		* @return Builder
		*/
		public RoleBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		/**
		* Sets when the Role was created
		* @param created - Creation date/time
		* @return Builder
		*/
		public RoleBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Adds Role level Candidates. These are candidates that are 
		* potentially interesting for this specific Role
		* @param candidates - Campaign level Candidates
		* @return Builder
		*/
		public RoleBuilder candidates(Set<Candidate> candidates) {
			this.candidates.clear();
			this.candidates.addAll(candidates);
			return this;
		}
		
		/**
		* Adds an additional Candidate to the Role level 
		* @param candidate - Candidate 
		* @return Builder
		*/
		public RoleBuilder candidate(Candidate candidate) {
			this.candidates.add(candidate);
			return this;
		}
		
		/**
		* Sets the Participations which are able to view and interact with the 
		* Role
		* @param participations - Role Participations
		* @return Builder:
		*/
		public RoleBuilder participants(Set<Participation> participations) {
			this.participations.clear();
			this.participations.addAll(participations);
			return this;
		}
		
		/**
		* Adds an additional Participation to the existing Participations
		* @param participation - Additional Participation
		* @return Builder
		*/
		public RoleBuilder participation(Participation participation) {
			this.participations.add(participation);
			return this;
		}
		
		/**
		* Sets ant notes relating to the Role
		* @param notes - Role level Notes
		* @return Builder
		*/
		public RoleBuilder notes(Set<Note> notes) {
			this.notes.clear();
			this.notes.addAll(notes);
			return this;
		}
		
		/**
		* Adds an additional Note to the existing Notes
		* @param note - Additional Note
		* @return Builder
		*/
		public RoleBuilder note(Note note) {
			this.notes.add(note);
			return this;
		}
		
		/**
		* Sets any appointments relating to the Role
		* @param appointments - Appointments such as client meetings or Candidate calls
		* @return Builder
		*/
		public RoleBuilder appointments(Set<Appointment> appointments) {
			this.appointments.clear();
			this.appointments.addAll(appointments);
			return this;
		}
		
		/**
		* Adds an additional Appointment to the existing Appointments
		* @param appointment - Additional Appointment
		* @return Builder
		*/
		public RoleBuilder appointment(Appointment appointment) {
			this.appointments.add(appointment);
			return this;
		}
		
		/**
		* Sets any documents associated with the Role
		* @param documents - Campaign Documents
		* @return Builder
		*/
		public RoleBuilder documents(Set<Document> documents) {
			this.documents.clear();
			this.documents.addAll(documents);
			return this;
		}
		
		/**
		* Adds an additional Document to the existing Documents
		* @param document - Additional document
		* @return Builder
		*/
		public RoleBuilder document(Document document) {
			this.documents.add(document);
			return this;
		}
		
		public Role build() {
			return new Role(this);
		}
		
	}
	
}
