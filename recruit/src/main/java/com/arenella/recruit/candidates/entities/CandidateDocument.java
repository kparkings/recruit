package com.arenella.recruit.candidates.entities;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.CANDIDATE_TYPE;
import com.arenella.recruit.candidates.beans.Candidate.DAYS_ON_SITE;
import com.arenella.recruit.candidates.beans.Candidate.SECURITY_CLEARANCE_TYPE;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Elasticasearch Document representation of a Candidate
* @author K Parkings
*/ 
@Document(indexName="candidates", writeTypeHint = org.springframework.data.elasticsearch.annotations.WriteTypeHint.FALSE)
public class CandidateDocument {

	@Id
	@Field(type = FieldType.Long)
	private Long		candidateId;
	
	@Field(type = FieldType.Keyword)
	private String 		firstname;
	
	@Field(type = FieldType.Keyword)
	private String 		surname;
	
	@Field(type = FieldType.Keyword)
	private String 		email;
	
	@Field(type = FieldType.Keyword)
	private String roleSought;
	
	@Field(type = FieldType.Keyword)
	@Enumerated(EnumType.STRING)
	private FUNCTION function;
	
	@Field(type = FieldType.Keyword)
	@Enumerated(EnumType.STRING)
	private COUNTRY 	country;
	
	@Field(type = FieldType.Keyword)
	private String 		city;
	
	@Field(type = FieldType.Keyword)
	@Enumerated(EnumType.STRING)
	private PERM 		perm;
	
	@Field(type = FieldType.Keyword)
	@Enumerated(EnumType.STRING)
	private FREELANCE 	freelance;
	
	@Field(type = FieldType.Integer)
	private int			yearsExperience;
	
	@Field(type = FieldType.Boolean)
	private boolean 	available;
	
	@Field(type = FieldType.Date)
	//@JsonFormat(pattern = "yyyy-MM-dd")
	private Date 	registerd;
	
	@Field(type = FieldType.Date)
	//@JsonFormat(pattern = "yyyy-MM-dd")
	private Date 	lastAvailabilityCheck;
	
	@Field(type = FieldType.Keyword)
	private String introduction;
	
	@Enumerated(EnumType.STRING)
	private RateDocument rateContract;
	
	@Enumerated(EnumType.STRING)
	private RateDocument ratePerm;
	
	@Field(type = FieldType.Date)
	//@JsonFormat(pattern = "yyyy-MM-dd")
	private Date 		availableFromDate;
	
	@Field(type = FieldType.Keyword)
	private String 			ownerId;
	
	@Field(type = FieldType.Keyword)
	@Enumerated(EnumType.STRING)
	private CANDIDATE_TYPE	candidateType;
	
	@Field(type = FieldType.Keyword)
	private String comments;
	
	@Field(type = FieldType.Keyword)
	@Enumerated(EnumType.STRING)
	private DAYS_ON_SITE daysOnSite;
    
	@Enumerated(EnumType.STRING)
	private PhotoDocument photo;      
    
	@Field(type = FieldType.Boolean)
    private boolean requiresSponsorship;
    
	@Field(type = FieldType.Keyword)
    @Enumerated(EnumType.STRING)
    private SECURITY_CLEARANCE_TYPE securityClearance;
       
	@Field(type = FieldType.Keyword)
	private Set<String> 			skills						= new LinkedHashSet<>();
	
	private Set<LanguageDocument> 	languages					= new LinkedHashSet<>();
	
	/**
	* Check if we need this. with Hibernate yes but with ES maybe not
	*/
	public CandidateDocument() {}
	
