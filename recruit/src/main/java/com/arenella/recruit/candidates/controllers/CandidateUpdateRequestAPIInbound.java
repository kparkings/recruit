package com.arenella.recruit.candidates.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.DAYS_ON_SITE;
import com.arenella.recruit.candidates.beans.Candidate.SECURITY_CLEARANCE_TYPE;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;
import com.arenella.recruit.candidates.beans.CandidateUpdateRequest;
import com.arenella.recruit.candidates.beans.CandidateUpdateRequest.CandidateUpdateRequestBuilder;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* Request to update a Candidates profile information
* @author K Parkings
*/
@JsonDeserialize(builder=CandidateUpdateRequestAPIInbound.CandidateUpdateRequestAPIInboundBuilder.class)
public class CandidateUpdateRequestAPIInbound {

	private String 					firstname;
	private String 					surname;
	private String 					email;
	private String					roleSought;
	private Set<FUNCTION>			functions				 	= new LinkedHashSet<>();
	private COUNTRY 				country;
	private String 					city;
	private PERM 					perm;
	private FREELANCE 				freelance;
	private int						yearsExperience;
	private Set<Language> 			languages					= new LinkedHashSet<>();
	private String					introduction;
	private Set<String>				skills						= new LinkedHashSet<>();
	private String 					comments;
	private DAYS_ON_SITE			daysOnSite;
	private Rate					rateContract;
	private Rate					ratePerm;
	private LocalDate 				availableFromDate;
	private SECURITY_CLEARANCE_TYPE securityClearance;
	private boolean					requiresSponsorship;
	
