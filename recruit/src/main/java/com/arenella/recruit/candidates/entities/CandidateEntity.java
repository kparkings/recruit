package com.arenella.recruit.candidates.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.CANDIDATE_TYPE;
import com.arenella.recruit.candidates.beans.Candidate.DAYS_ON_SITE;
import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Entity representation of a Candidate. A Candidate is 
* someone who is potentially open to Work.
* @author K Parkings
*/
@Entity
@Table(schema="candidate", name="candidate")
public class CandidateEntity {

	public static final String ANONYMOUS_DATA = "unknown"; 
	
	@Id
	@Column(name="candidate_id")
	private Long		candidateId;
	
	@Column(name="firstname")
	private String 		firstname;
	
	@Column(name="surname")
	private String 		surname;
	
	@Column(name="email")
	private String 		email;
	
	@Column(name="role_sought")
	private String roleSought;
	
	@Column(name="function")
	@Enumerated(EnumType.STRING)
	private FUNCTION function;
	
	@Column(name="country")
	@Enumerated(EnumType.STRING)
	private COUNTRY 	country;
	
	@Column(name="city")
	private String 		city;
	
	@Column(name="perm")
	@Enumerated(EnumType.STRING)
	private PERM 		perm;
	
	@Column(name="freelance")
	@Enumerated(EnumType.STRING)
	private FREELANCE 	freelance;
	
	@Column(name="years_experience")
	private int			yearsExperience;
	
	@Column(name="available")
	private boolean 	available;
	
	@Column(name="flagged_as_unavailable")
	private boolean flaggedAsUnavailable;
	
	@Column(name="registered")
	private LocalDate 	registerd;
	
	@Column(name="last_availability_check")
	private LocalDate 	lastAvailabilityCheck;
	
	@Column(name="introduction")
	private String introduction;
	
	@Enumerated(EnumType.STRING)
	@Column(name="rate_contract_currency")
	private CURRENCY rateContractCurrency;
		
	@Enumerated(EnumType.STRING)
	@Column(name="rate_contract_period")
	private PERIOD rateContractPeriod;
	    
	@Column(name="rate_contract_value_min")
	private float rateContractValueMin;   
	
	@Column(name="rate_contract_value_max")
	private float rateContractValueMax;
	
	@Enumerated(EnumType.STRING)
	@Column(name="rate_perm_currency")
	private CURRENCY ratePermCurrency;
		
	@Enumerated(EnumType.STRING)
	@Column(name="rate_perm_period")
	private PERIOD ratePermPeriod;
	    
	@Column(name="rate_perm_value_min")
	private float ratePermValueMin;   
	
	@Column(name="rate_perm_value_max")
	private float ratePermValueMax;
	
	@Column(name="available_from_date")
	private LocalDate 		availableFromDate;
	
	@Column(name="owner_id")
	private String 			ownerId;
	
	@Column(name="candidate_type")
	@Enumerated(EnumType.STRING)
	private CANDIDATE_TYPE	candidateType;
	
	@Column(name="comments")
	private String comments;
	
	@Column(name="days_on_site")
	@Enumerated(EnumType.STRING)
	private DAYS_ON_SITE daysOnSite;
    
	@Enumerated(EnumType.STRING)
    @Column(name="photo_format")
    private PHOTO_FORMAT photoFormat;      
    
    @Column(name="photo_bytes")
    private byte[] photoBytes;
       
