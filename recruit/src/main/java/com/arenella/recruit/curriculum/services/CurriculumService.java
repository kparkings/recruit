package com.arenella.recruit.curriculum.services;

import java.io.IOException;
import java.util.UUID;

import com.arenella.recruit.curriculum.beans.Curriculum;
import com.arenella.recruit.curriculum.beans.PendingCurriculum;
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
	* Persists a PendingCurriculum
	* @param pendingCurriculum - PendingCurriculum to persist
	*/
	public void persistPendingCurriculum(PendingCurriculum pendingCurriculum);
	
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
	public CurriculumUpdloadDetails extractDetails(String curriculumId, FileType fileType, byte[] curriculumFileBytes) throws IOException;
	
	/**
	* Retrieves the PendingCurriculum 
	* @param pendingCurriculumId - Unique Id of PendingCurriculum to fetch
	* @return PendingCurriculum
	*/
	public PendingCurriculum fetchPendingCurriculum(UUID pendingCurriculumId);
	
	/**
	* Deletes a Pending Curriculum
	* @param pendingCurriculumId - Unique Id of the PendingCurriculum
	*/
	public void deletePendingCurriculum(UUID pendingCurriculumId);
	
	/**
	* Deletes a Curriculum
	* @param curriculumId - Id of curriculum to delete
	*/
	public void deleteCurriculum(long curriculumId);

	/**
	* Returns the curriculum matching the curriculumId as the bytes for a PDF file. For
	* CVs in docx format these will first be converted to PDF
	* @param curriculumId - Unique Id of the curriculum to return
	* @return bytes for pdf representation of curriculum
	* @throws IOException 
	*/
	public byte[] getCurriculamAsPdfBytes(String curriculumId) throws IOException;

}