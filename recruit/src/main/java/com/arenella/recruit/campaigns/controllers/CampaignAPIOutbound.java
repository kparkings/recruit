package com.arenella.recruit.campaigns.controllers;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.arenella.recruit.campaigns.beans.Contact;

/**
* API Outbound representation of a Campaign with full details. 
*/
public class CampaignAPIOutbound {

	private UUID 							id;
	private String 							name;
	private String 							description;
	private CampaignLogo 					logo;
	private Set<ParticipationAPIOutbound> 	participations	= new LinkedHashSet<>(); 
	private Set<NoteAPIOutbound> 			notes			= new LinkedHashSet<>();
	private Set<AppointmentAPIOutbound> 	appointments	= new LinkedHashSet<>();
	private Set<DocumentAPIOutbound> 		documents		= new LinkedHashSet<>();

	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public CampaignAPIOutbound(CampaignAPIOutboundBuilder builder) {
		
		this.id 			= builder.id;
		this.name 			= builder.name;
		this.description 	= builder.description;
		this.logo 			= builder.logo;
		
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
	* @return unite identifier
	*/
	public UUID getId(){
		return this.id;
	}
	
	/**
	* Returns the name of the Campaign
	* @return name of the Campaign
	*/
	public String getName(){
		return this.name;
	}
	
	/**
	* Returns a description of the Campaign
	* @return
	*/
	public String getDescription(){
		return this.description;
	}
	
	/**
	* If present returns the Logo for the Campaign
	* @return logo
	*/
	public Optional<CampaignLogo> getLogo(){
		return Optional.ofNullable(this.logo);
	}
	
	/**
	* Returns a collection of Participation in the Campaign
	* @return Participations
	*/
	public Set<ParticipationAPIOutbound> getParticipations(){
		return this.participations;
	} 
	
	/**
	* Returns Notes associated with the Campaign
	* @return Notes
	*/
	public Set<NoteAPIOutbound> getNotes(){
		return this.notes;
	}
	
	/**
	* Returns Appointments associated with the Campaign
	* @return Appointments
	*/
	public Set<AppointmentAPIOutbound> getAppointments(){
		return this.appointments;
	}
	
	/**
	* Returns documents associated with the Campaign
	* @return documents
	*/
	public Set<DocumentAPIOutbound> getDocuments(){
		return this.documents;
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder
	*/
	public CampaignAPIOutboundBuilder builder() {
		return new CampaignAPIOutboundBuilder();
	}
	
	/**
	* Builder for the Class
	*/
	public static class CampaignAPIOutboundBuilder {
		
		private UUID 							id;
		private String 							name;
		private String 							description;
		private CampaignLogo 					logo;
		private Set<ParticipationAPIOutbound> 	participations	= new LinkedHashSet<>(); 
		private Set<NoteAPIOutbound> 			notes			= new LinkedHashSet<>();
		private Set<AppointmentAPIOutbound> 	appointments	= new LinkedHashSet<>();
		private Set<DocumentAPIOutbound> 		documents		= new LinkedHashSet<>();

		/**
		* Populates the Builder with values from the Domain objects
		* @param campaign - Domain representation of the Campaign
		* @param contacts - Collection of all Contacts referenced in the Campaigns Participations
		* @return Builder
		*/
		public CampaignAPIOutboundBuilder from(Campaign campaign, Set<Contact> contacts) {
			
			this.id 			= campaign.getId();
			this.name 			= campaign.getName();
			this.description 	= campaign.getDescription();
			
			campaign.getLogo().ifPresent(campaignLogo -> this.logo = campaignLogo);
			
			campaign.getParticipations().stream().forEach(participation -> {
				this.participations.add(ParticipationAPIOutbound.builder().from(participation, contacts.stream().filter(c -> c.id() == participation.getContactId()).findFirst().orElseThrow()).build());
			});
			
			this.notes.addAll(campaign.getNotes().stream().map(n -> NoteAPIOutbound.builder().from(n).build()).collect(Collectors.toCollection(LinkedHashSet::new)));
			this.appointments.addAll(campaign.getAppointments().stream().map(a -> AppointmentAPIOutbound.builder().from(a).build()).collect(Collectors.toCollection(LinkedHashSet::new)));
			this.documents.addAll(campaign.getDocuments().stream().map(d -> new DocumentAPIOutbound(d.title(), d.type(), d.created())).collect(Collectors.toCollection(LinkedHashSet::new)));
			
			return this;
		}
		
		/**
		* Returns a new Initialized instance of the Class
		* @return Initialized instance
		*/
		public CampaignAPIOutbound build() {
			return new CampaignAPIOutbound(this);
		}
		
	}

}