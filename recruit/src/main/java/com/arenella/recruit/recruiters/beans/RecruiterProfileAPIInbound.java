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
public class RecruiterProfileAPIInbound {

	private Set<COUNTRY> 		recruitsIn				= new LinkedHashSet<>();
	private Set<LANGUAGE>		languagesSpoken			= new LinkedHashSet<>();;
	private PhotoAPIInbound	profilePhoto;
	private boolean 			visibleToRecruiters;
	private boolean 			visibleToCandidates;
	private boolean 			visibleToPublic;
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
	public RecruiterProfileAPIInbound(RecruiterProfileAPIInboundBuilder builder) {
		
		this.recruitsIn 				= builder.recruitsIn;
		this.languagesSpoken 			= builder.languagesSpoken;
		this.profilePhoto 				= builder.profilePhoto;
		this.visibleToRecruiters 		= builder.visibleToRecruiters;
		this.visibleToCandidates 		= builder.visibleToCandidates;
		this.visibleToPublic 			= builder.visibleToPublic;
		this.jobTitle 					= builder.jobTitle;
		this.yearsExperience 			= builder.yearsExperience;
		this.introduction 				= builder.introduction;
		this.sectors 					= builder.sectors;
		this.coreTech 					= builder.coreTech;
		this.recruitsContractTypes 		= builder.recruitsContractTypes;
		this.recruiterType 				= builder.recruiterType;
		
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
	public PhotoAPIInbound getProfilePhoto() {
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
	public static RecruiterProfileAPIInboundBuilder builder() {
		return new RecruiterProfileAPIInboundBuilder();
	}
	
	/**
	* Builder for the class
	* @author K Parkings
	*/
	public static class RecruiterProfileAPIInboundBuilder{
		
		private Set<COUNTRY> 		recruitsIn				= new LinkedHashSet<>();;
		private Set<LANGUAGE>		languagesSpoken			= new LinkedHashSet<>();;
		private PhotoAPIInbound		profilePhoto;
		private boolean 			visibleToRecruiters;
		private boolean 			visibleToCandidates;
		private boolean 			visibleToPublic;
		private String				jobTitle;
		private int					yearsExperience;
		private String				introduction;
		private Set<SECTOR>			sectors					= new LinkedHashSet<>();;
		private Set<TECH>			coreTech				= new LinkedHashSet<>();;
		private Set<CONTRACT_TYPE> 	recruitsContractTypes	= new LinkedHashSet<>();;
		private REC_TYPE	 		recruiterType;
		
		/**
		* Sets the Countries the recruiter recruits candidates in
		* @param recruitsIn - all countries the recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileAPIInboundBuilder recruitsIn(Set<COUNTRY> recruitsIn) {
			this.recruitsIn.clear();
			this.recruitsIn.addAll(recruitsIn);
			return this;
		}
		
		/**
		* Sets the languages spoken by the recruiter
		* @param languagesSpoken - spoken languages
		* @return Builder
		*/
		public RecruiterProfileAPIInboundBuilder languagesSpoken(Set<LANGUAGE> languagesSpoken) {
			this.languagesSpoken.clear();
			this.languagesSpoken.addAll(languagesSpoken);
			return this;
		}
		
		/**
		* Sets a Photo of the Recruiter
		* @param profilePhoto - Recruiters photo
		* @return Builder
		*/
		public RecruiterProfileAPIInboundBuilder profilePhoto(PhotoAPIInbound profilePhoto) {
			this.profilePhoto = profilePhoto;
			return this;
		}
		
		/**
		* Sets whether other recruiters are allowed to view the profile
		* @param visibleToRecruiters - whether other recruiters are allowed to view the profile
		* @return Builder
		*/
		public RecruiterProfileAPIInboundBuilder visibleToRecruiters(boolean visibleToRecruiters) {
			this.visibleToRecruiters = visibleToRecruiters;
			return this;
		}
		
		/**
		* Sets whether registered candidates are allowed to view the profile
		* @param visibleToCandidates whether registered candidates are allowed to view the profile
		* @return Builder
		*/
		public RecruiterProfileAPIInboundBuilder visibleToCandidates(boolean visibleToCandidates) {
			this.visibleToCandidates = visibleToCandidates;
			return this;
		}
		
		/**
		* Sets whether non registered users are allowed to view the profile
		* @param visibleToPublic Whether non registered users are allowed to view the profile
		* @return Builder
		*/
		public RecruiterProfileAPIInboundBuilder visibleToPublic(boolean visibleToPublic) {
			this.visibleToPublic = visibleToPublic;
			return this;
		}
		
		/**
		* Sets the Job title of the Recruiter
		* @param jobTitle - Recruiters job title
		* @return Builder
		*/
		public RecruiterProfileAPIInboundBuilder jobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
			return this;
		}
		
		/**
		* Sets the number of years experience the recruiter has as a recruiter
		* @param yearsExperience - years of experience
		* @return Builder
		*/
		public RecruiterProfileAPIInboundBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets a textual introduction to the recruiter for profile visitors 
		* to read
		* @param introduction - Recruiters introduction to self
		* @return Builder
		*/
		public RecruiterProfileAPIInboundBuilder introduction(String introduction) {
			this.introduction = introduction;
			return this;
		}
		
		/**
		* Sets the sectors the Recruiter recruits in
		* @param sectors - Sectors the recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileAPIInboundBuilder sectors(Set<SECTOR> sectors) {
			this.sectors.clear();
			this.sectors.addAll(sectors);
			return this;
		}
		
		/**
		* Sets the core tech that the recruiter recruits for
		* @param coreTech - Technologies the recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileAPIInboundBuilder coreTech(Set<TECH> coreTech) {
			this.coreTech.clear();
			this.coreTech.addAll(coreTech);
			return this;
		}
		
		/**
		* Sets the type of contracts the recruiter recruits for
		* @param recruitsContractTypes - type of contract recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileAPIInboundBuilder recruitsContractTypes(Set<CONTRACT_TYPE> 	recruitsContractTypes) {
			this.recruitsContractTypes.clear();
			this.recruitsContractTypes.addAll(recruitsContractTypes);
			return this;
		}
		
		/**
		* Sets the type of the recruiter.
		* @param recruiterType - What contract type the recruiter has
		* @return Builder
		*/
		public RecruiterProfileAPIInboundBuilder recruiterType(REC_TYPE 	recruiterType) {
			this.recruiterType = recruiterType;
			return this;
		}
		
		/**
		* Returns intialized instance of class
		* @return instance of class
		*/
		public RecruiterProfileAPIInbound build() {
			return new RecruiterProfileAPIInbound(this);
		}
		 		
	
	}
	
	/**
	* Class represents a Photo
	* @author K Parkings
	*/
	public static class PhotoAPIInbound{
		
		private final byte[] 		imageBytes;
		private final PHOTO_FORMAT 	format;
	
		/**
		* Class represents an uploaded Photo
		* @param imageBytes - bytes of actual file
		* @param format		- format of photo file
		*/
		public PhotoAPIInbound(byte[] imageBytes, PHOTO_FORMAT format) {
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
		* Converts the APIInbound representation of the Profile to the 
		* domain representation
		* @param profile - Domain representation
		* @return APIOutbound representation
		*/
		public static Photo convertToDomain(PhotoAPIInbound photo) {
			return new Photo(photo.getImageBytes(), photo.getFormat());
		}
		
	}
		
	/**
	* Converts the Domain representation of the Profile to the 
	* APIOutbound representation
	* @param profile - Domain representation
	* @return APIOutbound representation
	*/
	public static RecruiterProfile convertToDomain(RecruiterProfileAPIInbound profile, Recruiter recruiter) {
		return RecruiterProfile
					.builder()
						.companyName(recruiter.getCompanyName())
						.recruiterFirstName(recruiter.getFirstName())
						.recruiterId(recruiter.getUserId())
						.recruiterSurname(recruiter.getSurname())
						.coreTech(profile.getCoreTech())
						.introduction(profile.getIntroduction())
						.jobTitle(profile.getJobTitle())
						.languagesSpoken(profile.getLanguagesSpoken())
						.profilePhoto(PhotoAPIInbound.convertToDomain(profile.getProfilePhoto()))
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