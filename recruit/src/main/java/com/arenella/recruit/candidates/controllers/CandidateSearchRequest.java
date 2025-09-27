package com.arenella.recruit.candidates.controllers;

import java.util.Set;

import com.arenella.recruit.candidates.beans.Candidate.SECURITY_CLEARANCE_TYPE;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions;
import com.arenella.recruit.candidates.beans.CandidateFilterOptions.CandidateFilterOptionsBuilder;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.PERM;
import com.arenella.recruit.candidates.enums.RESULT_ORDER;
import com.arenella.recruit.candidates.utils.GeoZoneSearchUtil.GEO_ZONE;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.HashSet;
import java.util.Optional;

/**
* Class represents a Request for candidates matching
* search parameters
*/
@JsonDeserialize(builder=CandidateSearchRequest.CandidateSearchRequestBuilder.class)
public class CandidateSearchRequest {
	
	private RequestFilters 		requestFilters;
	private LocationFilters 	locationFilters;
	private ContractFilters 	contractFilters;
	private LanguageFilters 	languageFilters;
	private ExperienceFilters 	experienceFilters;
	private SkillFilters 		skillFilters;
	private IncludeFilters 		includeFilters;
	private TermFilters 		termFilters;
	private CandidateFilters	candidateFilters;
	private SecurityFilters		securityFilters;
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public CandidateSearchRequest(CandidateSearchRequestBuilder builder) {
		this.requestFilters 		= builder.requestFilters;
		this.locationFilters 		= builder.locationFilters;
		this.contractFilters 		= builder.contractFilters;
		this.languageFilters 		= builder.languageFilters;
		this.experienceFilters 		= builder.experienceFilters;
		this.skillFilters 			= builder.skillFilters;
		this.includeFilters 		= builder.includeFilters;
		this.termFilters 			= builder.termFilters;
		this.candidateFilters		= builder.candidateFilters;
		this.securityFilters		= builder.securityFilters;
	}
	
	/**
	* If provided returns the filters at the request 
	* level
	* @return Request level filters
	*/
	public Optional<RequestFilters> requestFilters(){
		return Optional.ofNullable(this.requestFilters);
	}
	
	/**
	* If provided returns filters relating to the location
	* of the Candidates
	* @return Location filters
	*/
	public Optional<LocationFilters> locationFilters(){
		return Optional.ofNullable(this.locationFilters);
	}
	
	/**
	* If provided returns filters relating to the Contract 
	* type the candidate is searching for
	* @return Contract Filters
	*/
	public Optional<ContractFilters> contractFilters(){
		return Optional.ofNullable(this.contractFilters);
	}
	
	/**
	* If provided returns filters relating to the Languages
	* that are spoken by the Candidate
	* @return Language Filters
	*/
	public Optional<LanguageFilters> languageFilters(){
		return Optional.ofNullable(this.languageFilters);
	}
	
	/**
	* If provided returns filters relating the the number of 
	* years experience the candidate has
	* @return Experience filters
	*/
	public Optional<ExperienceFilters> experienceFilters(){
		return Optional.ofNullable(this.experienceFilters);
	}
	
	/**
	* If provided returns filters relating to the Skills the 
	* Candidate has
	* @return Skill filters
	*/
	public Optional<SkillFilters> skillFilters(){
		return Optional.ofNullable(this.skillFilters);
	}
	
	/**
	* If provided returns filters from searching on terms such 
	* as the name of a job title or email address
	* @return
	*/
	public Optional<TermFilters> termFilters(){
		return Optional.ofNullable(this.termFilters);
	}
	
	/**
	* If provided returns filters relating to options of including
	* certain types of Candidates that by default are not 
	* included in the results
	* @return Include filters
	*/
	public Optional<IncludeFilters> includeFilters(){
		return Optional.ofNullable(this.includeFilters);
	}
	
	public Optional<CandidateFilters> candidateFilters(){
		return Optional.ofNullable(this.candidateFilters);
	}
	
	/**
	* If provided returns security levels to filter on
	* @return filters
	*/
	public Optional<SecurityFilters> securityFilters(){
		return Optional.ofNullable(this.securityFilters);
	}
	
	/**
	* Returns a builder for the class 
	*/
	public static CandidateSearchRequestBuilder builder() {
		return new CandidateSearchRequestBuilder();
	}
	
