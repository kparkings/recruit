package com.arenella.recruit.recruiters.beans;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.recruiters.beans.OfferedCandidate.DAYS_ON_SITE;
import com.arenella.recruit.recruiters.beans.OfferedCandidate.LANGUAGES;
import com.arenella.recruit.recruiters.beans.OpenPosition.ContractType;
import com.arenella.recruit.recruiters.beans.OpenPosition.Country;

/**
* Represents an Offered Candidate that needs to be placed and 
* is being offered to other Recruiters
* @author K Parkings
*/
public class OfferedCandidateAPIOutbound {

	private UUID 						id;
	private RecruiterDetails 			recruiter;
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
	private Set<LANGUAGES>				spokenLanguages			= new HashSet<>();
	private String 						comments;
	private LocalDate					created;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	private OfferedCandidateAPIOutbound(OfferedCandidateAPIOutboundBuilder builder) {
		
		this.id 					= builder.id;
		this.recruiter 				= builder.recruiter;
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
		this.created				= builder.created;
		
	}
	
	/**
	* Returns the unique Id of the offered candidate
	* @return Uinque identifier for the Candidate
	*/
	public UUID getId(){
		return this.id;
	}
	
	/**
	* Returns the unique Identifier of the Recruiter offering the Candidate
	* @return Unique Id of the Recruiter
	*/
	public RecruiterDetails getRecruiter(){
		return this.recruiter;
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
	* Returns when the Candidate is avaialble to start a new role
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
	public Set<LANGUAGES> getSpokenLanguages(){
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
	* Returns the date the OfferedCandidate was created
	* @return when the Offered Candidate was created
	*/
	public LocalDate getCreated() {
		return this.created;
	}
	
	/**
	* Returns a Builder for the Class
	* @return Builder for the Class
	*/
	public static OfferedCandidateAPIOutboundBuilder builder() {
		return new OfferedCandidateAPIOutboundBuilder();
	} 
	
	/**
	* Builder for the OfferedCandidateAPIOutbound class
	* @author K Parkings
	*/
	public static class OfferedCandidateAPIOutboundBuilder{
		
		private UUID 						id;
		private RecruiterDetails			recruiter;
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
		private Set<LANGUAGES>				spokenLanguages			= new HashSet<>();
		private String 						comments;
		private LocalDate					created;
		
		/**
		* Sets the unique Id of the offered Candidate
		* @param id - Unique Id of the offered Candidate
		* @return Builder
		*/
		public OfferedCandidateAPIOutboundBuilder id(UUID id) {
			
			this.id = id;
			
			return this;
		}
		
		/**
		* Sets details of the Recruiter offering the Candidate
		* @param recruiter - Details of Recruiter
		* @return Builder
		*/
		public OfferedCandidateAPIOutboundBuilder recruiterId(RecruiterDetails recruiter) {
			
			this.recruiter = recruiter;
			
			return this;
		}
		
		/**
		* Sets the Title of the Role the Candidate performs
		* @param candidateRoleTitle - Role performed by the Candidate
		* @return Builder
		*/
		public OfferedCandidateAPIOutboundBuilder candidateRoleTitle(String candidateRoleTitle) {
			
			this.candidateRoleTitle = candidateRoleTitle;
			
			return this;
		}
		
		/**
		* Sets the Country the Candidate is available for work in
		* @param country - Country Candidate is available in
		* @return Builder
		*/
		public OfferedCandidateAPIOutboundBuilder country(Country country) {
			
			this.country = country;
			
			return this;
		}
		
		/**
		* 
		* @param location
		* @return Builder
		*/
		public OfferedCandidateAPIOutboundBuilder location(String location) {
			
			this.location = location;
			
			return this;
		}
		
		/**
		* Sets the Type of contract the Candidate is looking for
		* @param contractType - Type of Contract the Candidate is looking for
		* @return Builder
		*/
		public OfferedCandidateAPIOutboundBuilder contractType(ContractType contractType) {
			
			this.contractType = contractType;
			
			return this;
		}
		
		/**
		* Sets the number of days the Candidate will work on site
		* @param daysOnSite - Days available for on site work
		* @return Builder
		*/
		public OfferedCandidateAPIOutboundBuilder daysOnSite(DAYS_ON_SITE daysOnSite) {
			
			this.daysOnSite = daysOnSite;
			
			return this;
		}
		
		/**
		* Sets the renumeration requested for the Candidate
		* @param renumeration - Renumeration requested by the recruiter for the Candidate
		* @return Builder
		*/
		public OfferedCandidateAPIOutboundBuilder renumeration(String renumeration) {
			
			this.renumeration = renumeration;
			
			return this;
		}
		
		/**
		* Sets the Date the Candidate is available from
		* @param availableFromDate - When the Candidate can begin
		* @return Builder
		*/
		public OfferedCandidateAPIOutboundBuilder availableFromDate(LocalDate availableFromDate) {
			
			this.availableFromDate = availableFromDate;
			
			return this;
		}
		
		/**
		* Sets the Candidates core skills
		* @param coreSkills - Score skills offered by the Candidate
		* @return Builder
		*/
		public OfferedCandidateAPIOutboundBuilder coreSkills(Set<String> coreSkills) {
			
			this.coreSkills.clear();
			this.coreSkills.addAll(coreSkills);
			
			return this;
		}
		
		/**
		* Sets the number of years experience the Candidate has
		* @param yearsExperience - Candidates number of years experience
		* @return Builder
		*/
		public OfferedCandidateAPIOutboundBuilder yearsExperience(int yearsExperience) {
			
			this.yearsExperience = yearsExperience;
			
			return this;
		}
		
		/**
		* Sets a description of the Candidate
		* @param description - Description of the Candidate
		* @return Builder
		*/
		public OfferedCandidateAPIOutboundBuilder description(String description) {
			
			this.description = description;
			
			return this;
		}

		/**
		* Sets the Date the OfferedCandidate was created
		* @param created - When the Offered Candidate was created
		* @return Builder
		*/
		public OfferedCandidateAPIOutboundBuilder created(LocalDate created) {
			
			this.created = created;
			
			return this;
		}

		/**
		* Sets languages spoken by the offered Candidate
		* @param spokenLanguages - Languages spoken by the Candidate
		* @return Builder
		*/
		public OfferedCandidateAPIOutboundBuilder spokenLanguages(Set<LANGUAGES>spokenLanguages) {
			
			this.spokenLanguages.clear();
			this.spokenLanguages.addAll(spokenLanguages);
			
			return this;
		}
		
		/**
		* Sets recruiters comments
		* @param comments - Additional comments from recruiter
		* @return Builder
		*/
		public OfferedCandidateAPIOutboundBuilder comments(String comments) {
			
			this.comments = comments;
			
			return this;
		}
		
		/**
		* Returns an instance of OfferedCandidateAPIOutbound initalized with 
		* the values in the Builder
		* @return Initialized instance of OfferedCandidateAPIOutbound
		*/
		public OfferedCandidateAPIOutbound build() {
			return new OfferedCandidateAPIOutbound(this);
		}
		
	}
	
	/**
	* Class represents Contact Details for a Recruiter offering a Candidate
	* @author K Parkings
	*/
	public static class RecruiterDetails{
		
		private final String recruiterId;
		private final String recruiterName;
		private final String companyName;
		
		/**
		* Constructor
		* @param recruiterId	- Unique id of the Recruiter
		* @param recruiterName	- Name of the Recruiter
		* @param companyName	- Name of the Company Recruiter works for
		*/
		public RecruiterDetails(String recruiterId, String recruiterName, String companyName) {
			this.recruiterId 	= recruiterId;
			this.recruiterName 	= recruiterName;
			this.companyName 	= companyName;
		}
		
		/**
		* Returns the Unique identifier of the Recruiter
		* @return id of Recruiter
		*/
		public String getRecruiterId() {
			return this.recruiterId;
		}
		
		/**
		* Returns the name of the Recruiter
		* @return Recruiters name
		*/
		public String getRecruiterName() {
			return this.recruiterName;
		}
		
		/**
		* Return the name of the Company the reruiter is 
		* working for
		* @return Company Name
		*/
		public String getCompanyName() {
			return this.companyName;
		}
		
	}
	
}