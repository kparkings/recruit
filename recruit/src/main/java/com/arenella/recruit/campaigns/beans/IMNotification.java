package com.arenella.recruit.campaigns.beans;

import java.util.Optional;
import java.util.UUID;

/**
* Represents an unread IM notification relating to either a Campaign or a Role.
* 
*  ** Notification is generated in IM microservice when a candidate replies to a Recruiter that has a pro account. No check to see if it was generated for a Campaign or Role only that a message
*  was sent. to the recruiter by the candidate. That is enough to assume its related to a the Campaign/Role 
* 
*/
public class IMNotification {

	private UUID 	campaignId;
	private UUID 	roleId;
	private String 	recruiterId;
	private String 	candidateId;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public IMNotification(IMNotificationBuilder builder) {
		this.campaignId 	= builder.campaignId;
		this.roleId 		= builder.roleId;
		this.recruiterId 	= builder.recruiterId;
		this.candidateId 	= builder.candidateId;	
	}
	
	/**
	* Returns the Id of the Campaign the Notification relates to
	* @return Id of the Campaign
	*/
	public UUID getCampaignId() {
		return this.campaignId;
	}
	
	/**
	* Returns the Id of the Role the Notification relates to
	* @return Id of the Role ( If role level )
	*/
	public Optional<UUID> getRoleId() {
		return Optional.ofNullable(this.roleId);
	}
	
	/**
	* Returns the Id of the recruiter that received the message 
	* resulting in the Notification
	* @return Unique Id of the Recruiter
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}
	
	/**
	* Returns the Id of the Candidate that sent the message 
	* resulting in the Notification
	* @return Unique Id of the Recruiter
	*/
	public String getCandidateId() {
		return this.candidateId;
	}
	
	/**
	* Returns a Builder for the Class
	*/
	public static IMNotificationBuilder builder() {
		return new IMNotificationBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	public static class IMNotificationBuilder {
		
		private UUID 	campaignId;
		private UUID 	roleId;
		private String 	recruiterId;
		private String 	candidateId;
		
		/**
		* Sets the Id of the Campaign that the Notification is associated with
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public IMNotificationBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* Sets the Id of the Role the Notification is associated with. Not required if the 
		* Notification is at Campaign level
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public IMNotificationBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets the Id of the Recruiter the Notification relates to
		* @param recruiterId - Id of the Recruiter
		* @return Builder
		*/
		public IMNotificationBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the Id of the Candidate that sent an IM causing the Notification
		* to be created
		* @param candidateId
		* @return Builder
		*/
		public IMNotificationBuilder candidateId(String candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Returns an initialized instance of the Class
		* @return Initialized instance of the Class
		*/
		public IMNotification build() {
			return new IMNotification(this);
		}
	}
	
}