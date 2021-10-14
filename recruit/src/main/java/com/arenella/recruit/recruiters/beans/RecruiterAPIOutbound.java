package com.arenella.recruit.recruiters.beans;

import com.arenella.recruit.recruiters.beans.Recruiter.language;

/**
* API Outbound representation of a Recruiter
* @author K Parkings
*/
public class RecruiterAPIOutbound {

	private String 		userId;
	private String 		firstName;
	private String 		surname;
	private String 		email;
	private String		companyName;
	private boolean 	active			= true;
	private language 	language;
	
	/**
	* Constuctor based upon a builder
	* @param builder - Contains initialization details
	*/
	public RecruiterAPIOutbound(RecruiterAPIOutboundBuilder builder) {
		
		this.userId 			= builder.userId;
		this.firstName 			= builder.firstName;
		this.surname 			= builder.surname;
		this.email 				= builder.email;
		this.companyName 		= builder.companyName;
		this.active 			= builder.active;
		this.language 			= builder.language;
		
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
	* Returns a Builder for the Recruiter class
	* @return Builder for the Recruiter class
	*/
	public static RecruiterAPIOutboundBuilder builder() {
		return new RecruiterAPIOutboundBuilder();
	}
	
	/**
	* Builder for the Recruiter class 
	* @author K Parkings
	*/
	public static class RecruiterAPIOutboundBuilder{
		
		private String 		userId;
		private String 		firstName;
		private String 		surname;
		private String 		email;
		private String		companyName;
		private boolean 	active			= true;
		private language 	language;
		
		/**
		* Sets the userId associated with the Recruiter
		* @param userId - Unique userId for the Recruiter
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets the firstname of the Recruiter
		* @param firstName - Firstname of the Recruiter
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		* Sets the Surname of the Recruiter
		* @param surname - Surname of the Recruiter
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Email address for the Recruiter
		* @param email - Email address of the Recruiter
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the name of the Company the Recruiter works for 
		* @param companyName - Name of the Company the Recruiter works for
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder companyName(String companyName) {
			this.companyName = companyName;
			return this;
		}
		
		/**
		* Sets whether or not the account for the Recruiter is active
		* @param active - Whether or not the account is active
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder active(boolean active) {
			this.active = active;
			return this;
		}
		
		/**
		* Sets the Language spoken by the Recruiter 
		* @param language - Language spoken by the Recruiter
		* @return Builder
		*/
		public RecruiterAPIOutboundBuilder language(language language) {
			this.language = language;
			return this;
		}
		
		/**
		* Returns an instance of Recruiter initialized with the 
		* values in the builder
		* @return Initalized instance of Recruiter
		*/
		public RecruiterAPIOutbound build() {
			return new RecruiterAPIOutbound(this);
		}
		
	}
	
	/**
	* Converts a Domain representation of a Recruiter to a 
	* API Outbound representation
	* @param recruiter - Domain representation to convert
	* @return API Outbound version representation of the Recruiter
	*/
	public static RecruiterAPIOutbound convertFromDomain(Recruiter recruiter) {
		
		return RecruiterAPIOutbound
								.builder()
									.companyName(recruiter.getCompanyName())
									.email(recruiter.getEmail())
									.firstName(recruiter.getFirstName())
									.active(recruiter.isActive())
									.language(recruiter.getLanguage())
									.surname(recruiter.getSurname())
									.userId(recruiter.getUserId())
								.build();
	}
	
}