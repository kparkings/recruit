package com.arenella.recruit.adapters.events;

/**
* Event representing an update to an existing CV
* @author K Parkings
*/
public class CurriculumUpdatedEvent {

	private final String curriculumId;
	
	/**
	* Constructor
	* @param curriculumId - Unique id of updated curriculum
	*/
	public CurriculumUpdatedEvent(String curriculumId) {
		this.curriculumId = curriculumId;
	} 
	
	/**
	* Returns the Id of the updated Curriculum
	* @return
	*/
	public String getCurriculumId() {
		return this.curriculumId;
	}
	
}
