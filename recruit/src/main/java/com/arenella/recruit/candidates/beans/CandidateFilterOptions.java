package com.arenella.recruit.candidates.beans;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;

/**
* Represents the filter option to apply to a fetch of 
* Candidates
* @author K Parkings
*/
public class CandidateFilterOptions {

	private String 			orderAttribute;
	private RESULT_ORDER	order;
	private Set<String> 	candidateIds			= new HashSet<>();
	private Set<COUNTRY> 	countries				= new HashSet<>();
	private Set<FUNCTION> 	functions				= new HashSet<>();
	private boolean 		freelance				= true;
	private boolean 		perm					= true;
	private int 			yearsExperienceGtEq;
	private int				yearsExperienceLtEq;
	private Language.LEVEL 	dutch;
	private Language.LEVEL 	english;
	private Language.LEVEL 	french;
	private Set<String>		skills					= new HashSet<>();
	
	/**
	* Builder for the  
	* @param builder
	*/
	public CandidateFilterOptions(CandidateFilterOptionsBuilder builder) {
		
		this.orderAttribute 				= builder.orderAttribute;
		this.order 							= builder.order;
		this.candidateIds 					= builder.candidateIds;
		this.countries 						= builder.countries;
		this.functions		 				= builder.functions;
		this.freelance 						= builder.freelance;
		this.perm 							= builder.perm;
		this.yearsExperienceGtEq 			= builder.yearsExperienceGtEq;
		this.yearsExperienceLtEq			= builder.yearsExperienceLtEq;
		this.dutch							= builder.dutch;
		this.english						= builder.english;
		this.french							= builder.french;
		this.skills							 = builder.skills;
		
	}
	
	/**
	* Returns the attribute to sort the results on
	* @return name of attribute to sort on
	*/
	public Optional<String> getOrderAttribute(){
		return Optional.ofNullable(this.orderAttribute);
	}
	
	/**
	* Returns the direction to sort on
	* @return direction to sort on
	*/
	public Optional<RESULT_ORDER> getOrder(){
		return Optional.ofNullable(this.order);
	}
	
	/**
	* Returns the candidateIds if any to filter on
	* @return candidateIds to filteron
	*/
	public Set<String> 	getCandidateIds(){
		return this.candidateIds;
	}		
	
	/**
	* Returns the countries to filter on
	* @return countries to filter on
	*/
	public Set<COUNTRY> 	getCountries(){
		return this.countries;
	}	
	
	/**
	* Returns the function to filter on
	* @return functions to filter on
	*/
	public Set<FUNCTION> 	getFunctions(){
		return this.functions;
	}	
	
	/**
	* Returns whether or not to filter on freelance roles
	* @return to include freelance candidates or not
	*/
	public boolean isFreelance() {
		return this.freelance;
	}
	
	/**
	* Returns whether or not to filter on perm candidates
	* @return to include perm candidates or not
	*/
	public boolean isPerm() {
		return this.perm;
	}
	
	/**
	* Filters on candidates with at least the number of years experience
	* @return minimum years experience to filter on
	*/
	public int getYearsExperienceGtEq() {
		return this.yearsExperienceGtEq;
	}
	
	/**
	* Filters on candidates to exclude those with more 
	* than the specified number of years experience
	* @return maximum number of years experience to filter on
	*/
	public int getYearsExperienceLtEq() {
		return this.yearsExperienceLtEq;
	}
	
	/**
	* Returns the level of Dutch to filter on
	* @return Candidates level in Dutch
	*/
	public Optional<Language.LEVEL> getDutch(){
		return Optional.ofNullable(this.dutch);
	}
	
	/**
	* Returns the level of English to filter on
	* @return Candidate level in English
	*/
	public Optional<Language.LEVEL> getEnglish(){
		return Optional.ofNullable(this.english);
	}
	
	/**
	* Returns the level of French to filter on
	* @return Candidate level in French
	*/
	public Optional<Language.LEVEL> getFrench(){
		return Optional.ofNullable(this.french);
	}
	
	/**
	* Returns the skills to filter on
	* @return skills of the Candidate
	*/
	public Set<String> getSkills(){
		return skills;
	}	
	
	/**
	* Returns a Builder for the CandidateFilterOptions class
	* @return
	*/
	public static CandidateFilterOptionsBuilder builder() {
		return new CandidateFilterOptionsBuilder();
	}
	
	/**
	* Builder for the CandidateFilterOptions class
	* @author K Parkings
	*/
	public static class CandidateFilterOptionsBuilder {
		
