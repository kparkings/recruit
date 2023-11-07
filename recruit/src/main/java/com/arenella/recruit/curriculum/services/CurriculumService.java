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

	/**
	* Replaces an existing curriculum with a newer version
	* @param curriculumId 	- Id of Curriculum to replace (corresponds to a Candidate Id)
	* @param curriculum		- File of new Curriculum 
	* @return id of curriculum
	*/
	public String updateCurriculum(long curriculumId, Curriculum curriculum);

	/**
	* Updates the Credits for the Users
	* @param credits - Credits to assign to all users
	*/
	public void updateCredits(int credits);

	/**
	* Updates a Recruiters credits. Decrementing them by 1
	* @param userId - id of User who used a Credit
	*/
	void useCredit(String userId);

	/**
	* Returns whether the User either does not have Credit based access or does 
	* have credit based access to curriculums but has remining credits
	* @param name - id of Authorized User
	* @return whether User can access Curriculums
	*/
	public boolean doCreditsCheck(String name);

	/**
	* Returns the number of remaining credits the User has
	* @param name - id of the User
	* @return remaining credits
	*/
	public int getCreditCountForUser(String name);

}