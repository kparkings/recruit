package com.arenella.recruit.candidates.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
* Represents a Skill that may me present in a 
* Curriculum
* @author K Parkings
*/
@Entity
@Table(schema="candidate", name="skills")
public class CandidateSkillEntity {

	@Id
	private String skill;
	
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
	
}
