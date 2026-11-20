package com.arenella.recruit.campaigns.beans;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
* A Campaign email is an email that is associated with a Campaign.
* For example if a recruiter has added a listing to the job board 
* and they want to associate it with a Campaign or Role so that 
* they can manage it via the campaign and make it available for
* other participants to read they can opt to associate it with the 
* campaign so it is viewable as part of the campaign
*/
public class CampaignEmail {

	private UUID 							id;
	private UUID 							externalEmailId;
	private UUID 							campaignId;
	private UUID 							roleId;
	private String 							owningRecruiterId;
	private String 							sendingCandidateId;
	private String 							firstName;
	private String 							surname;
	private String 							subject;
	private Set<CampaignEmailAttachment> 	attachments	= new LinkedHashSet<>(); 
	
	/**
	* Default Constructor 
	*/
	public CampaignEmail() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Containst the Initalization values
	*/
	public CampaignEmail(CampaignEmailBuilder builder) {
		
		this.id 				= builder.id;
		this.externalEmailId	= builder.externalEmailId;
		this.campaignId			= builder.campaignId;
		this.roleId				= builder.roleId;
		this.owningRecruiterId 	= builder.owningRecruiterId;
		this.sendingCandidateId	= builder.sendingCandidateId;
		this.firstName 			= builder.firstName;
		this.surname 			= builder.surname;
		this.subject 			= builder.subject;
		
		this.attachments.clear();
		this.attachments.addAll(builder.attachments);
	}
	
	/**
	* Returns the Id of the Email
	* @return - Id of the email
	*/
	public UUID getId() {
		return this.id;
	}
	
	/**
	* Returns the Id of the Email in the external service
	* @return - Id of the email in the external service
	*/
	public UUID getExternalEmailId() {
		return this.externalEmailId;
	}
	
	/**
	* Returns the Id of the Campaign the Email is associated with
	* @return - Id of the Campaign
	*/
	public UUID getCampaignId() {
		return this.campaignId;
	}
	
	/**
	* Returns the Id of the Role ( If Role level ) the Email 
	* is associated with
	* @return - Id of the Role
	*/
	public Optional<UUID> getRoleId() {
		return Optional.ofNullable(this.roleId);
	}
	
	/**
	* Returns the Id of the Recruiter that the email was originally sent to. The email is visible to 
	* any recruiter in the Campaign/Role but the recruiter remains the owner and this Id can be 
	* used to link back to that recruiter.
	* @return
	*/
	public String getOwningRecruiterId() {
		return this.owningRecruiterId;
	}
	
	/**
	* If the person who sent the email was logged in then this is their candidateId. This will not 
	* be present if the the email was sent by an external candidate or the candidate was not 
	* logged in when sending the email
	* @return Id of the candidate sending the email
	*/
	public Optional<String> getSendingCandidateId() {
		return Optional.ofNullable(this.sendingCandidateId);
	}
	
	/**
	* Returns the email senders first name
	* @return first name of sender
	*/
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	* Returns the email senders surname
	* @return surname of sender
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* Returns the email subject
	* @return email subject
	*/
	public String getSubject() {
		return this.subject;
	}
	
	/**
	* Returns any attachements for the email
	* @return attachements
	*/
	public Set<CampaignEmailAttachment> getAttachments() {
		return this.attachments;
	}
	
	/**
	* Returns a Builder for the Class 
	* @return
	*/
	public static CampaignEmailBuilder builder() {
		return new CampaignEmailBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class CampaignEmailBuilder {
		
		private UUID 								id;
		private UUID 								externalEmailId;
		private UUID 								campaignId;
		private UUID 								roleId;
		private String 								owningRecruiterId;
		private String 								sendingCandidateId;
		private String 								firstName;
		private String 								surname;
		private String 								subject;
		private Set<CampaignEmailAttachment> 		attachments					= new LinkedHashSet<>(); 
		
		/**
		* Sets the Unique Id of the email
		* @param id - Id of the email
		* @return Builder
		*/
		public CampaignEmailBuilder id(UUID id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the external email. This is needed as an email could be associated with more than one
		* Campaign or Role. Having both Id an externalEmailId allows the external email to be used 
		* in multiple roles a campaigns completely separate from one another while the externalEmaildId
		* allows us to link back to the actual email in owned by an external service
		* @param externalEmailId - Id of the email in the external service
		* @return Builder
		*/
		public CampaignEmailBuilder externalEmailId(UUID externalEmailId) {
			this.externalEmailId = externalEmailId;
			return this;
		}
		
		/**
		* Sets the Campaign the Email is associated with
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public CampaignEmailBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* Sets the Role the Email is associated if Role level
		* @param roleId - Id of the role
		* @return Builder
		*/
		public CampaignEmailBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Sets the Id of the recruiter who is the official owner of the Email. In the case of Campaigns
		* the email belongs to one recruiter but is available to those in the Campaigin/Role. This id
		* can be used to link back to the original email in the external email service
		* @param owningRecruiterId - Id of the recruiter who owns the email
		* @return Builder
		*/
		public CampaignEmailBuilder owningRecruiterId(String owningRecruiterId) {
			this.owningRecruiterId = owningRecruiterId;
			return this;
		}
		
		public CampaignEmailBuilder sendingCandidateId(String sendingCandidateId) {
			this.sendingCandidateId = sendingCandidateId;
			return this;
		}
		
		/**
		* Sets the first name of the person who sent the email
		* @param firstName - First name of the Sender
		* @return Builder Builder
		*/
		public CampaignEmailBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		* Sets the surname of the person who sent the email
		* @param surname - Surname of the sender
		* @return Builder 
		*/
		public CampaignEmailBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* 
		* @param subject
		* @return
		*/
		public CampaignEmailBuilder subject(String subject) {
			this.subject = subject;
			return this;
		}
		
		/**
		* 
		* @param attachments
		* @return
		*/
		public CampaignEmailBuilder attachments(Set<CampaignEmailAttachment> attachments) {
			this.attachments.clear();
			this.attachments.addAll(attachments);
			return this;
		} 
		
		/**
		* Returns an initialized instance 
		* @return Initialized instance
		*/
		public CampaignEmail build() {
			return new CampaignEmail(this);
		}
	}
	
}
