package com.arenella.recruit.candidates.beans;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Class represents an Alert. An alert is the definition of a predefined search criteria 
* that a Recruiter wants to receive an alert for when a Candidate is uploaded that matched
* the specification of the Alert
* @author K Parkings
*/
public class CandidateSearchAlert {

	private UUID 				alertId;
	private String 				recruiterId;
	private String 				alertName;
	private Set<COUNTRY> 		countries							= new HashSet<>();
	private Set<FUNCTION> 		functions							= new HashSet<>();
	private Optional<Boolean> 	freelance							= Optional.empty();
	private Optional<Boolean> 	perm								= Optional.empty();
	private int 				yearsExperienceGtEq;
	private int					yearsExperienceLtEq;
	private Language.LEVEL 		dutch;
	private Language.LEVEL 		english;
	private Language.LEVEL 		french;
	private Set<String>			skills								= new HashSet<>();
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public CandidateSearchAlert(CandidateSearchAlertBuilder builder) {
		
		this.alertId 				= builder.alertId;
		this.recruiterId 			= builder.recruiterId;
		this.alertName 				= builder.alertName;
		this.countries 				= builder.countries;
		this.functions 				= builder.functions;
		this.freelance 				= builder.freelance;
		this.perm 					= builder.perm;
		this.yearsExperienceGtEq 	= builder.yearsExperienceGtEq;
		this.yearsExperienceLtEq 	= builder.yearsExperienceLtEq;
		this.dutch 					= builder.dutch;
		this.english 				= builder.english;
		this.french 				= builder.french;
		this.skills					= builder.skills;
		
	}
	
	/**
	* Initializes a new Action 
	* @param recruiterId - Owner of the Alert
	*/
	public void initAsNewAlert(String recruiterId) {
		
		if (this.recruiterId != null) {
			throw new IllegalStateException();
		}
		
		if (this.alertId != null) {
			throw new IllegalStateException();
		}
			
		this.recruiterId 	= recruiterId;
		this.alertId 		= UUID.randomUUID();
	}
	
	/**
	* Replaces any existing Functions with new 
	* set of Functions
	* @param functions - Functions to filter on
	*/
	public void setFunctions(Set<FUNCTION> functions) {
		this.functions.clear();
		this.functions.addAll(functions);
	}
	
	/**
	* Returns the unique id of the Alert
	* @return id of the Alert
	*/
	public UUID getAlertId() {
		return this.alertId;
	}
	
	/**
	* Returns the unique id of the Recruiter
	* @return owner of the Alert
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}
	
	/**
	* Returns the display name for the Alert
	* @return name of the Alter
	*/
	public String getAlertName() {
		return this.alertName;
	}
	
	/**
	* Returns the countries to filter on
	* @return Countries to filter on
	*/
	public Set<COUNTRY> getCountries() {
		return this.countries;
	}
	
	/**
	* Returns the functions to filter on
	* @return functions to filter on
	*/
	public Set<FUNCTION> getFunctions() {
		return this.functions;
	}
	
	/**
	* Returns whether to include freelance candidate
	* @return whether to include freelance candidates
	*/
	public Optional<Boolean> getFreelance() {
		return this.freelance;
	}
	
	/**
	* Returns whether to include perm candidate
	* @return whether to include perm candidates
	*/
	public Optional<Boolean> getPerm() {
		return this.perm;
	}
	
	/**
	* Returns minimum number of years experience to filter on
	* @return minimum years experience
	*/
	public int getYearsExperienceGtEq() {
		return this.yearsExperienceGtEq;
	}
	
	/**
	* Returns maximum number of years experience to filter on
	* @return maximum years experience
	*/
	public int getyearsExperienceLtEq() {
		return this.yearsExperienceLtEq;
	}
	
	/**
	* Returns Dutch language filter options
	* @return Dutch language filter options
	*/
	public Language.LEVEL getDutch() {
		return this.dutch;
	}
	
	/**
	* Returns English language filter options
	* @return English language filter options
	*/
	public Language.LEVEL getEnglish() {
		return this.english;
	}
	
	/**
	* Returns French language filter options	
	* @return French language filter options
	*/
	public Language.LEVEL getFrench() {
		return this.french;
	}
	
	/**
	* Returns skills to filter on
	* @return Skill filter options
	*/
	public Set<String> getSkills() {
		return this.skills;
	}
	
