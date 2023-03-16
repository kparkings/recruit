package com.arenella.recruit.recruiters.beans;

import java.util.LinkedHashSet;
import java.util.Set;

import com.arenella.recruit.recruiters.beans.RecruiterProfile.CONTRACT_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.COUNTRY;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.LANGUAGE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo.PHOTO_FORMAT;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.REC_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.SECTOR;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.TECH;

/**
* API Outbound representation of RecruiterProfile
* @author K Parkings
*/
public class RecruiterProfileAPIOutbound {

	private String 				recruiterId;
	private String				recruiterFirstName;
	private String 				recruiterSurname;
	private Set<COUNTRY> 		recruitsIn				= new LinkedHashSet<>();
	private Set<LANGUAGE>		languagesSpoken			= new LinkedHashSet<>();;
	private PhotoAPIOutbound	profilePhoto;
	private boolean 			visibleToRecruiters;
	private boolean 			visibleToCandidates;
	private boolean 			visibleToPublic;
	private String				companyName;
	private String				jobTitle;
	private int					yearsExperience;
	private String				introduction;
	private Set<SECTOR>			sectors					= new LinkedHashSet<>();;
	private Set<TECH>			coreTech				= new LinkedHashSet<>();;
	private Set<CONTRACT_TYPE> 	recruitsContractTypes	= new LinkedHashSet<>();;
	private REC_TYPE	 		recruiterType;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public RecruiterProfileAPIOutbound(RecruiterProfileAPIOutboundBuilder builder) {
		
		this.recruiterId 				= builder.recruiterId;
		this.recruiterFirstName 		= builder.recruiterFirstName;
		this.recruiterSurname 			= builder.recruiterSurname;
		this.recruitsIn 				= builder.recruitsIn;
		this.languagesSpoken 			= builder.languagesSpoken;
		this.profilePhoto 				= builder.profilePhoto;
		this.visibleToRecruiters 		= builder.visibleToRecruiters;
		this.visibleToCandidates 		= builder.visibleToCandidates;
		this.visibleToPublic 			= builder.visibleToPublic;
		this.companyName 				= builder.companyName;
		this.jobTitle 					= builder.jobTitle;
		this.yearsExperience 			= builder.yearsExperience;
		this.introduction 				= builder.introduction;
		this.sectors 					= builder.sectors;
		this.coreTech 					= builder.coreTech;
		this.recruitsContractTypes 		= builder.recruitsContractTypes;
		this.recruiterType 				= builder.recruiterType;
		
	}
	
	/**
	* Return the unique id of the Recruiter
	* @return id
	*/
	public String getRecruiterId() {
		return this.recruiterId;
	}
	
	/**
	* Returns the first name of the Recruiter
	* @return first name
	*/
	public String getRecruiterFirstName() {
		return this.recruiterFirstName;
	}
	
	/**
	* Returns the surname of the recruiter
	* @return surname
	*/
	public String getRecruiterSurname() {
		return this.recruiterSurname;
	}
	
	/**
	* Returns the countries the recruiter recruits in
	* @return where the recruiter recruits
	*/
	public Set<COUNTRY> getRecruitsIn() {
		return this.recruitsIn;
	}
	
	/**
	* Returns the languages spoken by the recruiter
	* @return languages spoken
	*/
	public Set<LANGUAGE> getLanguagesSpoken() {
		return this.languagesSpoken;
	}
	
	/**
	* Returns the Recruiters photo
	* @return photo of the recruiter
	*/
	public PhotoAPIOutbound getProfilePhoto() {
		return this.profilePhoto;
	}
	
	/**
	* Returns whether other recruiters can see the profile
	* @return whether other recruiters can see the profile
	*/
	public boolean isVisibleToRecruiters() {
		return this.visibleToRecruiters;
	}
	
	/**
	* Returns whether registered candidates can see the profile
	* @return whether registered candidates can see the profile
	*/
	public boolean isVisibleToCandidates() {
		return this.visibleToCandidates;
	}
	
	/**
	* Returns whether non registered users can see the profile
	* @return whether non registered users can see the profile
	*/
	public boolean isVisibleToPublic() {
		return this.visibleToPublic;
	}
	
