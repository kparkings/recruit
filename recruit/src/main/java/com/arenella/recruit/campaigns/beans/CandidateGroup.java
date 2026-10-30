package com.arenella.recruit.campaigns.beans;

import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

/**
* Class represents a Grouping of a given candidate within a Campaign or a Group.
* This grouping is a way for Recruiters to group candidates. 
* 
* Example use case:
* Group1 = Candidates not yet messaged about a role
* Group2 = Candidate messaged but haven't replied
* Group3 = Candidates not interested in the role
* Group4 = Candidates sent to client
* Group5 = Unsuitable Candidates
* 
* The exact meaning of the group is for the recruiter to decide. 
*/
public class CandidateGroup {

	public enum Group {GROUP0, GROUP1, GROUP2, GROUP3, GROUP4, GROUP5}
	
	@Id
	@Column(name="id")
	private UUID id;
	
	@Column(name="campaign_id")
	private UUID campaignId;
	
	@Column(name="role_id")
	private UUID roleId;

	@Column(name="candidate_id")
	private String candidateId;

	@Column(name="group")
	private Group group;
	
	/**
	* Constructor via Builder
	* @param builder - Contains initialization values
	*/
	public CandidateGroup(CandidateGroupBuilder builder) {
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
	public static CandidateGroupBuilder builder() {
		return new CandidateGroupBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class CandidateGroupBuilder {
		
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
		public CandidateGroupBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the Id of the Campaign the Candidate grouping relates to 
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public CandidateGroupBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* If Role level sets the Id of the Role the Candidate Grouping related to
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public CandidateGroupBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets the Id of the Candidate the Grouping related to 
		* @param candidateId - Id of the Candidate
		* @return Builder
		*/
		public CandidateGroupBuilder candidateId(String candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets the Group the Candidate is in relaring to the Campaign/Role
		* @param group - Group the Candidate belongs to
		* @return Builder
		*/
		public CandidateGroupBuilder group(Group group) {
			this.group = group;
			return this;
		}
		
		/**
		* Returns an initialized instance 
		* @return Initialized Instance
		*/
		public CandidateGroup build() {
			return new CandidateGroup(this);
		}
	}
}