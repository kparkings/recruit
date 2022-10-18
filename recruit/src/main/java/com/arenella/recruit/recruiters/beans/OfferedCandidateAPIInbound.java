package com.arenella.recruit.recruiters.beans;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.arenella.recruit.recruiters.beans.OfferedCandidate.DAYS_ON_SITE;
import com.arenella.recruit.recruiters.beans.OfferedCandidate.LANGUAGE;
import com.arenella.recruit.recruiters.beans.OpenPosition.ContractType;
import com.arenella.recruit.recruiters.beans.OpenPosition.Country;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Represents an incoming request from a Recruiter to 
* create a new Offered Candidate that needs to be placed and 
* is being offered to other Recruiters
* @author K Parkings
*/
@JsonDeserialize(builder=OfferedCandidateAPIInbound.OfferedCandidateAPIInboundBuilder.class)
public class OfferedCandidateAPIInbound {
	
	private String 						recruiterId;
	private String 						candidateRoleTitle;
	private Country	 					country;
	private String						location;
	private ContractType 				contractType;
	private DAYS_ON_SITE				daysOnSite;
	private String	 					renumeration;
	private LocalDate 					availableFromDate;
	private Set<String>					coreSkills				= new HashSet<>();
	private int							yearsExperience;
	private String 						description;
	private Set<LANGUAGE>				spokenLanguages			= new HashSet<>();
	private String 						comments;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	private OfferedCandidateAPIInbound(OfferedCandidateAPIInboundBuilder builder) {
		
		this.recruiterId 			= builder.recruiterId; //?? include contact details or via internal messages
		this.candidateRoleTitle 	= builder.candidateRoleTitle;
		this.country 				= builder.country;
		this.location 				= builder.location;
		this.contractType 			= builder.contractType;
		this.daysOnSite				= builder.daysOnSite;
		this.renumeration 			= builder.renumeration;
		this.availableFromDate 		= builder.availableFromDate;
		this.coreSkills 			= builder.coreSkills;
		this.yearsExperience 		= builder.yearsExperience;
		this.description 			= builder.description;
		this.spokenLanguages 		= builder.spokenLanguages;
		this.comments 				= builder.comments;
		
	}
	
	/**
	* Returns the unique Identifier of the Recruiter offering the Candidate
	* @return Unique Id of the Recruiter
	*/
	public String getRecruiterId(){
		return this.recruiterId;
	}
	
	/**
	* Returns the Job title of the offered Candidate
	* @return Role Candidate can perform
	*/
	public String getCandidateRoleTitle(){
		return this.candidateRoleTitle;
	}
	
	/**
	* Returns the Country the Candidate is available to work in
	* @return Country - Country where the Candidate is available to work
	*/
	public Country getCountry(){
		return this.country;
	}
	
	/**
	* Returns the location ( city / region ) where the Candidate is avaialbel 
	* for work
	* @return Location where the Candidate can work
	*/
	public String getLocation(){
		return this.location;
	}
	
	/**
	* Return the type of Contract the Candidate is looking for
	* @return Type of Contract being sought
	*/
	public ContractType getContractType(){
		return this.contractType;
	}
	
	/**
	* Returns the number of days the candidate is willing to work on site
	* @return Number of days on site
	*/
	public DAYS_ON_SITE getDaysOnSite() {
		return this.daysOnSite;
	}
	
	/**
	* Returns the renumeration requested for the Candidate.
	* @return renumeration requested for the candidate
	*/
	public String getRenumeration(){
		return this.renumeration;
	}
	
	/**
	* Returns when the Candidate is available to start a new role
	* @return First availability of the Candidate
	*/
	public LocalDate getAvailableFromDate(){
		return this.availableFromDate;
	}
	
	/**
	* Returns the core skills posessed by the Canddate
	* @return Candidates core skills
	*/
	public Set<String> getCoreSkills(){
		return this.coreSkills;
	}
	
	/**
	* Returns the number of years experience the Candidate has
	* @return years experience
	*/
	public int getYearsExperience(){
		return this.yearsExperience;
	}
	
	/**
	* Returns a short description of the Candidate being offered
	* @return description of the Candidate
	*/
	public String getDescription(){
		return this.description;
	}
	
	/**
	* Returns the languages spoken by the Candidate
	* @return candidate spoken languages
	*/
	public Set<LANGUAGE> getSpokenLanguages(){
		return this.spokenLanguages;
	}
	
