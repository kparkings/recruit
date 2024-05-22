package com.arenella.recruit.candidates.beans;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;
import com.arenella.recruit.candidates.utils.GeoZoneSearchUtil.GEO_ZONE;

/**
* Represents the filter option to apply to a fetch of 
* Candidates
* @author K Parkings
*/
public class CandidateFilterOptions {

	private String 				orderAttribute;
	private RESULT_ORDER		order;
	private String				searchText							= "";
	private Set<String> 		candidateIds						= new HashSet<>();
	private Set<GEO_ZONE>		geoZones							= new HashSet<>();
	private Set<COUNTRY> 		countries							= new HashSet<>();
	private Set<FUNCTION> 		functions							= new HashSet<>();
	private Optional<Boolean> 	freelance							= Optional.empty();
	private Optional<Boolean> 	perm								= Optional.empty();
	private Boolean			 	available							= null;
	private int 				yearsExperienceGtEq;
	private int					yearsExperienceLtEq;
	private Language.LEVEL 		dutch;
	private Language.LEVEL 		english;
	private Language.LEVEL 		french;
	private Language.LEVEL 		german;
	private Language.LEVEL 		italian;
	private Language.LEVEL 		polish;
	private Language.LEVEL 		portuguese;
	private Language.LEVEL 		spanish;
	private Set<String>			skills								= new HashSet<>();
	private String				firstname;
	private String 				surname;
	private String 				email;
	private Boolean				flaggedAsUnavailable 				= false;
	private Integer				daysSinceLastAvailabilityCheck;
	private String				ownerId;
	private Boolean				includeRequiresSponsorship			= null;
	private LocalDate			registeredAfter						= null;
	
	/**
	* Builder for the  
	* @param builder
	*/
	public CandidateFilterOptions(CandidateFilterOptionsBuilder builder) {
		this.init(builder);
	}
	
	/**
	* Sets values based upon builder
	* @param builder - Contains initialization values
	*/
	private void init(CandidateFilterOptionsBuilder builder) {
		
		this.orderAttribute 				= builder.orderAttribute;
		this.order 							= builder.order;
		this.candidateIds 					= builder.candidateIds;
		this.geoZones						= builder.geoZones;
		this.countries 						= builder.countries;
		this.functions		 				= builder.functions;
		this.freelance 						= builder.freelance;
		this.perm 							= builder.perm;
		this.yearsExperienceGtEq 			= builder.yearsExperienceGtEq;
		this.yearsExperienceLtEq			= builder.yearsExperienceLtEq;
		this.dutch							= builder.dutch;
		this.english						= builder.english;
		this.french							= builder.french;
		this.german							= builder.german;
		this.italian						= builder.italian;
		this.polish							= builder.polish;
		this.portuguese						= builder.portuguese;
		this.spanish						= builder.spanish;
		this.skills							= builder.skills;
		this.firstname						= builder.firstname;
		this.surname						= builder.surname;
		this.email							= builder.email;
		this.flaggedAsUnavailable			= builder.flaggedAsUnavailable;
		this.daysSinceLastAvailabilityCheck	= builder.daysSinceLastAvailabilityCheck;
		this.searchText						= builder.searchText;
		this.available 					 	= builder.available;
		this.ownerId						= builder.ownerId;
		this.includeRequiresSponsorship	 	= builder.includeRequiresSponsorship;
		this.registeredAfter				= builder.registeredAfter;
		
	}
	
