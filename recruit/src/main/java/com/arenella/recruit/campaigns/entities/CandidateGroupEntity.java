package com.arenella.recruit.campaigns.entities;

import java.util.Optional;
import java.util.UUID;

import com.arenella.recruit.campaigns.beans.CandidateGroup;
import com.arenella.recruit.campaigns.beans.CandidateGroup.Group;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Represents a Group that the Candidate belongs to in the context
* of a Campaign or Role. This group is shared amongst all participants
* in the Campaign or Role
*/
@Entity
@Table(schema="campaigns", name="candidate_groups")
public class CandidateGroupEntity {

	@Id
	@Column(name="id")
	private UUID id;
	
	@Column(name="campaign_id")
	private UUID campaignId;
	
	@Column(name="role_id")
	private UUID roleId;

	@Column(name="candidate_id")
	private String candidateId;

	@Enumerated(EnumType.STRING)
	@Column(name="candidate_group")
	private Group group;
	
	/**
	* Default constructor 
	*/
	public CandidateGroupEntity() {
		//Hibernate
	}
	
	/**
	* Constructor via Builder
	* @param builder - Contains initialization values
	*/
	public CandidateGroupEntity(CandidateGroupEntityBuilder builder) {
		this.id 			= builder.id;
		this.campaignId 	= builder.campaignId;
		this.roleId 		= builder.roleId;
		this.candidateId 	= builder.candidateId;
		this.group 			= builder.group;
	}
	
	/**
	* Returns the unique Id of the Grouping
	* @return Id
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns the Id of the Campaign the Grouping 
	* related to 
	* @return Id or the Campaign
	*/
	public UUID getCampaignId() {
		return this.campaignId;
	}
	
	/**
	* If Role level returns the Id of the Role the 
	* Grouping is associated with
	* @return Id of the Role
	*/
	public Optional<UUID> getRoleId() {
		return Optional.ofNullable(this.roleId);
	}
	
	/**
	* Returns the Id of the Candidate the grouping is associated
	* with
	* @return Id of the Candidate
	*/
	public String getCandidateId() {
		return this.candidateId;
	}
	
	/**
	* Returns which group the Candidate has been added to 
	* @return Group the candidate is in for the Campaign/Role
	*/
	public Group getGroup() {
		return this.group;
	}
	
	/**
	* Returns a Builder for the class 
	* @return Builder for the class
	*/
	public static CandidateGroupEntityBuilder builder() {
		return new CandidateGroupEntityBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class CandidateGroupEntityBuilder {
		
		private UUID id;
		private UUID campaignId;
		private UUID roleId;
		private String candidateId;
		private Group group;
		
		/**
		* Sets the unique Id of the Grouping
		* @param id - Id of the Grouping
		* @return Builder
		*/
		public CandidateGroupEntityBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the Id of the Campaign the Candidate grouping relates to 
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public CandidateGroupEntityBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* If Role level sets the Id of the Role the Candidate Grouping related to
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public CandidateGroupEntityBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets the Id of the Candidate the Grouping related to 
		* @param candidateId - Id of the Candidate
		* @return Builder
		*/
		public CandidateGroupEntityBuilder candidateId(String candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets the Group the Candidate is in relaring to the Campaign/Role
		* @param group - Group the Candidate belongs to
		* @return Builder
		*/
		public CandidateGroupEntityBuilder group(Group group) {
			this.group = group;
			return this;
		}
		
		/**
		* Returns an initialized instance 
		* @return Initialized Instance
		*/
		public CandidateGroupEntity build() {
			return new CandidateGroupEntity(this);
		}
		
	}
	
	/**
	* Converts form Domain to Entity representation
	* @param group - To convert
	* @return Converted
	*/
	public static CandidateGroupEntity toEntity(CandidateGroup group) {
		return CandidateGroupEntity
				.builder()
					.id(group.getId())
					.campaignId(group.getCampaignId())
					.roleId(group.getRoleId().orElse(null))
					.candidateId(group.getCandidateId())
					.group(group.getGroup())
				.build();
	}
	
	/**
	* Converts form Entity to Domain representation
	* @param entity - To convert
	* @return Converted
	*/
	public static CandidateGroup fromEntity(CandidateGroupEntity entity) {
		return CandidateGroup
				.builder()
					.id(entity.getId())
					.campaignId(entity.getCampaignId())
					.roleId(entity.getRoleId().orElse(null))
					.candidateId(entity.getCandidateId())
					.group(entity.getGroup())
				.build();
	}
	
}