	/**
	* Constructor based upon a builder
	* @param builder = Contains initialization information
	*/
	public CandidateUpdateRequestAPIInbound(CandidateUpdateRequestAPIInboundBuilder builder) {
		
		this.firstname					= builder.firstname;
		this.surname					= builder.surname;
		this.email						= builder.email;
		this.roleSought					= builder.roleSought;
		this.country					= builder.country;
		this.city 						= builder.city;
		this.perm 						= builder.perm;
		this.freelance 					= builder.freelance;
		this.yearsExperience 			= builder.yearsExperience;
		this.introduction				= builder.introduction;
		this.skills						= builder.skills;
		this.introduction				= builder.introduction;
		this.daysOnSite					= builder.daysOnSite;
		this.rateContract				= builder.rateContract;
		this.ratePerm					= builder.ratePerm;
		this.availableFromDate			= builder.availableFromDate;
		this.comments					= builder.comments;
		this.requiresSponsorship		= builder.requiresSponsorship;
		this.securityClearance			= builder.securityClearance;
		
		this.languages.addAll(builder.languages);
		this.functions.addAll(builder.functions);
		
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
	* Returns the functions the Candidate performs
	* @return functions the Candidate performs
	*/
	public Set<FUNCTION> getFunctions() {
		return this.functions.stream().filter(f -> f != null).collect(Collectors.toSet());
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
	* If available thecontract rate the Candidate is 
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
	* returns the date the Candidate is available from. If not specified 
	* uses the current date
	* @return When the candidate is available from
	*/
	public LocalDate getAvailableFromDate(){
		return Optional.ofNullable(this.availableFromDate).orElse(LocalDate.now());
	}
	
	/**
	* Returns the Candidates Skills
	* @return Candidates skills
	*/
	public Set<String> getSkills(){
		return this.skills;
	}
	
	/**
	* Returns the Candidate's Security clearance level
	* @return Security clearance
	*/
	public SECURITY_CLEARANCE_TYPE getSecurityClearance() {
		return this.securityClearance;
	}
	
	/**
	* Returns whether the Candidate requires sponsorship
	* @return Whether the Candidate requires sponsorship
	*/
	public boolean getRequiresSponsorship() {
		return this.requiresSponsorship;
	}
	
	/**
	* Builder for the Candidate class
	* @return A Builder for the Candidate class
	*/
	public static final CandidateUpdateRequestAPIInboundBuilder builder() {
		return new CandidateUpdateRequestAPIInboundBuilder();
	}
	
	/**
	* Builder Class for the Candidate Class
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class CandidateUpdateRequestAPIInboundBuilder {
		
		private String 					firstname;
		private String					surname;
		private String 					email;
		private String					roleSought;
		private Set<FUNCTION>			functions				 	= new LinkedHashSet<>();
		private COUNTRY 				country;
		private String 					city;
		private PERM 					perm;
		private FREELANCE 				freelance;
		private int						yearsExperience;
		private Set<Language> 			languages					= new LinkedHashSet<>();
		private String					introduction;
		private Set<String>				skills						= new LinkedHashSet<>();
		private String 					comments;
		private DAYS_ON_SITE			daysOnSite;
		private Rate					rateContract;
		private Rate					ratePerm;
		private LocalDate 				availableFromDate;
		private SECURITY_CLEARANCE_TYPE securityClearance;
		private boolean					requiresSponsorship;
		
		/**
		* Sets the First name of the Candidate
		* @param firstname - Candidates first name
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the Surname of the Candidate
		* @param surname - Candidates Surname
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets the Email address of the Candidate
		* @param email - Email address
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the Role sought by the Candidate 
		* @param roleSought - Role the candidate is looking for
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder roleSought(String roleSought) {
			this.roleSought = roleSought;
			return this;
		}
		
		/**
		* Sets the functions the candidate performs
		* @param function - Function performed by the Candidate
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder functions(Set<FUNCTION> functions) {
			this.functions.clear();
			this.functions.addAll(functions.stream().filter(f -> f != null).collect(Collectors.toSet()));
			return this;
		}
		
		/**
		* Returns the Country where the Candidate is located
		* @param country - Country where the Candidate is located
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder country(COUNTRY country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets the City where the Candidate is located
		* @param city - City where the Candidate is located
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder city(String city) {
			this.city = city;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for perm roles
		* @param freelance - Whether or not the Candidate is interested in perm roles
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder perm(PERM perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param freelance - Whether or not the Candidate is interested in freelance roles
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder freelance(FREELANCE freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Sets the number of years experience the Candidate has in the industry
		* @param yearsExperience - years of work experience in the industry
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets the languages spoken by the Candidate
		* @param languages - Languages spoken by the Candidate
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder languages(Set<Language> languages) {
			this.languages.clear();
			this.languages.addAll(languages);
			return this;
		}
		
		/**
		* Sets the Skills of the Candidate
		* @param skills - Candidates Skills
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Sets comments relating to the Candidate
		* @param comments - additional notes/comments
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder comments(String comments) {
			this.comments = comments;
			return this;
		}
		
		/**
		* Sets an introduction about the Candidate and their experience
		* @param introduction - Introduction to the Candidate
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder introduction(String introduction) {
			this.introduction = introduction;
			return this;
		}
		
		/**
		* Sets the max number of days the Candidate is prepared to work onsite
		* @param daysOnSite - Max number of days onsite
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder daysOnSite(DAYS_ON_SITE daysOnSite) {
			this.daysOnSite = daysOnSite;
			return this;
		}
		
		/**
		* Sets the contract rate the Candidate will accept
		* @param rateFromContract - rate
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder rateContract(Rate rateContract) {
			this.rateContract = rateContract;
			return this;
		}
		
		
		/**
		* Sets the perm salary the Candidate will accept
		* @param rateFromPerm -salary
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder ratePerm(Rate ratePerm) {
			this.ratePerm = ratePerm;
			return this;
		}
		
		/**
		* Sets the Date the Candidate is available from
		* @param availableFromDate - When the Candidate will be available from
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder availableFromDate(LocalDate availableFromDate) {
			this.availableFromDate = availableFromDate;
			return this;
		}
		
		/**
		* Sets the Security Clearance type held by the candidate
		* @param securityClearance - Security clearance type
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder securityClearance(SECURITY_CLEARANCE_TYPE securityClearance) {
			this.securityClearance = securityClearance;
			return this;
		}
		
		/**
		* Sets whether the Candidate requires sponsorship to take on a new role
		* @param requiresSponsorship - Whether sponsorship required
		* @return Builder
		*/
		public CandidateUpdateRequestAPIInboundBuilder requiresSponsorship(boolean requiresSponsorship) {
			this.requiresSponsorship = requiresSponsorship;
			return this;
		}	
		
		/**
		* Returns an instance of Candidate initialized with the 
		* values in the builder
		* @return Initialized instance of Candidate
		*/
		public CandidateUpdateRequestAPIInbound build() {
			return new CandidateUpdateRequestAPIInbound(this);
		}
	}
	
	/**
	* Converts Request from API Inbound representation to Domain representation
	* @param candidateId	- Unique id of the Candidate
	* @param updateRequest	- To concert
	* @return Converted
	* @throws IOException 
	*/
	public static  CandidateUpdateRequest convertToDomain(String candidateId, CandidateUpdateRequestAPIInbound updateRequest, Optional<MultipartFile> profileImage) throws IOException {
		
		CandidateUpdateRequestBuilder builder = CandidateUpdateRequest.builder();
		
		builder
			.candidateId(candidateId)
			.city(updateRequest.getCity())
			.country(updateRequest.getCountry())
			.email(updateRequest.getEmail())
			.firstname(updateRequest.getFirstname())
			.freelance(updateRequest.isFreelance())
			.functions(updateRequest.getFunctions())
			.languages(updateRequest.getLanguages())
			.perm(updateRequest.isPerm())
			.roleSought(updateRequest.roleSought)
			.surname(updateRequest.getSurname())
			.yearsExperience(updateRequest.getYearsExperience())
			.introduction(updateRequest.getIntroduction())
			.skills(updateRequest.getSkills())
			.photoBytes(profileImage.isEmpty() ? null : profileImage.get().getBytes())
			.comments(updateRequest.getComments())
			.introduction(updateRequest.getIntroduction())
			.availableFromDate(updateRequest.getAvailableFromDate())
			.securityClearance(updateRequest.getSecurityClearance())
			.requiresSponsorship(updateRequest.getRequiresSponsorship())
			.daysOnSite(updateRequest.getDaysOnSite());
			
			if (updateRequest.getRateContract().isPresent()) {
				builder.rateContract(new Candidate.Rate(updateRequest.getRateContract().get().getCurrency(), updateRequest.getRateContract().get().getPeriod(), updateRequest.getRateContract().get().getValueMin(), updateRequest.getRateContract().get().getValueMax()));
			}
		
			if (updateRequest.getRatePerm().isPresent()) {
				builder.ratePerm(new Candidate.Rate(updateRequest.getRatePerm().get().getCurrency(), updateRequest.getRatePerm().get().getPeriod(), updateRequest.getRatePerm().get().getValueMin(), updateRequest.getRatePerm().get().getValueMax()));
			}
			
		return builder.build();
	}
	
	/**
	* Class represents a Rate charged by the Candidate
	* for their work
	* @author K Parkings
	*/
	public static class Rate{
		
		private final CURRENCY 	currency;
		private final PERIOD 	period;
		private final float 	valueMin;
		private final float 	valueMax;

		/**
		* Constructor
		* @param currency - Currency being charged
		* @param period   - Unit of charging
		* @param value	  - amount charged per unit
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
	
}
