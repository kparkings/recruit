package com.arenella.recruit.entities;

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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.arenella.recruit.beans.Candidate;
import com.arenella.recruit.beans.Language;
import com.arenella.recruit.enums.COUNTRY;
import com.arenella.recruit.enums.FUNCTION;

/**
* Entity representation of a Candidate. A Candidate is 
* someone who is potentially open to Work.
* @author K Parkings
*/
@Entity
@Table(name="candidate")
public class CandidateEntity {

	@Id
	@Column(name="candidate_id")
	private String 		candidateId;
	
	@Column(name="firstname")
	private String 		firstname;
	
	@Column(name="surname")
	private String 		surname;
	
	@Column(name="email")
	private String 		email;
	
	@Column(name="function")
	@Enumerated(EnumType.STRING)
	private FUNCTION function;
	
	@Column(name="country")
	@Enumerated(EnumType.STRING)
	private COUNTRY 	country;
	
	@Column(name="city")
	private String 		city;
	
	@Column(name="perm")
	private boolean 	perm;
	
	@Column(name="freelance")
	private boolean 	freelance;
	
	@Column(name="years_experience")
	private int			yearsExperience;
	
	@Column(name="available")
	private boolean 	available;
	
	@Column(name="registered")
	private LocalDate 	registerd;
	
	@Column(name="last_availability_check")
	private LocalDate 	lastAvailabilityCheck;
	
	@ElementCollection(targetClass=String.class)
	@CollectionTable(name="candidate_skill", joinColumns=@JoinColumn(name="candidate_id"))
	@Column(name="skill")
	private Set<String> 			skills						= new LinkedHashSet<>();
	
	//TODO: Think the primary key columns need to be specified here
	@OneToMany(mappedBy = "id.candidateId", cascade = CascadeType.ALL, orphanRemoval=true)
	private Set<LanguageEntity> 	languages					= new LinkedHashSet<>();
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization parameters
	*/
	public CandidateEntity(CandidateEntityBuilder builder) {
		
		this.candidateId 			= builder.candidateId;
		this.firstname				= builder.firstname;
		this.surname				= builder.surname;
		this.email					= builder.email;
		this.function				= builder.function;
		this.country 				= builder.country;
		this.city 					= builder.city;
		this.perm 					= builder.perm;
		this.freelance 				= builder.freelance;
		this.yearsExperience 		= builder.yearsExperience;
		this.available 				= builder.available;
		this.registerd 				= builder.registerd;
		this.lastAvailabilityCheck 	= builder.lastAvailabilityCheck;
		
		this.skills.addAll(builder.skills);
		this.languages.addAll(builder.languages.stream().map(lang -> LanguageEntity.builder().candidate(this).language(lang.getLanguage()).level(lang.getLevel()).build()).collect(Collectors.toSet()));

	}
	
	private CandidateEntity() {}
	
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
	public boolean isFreelance() {
		return this.freelance;
	}
	
	/**
	* Returns whether or not the Candidate is interested in permanent roles
	* @return Whether or not the Candidate is interested in permanent roles
	*/
	public boolean isPerm() {
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
	* @return Whether the Candidate is avilable for work
	*/
	public boolean isAvailable() {
		return this.available;
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
	
		private String 			candidateId;
		private String 			firstname;
		private String			surname;
		private String 			email;
		private FUNCTION		function;
		private COUNTRY 		country;
		private String 			city;
		private boolean 		perm;
		private boolean 		freelance;
		private int				yearsExperience;
		private boolean 		available;
		private LocalDate 		registerd					= LocalDate.now();				//Set app to work with UAT
		private LocalDate 		lastAvailabilityCheck		= LocalDate.now();
		
		private Set<String> 	skills						= new LinkedHashSet<>();
		private Set<Language> 	languages					= new LinkedHashSet<>();
	
		/**
		* Sets the Unique Identifier of the Candidate
		* @param candidateId - UniqueId of the Candidate
		* @return Builder
		*/
		public CandidateEntityBuilder candidateId(String candidateId) {
			this.candidateId = candidateId;
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
		public CandidateEntityBuilder perm(boolean perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param perm - Whether candidate is looking for freelance roles
		* @return Builder
		*/
		public CandidateEntityBuilder freelance(boolean freelance) {
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
		* Sets the Date the Candidate was registered in the System
		* @param registerd - date of registration
		* @return Builder
		*/
		public CandidateEntityBuilder registerd(LocalDate registerd) {
			this.registerd = registerd;
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
						.candidateId(candidate.getCandidateId())
						.firstname(candidate.getFirstname())
						.surname(candidate.getSurname())
						.email(candidate.getEmail())
						.function(candidate.getFunction())
						.city(candidate.getCity())
						.country(candidate.getCountry())
						.freelance(candidate.isFreelance())
						.lastAvailabilityCheck(candidate.getLastAvailabilityCheckOn())
						.perm(candidate.isPerm())
						.registerd(candidate.getRegisteredOn())
						.yearsExperience(candidate.getYearsExperience())
						.skills(candidate.getSkills())
						.languages(candidate.getLanguages())
						.build();
		
	}

	/**
	* Converts a Entity representation of a Candidate Object to an Domain 
	* representation
	* @param candidate - Instance of Candidate to convert
	* @return Domain representation of a Candidate object
	*/
	public static Candidate convertFromEntity(CandidateEntity candidateEntity) {
		
		return Candidate
					.builder()
						.available(candidateEntity.isAvailable())
						.candidateId(candidateEntity.getCandidateId())
						.firstname(candidateEntity.getFirstname())
						.surname(candidateEntity.getSurname())
						.email(candidateEntity.getEmail())
						.function(candidateEntity.getFunction())
						.city(candidateEntity.getCity())
						.country(candidateEntity.getCountry())
						.freelance(candidateEntity.isFreelance())
						.lastAvailabilityCheck(candidateEntity.getLastAvailabilityCheckOn())
						.perm(candidateEntity.isPerm())
						.registerd(candidateEntity.getRegisteredOn())
						.yearsExperience(candidateEntity.getYearsExperience())
						.skills(candidateEntity.getSkills())
						.languages(candidateEntity.getLanguages().stream().map(lang -> Language.builder().language(lang.getLanguage()).level(lang.getLevel()).build()).collect(Collectors.toCollection(HashSet::new)))
						.build();
		
	}
	
}