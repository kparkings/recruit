package com.arenella.recruit.candidates.beans;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import com.arenella.recruit.candidates.beans.Candidate.DAYS_ON_SITE;
import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Request to update an existing Candidate
* @author K Parkings
*/
public class CandidateUpdateRequest {

	private String 			candidateId;
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
	private String			introduction;
	private byte[]			photoBytes;
	private Set<String>		skills						= new LinkedHashSet<>();
	private String 			comments;
	private DAYS_ON_SITE	daysOnSite;
	private Rate			rateContract;
	private Rate			ratePerm;
	private LocalDate 		availableFromDate;
	
	/**
	* Constructor based upon a builder
	* @param builder = Contains initialization information
	*/
	public CandidateUpdateRequest(CandidateUpdateRequestBuilder builder) {
		
		this.candidateId				= builder.candidateId;
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
		this.photoBytes					= builder.photoBytes;
		this.skills						= builder.skills;
		this.comments					= builder.comments;
		this.introduction				= builder.introduction;
		this.daysOnSite					= builder.daysOnSite;
		this.rateContract				= builder.rateContract;
		this.ratePerm					= builder.ratePerm;
		this.availableFromDate			= builder.availableFromDate;
		this.languages.addAll(builder.languages);
		
	
	}
	
	/**
	* Returns the unique identifier of the Candidate
	* @return unique Id of the candidate
	*/
	public String getCandidateId() {
		return this.candidateId;
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
	* Returns updates set of Candidates skills
	* @return skills
	*/
	public Set<String> getSkills(){
		return this.skills;
	}
	
	/**
	* If available Returns the Profile Photo for
	* the Candidate
	* @return
	*/
	public Optional<byte[]> getPhotoBytes(){
		return Optional.ofNullable(this.photoBytes);
	}
	
	/**
	* Returns comments relating to the Candidate
	* @return Comment about the candidate
	*/
	public String getComments() {
		return this.comments;
	}
	
	/**
	* Returns the introduction to the Candidate
	* @return candidate introduction text
	*/
	public String getIntroduction() {
		return this.introduction;
	}
	
	/**
	* Returns the max number of days the candidate is prepared
	* to work onsite
	* @return
	*/
	public DAYS_ON_SITE	getDaysOnSite() {
		return this.daysOnSite;
	}
	
	/**
	* If available the contract rate the Candidate is 
	* looking for
	* @return min contract Rate
	*/
	public Optional<Rate> getRateContract(){
		return Optional.ofNullable(this.rateContract);
	}
	
	/**
	* If available the salary the Candidate is 
	* looking for
	* @return max salary
	*/
	public Optional<Rate> getRatePerm(){
		return Optional.ofNullable(this.ratePerm);
	}
	
	/**
	* returns the date the Candidate is available from. If not specified 
	* uses the current date
	* @return When the candidate is available from
	*/
	public LocalDate getAvailableFromDate(){
		return Optional.ofNullable(this.availableFromDate).orElse(LocalDate.now());
	}
	
	/**
	* Builder for the Candidate class
	* @return A Builder for the Candidate class
	*/
	public static final CandidateUpdateRequestBuilder builder() {
		return new CandidateUpdateRequestBuilder();
	}
	
	/**
	* Builder Class for the Candidate Class
	* @author K Parkings
	*/
	public static class CandidateUpdateRequestBuilder {
		
		private String 			candidateId;
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
		private String			introduction;
		private byte[]			photoBytes;
		private Set<String> 	skills					= new LinkedHashSet<>();
		private String 			comments;
		private DAYS_ON_SITE	daysOnSite;
		private Rate			rateContract;
		private Rate			ratePerm;
		private LocalDate 		availableFromDate;
		
		/**
		* Sets the candidates Unique identifier in the System
		* @param candidateId - Unique identifier of the Candidate
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder candidateId(String candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets the First name of the Candidate
		* @param firstname - Candidates first name
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the Surname of the Candidate
		* @param surname - Candidates Surname
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Email address of the Candidate
		* @param email - Email address
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the Role sought by the Candidate 
		* @param roleSought - Role the candidate is looking for
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder roleSought(String roleSought) {
			this.roleSought = roleSought;
			return this;
		}
		
		/**
		* Sets the function the candidate performs
		* @param function - Function performed by the Candidate
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder function(FUNCTION function) {
			this.function = function;
			return this;
		}
		
		/**
		* Returns the Country where the Candidate is located
		* @param country - Country where the Candidate is located
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder country(COUNTRY country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets the City where the Candidate is located
		* @param city - City where the Candidate is located
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder city(String city) {
			this.city = city;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for perm roles
		* @param freelance - Whether or not the Candidate is interested in perm roles
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder perm(PERM perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param freelance - Whether or not the Candidate is interested in freelance roles
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder freelance(FREELANCE freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Sets the number of years experience the Candidate has in the industry
		* @param yearsExperience - years of work experience in the industry
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets the languages spoken by the Candidate
		* @param languages - Languages spoken by the Candidate
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder languages(Set<Language> languages) {
			this.languages.clear();
			this.languages.addAll(languages);
			return this;
		}
		
		/**
		* Sets the skills the Candidate has
		* @param skills - Candidates skills
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Sets the Candidates introduction about themselves
		* @param introduction - Candidate introduction
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder introduction(String introduction) {
			this.introduction = introduction;
			return this;
		}
		
		/**
		* Sets the Candidates profile Photo
		* @param photo - Candidates photo
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder photoBytes(byte[] photoBytes) {
			this.photoBytes = photoBytes;
			return this;
		}
		
		/**
		* Sets comments relating to the Candidate
		* @param comments - additional notes/comments
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder comments(String comments) {
			this.comments = comments;
			return this;
		}
		
		/**
		* Sets the max number of days the Candidate is prepared to work onsite
		* @param daysOnSite - Max number of days onsite
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder daysOnSite(DAYS_ON_SITE daysOnSite) {
			this.daysOnSite = daysOnSite;
			return this;
		}
		
		/**
		* Sets the contract rate the Candidate will accept
		* @param rateContract - rate
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder rateContract(Rate rateContract) {
			this.rateContract = rateContract;
			return this;
		}
		
		/**
		* Sets the perm salary the Candidate is looking for
		* @param rateToPerm - salary
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder ratePerm(Rate ratePerm) {
			this.ratePerm = ratePerm;
			return this;
		}
		
		/**
		* Sets the Date the Candidate is available from
		* @param availableFromDate - When the Candidate will be available from
		* @return Builder
		*/
		public CandidateUpdateRequestBuilder availableFromDate(LocalDate availableFromDate) {
			this.availableFromDate = availableFromDate;
			return this;
		}
		
		/**
		* Returns an instance of Candidate initialized with the 
		* values in the builder
		* @return Initialized instance of Candidate
		*/
		public CandidateUpdateRequest build() {
			return new CandidateUpdateRequest(this);
		}
	}
	
}