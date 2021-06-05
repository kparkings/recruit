package com.arenella.recruit.curriculum.services;

import java.io.IOException;

import com.arenella.recruit.curriculum.beans.Curriculum;
import com.arenella.recruit.curriculum.controllers.CurriculumUpdloadDetails;
import com.arenella.recruit.curriculum.enums.FileType;

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
	
	/**
	* Persists an CurriculumDownloadedEvent
	* @param curriculumId - Unique Id of the downlaoded Curriculum
	*/
	public void logCurriculumDownloadedEvent(String curriculumId);

	/**
	* Extracts Details out of a Curriculum
	* @param curriculumId 			- Unique Id of the Curriculum
	* @param fileType				- File Type of the Curriculum
	* @param  curriculumFileBytes	- bytes of the actual Curriculum file
	* @Throws IOException
	*/
	CurriculumUpdloadDetails extractDetails(String curriculumId, FileType fileType, byte[] curriculumFileBytes) throws IOException;
	
}
