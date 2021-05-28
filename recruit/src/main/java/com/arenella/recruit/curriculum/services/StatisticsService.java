package com.arenella.recruit.curriculum.services;

import com.arenella.recruit.curriculum.controllers.CurriculumDownloadsStatistics;

/**
* Defines services relating to Curriclum related statistics
* @author K Parkings
*/
public interface StatisticsService {

	/**
	* Returns Statistics relating to daily doloads of curriculums
	* @return
	*/
	public CurriculumDownloadsStatistics fetchCurriculumDownloadsStatistics();
	
}
