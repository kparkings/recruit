package com.arenella.recruit.campaigns.entities;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.arenella.recruit.campaigns.beans.Candidate;
import com.arenella.recruit.campaigns.beans.Candidate.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Entity representation of a Candidate 
*/
@Entity
@Table(schema="campaigns", name="candidates")
public class CandidateEntity {

	@Id
	@Column(name="id")
	private String 	id;
	
	@Column(name="type")
	@Enumerated(EnumType.STRING)
	private Type 	type;
	
	@Column(name="first_name")
	private String 	firstName;
	
	@Column(name="surname")
	private String 	surname;
	
	@Column(name="country_code")
	private String  countryCode;
	
	@Column(name="job_title")
	private String 	jobTitle;
	
	@Column(name="email")
	private String 	email;
	
	@Column(name="created")
	private LocalDateTime		created;

	@Column(name="deleted_from_system")
	private boolean deletedFromSystem;
	
	@Column(name="ext_candidate_last_candidate_data_retention_confirmation")
	private LocalDateTime lastDataRetentionConfirmation;
	
	@Column(name="ext_candidate_data_retention_renewal_sent")
	private LocalDateTime dataRetentionRenewalEmailSent;
	
	@Column(name="ext_candidate_created_by")
	private String createdBy;
	
	@Column(name="ext_candidate_campaign_id")
	private UUID campaignId;
	
	@Column(name="ext_candidate_role_id")
	private UUID roleId;
	
	/**
	* Default constructor 
	*/
	public CandidateEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public CandidateEntity(CandidateEntityBuilder builder) {
		this.id 							= builder.id;
		this.type 							= builder.type;
		this.firstName 						= builder.firstName;
		this.surname 						= builder.surname;
		this.countryCode 					= builder.countryCode;
		this.jobTitle 						= builder.jobTitle;
		this.email 							= builder.email;
		this.created						= builder.created;
		this.lastDataRetentionConfirmation 	= builder.lastDataRetentionConfirmation;
		this.dataRetentionRenewalEmailSent	= builder.dataRetentionRenewalEmailSent;
		this.deletedFromSystem 				= builder.deletedFromSystem;
		this.createdBy 						= builder.createdBy;
		this.campaignId 					= builder.campaignId;
		this.roleId 						= builder.roleId;
	}
	
	/**
	* Returns the unique Id of the Candidate
	* @return
	*/
	public String getId() {
		return this.id;
	}
	
	/**
	* Returns whether the Candidate is a Registered Candidate 
	* in the System or a reference to one of the Recruiters 
	* external Candidates
	* @return Candidate Type
	*/
	public Type getType() {
		return this.type;
	}
	
	/**
	* Returns the firstName of the Candidate
	* @return first name
	*/
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	* Returns the surname of the Candidate
	* @return surname
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* Returns the code of the Country where the 
	* Candidate is based
	* @return Country code 
	*/
	public String getCountryCode() {
		return this.countryCode;
	}
	
	/**
	* Returns the Job title of the Candidate
	* @return job title
	*/
	public String getJobTitle() {
		return this.jobTitle;
	}
	
	/**
	* Returns the candidates email address
	* @return Candidates email address
	*/
	public String getEmail() {
		return this.email;
	}
	
	/**
	* Date the Candidate was added to the Campaign/Role
	* @return date
	*/
	public LocalDateTime getCreated() {
		return this.created;
	}
	
	/**
	* Returns the last time the external candidate gave permission to store their
	* details
	* @return last date of authorisation
	*/
	public Optional<LocalDateTime> getLastDataRetentionConfirmation(){
		return Optional.ofNullable(this.lastDataRetentionConfirmation);
	}
	
	/**
	* Returns the last time an email was sent to the candidate asking for 
	* permission to keep their details for longer 
	* @return last time email sent
	*/
	public Optional<LocalDateTime> getDataRetentionRenewalEmailSent(){
		return Optional.ofNullable(this.dataRetentionRenewalEmailSent);
	}
	
	/**
	* For an external candidate returns the ID of the recruiter that 
	* added them to the system
	* @return
	*/
	public String getCreatedBy() {
		return this.createdBy;
	} 	
			
	/**
	* For external candidates if this is a Campaign level association, the Id
	* of the Campaign
	* @return Id of the Campaign External candidates is associated with
	*/
	public Optional<UUID> getCampaignId() {
		return Optional.ofNullable(this.campaignId);
	}
	
	/**
	* For external candidates if this is a Role level association, the Id
	* of the Role
	* @return Id of the Role External candidates is associated with
	*/
	public Optional<UUID> getRoleId() {
		return Optional.ofNullable(this.roleId);
	}
	
	/**
	* If a Candidate has deleted their profile form the 
	* system we need to delete their details. Their Candidate 
	* record will be updated to remove identifying details but the 
	* Id will remain to allow Recruiters to see that the Candidate 
	* they had previously added to a Campaign/Role is no 
	* longer in the system
	* @return is the Candidate has been deleted from the System
	*/
	public boolean isDeleteFromSystem() {
		return deletedFromSystem;
	}
	
	/**
	* Returns a builder for the class
	* @return Builder for the class
	*/
	public static CandidateEntityBuilder builder() {
		return new CandidateEntityBuilder();
	}
	
	/**
	* Builder for the Class 
	*/
	public static class CandidateEntityBuilder {
		
		private String 				id;
		private Type 				type;
		private String 				firstName;
		private String 				surname;
		private String  			countryCode;
		private String 				jobTitle;
		private String 				email;
		private LocalDateTime		created;
		private LocalDateTime 		lastDataRetentionConfirmation;
		private LocalDateTime 		dataRetentionRenewalEmailSent;
		private boolean 			deletedFromSystem;
		private String 				createdBy;
		private UUID 				campaignId;
		private UUID 				roleId;
		
