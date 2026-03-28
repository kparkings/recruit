package com.arenella.recruit.campaigns.beans;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
* Class represents a recruitment Campaign. It is a high level container
* that could, for example represent a recruiters clients and contain 
* amongst other things each of the Roles for that candidate 
*/
public class Campaign {

	private UUID 				id;
	private String 				name;
	private String 				description;
	private CampaignLogo 		logo;
	private LocalDateTime		created;
	private Set<Participation> 	participations	= new LinkedHashSet<>();
	private Set<Note> 			notes			= new LinkedHashSet<>();
	private Set<Appointment> 	appointments	= new LinkedHashSet<>();
	private Set<Document> 		documents		= new LinkedHashSet<>();
	//Role level should also have ListingIds ?? Possibly as part of its attribute list. 1 listing per role.

	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public Campaign(CampaignBuilder builder) {
		
		this.id				= builder.id;
		this.name 			= builder.name;
		this.description 	= builder.description;
		this.logo 			= builder.logo;
		this.created 		= builder.created;
		
		this.participations.clear();
		this.notes.clear();
		this.appointments.clear();
		this.documents.clear();

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
	* Returns the Campaigns log. This could be for example the business 
	* logo of the company the Campaign relates to
	* @return Campaign logo
	*/
	public Optional<CampaignLogo> getLogo() {
		return Optional.ofNullable(this.logo);
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
	public static CampaignBuilder builder() {
		return new CampaignBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class CampaignBuilder{
		
		private UUID 				id;
		private String 				name;
		private String 				description;
		private CampaignLogo 		logo;
		private LocalDateTime		created;
		private Set<Participation>  participations	= new LinkedHashSet<>();
		private Set<Note> 			notes			= new LinkedHashSet<>();
		private Set<Appointment> 	appointments	= new LinkedHashSet<>();
		private Set<Document> 		documents		= new LinkedHashSet<>();

		/**
		* Populates the Builder with the values from an existing Campaign
		* @param campaign - Contains initialization values
		* @return Builder
		*/
		public CampaignBuilder from(Campaign campaign) {
			this.id 			= campaign.id;
			this.name 			= campaign.name;
			this.description 	= campaign.description;
			this.logo 			= campaign.logo;
			this.created	 	= campaign.created;
			
			this.participations.clear();
			this.notes.clear();
			this.appointments.clear();
			this.documents.clear();
			
			this.participations.addAll(campaign.participations);
			this.notes.addAll(campaign.notes);
			this.appointments.addAll(campaign.appointments);
			this.documents.addAll(campaign.documents);
			
			return this;
		}
		
		/**
		* Sets the unique Id of the Campaign
		* @param id - Unique Id of the Campaign
		* @return Builder
		*/
		public CampaignBuilder id (UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the name of the Campaign
		* @param name - Name of the Campaign
		* @return Builder
		*/
		public CampaignBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		/**
		* Sets a description of the Campaign
		* @param description - Campaign description
		* @return Builder
		*/
		public CampaignBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		/**
		* Sets the Logo or the Campaign
		* @param logo - Campaign Logo
		* @return Builder
		*/
		public CampaignBuilder logo(CampaignLogo logo) {
			this.logo = logo;
			return this;
		}
		
		/**
		* Sets when the Campaign was created
		* @param created - Creation date/time
		* @return Builder
		*/
		public CampaignBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the Participations which are able to view and interact with the 
		* Campaign
		* @param participations - Campaign Participations
		* @return Builder:
		*/
		public CampaignBuilder participants(Set<Participation> participations) {
			this.participations.clear();
			this.participations.addAll(participations);
			return this;
		}
		
		/**
		* Adds an additional Participation to the existing Participations
		* @param participation - Additional Participation
		* @return Builder
		*/
		public CampaignBuilder participation(Participation participation) {
			this.participations.add(participation);
			return this;
		}
		
		/**
		* Sets ant notes relating to the Campaign
		* @param notes - Campaign level Notes
		* @return Builder
		*/
		public CampaignBuilder notes(Set<Note> notes) {
			this.notes.clear();
			this.notes.addAll(notes);
			return this;
		}
		
		/**
		* Adds an additional Note to the existing Notes
		* @param note - Additional Note
		* @return Builder
		*/
		public CampaignBuilder note(Note note) {
			this.notes.add(note);
			return this;
		}
		
		/**
		* Sets any appointments relating to the Campaign
		* @param appointments - Appointments such as client meetings or Candidate calls
		* @return Builder
		*/
		public CampaignBuilder appointments(Set<Appointment> appointments) {
			this.appointments.clear();
			this.appointments.addAll(appointments);
			return this;
		}
		
		/**
		* Adds an additional Appointment to the existing Appointments
		* @param appointment - Additional Appointment
		* @return Builder
		*/
		public CampaignBuilder appointment(Appointment appointment) {
			this.appointments.add(appointment);
			return this;
		}
		
		/**
		* Sets any documents associated with the Campaign
		* @param documents - Campaign Documents
		* @return Builder
		*/
		public CampaignBuilder documents(Set<Document> documents) {
			this.documents.clear();
			this.documents.addAll(documents);
			return this;
		}
		
		/**
		* Adds an additional Document to the existing Documents
		* @param document - Additional document
		* @return Buuilder
		*/
		public CampaignBuilder document(Document document) {
			this.documents.add(document);
			return this;
		}
		
		public Campaign build() {
			return new Campaign(this);
		}
		
	}
	
}
