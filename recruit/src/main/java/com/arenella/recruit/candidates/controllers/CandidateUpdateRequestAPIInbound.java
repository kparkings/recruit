package com.arenella.recruit.candidates.controllers;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;
import com.arenella.recruit.candidates.beans.CandidateUpdateRequest;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Request to update a Canidates profile information
* @author K Parkings
*/
@JsonDeserialize(builder=CandidateUpdateRequestAPIInbound.CandidateUpdateRequestAPIInboundBuilder.class)
public class CandidateUpdateRequestAPIInbound {

	private String 			firstname;
	private String 			surname;
	private String 			email;
	private String			roleSought;
	private FUNCTION		function;
	private COUNTRY 		country;
	private String 			city;
	private PERM 			perm;
	private FREELANCE 		freelance;
	private int				yearsExperience;
	private Set<Language> 	languages					= new LinkedHashSet<>();
	private Rate			rate;
	private String			introduction;
	
	/**
	* Constructor based upon a builder
	* @param builder = Contains initialization information
	*/
	public CandidateUpdateRequestAPIInbound(CandidateUpdateRequestAPIInboundBuilder builder) {
		
		this.firstname					= builder.firstname;
		this.surname					= builder.surname;
		this.email						= builder.email;
		this.roleSought					= builder.roleSought;
		this.function				 	= builder.function;
		this.country					= builder.country;
		this.city 						= builder.city;
		this.perm 						= builder.perm;
		this.freelance 					= builder.freelance;
		this.yearsExperience 			= builder.yearsExperience;
		this.rate						= builder.rate;
		this.introduction				= builder.introduction;
		
		this.languages.addAll(builder.languages);
	
	}

	/***
	* Returns the name of the Candidate
	* @return Candidates name
	*/
	public String getFirstname() {
		return this.firstname;
	}
	
	/***
	* Returns the surname of the Candidate
	* @return Candidates name
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* Returns the Candidates Email address
	* @return Email address
	*/
	public String getEmail() {
		return this.email;
	}
	
	/**
	* Returns the title on the role the user is looking for. This can be a more 
	* specific description of the users role that provided by the function 
	* attribute
	* @return Role sought my the Candidate
	*/
	public String getRoleSought() {
		return this.roleSought;
	}
	
	/**
	* Returns the function the Candidate performs
	* @return function the Candidate performs
	*/
	public FUNCTION getFunction() {
		return this.function;
	}
	
	/**
	* Returns the Country in which the candidate is 
	* located
	* @return Country 
	*/
	public COUNTRY getCountry() {
		return this.country;
	}
	
	/**
	* Returns the City where the Candidate is located
	* @return Name of City where Candidate is located
	*/
	public String getCity() {
		return this.city;
	}
	
	/**
	* Returns whether or not the Candidate is looking for
	* freelance projects
	* @return Whether or not the Candidate is interested in freelance roles
	*/
	public FREELANCE isFreelance() {
		return this.freelance;
	}
	
	/**
	* Returns whether or not the Candidate is interested in permanent roles
	* @return Whether or not the Candidate is interested in permanent roles
	*/
	public PERM isPerm() {
		return this.perm;
	}
	
	/**
	* Returns the number of years experience the Candidate has in the industry
	* @return Number of years the Candidate has worked in the industry
	*/
	public int getYearsExperience() {
		return this.yearsExperience;
	}
	
	/**
	* Returns the languages spoken by the Candidate
	* @return Languages the candidate can speak
	*/
	public Set<Language> getLanguages(){
		return this.languages;
	}
	
	/**
	* Returns the Rate charged by the Candidate
	* @return Rate charged by the candidate
	*/
	public Optional<Rate> getRate() {
		return Optional.ofNullable(this.rate);
	}
	
	/**
	* Returns the Candidates introduction about themselves
	* @return Candidate introduction
	*/
	public String getIntroduction() {
		return this.introduction;
	}
	
	/**
	* Builder for the Candidate class
	* @return A Builder for the Candidate class
	*/
	public static final CandidateUpdateRequestAPIInboundBuilder builder() {
		return new CandidateUpdateRequestAPIInboundBuilder();
	}
	
