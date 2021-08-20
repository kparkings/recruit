package com.arenella.recruit.curriculum.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.curriculum.entity.PendingCurriculumEntity;

/**
* Repository for working with PendiunCurriculums
* @author K Parkings
*/
public interface PendingCurriculumDao extends CrudRepository<PendingCurriculumEntity, UUID>{

}
