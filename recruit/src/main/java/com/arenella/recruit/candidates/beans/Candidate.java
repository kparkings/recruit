package com.arenella.recruit.candidates.beans;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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

	public enum DAYS_ON_SITE 				{ZERO,ONE,TWO,THREE,FOUR,FIVE}
	public enum CANDIDATE_TYPE 				{CANDIDATE, MARKETPLACE_CANDIDATE}
	public enum SECURITY_CLEARANCE_TYPE 	{NONE, SC, DV, NATO}
	
	public static final String ANONYMOUS_USER_ATTR_VALUE = "unknown";
			
	private String 						candidateId;
	private String 						firstname;
	private String 						surname;
	private String 						email;
	private String						roleSought;
	private Set<FUNCTION>				functions					= new LinkedHashSet<>();
	private COUNTRY 					country;
	private String 						city;
	private double						latitude;
	private double						longitude;
	private PERM 						perm;
	private FREELANCE 					freelance;
	private int							yearsExperience;
	private boolean 					available;
	private boolean						flaggedAsUnavailable;
	private LocalDate 					registerd					= LocalDate.now();
	private LocalDate 					lastAvailabilityCheck		= LocalDate.now();	
	private Set<String> 				skills						= new LinkedHashSet<>();
	private Set<Language> 				languages					= new LinkedHashSet<>();
	private String						introduction;
	private Photo						photo;
	
	private String 						comments;
	private DAYS_ON_SITE				daysOnSite;
	private Rate						rateContract;
	private Rate						ratePerm;
	private LocalDate 					availableFromDate;
	//To be set if MP candidate based upon authenticated user via service
	private String 						ownerId;
	private CANDIDATE_TYPE				candidateType;
	private boolean 					requiresSponsorship;
	private SECURITY_CLEARANCE_TYPE 	securityClearance;
	private LocalDate					lastAccountRefresh;
	private LocalDate					lastAvailabilityCheckEmailSent;
	private UUID 						lastAvailabilityCheckIdSent;
	private LocalDate 					lastAvailabilityCheckConfirmedOn;
	
	/**
	* Constructor based upon a builder
	* @param builder = Contains initialization information
	*/
	public Candidate(CandidateBuilder builder) {
		
		this.candidateId						= builder.candidateId;
		this.firstname							= builder.firstname;
		this.surname							= builder.surname;
		this.email								= builder.email;
		this.roleSought							= builder.roleSought;
		this.country							= builder.country;
		this.city 								= builder.city;
		this.longitude							= builder.longitude;
		this.latitude							= builder.latitude;
		this.perm 								= builder.perm;
		this.freelance 							= builder.freelance;
		this.yearsExperience 					= builder.yearsExperience;
		this.available 							= builder.available;
		this.flaggedAsUnavailable				= builder.flaggedAsUnavailable;
		this.registerd 							= builder.registerd;
		this.lastAvailabilityCheck 				= builder.lastAvailabilityCheck;
		this.introduction						= builder.introduction;
		this.photo								= builder.photo;
		this.comments							= builder.comments;
		this.daysOnSite							= builder.daysOnSite;
		this.rateContract						= builder.rateContract;
		this.ratePerm							= builder.ratePerm;
		this.availableFromDate					= builder.availableFromDate;
		this.ownerId							= builder.ownerId;
		this.candidateType						= builder.candidateType;
		this.requiresSponsorship				= builder.requiresSponsorship;
		this.securityClearance		 			= builder.securityClearance;
		this.lastAccountRefresh					= builder.lastAccountRefresh;
		this.lastAvailabilityCheckEmailSent 	= builder.lastAvailabilityCheckEmailSent;
		this.lastAvailabilityCheckIdSent 		= builder.lastAvailabilityCheckIdSent;
		this.lastAvailabilityCheckConfirmedOn 	= builder.lastAvailabilityCheckConfirmedOn;
		
		this.skills.addAll(builder.skills);
		this.languages.addAll(builder.languages);
		this.functions.addAll(builder.functions);
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
	* specific description of the users role that provided by the functions 
	* attribute
	* @return Role sought my the Candidate
	*/
	public String getRoleSought() {
		return this.roleSought;
	}
	
	/**
	* Returns the functions the Candidate performs
	* @return function the Candidate performs
	*/
	public Set<FUNCTION> getFunctions() {
		return this.functions;
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
	* Returns the Longitude for the position of the 
	* Candidates City
	* @return longitude position
	*/
	public double getLongitude() {
		return this.longitude;
	}
	
	/**
	* Returns the Latitude for the position of the 
	* Candidates City
	* @return latitude position
	*/
	public double getLatitude() {
		return this.latitude;
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
	* Returns comments relating to the Candidate
	* @return Comment about the candidate
	*/
	public String getComments() {
		return this.comments;
	}
	
	/**
	* Returns the introduction to the Candidate
	* @return candidate introduction text
	*/
	public String getIntroduction() {
		return this.introduction;
	}
	
	/**
	* Returns the max number of days the candidate is prepared
	* to work onsite
	* @return Number of days Candidate will work onsite
	*/
	public DAYS_ON_SITE	getDaysOnSite() {
		return this.daysOnSite;
	}
	
	/**
	* Returns the unique id of the Owner of the Candidate if the Candidate 
	* is associated with another entity such as a recruiter
	* @return id of owner
	*/
	public Optional<String> getOwnerId() {
		return Optional.ofNullable(this.ownerId);
	}
	
	/**
	* Returns the type/flavour of the Candidate
	* @return type of Candidate
	*/
	public CANDIDATE_TYPE candidateType() {
		return this.candidateType;
	}
	
	/**
	* If available the Contract Rate
	* @return Contract Rate
	*/
	public Optional<Rate> getRateContract(){
		return Optional.ofNullable(this.rateContract);
	}
	
	/**
	* If available Perm rate the Candidate is 
	* looking for
	* @return Perm rate
	*/
	public Optional<Rate> getRatePerm(){
		return Optional.ofNullable(this.ratePerm);
	}
	
	/**
	* returns the date the Candidate is available from. If not specified 
	* uses the current date
	* @return When the candidate is available from
	*/
	public LocalDate getAvailableFromDate(){
		return Optional.ofNullable(this.availableFromDate).orElse(LocalDate.now());
	}
	
	/**
	* Returns the type of the Candidate
	* @return - Type of the Candidate
	*/
	public CANDIDATE_TYPE getCandidateType() {
		return this.candidateType;
	}
	
	/**
	* Returns the Canidate's level of security Clearance
	* @return Candidates level of Security Clearance
	*/
	public SECURITY_CLEARANCE_TYPE getSecurityClearance() {
		return this.securityClearance;
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
	* If available returns a photo of the Candidate
	* @return Profile Photo
	*/
	public Optional<Photo> getPhoto(){
		return Optional.ofNullable(this.photo);
	}
	
	/**
	* Returns whether the Candidate requires sponsorship to take 
	* on a new role
	* @return Whether the Candidate requires sponsorship
	*/
	public boolean getRequiresSponsorship() {
		return this.requiresSponsorship;
	}
	
	/**
	* Returns the Date the Candidate account was last refreshed
	* @return Date of last refresh
	*/
	public LocalDate getLastAccountRefresh() {
		return Optional.ofNullable(this.lastAccountRefresh).isEmpty() ? (Optional.ofNullable(this.registerd).isEmpty() ?  LocalDate.now() : this.registerd ) : lastAccountRefresh;
	}
	
	/**
	* Returns the Date of the last time that an email was sent to the Candidate
	* asking them to confirm their availability
	* @return Date of last email
	*/
	public Optional<LocalDate> getLastAvailabilityCheckEmailSent(){
		return Optional.ofNullable(this.lastAvailabilityCheckEmailSent);
	}
	
	/**
	* Adds skills the Candidate posesses
	* @param skills - Skills
	*/
	public void addSkills(Set<String> skills) {
		this.skills.addAll(skills);
	}
	
	/**
	* Sets the introduction describing the Candidate
	* @param introduction - intro
	*/
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	/**
	* Sets the Candidates profile photo
	* @param photo - profile photo
	*/
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	
	/**
	* Sets the Contract rate the candidate wants
	* @param rateContract
	*/
	public void setRateContract(Rate rateContract) {
		this.rateContract = rateContract;
	}
	
	/**
	* Sets the Perm salary the Candidate wants
	* @param ratePerm
	*/
	public void setRatePerm(Rate ratePerm) {
		this.ratePerm = ratePerm;
	}
	
	/**
	* Sets the email address to contact the Candidate
	* could be recruiter email if candidate offered by 
	* recruiter 
	* @param email - email address
	*/
	public void setEmail(String email) {
		this.email = email;
		
	}
	
	/**
	* Sets the latitude of the City of the Candidate
	* @param latitude - candidates position
	*/
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	* Sets the longitude of the City of the Candidate
	* @param longitude - candidates position
	*/
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	* Sets the type of Candidate
	* @param marketplaceCandidate - type of Candidate
	*/
	public void setCandidateType(CANDIDATE_TYPE candidateType) {
		this.candidateType = candidateType;
		
	}

	/**
	* If the Candidate is offered via an Agency the id of the 
	* owner
	*/
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	/**
	* Manke the Candidates details anonymous and mark the candidate 
	* as no longer being available
	*/
	public void noLongerAvailable() {
		this.available 				= false;
		this.lastAvailabilityCheck 	= LocalDate.now();
	}
	
	/**
	* Makes the candidate available for be searched on 
	*/
	public void makeAvailable() {
		this.available = true;
		this.lastAvailabilityCheck 	= LocalDate.now();
	}
	
	/**
	* Updates the last time the Candidate had their availability checked
	*/
	public void setCandidateAvailabilityChecked() {
		this.lastAvailabilityCheck 	= LocalDate.now();
		this.available 				= true;
	}
	
	/**
	* Sets Date of the last time an email was sent to the Candidate asking them to 
	* confirm their availability
	* @param availabiiltyChkEmailDate - Date last email was sent
	*/
	public void setLastAvailabilityCheckEmailSent(LocalDate lastAvailabilityCheckEmailSent) {
		this.lastAvailabilityCheckEmailSent = lastAvailabilityCheckEmailSent;
	}
	
	/**
	* Returns the last ID ( And only currently authorized ) Id/Token for the
	* Candidate to confirm their availability
	* @return ID/Token to confirm availability of Candidate
	*/
	public Optional<UUID> getLastAvailabilityCheckIdSent() {
		return Optional.ofNullable(this.lastAvailabilityCheckIdSent);
	}
	
	/**
	* Returns the Date the Candidate last confirmed their availability 
	* @return Date the Candidate last confirmed their availability
	*/
	public Optional<LocalDate> getLastAvailabilityCheckConfirmedOn() {
		return Optional.ofNullable(this.lastAvailabilityCheckConfirmedOn);
	}
	
	/**
	* Sets the Date of the last time the Candidate account was Refreshed
	* @param lastAccountRefresh - Date of last refresh
	*/
	public void setLastAccountRefresh(LocalDate lastAccountRefresh) {
		this.lastAccountRefresh = lastAccountRefresh;
	}
	
	/**
	* Sets the current ID/Token the Candidate can use to confirm their availability
	* @param lastAvailabilityCheckIdSent - Id
	*/
	public void setLastAvailabilityCheckIdSent(UUID lastAvailabilityCheckIdSent) {
		this.lastAvailabilityCheckIdSent = lastAvailabilityCheckIdSent;
	}
	
	/**
	* Adds an additional Function that the candidate is available for
	* @param function
	*/
	public void addFunction(FUNCTION function) {
		this.functions.add(function);
	}
	
	/**
	* Sets the last Date that the Candidate confirmed their own availability
	* @param lastAvailabilityCheckConfirmed - Date of confirmation
	*/
	public void setLastAvailabilityCheckConfirmedOn(LocalDate lastAvailabilityCheckConfirmedOn) {
		this.lastAvailabilityCheckConfirmedOn = lastAvailabilityCheckConfirmedOn;
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
		
		private String 						candidateId;
		private String 						firstname;
		private String						surname;
		private String 						email;
		private String						roleSought;
		private Set<FUNCTION>				functions					= new LinkedHashSet<>();
		private COUNTRY 					country;
		private String 						city;
		private double						longitude;
		private double						latitude;
		private PERM 						perm;
		private FREELANCE 					freelance;
		private int							yearsExperience;
		private boolean 					available;
		private boolean 					flaggedAsUnavailable;
		private LocalDate 					registerd					= LocalDate.now();
		private LocalDate 					lastAvailabilityCheck		= LocalDate.now();
		private Set<String> 				skills						= new LinkedHashSet<>();
		private Set<Language> 				languages					= new LinkedHashSet<>();
		private String						introduction;
		private Photo						photo;
		private String 						comments;
		private DAYS_ON_SITE				daysOnSite;
		private Rate						rateContract;
		private Rate						ratePerm;
		private LocalDate 					availableFromDate;
		//To be set if MP candidate based upon authenticated user via service
		private String 						ownerId;
		private CANDIDATE_TYPE				candidateType;
		private boolean 					requiresSponsorship;
		private SECURITY_CLEARANCE_TYPE 	securityClearance;
		private LocalDate					lastAccountRefresh;
		private LocalDate					lastAvailabilityCheckEmailSent;
		private UUID 						lastAvailabilityCheckIdSent;
		private LocalDate 					lastAvailabilityCheckConfirmedOn;
		
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
		* Sets the functions the candidate performs
		* @param function - Functions performed by the Candidate
		* @return Builder
		*/
		public CandidateBuilder functions(Set<FUNCTION> functions) {
			this.functions.clear();
			this.functions.addAll(functions);
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
		* Sets the longitude of the candidates city
		* @param longitude - longitude of candidates city
		* @return Builder
		*/
		public CandidateBuilder longitude(double longitude) {
			this.longitude = longitude;
			return this;
		}
		
		/**
		* Sets the latitude of the candidates city
		* @param latitude - latitude of candidates city
		* @return Builder
		*/
		public CandidateBuilder latitude(double latitude) {
			this.latitude = latitude;
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
		* Sets the candidates introduction about themselves
		* @param introduction - introduction
		* @return Builder
		*/
		public CandidateBuilder introduction(String introduction) {
			this.introduction = introduction;
			return this;
		}
		
		/**
		* Sets the Security clearance level held by the Candidate
		* @param securityClearance - Level of Security Clearance 
		* @return Builder
		*/
		public CandidateBuilder securityClearance(SECURITY_CLEARANCE_TYPE securityClearance) {
			this.securityClearance = securityClearance;
			return this;
		}
		
		/**
		* Sets Profile Photo for the Candidate
		* @param photo - Photo of the Candidate
		* @return Builder
		*/
		public CandidateBuilder photo(Photo photo) {
			this.photo = photo;
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
		* Sets comments relating to the Candidate
		* @param comments - additional notes/comments
		* @return Builder
		*/
		public CandidateBuilder comments(String comments) {
			this.comments = comments;
			return this;
		}
		
		/**
		* Sets the max number of days the Candidate is prepared to work onsite
		* @param daysOnSite - Max number of days onsite
		* @return Builder
		*/
		public CandidateBuilder daysOnSite(DAYS_ON_SITE daysOnSite) {
			this.daysOnSite = daysOnSite;
			return this;
		}
		
		/**
		* Sets the contract rate the Candidate will accept
		* @param rateContract - rate
		* @return Builder
		*/
		public CandidateBuilder rateContract(Rate rateContract) {
			this.rateContract = rateContract;
			return this;
		}
		
		
		/**
		* Sets the perm salary the Candidate will accept
		* @param ratePerm - salary
		* @return Builder
		*/
		public CandidateBuilder ratePerm(Rate ratePerm) {
			this.ratePerm = ratePerm;
			return this;
		}
		
		/**
		* Sets the Date the Candidate is available from
		* @param availableFromDate - When the Candidate will be available from
		* @return Builder
		*/
		public CandidateBuilder availableFromDate(LocalDate availableFromDate) {
			this.availableFromDate = availableFromDate;
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
		* Sets the Id of the Owner of the Candidate
		* @param ownerId - unique id of the Owner
		* @return Builder
		*/
		public CandidateBuilder ownerId(String ownerId) {
			this.ownerId = ownerId;
			return this;
		}
		
		/**
		* Sets whether or not the Candidate requires sponsorship to take 
		* on a new role
		* @param requiresSponsorship - Whether the Candidate requires sponsorship
		* @return Builder
		*/
		public CandidateBuilder requiresSponsorship(boolean requiresSponsorship) {
			this.requiresSponsorship = requiresSponsorship;
			return this;
		}
		
		/**
		* Sets the Candidate type
		* @param candidateType - type of the Candidate
		* @return Builder
		*/
		public CandidateBuilder candidateType(CANDIDATE_TYPE candidateType) {
			this.candidateType = candidateType;
			return this;
		}
		
		/**
		* Sets date of last time Candidate account was refreshed
		* @param lastAccountRefresh - Date of the last refresh
		* @return
		*/
		public CandidateBuilder lastAccountRefresh(LocalDate lastAccountRefresh) {
			this.lastAccountRefresh = lastAccountRefresh;
			return this;
		}
		
		/**
		* Sets the Date of the last time an email was sent to the Candidate asking them
		* to confirm their availability
		* @param lastAvailabilityCheckEmailSent - Date email last sent
		* @return Builder
		*/
		public CandidateBuilder lastAvailabilityCheckEmailSent(LocalDate lastAvailabilityCheckEmailSent) {
			this.lastAvailabilityCheckEmailSent = lastAvailabilityCheckEmailSent;
			return this;
		}
		
		/**
		* Sets the last UUID ( And now only valid id ) for a request for the Candidate 
		* to confirm their availability
		* @param lastAvailabilityCheckIdSent - ID
		* @return Builder
		*/
		public CandidateBuilder lastAvailabilityCheckIdSent(UUID lastAvailabilityCheckIdSent) {
			this.lastAvailabilityCheckIdSent = lastAvailabilityCheckIdSent;
			return this;
		}
		
		/**
		* Sets the Date the Candidate last confirmed their own Availability
		* @param lastAvailabilityCheckConfirmed
		* @return Builder
		*/
		public CandidateBuilder lastAvailabilityCheckConfirmedOn(LocalDate lastAvailabilityCheckConfirmedOn) {
			this.lastAvailabilityCheckConfirmedOn = lastAvailabilityCheckConfirmedOn;
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
	* Class represents a Rate charged by the Candidate
	* for their work
	* @author K Parkings
	*/
	public static class Rate{
		
		public enum CURRENCY{EUR,GBP}
		public enum PERIOD{HOUR,DAY,YEAR}
		
		private final CURRENCY 	currency;
		private final PERIOD 	period;
		private final float 	valueMin;
		private final float 	valueMax;

		/**
		* Constructor
		* @param currency 	- Currency being charged
		* @param period   	- Unit of charging
		* @param valueMin	- Min value acceptable for the Candidate
		* @param valueMax	- Max value acceptable for the Candidate
		*/
		public Rate(CURRENCY currency, PERIOD period, float valueMin, float valueMax) {
			this.currency 	= currency;
			this.period 	= period;
			this.valueMin 	= valueMin;
			this.valueMax	= valueMax;
		}
		
		/**
		* Return the currency being charged
		* @return currency
		*/
		public CURRENCY getCurrency() {
			return this.currency;
		}
		
		/**
		* Returns the unit of charging
		* @return period
		*/
		public PERIOD getPeriod() {
			return this.period;
		}
		
		/**
		* Returns minimum amount charged per unit 
		* of charging
		* @return value
		*/
		public float getValueMin() {
			return this.valueMin;
		}
		
		/**
		* Returns max amount charged per unit 
		* of charging
		* @return value
		*/
		public float getValueMax() {
			return this.valueMax;
		}

	}
	
	/**
	* Class represents a Photo
	* @author K Parkings
	*/
	public static class Photo{
		
		public  enum PHOTO_FORMAT {jpeg, png}
		
		private final byte[] 		imageBytes;
		private final PHOTO_FORMAT 	format;
	
		/**
		* Class represents an uploaded Photo
		* @param imageBytes - bytes of actual file
		* @param format		- format of photo file
		*/
		public Photo(byte[] imageBytes, PHOTO_FORMAT format) {
			this.imageBytes 	= imageBytes;
			this.format 		= format;
		}
		
		/**
		* Returns the bytes of the file
		* @return file bytes
		*/
		public byte[] getImageBytes() {
			return this.imageBytes;
		}
		
		/**
		* Returns the file format
		* @return format of the file
		*/
		public PHOTO_FORMAT getFormat() {
			return this.format;
		}
		
	}

}