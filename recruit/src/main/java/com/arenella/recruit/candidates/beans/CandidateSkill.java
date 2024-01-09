package com.arenella.recruit.candidates.beans;

import com.arenella.recruit.candidates.entities.CandidateSkillEntity.VALIDATION_STATUS;

/**
* A Skill that a Candidate could potentially possesses
* @author K Parkings
*/
public class CandidateSkill {

	private String 				skill;
	private VALIDATION_STATUS 	validationStatus	= VALIDATION_STATUS.PENDING;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public CandidateSkill(CandidateSkillBuilder builder) {
		this.skill 				= builder.skill;
		this.validationStatus 	= builder.validationStatus;
	}
	
	/**
	* Sets the validationStatus of the Skill
	* @param validationStatus - validation status of the skill
	*/
	public void setValidationStatus(VALIDATION_STATUS validationStatus) {
		this.validationStatus = validationStatus;
	}
	
	/**
	* Returns the skill
	* @return skill
	*/
	public String getSkill() {
		return this.skill;
	}
	
	/**
	* Returns the validation status of the skill
	* @return validation status
	*/
	public VALIDATION_STATUS getValidationStatus() {
		return this.validationStatus;
	}
	
	/**
	* Returns a builder for the class
	* @return Builder
	*/
	public static CandidateSkillBuilder builder() {
		return new CandidateSkillBuilder();
	}
	
	/**
	* Builder for the class
	* @author K Parkings
	*/
	public static class CandidateSkillBuilder{
	
		private String 				skill;
		private VALIDATION_STATUS 	validationStatus	= VALIDATION_STATUS.PENDING;
		
		/**
		* Sets the skill ( Unique id )
		* @param skill - The name of the Skill
		* @return Builder
		*/
		public CandidateSkillBuilder skill(String skill) {
			this.skill = skill;
			return this;
		}
		
		/**
		* Sets the validation status of the Skill
		* @param validationStatus - validation Status
		* @return Builder
		*/
		public CandidateSkillBuilder validationStatus(VALIDATION_STATUS validationStatus) {
			this.validationStatus = validationStatus;
			return this;
		}

		/**
		* Returns instance initialized with the values in the Builder
		* @return
		*/
		public CandidateSkill build() {
			return new CandidateSkill(this);
		}
	}
	
}