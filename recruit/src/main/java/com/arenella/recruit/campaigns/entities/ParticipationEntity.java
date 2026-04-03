package com.arenella.recruit.campaigns.entities;

import java.util.Optional;
import java.util.UUID;

import com.arenella.recruit.campaigns.beans.Participation;
import com.arenella.recruit.campaigns.beans.Participation.ParticipantType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Entity representation of a Participation in a Campaign 
*/
@Entity
@Table(schema="campaigns", name="participations")
public class ParticipationEntity {

	@Id
	@Column(name="id")
	private UUID			participationId;
	
	@Column(name="contact_id")
	private String 			contactId;
	
	@Column(name="campaign_id")
	private UUID 			campaignId;
	
	@Column(name="role_id")
	private UUID 			roleId;
	
	@Column(name="type")
	private ParticipantType type;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public ParticipationEntity(ParticipationEntityBuilder builder) {
		this.participationId 	= builder.participationId;
		this.contactId 			= builder.contactId;
		this.campaignId 		= builder.campaignId;
		this.roleId 			= builder.roleId;
		this.type 				= builder.type;
	}
	
	/**
	* Returns the Unique Id of the Participation in a Campaign
	* @return Id of the Participation
	*/
	public UUID	getParticipationId() {
		return this.participationId;
	}
	
	/**
	* Returns the Id of the Contact involved in the Participation
	* in a Campaign
	* @return Id of the Contact
	*/
	public String getContactId() {
		return this.contactId;
	}
	
	/**
	* Returns the Id of the Campaign a Contact is participating in
	* @return Id of the Campaign
	*/
	public UUID getCampaignId() {
		return this.campaignId;
	}
	
	/**
	* If a Role level Participation returns the Role the Contact is 
	* participation in 
	* @return Id of the Role
	*/
	public Optional<UUID> getRoleId() {
		return Optional.ofNullable(this.roleId);
	}
	
	/**
	* Returns the type of the Participation the Contact has in the 
	* Campaign or Role
	* @return Participation type
	*/
	public ParticipantType getType() {
		return this.type;
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder for the Class
	*/
	public static ParticipationEntityBuilder builder() {
		return new ParticipationEntityBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class ParticipationEntityBuilder {
		
		private UUID			participationId;
		private String 			contactId;
		private UUID 			campaignId;
		private UUID 			roleId;
		private ParticipantType type;
		
		/**
		* Sets the Id of the Participation
		* @param participationId - Unique id of the Participation
		* @return Builder
		*/
		public ParticipationEntityBuilder participationId(UUID participationId) {
			this.participationId = participationId;
			return this;
		}
		
		/**
		* Sets the Id of the Contact involved in the Participation
		* @param contactId - Unique id of the Contact
		* @return Builder
		*/
		public ParticipationEntityBuilder contactId(String contactId) {
			this.contactId = contactId;
			return this;
		}
		
		/**
		* Sets the Id of the Campaign being participated in
		* @param campaignId - Unique Id of the Campaign
		* @return BUilder
		*/
		public ParticipationEntityBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* Sets, if at Role level the Role the Participation is for
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public ParticipationEntityBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets the type of the Participation of the Contact in the 
		* Campaign/Role
		* @param type - Type of Participation
		* @return Builder
		*/
		public ParticipationEntityBuilder type(ParticipantType type) {
			this.type = type;
			return this;
		}		
		
		/**
		* Returns an initialized instance
		* @return Initialized instance
		*/
		public ParticipationEntity build() {
			return new ParticipationEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity to Domain representation of a Participation
	* @param entity - To convert
	* @return Converted
	*/
	public static Participation fromEntity(ParticipationEntity entity) {
		return Participation
				.builder()
					.campaignId(entity.campaignId)
					.participationId(entity.participationId)
					.contactId(entity.contactId)
					.roleId(entity.roleId)
					.type(entity.type)
				.build();
	}
	
	/**
	* Converts from Entity to Domain representation of a Participation
	* @param entity - To convert
	* @return Converted
	*/
	public static ParticipationEntity toEntity(Participation participation) {
		return ParticipationEntity
				.builder()
					.campaignId(participation.getCampaignId())
					.participationId(participation.getParticipationId())
					.contactId(participation.getContactId())
					.roleId(participation.getRoleId().orElse(null))
					.type(participation.getType())
				.build();
	}
	
}