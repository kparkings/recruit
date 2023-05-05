package com.arenella.recruit.candidates.beans;

import java.util.HashSet;
import java.util.Set;

import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Returns the filters extracted from a Document
* @author K Parkings
*/
public class CandidateExtractedFilters {

	private String 			jobTitle			= "";
	private Set<String> 	skills 				= new HashSet<>();
	private String 			experienceGTE 		= "";
	private String 			experienceLTE 		= "";
	private boolean			dutch;
	private boolean			english;
	private boolean			french;
	private boolean			netherlands;
	private boolean			uk;
	private boolean			belgium;
	private boolean			ireland;
	private FREELANCE 		freelance;
	private PERM 			perm;
	private String			extractedText;
	
	/**
	* Constructor based on a Builder
	* @param builder - Contains initialisation values
	*/
	public CandidateExtractedFilters(CandidateExtractedFiltersBuilder builder) {
		this.jobTitle 		= builder.jobTitle;
		this.skills 		= builder.skills;
		this.experienceGTE 	= builder.experienceGTE;
		this.experienceLTE 	= builder.experienceLTE;
		this.dutch 			= builder.dutch;
		this.english 		= builder.english;
		this.french 		= builder.french;
		this.netherlands	= builder.netherlands;
		this.uk				= builder.uk;
		this.belgium		= builder.belgium;
		this.ireland		= builder.ireland;
		this.freelance 		= builder.freelance;
		this.perm 			= builder.perm;
		this.extractedText	= builder.extractedText;
	}
	
	/**
	* Returns the job title to filter on
	* @return job title to filter on
	*/
	public String getJobTitle() {
		return this.jobTitle;
	}
	
	/**
	* Returns the skills to filter on
	* @return skills to filter on
	*/
	public Set<String> getSkills(){
		return this.skills;
	} 				
	
	/**
	* Returns the GTE experience to filter on
	* @return Greater than or equals to years experience
	*/
	public String getExperienceGTE(){
		return this.experienceGTE;
	}
	
	/**
	* Returns the LTE experience to filter on
	* @return Less than or equals to years experience
	*/
	public String getExperienceLTE(){
		return this.experienceLTE;
	}
	
	/**
	* Returns whether to filter on Dutch language
	* @return dutch language
	*/
	public boolean getDutch(){
		return this.dutch;
	}
	
	/**
	* Returns whether to filter on English language
	* @return english language
	*/
	public boolean getEnglish(){
		return this.english;
	}
	
	/**
	* Returns whether to filter on French language
	* @return french language
	*/
	public boolean getFrench(){
		return this.french;
	}
	
	/**
	* Returns whether to filter on candidates in the Netherlands
	* @return whether to filter on candidates in the Netherlands
	*/
	public boolean getNetherlands(){
		return this.netherlands;
	}
	
	/**
	* Returns whether to filter on candidates in Belgium
	* @return whether to filter on candidates in Belgium
	*/
	public boolean getBelgium(){
		return this.belgium;
	}
	
	/**
	* Returns whether to filter on candidates in the UK
	* @return whether to filter on candidates in the UK
	*/
	public boolean getUK(){
		return this.uk;
	}
	
	/**
	* Returns whether to filter on candidates in Ireland
	* @return whether to filter on candidates in Ireland
	*/
	public boolean getIreland(){
		return this.ireland;
	}
	
	/**
	* Returns whether to filter on freelance roles
	* @return whether to filter on freelane roles
	*/
	public FREELANCE getFreelance(){
		return this.freelance;
	}
	
	/**
	* Returns whether to filter on perm roles
	* @return whether to filter on perm roles
	*/
	public PERM getPerm(){
		return this.perm;
	}
	
	/**
	* Text extracted from the document
	* @return document contents
	*/
	public String getExtractedText() {
		return this.extractedText;
	}
	
	/**
	* Returns a builder for the CandidateExtractedFilters class
	* @return Builder
	*/
	public static CandidateExtractedFiltersBuilder builder() {
		return new CandidateExtractedFiltersBuilder();
	}
	
	/**
	* Builder for the CandidateExtractedFilters class
	* @author K Parkings
	*/
	public static class CandidateExtractedFiltersBuilder{
		
