package com.arenella.recruit.campaigns.entities;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
	
	@OneToMany(mappedBy = "campaignId", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
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
	
}