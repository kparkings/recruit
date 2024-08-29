package com.arenella.recruit.adapters.events;

import java.util.HashSet;
import java.util.Set;

/**
* Event informing of Skill extraction from a Curriculum
* NB: Probably triggered from corresponsing Command
* @author K Parkings
*/
public class CurriculumSkillsExtractionEvent {

	private long 		curriculumId;
	private Set<String> skills 			= new HashSet<>();
	
	/**
	* Constructor
	* @param curriculumId 	- Unique Id of the Curriculum
	* @param skills 		- Skills found in the Curriculum
	*/
	public CurriculumSkillsExtractionEvent(long curriculumId, Set<String> skills) {
		this.curriculumId = curriculumId;
		this.skills.addAll(skills);
	}
	
	/**
	* Returns the Unique Id of the Curriculum
	* @return Id of the Curriculum
	*/
	public long getCurriculumId() {
		return this.curriculumId;
	}
	
	/**
	* Returns the Skills found in the Curriculym
	* @return Skills found in the Curriculym
	*/
	public Set<String> getSkills(){
		return this.skills;
	}
	
}