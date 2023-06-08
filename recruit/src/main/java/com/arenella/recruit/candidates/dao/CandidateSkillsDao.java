package com.arenella.recruit.candidates.dao;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.entities.CandidateSkillEntity;

public interface CandidateSkillsDao extends CrudRepository<CandidateSkillEntity,String>{

	/**
	* Returns set of skills
	* @return skills
	*/
	default Set<String> getSkills(){
		return StreamSupport.stream(this.findAll().spliterator(), false).map(skill -> skill.getSkill()).collect(Collectors.toSet());
	}

	/**
	* Persists skills 
	* @param skills - Skills to persist
	*/
	default void persistSkills(Set<String> skills) {
		this.saveAll(skills.stream().map(CandidateSkillEntity::new).collect(Collectors.toSet()));
	}
	
}
