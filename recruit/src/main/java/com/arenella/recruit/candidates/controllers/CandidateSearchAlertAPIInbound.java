package com.arenella.recruit.candidates.controllers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.arenella.recruit.candidates.beans.CandidateSearchAlert;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Inbound API representation of a CandidateSearchAlert
* @author K Parkings
*/
@JsonDeserialize(builder=CandidateSearchAlertAPIInbound.CandidateSearchAlertAPIInboundBuilder.class)
public class CandidateSearchAlertAPIInbound {

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
	* Default constructor 
	*/
	public CandidateSearchAlertAPIInbound() {
		//JASKSON
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public CandidateSearchAlertAPIInbound(CandidateSearchAlertAPIInboundBuilder builder) {
		
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
	* Returns a Builder for the CandidateSearchAlert class
	* @return Builder
	*/
	public static CandidateSearchAlertAPIInboundBuilder builder() {
		return new CandidateSearchAlertAPIInboundBuilder();
	}
	
	/**
	* Builder for the CandidateSearchAlert class
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class CandidateSearchAlertAPIInboundBuilder{
		
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
		* Sets the name of the Alert
		* @param alertName - Display name of the alert
		* @return Builder
		*/
		public CandidateSearchAlertAPIInboundBuilder alertName(String alertName) {
			this.alertName = alertName;
			return this;
		}
		
		/**
		* Sets the countries to filter on
		* @param countries - countries to filter on
		* @return Builder
		*/
		public CandidateSearchAlertAPIInboundBuilder countries(Set<COUNTRY> countries) {
			this.countries.clear();
			this.countries.addAll(countries);
			return this;
		}
		
		/**
		* Sets the functions to filter on
		* @param functions - functions to filter on
		* @return Builder
		*/
		public CandidateSearchAlertAPIInboundBuilder functions(Set<FUNCTION> functions) {
			this.functions.clear();
			this.functions.addAll(functions);
			return this;
		}
		
		/**
		* Sets the whether to include freelance candidates
		* @param freelance - freelance filter
		* @return Builder
		*/
		public CandidateSearchAlertAPIInboundBuilder freelance(Optional<Boolean> freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Sets whether to include perm candidates
		* @param perm - perm filter
		* @return Builder
		*/
		public CandidateSearchAlertAPIInboundBuilder perm (Optional<Boolean> perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Minimum number of years experience filter
		* @param yearsExperienceGtEq - experience filter
		* @return Builder
		*/
		public CandidateSearchAlertAPIInboundBuilder yearsExperienceGtEq (int yearsExperienceGtEq) {
			this.yearsExperienceGtEq = yearsExperienceGtEq;
			return this;
		}
		
		/**
		* Maximum number of years experience filter
		* @param yearsExperienceLtEq
		* @return Builder
		*/
		public CandidateSearchAlertAPIInboundBuilder yearsExperienceLtEq(int yearsExperienceLtEq) {
			this.yearsExperienceLtEq = yearsExperienceLtEq;
			return this;
		}
		
		/**
		* Set the Dutch language filter options
		* @param dutch - Dutch language filter options
		* @return Builder
		*/
		public CandidateSearchAlertAPIInboundBuilder dutch(Language.LEVEL dutch) {
			this.dutch = dutch;
			return this;
		}
		
		/**
		* Sets the English language filter options
		* @param english - English language filter options
		* @return Builder
		*/
		public CandidateSearchAlertAPIInboundBuilder english(Language.LEVEL english) {
			this.english = english;
			return this;
		}
		
		/**
		* Sets the French language filter options
		* @param french - French language filter options
		* @return Builder
		*/
		public CandidateSearchAlertAPIInboundBuilder french(Language.LEVEL french) {
			this.french = french;
			return this;
		}
		
		/**
		* Set the skills to filter on
		* @param skills - Skills to filter on
		* @return Builder
		*/
		public CandidateSearchAlertAPIInboundBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Returns a CandidateSearchAlert initialized with the values 
		* in the Builder
		* @return CandidateSearchAlert
		*/
		public CandidateSearchAlertAPIInbound build() {
			return new CandidateSearchAlertAPIInbound(this);
		}
		
	}
	
	/**
	* Converts API Inbound representation to domain representation. 
	* @param inbound
	* @return
	*/
	public static CandidateSearchAlert convertToDomain(CandidateSearchAlertAPIInbound inbound) {
		return CandidateSearchAlert
				.builder()
					.alertName(inbound.getAlertName())
					.countries(inbound.getCountries())
					.dutch(inbound.getDutch())
					.english(inbound.getEnglish())
					.french(inbound.getFrench())
					.freelance(inbound.getFreelance())
					.functions(inbound.getFunctions())
					.perm(inbound.getPerm())
					.skills(inbound.getSkills())
					.yearsExperienceGtEq(inbound.getYearsExperienceGtEq())
					.yearsExperienceLtEq(inbound.getyearsExperienceLtEq())					
				.build(); 
	}
	
}