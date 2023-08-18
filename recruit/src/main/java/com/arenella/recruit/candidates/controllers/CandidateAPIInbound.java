package com.arenella.recruit.candidates.controllers;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.CandidateBuilder;
import com.arenella.recruit.candidates.beans.Candidate.DAYS_ON_SITE;
import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Class represents a Recruitment Candidate. A Candidate that 
* can be place on a project
* @author K Parkings
*/
@JsonDeserialize(builder=CandidateAPIInbound.CandidateAPIInboundBuilder.class)
public class CandidateAPIInbound {

	private String 			candidateId;
	private String 			firstname;
	private String 			surname;
	private String 			email;
	private String			roleSought;
	private FUNCTION		function;
	private COUNTRY 		country;
	private String 			city;
	private PERM 			perm;
	private FREELANCE 		freelance;
	private int				yearsExperience;
	private boolean 		available					= true;
	private Set<String> 	skills						= new LinkedHashSet<>();
	private Set<Language> 	languages					= new LinkedHashSet<>();
	private String 			comments;
	private String 			introduction;
	private DAYS_ON_SITE	daysOnSite;
	private Rate			rateContract;
	private Rate			ratePerm;
//	private Rate			rateToPerm;
	private LocalDate 		availableFromDate;
	
	/**
	* Constructor based upon a builder
	* @param builder = Contains initialization information
	*/
	public CandidateAPIInbound(CandidateAPIInboundBuilder builder) {
		
		this.candidateId				= builder.candidateId;
		this.firstname					= builder.firstname;
		this.surname					= builder.surname;
		this.email						= builder.email;
		this.function				 	= builder.function;
		this.roleSought					= builder.roleSought;
		this.country					= builder.country;
		this.city 						= builder.city;
		this.perm 						= builder.perm;
		this.freelance 					= builder.freelance;
		this.yearsExperience 			= builder.yearsExperience;
		this.available 					= builder.available;
		this.comments					= builder.comments;
		this.introduction				= builder.introduction;
		this.daysOnSite					= builder.daysOnSite;
		this.rateContract				= builder.rateContract;
		this.ratePerm					= builder.ratePerm;
		this.availableFromDate			= builder.availableFromDate;
		
		this.skills.addAll(builder.skills);
		this.languages.addAll(builder.languages);
	
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
	* Returns the role the Candidate is looking for
	* @return Role sought by the Candidate
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
	public FREELANCE  isFreelance() {
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
	* @return Whether the Candidate is avilable for work
	*/
	public boolean isAvailable() {
		return this.available;
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
	* @return
	*/
	public DAYS_ON_SITE	getDaysOnSite() {
		return this.daysOnSite;
	}
	
	/**
	* If available the contract rate the Candidate is 
	* looking for
	* @return contract Rate
	*/
	public Optional<Rate> getRateContract(){
		return Optional.ofNullable(this.rateContract);
	}
	
	/**
	* If available the perm salary the Candidate is 
	* looking for
	* @return salary
	*/
	public Optional<Rate> getRatePerm(){
		return Optional.ofNullable(this.ratePerm);
	}
	
	/**
	* If available the max perm salary the Candidate is 
	* looking for
	* @return max salary
	*/
	//public Optional<Rate> getRateToPerm(){
	//	return Optional.ofNullable(this.rateToPerm);
	//}
	
	/**
	* returns the date the Candidate is available from. If not specified 
	* uses the current date
	* @return When the candidate is available from
	*/
	public LocalDate getAvailableFromDate(){
		return Optional.ofNullable(this.availableFromDate).orElse(LocalDate.now());
	}
	
	/**
	* Builder for the Candidate class
	* @return A Builder for the Candidate class
	*/
	public static final CandidateAPIInboundBuilder builder() {
		return new CandidateAPIInboundBuilder();
	}
	
	/**
	* Builder Class for the CandidateAPIInbound Class
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class CandidateAPIInboundBuilder {
		
		private String 			candidateId;
		private String 			firstname;
		private String			surname;
		private String 			email;
		private String			roleSought;
		private FUNCTION		function;
		private COUNTRY 		country;
		private String 			city;
		private PERM 			perm;
		private FREELANCE 		freelance;
		private int				yearsExperience;
		private boolean 		available					= true;
		private Set<String> 	skills						= new LinkedHashSet<>();
		private Set<Language> 	languages					= new LinkedHashSet<>();
		private String 			comments;
		private String 			introduction;
		private DAYS_ON_SITE	daysOnSite;
		private Rate			rateContract;
		private Rate			ratePerm;
		private LocalDate 		availableFromDate;
		
		/**
		* Sets the candidates Unique identifier in the System
		* @param candidateId - Unique identifier of the Candidate
		* @return Builder
		*/
		public CandidateAPIInboundBuilder candidateId(String candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets the First name of the Candidate
		* @param firstname - Candidates first name
		* @return Builder
		*/
		public CandidateAPIInboundBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the Surname of the Candidate
		* @param surname - Candidates Surname
		* @return Builder
		*/
		public CandidateAPIInboundBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Email address of the Candidate
		* @param email - Email address
		* @return Builder
		*/
		public CandidateAPIInboundBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the Role sought by the Candidate 
		* @param roleSought - Role sought by the Candidate
		* @return Builder
		*/
		public CandidateAPIInboundBuilder roleSought(String roleSought) {
			this.roleSought = roleSought;
			return this;
		}
		
		/**
		* Sets the function the candidate performs
		* @param function - Function performed by the Candidate
		* @return Builder
		*/
		public CandidateAPIInboundBuilder function(FUNCTION function) {
			this.function = function;
			return this;
		}
		
		/**
		* Returns the Country where the Candidate is located
		* @param country - Country where the Candidate is located
		* @return Builder
		*/
		public CandidateAPIInboundBuilder country(COUNTRY country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets the City where the Candidate is located
		* @param city - City where the Candidate is located
		* @return Builder
		*/
		public CandidateAPIInboundBuilder city(String city) {
			this.city = city;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for perm roles
		* @param freelance - Whether or not the Candidate is interested in perm roles
		* @return Builder
		*/
		public CandidateAPIInboundBuilder perm(PERM perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param freelance - Whether or not the Candidate is interested in freelance roles
		* @return Builder
		*/
		public CandidateAPIInboundBuilder freelance(FREELANCE freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Sets the number of years experience the Candidate has in the industry
		* @param yearsExperience - years of work experience in the industry
		* @return Builder
		*/
		public CandidateAPIInboundBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets whether or not the Candidate is currently available
		* @param available - whether or not the Candidate is currently looking for work
		* @return Builder
		*/
		public CandidateAPIInboundBuilder available(boolean available) {
			this.available = available;
			return this;
		}
			
		/**
		* Sets the skills that the Candidate has
		* @param skills - Skills Candidate has experience with
		* @return Builder
		*/
		public CandidateAPIInboundBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Sets the languages spoken by the Candidate
		* @param languages - Languages spoken by the Candidate
		* @return Builder
		*/
		public CandidateAPIInboundBuilder languages(Set<Language> languages) {
			this.languages.clear();
			this.languages.addAll(languages);
			return this;
		}
		
		/**
		* Sets comments relating to the Candidate
		* @param comments - additional notes/comments
		* @return Builder
		*/
		public CandidateAPIInboundBuilder comments(String comments) {
			this.comments = comments;
			return this;
		}
		
		/**
		* Sets an introduction about the Candidate and their experience
		* @param introduction - Introduction to the Candidate
		* @return Builder
		*/
		public CandidateAPIInboundBuilder introduction(String introduction) {
			this.introduction = introduction;
			return this;
		}
		
		/**
		* Sets the max number of days the Candidate is prepared to work onsite
		* @param daysOnSite - Max number of days onsite
		* @return Builder
		*/
		public CandidateAPIInboundBuilder daysOnSite(DAYS_ON_SITE daysOnSite) {
			this.daysOnSite = daysOnSite;
			return this;
		}
		
		/**
		* Sets the contract rate the Candidate will accept
		* @param rateContract - rate
		* @return Builder
		*/
		public CandidateAPIInboundBuilder rateContract(Rate rateContract) {
			this.rateContract = rateContract;
			return this;
		}
		
		/**
		* Sets the perm salary the Candidate will accept
		* @param ratePerm - salary
		* @return Builder
		*/
		public CandidateAPIInboundBuilder ratePerm(Rate ratePerm) {
			this.ratePerm = ratePerm;
			return this;
		}
		
		/**
		* Sets the Date the Candidate is available from
		* @param availableFromDate - When the Candidate will be available from
		* @return Builder
		*/
		public CandidateAPIInboundBuilder availableFromDate(LocalDate availableFromDate) {
			this.availableFromDate = availableFromDate;
			return this;
		}
		
		/**
		* Returns an instance of Candidate initialized with the 
		* values in the builder
		* @return Initialized instance of Candidate
		*/
		public CandidateAPIInbound build() {
			return new CandidateAPIInbound(this);
		}
	}
	
	/**
	* Converts an incoming API representation of a Canidate to the 
	* Domain version
	* @param candiateAPIInbound - API Incoming version of the Candidate
	* @return Domain version of the Candidate
	*/
	public static Candidate convertToCandidate(CandidateAPIInbound candiateAPIInbound) {
		
		CandidateBuilder builder = Candidate.builder();
		
				builder
					.available(candiateAPIInbound.isAvailable())
					.candidateId(candiateAPIInbound.getCandidateId())
					.firstname(candiateAPIInbound.getFirstname())
					.surname(candiateAPIInbound.getSurname())
					.email(candiateAPIInbound.getEmail())
					.roleSought(candiateAPIInbound.roleSought)
					.function(candiateAPIInbound.getFunction())
					.city(candiateAPIInbound.getCity())
					.country(candiateAPIInbound.getCountry())
					.freelance(candiateAPIInbound.isFreelance())
					.perm(candiateAPIInbound.isPerm())
					.yearsExperience(candiateAPIInbound.getYearsExperience())
					.skills(candiateAPIInbound.getSkills())
					.languages(candiateAPIInbound.getLanguages().stream().map(lang -> Language.builder().language(lang.getLanguage()).level(lang.getLevel()).build()).collect(Collectors.toCollection(HashSet::new)))
					.comments(candiateAPIInbound.getComments())
					.introduction(candiateAPIInbound.getIntroduction())
					.availableFromDate(candiateAPIInbound.getAvailableFromDate())
					.daysOnSite(candiateAPIInbound.getDaysOnSite());
					
					if (candiateAPIInbound.getRateContract().isPresent()) {
						builder.rateContract(candiateAPIInbound.getRateContract().get());
					}
					
					if (candiateAPIInbound.getRatePerm().isPresent()) {
						builder.ratePerm(candiateAPIInbound.getRatePerm().get());
					}
					
					Candidate c = builder.build();
					
					return builder.build();
		
	}
	
}