	/**
	* Returns the name of the Recruiters company
	* @return name of the company
	*/
	public String getCompanyName() {
		return this.companyName;
	}
	
	/**
	* Returns the Recruiters Job title
	* @return job title
	*/
	public String getJobTitle() {
		return this.jobTitle;
	}
	
	/**
	* Returns the number of years experience the Recruiter 
	* has in recruitment
	* @return years experience
	*/
	public int getYearsExperience() {
		return this.yearsExperience;
	}
	
	/**
	* Returns the Recruiters introduction about themselves
	* @return introduction
	*/
	public String getIntroduction() {
		return this.introduction;
	}
	
	/**
	* Returns the sectors the recruiter recruits in
	* @return sectors
	*/
	public Set<SECTOR> getSectors() {
		return this.sectors;
	}
	
	/**
	* Returns the core Technologies the recruiter recruits for
	* @return tech
	*/
	public Set<TECH> getCoreTech() {
		return this.coreTech;
	}
	
	/**
	* Returns the type of contracts the recruiter recruits for
	* @return contract types
	*/
	public Set<CONTRACT_TYPE> getRecruitsContractTypes() {
		return this.recruitsContractTypes;
	}
	
	/**
	* Returns the Reruiter's own contract type
	* @return type of recruiter
	*/
	public REC_TYPE getRecruiterType() {
		return this.recruiterType;
	}
	
	/**
	* Returns a Builder for the class
	* @return Builder for the class
	*/
	public static RecruiterProfileAPIOutboundBuilder builder() {
		return new RecruiterProfileAPIOutboundBuilder();
	}
	
	/**
	* Builder for the class
	* @author K Parkings
	*/
	public static class RecruiterProfileAPIOutboundBuilder{
		
		private String 				recruiterId;
		private String				recruiterFirstName;
		private String 				recruiterSurname;
		private Set<COUNTRY> 		recruitsIn				= new LinkedHashSet<>();;
		private Set<LANGUAGE>		languagesSpoken			= new LinkedHashSet<>();;
		private PhotoAPIOutbound	profilePhoto;
		private boolean 			visibleToRecruiters;
		private boolean 			visibleToCandidates;
		private boolean 			visibleToPublic;
		private String				companyName;
		private String				jobTitle;
		private int					yearsExperience;
		private String				introduction;
		private Set<SECTOR>			sectors					= new LinkedHashSet<>();;
		private Set<TECH>			coreTech				= new LinkedHashSet<>();;
		private Set<CONTRACT_TYPE> 	recruitsContractTypes	= new LinkedHashSet<>();;
		private REC_TYPE	 		recruiterType;
		