	/**
	* Resets filter values to the defaults 
	*/
	public void reset() {
		this.init(CandidateFilterOptions.builder());
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
	* Returns the GeoZones to filter on
	* @return GeoZones to filter on
	*/
	public Set<GEO_ZONE> getGeoZones(){
		return this.geoZones;
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
	public Optional<Boolean> isFreelance() {
		return this.freelance;
	}
	
	/**
	* Returns whether or not to filter on perm candidates
	* @return to include perm candidates or not
	*/
	public Optional<Boolean> isPerm() {
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
	
	//REFACTOR
	public Optional<Language.LEVEL> getGerman(){
		return Optional.ofNullable(this.german);
	}
	
	//REFACTOR
	public Optional<Language.LEVEL> getItalian(){
		return Optional.ofNullable(this.italian);
	}
	
	//REFACTOR
	public Optional<Language.LEVEL> getPolish(){
		return Optional.ofNullable(this.polish);
	}
	
	//REFACTOR
	public Optional<Language.LEVEL> getPortuguese(){
		return Optional.ofNullable(this.portuguese);
	}
	
	//REFACTOR
	public Optional<Language.LEVEL> getSpanish(){
		return Optional.ofNullable(this.spanish);
	}
	
	/**
	* Sets the level of Dutch to filter on
	* @return Candidates level in Dutch
	*/
	public void setDutch(Language.LEVEL dutch){
		this.dutch = dutch;
	}
	
	/**
	* Sets the level of English to filter on
	* @return Candidate level in English
	*/
	public void setEnglish(Language.LEVEL english){
		this.english = english;
	}
	
	/**
	* Sets the level of French to filter on
	* @return Candidate level in French
	*/
	public void setFrench(Language.LEVEL french){
		this.french = french;
	}
	
	/**
	* Temp to be refacored
	*/
	public void setGerman(Language.LEVEL german){
		this.german = german;
	}
	
	/**
	* Temp to be refacored
	*/
	public void setItalian(Language.LEVEL italian){
		this.italian = italian;
	}
	
	/**
	* Temp to be refacored
	*/
	public void setPolish(Language.LEVEL polish){
		this.polish = polish;
	}
	
	/**
	* Temp to be refacored
	*/
	public void setPortuguese(Language.LEVEL potruguese){
		this.portuguese = potruguese;
	}
	
	/**
	* Temp to be refacored
	*/
	public void setSpanish(Language.LEVEL spanish){
		this.spanish = spanish;
	}
	
	/**
	* Sets whether to filter on whether a Candidate 
	* if Available, Unavailable or Both
	* @param available
	*/
	public void setAvailable(Boolean available) {
		this.available = available;
	}
	
	/**
	* Returns the skills to filter on
	* @return skills of the Candidate
	*/
	public Set<String> getSkills(){
		return skills;
	}	
	
	/**
	* Returns the firstname to filter on
	* @return firstname to filter on
	*/
	public Optional<String> getFirstname(){
		return Optional.ofNullable(firstname);
	}
	
	/**
	* Returns the surname to filter on 
	* @return Surname to filter on
	*/
	public Optional<String> getSurname(){
		return Optional.ofNullable(surname);
	}
	
	/**
	* Returns the email address to filter on 
	* @return email to filter on
	*/
	public Optional<String> getEmail(){
		return Optional.ofNullable(email);
	}
	
	/**
	* Returns the ownerId to filter on
	* @return ownerId
	*/
	public Optional<String> getOwnerId(){
		return Optional.ofNullable(this.ownerId);
	}
	
	/**
	* Returns the search text to filter on
	* @return a search term to filter on
	*/
	public String getSearchText() {
		return this.searchText;
	}
	
	/**
	* Returns whether to filter on Candidates that 
	* have been flagged as Unavailable
	* @return flagged as unavailable
	*/
	public Optional<Boolean> isFlaggedAsUnavailable(){
		return Optional.ofNullable(this.flaggedAsUnavailable);
	}
	
	/**
	* Returns whether to filter on the Candidates availability flag
	* @return availability
	*/
	public Optional<Boolean> isAvailable(){
		return Optional.ofNullable(this.available);
	}
	
	/**
	* Returns whether to filter on Candidates that have not had their 
	* availability checked since a certain number of days
	* @return daysSinceLastAvailabilityCheck to filter on
	*/
	public Optional<Integer> getDaysSinceLastAvailabilityCheck(){
		return Optional.ofNullable(this.daysSinceLastAvailabilityCheck);
	}
	
	/**
	* Returns filter options for filtering on Candidate's that require
	* Sponsorship
	* @return Filter options for sponsorship
	*/
	public Optional<Boolean> getIncludeRequiresSponsorship() {
		return Optional.ofNullable(this.includeRequiresSponsorship);
	}
	
	/**
	* Returns filter options for RegistrationDate. Results must be 
	* GTE than the provided registration Date
	* @return
	*/
	public Optional<LocalDate> getRegisteredAfter() {
		return Optional.ofNullable(this.registeredAfter);
	}
	
	/**
	* Replaces existing functions
	* @param functions - Functions to filter on
	*/
	public void setFunctions(Set<FUNCTION> functions) {
		this.functions.clear();
		this.functions.addAll(functions);
	}
	
	/**
	* Removes GeoZone filters
	*/
	public void removeGeoZones() {
		this.geoZones.clear();
	}
	
	/**
	* Adds a country to the filters
	* @param country - country to add to filter on
	*/
	public void addCountry(COUNTRY country) {
		this.countries.add(country);
	}

	/**
	* Sets whether or not to include Candidates that require sponsorship 
	* in the search results
	* @param includeRequiresSponsorship - Whether to include requires sponsorship candidates
	*/
	public void setIncludeRequiresSponsorship(Boolean includeRequiresSponsorship) {
		this.includeRequiresSponsorship = includeRequiresSponsorship;
	}
	
	/**
	* Sets the candidate Id's to filter on 
	* @param candidateIds - Id's to filter on
	*/
	public void setCandidateIds(Set<String> candidateIds) {
		this.candidateIds.clear();
		this.candidateIds.addAll(candidateIds);
		
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
		
		private String 				orderAttribute;
		private RESULT_ORDER		order;
		private Set<String> 		candidateIds						= new HashSet<>();
		private Set<GEO_ZONE>		geoZones							= new HashSet<>();
		private Set<COUNTRY> 		countries							= new HashSet<>();
		private Set<FUNCTION> 		functions							= new HashSet<>();
		private Optional<Boolean> 	freelance							= Optional.empty();
		private Optional<Boolean> 	perm								= Optional.empty();
		private int 				yearsExperienceGtEq;
		private int					yearsExperienceLtEq;
		private Language.LEVEL 		dutch;
		private Language.LEVEL 		english;
		private Language.LEVEL 		french;
		private Language.LEVEL 		german;
		private Language.LEVEL 		italian;
		private Language.LEVEL 		polish;
		private Language.LEVEL 		portuguese;
		private Language.LEVEL 		spanish;
		private Set<String>			skills								= new HashSet<>();
		private String				firstname;
		private String 				surname;
		private String 				email;
		private Boolean				flaggedAsUnavailable				= null;
		private Boolean			 	available							= null;
		private Integer				daysSinceLastAvailabilityCheck; 
		private String 				searchText							= "";
		private String 				ownerId;
		private Boolean				includeRequiresSponsorship			= null;
		private LocalDate			registeredAfter						= null;
		
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
		* Sets the GeoZones to filter on
		* @param geoZones - GeoZones to filter on
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder geoZones(Set<GEO_ZONE> geoZones) {
			if (Optional.ofNullable(geoZones).isPresent()) {
				this.geoZones.addAll(geoZones);
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
			this.freelance = Optional.ofNullable(freelance); // == null ? false : freelance;
			return this;
		}
		
		/**
		* Sets whether or not to filter on perm
		* @param perm - whether or not to include perm candidates
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder perm(Boolean perm) {
			this.perm = Optional.ofNullable(perm); // == null ? false : perm;
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
		
		//REFACTOR
		public CandidateFilterOptionsBuilder german(Language.LEVEL german) {
			this.german = german;
			return this;
		}
		
		//REFACTOR
		public CandidateFilterOptionsBuilder italian(Language.LEVEL italian) {
			this.italian = italian;
			return this;
		}
		
		//REFACTOR
		public CandidateFilterOptionsBuilder polish(Language.LEVEL polish) {
			this.polish = polish;
			return this;
		}
		
		//REFACTOR
		public CandidateFilterOptionsBuilder portuguese(Language.LEVEL portuguese) {
			this.portuguese = portuguese;
			return this;
		}
		
		//REFACTOR
		public CandidateFilterOptionsBuilder spanish(Language.LEVEL spanish) {
			this.spanish = spanish;
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
		* Sets the firstname of the Candidate to filter on 
		* @param firstname firstname of the candidate to filter on
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the surname of the Candidate to filter on 
		* @param surname surname of the candidate to filter on
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the email of the Candidate to filter on 
		* @param email email of the candidate to filter on
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets a search term to filter on
		* @param searchText - a search term to filter on
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder searchText(String searchText) {
			this.searchText = searchText;
			return this;
		}

		/**
		* Sets whether to filter on Candidates that have been marked as being unavailable
		* @param flaggedAsUnavailable - Whether to filter on flaggedAsUnavailable
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder flaggedAsUnavailable(Boolean flaggedAsUnavailable) {
			this.flaggedAsUnavailable = flaggedAsUnavailable;
			return this;
		}
		
		/**
		* Sets whether to filer on the whether a Candidate has been marked as available/unavailable
		* By default only available candidates will be returned
		* @param available - Filter on availability
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder available(Boolean available) {
			this.available = available;
			return this;
		}
		
		/**
		* Sets the Id of the User who owns/represents the Candidate
		* @param ownerId - Owner Id
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder ownerId(String ownerId) {
			this.ownerId = ownerId;
			return this;
		}
		
		/**
		* Sets whether to filter on Candidates that have not had their availability checked since
		* a given number of days
		* @param daysSinceLastAvailabilityCheck - Days since Candidates availability was last checked
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder daysSinceLastAvailabilityCheck(Integer daysSinceLastAvailabilityCheck) {
			this.daysSinceLastAvailabilityCheck = daysSinceLastAvailabilityCheck;
			return this;
		}
		
		/**
		* Sets whether or not to include candidates needing sponsorship
		* @param includeRequiresSponsorship = Whether or not to include candidates needing sponsorship
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder includeRequiresSponsorship(Boolean includeRequiresSponsorship) {
			this.includeRequiresSponsorship = includeRequiresSponsorship;
			return this;
		}
		
		/**
		* Sets registrations Date. Must be GTE date provided 
		* @param registeredAfter - registeredDate >=
		* @return Builder
		*/
		public CandidateFilterOptionsBuilder registeredAfter(LocalDate registeredAfter) {
			this.registeredAfter = registeredAfter;
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