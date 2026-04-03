package com.arenella.recruit.campaigns.entities;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.campaigns.beans.Campaign;
import com.arenella.recruit.campaigns.beans.CampaignLogo;
import com.arenella.recruit.campaigns.beans.CampaignLogo.PHOTO_FORMAT;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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