		/**
		* Sets the Id of the Candidate
		* @param id - Candidate Id 
		* @return Builder
		*/
		public CandidateEntityBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the Type of the Candidate
		* @param type - Candidate Type
		* @return Builder
		*/
		public CandidateEntityBuilder type(Type type) {
			this.type = type;
			return this;
		}
		
		/**
		* Sets the Candidate's firstName
		* @param firstName - firstName of the Candidate
		* @return Builder
		*/
		public CandidateEntityBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		* Sets the surname of the Candidate
		* @param surname - Candidates surname
		* @return Builder
		*/
		public CandidateEntityBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Country code of the Country where the Candidate
		* resides
		* @param countryCode - Code of Country
		* @return Builder
		*/
		public CandidateEntityBuilder countryCode(String countryCode) {
			this.countryCode = countryCode;
			return this;
		}
		
		/**
		* Sets the Candidate's Job title
		* @param jobTitle - Role Candidate performs
		* @return Builder
		*/
		public CandidateEntityBuilder jobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
			return this;
		}
		
		/**
		* Sets the email address to contact the Candidate
		* @param email - Candidates email address
		* @return Builder
		*/
		public CandidateEntityBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets whether the Candidate has been deleted from the System
		* @param deletedFromSystem - If Candidate has been deleted from the System
		* @return Builder
		*/
		public CandidateEntityBuilder deletedFromSystem(boolean deletedFromSystem) {
			this.deletedFromSystem = deletedFromSystem;
			return this;
		}
		
		/**
		* Sets when the Candidate added
		* @param created - Data Candidate added
		* @return Builder
		*/
		public CandidateEntityBuilder created(LocalDateTime created) {
			this.created = created;
			return this;
		}
		
		/**
		* Sets the last time the Candidate agreed to have their details stored in the System. This is specific 
		* to External candidates. Candidates with profiles can manage/delete their own profile
		* @param lastDataRetentionConfirmation - Date of last time permission was given to store details
		* @return Builder
		*/
		public CandidateEntityBuilder lastDataRetentionConfirmation(LocalDateTime lastDataRetentionConfirmation) {
			this.lastDataRetentionConfirmation = lastDataRetentionConfirmation;
			return this;
		}
		
		/**
		* Sets the last time a email was sent to the Candidate asking for permission to retain their 
		* details in the system
		* @param dataRetentionRenewalEmailSent - When last email was sent
		* @return Builder
		*/
		public CandidateEntityBuilder dataRetentionRenewalEmailSent(LocalDateTime dataRetentionRenewalEmailSent) {
			this.dataRetentionRenewalEmailSent = dataRetentionRenewalEmailSent;
			return this;
		}
		
		/**
		* For External candidates the Id of the Recruiter that created them 
		* @param createdBy - Id of Recruiter
		* @return Builder
		*/
		public CandidateEntityBuilder createdBy(String createdBy) {
			this.createdBy = createdBy;
			return this;
		}
		
		/**
		* For External candidates the id of the Campaign they are associated with if the 
		* association is at Campaign level
		* @param campaignId - Id of the Campaign
		* @return Builder
		*/
		public CandidateEntityBuilder campaignId(UUID campaignId) {
			this.campaignId = campaignId;
			return this;
		}
		
		/**
		* For External candidates the id of the Role they are associated with if the 
		* association is at Role level
		* @param roleId - Id of the Role
		* @return Builder
		*/
		public CandidateEntityBuilder roleId(UUID roleId) {
			this.roleId = roleId;
			return this;
		}
		
		/**
		* Returns an initialized instance 
		* @return initialized instance
		*/
		public CandidateEntity build() {
			return new CandidateEntity(this);
		}
		
	}
	
	/**
	* Converts from Entity to Domain representation
	* @param entity - To convert
	* @return converted
	*/
	public static Candidate fromEntity(CandidateEntity entity) {
		return Candidate
				.builder()
					.deletedFromSystem(entity.isDeleteFromSystem())
					.countryCode(entity.getCountryCode())
					.email(entity.getEmail())
					.firstName(entity.getFirstName())
					.id(entity.getId())
					.jobTitle(entity.getJobTitle())
					.surname(entity.getSurname())
					.type(entity.getType())
					.created(entity.getCreated())
					.lastDataRetentionConfirmation(entity.getLastDataRetentionConfirmation().orElse(null))
					.dataRetentionRenewalEmailSent(entity.getDataRetentionRenewalEmailSent().orElse(null))
					.createdBy(entity.getCreatedBy())
					.campaignId(entity.getCampaignId().orElse(null))
					.roleId(entity.getRoleId().orElse(null))
				.build();
	}
	
	/**
	* Converts from Domain to Entity representation
	* @param candidate - To convert
	* @return converted
	*/
	public static CandidateEntity toEntity(Candidate candidate) {
		return CandidateEntity
				.builder()
					.deletedFromSystem(candidate.isDeleteFromSystem())
					.countryCode(candidate.getCountryCode())
					.email(candidate.getEmail())
					.firstName(candidate.getFirstName())
					.id(candidate.getId())
					.jobTitle(candidate.getJobTitle())
					.surname(candidate.getSurname())
					.type(candidate.getType())
					.created(candidate.getCreated())
					.lastDataRetentionConfirmation(candidate.getLastDataRetentionConfirmation().orElse(null))
					.dataRetentionRenewalEmailSent(candidate.getDataRetentionRenewalEmailSent().orElse(null))
					.createdBy(candidate.getCreatedBy())
					.campaignId(candidate.getCampaignId().orElse(null))
					.roleId(candidate.getRoleId().orElse(null))
				.build();
	}
	
}