	/**
	* Replaces any existing skills with an updated
	* skill list
	* @param skills
	*/
	public void setSkills(Set<String> skills) {
		this.skills.clear();
		this.skills.addAll(skills);
	}
	
	/**
	* Returns a Builder for the CandidateSearchAlert class
	* @return Builder
	*/
	public static CandidateSearchAlertBuilder builder() {
		return new CandidateSearchAlertBuilder();
	}
	
	/**
	* Builder for the CandidateSearchAlert class
	* @author K Parkings
	*/
	public static class CandidateSearchAlertBuilder{
		
		private UUID 				alertId;
		private String 				recruiterId;
		private String 				alertName;
		private Set<COUNTRY> 		countries							= new HashSet<>();
		private Set<FUNCTION> 		functions							= new HashSet<>();
		private Optional<Boolean> 	freelance							= Optional.empty();
		private Optional<Boolean> 	perm								= Optional.empty();
		private int 				yearsExperienceGtEq;
		private int					yearsExperienceLtEq;
		private Language.LEVEL 		dutch;
		private Language.LEVEL 		english;
		private Language.LEVEL 		french;
		private Set<String>			skills								= new HashSet<>();
		
		/**
		* Sets the Unique identifier of the Alert
		* @param alertId - Unique identifier
		* @return Builder
		*/
		public CandidateSearchAlertBuilder alertId(UUID alertId) {
			this.alertId = alertId;
			return this;
		}
		
		/**
		* Sets the unique Id of the recruiter who owns the alert
		* @param recruiterId - Unique id of the recruiter
		* @return Builder
		*/
		public CandidateSearchAlertBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the name of the Alert
		* @param alertName - Display name of the alert
		* @return Builder
		*/
		public CandidateSearchAlertBuilder alertName(String alertName) {
			this.alertName = alertName;
			return this;
		}
		
		/**
		* Sets the countries to filter on
		* @param countries - countries to filter on
		* @return Builder
		*/
		public CandidateSearchAlertBuilder countries(Set<COUNTRY> countries) {
			this.countries.clear();
			this.countries.addAll(countries);
			return this;
		}
		
		/**
		* Sets the functions to filter on
		* @param functions - functions to filter on
		* @return Builder
		*/
		public CandidateSearchAlertBuilder functions(Set<FUNCTION> functions) {
			this.functions.clear();
			this.functions.addAll(functions);
			return this;
		}
		
		/**
		* Sets the whether to include freelance candidates
		* @param freelance - freelance filter
		* @return Builder
		*/
		public CandidateSearchAlertBuilder freelance(Optional<Boolean> freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Sets whether to include perm candidates
		* @param perm - perm filter
		* @return Builder
		*/
		public CandidateSearchAlertBuilder perm (Optional<Boolean> perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Minimum number of years experience filter
		* @param yearsExperienceGtEq - experience filter
		* @return Builder
		*/
		public CandidateSearchAlertBuilder yearsExperienceGtEq (int yearsExperienceGtEq) {
			this.yearsExperienceGtEq = yearsExperienceGtEq;
			return this;
		}
		
		/**
		* Maximum number of years experience filter
		* @param yearsExperienceLtEq
		* @return Builder
		*/
		public CandidateSearchAlertBuilder yearsExperienceLtEq(int yearsExperienceLtEq) {
			this.yearsExperienceLtEq = yearsExperienceLtEq;
			return this;
		}
		
		/**
		* Set the Dutch language filter options
		* @param dutch - Dutch language filter options
		* @return Builder
		*/
		public CandidateSearchAlertBuilder dutch(Language.LEVEL dutch) {
			this.dutch = dutch;
			return this;
		}
		
		/**
		* Sets the English language filter options
		* @param english - English language filter options
		* @return Builder
		*/
		public CandidateSearchAlertBuilder english(Language.LEVEL english) {
			this.english = english;
			return this;
		}
		
		/**
		* Sets the French language filter options
		* @param french - French language filter options
		* @return Builder
		*/
		public CandidateSearchAlertBuilder french(Language.LEVEL french) {
			this.french = french;
			return this;
		}
		
		/**
		* Set the skills to filter on
		* @param skills - Skills to filter on
		* @return Builder
		*/
		public CandidateSearchAlertBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Returns a CandidateSearchAlert initialized with the values 
		* in the Builder
		* @return CandidateSearchAlert
		*/
		public CandidateSearchAlert build() {
			return new CandidateSearchAlert(this);
		}
		
	}
	
}