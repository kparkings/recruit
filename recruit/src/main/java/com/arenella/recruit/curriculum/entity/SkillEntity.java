package com.arenella.recruit.curriculum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Represents a Skill that may me present in a 
* Curriculum
* @author K Parkings
*/
@Entity
@Table(schema="curriculum", name="skills")
public class SkillEntity {

	@Id
	private String skill;
	
	/**
	* Costructor : Hibernate
	*/
	@SuppressWarnings("unused")
	private SkillEntity() {
		
	}
	
	/**
	* Contructor
	* @param skill - name of the skill
	*/
	public SkillEntity(String skill) {
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
