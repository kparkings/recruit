package com.arenella.recruit.candidates.adapters;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Event to inform the world that a new Candidate has been added to the 
* System.
* @author K Parkings
*/
public class CandidateCreatedEvent {

	private String 			candidateId;
	private String			roleSought;
	private FUNCTION		function;
	private COUNTRY 		country;
	private String 			city;
	private PERM 			perm;
	private FREELANCE 		freelance;
	private int				yearsExperience;
	private Set<String> 	skills						= new LinkedHashSet<>();
	private Set<Language> 	languages					= new LinkedHashSet<>();
	
	/**
	* Constructor based upon Builder
	* @param builder - Contains initialzation values
	*/
	public CandidateCreatedEvent(CandidateCreatedEventBuilder builder) {
		
		this.candidateId 		= builder.candidateId;
		this.roleSought 		= builder.roleSought;
		this.function 			= builder.function;
		this.country 			= builder.country;
		this.city 				= builder.city;
		this.perm 				= builder.perm;
		this.freelance 			= builder.freelance;
		this.yearsExperience 	= builder.yearsExperience;
		this.skills 			= builder.skills;
		this.languages 			= builder.languages;
		
	}
	
	/**
	* Return the Unique Identifier of the Candidate
	* @return id
	*/
	public String getCandidateId() {
		return this.candidateId;
	}
	
	/**
	* Return the role sought by the Candidate
	* @return role
	*/
	public String getRoleSought() {
		return this.roleSought;
	}
	
	/**
	* Return the Function type sort by the Candidate
	* @return Function type
	*/
	public FUNCTION	getFunction() {
		return this.function;
	}
	
	/**
	* Return the Country the Candidate is looking for work in
	* @return country
	*/
	public COUNTRY getCountry() {
		return this.country;
	}
	
	/**
	* Returns the City the Candidate is based in
	* @return City
	*/
	public String getCity() {
		return this.city;
	}
	
	/**
	* Returns whether the candidate is looking for a Perm position
	* @return whether the candidate is looking for a Perm position
	*/
	public PERM isPerm() {
		return this.perm;
	}
	
	/**
	* Returns whether the candidate is looking for a Freelance position
	* @return whether the candidate is looking for a Freelance position
	*/
	public FREELANCE isFreelance() {
		return this.freelance;
	}
	
	/**
	* Returns the number of years expereince the Candidate has
	* @return years experience
	*/
	public int getYearsExperience() {
		return this.yearsExperience;
	}
	
	/**
	* Returns the skills posessed by the Candidate
	* @return skills
	*/
	public Set<String> getSkills() {
		return this.skills;
	}
	
	/**
	* Returns the languages spoken by the Candidate
	* @return candidates languages
	*/
	public Set<Language> getLanguages() {
		return this.languages;
	}
	
	/**
	* Returns a Builder for the CandidateCreatedEvent class
	* @return Builder for the CandidateCreatedEvent class
	*/
	public static CandidateCreatedEventBuilder builder() {
		return new CandidateCreatedEventBuilder();
	}
	
	/**
	* Builder for the CandidateCreatedEvent class
	* @author K Parkings
	*/
	public static class CandidateCreatedEventBuilder {
		
		private String 			candidateId;
		private String			roleSought;
		private FUNCTION		function;
		private COUNTRY 		country;
		private String 			city;
		private PERM 			perm;
		private FREELANCE 		freelance;
		private int				yearsExperience;
		private Set<String> 	skills						= new LinkedHashSet<>();
		private Set<Language> 	languages					= new LinkedHashSet<>();
		
		/**
		* Initializes the builder with values in the Candidate
		* @param candidates - Added Candidate
		* @return Builder
		*/
		public CandidateCreatedEventBuilder candidate(Candidate candidate) {
			
			if (StringUtils.hasText(this.candidateId)) {
				throw new IllegalArgumentException("Cant update existing unique id");
			}
			
			this.candidateId 		= candidate.getCandidateId();
			this.roleSought 		= candidate.getRoleSought();
			this.function 			= candidate.getFunction();
			this.country 			= candidate.getCountry();
			this.city 				= candidate.getCity();
			this.perm 				= candidate.isPerm();
			this.freelance 			= candidate.isFreelance();
			this.yearsExperience 	= candidate.getYearsExperience();
			
			this.skills.clear();
			this.languages.clear();
			
			this.skills.addAll(candidate.getSkills());
			this.languages.addAll(candidate.getLanguages());
			
			return this;
		}
		
		/**
		* Returns an initialize instance of CandidateCreatedEvent 
		* @return initialized instance of CandidateCreatedEvent
		*/
		public CandidateCreatedEvent build() {
			return new CandidateCreatedEvent(this);
		}

		/**
		* Sets the Id of the Candidate
		* @param candidateId - Unique Id of the Candidate
		* @return Builder
		*/
		public CandidateCreatedEventBuilder candidateId(String candidateId) {
			
			if (StringUtils.hasText(this.candidateId)) {
				throw new IllegalArgumentException("Cant update existing unique id");
			}
			
			this.candidateId = candidateId;
			return this;
		}
		
	}
	
}