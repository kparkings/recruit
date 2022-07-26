package com.arenella.recruit.recruiters.beans;

import com.arenella.recruit.recruiters.beans.Recruiter.language;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Inbound API representation of a Recruiter
* @author K Parkings
*/
@JsonDeserialize(builder=RecruiterAPIInbound.RecruiterAPIInboundBuilder.class)
public class RecruiterAPIInbound {

	private String 		userId;
	private String 		firstName;
	private String 		surname;
	private String 		email;
	private String		companyName;
	private language 	language;
	
	/**
	* Constuctor based upon a builder
	* @param builder - Contains initialization details
	*/
	public RecruiterAPIInbound(RecruiterAPIInboundBuilder builder) {
		
		this.userId 			= builder.userId;
		this.firstName 			= builder.firstName;
		this.surname 			= builder.surname;
		this.email 				= builder.email;
		this.companyName 		= builder.companyName;
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
	public static RecruiterAPIInboundBuilder builder() {
		return new RecruiterAPIInboundBuilder();
	}
	
	/**
	* Builder for the Recruiter class 
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class RecruiterAPIInboundBuilder{
		
		private String 		userId;
		private String 		firstName;
		private String 		surname;
		private String 		email;
		private String		companyName;
		private language 	language;
		
		/**
		* Sets the userId associated with the Recruiter
		* @param userId - Unique userId for the Recruiter
		* @return Builder
		*/
		public RecruiterAPIInboundBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		/**
		* Sets the firstname of the Recruiter
		* @param firstName - Firstname of the Recruiter
		* @return Builder
		*/
		public RecruiterAPIInboundBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		* Sets the Surname of the Recruiter
		* @param surname - Surname of the Recruiter
		* @return Builder
		*/
		public RecruiterAPIInboundBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Email address for the Recruiter
		* @param email - Email address of the Recruiter
		* @return Builder
		*/
		public RecruiterAPIInboundBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the name of the Company the Recruiter works for 
		* @param companyName - Name of the Company the Recruiter works for
		* @return Builder
		*/
		public RecruiterAPIInboundBuilder companyName(String companyName) {
			this.companyName = companyName;
			return this;
		}
		
		/**
		* Sets the Language spoken by the Recruiter 
		* @param language - Language spoken by the Recruiter
		* @return Builder
		*/
		public RecruiterAPIInboundBuilder language(language language) {
			this.language = language;
			return this;
		}
		
		/**
		* Returns an instance of Recruiter initialized with the 
		* values in the builder
		* @return Initalized instance of Recruiter
		*/
		public RecruiterAPIInbound build() {
			return new RecruiterAPIInbound(this);
		}
		
	}
	
	/**
	* Converts an Inbound API representation of a Recruiter to a
	* Domain representation
	* @param recruiter - API Inbound representation
	* @return Domain representation
	*/
	public static Recruiter convertToDomain(RecruiterAPIInbound recruiter) {
		return Recruiter
				.builder()
					.companyName(recruiter.getCompanyName())
					.email(recruiter.getEmail())
					.firstName(recruiter.getFirstName())
					.language(recruiter.getLanguage())
					.surname(recruiter.getSurname())
					.userId(recruiter.userId)
				.build();
	}
	
}
