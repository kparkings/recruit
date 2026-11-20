package com.arenella.recruit.campaigns.entities;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.arenella.recruit.campaigns.beans.CampaignEmail;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
* Entity to represent an Email sent by a Candidate ( registered or not )
* relating to a Campaign or role 
*/
@Entity
@Table(schema="campaigns", name="emails")
public class CampaignEmailEntity {

	@Id
	@Column(name="id")
	private UUID id;
	
	@Column(name="external_email_id")
	private UUID externalEmailId;
	
	@Column(name="campaign_id")
	private UUID campaignId;
	
	@Column(name="role_id")
	private UUID roleId;
	
	@Column(name="owning_recruiter_id")
	private String owningRecruiterId;
	
	@Column(name="sending_candidate_id")
	private String sendingCandidateId;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="surname")
	private String surname;

	@Column(name="subject")
	private String subject;
	
	@OneToMany(mappedBy = "emailId", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	private Set<CampaignEmailAttachmentEntity> attachments	= new LinkedHashSet<>(); 
	
	/**
	* Default Constructor 
	*/
	public CampaignEmailEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Containst the Initalization values
	*/
	public CampaignEmailEntity(CampaignEmailEntityBuilder builder) {
		
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
	* @return - Id of the campaign
	*/
	public UUID getCampaignId() {
		return this.campaignId;
	}
	
	/**
	* Returns the Id of the Role ( If role level ) the Email is associated with
	* @return - Id of the campaign
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
	public Set<CampaignEmailAttachmentEntity> getAttachments() {
		return this.attachments;
	}
	
	/**
	* Returns a Builder for the Class 
	* @return
	*/
	public static CampaignEmailEntityBuilder builder() {
		return new CampaignEmailEntityBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class CampaignEmailEntityBuilder {
		
		private UUID 								id;
		private UUID 								externalEmailId;
		private UUID 								campaignId;
		private UUID 								roleId;
		private String 								owningRecruiterId;
		private String 								sendingCandidateId;
		private String 								firstName;
		private String 								surname;
		private String 								subject;
		private Set<CampaignEmailAttachmentEntity> 	attachments					= new LinkedHashSet<>(); 
		
		/**
		* Sets the Unique Id of the email
		* @param id - Id of the email
		* @return Builder
		*/
		public CampaignEmailEntityBuilder id(UUID id) {
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
		public CampaignEmailEntityBuilder externalEmailId(UUID externalEmailId) {
			this.externalEmailId = externalEmailId;
			return this;
		}
		
		/**
		* Sets the Campaign the Email is associated with
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public CampaignEmailEntityBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* Sets the Role the Email is associated if Role level
		* @param roleId - Id of the role
		* @return Builder
		*/
		public CampaignEmailEntityBuilder roleId(UUID roleId) {
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
		public CampaignEmailEntityBuilder owningRecruiterId(String owningRecruiterId) {
			this.owningRecruiterId = owningRecruiterId;
			return this;
		}
		
		/**
		* 
		* @param sendingCandidateId
		* @return
		*/
		public CampaignEmailEntityBuilder sendingCandidateId(String sendingCandidateId) {
			this.sendingCandidateId = sendingCandidateId;
			return this;
		}
		
		/**
		* Sets the first name of the person who sent the email
		* @param firstName - First name of the Sender
		* @return Builder Builder
		*/
		public CampaignEmailEntityBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		* Sets the surname of the person who sent the email
		* @param surname - Surname of the sender
		* @return Builder 
		*/
		public CampaignEmailEntityBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* 
		* @param subject
		* @return
		*/
		public CampaignEmailEntityBuilder subject(String subject) {
			this.subject = subject;
			return this;
		}
		
		/**
		* 
		* @param attachments
		* @return
		*/
		public CampaignEmailEntityBuilder attachments(Set<CampaignEmailAttachmentEntity> attachments) {
			this.attachments.clear();
			this.attachments.addAll(attachments);
			return this;
		} 
		
		/**
		* Returns an initialized instance 
		* @return Initialized instance
		*/
		public CampaignEmailEntity build() {
			return new CampaignEmailEntity(this);
		}
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param email - To convert
	* @return converted
	*/
	public static CampaignEmailEntity toEntity(CampaignEmail email) {
		return CampaignEmailEntity
				.builder()
					.id(email.getId())
					.externalEmailId(email.getExternalEmailId())
					.campaignId(email.getCampaignId())
					.roleId(email.getRoleId().orElse(null))
					.attachments(email.getAttachments().stream().map(CampaignEmailAttachmentEntity::toEntity).collect(Collectors.toSet()))
					.firstName(email.getFirstName())
					.owningRecruiterId(email.getOwningRecruiterId())
					.sendingCandidateId(email.getSendingCandidateId().orElse(null))
					.subject(email.getSubject())
					.surname(email.getSurname())
				.build();
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param email - To convert
	* @return converted
	*/
	public static CampaignEmail fromEntity(CampaignEmailEntity entity) {
		return CampaignEmail
				.builder()
					.id(entity.getId())
					.externalEmailId(entity.getExternalEmailId())
					.campaignId(entity.getCampaignId())
					.roleId(entity.getRoleId().orElse(null))
					.attachments(entity.getAttachments().stream().map(CampaignEmailAttachmentEntity::fromEntity).collect(Collectors.toSet()))
					.firstName(entity.getFirstName())
					.owningRecruiterId(entity.getOwningRecruiterId())
					.sendingCandidateId(entity.getSendingCandidateId().orElse(null))
					.subject(entity.getSubject())
					.surname(entity.getSurname())
				.build();
	}
	
}