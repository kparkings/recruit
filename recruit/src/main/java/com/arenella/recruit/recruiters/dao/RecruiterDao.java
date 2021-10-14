package com.arenella.recruit.recruiters.dao;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.recruiters.entities.RecruiterEntity;

/**
* Repository for working with Recruiter Entities
* @author K Parkings
*/
public interface RecruiterDao extends CrudRepository<RecruiterEntity, String>{

}
