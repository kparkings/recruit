package com.arenella.recruit.curriculum.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.curriculum.beans.Curriculum;
import com.arenella.recruit.curriculum.entity.CurriculumEntity;

/**
* Dao for persisting Curriculum Entities
* @author K Parkings
*/
public interface CurriculumDao extends CrudRepository<CurriculumEntity, Long> {

	/**
	* Get the last added Curriculum
	* @return last added Curriculum
	*/
	public Optional<CurriculumEntity> findTopByOrderByCurriculumIdDesc();

	/**
	* Updates an Existing Curriculum
	* @param curriculum - Curriculum to Update
	*/
	default void updateCurriculum(Curriculum curriculum) {
		
		CurriculumEntity entity = CurriculumEntity.convertToEntity(curriculum);
		
		this.save(entity);
		
	}
	
}
