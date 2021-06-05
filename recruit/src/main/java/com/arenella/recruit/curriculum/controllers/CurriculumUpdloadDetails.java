package com.arenella.recruit.curriculum.controllers;

import java.util.LinkedHashSet;
import java.util.Set;

/**
* Contains details about the CV that has been uploaded
* @author K Parkings
*/
public class CurriculumUpdloadDetails {

	private String 			id				= "";
	private String 			emailAddress	= "";
	private Set<String> 	skills 			= new LinkedHashSet<>();
	
	/**
	* Constructor based upon a builder
	* @param builder Contains initialization values
	*/
	public CurriculumUpdloadDetails(CurriculumUpdloadDetailsBuilder builder) {
		
		this.id = builder.id;
		this.emailAddress = builder.emailAddress;
		this.skills.addAll(builder.skills);
		
	}
	
	/**
	* Returns the Unique identifier for the Curriculum
	* @return unique id of Curriculum
	*/
	public String getId() {
		return this.id;
	}
	
	/**
	* Returns the email address found in the Curriculum
	* @return email address
	*/
	public String getEmailAddress() {
		return this.emailAddress;
	}
	
	/**
	* Returns the skills listed in the Curriculum
	* @return skills found in Curriculum
	*/
	public Set<String> getSkills(){
		return this.skills;
	}
	
	/**
	* Returns a builder for the class
	* @return Builder for the class
	*/
	public static CurriculumUpdloadDetailsBuilder builder() {
		return new CurriculumUpdloadDetailsBuilder();
	}
	
	/**
	* Builder for the CurriculumUpdloadDetailsOutbound class
	* @author K Parkings
	*/
	public static class CurriculumUpdloadDetailsBuilder{
	
		private String 			id				= "";
		private String 			emailAddress	= "";
		private Set<String> 	skills 			= new LinkedHashSet<>();
		
		/**
		* Returns the unique identifier assigned to the Curriculum
		* @param id - Unique identifier for the Curriculum
		* @return Builder
		*/
		public CurriculumUpdloadDetailsBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		/**
		* Sets the email address found in the Curriculum
		* @param emailAddress - Email address in Curriculum
		* @return Builder
		*/
		public CurriculumUpdloadDetailsBuilder emailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
			return this;
		}
		
		/**
		* Sets the Skills found in the CV
		* @param skills - Skills present in the CV
		* @return Builder
		*/
		public CurriculumUpdloadDetailsBuilder skills(Set<String> skills) {
			this.skills.clear();
			this.skills.addAll(skills);
			return this;
		}
		
		/**
		* Returns an initialized instance of CurriculumUpdloadDetailsOutbound
		* @return instance of CurriculumUpdloadDetailsOutbound
		*/
		public CurriculumUpdloadDetails build() {
			return new CurriculumUpdloadDetails(this);
		}
		
	}
	
}