	/**
	* Builder for the class 
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class CandidateSearchRequestBuilder {
		
		private RequestFilters 		requestFilters;
		private LocationFilters 	locationFilters;
		private ContractFilters 	contractFilters;
		private LanguageFilters 	languageFilters;
		private ExperienceFilters 	experienceFilters;
		private SkillFilters 		skillFilters;
		private IncludeFilters 		includeFilters;
		private TermFilters 		termFilters;
		private CandidateFilters	candidateFilters;
		private SecurityFilters		securityFilters;
		
		/**
		* Sets the Term Request to apply to the search request
		* @param requestFilters
		* @return Builder
		*/
		public CandidateSearchRequestBuilder requestFilters(RequestFilters requestFilters) {
			this.requestFilters = requestFilters;
			return this;
		}
		
		/**
		* Sets the Location filters to apply to the search request
		* @param locationFilters - Filters to apply to search request
		* @return Builder
		*/
		public CandidateSearchRequestBuilder locationFilters(LocationFilters locationFilters) {
			this.locationFilters = locationFilters;
			return this;
		}
		
		/**
		* Sets the Contract filters to apply to the search request
		* @param contractFilters - Filters to apply to search request
		* @return Builder
		*/
		public CandidateSearchRequestBuilder contractFilters(ContractFilters contractFilters){
			this.contractFilters = contractFilters;
			return this;
		}
		
		/**
		* Sets the Language filters to apply to the search request
		* @param languageFilters - Filters to apply to search request
		* @return Builder
		*/
		public CandidateSearchRequestBuilder languageFilters(LanguageFilters languageFilters){
			this.languageFilters = languageFilters;
			return this;
		}
		
		/**
		* Sets Experience filters to apply to the search request
		* @param experienceFilters - Filters to apply to search request
		* @return Builder
		*/
		public CandidateSearchRequestBuilder experienceFilters(ExperienceFilters experienceFilters){
			this.experienceFilters = experienceFilters;
			return this;
		}
		
		/**
		* Sets the Skill filters to apply to the search request
		* @param skillFilters - Filters to apply to search request
		* @return Builder
		*/
		public CandidateSearchRequestBuilder skillFilters(SkillFilters skillFilters){
			this.skillFilters = skillFilters;
			return this;
		}
		
		/**
		* Sets the Include filters to apply to the search request
		* @param includeFilters - Filters to apply to search request
		* @return Builder
		*/
		public CandidateSearchRequestBuilder includeFilters(IncludeFilters includeFilters){
			this.includeFilters = includeFilters;
			return this;
		}
		
		/**
		* Sets optionla security levels to filter on
		* @param securityFilters - Security related filters
		* @return Builder
		*/
		public CandidateSearchRequestBuilder securityFilters(SecurityFilters securityFilters){
			this.securityFilters = securityFilters;
			return this;
		}
		
		
		/**
		* Sets the Term filters to apply to the search request
		* @param termFilters - Filters to apply to search request
		* @return Builder
		*/
		public CandidateSearchRequestBuilder termFilters(TermFilters termFilters){
			this.termFilters = termFilters;
			return this;
		}
		
		/**
		* Sets the Candidate filters to apply to the search request
		* @param candidateFilters - Filters to apply to search request
		* @return Builder
		*/
		public CandidateSearchRequestBuilder candidateFilters(CandidateFilters candidateFilters){
			this.candidateFilters = candidateFilters;
			return this;
		}
		