		private String 			jobTitle			= "";
		private Set<String> 	skills 				= new HashSet<>();
		private String 			experienceGTE 		= "";
		private String 			experienceLTE 		= "";
		private boolean			dutch;
		private boolean			english;
		private boolean			french;
		private boolean			netherlands;
		private boolean			uk;
		private boolean			belgium;
		private boolean			ireland;
		private FREELANCE 		freelance;
		private PERM 			perm;
		private String			extractedText;
		
		/**
		* Sets the Job title to filter on
		* @param jobTitle - to filter on
		* @return Builder
		*/
		public CandidateExtractedFiltersBuilder jobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
			return this;
		}
		
		/**
		* Sets the skills to filter on
		* @param skills - to filter on
		* @return Builder
		*/
		public CandidateExtractedFiltersBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Sets the minimum years experience to filter on
		* @param experienceGTE - experience filter option
		* @return Builder
		*/
		public CandidateExtractedFiltersBuilder experienceGTE(String experienceGTE) {
			this.experienceGTE = experienceGTE; 
			return this;
		}
		
		/**
		* Sets the maximum years experience to filter on
		* @param experienceLTE - experience filter option
		* @return Builder
		*/
		public CandidateExtractedFiltersBuilder experienceLTE(String experienceLTE) {
			this.experienceLTE = experienceLTE;
			return this;
		}
		
		/**
		* Sets whether to filter on Dutch speaking candidates
		* @param dutch - language filter option
		* @return Builder
		*/
		public CandidateExtractedFiltersBuilder dutch(boolean dutch) {
			this.dutch = dutch;
			return this;
		}
		
		/**
		* Sets whether to filter on English speaking candidates
		* @param english - language filter option
		* @return Builder
		*/
		public CandidateExtractedFiltersBuilder english(boolean english) {
			this.english = english;
			return this;
		}
		
		/**
		* Sets whether to filter on French speaking candidates
		* @param french - language filter option
		* @return Builder
		*/
		public CandidateExtractedFiltersBuilder french(boolean french) {
			this.french = french;
			return this;
		}
		
		/**
		* Sets whether to filter on candidates in the Netherlands
		* @param dutch - language filter option
		* @return Builder
		*/
		public CandidateExtractedFiltersBuilder netherlands(boolean netherlands) {
			this.netherlands = netherlands;
			return this;
		}
		
		/**
		* Sets whether to filter on candidates in Belgium
		* @param english - language filter option
		* @return Builder
		*/
		public CandidateExtractedFiltersBuilder belgium(boolean belgium) {
			this.belgium = belgium;
			return this;
		}
		
		/**
		* Sets whether to filter on candidates in the UK
		* @param french - language filter option
		* @return Builder
		*/
		public CandidateExtractedFiltersBuilder uk(boolean uk) {
			this.uk = uk;
			return this;
		}
		
		/**
		* Sets whether to filter on candidates in the Ireland
		* @param french - language filter option
		* @return Builder
		*/
		public CandidateExtractedFiltersBuilder ireland(boolean ireland) {
			this.ireland = ireland;
			return this;
		}
		
		/**
		* Sets the filter option for freelance positions
		* @param freelance - filter option
		* @return Builder
		*/
		public CandidateExtractedFiltersBuilder freelance(FREELANCE freelance) {
			this.freelance = freelance;
			return this;
		}
		
		/**
		* Sets the filter option for perm positions
		* @param perm - filter option
		* @return Builder
		*/
		public CandidateExtractedFiltersBuilder perm(PERM perm) {
			this.perm = perm;
			return this;
		}
		
		/**
		* Sets the text contained in the document
		* @param extractedText - text 
		* @return Builder
		*/
		public CandidateExtractedFiltersBuilder extractedText(String extractedText) {
			this.extractedText = extractedText;
			return this;
		}
		
		/**
		* Returns instance of CandidateExtractedFilters initialized with 
		* the values in the Builder
		* @return instance of CandidateExtractedFilters
		*/
		public CandidateExtractedFilters build() {
			return new CandidateExtractedFilters(this);
		}
		
	}
	
}
