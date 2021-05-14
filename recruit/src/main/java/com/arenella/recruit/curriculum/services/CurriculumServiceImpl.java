package com.arenella.recruit.curriculum.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arenella.recruit.curriculum.beans.Curriculum;
import com.arenella.recruit.curriculum.dao.CurriculumDao;
import com.arenella.recruit.curriculum.entity.CurriculumEntity;

/**
* Services for interacting with Curriculums
* @author K Parkings
*/
@Service
public class CurriculumServiceImpl implements CurriculumService{

	@Autowired
	private CurriculumDao curriculumDao;
	
	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public String persistCurriculum(Curriculum curriculum) {

		CurriculumEntity entity = CurriculumEntity.convertToEntity(curriculum);
		
		curriculumDao.save(entity);
		
		return "1";
	}

}
