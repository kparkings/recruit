package com.arenella.recruit.campaign.dao;

import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Entity representation of a Notification 
*/
@Entity
@Table(schema="campaigns", name="im_notifications")
public class IMNotificationEntity {

	@Id
	@Column(name="id")
	private UUID 	notificationId;
	
	@Column(name="campaign_id")
	private UUID 	campaignId;
	
	@Column(name="role_id")
	private UUID 	roleId;
	
	@Column(name="recruiter_id")
	private String 	recruiterId;
	
	@Column(name="candidate_id")
	private String 	candidateId;
	
	/**
	* Default constructor
	*/
	public IMNotificationEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public IMNotificationEntity(IMNotificationEntityBuilder builder) {
		this.notificationId 	= builder.notificationId;
		this.campaignId 		= builder.campaignId;
		this.roleId 			= builder.roleId;
		this.recruiterId 		= builder.recruiterId;
		this.candidateId 		= builder.candidateId;
	}
	
	/**
	* Returns the unique Id of the Notification
	* @return Id of the Notification
	*/
	public UUID getNotificationId() {
		return this.notificationId;
	}
	
	/**
	* Returns the unique Id of the Campaign the Notification
	* is associated with
	* @return Id of the Campaign
	*/
	public UUID getCampaignId() {
		return this.campaignId;
	}
	
	/**
	* Returns the unique Id of the Role ( if Role based ) that the 
	* Notification is associated with
	* @return Id of the role
	*/
	public Optional<UUID> getRoleId() {
		return Optional.ofNullable(this.roleId);
	}
	
	/**
	* Returns the Id of the recruiter the Notification related to. This is the 
	* Recruiter that has a new IM message from the Candidate
	* @return Id of the Recruiter
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}
	
	/**
	* Returns the Id of the Candidate that send the IM that results in the 
	* Notification 
	* @return Id of the Candidate
	*/
	public String getCandidateId() {
		return this.candidateId;
	}
	
	/**
	* Returns a Builder for the class 
	* @return Builder for the class
	*/
	public static IMNotificationEntityBuilder builder() {
		return new IMNotificationEntityBuilder();
	}
	
	/**
	* Builder for the class
	*/
	public static class IMNotificationEntityBuilder {
		
		private UUID 	notificationId;
		private UUID 	campaignId;
		private UUID 	roleId;
		private String 	recruiterId;
		private String 	candidateId;
		
		/**
		* Sets the UniqueId of the Notification
		* @param notificationId - Id of the Notification
		* @return
		*/
		public IMNotificationEntityBuilder notificationId(UUID notificationId) {
			this.notificationId = notificationId;
			return this;
		}
		
		/**
		* Sets the Id of the Campaign the Notification is associated with
		* @param campaignId - Id of the Campaign
		* @return
		*/
		public IMNotificationEntityBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* If Role based Sets the Id of the Role
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public IMNotificationEntityBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets the Id of the Recruiter the IM was sent to
		* @param recruiterId - Id of the Recruiter
		* @return Builder
		*/
		public IMNotificationEntityBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the Id of the Candidate that send an IM causing the Notification
		* to be Generated
		* @param candidateId - Id of the Candidate
		* @return Builder
		*/
		public IMNotificationEntityBuilder candidateId(String candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Returns initialized instance
		* @return Initialized instance
		*/
		public IMNotificationEntity build() {
			return new IMNotificationEntity(this);
		}
		
	}
	
}