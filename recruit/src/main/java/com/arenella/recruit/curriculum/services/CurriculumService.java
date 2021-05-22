package com.arenella.recruit.curriculum.services;

import com.arenella.recruit.curriculum.beans.Curriculum;

/**
* Defines services for working with Curriculums
* @author K Parkings
*/
public interface CurriculumService {

	/**
	* Persists a Curriculum
	* @return Unique Id of the Curriculum
	*/
	public String persistCurriculum(Curriculum curriculum);

	/**
	* Retrieves a Curriculum matching the ID
	* @param curriculumId
	* @return Curriculum natching curriculumId
	*/
	public Curriculum fetchCurriculum(String curriculumId); 
	
	/**
	* Returns the next available Curriculum Id
	* @return next available CurriculumId
	*/
	public long getNextCurriculumId();
	
}
