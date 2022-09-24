package com.arenella.recruit.recruiters.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.arenella.recruit.recruiters.beans.OfferedCandidate;
import com.arenella.recruit.recruiters.beans.OfferedCandidate.DAYS_ON_SITE;
import com.arenella.recruit.recruiters.beans.OfferedCandidate.LANGUAGE;
import com.arenella.recruit.recruiters.beans.OpenPosition.ContractType;
import com.arenella.recruit.recruiters.beans.OpenPosition.Country;

/**
* Entity representation of an OfferedCandidate 
* @author K Parkings
*/
@Entity
@Table(name="offered_candidates")
public class OfferedCandidateEntity {

	private UUID 						id;
	private String 						recruiterId;
	private String 						candidateRoleTitle;
	private Country	 					country;
	private String						location;
	private ContractType 				contractType;
	private DAYS_ON_SITE				daysOnSite;
	private String	 					renumeration;
	private LocalDate 					availableFromDate;
	private Set<String>					coreSkills				= new HashSet<>();
	private int							yearsExperience;
	private String 						description;
	private Set<LANGUAGE>				spokenLanguages			= new HashSet<>();
	private String 						comments;
	private LocalDate					created;
	
	/**
	* Constructor based upon a builder 
	* @param builder - Contains initialization values
	*/
	public OfferedCandidateEntity(OfferedCandidateEntityBuilder builder) {
		
		this.id 					= builder.id;
		this.recruiterId 			= builder.recruiterId;
		this.candidateRoleTitle 	= builder.candidateRoleTitle;
		this.country 				= builder.country;
		this.location	 			= builder.location;
		this.contractType 			= builder.contractType;
		this.daysOnSite 			= builder.daysOnSite;
		this.renumeration 			= builder.renumeration;
		this.availableFromDate 		= builder.availableFromDate;
		this.coreSkills				= builder.coreSkills;
		this.yearsExperience 		= builder.yearsExperience;
		this.description 			= builder.description;
		this.spokenLanguages 		= builder.spokenLanguages;
		this.comments 				= builder.comments;
		this.created 				= builder.created;
		
	}
	
	/**
	* Returns the Unique Id of the Offered Candidate
	* @return unique id
	*/
	public UUID getId(){
		return this.id;
	}
	
	/**
	* Returns the Unique Id of the Recruiter offering the Candidate
	* @return id of recruiter
	*/
	public String getRecruiterId(){
		return this.recruiterId;
	}
	
	/**
	* Returns the title of the Role the Candidate performs
	* @return Candidates title
	*/
	public String getCandidateRoleTitle(){
		return this.candidateRoleTitle;
	}
	
	/** 
	* Returns the Country the Candidate is available in
	* @return Country of Candidate
	*/
	public Country getCountry(){
		return this.country;
	}
	
	/**
	* Returns the Location where the Candidate is avaiable to work in
	* @return Location of Candidate
	*/
	public String getLocation(){
		return this.location;
	}
	
	/**
	* Returns the type of Contract the Candidate is looking for
	* @return Contract Type
	*/
	public ContractType getContractType(){
		return this.contractType;
	}
	
	/**
	* Returns the max number of days the Candidate is available on site
	* @return Max number of days on site
	*/
	public DAYS_ON_SITE	getDaysOnSite(){
		return this.daysOnSite;
	}
	
	/**
	* Returns the renumeration requested for the Candidate
	* @return Renumeration for the Candidate
	*/
	public String getRenumeration(){
		return this.renumeration;
	}
	
	/**
	* Returns the Date the Candidate is available from
	* @return First date of availability
	*/
	public LocalDate getAvailableFromDate(){
		return this.availableFromDate;
	}
	
	/**
	* Returns the Candidates core skills
	* @return candidates skills
	*/
	public Set<String> getCoreSkills(){
		return this.coreSkills;
	}
	
	/**
	* Returns the Candidates expereince in years
	* @return years experience
	*/
	public int getYearsExperience(){
		return this.yearsExperience;
	}
	
	/**
	* Returns a description of the Candidate
	* @return description of the Candidate
	*/
	public String getDescription(){
		return this.description;
	}
	
	/**
	* Returns languages spoken by the Candidate
	* @return Candidates Languages
	*/
	public Set<LANGUAGE> getSpokenLanguages(){
		return this.spokenLanguages;
	}
	
