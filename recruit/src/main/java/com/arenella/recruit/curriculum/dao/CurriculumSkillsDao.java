package com.arenella.recruit.curriculum.dao;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.curriculum.entity.SkillEntity;

public interface CurriculumSkillsDao extends CrudRepository<SkillEntity,String>{
	
	/**
	* Persists skills 
	* @param skills - Skills to persist
	*/
	default void persistSkills(Set<String> skills) {
		this.saveAll(skills.stream().map(s -> new SkillEntity(s)).collect(Collectors.toSet()));
	}
}
