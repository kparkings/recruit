package com.arenella.recruit.candidates.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.arenella.recruit.candidates.beans.CandidateSkill;

/**
* Represents a Skill that may me present in a 
* Curriculum
* @author K Parkings
*/
@Entity
@Table(schema="candidate", name="skills")
public class CandidateSkillEntity {

	public enum VALIDATION_STATUS {PENDING, ACCEPTED, REJECTED}
	
	@Id
	private String 	skill;
	
	@Column(name="validation_status")
	@Enumerated(EnumType.STRING)
	private VALIDATION_STATUS validationStatus	= VALIDATION_STATUS.PENDING;
	
	/**
	* Costructor : Hibernate
	*/
	@SuppressWarnings("unused")
	private CandidateSkillEntity() {
		
	}
	
	/**
	* Contructor
	* @param skill - name of the skill
	*/
	public CandidateSkillEntity(String skill) {
		this.skill = skill;
	}
	
	/**
	* Returns the skill
	* @return
	*/
	public String getSkill() {
		return skill;
	}

	/**
	* Returns whether a skill has been validated as being a 
	* valid skill
	* @return whether a skill has been validated
	*/
	public VALIDATION_STATUS getValidationStatus() {
		return this.validationStatus;
	}
	
	/**
	* Sets whether a skill has been validated as being a 
	* valid skill
	* @param validated - Whether skill has been validated
	*/
	public void setValidationStatus(VALIDATION_STATUS validationStatus) {
		this.validationStatus = validationStatus;
	}
	
	/**
	* Converts from the Entity to Domain representation
	* @param entity - To convert
	* @return Domain representation
	*/
	public static CandidateSkill convertFromEntity(CandidateSkillEntity entity) {
		return CandidateSkill
				.builder()
					.skill(entity.getSkill())
					.validationStatus(entity.getValidationStatus())
				.build();
	}
	
	/**
	* Converts from the Domain to Entity representation 
 	* @param skill - To convert
	* @return Entity representation
	*/
	public static CandidateSkillEntity convertToEntity(CandidateSkill skill) {
		CandidateSkillEntity entity = new CandidateSkillEntity(skill.getSkill());
		entity.setValidationStatus(skill.getValidationStatus());
		return entity;
	}
	
}