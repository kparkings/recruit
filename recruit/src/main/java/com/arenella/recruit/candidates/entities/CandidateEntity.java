package com.arenella.recruit.candidates.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
	@Column(name="rate_currency")
	private CURRENCY rateCurrency;
	
	@Enumerated(EnumType.STRING)
    @Column(name="rate_period")
    private PERIOD ratePeriod;
    
    @Column(name="rate_value")
    private float rateValue;      
    
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
		this.rateCurrency				= builder.rateCurrency;
		this.ratePeriod					= builder.ratePeriod;
		this.rateValue					= builder.rateValue;      
		this.photoFormat				= builder.photoFormat;      
		this.photoBytes					= builder.photoBytes;
		
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
	* Returns the Currency the Candidate 
	* charges in
	* @return currency
	*/
	public CURRENCY getRateCurrency() {
		return this.rateCurrency;
	}
	
	/**
	* Returns the period the Candidate 
	* charges in
	* @return Period
	*/
	public PERIOD getRatePeriod() {
		return this.ratePeriod;
	}
	
	/**
	* Returns the value per period
	* the Candidate charges
	* @return value
	*/
	public float getRateValue() {
		return this.rateValue;
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
		private CURRENCY 		rateCurrency;
		private PERIOD 			ratePeriod;
		private float 			rateValue;      
		private PHOTO_FORMAT 	photoFormat;      
		private byte[] 			photoBytes;
		
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
		* Sets the Currency the Candidate charges in
		* @param rateCurrency - Currency
		* @return Builder
		*/
		public CandidateEntityBuilder rateCurrency(CURRENCY rateCurrency) {
			this.rateCurrency = rateCurrency;
			return this;
		}
		
		/**
		* Sets the unit the Rate is in
		* @param ratePeriod - Unit of the Rate
		* @return Builder
		*/
		public CandidateEntityBuilder ratePeriod(PERIOD ratePeriod) {
			this.ratePeriod = ratePeriod;
			return this;
		}
		
		/**
		* Sets the amount the Candidate charges per period
		* @param rateValue - amount per period
		* @return Builder
		*/
		public CandidateEntityBuilder rateValue(float rateValue) {
			this.rateValue = rateValue;
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
						.skills(candidate.getSkills().stream().map(skill -> skill.trim()).map(skill ->skill.toLowerCase()).collect(Collectors.toSet()))
						.languages(candidate.getLanguages())
						.introduction(candidate.getIntroduction())
						.rateCurrency(candidate.getRate().isEmpty() ? null 	: candidate.getRate().get().getCurrency())
						.ratePeriod(candidate.getRate().isEmpty() 	? null 	: candidate.getRate().get().getPeriod())
						.rateValue(candidate.getRate().isEmpty() 	? 0 	: candidate.getRate().get().getValue())
						.photoFormat(candidate.getPhoto().isEmpty() ? null 	: candidate.getPhoto().get().getFormat())
						.photoBytes(candidate.getPhoto().isEmpty() 	? null 	: candidate.getPhoto().get().getImageBytes())
					.build();
		
	}

	/**
	* Converts a Entity representation of a Candidate Object to an Domain 
	* representation
	* @param candidate - Instance of Candidate to convert
	* @return Domain representation of a Candidate object
	*/
	public static Candidate convertFromEntity(CandidateEntity candidateEntity) {
		
		Rate 	rate 	= null;
		Photo 	photo 	= null;
		
		if (candidateEntity.rateCurrency != null && candidateEntity.ratePeriod != null) {
			rate = new Rate(candidateEntity.getRateCurrency(), candidateEntity.getRatePeriod(), candidateEntity.getRateValue());
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
						.rate(rate)
						.photo(photo)
						.languages(candidateEntity.getLanguages().stream().map(lang -> Language.builder().language(lang.getLanguage()).level(lang.getLevel()).build()).collect(Collectors.toCollection(HashSet::new)))
						.build();
		
	}
	
}