package com.arenella.recruit.adapters.actions;

/**
* Command requesting that the Skills for a given Curriculum are 
* published so that they can be consumed by another service
* @author K Parkings
*/
public class RequestSkillsForCurriculumCommand {

	private long curriculumId;
	
	/**
	* Constructor
	* @param curriculumId - Unique ID of Curriculum to extract skills for
	*/
	public RequestSkillsForCurriculumCommand(long curriculumId) {
		this.curriculumId = curriculumId;
	}
	
	/**
	* Returns the unique Id of the Curriculun to extract skills for
	* @return Id of Curriculum
	*/
	public long getCurriculumId() {
		return this.curriculumId;
	}
	
}
