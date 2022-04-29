package com.arenella.recruit.candidates.beans;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Class represents a Recruitment Candidate. A Candidate that 
* can be place on a project
* @author K Parkings
*/
public class Candidate {

	public static final String ANONYMOUS_USER_ATTR_VALUE = "unknown";
			
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
	private boolean 		available;
	private boolean			flaggedAsUnavailable;
	private LocalDate 		registerd					= LocalDate.now();
	private LocalDate 		lastAvailabilityCheck		= LocalDate.now();	
	private Set<String> 	skills						= new LinkedHashSet<>();
	private Set<Language> 	languages					= new LinkedHashSet<>();
	
	/**
	* Constructor based upon a builder
	* @param builder = Contains initialization information
	*/
	public Candidate(CandidateBuilder builder) {
		
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
		this.available 					= builder.available;
		this.flaggedAsUnavailable		= builder.flaggedAsUnavailable;
		this.registerd 					= builder.registerd;
		this.lastAvailabilityCheck 		= builder.lastAvailabilityCheck;
	
		this.skills.addAll(builder.skills);
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
	* Returns whether the Candidate is currently available for work
	* @return Whether the Candidate is available for work
	*/
	public boolean isAvailable() {
		return this.available;
	}
	
	/**
	* Returns whether or not somebody has flagged the Candidate 
	* as no longer being available
	* @return if the Candidate is flagged as unavailable
	*/
	public boolean isFlaggedAsUnavailable() {
		return this.flaggedAsUnavailable;
	}
	
	/**
	* Returns the Date that the Candidate was registered in the System
	* @return Date the Candidate Registered
	*/
	public LocalDate getRegisteredOn() {
		return this.registerd;
	}
	
	/**
	* Returns the Date of the last time the Candidate was contacted to check 
	* that they were still available for a new role
	* @return Date of last availability check
	*/
	public LocalDate getLastAvailabilityCheckOn() {
		return this.lastAvailabilityCheck;
	}
	
	/**
	* Returns the Skills the candidate has experience with
	* @return candidates skills
	*/
	public Set<String> getSkills(){
		return this.skills;
	}
	
	/**
	* Returns the languages spoken by the Candidate
	* @return Languages the candidate can speak
	*/
	public Set<Language> getLanguages(){
		return this.languages;
	}
	
	/**
	* Manke the Candidates details anonymous and mark the candidate 
	* as no longer being available
	*/
	public void noLongerAvailable() {
		this.available 		= false;
		this.email 			= ANONYMOUS_USER_ATTR_VALUE;
		this.firstname 		= ANONYMOUS_USER_ATTR_VALUE;
		this.surname 		= ANONYMOUS_USER_ATTR_VALUE;
	}
	
	/**
	* Makes the candidate available for be searched on 
	*/
	public void makeAvailable() {
		this.available = true;
	}
	
	/**
	* Builder for the Candidate class
	* @return A Builder for the Candidate class
	*/
	public static final CandidateBuilder builder() {
		return new CandidateBuilder();
	}
	
	/**
	* Builder Class for the Candidate Class
	* @author K Parkings
	*/
	public static class CandidateBuilder {
		
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
		private boolean 		available;
		private boolean 		flaggedAsUnavailable;
		private LocalDate 		registerd					= LocalDate.now();;
		private LocalDate 		lastAvailabilityCheck		= LocalDate.now();;
		private Set<String> 	skills						= new LinkedHashSet<>();
		private Set<Language> 	languages					= new LinkedHashSet<>();
		
		/**
		* Sets the candidates Unique identifier in the System
		* @param candidateId - Unique identifier of the Candidate
		* @return Builder
		*/
		public CandidateBuilder candidateId(String candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets the First name of the Candidate
		* @param firstname - Candidates first name
		* @return Builder
		*/
		public CandidateBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the Surname of the Candidate
		* @param surname - Candidates Surname
		* @return Builder
		*/
		public CandidateBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Email address of the Candidate
		* @param email - Email address
		* @return Builder
		*/
		public CandidateBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the Role sought by the Candidate 
		* @param roleSought - Role the candidate is looking for
		* @return Builder
		*/
		public CandidateBuilder roleSought(String roleSought) {
			this.roleSought = roleSought;
			return this;
		}
		
		/**
		* Sets the function the candidate performs
		* @param function - Function performed by the Candidate
		* @return Builder
		*/
		public CandidateBuilder function(FUNCTION function) {
			this.function = function;
			return this;
		}
		
		/**
		* Returns the Country where the Candidate is located
		* @param country - Country where the Candidate is located
		* @return Builder
		*/
		public CandidateBuilder country(COUNTRY country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets the City where the Candidate is located
		* @param city - City where the Candidate is located
		* @return Builder
		*/
		public CandidateBuilder city(String city) {
			this.city = city;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for perm roles
		* @param freelance - Whether or not the Candidate is interested in perm roles
		* @return Builder
		*/
		public CandidateBuilder perm(PERM perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param freelance - Whether or not the Candidate is interested in freelance roles
		* @return Builder
		*/
		public CandidateBuilder freelance(FREELANCE freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Sets the number of years experience the Candidate has in the industry
		* @param yearsExperience - years of work experience in the industry
		* @return Builder
		*/
		public CandidateBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets whether or not the Candidate is currently available
		* @param available - whether or not the Candidate is currently looking for work
		* @return Builder
		*/
		public CandidateBuilder available(boolean available) {
			this.available = available;
			return this;
		}
		
		/**
		* Sets whether or not somebody has marked the candidate as being unavailable
		* @param flaggedAsUnavailable - If Candidate flagged as Unavailable
		* @return Builder
		*/
		public CandidateBuilder flaggedAsUnavailable(boolean flaggedAsUnavailable) {
			this.flaggedAsUnavailable = flaggedAsUnavailable;
			return this;
		}
		
		/**
		* Sets the Date the Candidate was registered in the System
		* @param registerd - Date of registration
		* @return Builder
		*/
		public CandidateBuilder registerd(LocalDate registerd) {
			this.registerd = registerd;
			return this;
		}
		
		/**
		* Sets the Date of the last time the Candidates availability 
		* was checked
		* @param lastAvailabilityCheck - Date of last availability check
		* @return Builder
		*/
		public CandidateBuilder lastAvailabilityCheck(LocalDate lastAvailabilityCheck) {
			this.lastAvailabilityCheck = lastAvailabilityCheck;
			return this;
		}
		
		/**
		* Sets the skills that the Candidate has
		* @param skills - Skills Candidate has experience with
		* @return Builder
		*/
		public CandidateBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Sets the languages spoken by the Candidate
		* @param languages - Languages spoken by the Candidate
		* @return Builder
		*/
		public CandidateBuilder languages(Set<Language> languages) {
			this.languages.clear();
			this.languages.addAll(languages);
			return this;
		}
		
		/**
		* Returns an instance of Candidate initialized with the 
		* values in the builder
		* @return Initialized instance of Candidate
		*/
		public Candidate build() {
			return new Candidate(this);
		}
	}
	
}