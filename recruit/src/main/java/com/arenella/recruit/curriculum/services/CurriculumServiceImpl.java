package com.arenella.recruit.curriculum.services;

import org.springframework.stereotype.Service;

import com.arenella.recruit.curriculum.beans.Curriculum;

/**
* Services for interacting with Curriculums
* @author K Parkings
*/
@Service
public class CurriculumServiceImpl implements CurriculumService{

	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public String persistCurriculum(Curriculum curriculum) {

		return "1";
	}

}
