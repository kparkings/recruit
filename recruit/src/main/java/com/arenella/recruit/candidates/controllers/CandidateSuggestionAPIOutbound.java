package com.arenella.recruit.candidates.controllers;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import com.arenella.recruit.candidates.beans.Candidate.SECURITY_CLEARANCE_TYPE;
import com.arenella.recruit.candidates.beans.CandidateSearchAccuracyWrapper;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;

/**
* Outboud API representation of a Candidate which is related to some search critera. This representation
* of the object contains information relating to how accurate the Candidate is in relation to the 
* search having been carried out
* @author K Parkings
*/
public class CandidateSuggestionAPIOutbound implements CandidateAPIOutbound{

	public static final String CENSORED_ITEM = "";
	
	private String 					candidateId;
	private FUNCTION				function;
	private String					roleSought;
	private COUNTRY 				country;
	private String 					city;
	private PERM 					perm;
	private FREELANCE 				freelance;
	private int						yearsExperience;
	private boolean 				available;
	private LocalDate 				lastAvailabilityCheck;
	private boolean					flaggedAsUnavailable;
	private Set<String> 			skills						= new LinkedHashSet<>();
	private Set<Language> 			languages					= new LinkedHashSet<>();
	private String					firstname;
	private String					surname;
	private String					email;
	private suggestion_accuracy		accuracyLanguages;
	private suggestion_accuracy		accuracySkills;
	private SECURITY_CLEARANCE_TYPE securityClearance;
	private boolean					requiresSponsorship;

