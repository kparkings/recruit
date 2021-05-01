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
	
}