	/**
	* Constructor based on a Builder
	* @param builder - Contains initialization values
	*/
	public CandidateDocument(CandidateDocumentBuilder builder) {
		
		ZoneId defaultZoneId = ZoneId.systemDefault();
		
		this.candidateId 				= builder.candidateId;
		this.firstname 					= builder.firstname;
		this.surname 					= builder.surname;
		this.email 						= builder.email;
		this.roleSought 				= builder.roleSought;
		this.function 					= builder.function;
		this.country 					= builder.country;
		this.city 						= builder.city;
		this.perm 						= builder.perm;
		this.freelance 					= builder.freelance;
		this.yearsExperience 			= builder.yearsExperience;
		this.available 					= builder.available;
		this.registerd 					= Date.from(builder.registerd.atStartOfDay(defaultZoneId).toInstant());
		this.lastAvailabilityCheck 		= Date.from(builder.lastAvailabilityCheck.atStartOfDay(defaultZoneId).toInstant());
		this.introduction 				= builder.introduction;
		this.rateContract 				= builder.rateContract;
		this.ratePerm 					= builder.ratePerm;
		this.availableFromDate 			= Date.from(builder.availableFromDate.atStartOfDay(defaultZoneId).toInstant());
		this.ownerId 					= builder.ownerId;
		this.candidateType 				= builder.candidateType;
		this.comments 					= builder.comments;
		this.daysOnSite 				= builder.daysOnSite;
		this.photo 						= builder.photo;      
		this.requiresSponsorship 		= builder.requiresSponsorship;
		this.securityClearance 			= builder.securityClearance;
		
		
		
		this.skills.clear();
		this.skills.addAll(builder.skills);
		this.languages.clear();
		this.languages.addAll(builder.languages);
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
	* Returns the title on the role the user is looking for. This can be a more 
	* specific description of the users role that provided by the function 
	* attribute
	* @return Role sought my the Candidate
	*/
	public String getRoleSought() {
		return this.roleSought;
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
	public FREELANCE isFreelance() {
		return this.freelance;
	}
	
	public FREELANCE getFreelance() {
		return this.freelance;
	}
	
	/**
	* Returns whether or not the Candidate is interested in permanent roles
	* @return Whether or not the Candidate is interested in permanent roles
	*/
	public PERM isPerm() {
		return this.perm;
	}
	
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
	* Returns the Date that the Candidate was registered in the System
	* @return Date the Candidate Registered
	*/
	public Date getRegisteredOn() {
		//ZoneId defaultZoneId = ZoneId.systemDefault();
		return this.registerd;
	}
	
	//@JsonFormat(pattern = "yyyy-MM-dd")
   public Date getRegisterd() {
		//ZoneId defaultZoneId = ZoneId.systemDefault();
		return this.registerd;
	}
	
	/**
	* Returns the Date of the last time the Candidate was contacted to check 
	* that they were still available for a new role
	* @return Date of last availability check
	*/
	//@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getLastAvailabilityCheckOn() {
		//ZoneId defaultZoneId = ZoneId.systemDefault();
		return this.lastAvailabilityCheck;
	}
	
	public Date getLastAvailabilityCheck() {
		//ZoneId defaultZoneId = ZoneId.systemDefault();
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
	public Optional<RateDocument> getRateContract(){
		return Optional.ofNullable(this.rateContract);
	}
	
	/**
	* If available Perm rate the Candidate is 
	* looking for
	* @return Perm rate
	*/
	public Optional<RateDocument> getRatePerm(){
		return Optional.ofNullable(this.ratePerm);
	}
	
	/**
	* returns the date the Candidate is available from. If not specified 
	* uses the current date
	* @return When the candidate is available from
	*/
	public LocalDate getAvailableFromDate(){
		
		return Optional.ofNullable(this.availableFromDate).isEmpty() ? LocalDate.now() : Instant.ofEpochMilli(this.availableFromDate.getTime())
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate();
			
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
	public Set<LanguageDocument> getLanguages(){
		return this.languages;
	}
	
	/**
	* If available returns a photo of the Candidate
	* @return Profile Photo
	*/
	public Optional<PhotoDocument> getPhoto(){
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
	* Returns a Builder for the class
	* @return Builder
	*/
	public static CandidateDocumentBuilder builder() {
		return new CandidateDocumentBuilder();
	}
	
	/**
	* Builder for the class
	* @author K Parkings
	*/
	public static class CandidateDocumentBuilder{
		
		private long					candidateId;
		private String 					firstname;
		private String 					surname;
		private String 					email;
		private String 					roleSought;
		private FUNCTION 				function;
		private COUNTRY 				country;
		private String 					city;
		private PERM 					perm;
		private FREELANCE 				freelance;
		private int						yearsExperience;
		private boolean 				available;
		private LocalDate 				registerd;
		private LocalDate 				lastAvailabilityCheck;
		private String 					introduction;
		private RateDocument 			rateContract;
		private RateDocument 			ratePerm;
		private LocalDate 				availableFromDate;
		private String 					ownerId;
		private CANDIDATE_TYPE			candidateType;
		private String 					comments;
		private DAYS_ON_SITE 			daysOnSite;
	    private PhotoDocument 			photo;      
	    private boolean 				requiresSponsorship;
	    private SECURITY_CLEARANCE_TYPE securityClearance;
	    private Set<String> 			skills						= new LinkedHashSet<>();
		private Set<LanguageDocument> 	languages					= new LinkedHashSet<>();
		
		/**
		* Sets the candidates Unique identifier in the System
		* @param candidateId - Unique identifier of the Candidate
		* @return Builder
		*/
		public CandidateDocumentBuilder candidateId(long candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets the First name of the Candidate
		* @param firstname - Candidates first name
		* @return Builder
		*/
		public CandidateDocumentBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the Surname of the Candidate
		* @param surname - Candidates Surname
		* @return Builder
		*/
		public CandidateDocumentBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Email address of the Candidate
		* @param email - Email address
		* @return Builder
		*/
		public CandidateDocumentBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the Role sought by the Candidate 
		* @param roleSought - Role the candidate is looking for
		* @return Builder
		*/
		public CandidateDocumentBuilder roleSought(String roleSought) {
			this.roleSought = roleSought;
			return this;
		}
		
		/**
		* Sets the function the candidate performs
		* @param function - Function performed by the Candidate
		* @return Builder
		*/
		public CandidateDocumentBuilder function(FUNCTION function) {
			this.function = function;
			return this;
		}
		
		/**
		* Returns the Country where the Candidate is located
		* @param country - Country where the Candidate is located
		* @return Builder
		*/
		public CandidateDocumentBuilder country(COUNTRY country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets the City where the Candidate is located
		* @param city - City where the Candidate is located
		* @return Builder
		*/
		public CandidateDocumentBuilder city(String city) {
			this.city = city;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for perm roles
		* @param freelance - Whether or not the Candidate is interested in perm roles
		* @return Builder
		*/
		public CandidateDocumentBuilder perm(PERM perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param freelance - Whether or not the Candidate is interested in freelance roles
		* @return Builder
		*/
		public CandidateDocumentBuilder freelance(FREELANCE freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Sets the number of years experience the Candidate has in the industry
		* @param yearsExperience - years of work experience in the industry
		* @return Builder
		*/
		public CandidateDocumentBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets whether or not the Candidate is currently available
		* @param available - whether or not the Candidate is currently looking for work
		* @return Builder
		*/
		public CandidateDocumentBuilder available(boolean available) {
			this.available = available;
			return this;
		}
		
		/**
		* Sets the Date the Candidate was registered in the System
		* @param registerd - Date of registration
		* @return Builder
		*/
		public CandidateDocumentBuilder registerd(LocalDate registerd) {
			this.registerd = registerd;
			return this;
		}
		
		/**
		* Sets the candidates introduction about themselves
		* @param introduction - introduction
		* @return Builder
		*/
		public CandidateDocumentBuilder introduction(String introduction) {
			this.introduction = introduction;
			return this;
		}
		
		/**
		* Sets the Security clearance level held by the Candidate
		* @param securityClearance - Level of Security Clearance 
		* @return Builder
		*/
		public CandidateDocumentBuilder securityClearance(SECURITY_CLEARANCE_TYPE securityClearance) {
			this.securityClearance = securityClearance;
			return this;
		}
		
		/**
		* Sets Profile Photo for the Candidate
		* @param photo - Photo of the Candidate
		* @return Builder
		*/
		public CandidateDocumentBuilder photo(PhotoDocument photo) {
			this.photo = photo;
			return this;
		}
		
		/**
		* Sets the Date of the last time the Candidates availability 
		* was checked
		* @param lastAvailabilityCheck - Date of last availability check
		* @return Builder
		*/
		public CandidateDocumentBuilder lastAvailabilityCheck(LocalDate lastAvailabilityCheck) {
			this.lastAvailabilityCheck = lastAvailabilityCheck;
			return this;
		}
		
		/**
		* Sets comments relating to the Candidate
		* @param comments - additional notes/comments
		* @return Builder
		*/
		public CandidateDocumentBuilder comments(String comments) {
			this.comments = comments;
			return this;
		}
		
		/**
		* Sets the max number of days the Candidate is prepared to work onsite
		* @param daysOnSite - Max number of days onsite
		* @return Builder
		*/
		public CandidateDocumentBuilder daysOnSite(DAYS_ON_SITE daysOnSite) {
			this.daysOnSite = daysOnSite;
			return this;
		}
		
		/**
		* Sets the contract rate the Candidate will accept
		* @param rateContract - rate
		* @return Builder
		*/
		public CandidateDocumentBuilder rateContract(RateDocument rateContract) {
			this.rateContract = rateContract;
			return this;
		}
		
		
		/**
		* Sets the perm salary the Candidate will accept
		* @param ratePerm - salary
		* @return Builder
		*/
		public CandidateDocumentBuilder ratePerm(RateDocument ratePerm) {
			this.ratePerm = ratePerm;
			return this;
		}
		
		/**
		* Sets the Date the Candidate is available from
		* @param availableFromDate - When the Candidate will be available from
		* @return Builder
		*/
		public CandidateDocumentBuilder availableFromDate(LocalDate availableFromDate) {
			this.availableFromDate = availableFromDate;
			return this;
		}
		
		/**
		* Sets the skills that the Candidate has
		* @param skills - Skills Candidate has experience with
		* @return Builder
		*/
		public CandidateDocumentBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Sets the languages spoken by the Candidate
		* @param languages - Languages spoken by the Candidate
		* @return Builder
		*/
		public CandidateDocumentBuilder languages(Set<LanguageDocument> languages) {
			this.languages.clear();
			this.languages.addAll(languages);
			return this;
		}
		
		/**
		* Sets the Id of the Owner of the Candidate
		* @param ownerId - unique id of the Owner
		* @return Builder
		*/
		public CandidateDocumentBuilder ownerId(String ownerId) {
			this.ownerId = ownerId;
			return this;
		}
		
		/**
		* Sets whether or not the Candidate requires sponsorship to take 
		* on a new role
		* @param requiresSponsorship - Whether the Candidate requires sponsorship
		* @return Builder
		*/
		public CandidateDocumentBuilder requiresSponsorship(boolean requiresSponsorship) {
			this.requiresSponsorship = requiresSponsorship;
			return this;
		}
		
		/**
		* Sets the Candidate type
		* @param candidateType - type of the Candidate
		* @return Builder
		*/
		public CandidateDocumentBuilder candidateType(CANDIDATE_TYPE candidateType) {
			this.candidateType = candidateType;
			return this;
		}
		
		/**
		* Returns an instance of CandidateDocument initialized with the 
		* values in the builder
		* @return Initialized instance of CandidateDocument
		*/
		public CandidateDocument build() {
			return new CandidateDocument(this);
		}
	}
	
	/**
	* Converts from Persistence to Domain representation
	* @param doc - Persistence representation
	* @return Domain representation
	*/
	public static Candidate convertFromDocument(CandidateDocument doc) {
		return Candidate
				.builder()
					.available(doc.isAvailable())
					.availableFromDate(doc.getAvailableFromDate())
					.candidateId(String.valueOf(doc.getCandidateId()))
					.candidateType(doc.getCandidateType())
					.city(doc.getCity())
					.comments(doc.getComments())
					.country(doc.getCountry())
					.daysOnSite(doc.getDaysOnSite())
					.email(doc.getEmail())
					.firstname(doc.getFirstname())
					.flaggedAsUnavailable(false)
					.freelance(doc.isFreelance())
					.function(doc.getFunction())
					.introduction(doc.getIntroduction())
					.languages(doc.getLanguages().stream().map(l ->  LanguageDocument.convertToDomain(l)).collect(Collectors.toCollection(LinkedHashSet::new)))
					.lastAvailabilityCheck(doc.getLastAvailabilityCheckOn().toInstant()
						      .atZone(ZoneId.systemDefault())
						      .toLocalDate())
					.ownerId(doc.getOwnerId().isEmpty() ? null : doc.getOwnerId().get())
					.perm(doc.isPerm())
					.photo(doc.getPhoto().isEmpty() ? null : PhotoDocument.convertToDomain(doc.getPhoto().get()))
					.rateContract(doc.getRateContract().isEmpty() ? null : RateDocument.convertToDomain(doc.getRateContract().get()))
					.ratePerm(doc.getRatePerm().isEmpty() ? null : RateDocument.convertToDomain(doc.getRatePerm().get()))
					.registerd(doc.getRegisteredOn().toInstant()
						      .atZone(ZoneId.systemDefault())
						      .toLocalDate())
					.requiresSponsorship(doc.getRequiresSponsorship())
					.roleSought(doc.getRoleSought())
					.securityClearance(doc.getSecurityClearance())
					.skills(doc.getSkills())
					.surname(doc.getSurname())
					.yearsExperience(doc.getYearsExperience())
				.build();

	}
	
	/**
	* Converts from Domain to Persistence representation
	* @param candidate - Domain representation
	* @return Persistence representation
	*/
	public static CandidateDocument convertToDocument(Candidate candidate) {
		return CandidateDocument
				.builder()
					.available(candidate.isAvailable())
					.availableFromDate(candidate.getAvailableFromDate())
					.candidateId(Long.valueOf(candidate.getCandidateId()))
					.candidateType(candidate.getCandidateType())
					.city(candidate.getCity())
					.comments(candidate.getComments())
					.country(candidate.getCountry())
					.daysOnSite(candidate.getDaysOnSite())
					.email(candidate.getEmail())
					.firstname(candidate.getFirstname())
					.freelance(candidate.isFreelance())
					.function(candidate.getFunction())
					.introduction(candidate.getIntroduction())
					.languages(candidate.getLanguages().stream().map(l ->  LanguageDocument.convertFromDomain(l)).collect(Collectors.toCollection(LinkedHashSet::new)))
					.lastAvailabilityCheck(candidate.getLastAvailabilityCheckOn())
					.ownerId(candidate.getOwnerId().isEmpty() ? null : candidate.getOwnerId().get())
					.perm(candidate.isPerm())
					.photo(candidate.getPhoto().isEmpty() ? null : PhotoDocument.convertFromDomain(candidate.getPhoto().get()))
					.rateContract(candidate.getRateContract().isEmpty() ? null : RateDocument.convertFromDomain(candidate.getRateContract().get()))
					.ratePerm(candidate.getRatePerm().isEmpty() ? null : RateDocument.convertFromDomain(candidate.getRatePerm().get()))
					.registerd(candidate.getRegisteredOn())
					.requiresSponsorship(candidate.getRequiresSponsorship())
					.roleSought(candidate.getRoleSought())
					.securityClearance(candidate.getSecurityClearance())
					.skills(candidate.getSkills())
					.surname(candidate.getSurname())
					.yearsExperience(candidate.getYearsExperience())
				.build();

	}
	
}