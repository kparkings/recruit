package com.arenella.recruit.candidates.dao;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.beans.CandidateSkill;
import com.arenella.recruit.candidates.entities.CandidateSkillEntity;
import com.arenella.recruit.candidates.entities.CandidateSkillEntity.VALIDATION_STATUS;

public interface CandidateSkillsDao extends CrudRepository<CandidateSkillEntity,String>{
	
	/**
	* Gets all Skills that have not been rejected
	* @return skills
	*/
	default Set<String> getActiveAndPendingSkills(){
		return StreamSupport
				.stream(this.findAll()
				.spliterator(), false)
				.filter(s -> s.getValidationStatus() == VALIDATION_STATUS.ACCEPTED || s.getValidationStatus() == VALIDATION_STATUS.PENDING)
				.map(skill -> skill.getSkill())
				.collect(Collectors.toSet());
	}
	
	/**
	* Gets Skills that have not yet been validated
	* @return skills
	*/
	default Set<CandidateSkillEntity> getValidationPendingSkillsEntities(){
		return StreamSupport
				.stream(this.findAll()
				.spliterator(), false)
				.filter(s -> s.getValidationStatus() == VALIDATION_STATUS.PENDING)
				.collect(Collectors.toSet());
	}
	
	/**
	* Gets Skills that have not yet been validated
	* @return skills
	*/
	default Set<CandidateSkill> getValidationPendingSkills(){
		return getValidationPendingSkillsEntities().stream().map(CandidateSkillEntity::convertFromEntity).collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	/**
	* Persists skills 
	* @param skills - Skills to persist
	*/
	default void persistSkills(Set<String> skills) {
		this.saveAll(skills.stream().map(CandidateSkillEntity::new).collect(Collectors.toSet()));
	}

	/**
	* Updates the validation status of existing Candidate skills
	* @param skills - Skills to update
	*/
	default void persistExistingSkills(Set<CandidateSkill> skills) {
		this.saveAll(skills.stream().map(CandidateSkillEntity::convertToEntity).collect(Collectors.toSet()));
	}
	
}
