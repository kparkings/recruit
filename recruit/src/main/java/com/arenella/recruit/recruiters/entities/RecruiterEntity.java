package com.arenella.recruit.recruiters.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.recruiters.beans.Recruiter;
import com.arenella.recruit.recruiters.beans.Recruiter.language;

/**
* Entity representation of a Recruiter 
* @author K Parkings
*/
@Entity
@Table(schema="recruiter", name="recruiter")
public class RecruiterEntity {

	/**
	* For Hibernate 
	*/
	public RecruiterEntity() {}
	
	@Id
	@Column(name="user_id")
	private String 		userId;
	
	@Column(name="firstname")
	private String 		firstName;
	
	@Column(name="surname")
	private String 		surname;
	
	@Column(name="email")
	private String 		email;
	
	@Column(name="company_name")
	private String		companyName;
	
	@Column(name="active")
	private boolean 	active			= true;
	
	@Column(name="language_spoken")
	@Enumerated(EnumType.STRING)
	private language 	language;
	
	@Column(name="account_created")
	private LocalDate	accountCreated;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public RecruiterEntity(RecruiterEntityBuilder builder) {
	
		this.userId 		= builder.userId;
		this.firstName 		= builder.firstName;
		this.surname 		= builder.surname;
		this.email 			= builder.email;
		this.companyName 	= builder.companyName;
		this.active			= builder.active;
		this.language 		= builder.language;
		this.accountCreated = builder.accountCreated;

	}
	
	/**
	* Returns the recruiters loginId
	* @return Unique login for the Recruiter
	*/
	public String getUserId() {
		return userId;
	}
	
	/**
	* Returns the firstname of the Recruiter
	* @return Recruiters first name
	*/
	public String getFirstName() {
		return firstName;
	}
	
	/**
	* Returns the surname of the Recruiter
	* @return Recruiters Surname
	*/
	public String getSurname() {
		return surname;
	}
	
	/**
	* Returns the Recruiters email address
	* @return Email address of the Recruiter
	*/
	public String getEmail() {
		return email;
	}
	
	/**
	* Returns the name of the recruitment company the 
	* Recruiter works for
	* @return
	*/
	public String getCompanyName() {
		return companyName;
	}
	
	/**
	* Returns whether or not the Recruiter account is 
	* active
	* @return whether the account is active
	*/
	public boolean isActive(){
		return active;
	}
	
	/**
	* Returns the language spoken by the Recruiter
	* @return language spoken by the recruiter
	*/
	public language getLanguage(){
		return language;
	}

	/**
	* Returns the date the recruiters account was created
	* @return
	*/
	public LocalDate getAccountCreated() {
		return accountCreated;
	}
	
	/**
	* Returns a Builder for the RecruiterEntity class
	* @return Builder for the RecruiterEntity class
	*/
	public static RecruiterEntityBuilder builder() {
		return new RecruiterEntityBuilder();
	}

	/**
	* Sets the UserId of the Recruiter
	* @param userId - Unique id of the Recruiter
	*/
	public void setUserId(String userId) {
		this.userId = userId.toLowerCase().trim();
	}
	
	/**
	* Sets the firstname of the Recruiter
	* @param firstname - Recruiters first name
	*/
	public void setFirstName(String firstname) {
		this.firstName = firstname.toLowerCase().trim();
	}
	
	/**
	* Sets the surname of the Recruiter
	* @param surname - Recruiters surname
	*/
	public void setSurname(String surname) {
		this.surname = surname.toLowerCase().trim();
	}
	
	/**
	* Sets the email address of the Recruiter
	* @param email - email address of the Recruiter
	*/
	public void setEmail(String email) {
		this.email = email.toLowerCase().trim();
	}
	
	/**
	* Sets the name of the Company the Recruiter works for
	* @param companyName - Company name
	*/
	public void setCompanyName(String companyName) {
		this.companyName = companyName.toLowerCase().trim();
	}
	
	/**
	* Sets the primary langauge spoken by the Recruiter 
	* @param language - language spoken by the Recruiter
	*/
	public void setLanguage(language language) {
		this.language = language;
	}

	/**
	* Builder for the RecruiterEntity class
	* @author K Parkings
	*/
	public static class RecruiterEntityBuilder {
		
		private String 		userId;
		private String 		firstName;
		private String 		surname;
		private String 		email;
		private String		companyName;
		private boolean 	active			= true;
		private language 	language;
		private LocalDate	accountCreated;
		
		/**
		* Sets the Recruiters unique iderId
		* @param userId - UserId associated with the Recruiter
		* @return Builder
		*/
		public RecruiterEntityBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets the firstname of the Recruiter
		* @param firstName - firstname of the Recruiter
		* @return Builder
		*/
		public RecruiterEntityBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
	
		/**
		* Sets the Suranme of of the Recruiter
		* @param surname - Surname of the Recruiter
		* @return Builder
		*/
		public RecruiterEntityBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Recruiters email address
		* @param email - Recruiters email address
		* @return Builder
		*/
		public RecruiterEntityBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the name of the Company the Recruiter works for
		* @param companyName - Company the recruiter is working for
		* @return Builder
		*/
		public RecruiterEntityBuilder companyName(String companyName) {
			this.companyName = companyName;
			return this;
		}
		
		/**
		* Sets whether or not the Recrutiers account is active
		* @param active - Whether or not the Recruiter is active
		* @return Builder
		*/
		public RecruiterEntityBuilder active(boolean active) {
			this.active = active;
			return this;
		}
		
		/**
		* Sets the primary language spoken by the Recruiter
		* @param language - Language spoken by the Recruiter
		* @return Builder
		*/
		public RecruiterEntityBuilder language(language language) {
			this.language = language;
			return this;
		}
		
		/**
		* Sets the Date the Recrutier was added to the System
		* @param accountCreated - Date Recruiter added to the Syste,
		* @return Builder
		*/
		public RecruiterEntityBuilder accountCreated(LocalDate accountCreated) {
			this.accountCreated = accountCreated;
			return this;
		}
		
		/**
		* Returns a new RecruiterEntity initialized with the 
		* values in the Builder
		* @return Initalized instance of the RecruiterEntity
		*/
		public RecruiterEntity build() {
			return new RecruiterEntity(this);
		}
		
	}
	
	/**
	* 
	* @param recruiter
	* @return
	*/
	public static RecruiterEntity convertToEntity(Recruiter recruiter) {
		return RecruiterEntity
						.builder()
							.accountCreated(recruiter.getAccountCreated())
							.active(recruiter.isActive())
							.companyName(recruiter.getCompanyName())
							.email(recruiter.getEmail())
							.firstName(recruiter.getFirstName())
							.language(recruiter.getLanguage())
							.surname(recruiter.getSurname())
							.userId(recruiter.getUserId())
						.build();
	}
	
	/**
	* 
	* @param recruiter
	* @return
	*/
	public static Recruiter convertFromEntity(RecruiterEntity recruiterEntity) {
		return Recruiter
				.builder()
					.accountCreated(recruiterEntity.getAccountCreated())
					.active(recruiterEntity.isActive())
					.companyName(recruiterEntity.getCompanyName())
					.email(recruiterEntity.getEmail())
					.firstName(recruiterEntity.getFirstName())
					.language(recruiterEntity.getLanguage())
					.surname(recruiterEntity.getSurname())
					.userId(recruiterEntity.getUserId())
				.build();
	}
	
}