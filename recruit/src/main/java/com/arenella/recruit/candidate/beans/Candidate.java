package com.arenella.recruit.candidate.beans;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import com.arenella.recruit.candidate.entities.CandidateEntity;

/**
* Class represents a Recruitment Candidate. A Candidate that 
* can be place on a project
* @author K Parkings
*/
public class Candidate {

	public static enum COUNTRY 	{NETHERLANDS, BELGIUM, UK}
	public static enum FUNCTION {JAVA_DEV, CSHARP_DEV, SUPPORT, BA, UI_UX, PROJECT_MANAGER, SOFTWARE_ARCHITECT, SOLUTIONS_ARCHITECT, ENTERPRISE_ARCHITECT, TESTER, WEB_DEV}
	
	private String 			candidateId;
	private FUNCTION		function;
	private COUNTRY 		country;
	private String 			city;
	private boolean 		perm;
	private boolean 		freelance;
	private int				yearsExperience;
	private boolean 		available;
	private LocalDate 		registerd;
	private LocalDate 		lastAvailabilityCheck;
	private Set<String> 	skills						= new LinkedHashSet<>();
	private Set<Language> 	languages					= new LinkedHashSet<>();
	
	/**
	* Constructor based upon a builder
	* @param builder = Contains initialization information
	*/
	public Candidate(CandidateBuilder builder) {
		
		this.candidateId				= builder.candidateId;
		this.function				 	= builder.function;
		this.country					= builder.country;
		this.city 						= builder.city;
		this.perm 						= builder.perm;
		this.freelance 					= builder.freelance;
		this.yearsExperience 			= builder.yearsExperience;
		this.available 					= builder.available;
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
	public boolean isFreelance() {
		return this.freelance;
	}
	
	/**
	* Returns whether or not the Candidate is interested in permanent roles
	* @return Whether or not the Candidate is interested in permanent roles
	*/
	public boolean isPerm() {
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
	* @return Whether the Candidate is avilable for work
	*/
	public boolean isAvailable() {
		return this.available;
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
		private FUNCTION		function;
		private COUNTRY 		country;
		private String 			city;
		private boolean 		perm;
		private boolean 		freelance;
		private int				yearsExperience;
		private boolean 		available;
		private LocalDate 		registerd;
		private LocalDate 		lastAvailabilityCheck;
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
		public CandidateBuilder perm(boolean perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param freelance - Whether or not the Candidate is interested in freelance roles
		* @return Builder
		*/
		public CandidateBuilder freelance(boolean freelance) {
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
	
	/**
	* Converts a Domain representation of a Candidate Object to an Entity 
	* representation
	* @param candidate - Instance of Candidate to convert to an Entity
	* @return Entity representation of a Candidate object
	*/
	public static CandidateEntity convertToEntity(Candidate candidate) {
		
		return CandidateEntity
					.builder()
						.available(candidate.isAvailable())
						.candidateId(candidate.getCandidateId())
						.function(candidate.getFunction())
						.city(candidate.getCity())
						.country(candidate.getCountry())
						.freelance(candidate.isFreelance())
						.lastAvailabilityCheck(candidate.getLastAvailabilityCheckOn())
						.perm(candidate.isPerm())
						.registerd(candidate.getRegisteredOn())
						.yearsExperience(candidate.getYearsExperience())
						.skills(candidate.getSkills())
						.languages(candidate.getLanguages())
						.build();
		
	}

	/**
	* Converts a Entity representation of a Candidate Object to an Domain 
	* representation
	* @param candidate - Instance of Candidate to convert
	* @return Domain representation of a Candidate object
	*/
	public static Candidate convertFromEntity(CandidateEntity candidateEntity) {
		
		return Candidate
					.builder()
						.available(candidateEntity.isAvailable())
						.candidateId(candidateEntity.getCandidateId())
						.function(candidateEntity.getFunction())
						.city(candidateEntity.getCity())
						.country(candidateEntity.getCountry())
						.freelance(candidateEntity.isFreelance())
						.lastAvailabilityCheck(candidateEntity.getLastAvailabilityCheckOn())
						.perm(candidateEntity.isPerm())
						.registerd(candidateEntity.getRegisteredOn())
						.yearsExperience(candidateEntity.getYearsExperience())
						.skills(candidateEntity.getSkills())
						.languages(candidateEntity.getLanguages())
						.build();
		
	}
}