	@ElementCollection(targetClass=String.class, fetch=FetchType.EAGER)
	@CollectionTable(schema="candidate", name="candidate_skill", joinColumns=@JoinColumn(name="candidate_id"))
	@Column(name="skill")
	private Set<String> 			skills						= new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "id.candidateId", cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	private Set<LanguageEntity> 	languages					= new LinkedHashSet<>();
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization parameters
	*/
	public CandidateEntity(CandidateEntityBuilder builder) {
		
		this.candidateId 				= builder.candidateId;
		this.firstname					= builder.firstname;
		this.surname					= builder.surname;
		this.email						= builder.email;
		this.roleSought					= builder.roleSought;
		this.function					= builder.function;
		this.country 					= builder.country;
		this.city 						= builder.city;
		this.perm 						= builder.perm;
		this.freelance 					= builder.freelance;
		this.yearsExperience 			= builder.yearsExperience;
		this.available 					= builder.available;
		this.registerd 					= builder.registerd;
		this.lastAvailabilityCheck 		= builder.lastAvailabilityCheck;
		this.flaggedAsUnavailable		= builder.flaggedAsUnavailable;
		this.introduction				= builder.introduction;
		
		this.rateContractCurrency 		= builder.rateContractCurrency;
		this.rateContractPeriod 		= builder.rateContractPeriod;
		this.rateContractValueMin 		= builder.rateContractValueMin;   
		this.rateContractValueMax 		= builder.rateContractValueMax;
		this.ratePermCurrency 			= builder.ratePermCurrency;
		this.ratePermPeriod 			= builder.ratePermPeriod;
		this.ratePermValueMin 			= builder.ratePermValueMin;   
		this.ratePermValueMax 			= builder.ratePermValueMax;
		
		this.photoFormat				= builder.photoFormat;      
		this.photoBytes					= builder.photoBytes;
		
		this.availableFromDate			= builder.availableFromDate;
		this.ownerId					= builder.ownerId;
		this.candidateType				= builder.candidateType;
		
		this.comments					= builder.comments;
		this.daysOnSite				 	= builder.daysOnSite;
		
		this.skills.addAll(builder.skills);
		this.languages.addAll(builder.languages.stream().map(lang -> LanguageEntity.builder().candidate(this).language(lang.getLanguage()).level(lang.getLevel()).build()).collect(Collectors.toSet()));

	}
	
	/**
	* Default constructor 
	*/
	@SuppressWarnings("unused")
	private CandidateEntity() {
		//Hibernate
	}
	