	/**
	* Returns comments from the recruiter
	* @return additional comments
	*/
	public String getComments(){
		return this.comments;
	}
	
	/**
	* Returns the Date the OfferedCandidate was added to the system
	* @return Creation date of the OfferedCandidate
	*/
	public LocalDate getCreated(){
		return this.created;
	}
	
	/**
	* Returns a Builder for the OfferedCandidateEntity class
	* @return Builder
	*/
	public static OfferedCandidateEntityBuilder builder() {
		return new OfferedCandidateEntityBuilder();
	}

	/**
	* Builder for the OfferedCandidateEntity class
	* @author K Parkings
	*/
	public static class OfferedCandidateEntityBuilder{
		
		private UUID 						id;
		private String 						recruiterId;
		private String 						candidateRoleTitle;
		private Country	 					country;
		private String						location;
		private ContractType 				contractType;
		private DAYS_ON_SITE				daysOnSite;
		private String	 					renumeration;
		private LocalDate 					availableFromDate;
		private Set<String>					coreSkills				= new HashSet<>();
		private int							yearsExperience;
		private String 						description;
		private Set<LANGUAGE>				spokenLanguages			= new HashSet<>();
		private String 						comments;
		private LocalDate					created;
		
		/**
		* Sets the unique identifier for the offered candidate
		* @param id - unique Id
		* @return Builder
		*/
		public OfferedCandidateEntityBuilder id(UUID id){
			this.id = id;
			return this;
		}
		