		private String 			orderAttribute;
		private RESULT_ORDER	order;
		private Set<String> 	candidateIds			= new HashSet<>();
		private Set<COUNTRY> 	countries				= new HashSet<>();
		private Set<FUNCTION> 	functions				= new HashSet<>();
		private boolean 		freelance				= true;
		private boolean 		perm					= true;
		private int 			yearsExperienceGtEq;
		private int				yearsExperienceLtEq;
		private Language.LEVEL 	dutch;
		private Language.LEVEL 	english;
		private Language.LEVEL 	french;
		private Set<String>		skills					= new HashSet<>();
		
		/**
		* Sets the name of the attribute to order on
		* @param orderAttribute - name of the attribute to order on
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder orderAttribute(String orderAttribute) {
			this.orderAttribute = orderAttribute;
			return this;
		}
		
		/**
		* Sets the order direction to order the results on
		* @param order - direction of ordering
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder order(RESULT_ORDER order) {
			this.order = order;
			return this;
		}
		
		/**
		* Sets the Candidate Id's to filter on. The Candidate must have at least one of the 
		* provided ids to be included in the results. If no functions are provided 
		* all candidates matching the remaining filters will be returned
		* @param candidateId - ids of candidates to filter on
		*/
		public CandidateFilterOptionsBuilder candidateIds(Set<String> candidateIds) {
			if (Optional.ofNullable(candidateIds).isPresent()) {
				this.candidateIds.addAll(candidateIds);
			}
			return this;
		}
		
		/**
		* Sets the Countries to filter on. The Candidate must have at least one of the 
		* provided Countries to be included in the results. If no functions are provided 
		* all candidates matching the remaining filters will be returned
		* @param countries - countries to filter on
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder countries(Set<COUNTRY> countries) {
			if (Optional.ofNullable(countries).isPresent()) {
				this.countries.addAll(countries);
			}
			return this;
		}
		
		/**
		* Sets the Functions to filter on. The Candidate must have at least one of the 
		* provided functions to be included in the results. If no functions are provided 
		* all candidates matching the remaining filters will be returned
		* @param functions - functions to filter on
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder functions(Set<FUNCTION> functions) {
			if (Optional.ofNullable(functions).isPresent()) {
				this.functions.addAll(functions);
			}
			return this;
		}
		
		/**
		* Sets whether or not to filter on freelance
		* @param freelance - whether or not to include freelancers
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder freelance(Boolean freelance) {
			this.freelance = freelance == null ? true : freelance;
			return this;
		}
		
		/**
		* Sets whether or not to filter on perm
		* @param perm - whether or not to include perm candidates
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder perm(Boolean perm) {
			this.perm = perm == null ? true : perm;
			return this;
		}
		
		/**
		* Sets minimum number of years experience the candidate must have 
		* to be included in the results
		* @param yearsExperienceGtEq - Minimum number of years experience
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder yearsExperienceGtEq(Integer yearsExperienceGtEq) {
			if (Optional.ofNullable(yearsExperienceGtEq).isPresent()) {
				this.yearsExperienceGtEq = yearsExperienceGtEq;
			}
			return this;
		
		
		}
		
		/**
		* Sets maximum number of years experience the candidate can have 
		* to be included in the results
		* @param yearsExperienceLtEq - Maximum number of years experience
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder yearsExperienceLtEq(Integer yearsExperienceLtEq) {
			if (Optional.ofNullable(yearsExperienceLtEq).isPresent()) {
				this.yearsExperienceLtEq = yearsExperienceLtEq;
			}
			return this;
		}
		
		/**
		* Sets the level of Dutch needed to be included in the Search results
		* @param perm - Level of Dutch needed to be included in the Search results
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder dutch(Language.LEVEL dutch) {
			this.dutch = dutch;
			return this;
		}
		
		/**
		* Sets the level of English needed to be included in the Search results
		* @param perm - Level of English needed to be included in the Search results
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder english(Language.LEVEL english) {
			this.english = english;
			return this;
		}
		
		
		/**
		* Sets the level of French needed to be included in the Search results
		* @param perm - Level of French needed to be included in the Search results
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder french(Language.LEVEL french) {
			this.french = french;
			return this;
		}
		
		/**
		* Sets the Skills the Candidate posesses. The Candidate must have at least one 
		* of the skills provided to be included. If no skills are provided any candidate 
		* meeting the remaining search filters will be included in the results
		* @param functions - skills ot filter on
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder skills(Set<String> skills) {
			if (Optional.ofNullable(skills).isPresent()) {
				this.skills.addAll(skills);
			}
			return this;
		}
		
		/**
		* Returns an instance of CandidateFilterOptions initialized with the
		* values in the Builder 
		* @return Instance of CandidateFilterOptions
		*/
		public CandidateFilterOptions build() {
			return new CandidateFilterOptions(this);
		}
		
	}
	
}