		/**
		* Sets the unique id of the Recruiter
		* @param recruiterId - Unique identifier
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the first name of the recruiter
		* @param recruiterFirstName - first name
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder recruiterFirstName(String recruiterFirstName) {
			this.recruiterFirstName = recruiterFirstName;
			return this;
		}
		
		/**
		* Sets the Surname of the recruiter
		* @param recruiterSurname - surname
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder recruiterSurname(String recruiterSurname) {
			this.recruiterSurname = recruiterSurname;
			return this;
		}
		
		/**
		* Sets the Countries the recruiter recruits candidates in
		* @param recruitsIn - all countries the recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder recruitsIn(Set<COUNTRY> recruitsIn) {
			this.recruitsIn.clear();
			this.recruitsIn.addAll(recruitsIn);
			return this;
		}
		
		/**
		* Sets the languages spoken by the recruiter
		* @param languagesSpoken - spoken languages
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder languagesSpoken(Set<LANGUAGE> languagesSpoken) {
			this.languagesSpoken.clear();
			this.languagesSpoken.addAll(languagesSpoken);
			return this;
		}
		
		/**
		* Sets a Photo of the Recruiter
		* @param profilePhoto - Recruiters photo
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder profilePhoto(PhotoAPIOutbound profilePhoto) {
			this.profilePhoto = profilePhoto;
			return this;
		}
		
		/**
		* Sets whether other recruiters are allowed to view the profile
		* @param visibleToRecruiters - whether other recruiters are allowed to view the profile
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder visibleToRecruiters(boolean visibleToRecruiters) {
			this.visibleToRecruiters = visibleToRecruiters;
			return this;
		}
		
		/**
		* Sets whether registered candidates are allowed to view the profile
		* @param visibleToCandidates whether registered candidates are allowed to view the profile
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder visibleToCandidates(boolean visibleToCandidates) {
			this.visibleToCandidates = visibleToCandidates;
			return this;
		}
		
		/**
		* Sets whether non registered users are allowed to view the profile
		* @param visibleToPublic Whether non registered users are allowed to view the profile
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder visibleToPublic(boolean visibleToPublic) {
			this.visibleToPublic = visibleToPublic;
			return this;
		}
		
		/**
		* Sets the name of the Recruiters company
		* @param companyName - name of company
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder companyName(String companyName) {
			this.companyName = companyName;
			return this;
		}
		
		/**
		* Sets the Job title of the Recruiter
		* @param jobTitle - Recruiters job title
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder jobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
			return this;
		}
		
		/**
		* Sets the number of years experience the recruiter has as a recruiter
		* @param yearsExperience - years of experience
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets a textual introduction to the recruiter for profile visitors 
		* to read
		* @param introduction - Recruiters introduction to self
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder introduction(String introduction) {
			this.introduction = introduction;
			return this;
		}
		
		/**
		* Sets the sectors the Recruiter recruits in
		* @param sectors - Sectors the recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder sectors(Set<SECTOR> sectors) {
			this.sectors.clear();
			this.sectors.addAll(sectors);
			return this;
		}
		
		/**
		* Sets the core tech that the recruiter recruits for
		* @param coreTech - Technologies the recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder coreTech(Set<TECH> coreTech) {
			this.coreTech.clear();
			this.coreTech.addAll(coreTech);
			return this;
		}
		
		/**
		* Sets the type of contracts the recruiter recruits for
		* @param recruitsContractTypes - type of contract recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder recruitsContractTypes(Set<CONTRACT_TYPE> 	recruitsContractTypes) {
			this.recruitsContractTypes.clear();
			this.recruitsContractTypes.addAll(recruitsContractTypes);
			return this;
		}
		
		/**
		* Sets the type of the recruiter.
		* @param recruiterType - What contract type the recruiter has
		* @return Builder
		*/
		public RecruiterProfileAPIOutboundBuilder recruiterType(REC_TYPE 	recruiterType) {
			this.recruiterType = recruiterType;
			return this;
		}
		
		/**
		* Returns intialized instance of class
		* @return instance of class
		*/
		public RecruiterProfileAPIOutbound build() {
			return new RecruiterProfileAPIOutbound(this);
		}
		 		
	
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
		public static PhotoAPIOutbound convertFromDomain(Photo photo) {
			return new PhotoAPIOutbound(photo.getImageBytes(), photo.getFormat());
		}
		
	}
	
	/**
	* Converts the Domain representation of the Profile to the 
	* APIOutbound representation
	* @param profile - Domain representation
	* @return APIOutbound representation
	*/
	public static RecruiterProfileAPIOutbound convertFromDomain(RecruiterProfile profile) {
		return RecruiterProfileAPIOutbound
					.builder()
						.companyName(profile.getCompanyName())
						.coreTech(profile.getCoreTech())
						.introduction(profile.getIntroduction())
						.jobTitle(profile.getJobTitle())
						.languagesSpoken(profile.getLanguagesSpoken())
						.profilePhoto(PhotoAPIOutbound.convertFromDomain(profile.getProfilePhoto()))
						.recruiterFirstName(profile.getRecruiterFirstName())
						.recruiterId(profile.getRecruiterId())
						.recruiterSurname(profile.getRecruiterSurname())
						.recruiterType(profile.getRecruiterType())
						.recruitsContractTypes(profile.getRecruitsContractTypes())
						.recruitsIn(profile.getRecruitsIn())
						.sectors(profile.getSectors())
						.visibleToCandidates(profile.isVisibleToCandidates())
						.visibleToPublic(profile.isVisibleToPublic())
						.visibleToRecruiters(profile.isVisibleToRecruiters())
						.yearsExperience(profile.getYearsExperience())
					.build();
	}
	
}