		/**
		* Creates instance using values in Builder
		* @return Initialized instance
		*/
		public CandidateSearchRequest build() {
			return new CandidateSearchRequest(this);
		}
		
	}
	
	/**
	* Filters at the request level f a CandidateSearchRequest. These are 
	* non Candidates specific filters such as max number of results etc
	*/
	@JsonDeserialize(builder=RequestFilters.RequestFiltersBuilder.class)
	public static class RequestFilters{
	
		private Integer	backendRequestId;
		private Boolean	unfiltered;
		private Integer	maxNumberOfSuggestions;
	
		/**
		* Constructor based upon a Builder
		* @param builder - Contains initialization values
		*/
		public RequestFilters(RequestFiltersBuilder builder) {
			this.backendRequestId 			= builder.backendRequestId;
			this.unfiltered 				= builder.unfiltered;
			this.maxNumberOfSuggestions 	= builder.maxNumberOfSuggestions;
		}
	
		/**
		* Returns if provided the backendRequestId. This is used by the 
		* FE to identify the individual response to a request to ensure 
		* an older requests response does not override a newer one
		* It is typically written to the response for use by the FE
		* @return Unique Id of the request
		*/
		public Optional<Integer> getBackendRequestId(){
			return Optional.ofNullable(this.backendRequestId);
		}
		
		/**
		* Returns if provided whether apply filters or not. If the 
		* value is true it indicates that the User wants just an 
		* un-filtered page of results. Useful in the FE to quickly 
		* display a page of Candidates without the delay of 
		* applying filter logic
		* @return Whether to perform an un-filtered search
		*/
		public Optional<Boolean> getUnfiltered(){
			return Optional.ofNullable(this.unfiltered);
		}
		
		/**
		* Returns the maximum number of results the search should return
		* @return maximum number of results to return
		*/
		public Optional<Integer> getMaxNumberOfSuggestions(){
			return Optional.ofNullable(this.maxNumberOfSuggestions);
		}
	
		/**
		* Returns a builder for the class 
		*/
		public static RequestFiltersBuilder builder() {
			return new RequestFiltersBuilder();
		}
		
		/**
		* Builder for the Class 
		*/
		@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
		public static class RequestFiltersBuilder {
			
			private Integer	backendRequestId;
			private Boolean	unfiltered;
			private Integer	maxNumberOfSuggestions;
			
			/**
			* Sets the uniqueId of the request
			* @param backendRequestId - Unique Id
			* @return Builder
			*/
			public RequestFiltersBuilder backendRequestId(Integer	backendRequestId) {
				this.backendRequestId = backendRequestId;
				return this;
			}
			
			/**
			* Sets whether to perform a search without applying filters
			* @param unfiltered - Whether to perform an un-filtered search
			* @return Builder
			*/
			public RequestFiltersBuilder unfiltered(Boolean unfiltered) {
				this.unfiltered = unfiltered;
				return this;
			}
			
			/**
			* Sets the maximum number of Suggested Candidates to return
			* @param maxNumberOfSuggestions - Max number of results
			* @return Builder
			*/
			public RequestFiltersBuilder maxNumberOfSuggestions(Integer maxNumberOfSuggestions) {
				this.maxNumberOfSuggestions = maxNumberOfSuggestions;
				return this;
			}
			
			/**
			* Returns an initialized instance of the Class
			* @return Instance of Class
			*/
			public RequestFilters build() {
				return new RequestFilters(this);
			}
			
		}
		
	}
	
	/**
	* Filters relating to the Candidates location 
	*/
	@JsonDeserialize(builder=LocationFilters.LocationFiltersBuilder.class)
	public static class LocationFilters{
		
		private Set<GEO_ZONE> 			geoZones = new HashSet<>(); 
		private Set<COUNTRY> 			countries = new HashSet<>();
		private LocationRangeFilters 	range;
		
		/**
		* Constructor based upon a Builder
		* @param builder - Contains initalizatiton values
		*/
		public LocationFilters(LocationFiltersBuilder builder) {
			this.geoZones.clear();
			this.countries.clear();
			
			this.geoZones.addAll(builder.geoZones);
			this.countries.addAll(builder.countries);
			
			this.range 	= builder.range;
		}
		
		/**
		* Returns GeoZone's to filter on
		* @return GeoZones
		*/
		public Set<GEO_ZONE> getGeoZones(){
			return this.geoZones;
		} 
		
		/**
		* Returns Countrie's to filter on
		* @return Countries
		*/
		public Set<COUNTRY> getCountries(){
			return this.countries;
		}
		
		/**
		* If applicable returns the Range from a specific location 
		* to filter on
		* @return Range information
		*/
		public Optional<LocationRangeFilters> getRange() {
			return Optional.ofNullable(this.range);
		}
		
		/**
		* Returns a builder for the class
		* @return
		*/
		public static LocationFiltersBuilder builder() {
			return new LocationFiltersBuilder();
		}
		
		/**
		* Builder for the class 
		*/
		@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
		public static class LocationFiltersBuilder {
			
			private Set<GEO_ZONE> 			geoZones = new HashSet<>(); 
			private Set<COUNTRY> 			countries = new HashSet<>();
			private LocationRangeFilters 	range;
			
			/**
			* Sets the GeoZones to filter on
			* @param geoZones - GeoZone's to filter on
			* @return Builder
			*/
			public LocationFiltersBuilder geoZones(Set<GEO_ZONE> geoZones) {
				this.geoZones.clear();
				this.geoZones.addAll(geoZones);
				return this;
			} 
			
			/**
			* Sets the Countrie's to filter on
			* @param countries - Countries to filter on
			* @return Builder
			*/
			public LocationFiltersBuilder countries(Set<COUNTRY> countries) {
				this.countries.clear();
				this.countries.addAll(countries);
				return this;
			}
			
			/**
			* Sets Filters for distance from a location to filter on
			* @param locationFilters - Location filters
			* @return Builder
			*/
			public LocationFiltersBuilder range(LocationRangeFilters range) {
				this.range = range;
				return this;
			}
			
			/**
			* Returns an initialized instance
			* @return initialized instance of the class
			*/
			public LocationFilters build() {
				return new LocationFilters(this);
			}
			
		}
		
		/**
		* Filters located to the range of a Candidate from a specific
		* location
		*/
		public static class LocationRangeFilters {
			
			private final COUNTRY 	country;
			private final String 	city;
			private final int 		distanceInKm;
			
			/**
			* Constuctor
			* @param country		- Country to perform search in
			* @param city			- City to use as location
			* @param distanceInKm	- Max distance from location to be included in the search results
			*/
			public LocationRangeFilters(COUNTRY country, String city, int distanceInKm) {
				this.country 		= country;
				this.city 			= city;
				this.distanceInKm 	= distanceInKm;
			}
			
			/**
			* Returns the Country to use as base for the search
			* @return Country
			*/
			public COUNTRY country() {
				return this.country;
			}
			
			/**
			* Returns the City to use as base for the seach
			* @return City
			*/
			public String city() {
				return this.city;
			}
			
			/**
			* Returns the distance a Candidate can be in from the County/City
			* combination to be included in the search results
			* @return max distance (lte)
			*/
			public int distanceInKm() {
				return this.distanceInKm < 1 ? 1 : this.distanceInKm;  
			}
			
		}
		
	}
	
	/**
	* Filters relating to the type of Contract the Candidate 
	* is open to
	*/
	@JsonDeserialize(builder=ContractFilters.ContractFiltersBuilder.class)
	public static class ContractFilters{
		
		private PERM 		perm;
		private FREELANCE 	contract;
		
		/**
		* Constructor based upon a builder
		* @param builder - Contains initialization values
		*/
		public ContractFilters(ContractFiltersBuilder builder){
			this.perm 		= builder.perm;
			this.contract 	= builder.contract;
		}
		
		/**
		* Returns a builder for the Class
		* @return Builder
		*/
		public static ContractFiltersBuilder builder() {
			return new ContractFiltersBuilder();
		}
		
		/**
		* Returns where applicable filter information 
		* relating to Candidate perm preferences 
		* @return perm filter
		*/
		public Optional<PERM> getPerm(){
			return Optional.ofNullable(this.perm);
		}
		
		/**
		* Returns where applicable filter information 
		* relating to Candidate contract preferences 
		* @return contract filter
		*/
		public Optional<FREELANCE> getContract(){
			return Optional.ofNullable(this.contract);
		}
		
		/**
		* Builder for Class
		*/
		@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
		public static class ContractFiltersBuilder{
			
			private PERM 		perm;
			private FREELANCE 	contract;
			
			/**
			* Sets the filters for the Candidates preferences 
			* relating to permanent contracts
			* @param perm - Perm preferences
			* @return Builder
			*/
			public ContractFiltersBuilder perm(PERM perm) {
				this.perm = perm;
				return this;
			}
			
			/**
			* Sets the filters for the Candidates preferences 
			* relating to Freelance contracts
			* @param contract - Contract preferences
			* @return Builder
			*/
			public ContractFiltersBuilder contract(FREELANCE contract) {
				this.contract = contract;
				return this;
			}
			
			/**
			* Returns an initialized instance of Class
			* @return initialized instance of Class
			*/
			public ContractFilters build() {
				return new ContractFilters(this);
			}
			
		}
		
	}
	
	/**
	* Filters relating to Languages spoken by the Candidate 
	*/
	public static class LanguageFilters{
		
		private Set<Language.LANGUAGE> languages = new HashSet<>();
		
		/**
		* Default constructor 
		*/
		public LanguageFilters() {
			//Jackson
		}
		
		/**
		* Constructor
		* @param languages - Languages to filter on
		*/
		public LanguageFilters(Set<Language.LANGUAGE> languages) {
			if (Optional.ofNullable(languages).isPresent()) {
				this.languages.clear();
				this.languages.addAll(languages);
			}
		}
		
		/**
		* Returns Languages to filter on
		* @return
		*/
		public Set<Language.LANGUAGE> getLanguages(){
			return this.languages;
		}
		
	}
	
	/**
	* Filters relating to a Candidates experience
	*/
	public static class ExperienceFilters{
	
		private Integer experienceMin;
		private Integer experienceMax;
	
		/**
		* Default constructor 
		*/
		public ExperienceFilters() {
			//Jackson
		}
		/**
		* Constructor
		* @param experienceMin - minimum (gte) years of experience to filter on
		* @param experienceMax - maximum (lte) years of experience to filter on
		*/
		public ExperienceFilters(Integer experienceMin, Integer experienceMax) {
			this.experienceMin = experienceMin;
			this.experienceMax = experienceMax;
		}
		
		/**
		* Returns the minimum (gte) years of experience to filter on
		* @return years to filter on
		*/
		public Optional<Integer> getExperienceMin(){
			return Optional.ofNullable(experienceMin);
		}
		
		/**
		* Returns the maximum (lte) years of experience to filter on
		* @return years to filter on
		*/
		public Optional<Integer> getExperienceMax(){
			return Optional.ofNullable(experienceMax);
		}
		
	}
	
	/**
	* Filters relating to the Candidates skills 
	*/
	public static class SkillFilters{
	
		private Set<String> skills = new HashSet<>();
		
		/**
		* Default constructor 
		*/
		public SkillFilters() {
			//Jackson
		}
		
		/**
		* Constructor
		* @param skills - Skills to filter on
		*/
		public SkillFilters(Set<String> skills) {
			if (Optional.ofNullable(skills).isPresent()) {
				this.skills.clear();
				this.skills.addAll(skills);
			}
		}
		
		/**
		* Returns the skills to filter on.
		* @return Skills
		*/
		public Set<String> getSkills(){
			return this.skills;
		}
	
	}
	
	/**
	* Include filters. These are used to include certain types
	* of candidates that by default are ommitted from the 
	* search results
	*/
	@JsonDeserialize(builder=IncludeFilters.IncludeFiltersBuilder.class)
	public static class IncludeFilters{
	
		private Boolean includeUnavailableCandidates;
		private Boolean includeRequiresSponsorshipCandidates; 	
		
		/**
		* Constructor 
		* @param builder - Contains initialization values
		*/
		public IncludeFilters(IncludeFiltersBuilder builder) {
			this.includeUnavailableCandidates 			= builder.includeUnavailableCandidates;
			this.includeRequiresSponsorshipCandidates 	= builder.includeRequiresSponsorshipCandidates;
		}
	
		/**
		* If applicable returns whether to include candidates 
		* not currently available
		* @return whether to include candidates not currently available
		*/
		public Optional<Boolean> includeUnavailableCandidates(){
			return Optional.ofNullable(this.includeUnavailableCandidates);
		}
		
		/**
		* If applicable returns whether or not to include candidates 
		* requiring sponsorship
		* @return whether or not to include candidates requiring sponsorship
		*/
		public Optional<Boolean> includeRequiresSponsorshipCandidates(){
			return Optional.ofNullable(this.includeRequiresSponsorshipCandidates);
		} 	
		
		/**
		* Returns a builder for the class
		*/
		public static IncludeFiltersBuilder builder(){
			return new IncludeFiltersBuilder();
		}
		
		/**
		* Builder for the IncludeFiltters
		*/
		@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
		public static class IncludeFiltersBuilder{
		
			private Boolean includeUnavailableCandidates;
			private Boolean includeRequiresSponsorshipCandidates; 
			
			/**
			* Sets whether to include candidates that are not immediately available
			* @param includeUnavailableCandidates - include non available candidates
			* @return Builder
			*/
			public IncludeFiltersBuilder includeUnavailableCandidates(Boolean includeUnavailableCandidates) {
				this.includeUnavailableCandidates = includeUnavailableCandidates;
				return this;
			}
			
			/**
			* Sets whether to include candidates requiring sponsorship
			* @param includeRequiresSponsorshipCandidates - Include candidates requiring sponsorship
			* @return Builder
			*/
			public IncludeFiltersBuilder includeRequiresSponsorshipCandidates(Boolean includeRequiresSponsorshipCandidates) {
				this.includeRequiresSponsorshipCandidates = includeRequiresSponsorshipCandidates;
				return this;
			} 
			
			/**
			* Return Instance of class
			* @return Initialized instance
			*/
			public IncludeFilters build() {
				return new IncludeFilters(this);
			}
			
		}
	
	}
	
	/**
	* Filters relating to search terms of various types
	*/
	@JsonDeserialize(builder=TermFilters.TermFiltersBuilder.class)
	public static class TermFilters{
		
		private String title; 
		private String candidateId;
		private String firstName;
		private String surname;
		private String email;	
	
		/**
		* Constructor based upon a Builder
		* @param builder - Contains initialization values
		*/
		public TermFilters(TermFiltersBuilder builder) {
			this.title			= builder.title; 
			this.candidateId 	= builder.candidateId;
			this.firstName 		= builder.firstName;
			this.surname 		= builder.surname;
			this.email 			= builder.email;	
		}
		
		/**
		* Returns the job title to filter on
		* @return Job title
		*/
		public Optional<String> getTitle() {
			return Optional.ofNullable(this.title);
		} 
		
		/**
		* Returns the Id of the candidate to filter n
		* @return
		*/
		public Optional<String> getCandidateId() {
			return Optional.ofNullable(this.candidateId);
		}
		
		/**
		* Returns the first name of a Candidate to filter on
		* @return firstname to filter on
		*/
		public Optional<String> getFirstName() {
			
			if (Optional.ofNullable(this.firstName).isEmpty()) {
				return Optional.empty();
			}
			
			return this.firstName.length() == 0 ? Optional.empty() : Optional.ofNullable(this.firstName);
			
		}
		
		/**
		* Returns the Surname of a Candidate to filter on
		* @return Surname to filter on
		*/
		public Optional<String> getSurname() {
			
			if (Optional.ofNullable(this.surname).isEmpty()) {
				return Optional.empty();
			}
			
			return this.surname.length() == 0 ? Optional.empty() : Optional.ofNullable(this.surname);
			
		}
		
		/**
		* Returns a Candidates email address to filter on
		* @return Email to filter on
		*/
		public Optional<String> getEmail() {
			return Optional.ofNullable(this.email);
		}	
	
		/**
		* Returns a builder for the class
		* @return Builder
		*/
		public static TermFiltersBuilder builder(){
			return new TermFiltersBuilder();
		}
		
		/**
		* Builder for the class 
		*/
		@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
		public static class TermFiltersBuilder{
			
			public  String title; 
			private String candidateId;
			private String firstName;
			private String surname;
			private String email;	
			
			/**
			* Sets the job title to filter on
			* @param title - job title to filter on
			* @return Builder
			*/
			public TermFiltersBuilder title(String title) {
				this.title = title;
				return this;
			} 
			
			/**
			* Sets the Candidates unique Id to filter on
			* @param candidateId - Candidate Id to filter on
			* @return Builder
			*/
			public TermFiltersBuilder candidateId(String candidateId) {
				this.candidateId = candidateId;
				return this;
			}
			
			/**
			* Sets the Candidates first name to filter on
			* @param firstName - First name of the Candidate
			* @return Builder
			*/
			public TermFiltersBuilder firstName(String firstName) {
				this.firstName = firstName;
				return this;
			}
			
			/**
			* Sets the surname of the Candidate to filter on
			* @param surname - surname of the Candidate
			* @return Builder
			*/
			public TermFiltersBuilder surname(String surname) {
				this.surname = surname;
				return this;
			}
			
			/**
			* Sets the email address of the Candidate to filter on
			* @param email - Email address of the Candidate
			* @return Builder
			*/
			public TermFiltersBuilder email(String email) {
				this.email = email;
				return this;
			}
			
			/**
			* Returns initialized instance of class
			* @return instance of class
			*/
			public TermFilters build() {
				return new TermFilters(this);
			}
			
		}
	}
	
	@JsonDeserialize(builder=SecurityFilters.SecurityFiltersBuilder.class)
	public static class SecurityFilters { 
		
		private Set<SECURITY_CLEARANCE_TYPE> securityLevels = new HashSet<>();
		
		/**
		* Constructor based on a builder
		* @param builder - Contains initialization values
		*/
		public SecurityFilters(SecurityFiltersBuilder builder) {
			this.securityLevels.addAll(builder.securityLevels);
		}
		
		/**
		* Returns the security levels to filter on if any
		* @return Security levels
		*/
		public Set<SECURITY_CLEARANCE_TYPE> getSecurityLevels() {
			return this.securityLevels;
		}
		
		/**
		* Returns an instance of the builder for the class
		* @return Builder
		*/
		public static SecurityFiltersBuilder builder() {
			return new SecurityFiltersBuilder();
		}
		
		/**
		* Builder for the calss 
		*/
		@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
		public static class SecurityFiltersBuilder {
			
			private Set<SECURITY_CLEARANCE_TYPE> securityLevels = new HashSet<>();
		
			/**
			* Sets Security levels to filter on
			* @param securityLevels - security levels 
			* @return Builder
			*/
			public SecurityFiltersBuilder securityLevels(Set<SECURITY_CLEARANCE_TYPE> securityLevels) {
				this.securityLevels.clear();
				this.securityLevels.addAll(securityLevels);
				return this;
			}
			
			/**
			* Returns an initialized instance of the class
			* @return Initialized instance
			*/
			public SecurityFilters build() {
				return new SecurityFilters(this);
			}
			
		}
		
	}
	
	/**
	* Filters relating to types of candidates to filter on
	*/
	@JsonDeserialize(builder=CandidateFilters.CandidateFiltersBuilder.class)
	public static class CandidateFilters{
		
		private Boolean 	available;
		private String 		ownerId;
		private Set<String>	candidateIds						= new HashSet<>();
		private Integer 	daysSinceLastAvailabilityCheck;
		
		/**
		* Default constructor 
		*/
		public CandidateFilters() {
			//Jackson
		}
		
		/**
		* Constructor based upon a Builder 
		* @param builder - Contains initialization values
		*/
		public CandidateFilters(CandidateFiltersBuilder builder) {
			this.available 						= builder.available;
			this.ownerId 						= builder.ownerId;
			this.daysSinceLastAvailabilityCheck = builder.daysSinceLastAvailabilityCheck;
		
			this.candidateIds.clear();
			this.candidateIds.addAll(builder.candidateIds);
			
		}
		
		/**
		* Returns if applicable the availability of the Candidate 
		* to filter on
		* @return availability of the Candidate
		*/
		public Optional<Boolean> isAvailable(){
			return Optional.ofNullable(this.available);
		}
		
		/**
		* Returns if applicable the recruiter owning the Candidate
		* to filter on
		* @return Id of the owning Recruiter
		*/
		public Optional<String> getOwnerId(){
			return Optional.ofNullable(this.ownerId);
		}
		
		/**
		* Returns if applicable the candidate Id's
		* to filter on
		* @return Id's of the candidates
		*/
		public Set<String> getCandidateIds(){
			return this.candidateIds;
		}
		
		/**
		* Returns is applicable number of days since last availability 
		* check was performed to filter on
		* @return days since last check
		*/
		public Optional<Integer> getDaysSinceLastAvailabilityCheck(){
			return Optional.ofNullable(this.daysSinceLastAvailabilityCheck);
		}
		
		/**
		* Returns a builder for the calss
		* @return Builder
		*/
		public static CandidateFiltersBuilder builder() {
			return new CandidateFiltersBuilder();
		}
		
		/**
		* Builder for the clas 
		*/
		@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
		public static class CandidateFiltersBuilder{
			
			private Boolean 		available;
			private String 			ownerId;
			private Set<String>  	candidateIds						= new HashSet<>();
			private Integer 		daysSinceLastAvailabilityCheck;
			
			/**
			* Sets whether to filter specifically on available/unavailable
			* Candidates
			* @param available - Availability option
			* @return Builder
			*/
			public CandidateFiltersBuilder available(Boolean available) {
				this.available = available;
				return this;
			}
			
			/**
			* Sets whether to filter on Candidates belonging to a 
			* specific recruiter
			* @param ownerId - unique Id of the owning Recruiter
			* @return Builder
			*/
			public CandidateFiltersBuilder ownerId(String ownerId) {
				this.ownerId = ownerId;
				return this;
			}
			
			/**
			* Sets where applicable the Id's of the candidates to filter n
			* @param candidateIds - Candidate Id's
			* @return Builder
			*/
			public CandidateFiltersBuilder candidateIds(Set<String> candidateIds) {
				this.candidateIds.clear();
				this.candidateIds.addAll(candidateIds);
				return this;
			}
			
			/**
			* Sets filter to only include Candidates that have not had an 
			* availability check since the specific number of days ago
			* @param daysSinceLastAvailabilityCheck - Days since last check
			* @return Builder
			*/
			public CandidateFiltersBuilder daysSinceLastAvailabilityCheck(Integer daysSinceLastAvailabilityCheck) {
				this.daysSinceLastAvailabilityCheck = daysSinceLastAvailabilityCheck;
				return this;
			}
			
			/**
			* Returns initialized instance  
			* @return Initialized instance
			*/
			public CandidateFilters build() {
				return new CandidateFilters(this);
			}
		}
		
	}
	
	/**
	* Converts from API Inboud representation to internal filter
	* structure
	* @param req								- To be converted
	* @param orderAttribute						- Result order attribute
	* @param order								- Direction to filter results n
	* @param candidateIdFilters					- Candidate Id's to filter on
	* @param functions							- Functions to filter on
	* @param ownerId							- Owner Id to filter on
	* @param daysSinceLastAvailabilityCheck		- Days since candidate availability check
	* @return
	*/
	public static CandidateFilterOptions convertToCandidateFilterOptions(
			CandidateSearchRequest 	req, 
			String 					orderAttribute, 
			RESULT_ORDER 			order) {
		
		CandidateFilterOptionsBuilder builder = CandidateFilterOptions.builder();
		
		builder.orderAttribute(orderAttribute);
		builder.order(order);
		
		req.requestFilters.getMaxNumberOfSuggestions().ifPresent(builder::maxResults);
		
		req.candidateFilters().ifPresent(f -> {
			f.getOwnerId().ifPresent(builder::ownerId);
			f.isAvailable().ifPresent(builder::available);
			f.getDaysSinceLastAvailabilityCheck().ifPresent(builder::daysSinceLastAvailabilityCheck);
		});
		
		req.contractFilters().ifPresent(f -> {
			f.getContract().ifPresent(c 	-> builder.freelance(true));
			f.getPerm().ifPresent(p 		-> builder.perm(true));
		});
		
		req.experienceFilters().ifPresent(f -> f.getExperienceMax().ifPresent(builder::yearsExperienceLtEq));
		req.experienceFilters().ifPresent(f -> f.getExperienceMin().ifPresent(builder::yearsExperienceGtEq));
		
		builder.available(true);
		req.includeFilters().ifPresent(f ->{
			if (f.includeUnavailableCandidates().isPresent()) {
				builder.available(null);
			}
			f.includeRequiresSponsorshipCandidates().ifPresent(builder::includeRequiresSponsorship);
		});
		
		req.languageFilters().ifPresent(f -> 
			builder.languages(f.getLanguages())
		);
		
		req.locationFilters().ifPresent(f -> {
			builder.countries(f.getCountries());
			builder.geoZones(f.getGeoZones());
			f.getRange().ifPresent(loc ->{
				if (Optional.ofNullable(loc.country()).isPresent() &&  Optional.ofNullable(loc.city()).isPresent()) {
					builder.geoPosFilter(loc.country(), loc.city(), loc.distanceInKm());
				}
			} );
		});
		
		req.skillFilters().ifPresent(f -> builder.skills(f.getSkills()));
		
		req.termFilters().ifPresent(f -> {
			f.getEmail().ifPresent(builder::email);
			f.getFirstName().ifPresent(builder::firstname);
			f.getSurname().ifPresent(builder::surname);
			f.getTitle().ifPresent(builder::searchText);
			f.getCandidateId().ifPresent(id -> builder.candidateIds(Set.of(String.valueOf(""+id))));
		});
		
		req.candidateFilters().ifPresent(f -> {
			f.isAvailable().ifPresent(builder::available);
			f.getOwnerId().ifPresent(builder::ownerId);
			f.getDaysSinceLastAvailabilityCheck().ifPresent(builder::daysSinceLastAvailabilityCheck);
			builder.candidateIds(f.getCandidateIds());
		});
		
		req.securityFilters().ifPresent(s ->
			builder.securityLevels(s.getSecurityLevels())
		);
		
		return builder.build();
		
	}
	
}