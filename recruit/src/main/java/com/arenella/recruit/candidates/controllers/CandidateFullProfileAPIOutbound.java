package com.arenella.recruit.candidates.controllers;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Candidate.Photo;
import com.arenella.recruit.candidates.beans.Candidate.Photo.PHOTO_FORMAT;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Outboud API representation of a Candidate as a Profile
* @author K Parkings
*/
public class CandidateFullProfileAPIOutbound implements CandidateAPIOutbound{

	private String 					candidateId;
	private FUNCTION				function;
	private String					roleSought;
	private COUNTRY 				country;
	private String 					city;
	private PERM 					perm;
	private FREELANCE 				freelance;
	private int						yearsExperience;
	private boolean 				available;
	private LocalDate 				lastAvailabilityCheck;
	private Set<String> 			skills						= new LinkedHashSet<>();
	private Set<Language> 			languages					= new LinkedHashSet<>();
	private String					firstname;
	private String					surname;
	private RateAPIOutbound			rate;
	private PhotoAPIOutbound		photo;
	private String					introduction;
	private String					email;
	
	/**
	* Constructor based upon a builder
	* @param builder = Contains initialization information
	*/
	public CandidateFullProfileAPIOutbound(CandidateFullProfileAPIOutboundBuilder builder) {
		
		this.candidateId				= builder.candidateId;
		this.function				 	= builder.function;
		this.roleSought					= builder.roleSought;
		this.country					= builder.country;
		this.city 						= builder.city;
		this.perm	 					= builder.perm;
		this.freelance 					= builder.freelance;
		this.yearsExperience 			= builder.yearsExperience;
		this.available 					= builder.available;
		this.lastAvailabilityCheck 		= builder.lastAvailabilityCheck;
		this.firstname					= builder.firstname;
		this.surname					= builder.surname;
		this.rate						= builder.rate;
		this.photo						= builder.photo;
		this.introduction				= builder.introduction;
		this.email					 	= builder.email;
		
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

	/**
	* Returns the function the Candidate performs
	* @return function the Candidate performs
	*/
	public FUNCTION getFunction() {
		return this.function;
	}
	
	/**
	* Returns the role the Candidate is looking for
	* @return role Sought by the Candidate
	*/
	public String getRoleSought() {
		return this.roleSought;
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
	public FREELANCE getFreelance() {
		return this.freelance;
	}
	
	/**
	* Returns whether or not the Candidate is interested in permanent roles
	* @return Whether or not the Candidate is interested in permanent roles
	*/
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
	* Returns the Date of the last time the Candidate was contacted to check 
	* that they were still available for a new role
	* @return Date of last availability check
	*/
	public LocalDate getLastAvailabilityCheckOn() {
		return this.lastAvailabilityCheck;
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
	* Returns the Candidates firstname
	* @return firstname of the Candidate
	*/
	public String getFirstname() {
		return this.firstname;
	}
	
	/**
	* Returns the Surname of the Candidate
	* @return Candidates Surname
	*/
	public String getSurname() {
		return this.surname;
	}
	
	/**
	* Returns the Rate charged by the Candidate
	* @return Rate charged by the Candidate
	*/
	public Optional<RateAPIOutbound> getRate(){
		return Optional.ofNullable(this.rate);
	}
	
	/**
	* If available returns the Candidates profile Photo
	* @return Profile Photo
	*/
	public Optional<PhotoAPIOutbound> getPhoto(){
		return Optional.ofNullable(this.photo);
	}
	
	/**
	* Returns the Candidates introduction about themselves
	* @return Candidate introduction
	*/
	public String getIntroduction() {
		return this.introduction;
	}
	
	/**
	* Candidates Email address
	* @return
	*/
	public String getEmail() {
		return this.email;
	}
	
	/**
	* Builder for the  class
	* @return A Builder for the  class
	*/
	public static final CandidateFullProfileAPIOutboundBuilder builder() {
		return new CandidateFullProfileAPIOutboundBuilder();
	}
	
	/**
	* Builder Class for the CandidateFullProfileAPIOutbound Class
	* @author K Parkings
	*/
	public static class CandidateFullProfileAPIOutboundBuilder {
		
		private String 					candidateId;
		private FUNCTION				function;
		private String					roleSought;
		private COUNTRY 				country;
		private String 					city;
		private PERM 					perm;
		private FREELANCE 				freelance;
		private int						yearsExperience;
		private boolean 				available;
		private LocalDate 				lastAvailabilityCheck;
		private Set<String> 			skills						= new LinkedHashSet<>();
		private Set<Language> 			languages					= new LinkedHashSet<>();
		private String					firstname;
		private String					surname;
		private RateAPIOutbound			rate;
		private PhotoAPIOutbound		photo;
		private String					introduction;
		private String					email;
		
		/**
		* Sets the candidates Unique identifier in the System
		* @param candidateId - Unique identifier of the Candidate
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder candidateId(String candidateId) {
			this.candidateId = candidateId;
			return this;
		}
		
		/**
		* Sets the function the candidate performs
		* @param function - Function performed by the Candidate
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder function(FUNCTION function) {
			this.function = function;
			return this;
		}
		
		/**
		* Sets the role sought by the Candidate
		* @param roleSought - Role the Candidate is looking for
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder roleSought(String roleSought) {
			this.roleSought = roleSought;
			return this;
		}
		
		/**
		* Returns the Country where the Candidate is located
		* @param country - Country where the Candidate is located
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder country(COUNTRY country) {
			this.country = country;
			return this;
		}
		
		/**
		* Sets the City where the Candidate is located
		* @param city - City where the Candidate is located
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder city(String city) {
			this.city = city;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for perm roles
		* @param freelance - Whether or not the Candidate is interested in perm roles
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder perm(PERM perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets whether the Candidate is looking for freelance roles
		* @param freelance - Whether or not the Candidate is interested in freelance roles
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder freelance(FREELANCE freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Sets the number of years experience the Candidate has in the industry
		* @param yearsExperience - years of work experience in the industry
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets whether or not the Candidate is currently available
		* @param available - whether or not the Candidate is currently looking for work
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder available(boolean available) {
			this.available = available;
			return this;
		}
		
		/**
		* Sets the Date of the last time the Candidates availability 
		* was checked
		* @param lastAvailabilityCheck - Date of last availability check
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder lastAvailabilityCheck(LocalDate lastAvailabilityCheck) {
			this.lastAvailabilityCheck = lastAvailabilityCheck;
			return this;
		}
		
		/**
		* Sets the skills that the Candidate has
		* @param skills - Skills Candidate has experience with
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Sets the languages spoken by the Candidate
		* @param languages - Languages spoken by the Candidate
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder languages(Set<Language> languages) {
			this.languages.clear();
			this.languages.addAll(languages);
			return this;
		}
		
		/**
		* Sets the Candidates firstname
		* @param firstname - Firstname of the Candidate
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder firstname(String firstname) {
			this.firstname = firstname;
			return this;
		}
		
		/**
		* Sets the Candidates surname
		* @param surname - surname of the Candidate
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		/**
		* Sets candidates Email address
		* @param email - email address
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		/**
		* Sets the Rate charged by the candidate
		* @param rate - Rate charged by the Candidate
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder rate(RateAPIOutbound rate) {
			this.rate = rate;
			return this;
		}
		
		/**
		* Sets the Photo
		* @param photo - Profile photo
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder photo(PhotoAPIOutbound photo) {
			this.photo = photo;
			return this;
		}
		
		/**
		* Sets the Candidates introduction about themselves
		* @param introduction - intro
		* @return Builder
		*/
		public CandidateFullProfileAPIOutboundBuilder introduction(String introduction) {
			this.introduction = introduction;
			return this;
		}
		
		/**
		* Returns an instance of Candidate initialized with the 
		* values in the builder
		* @return Initialized instance of Candidate
		*/
		public CandidateFullProfileAPIOutbound build() {
			return new CandidateFullProfileAPIOutbound(this);
		}
	}
	
	/**
	* Converts an incoming API representation of a Candidate to the 
	* Domain version
	* @param candiateAPIInbound - API Incoming version of the Candidate
	* @return Domain version of the Candidate
	*/
	public static CandidateFullProfileAPIOutbound convertFromDomain(Candidate candidate) {
		
		return CandidateFullProfileAPIOutbound
				.builder()
					.candidateId(candidate.getCandidateId())
					.country(candidate.getCountry())
					.function(candidate.getFunction())
					.roleSought(candidate.getRoleSought())
					.city(candidate.getCity())
					.freelance(candidate.isFreelance())
					.perm(candidate.isPerm())
					.languages(candidate.getLanguages())
					.skills(candidate.getSkills())
					.yearsExperience(candidate.getYearsExperience())
					.available(candidate.isAvailable())
					.lastAvailabilityCheck(candidate.getLastAvailabilityCheckOn())
					.firstname(candidate.getFirstname())
					.surname(candidate.getSurname())
					.rate(RateAPIOutbound.convertFromDomain(candidate.getRate()))
					.photo(PhotoAPIOutbound.convertFromDomain(candidate.getPhoto()))
					.introduction(candidate.getIntroduction())
					.email(candidate.getEmail())
				.build();
		
	}
	
	/**
	* Class represents a Photo
	* @author K Parkings
	*/
	public static class PhotoAPIOutbound{
		
		private final byte[] 		imageBytes;
		private final PHOTO_FORMAT 	format;
	
		/**
		* Class represents an uploaded Photo
		* @param imageBytes - bytes of actual file
		* @param format		- format of photo file
		*/
		public PhotoAPIOutbound(byte[] imageBytes, PHOTO_FORMAT format) {
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
	
		/**
		* Converts the Domain representation of the Profile to the 
		* APIOutbound representation
		* @param profile - Domain representation
		* @return APIOutbound representation
		*/
		public static PhotoAPIOutbound convertFromDomain(Optional<Photo> photo) {
			
			if (photo.isEmpty()) {
				return null;
			}
			
			return new PhotoAPIOutbound(photo.get().getImageBytes(), photo.get().getFormat());
		}
		
	}
	
}