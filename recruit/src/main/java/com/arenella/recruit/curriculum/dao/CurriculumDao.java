package com.arenella.recruit.curriculum.dao;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.curriculum.entity.CurriculumEntity;

/**
* Dao for persisting Curriculum Entities
* @author K Parkings
*/
public interface CurriculumDao extends CrudRepository<CurriculumEntity, Long> {

}
