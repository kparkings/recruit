package com.arenella.recruit.recruiters.beans;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
* Class represents a public profile to provide other system users
* insight into the recruiter
* @author K Parkings
*/
public class RecruiterProfile {

	public static enum COUNTRY 			{NETHERLANDS, BELGIUM, UK, IRELAND, EUROPE, WORLD}
	public static enum LANGUAGE 		{ENGLISH, DUTCH, FRENCH}
	public static enum SECTOR  			{FINTECH, EU_INSTITUTIONS, GOVERNMENT, AUTOMOBILES, LOGISTICS, POLICE_AND_DEFENSE, CYBER_INTEL, GAMING}
	public static enum TECH				{JAVA, DOT_NET, DEV_OPS, NETWORKS, CLOUD, WEB, UI_UX, PROJECT_NANAGMENT, TESTING, BUSINESS_ANALYSTS, SECURITY, IT_SUPPORT, ARCHITECT, BI, REC2REC }
	public static enum CONTRACT_TYPE	{FREELANCE, PERM}
	public static enum REC_TYPE			{INDEPENDENT, COMMERCIAL}
	
	private String 				recruiterId;
	private Set<COUNTRY> 		recruitsIn				= new LinkedHashSet<>();
	private Set<LANGUAGE>		languagesSpoken			= new LinkedHashSet<>();;
	private Photo				profilePhoto;
	private boolean 			visibleToRecruiters;
	private boolean 			visibleToCandidates;
	private boolean 			visibleToPublic;
	private String				jobTitle;
	private int					yearsExperience;
	private String				introduction;
	private Set<SECTOR>			sectors					= new LinkedHashSet<>();
	private Set<TECH>			coreTech				= new LinkedHashSet<>();
	private Set<CONTRACT_TYPE> 	recruitsContractTypes	= new LinkedHashSet<>();
	private REC_TYPE	 		recruiterType;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public RecruiterProfile(RecruiterProfileBuilder builder) {
		
		this.recruiterId 				= builder.recruiterId;
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
	* Return the unique id of the Recruiter
	* @return id
	*/
	public String getRecruiterId() {
		return this.recruiterId;
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
	public Optional<Photo> getProfilePhoto() {
		return Optional.ofNullable(this.profilePhoto);
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
	* Sets the Profile Photo
	* @param profilePhoto - Sets the Profile Photo
	*/
	public void setProfilePhoto(Photo profilePhoto) {
		this.profilePhoto = profilePhoto;
	}
	
	/**
	* Returns a Builder for the class
	* @return Builder for the class
	*/
	public static RecruiterProfileBuilder builder() {
		return new RecruiterProfileBuilder();
	}
	
	/**
	* Builder for the class
	* @author K Parkings
	*/
	public static class RecruiterProfileBuilder{
		
		private String 				recruiterId;
		private Set<COUNTRY> 		recruitsIn				= new LinkedHashSet<>();
		private Set<LANGUAGE>		languagesSpoken			= new LinkedHashSet<>();
		private Photo				profilePhoto;
		private boolean 			visibleToRecruiters;
		private boolean 			visibleToCandidates;
		private boolean 			visibleToPublic;
		private String				jobTitle;
		private int					yearsExperience;
		private String				introduction;
		private Set<SECTOR>			sectors					= new LinkedHashSet<>();
		private Set<TECH>			coreTech				= new LinkedHashSet<>();
		private Set<CONTRACT_TYPE> 	recruitsContractTypes	= new LinkedHashSet<>();
		private REC_TYPE	 		recruiterType;
		
		/**
		* Sets the unique id of the Recruiter
		* @param recruiterId - Unique identifier
		* @return Builder
		*/
		public RecruiterProfileBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
	
		/**
		* Sets the Countries the recruiter recruits candidates in
		* @param recruitsIn - all countries the recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileBuilder recruitsIn(Set<COUNTRY> recruitsIn) {
			this.recruitsIn.clear();
			this.recruitsIn.addAll(recruitsIn);
			return this;
		}
		
		/**
		* Sets the languages spoken by the recruiter
		* @param languagesSpoken - spoken languages
		* @return Builder
		*/
		public RecruiterProfileBuilder languagesSpoken(Set<LANGUAGE> languagesSpoken) {
			this.languagesSpoken.clear();
			this.languagesSpoken.addAll(languagesSpoken);
			return this;
		}
		
		/**
		* Sets a Photo of the Recruiter
		* @param profilePhoto - Recruiters photo
		* @return Builder
		*/
		public RecruiterProfileBuilder profilePhoto(Photo profilePhoto) {
			this.profilePhoto = profilePhoto;
			return this;
		}
		
		/**
		* Sets whether other recruiters are allowed to view the profile
		* @param visibleToRecruiters - whether other recruiters are allowed to view the profile
		* @return Builder
		*/
		public RecruiterProfileBuilder visibleToRecruiters(boolean visibleToRecruiters) {
			this.visibleToRecruiters = visibleToRecruiters;
			return this;
		}
		
		/**
		* Sets whether registered candidates are allowed to view the profile
		* @param visibleToCandidates whether registered candidates are allowed to view the profile
		* @return Builder
		*/
		public RecruiterProfileBuilder visibleToCandidates(boolean visibleToCandidates) {
			this.visibleToCandidates = visibleToCandidates;
			return this;
		}
		
		/**
		* Sets whether non registered users are allowed to view the profile
		* @param visibleToPublic Whether non registered users are allowed to view the profile
		* @return Builder
		*/
		public RecruiterProfileBuilder visibleToPublic(boolean visibleToPublic) {
			this.visibleToPublic = visibleToPublic;
			return this;
		}
		
		/**
		* Sets the Job title of the Recruiter
		* @param jobTitle - Recruiters job title
		* @return Builder
		*/
		public RecruiterProfileBuilder jobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
			return this;
		}
		
		/**
		* Sets the number of years experience the recruiter has as a recruiter
		* @param yearsExperience - years of experience
		* @return Builder
		*/
		public RecruiterProfileBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets a textual introduction to the recruiter for profile visitors 
		* to read
		* @param introduction - Recruiters introduction to self
		* @return Builder
		*/
		public RecruiterProfileBuilder introduction(String introduction) {
			this.introduction = introduction;
			return this;
		}
		
		/**
		* Sets the sectors the Recruiter recruits in
		* @param sectors - Sectors the recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileBuilder sectors(Set<SECTOR> sectors) {
			this.sectors.clear();
			this.sectors.addAll(sectors);
			return this;
		}
		
		/**
		* Sets the core tech that the recruiter recruits for
		* @param coreTech - Technologies the recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileBuilder coreTech(Set<TECH> coreTech) {
			this.coreTech.clear();
			this.coreTech.addAll(coreTech);
			return this;
		}
		
		/**
		* Sets the type of contracts the recruiter recruits for
		* @param recruitsContractTypes - type of contract recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileBuilder recruitsContractTypes(Set<CONTRACT_TYPE> 	recruitsContractTypes) {
			this.recruitsContractTypes.clear();
			this.recruitsContractTypes.addAll(recruitsContractTypes);
			return this;
		}
		
		/**
		* Sets the type of the recruiter.
		* @param recruiterType - What contract type the recruiter has
		* @return Builder
		*/
		public RecruiterProfileBuilder recruiterType(REC_TYPE 	recruiterType) {
			this.recruiterType = recruiterType;
			return this;
		}
		
		/**
		* Returns intialized instance of class
		* @return instance of class
		*/
		public RecruiterProfile build() {
			return new RecruiterProfile(this);
		}
		 		
	
	}
	
	/**
	* Class represents a Photo
	* @author K Parkings
	*/
	public static class Photo{
		
		public static enum PHOTO_FORMAT {jpeg, png}
		
		private final byte[] 		imageBytes;
		private final PHOTO_FORMAT 	format;
	
		/**
		* Class represents an uploaded Photo
		* @param imageBytes - bytes of actual file
		* @param format		- format of photo file
		*/
		public Photo(byte[] imageBytes, PHOTO_FORMAT format) {
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
		
	}
	
}