	/**
	* Returns additional comments from the recruiter offering the Candidate
	* @return additional comments from the Recruiter
	*/
	public String getComments(){
		return this.comments;
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder for the Class
	*/
	public static OfferedCandidateAPIInboundBuilder builder() {
		return new OfferedCandidateAPIInboundBuilder();
	} 
	
	/**
	* Builder for the OfferedCandidateAPIInbound class
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class OfferedCandidateAPIInboundBuilder{
		
		private String 						recruiterId;
		private String 						candidateRoleTitle;
		private Country	 					country;
		private String						location;
		private ContractType 				contractType;
		private DAYS_ON_SITE				daysOnSite;
		private String	 					renumeration;
		private LocalDate 					availableFromDate;
		private Set<String>					coreSkills				= new HashSet<>();
		private int							yearsExperience;
		private String 						description;
		private Set<LANGUAGE>				spokenLanguages			= new HashSet<>();
		private String 						comments;
		
		/**
		* Sets the Unique Id of the Recruiter offering the Candidate
		* @param recruiterId - Unique Id of the Candidate
		* @return Builder
		*/
		public OfferedCandidateAPIInboundBuilder recruiterId(String recruiterId) {
			
			this.recruiterId = recruiterId;
			
			return this;
		}
		
		/**
		* Sets the Title of the Role the Candidate performs
		* @param candidateRoleTitle - Role performed by the Candidate
		* @return Builder
		*/
		public OfferedCandidateAPIInboundBuilder candidateRoleTitle(String candidateRoleTitle) {
			
			this.candidateRoleTitle = candidateRoleTitle;
			
			return this;
		}
		
		/**
		* Sets the Country the Candidate is available for work in
		* @param country - Country Candidate is available in
		* @return Builder
		*/
		public OfferedCandidateAPIInboundBuilder country(Country country) {
			
			this.country = country;
			
			return this;
		}
		
		/**
		* 
		* @param location
		* @return Builder
		*/
		public OfferedCandidateAPIInboundBuilder location(String location) {
			
			this.location = location;
			
			return this;
		}
		
		/**
		* Sets the Type of contract the Candidate is looking for
		* @param contractType - Type of Contract the Candidate is looking for
		* @return Builder
		*/
		public OfferedCandidateAPIInboundBuilder contractType(ContractType contractType) {
			
			this.contractType = contractType;
			
			return this;
		}
		
		/**
		* Sets the number of days the Candidate will work on site
		* @param daysOnSite - Days available for on site work
		* @return Builder
		*/
		public OfferedCandidateAPIInboundBuilder daysOnSite(DAYS_ON_SITE daysOnSite) {
			
			this.daysOnSite = daysOnSite;
			
			return this;
		}
		
		/**
		* Sets the renumeration requested for the Candidate
		* @param renumeration - Renumeration requested by the recruiter for the Candidate
		* @return Builder
		*/
		public OfferedCandidateAPIInboundBuilder renumeration(String renumeration) {
			
			this.renumeration = renumeration;
			
			return this;
		}
		
		/**
		* Sets the Date the Candidate is avaialble from
		* @param availableFromDate - When the Candidate can begin
		* @return Builder
		*/
		public OfferedCandidateAPIInboundBuilder availableFromDate(LocalDate availableFromDate) {
			
			this.availableFromDate = availableFromDate;
			
			return this;
		}
		
		/**
		* Sets the Candidates core skills
		* @param coreSkills - Score skills offered by the Candidate
		* @return Builder
		*/
		public OfferedCandidateAPIInboundBuilder coreSkills(Set<String> coreSkills) {
			
			this.coreSkills.clear();
			this.coreSkills.addAll(coreSkills);
			
			return this;
		}
		
		/**
		* Sets the number of years expereince the Candidate has
		* @param yearsExperience - Candidates number of years expereince
		* @return Builder
		*/
		public OfferedCandidateAPIInboundBuilder yearsExperience(int yearsExperience) {
			
			this.yearsExperience = yearsExperience;
			
			return this;
		}
		
		/**
		* Sets a description of the Candidate
		* @param description - Description of the Candidate
		* @return Builder
		*/
		public OfferedCandidateAPIInboundBuilder description(String description) {
			
			this.description = description;
			
			return this;
		}
		
		/**
		* Sets languages spoken by the offered Candidate
		* @param spokenLanguages - Languages spoken by the Candidate
		* @return Builder
		*/
		public OfferedCandidateAPIInboundBuilder spokenLanguages(Set<LANGUAGE>spokenLanguages) {
			
			this.spokenLanguages.clear();
			this.spokenLanguages.addAll(spokenLanguages);
			
			return this;
		}
		
		/**
		* Sets recruiters comments
		* @param comments - Additional comments from recruiter
		* @return Builder
		*/
		public OfferedCandidateAPIInboundBuilder comments(String comments) {
			
			this.comments = comments;
			
			return this;
		}
		
		/**
		* Returns an instance of OfferedCandidateAPIInbound initalized with 
		* the values in the Builder
		* @return Initialzied instance of OfferedCandidateAPIInbound
		*/
		public OfferedCandidateAPIInbound build() {
			return new OfferedCandidateAPIInbound(this);
		}
		
	}

	/**
	* Converts the Incoming API version of the OfferedCandidate to a Domain version
	* @param offeredCandidate
	* @return
	*/
	public static OfferedCandidate convertToDomain(OfferedCandidateAPIInbound offeredCandidate) {
		
		return OfferedCandidate
				.builder()
					.availableFromDate(offeredCandidate.getAvailableFromDate())
					.candidateRoleTitle(offeredCandidate.getCandidateRoleTitle())
					.comments(offeredCandidate.getComments())
					.contractType(offeredCandidate.getContractType())
					.coreSkills(offeredCandidate.getCoreSkills())
					.country(offeredCandidate.getCountry())
					.daysOnSite(offeredCandidate.getDaysOnSite())
					.description(offeredCandidate.getDescription())
					.location(offeredCandidate.getLocation())
					.recruiterId(offeredCandidate.getRecruiterId())
					.renumeration(offeredCandidate.getRenumeration())
					.spokenLanguages(offeredCandidate.getSpokenLanguages())
					.yearsExperience(offeredCandidate.getYearsExperience())
				.build();
	
	}

}