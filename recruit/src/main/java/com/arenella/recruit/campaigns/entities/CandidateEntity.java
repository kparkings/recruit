package com.arenella.recruit.campaigns.entities;

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
	
	@Column(name="deleted_from_system")
	private boolean deletedFromSystem;
	
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
		this.id 				= builder.id;
		this.type 				= builder.type;
		this.firstName 			= builder.firstName;
		this.surname 			= builder.surname;
		this.countryCode 		= builder.countryCode;
		this.jobTitle 			= builder.jobTitle;
		this.email 				= builder.email;
		this.deletedFromSystem 	=  builder.deletedFromSystem;
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
		
		private String 	id;
		private Type 	type;
		private String 	firstName;
		private String 	surname;
		private String  countryCode;
		private String 	jobTitle;
		private String 	email;
		private boolean deletedFromSystem;
		
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
				.build();
	}
	
}