package com.arenella.recruit.curriculum.services;

import java.util.Optional;

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
		
		return curriculum.getId().get();
	}

	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public Curriculum fetchCurriculum(String curriculumId) {
		
		Optional<CurriculumEntity> entity = this.curriculumDao.findById(Long.valueOf(curriculumId));
		
		return CurriculumEntity.convertFromEntity(entity.get());
		
	}

	/**
	* Refer to the CurriculumService interface for details
	*/
	@Override
	public long getNextCurriculumId() {
		
		Optional<CurriculumEntity> entity = this.curriculumDao.findTopByOrderByCurriculumIdDesc();
		
		return entity.isEmpty() ? 1 : entity.get().getCurriculumId() + 1;
		
	}

}
