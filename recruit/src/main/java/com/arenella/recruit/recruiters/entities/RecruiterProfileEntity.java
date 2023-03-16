package com.arenella.recruit.recruiters.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.arenella.recruit.recruiters.beans.RecruiterProfile.CONTRACT_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.COUNTRY;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.LANGUAGE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.Photo.PHOTO_FORMAT;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.REC_TYPE;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.SECTOR;
import com.arenella.recruit.recruiters.beans.RecruiterProfile.TECH;

/**
* Entity representation of RecruiterProfile
* @author K Parkings
*/
@Entity
@Table(schema="recuiter", name="recruiter_profiles")
public class RecruiterProfileEntity {

	@Id
	@Column(name="recruiter_id")
	private String 				recruiterId;
	
	@Column(name="photo_bytes")
	private byte[]				photoBytes;
	
	@Column(name="photo_format")
	private PHOTO_FORMAT		photoFormat;
	
	@Column(name="visible_to_recruiters")
	private boolean 			visibleToRecruiters;
	
	@Column(name="visible_to_candidates")
	private boolean 			visibleToCandidates;
	
	@Column(name="visible_to_public")
	private boolean 			visibleToPublic;
	
	@Column(name="job_title")
	private String				jobTitle;
	
	@Column(name="years_experience")
	private int					yearsExperience;
	
	@Column(name="introduction")
	private String				introduction;
	
	@Column(name="recruiter_type")
	private REC_TYPE	 		recruiterType;
	
	@Column(name="recruits_in")
	@ElementCollection(targetClass=String.class)
	@CollectionTable(schema="recruiter", name="recruiter_profile_country", joinColumns=@JoinColumn(name="listing_id"))
	private Set<COUNTRY> 		recruitsIn				= new LinkedHashSet<>();
	
	@Column(name="languages_spoken")
	@ElementCollection(targetClass=String.class)
	@CollectionTable(schema="recruiter", name="recruiter_profile_language", joinColumns=@JoinColumn(name="listing_id"))
	private Set<LANGUAGE>		languagesSpoken			= new LinkedHashSet<>();
	
	@Column(name="sectors")
	@ElementCollection(targetClass=String.class)
	@CollectionTable(schema="recruiter", name="recruiter_profile_sector", joinColumns=@JoinColumn(name="listing_id"))
	private Set<SECTOR>			sectors					= new LinkedHashSet<>();
	
	@Column(name="core_tech")
	@ElementCollection(targetClass=String.class)
	@CollectionTable(schema="recruiter", name="recruiter_profile_tech", joinColumns=@JoinColumn(name="listing_id"))
	private Set<TECH>			coreTech				= new LinkedHashSet<>();
	
	@Column(name="recruits_contract_types")
	@ElementCollection(targetClass=String.class)
	@CollectionTable(schema="recruiter", name="recruiter_profile_contract_type", joinColumns=@JoinColumn(name="listing_id"))
	private Set<CONTRACT_TYPE> 	recruitsContractTypes	= new LinkedHashSet<>();
	
	/**
	* Default constructor 
	*/
	public RecruiterProfileEntity() {
		//Hibernate
	}
	
