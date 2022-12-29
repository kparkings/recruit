package com.arenella.recruit.candidates.entities;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.arenella.recruit.candidates.beans.CandidateSearchAlert;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Entity representation of CandidateSearchAlert
* @author K Parkings
*/
@Entity
@Table(schema="candidate", name="candidate_search_alert")
public class CandidateSearchAlertEntity {

	@Id
	@Column(name="id")
	private UUID 				alertId;
	
	@Column(name="recruiter_id")
	private String 				recruiterId;
	
	@Column(name="alert_name")
	private String 				alertName;
	
	@Column(name="freelance")
	private Boolean 			freelance;
	
	@Column(name="perm")
	private Boolean 			perm;
	
	@Column(name="years_exp_gte")
	private int 				yearsExperienceGtEq;
	
	@Column(name="years_exp_lte")
	private int					yearsExperienceLtEq;
	
	@Column(name="dutch")
	@Enumerated(EnumType.STRING)
	private Language.LEVEL 		dutch;
	
	@Column(name="english")
	@Enumerated(EnumType.STRING)
	private Language.LEVEL 		english;
	
	@Column(name="french")
	@Enumerated(EnumType.STRING)
	private Language.LEVEL 		french;
	
	@ElementCollection(targetClass=String.class)
	@CollectionTable(schema="candidate", name="candidate_search_alert_skill", joinColumns=@JoinColumn(name="alert_id"))
	@Column(name="skill")
	private Set<String>			skills								= new HashSet<>();
	
	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass=COUNTRY.class, fetch=FetchType.EAGER)
	@CollectionTable(schema="candidate", name="candidate_search_alert_country", joinColumns=@JoinColumn(name="alert_id"))
	@Column(name="country")
	private Set<COUNTRY> 		countries							= new HashSet<>();
	
	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass=FUNCTION.class, fetch=FetchType.EAGER)
	@CollectionTable(schema="candidate", name="candidate_search_alert_function", joinColumns=@JoinColumn(name="alert_id"))
	@Column(name="function")
	private Set<FUNCTION> 		functions							= new HashSet<>();
	
	/**
	* Default Constructor 
	*/
	public CandidateSearchAlertEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public CandidateSearchAlertEntity(CandidateSearchAlertEntityBuilder builder) {
		
		this.alertId 				= builder.alertId;
		this.recruiterId 			= builder.recruiterId;
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
	* Returns the unique id of the Alert
	* @return id of the Alert
	*/
	public UUID getAlertId() {
		return this.alertId;
	}
	
	/**
	* Returns the unique id of the Recruiter
	* @return owner of the Alert
	*/
	public String getRecruiterId() {
		return this.recruiterId;
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
		return Optional.ofNullable(this.freelance);
	}
	
	/**
	* Returns whether to include perm candidate
	* @return whether to include perm candidates
	*/
	public Optional<Boolean> getPerm() {
		return Optional.ofNullable(this.perm);
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
	* Returns a Builder for the CandidateSearchAlertEntity class
	* @return Builder
	*/
	public static CandidateSearchAlertEntityBuilder builder() {
		return new CandidateSearchAlertEntityBuilder();
	}
	
	/**
	* Builder for the CandidateSearchAlertEntity class
	* @author K Parkings
	*/
	public static class CandidateSearchAlertEntityBuilder{
		
		private UUID 				alertId;
		private String 				recruiterId;
		private String 				alertName;
		private Set<COUNTRY> 		countries							= new HashSet<>();
		private Set<FUNCTION> 		functions							= new HashSet<>();
		private Boolean 			freelance;
		private Boolean		 		perm;
		private int 				yearsExperienceGtEq;
		private int					yearsExperienceLtEq;
		private Language.LEVEL 		dutch;
		private Language.LEVEL 		english;
		private Language.LEVEL 		french;
		private Set<String>			skills								= new HashSet<>();
		
		/**
		* Sets the Unique identifier of the Alert
		* @param alertId - Unique identifier
		* @return Builder
		*/
		public CandidateSearchAlertEntityBuilder alertId(UUID alertId) {
			this.alertId = alertId;
			return this;
		}
		
		/**
		* Sets the unique Id of the recruiter who owns the alert
		* @param recruiterId - Unique id of the recruiter
		* @return Builder
		*/
		public CandidateSearchAlertEntityBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the name of the Alert
		* @param alertName - Display name of the alert
		* @return Builder
		*/
		public CandidateSearchAlertEntityBuilder alertName(String alertName) {
			this.alertName = alertName;
			return this;
		}
		
		/**
		* Sets the countries to filter on
		* @param countries - countries to filter on
		* @return Builder
		*/
		public CandidateSearchAlertEntityBuilder countries(Set<COUNTRY> countries) {
			this.countries.clear();
			this.countries.addAll(countries);
			return this;
		}
		
		/**
		* Sets the functions to filter on
		* @param functions - functions to filter on
		* @return Builder
		*/
		public CandidateSearchAlertEntityBuilder functions(Set<FUNCTION> functions) {
			this.functions.clear();
			this.functions.addAll(functions);
			return this;
		}
		
		/**
		* Sets the whether to include freelance candidates
		* @param freelance - freelance filter
		* @return Builder
		*/
		public CandidateSearchAlertEntityBuilder freelance(Optional<Boolean> freelance) {
			
			if (freelance.isPresent()) {
				this.freelance = freelance.get();
			}
			
			return this;
		}
		
		/**
		* Sets whether to include perm candidates
		* @param perm - perm filter
		* @return Builder
		*/
		public CandidateSearchAlertEntityBuilder perm (Optional<Boolean> perm) {
			
			if (perm.isPresent()) {
				this.perm = perm.get();
			}
			
			return this;
		}
		
		/**
		* Minimum number of years experience filter
		* @param yearsExperienceGtEq - experience filter
		* @return Builder
		*/
		public CandidateSearchAlertEntityBuilder yearsExperienceGtEq (int yearsExperienceGtEq) {
			this.yearsExperienceGtEq = yearsExperienceGtEq;
			return this;
		}
		
		/**
		* Maximum number of years experience filter
		* @param yearsExperienceLtEq
		* @return Builder
		*/
		public CandidateSearchAlertEntityBuilder yearsExperienceLtEq(int yearsExperienceLtEq) {
			this.yearsExperienceLtEq = yearsExperienceLtEq;
			return this;
		}
		
		/**
		* Set the Dutch language filter options
		* @param dutch - Dutch language filter options
		* @return Builder
		*/
		public CandidateSearchAlertEntityBuilder dutch(Language.LEVEL dutch) {
			this.dutch = dutch;
			return this;
		}
		
		/**
		* Sets the English language filter options
		* @param english - English language filter options
		* @return Builder
		*/
		public CandidateSearchAlertEntityBuilder english(Language.LEVEL english) {
			this.english = english;
			return this;
		}
		
		/**
		* Sets the French language filter options
		* @param french - French language filter options
		* @return Builder
		*/
		public CandidateSearchAlertEntityBuilder french(Language.LEVEL french) {
			this.french = french;
			return this;
		}
		
		/**
		* Set the skills to filter on
		* @param skills - Skills to filter on
		* @return Builder
		*/
		public CandidateSearchAlertEntityBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Returns a CandidateSearchAlertEntity initialized with the values 
		* in the Builder
		* @return CandidateSearchAlertEntity
		*/
		public CandidateSearchAlertEntity build() {
			return new CandidateSearchAlertEntity(this);
		}
		
	}
	
	/**
	* Converts from the Entity representation to the Domain representation
	* @param entity - Entity Representation
	* @return Domain representation
	*/
	public static CandidateSearchAlert convertFromEntity(CandidateSearchAlertEntity entity) {
		return CandidateSearchAlert
				.builder()
					.alertId(entity.getAlertId())
					.alertName(entity.getAlertName())
					.countries(entity.getCountries())
					.dutch(entity.getDutch())
					.english(entity.getEnglish())
					.freelance(entity.getFreelance())
					.french(entity.getFrench())
					.functions(entity.getFunctions())
					.perm(entity.getPerm())
					.recruiterId(entity.getRecruiterId())
					.skills(entity.getSkills())
					.yearsExperienceGtEq(entity.getYearsExperienceGtEq())
					.yearsExperienceLtEq(entity.getyearsExperienceLtEq())
				.build();
	}

	/**
	* Converts from the Domain representation to the Entity representation
	* @param alert - Domain representation
	* @return Entity representation
	*/
	public static CandidateSearchAlertEntity convertToEntity(CandidateSearchAlert alert) {
		return CandidateSearchAlertEntity
				.builder()
					.alertId(alert.getAlertId())
					.alertName(alert.getAlertName())
					.countries(alert.getCountries())
					.dutch(alert.getDutch())
					.english(alert.getEnglish())
					.freelance(alert.getFreelance())
					.french(alert.getFrench())
					.functions(alert.getFunctions())
					.perm(alert.getPerm())
					.recruiterId(alert.getRecruiterId())
					.skills(alert.getSkills())
					.yearsExperienceGtEq(alert.getYearsExperienceGtEq())
					.yearsExperienceLtEq(alert.getyearsExperienceLtEq())
				.build();
	}

}