	/**
	* Constructor based upon a builder
	* @param builder = Contains initialization information
	*/
	public CandidateSuggestionAPIOutbound(CandidateAPIOutboundBuilder builder) {
		
		this.candidateId				= builder.candidateId;
		this.function				 	= builder.function;
		this.roleSought					= builder.roleSought;
		this.country					= builder.country;
		this.city 						= builder.city;
		this.perm	 					= builder.perm;
		this.freelance 					= builder.freelance;
		this.yearsExperience 			= builder.yearsExperience;
		this.available 					= builder.available;
		this.flaggedAsUnavailable		= builder.flaggedAsUnavailable;
		this.lastAvailabilityCheck 		= builder.lastAvailabilityCheck;
		this.firstname					= builder.firstname;
		this.surname					= builder.surname;
		this.email						= builder.email;
		this.accuracyLanguages			= builder.accuracyLanguages;
		this.accuracySkills				= builder.accuracySkills;
		this.securityClearance			= builder.securityClearance;
		this.requiresSponsorship		= builder.requiresSponsorship;
		
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
	* Returns the role the Candidate is looking for
	* @return role Sought by the Candidate
	*/
	public String getRoleSought() {
		return this.roleSought;
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
	public FREELANCE getFreelance() {
		return this.freelance;
	}
	
	/**
	* Returns whether or not the Candidate is interested in permanent roles
	* @return Whether or not the Candidate is interested in permanent roles
	*/
	public PERM getPerm() {
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
	* Returns whether the Candidate has been marked as being 
	* potentially unavailable
	* @return whether the Canidate has been flagged as unavailable
	*/
	public boolean isFlaggedAsUnavailable() {
		return this.flaggedAsUnavailable;
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
	* Returns the Candidates firstname
	* @return firstname of the Candidate
	*/
	public String getFirstname() {
		return this.firstname;
	}
	
	/**
	* Returns the Surname of the Candidate
	* @return Candidates Surname
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* Returns the email address of the Candidate
	* @return Candidates Email address
	*/
	public String getEmail() {
		return this.email;
	}
	
	/**
	* Returns the Candidates security clearance level
	* @return Security clearance level
	*/
	public SECURITY_CLEARANCE_TYPE getSecurityClearance() {
		return this.securityClearance;
	}	
	
	/**
	* Returns whether the Candidate requires sponsorship to 
	* take on a new role
	* @return Whether the Candidate requires sponsorship
	*/
	public boolean getRequiresSponsorship() {
		return this.requiresSponsorship;
	}
	
	/**
	* Returns how accurate the languages possessed by the 
	* Candidate are in relation to the search criteria
	* used to get suggested candidates
	* @return accuracy of the languages
	*/
	public suggestion_accuracy getAccuracyLanguages() {
		return this.accuracyLanguages;
	}
	
	/**
	* Returns how accurate the skills possessed by the 
	* Candidate are in relation to the search criteria
	* used to get suggested candidates
	* @return accuracy of the skills
	*/
	public suggestion_accuracy getAccuracySkills() {
		return this.accuracySkills;
	}
	
	/**
	* Builder for the Candidate class
	* @return A Builder for the CandidateAPIOutbound class
	*/
	public static final CandidateAPIOutboundBuilder builder() {
		return new CandidateAPIOutboundBuilder();
	}
	
	/**
	* Builder Class for the CandidateAPIOutbound Class
	* @author K Parkings
	*/
	public static class CandidateAPIOutboundBuilder {
		
		private String 					candidateId;
		private FUNCTION				function;
		private String					roleSought;
		private COUNTRY 				country;
		private String 					city;
		private PERM 					perm;
		private FREELANCE 				freelance;
		private int						yearsExperience;
		private boolean 				available;
		private boolean					flaggedAsUnavailable;
		private LocalDate 				lastAvailabilityCheck;
		private Set<String> 			skills						= new LinkedHashSet<>();
		private Set<Language> 			languages					= new LinkedHashSet<>();
		private String					firstname;
		private String					surname;
		private String					email;
		private suggestion_accuracy		accuracyLanguages;
		private suggestion_accuracy		accuracySkills;
		private SECURITY_CLEARANCE_TYPE securityClearance;
		private boolean					requiresSponsorship;
		
		/**
		* Sets the candidates Unique identifier in the System
		* @param candidateId - Unique identifier of the Candidate
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder candidateId(String candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets the function the candidate performs
		* @param function - Function performed by the Candidate
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder function(FUNCTION function) {
			this.function = function;
			return this;
		}
		
		/**
		* Sets the role sought by the Candidate
		* @param roleSought - Role the Candidate is looking for
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder roleSought(String roleSought) {
			this.roleSought = roleSought;
			return this;
		}
		
		/**
		* Returns the Country where the Candidate is located
		* @param country - Country where the Candidate is located
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder country(COUNTRY country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets the City where the Candidate is located
		* @param city - City where the Candidate is located
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder city(String city) {
			this.city = city;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for perm roles
		* @param freelance - Whether or not the Candidate is interested in perm roles
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder perm(PERM perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param freelance - Whether or not the Candidate is interested in freelance roles
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder freelance(FREELANCE freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Sets the number of years experience the Candidate has in the industry
		* @param yearsExperience - years of work experience in the industry
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets whether or not the Candidate is currently available
		* @param available - whether or not the Candidate is currently looking for work
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder available(boolean available) {
			this.available = available;
			return this;
		}
		
		/**
		* Sets whether or not the Candidate has been marked as being 
		* unavailable
		* @param flaggedAsUnavailable - Whether the Candidate has been flagged as unavialble
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder flaggedAsUnavailable(boolean flaggedAsUnavailable) {
			this.flaggedAsUnavailable = flaggedAsUnavailable;
			return this;
		}
		
		/**
		* Sets the Date of the last time the Candidates availability 
		* was checked
		* @param lastAvailabilityCheck - Date of last availability check
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder lastAvailabilityCheck(LocalDate lastAvailabilityCheck) {
			this.lastAvailabilityCheck = lastAvailabilityCheck;
			return this;
		}
		
		/**
		* Sets the skills that the Candidate has
		* @param skills - Skills Candidate has experience with
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Sets the languages spoken by the Candidate
		* @param languages - Languages spoken by the Candidate
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder languages(Set<Language> languages) {
			this.languages.clear();
			this.languages.addAll(languages);
			return this;
		}
		
		/**
		* Sets the Candidates firstname
		* @param firstname - Firstname of the Candidate
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the Candidates surname
		* @param surname - surname of the Candidate
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Candidates email address
		* @param email - Email address of the Candidate
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the Security clearance level held by the Candidate
		* @param securityClearance - Level of security clearance
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder securityClearance(SECURITY_CLEARANCE_TYPE securityClearance) {
			this.securityClearance = securityClearance;
			return this;
		}
		
		/**
		* Sets whether the Candidate requires sponsorship to take 
		* on a new role
		* @param requiresSponsorship - Whether the Candidate requires sponsorship
		* @return Builder
		*/
		public CandidateAPIOutboundBuilder requiresSponsorship(boolean requiresSponsorship) {
			this.requiresSponsorship = requiresSponsorship;
			return this;
		}
		
		/**
		* Sets how accurate the languages match that of the Suggestion Search criteria that was 
		* performed
		* @param accuracyLanguages - How accurate the Candidates language skills are in relation to those required
		* @return Builder
		*/
		public  CandidateAPIOutboundBuilder accuracyLanguages(suggestion_accuracy accuracyLanguages) {
			this.accuracyLanguages = accuracyLanguages;
			return this;
		}
		
		/**
		* Sets how accurate the Skills match that of the Suggestion Search criteria that was 
		* performed
		* @param accuracySkills
		* @return
		*/
		public  CandidateAPIOutboundBuilder accuracySkills(suggestion_accuracy accuracySkills) {
			this.accuracySkills = accuracySkills;
			return this;
		}
		
		/**
		* Returns an instance of Candidate initialized with the 
		* values in the builder
		* @return Initialized instance of Candidate
		*/
		public CandidateSuggestionAPIOutbound build() {
			return new CandidateSuggestionAPIOutbound(this);
		}
	}
	
	/**
	* Converts an incoming API representation of a Candidate to the 
	* Domain version
	* @param candiateAPIInbound - API Incoming version of the Candidate
	* @return Domain version of the Candidate
	*/
	public static CandidateSuggestionAPIOutbound convertFromCandidate(CandidateSearchAccuracyWrapper candidate) {
		
		return CandidateSuggestionAPIOutbound
				.builder()
					.candidateId(candidate.get().getCandidateId())
					.country(candidate.get().getCountry())
					.function(candidate.get().getFunction())
					.roleSought(candidate.get().getRoleSought())
					.city(candidate.get().getCity())
					.freelance(candidate.get().isFreelance())
					.perm(candidate.get().isPerm())
					.languages(candidate.get().getLanguages())
					//.skills(candidate.get().getSkills())
					.yearsExperience(candidate.get().getYearsExperience())
					.available(candidate.get().isAvailable())
					.flaggedAsUnavailable(candidate.get().isFlaggedAsUnavailable())
					.lastAvailabilityCheck(candidate.get().getLastAvailabilityCheckOn())
					.firstname(candidate.get().getFirstname())
					.surname(candidate.get().getSurname())
					.email(candidate.get().getEmail())
					.accuracyLanguages(candidate.getAccuracyLanguages())
					.accuracySkills(candidate.getAccuracySkills())
					.requiresSponsorship(candidate.get().getRequiresSponsorship())
					.securityClearance(candidate.get().getSecurityClearance())
				.build();
		
	}
	
	/**
	* Returns results with identifying features of the Candidate removed
	* @param candidate - Candidate to be converted
	* @return converted candidate
	*/
	public static CandidateSuggestionAPIOutbound convertFromCandidateAsCensored(CandidateSearchAccuracyWrapper candidate) {
		
		return CandidateSuggestionAPIOutbound
				.builder()
					.candidateId(candidate.get().getCandidateId())
					.country(candidate.get().getCountry())
					.function(candidate.get().getFunction())
					.roleSought(candidate.get().getRoleSought())
					.city(candidate.get().getCity())
					.freelance(candidate.get().isFreelance())
					.perm(candidate.get().isPerm())
					.languages(candidate.get().getLanguages())
					//.skills(candidate.get().getSkills())
					.yearsExperience(candidate.get().getYearsExperience())
					.available(candidate.get().isAvailable())
					.flaggedAsUnavailable(candidate.get().isFlaggedAsUnavailable())
					.lastAvailabilityCheck(candidate.get().getLastAvailabilityCheckOn())
					.firstname(candidate.get().getFirstname())
					.surname(CENSORED_ITEM)
					.email(CENSORED_ITEM)
					.accuracyLanguages(candidate.getAccuracyLanguages())
					.accuracySkills(candidate.getAccuracySkills())
					.requiresSponsorship(candidate.get().getRequiresSponsorship())
					.securityClearance(candidate.get().getSecurityClearance())
				.build();
		
	}
	
	/**
	* Returns results with identifying features of the Candidate removed. In this case we just want to 
	* hide the surname so the recruiter can't find them on other sites and avoid paying the subscription fee
	* @param candidate - Candidate to be converted
	* @return converted candidate
	*/
	public static CandidateSuggestionAPIOutbound convertFromCandidateAsCensoredForActiveCredits(CandidateSearchAccuracyWrapper candidate) {
		
		return CandidateSuggestionAPIOutbound
				.builder()
					.candidateId(candidate.get().getCandidateId())
					.country(candidate.get().getCountry())
					.function(candidate.get().getFunction())
					.roleSought(candidate.get().getRoleSought())
					.city(candidate.get().getCity())
					.freelance(candidate.get().isFreelance())
					.perm(candidate.get().isPerm())
					.languages(candidate.get().getLanguages())
					//.skills(candidate.get().getSkills())
					.yearsExperience(candidate.get().getYearsExperience())
					.available(candidate.get().isAvailable())
					.flaggedAsUnavailable(candidate.get().isFlaggedAsUnavailable())
					.lastAvailabilityCheck(candidate.get().getLastAvailabilityCheckOn())
					.firstname(candidate.get().getFirstname())
					.surname(" ")
					.email(candidate.get().getEmail())
					.accuracyLanguages(candidate.getAccuracyLanguages())
					.accuracySkills(candidate.getAccuracySkills())
					.requiresSponsorship(candidate.get().getRequiresSponsorship())
					.securityClearance(candidate.get().getSecurityClearance())
				.build();
		
	}
	
}