	/**
	* Builder Class for the Candidate Class
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class CandidateUpdateRequestAPIInboundBuilder {
		
		private String 			firstname;
		private String			surname;
		private String 			email;
		private String			roleSought;
		private FUNCTION		function;
		private COUNTRY 		country;
		private String 			city;
		private PERM 			perm;
		private FREELANCE 		freelance;
		private int				yearsExperience;
		private Set<Language> 	languages					= new LinkedHashSet<>();
		private Rate			rate;
		private String			introduction;
		
		/**
		* Sets the First name of the Candidate
		* @param firstname - Candidates first name
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the Surname of the Candidate
		* @param surname - Candidates Surname
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Email address of the Candidate
		* @param email - Email address
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the Role sought by the Candidate 
		* @param roleSought - Role the candidate is looking for
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder roleSought(String roleSought) {
			this.roleSought = roleSought;
			return this;
		}
		
		/**
		* Sets the function the candidate performs
		* @param function - Function performed by the Candidate
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder function(FUNCTION function) {
			this.function = function;
			return this;
		}
		
		/**
		* Returns the Country where the Candidate is located
		* @param country - Country where the Candidate is located
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder country(COUNTRY country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets the City where the Candidate is located
		* @param city - City where the Candidate is located
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder city(String city) {
			this.city = city;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for perm roles
		* @param freelance - Whether or not the Candidate is interested in perm roles
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder perm(PERM perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param freelance - Whether or not the Candidate is interested in freelance roles
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder freelance(FREELANCE freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Sets the number of years experience the Candidate has in the industry
		* @param yearsExperience - years of work experience in the industry
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets the languages spoken by the Candidate
		* @param languages - Languages spoken by the Candidate
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder languages(Set<Language> languages) {
			this.languages.clear();
			this.languages.addAll(languages);
			return this;
		}
		
		/**
		* Sets the Rate charged by the Candidate
		* @param rate - Rate information
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder getRate(Rate rate) {
			this.rate = rate;
			return this;
		}
		
		/**
		* Sets the Candidates introduction about themselves
		* @param introduction - Introduction
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder introduction(String introduction) {
			this.introduction = introduction;
			return this;
		}
		
		/**
		* Returns an instance of Candidate initialized with the 
		* values in the builder
		* @return Initialized instance of Candidate
		*/
		public CandidateUpdateRequestAPIInbound build() {
			return new CandidateUpdateRequestAPIInbound(this);
		}
	}
	
	/**
	* Converts Request from API Inbound representation to Domain representation
	* @param candidateId	- Unique id of the Candidate
	* @param updateRequest	- To concert
	* @return Converted
	 * @throws IOException 
	*/
	public static  CandidateUpdateRequest convertToDomain(String candidateId, CandidateUpdateRequestAPIInbound updateRequest, Optional<MultipartFile> profileImage) throws IOException {
		
		com.arenella.recruit.candidates.beans.Candidate.Rate rate = null;
		
		if (updateRequest.getRate().isPresent()) {
		
			rate = new com.arenella.recruit.candidates.beans.Candidate.Rate(
					updateRequest.getRate().get().getCurrency(),
					updateRequest.getRate().get().getPeriod(),
					updateRequest.getRate().get().getValue());
			
		}
		
		return CandidateUpdateRequest
					.builder()
						.candidateId(candidateId)
						.city(updateRequest.getCity())
						.country(updateRequest.getCountry())
						.email(updateRequest.getEmail())
						.firstname(updateRequest.getFirstname())
						.freelance(updateRequest.isFreelance())
						.function(updateRequest.getFunction())
						.languages(updateRequest.getLanguages())
						.perm(updateRequest.isPerm())
						.roleSought(updateRequest.roleSought)
						.surname(updateRequest.getSurname())
						.yearsExperience(updateRequest.getYearsExperience())
						.introduction(updateRequest.getIntroduction())
						.rate(rate)
						.photoBytes(profileImage.isEmpty() ? null : profileImage.get().getBytes())
					.build();
	}
	
	/**
	* Class represents a Rate charged by the Candidate
	* for their work
	* @author K Parkings
	*/
	public static class Rate{
		
		private final CURRENCY 	currency;
		private final PERIOD 	period;
		private final float 	value;

		/**
		* Constructor
		* @param currency - Currency being charged
		* @param period   - Unit of charging
		* @param value	  - amount charged per unit
		*/
		public Rate(CURRENCY currency, PERIOD period, float value) {
			this.currency 	= currency;
			this.period 	= period;
			this.value 		= value;
		}
		
		/**
		* Return the currency being charged
		* @return currency
		*/
		public CURRENCY getCurrency() {
			return this.currency;
		}
		
		/**
		* Returns the unit of charging
		* @return period
		*/
		public PERIOD getPeriod() {
			return this.period;
		}
		
		/**
		* Returns amount charged per unit 
		* of charging
		* @return value
		*/
		public float getValue() {
			return this.value;
		}

	}
	
}