		/**
		* Sets the unique Id of the Recruiter offering the Candidate
		* @param recruiterId - Unique Id of the Recruiter
		* @return Builder
		*/
		public OfferedCandidateEntityBuilder recruiterId(String recruiterId){
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the title of the role performed by the Candidate
		* @param candidateRoleTitle - Title of role performed by the Candidate
		* @return Builder
		*/
		public OfferedCandidateEntityBuilder candidateRoleTitle(String candidateRoleTitle){
			this.candidateRoleTitle = candidateRoleTitle;
			return this;
		}
		
		/**
		* Sets the Country the Candidate is available in
		* @param country - Country where the Candidate is available for work
		* @return Builder
		*/
		public OfferedCandidateEntityBuilder country(Country country){
			this.country = country;
			return this;
		}
		
		/**
		* Sets the Location where the Candidate is available for work
		* @param location - Location where the Candidate is available
		* @return Builder
		*/
		public OfferedCandidateEntityBuilder location(String location){
			this.location = location;
			return this;
		}
		
		/**
		* Sets the type of Contract the Candidate is looking for
		* @param contractType - Type of contract the Candidate is looking for
		* @return Builder
		*/
		public OfferedCandidateEntityBuilder contractType(ContractType contractType){
			this.contractType = contractType;
			return this;
		}
		
		/**
		* Sets the number of days the Candidate is prepared to work on site
		* @param daysOnSite - Days Candidate will work on site
		* @return Builder
		*/
		public OfferedCandidateEntityBuilder daysOnSite(DAYS_ON_SITE daysOnSite){
			this.daysOnSite = daysOnSite;
			return this;
		}
		
		/**
		* Sets the renumeration requested for the Candidate
		* @param renumeration - renumeration requested for the Candidate
		* @return Builder
		*/
		public OfferedCandidateEntityBuilder renumeration(String renumeration){
			this.renumeration = renumeration;
			return this;
		}
		
		/**
		* Sets the Date the Candidate is available from
		* @param availableFromDate - Date Candidate is available from 
		* @return Builder
		*/
		public OfferedCandidateEntityBuilder availableFromDate(LocalDate availableFromDate){
			this.availableFromDate = availableFromDate;
			return this;
		}
		
		/**
		* Sets the Core skills offered by the Candidate
		* @param coreSkills - Candidates core skills
		* @return Builder
		*/
		public OfferedCandidateEntityBuilder coreSkills(Set<String> coreSkills) {
			this.coreSkills.clear();
			this.coreSkills.addAll(coreSkills);
			return this;
		}

		/**
		* Sets the number of years experience the Candidate has
		* @param yearsExperience - Candidates experience in years
		* @return Builder
		*/
		public OfferedCandidateEntityBuilder yearsExperience(int yearsExperience){
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets a description of the Candidate
		* @param description - Candidate Description
		* @return Builder
		*/
		public OfferedCandidateEntityBuilder description(String description){
			this.description = description;
			return this;
		}
		
		/**
		* Sets the Languages spoken by the Candidate
		* @param spokenLanguages - Languages spoken by the Candidate
		* @return Builder
		*/
		public OfferedCandidateEntityBuilder spokenLanguages(Set<LANGUAGE> spokenLanguages) {
			this.spokenLanguages.clear();
			this.spokenLanguages.addAll(spokenLanguages);
			return this;
		}
		
		/**
		* Sets additional comments from the Recruiter
		* @param comments - Recruiters comments
		* @return Builder
		*/
		public OfferedCandidateEntityBuilder comments(String comments){
			this.comments = comments;
			return this;
		}
		
		/**
		* Sets the Date the OfferedCandidate was added to the site
		* @param created - When Candidate was added to site
		* @return Builder
		*/
		public OfferedCandidateEntityBuilder created(LocalDate created){
			this.created = created;
			return this;
		}
		
		/**
		* Returns a new instance initialized with the values
		* in the Builder 
		* @return Builder
		*/
		public OfferedCandidateEntity build() {
			return new OfferedCandidateEntity(this);
		}
		
	}
	
	/**
	* Converts the Entity representation to the Domain representation of OfferedCandidate
	* @param entity - Entity representation
	* @return Domain representation
	*/
	public static OfferedCandidate convertFromEntity(OfferedCandidateEntity entity) {
		return OfferedCandidate
				.builder()
					.availableFromDate(entity.getAvailableFromDate())
					.candidateRoleTitle(entity.getCandidateRoleTitle())
					.comments(entity.getComments())
					.contractType(entity.getContractType())
					.coreSkills(entity.getCoreSkills())
					.country(entity.getCountry())
					.created(entity.getCreated())
					.daysOnSite(entity.getDaysOnSite())
					.description(entity.getDescription())
					.id(entity.getId())
					.location(entity.getLocation())
					.recruiterId(entity.getRecruiterId())
					.renumeration(entity.getRenumeration())
					.spokenLanguages(entity.getSpokenLanguages())
					.yearsExperience(entity.getYearsExperience())
				.build();
	}
	
	/**
	* Converts the Domain representation to the Entity representation of OfferedCandidate
	* @param candidate
	* @return
	*/
	public static OfferedCandidateEntity convertToEntity(OfferedCandidate candidate, Optional<OfferedCandidateEntity> originalEntity) {
		
		if (originalEntity.isEmpty()) {
			return OfferedCandidateEntity
					.builder()
						.availableFromDate(candidate.getavailableFromDate())
						.candidateRoleTitle(candidate.getcandidateRoleTitle())
						.comments(candidate.getcomments())
						.contractType(candidate.getcontractType())
						.coreSkills(candidate.getcoreSkills())
						.country(candidate.getcountry())
						.created(candidate.getCreated())
						.daysOnSite(candidate.getDaysOnSite())
						.description(candidate.getdescription())
						.id(candidate.getId())
						.location(candidate.getlocation())
						.recruiterId(candidate.getRecruiterId())
						.renumeration(candidate.getrenumeration())
						.spokenLanguages(candidate.getspokenLanguages())
						.yearsExperience(candidate.getyearsExperience())
					.build();
		}
		
		OfferedCandidateEntity entity = originalEntity.get();
		
		entity.candidateRoleTitle	= candidate.getcandidateRoleTitle();
		entity.country 				= candidate.getcountry();
		entity.location 			= candidate.getlocation();
		entity.contractType 		= candidate.getcontractType();
		entity.daysOnSite 			= candidate.getDaysOnSite();
		entity.renumeration 		= candidate.getrenumeration();
		entity.availableFromDate	= candidate.getavailableFromDate();
		entity.coreSkills 			= candidate.getcoreSkills();
		entity.yearsExperience 		= candidate.getyearsExperience();
		entity.description 			= candidate.getdescription();
		entity.spokenLanguages 		= candidate.getspokenLanguages();
		entity.comments 			= candidate.getcomments();
		
		return entity;
		
	}
	
}