	/**
	* Constructor based upon a builder
	* @param builder - Contains initialization values
	*/
	public RecruiterProfileEntity(RecruiterProfileEntityBuilder builder) {
		
		this.recruiterId			= builder.recruiterId;
		this.photoBytes				= builder.photoBytes;
		this.photoFormat			= builder.photoFormat;
		this.visibleToRecruiters 	= builder.visibleToRecruiters;
		this.visibleToCandidates 	= builder.visibleToCandidates;
		this.visibleToPublic 		= builder.visibleToPublic;
		this.jobTitle 				= builder.jobTitle;
		this.yearsExperience 		= builder.yearsExperience;
		this.introduction 			= builder.introduction;
		this.recruiterType 			= builder.recruiterType;
		this.recruitsIn				= builder.recruitsIn;
		this.languagesSpoken		= builder.languagesSpoken;
		this.sectors				= builder.sectors;
		this.coreTech				= builder.coreTech;
		this.recruitsContractTypes	= builder.recruitsContractTypes;
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
	* Returns the bytes of the Recruiters photo
	* @return bytes of photo of the recruiter
	*/
	public byte[] getPhotoBytes() {
		return this.photoBytes;
	}
	
	/**
	* Returns the format of the Recruiters photo
	* @return format of photo of the recruiter
	*/
	public PHOTO_FORMAT getPhotoFormat() {
		return this.photoFormat;
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
	public static RecruiterProfileEntityBuilder builder() {
		return new RecruiterProfileEntityBuilder();
	}
	
	/**
	* Builder for the class
	* @author K Parkings
	*/
	public static class RecruiterProfileEntityBuilder{
		
		private String 				recruiterId;
		private Set<COUNTRY> 		recruitsIn				= new LinkedHashSet<>();;
		private Set<LANGUAGE>		languagesSpoken			= new LinkedHashSet<>();;
		private byte[]				photoBytes;
		private PHOTO_FORMAT		photoFormat;
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
		* Sets the unique id of the Recruiter
		* @param recruiterId - Unique identifier
		* @return Builder
		*/
		public RecruiterProfileEntityBuilder recruiterId(String recruiterId) {
			this.recruiterId = recruiterId;
			return this;
		}
		
		/**
		* Sets the Countries the recruiter recruits candidates in
		* @param recruitsIn - all countries the recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileEntityBuilder recruitsIn(Set<COUNTRY> recruitsIn) {
			this.recruitsIn.clear();
			this.recruitsIn.addAll(recruitsIn);
			return this;
		}
		
		/**
		* Sets the languages spoken by the recruiter
		* @param languagesSpoken - spoken languages
		* @return Builder
		*/
		public RecruiterProfileEntityBuilder languagesSpoken(Set<LANGUAGE> languagesSpoken) {
			this.languagesSpoken.clear();
			this.languagesSpoken.addAll(languagesSpoken);
			return this;
		}
		
		/**
		* Sets a bytes of the Photo of the Recruiter
		* @param profilePhoto - Recruiters photo bytes
		* @return Builder
		*/
		public RecruiterProfileEntityBuilder photoBytes(byte[] photoBytes) {
			this.photoBytes = photoBytes;
			return this;
		}
		
		/**
		* Sets format of profile photo
		* @param photoFormat - file format
		* @return Builder
		*/
		public RecruiterProfileEntityBuilder photoFormat(PHOTO_FORMAT photoFormat) {
			this.photoFormat = photoFormat;
			return this;
		}
		
		/**
		* Sets whether other recruiters are allowed to view the profile
		* @param visibleToRecruiters - whether other recruiters are allowed to view the profile
		* @return Builder
		*/
		public RecruiterProfileEntityBuilder visibleToRecruiters(boolean visibleToRecruiters) {
			this.visibleToRecruiters = visibleToRecruiters;
			return this;
		}
		
		/**
		* Sets whether registered candidates are allowed to view the profile
		* @param visibleToCandidates whether registered candidates are allowed to view the profile
		* @return Builder
		*/
		public RecruiterProfileEntityBuilder visibleToCandidates(boolean visibleToCandidates) {
			this.visibleToCandidates = visibleToCandidates;
			return this;
		}
		
		/**
		* Sets whether non registered users are allowed to view the profile
		* @param visibleToPublic Whether non registered users are allowed to view the profile
		* @return Builder
		*/
		public RecruiterProfileEntityBuilder visibleToPublic(boolean visibleToPublic) {
			this.visibleToPublic = visibleToPublic;
			return this;
		}
		
		/**
		* Sets the Job title of the Recruiter
		* @param jobTitle - Recruiters job title
		* @return Builder
		*/
		public RecruiterProfileEntityBuilder jobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
			return this;
		}
		
		/**
		* Sets the number of years experience the recruiter has as a recruiter
		* @param yearsExperience - years of experience
		* @return Builder
		*/
		public RecruiterProfileEntityBuilder yearsExperience(int yearsExperience) {
			this.yearsExperience = yearsExperience;
			return this;
		}
		
		/**
		* Sets a textual introduction to the recruiter for profile visitors 
		* to read
		* @param introduction - Recruiters introduction to self
		* @return Builder
		*/
		public RecruiterProfileEntityBuilder introduction(String introduction) {
			this.introduction = introduction;
			return this;
		}
		
		/**
		* Sets the sectors the Recruiter recruits in
		* @param sectors - Sectors the recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileEntityBuilder sectors(Set<SECTOR> sectors) {
			this.sectors.clear();
			this.sectors.addAll(sectors);
			return this;
		}
		
		/**
		* Sets the core tech that the recruiter recruits for
		* @param coreTech - Technologies the recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileEntityBuilder coreTech(Set<TECH> coreTech) {
			this.coreTech.clear();
			this.coreTech.addAll(coreTech);
			return this;
		}
		
		/**
		* Sets the type of contracts the recruiter recruits for
		* @param recruitsContractTypes - type of contract recruiter recruits for
		* @return Builder
		*/
		public RecruiterProfileEntityBuilder recruitsContractTypes(Set<CONTRACT_TYPE> 	recruitsContractTypes) {
			this.recruitsContractTypes.clear();
			this.recruitsContractTypes.addAll(recruitsContractTypes);
			return this;
		}
		
		/**
		* Sets the type of the recruiter.
		* @param recruiterType - What contract type the recruiter has
		* @return Builder
		*/
		public RecruiterProfileEntityBuilder recruiterType(REC_TYPE 	recruiterType) {
			this.recruiterType = recruiterType;
			return this;
		}
		
		/**
		* Returns intialized instance of class
		* @return instance of class
		*/
		public RecruiterProfileEntity build() {
			return new RecruiterProfileEntity(this);
		}
		 		
	
	}
	
}