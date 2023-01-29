package com.arenella.recruit.curriculum.dao;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.curriculum.entity.SkillEntity;

public interface SkillsDao extends CrudRepository<SkillEntity,String>{

	/**
	* Returns set of skills
	* @return skills
	*/
	default Set<String> getSkills(){
		return StreamSupport.stream(this.findAll().spliterator(), false).map(skill -> skill.getSkill()).collect(Collectors.toSet());
				
	}
	
}