	/**
	* Returns the unique identifier of the Candidate
	* @return unique Id of the candidate
	*/
	public long getCandidateId() {
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
	* Returns the name of the Role sought by the Candidate
	* @return Role Sought
	*/
	public String getRoleSought() {
		return this.roleSought;
	}
	
	/**
	* Returns the function the Candidate is 
	* searching for
	* @return what the candidate does
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
	* Returns whether or not the Candidate has been Flagged as
	* being unavailable 
	* @return whether the Canidate is potentially unavailable
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
	* Returns the Skills the Candidate has experience with
	* @return Candidates Skills
	*/
	public Set<String> getSkills(){
		return this.skills;
	}
	
	/**
	* Returns the Languages spoken by the Candidate
	* @return Languages spoken by the Candidate
	*/
	public Set<LanguageEntity> getLanguages(){
		return this.languages;
	}
	
	/**
	* Return the Date the Candidate is available to begin from
	* @return first possible begin date
	*/
	public LocalDate getAvailableFromDate() {
		return this.availableFromDate;
	}
	
	/**
	* Returns the id of the party that is representing the 
	* Candidate if a party is representing the candidate
	* @return id of owner
	*/
	public Optional<String> getOwnerId(){
		return Optional.ofNullable(this.ownerId);
	}
	
	/**
	* Returns the type of the Candidate
	* @return type of the Candidate
	*/
	public CANDIDATE_TYPE getCandidateType() {
		return this.candidateType;
	}
	
	/**
	* Returns comments relating to the Candidate
	* @return comments about the Candidate
	*/
	public String getComments(){
		return this.comments;
	}
	
	/**
	* Returns max number of days the Candidate will work onsite
	* @return max number of days onsite
	*/
	public DAYS_ON_SITE getDaysOnSite(){
		return this.daysOnSite;
	}
	
	/**
	* Updates the availability of the Candidate
	* @param available - Whether or not the Candidate is available
	*/
	public void setAvailable(boolean available) {
		
		this.available = available;
		
		if (!available) {
			this.firstname 	= ANONYMOUS_DATA;
			this.surname 	= ANONYMOUS_DATA;
			this.email 		= ANONYMOUS_DATA;
		}
		
	}
	
	/**
	* Sets whether or not someone has marked the Candidate as 
	* being unavailable
	* @param flaggedAsUnavailable - Whether Candidate is flagged as being unavailable
	*/
	public void setFlaggedAsUnavailable(boolean flaggedAsUnavailable) {
		this.flaggedAsUnavailable = flaggedAsUnavailable;
	}
	
	/**
	* Updates the last time the Candidate had their availability checked
	*/
	public void setCandidateAvailabilityChecked() {
		this.lastAvailabilityCheck 	= LocalDate.now();
		this.available 				= true;
	}
	
	/**
	* Returns the Candidates introduction about themselves
	* @return intro
	*/
	public String getIntroduction() {
		return this.introduction;
	}
	
	/**
	* Returns the Currency for the contract rate
	* @return Currency for the contract rate
	*/
	public CURRENCY getRateContractCurrency() {
		return this.rateContractCurrency;
	}
	
	/**
	* Returns the Period for the Contract Rate
	* @return Period for the Contract Rate
	*/
	public PERIOD getRateContractPeriod() {
		return this.rateContractPeriod;
	}
	
	/**
	* Returns the min contract rate
	* @return min contract rate
	*/
	public float getRateContractValueMin() {
		return this.rateContractValueMin;
	}   
	
	/**
	* Returns the max contract rate
	* @return max contract rate
	*/
	public float getRateContractValueMax() {
		return this.rateContractValueMax;
	}
	
	/**
	* Returns the Currency for the perm salary
	* @return Currency for the perm salary
	*/
	public CURRENCY getRatePermCurrency() {
		return this.ratePermCurrency;
	}
	
	/**
	* Returns the Period for the Perm salary
	* @return Period for the Perm salary
	*/
	public PERIOD getRatePermPeriod() {
		return this.ratePermPeriod;
	}
	
	/**
	* Returns the min perm salart
	* @return min perm salary
	*/
	public float getRatePermValueMin() {
		return this.ratePermValueMin;
	}   
	
	/**
	* Returns the max perm salart
	* @return max perm salary
	*/
	public float getRatePermValueMax() {
		return this.ratePermValueMax;
	}
	
	/**
	* Returns the format of the photo
	* @return file format
	*/
	public PHOTO_FORMAT getPhotoFormat() {
		return this.photoFormat;
	}
	
	/**
	* Returns the bytes for the image file
	* @return bytes
	*/
	public byte[] getPhotoBytes() {
		return this.photoBytes;
	}
	
	/**
	* Returns a Builder for the CandidateEntity class
	* @return Builder for the CandidateEntity class
	*/
	public static CandidateEntityBuilder builder() {
		return new CandidateEntityBuilder();
	}
	
	/**
	* Builder for the CandidateEntity class
	* @author K Parkings
	*/
	public static class CandidateEntityBuilder {
	
		private Long 			candidateId;
		private String 			firstname;
		private String			surname;
		private String 			email;
		private String			roleSought;
		private FUNCTION		function;
		private COUNTRY 		country;
		private String 			city;
		private PERM	 		perm;
		private FREELANCE 		freelance;
		private int				yearsExperience;
		private boolean 		available;
		private LocalDate 		registerd					= LocalDate.now();				//Set app to work with UAT
		private LocalDate 		lastAvailabilityCheck		= LocalDate.now();
		private boolean			flaggedAsUnavailable;
		private String 			introduction;
		private CURRENCY 		rateContractCurrency;
		private PERIOD 			rateContractPeriod;
		private float 			rateContractValueMin;   
		private float 			rateContractValueMax;
		private CURRENCY 		ratePermCurrency;
		private PERIOD 			ratePermPeriod;
		private float 			ratePermValueMin;   
		private float 			ratePermValueMax;
		
		private PHOTO_FORMAT 	photoFormat;      
		private byte[] 			photoBytes;
		
		private LocalDate 		availableFromDate;
		private String 			ownerId;
		private CANDIDATE_TYPE	candidateType;
		
		private String comments;
		private DAYS_ON_SITE daysOnSite;
		
		private Set<String> 	skills						= new LinkedHashSet<>();
		private Set<Language> 	languages					= new LinkedHashSet<>();
	
		/**
		* Sets the Unique Identifier of the Candidate
		* @param candidateId - UniqueId of the Candidate
		* @return Builder
		*/
		public CandidateEntityBuilder candidateId(String candidateId) {
			this.candidateId = Long.valueOf(candidateId);
			return this;
		}
		
		/**
		* Sets the First name of the Candidate
		* @param firstname - Candidates first name
		* @return Builder
		*/
		public CandidateEntityBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the Surname of the Candidate
		* @param surname - Candidates Surname
		* @return Builder
		*/
		public CandidateEntityBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Email address of the Candidate
		* @param email - Email address
		* @return Builder
		*/
		public CandidateEntityBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the name of the Role sought by the Candidate
		* @param roleSought - role sought by the Candidate
		* @return Builder
		*/
		public CandidateEntityBuilder roleSought(String roleSought) {
			this.roleSought = roleSought;
			return this;
		}
		
		/**
		* Sets the Function performed by the Candidate
		* @param function - Function performed by the Candidate
		* @return Builder
		*/
		public CandidateEntityBuilder function(FUNCTION function) {
			this.function = function;
			return this;
		}
		
		/**
		* Sets the Country where the Candidate is located
		* @param country - Name of the Country
		* @return Builder
		*/
		public CandidateEntityBuilder country(COUNTRY country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets the City where the Candidate is located
		* @param city - Name of the City
		* @return Builder
		*/
		public CandidateEntityBuilder city(String city) {
			this.city = city;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for Perm roles
		* @param perm - Whether candidate is looking for Perm roles
		* @return Builder
		*/
		public CandidateEntityBuilder perm(PERM perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param perm - Whether candidate is looking for freelance roles
		* @return Builder
		*/
		public CandidateEntityBuilder freelance(FREELANCE freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Sets the number of years experience the Candidate has in the Industry
		* @param yearsOfExperience - number of years of experience
		* @return Builder
		*/
		public CandidateEntityBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets whether or not the Candidate is currently looking for work
		* @param available - Whether the Candidate is available for work
		* @return Builder
		*/
		public CandidateEntityBuilder available(boolean available) {
			this.available = available;
			return this;
		}
		
		/**
		* Sets whether or not the Candidate has been marked as being 
		* potentially unavailable
		* @param flaggedAsUnavailable - Whether the Candidate is potentially unavailable
		* @return Builder
		*/
		public CandidateEntityBuilder flaggedAsUnavailable(boolean flaggedAsUnavailable) {
			this.flaggedAsUnavailable = flaggedAsUnavailable;
			return this;
		}
		
		/**
		* Sets the Date the Candidate was registered in the System
		* @param registerd - date of registration
		* @return Builder
		*/
		public CandidateEntityBuilder registerd(LocalDate registerd) {
			this.registerd = registerd;
			return this;
		}
		
		/**
		* Sets the Candidates introduction about themselves
		* @param introduction - Candiadate's introduction
		* @return Builder
		*/
		public CandidateEntityBuilder introduction(String introduction) {
			this.introduction = introduction;
			return this;
		}
		
		/**
		* Sets the format of the Candidates Profile Photo
		* @param photoFormat - file format
		* @return Builder
		*/
		public CandidateEntityBuilder photoFormat(PHOTO_FORMAT photoFormat) {
			this.photoFormat = photoFormat;
			return this;
		}
		
		/**
		* Sets the currency to use 
		* @param rateContractCurrency - contract currency
		* @return Builder
		*/
		public CandidateEntityBuilder rateContractCurrency(CURRENCY rateContractCurrency) {
			this.rateContractCurrency = rateContractCurrency;
			return this;
		}
		
		/**
		* Sets the Rate period
		* @param rateContractPeriod - Rate period for contract
		* @return Builder
		*/
		public CandidateEntityBuilder rateContractPeriod(PERIOD rateContractPeriod) {
			this.rateContractPeriod = rateContractPeriod;
			return this;
		}
		
		/**
		* Sets the min contract rate
		* @param rateContractValueFrom - Min contract rate
		* @return Builder
		*/
		public CandidateEntityBuilder rateContractValueMin(float rateContractValueMin) {
			this.rateContractValueMin = rateContractValueMin;
			return this;
		}   
		
		/**
		* Sets the max contract rate
		* @param rateContractValueTo - Max contract rate
		* @return Builder
		*/
		public CandidateEntityBuilder rateContractValueMax(float rateContractValueMax) {
			this.rateContractValueMax = rateContractValueMax;
			return this;
		}
		
		/**
		* Sets the currency to use 
		* @param ratePermCurrency - perm currency
		* @return Builder
		*/
		public CandidateEntityBuilder ratePermCurrency(CURRENCY ratePermCurrency) {
			this.ratePermCurrency = ratePermCurrency;
			return this;
		}
		
		/**
		* Sets the Rate period
		* @param rateContractPeriod - Contract rate period
		* @return Builder
		*/
		public CandidateEntityBuilder ratePermPeriod(PERIOD ratePermPeriod) {
			this.ratePermPeriod = ratePermPeriod;
			return this;
		}
		
		/**
		* Sets the min perm salary
		* @param ratePermValueFrom - min perm salary
		* @return Builder
		*/
		public CandidateEntityBuilder ratePermValueMin(float ratePermValueMin) {
			this.ratePermValueMin = ratePermValueMin;
			return this;
		}   
		
		/**
		* Sets the max perm salary
		* @param ratePermValueTo - max perm salary
		* @return Builder
		*/
		public CandidateEntityBuilder ratePermValueMax(float ratePermValueMax) {
			this.ratePermValueMax = ratePermValueMax;
			return this;
		}
		
		/**
		* Sets the bytes of the Candidates profile photo
		* @param photoBytes - bytes of image
		* @return Builder
		*/
		public CandidateEntityBuilder photoBytes(byte[] photoBytes) {
			this.photoBytes = photoBytes;
			return this;
		}
		
		/**
		* Sets the Date of the last check performed to check if the Candidate is still
		* available
		* @param lastAvailabilityCheck - Date of last availability check performed
		* @return Builder
		*/
		public CandidateEntityBuilder lastAvailabilityCheck(LocalDate lastAvailabilityCheck) {
			this.lastAvailabilityCheck = lastAvailabilityCheck;
			return this;
		}
		
		/**
		* Sets the skills that the Candidate has
		* @param skills - Skills Candidate has experience with
		* @return Builder
		*/
		public CandidateEntityBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Sets the languages spoken by the Candidate
		* @param languages - Languages spoken by the Candidate
		* @return Builder
		*/
		public CandidateEntityBuilder languages(Set<Language> languages) {
			this.languages.clear();
			this.languages.addAll(languages);
			return this;
		}
		
		/**
		* Sets when the Candidate is available from
		* @param availableFromDate - When the candidate will be available
		* @return Builder
		*/
		public CandidateEntityBuilder availableFromDate(LocalDate availableFromDate) {
			this.availableFromDate = availableFromDate;
			return this;
		}
		
		/**
		* Sets the id of the owner of the Candidate if the candidate is offered via
		* another party
		* @param ownerId - Id of owner
		* @return Builder
		*/
		public CandidateEntityBuilder ownerId(String ownerId) {
			this.ownerId = ownerId;
			return this;
		}
		
		/**
		* Sets the type of the Candidate
		* @param candidateType - type of candidate
		* @return
		*/
		public CandidateEntityBuilder candidateType(CANDIDATE_TYPE	candidateType) {
			this.candidateType = candidateType;
			return this;
		}
		
		/**
		* Sets comments relating to the Candidate
		* @param comments
		* @return Builder
		*/
		public CandidateEntityBuilder comments(String comments) {
			this.comments = comments;
			return this;
		}
		
		/**
		* Sets the Max number of days on site the Candidate is prepared to work
		* @param daysOnSite - Max number of days onsite
		* @return Builder
		*/
		public CandidateEntityBuilder daysOnSite(DAYS_ON_SITE daysOnSite) {
			this.daysOnSite = daysOnSite;
			return this;
		}
		
		/**
		* Returns a CandidateEntity instance initialized with 
		* the values in the Builder
		* @return Instance of CandidateEntity
		*/
		public CandidateEntity build() {
			return new CandidateEntity(this);
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
						.flaggedAsUnavailable(candidate.isFlaggedAsUnavailable())
						.candidateId(candidate.getCandidateId())
						.firstname(candidate.getFirstname())
						.surname(candidate.getSurname())
						.email(candidate.getEmail())
						.roleSought(candidate.getRoleSought())
						.function(candidate.getFunction())
						.city(candidate.getCity())
						.country(candidate.getCountry())
						.freelance(candidate.isFreelance())
						.lastAvailabilityCheck(candidate.getLastAvailabilityCheckOn())
						.perm(candidate.isPerm())
						.registerd(candidate.getRegisteredOn())
						.yearsExperience(candidate.getYearsExperience())
						.skills(candidate.getSkills().stream().map(String::trim).map(String::toLowerCase).collect(Collectors.toSet()))
						.languages(candidate.getLanguages())
						.introduction(candidate.getIntroduction())
						.photoFormat(candidate.getPhoto().isEmpty() ? null 	: candidate.getPhoto().get().getFormat())
						.photoBytes(candidate.getPhoto().isEmpty() 	? null 	: candidate.getPhoto().get().getImageBytes())
						.rateContractCurrency(candidate.getRateContract().isEmpty() ? null 	: candidate.getRateContract().get().getCurrency())
						.rateContractPeriod(candidate.getRateContract().isEmpty() ? null 	: candidate.getRateContract().get().getPeriod())
						.rateContractValueMin(candidate.getRateContract().isEmpty() ? 0f 	: candidate.getRateContract().get().getValueMin())
						.rateContractValueMax(candidate.getRateContract().isEmpty() ? 0f 	: candidate.getRateContract().get().getValueMax())
						.ratePermCurrency(candidate.getRatePerm().isEmpty() ? null 	: candidate.getRatePerm().get().getCurrency())
						.ratePermPeriod(candidate.getRatePerm().isEmpty() ? null 	: candidate.getRatePerm().get().getPeriod())
						.ratePermValueMin(candidate.getRatePerm().isEmpty() ? 0f 	: candidate.getRatePerm().get().getValueMin())
						.ratePermValueMax(candidate.getRatePerm().isEmpty() ? 0f 	: candidate.getRatePerm().get().getValueMax())
						.availableFromDate(candidate.getAvailableFromDate())
						.ownerId(candidate.getOwnerId().isPresent() ? candidate.getOwnerId().get() : null)
						.candidateType(candidate.getCandidateType())
						.comments(candidate.getComments())
						.daysOnSite(candidate.getDaysOnSite())
					.build();
		
	}

	/**
	* Converts a Entity representation of a Candidate Object to an Domain 
	* representation
	* @param candidate - Instance of Candidate to convert
	* @return Domain representation of a Candidate object
	*/
	public static Candidate convertFromEntity(CandidateEntity candidateEntity) {
		
		Rate 	rateContract 	= null;
		Rate 	ratePerm 		= null;
		Photo 	photo 			= null;
		
		if (candidateEntity.rateContractCurrency != null && candidateEntity.rateContractPeriod != null) {
			rateContract = new Rate(candidateEntity.getRateContractCurrency(), candidateEntity.getRateContractPeriod(), candidateEntity.getRateContractValueMin(), candidateEntity.getRateContractValueMax());
		}
		
		if (candidateEntity.ratePermCurrency != null && candidateEntity.ratePermPeriod != null) {
			ratePerm = new Rate(candidateEntity.getRatePermCurrency(), candidateEntity.getRatePermPeriod(), candidateEntity.getRatePermValueMin(), candidateEntity.getRatePermValueMax());
		}
		
		if (candidateEntity.getPhotoBytes() != null && candidateEntity.getPhotoFormat() != null) {
			photo = new Photo(candidateEntity.getPhotoBytes(), candidateEntity.getPhotoFormat());
		}
		
		return Candidate
					.builder()
						.available(candidateEntity.isAvailable())
						.flaggedAsUnavailable(candidateEntity.isFlaggedAsUnavailable())
						.candidateId(String.valueOf(candidateEntity.getCandidateId()))
						.firstname(candidateEntity.getFirstname())
						.surname(candidateEntity.getSurname())
						.email(candidateEntity.getEmail())
						.roleSought(candidateEntity.getRoleSought())
						.function(candidateEntity.getFunction())
						.city(candidateEntity.getCity())
						.country(candidateEntity.getCountry())
						.freelance(candidateEntity.isFreelance())
						.lastAvailabilityCheck(candidateEntity.getLastAvailabilityCheckOn())
						.perm(candidateEntity.isPerm())
						.registerd(candidateEntity.getRegisteredOn())
						.yearsExperience(candidateEntity.getYearsExperience())
						.skills(candidateEntity.getSkills())
						.introduction(candidateEntity.getIntroduction())
						.rateContract(rateContract)
						.ratePerm(ratePerm)
						.photo(photo)
						.languages(candidateEntity.getLanguages().stream().map(lang -> Language.builder().language(lang.getLanguage()).level(lang.getLevel()).build()).collect(Collectors.toCollection(HashSet::new)))
						.availableFromDate(candidateEntity.getAvailableFromDate())
						.ownerId(candidateEntity.getOwnerId().isPresent() ? candidateEntity.getOwnerId().get() : null)
						.candidateType(candidateEntity.getCandidateType())
						.comments(candidateEntity.getComments())
						.daysOnSite(candidateEntity.getDaysOnSite())
						.availableFromDate(candidateEntity.getAvailableFromDate())
					.build();
		
	}
	
}