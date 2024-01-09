package com.arenella.recruit.candidates.controllers;

import com.arenella.recruit.candidates.beans.CandidateSkill;
import com.arenella.recruit.candidates.entities.CandidateSkillEntity.VALIDATION_STATUS;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
* A Skill that a Candidate could potentially possesses
* @author K Parkings
*/
@JsonDeserialize(builder=CandidateSkillAPIInbound.CandidateSkillAPIInboundBuilder.class)
public class CandidateSkillAPIInbound {

	private String 				skill;
	private VALIDATION_STATUS 	validationStatus	= VALIDATION_STATUS.PENDING;
	
	/**
	* Constructor based upon a Builder
	* @param builder - Contains initialization values
	*/
	public CandidateSkillAPIInbound(CandidateSkillAPIInboundBuilder builder) {
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
	public static CandidateSkillAPIInboundBuilder builder() {
		return new CandidateSkillAPIInboundBuilder();
	}
	
	/**
	* Builder for the class
	* @author K Parkings
	*/
	@JsonPOJOBuilder(buildMethodName="build", withPrefix="")
	public static class CandidateSkillAPIInboundBuilder{
	
		private String 				skill;
		private VALIDATION_STATUS 	validationStatus	= VALIDATION_STATUS.PENDING;
		
		/**
		* Sets the skill ( Unique id )
		* @param skill - The name of the Skill
		* @return Builder
		*/
		public CandidateSkillAPIInboundBuilder skill(String skill) {
			this.skill = skill;
			return this;
		}
		
		/**
		* Sets the validation status of the Skill
		* @param validationStatus - validation Status
		* @return Builder
		*/
		public CandidateSkillAPIInboundBuilder validationStatus(VALIDATION_STATUS validationStatus) {
			this.validationStatus = validationStatus;
			return this;
		}

		/**
		* Returns instance initialized with the values in the Builder
		* @return
		*/
		public CandidateSkillAPIInbound build() {
			return new CandidateSkillAPIInbound(this);
		}
	}
	
	/**
	* Converts from APIOutbound to Domain representation
	* @param skill - To convert
	* @return API Domain representation
	*/
	public static CandidateSkill convertToDomain(CandidateSkillAPIInbound skill) {
		return CandidateSkill
				.builder()
					.skill(skill.getSkill())
					.validationStatus(skill.getValidationStatus())
				.build();
	}
	
}