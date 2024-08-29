package com.arenella.recruit.candidates.dao;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.beans.SkillUpdateStat;
import com.arenella.recruit.candidates.entities.SkillUpdateStatEntity;

/**
* Repository for Stat relating to periodic updating of Candidate Skills
* @author K Parkings
*/
public interface SkillUpdateStatDao extends CrudRepository<SkillUpdateStatEntity, Long>{

	/**
	* Saves the SkillUpdateStat
	* @param stat - Stat to be saved
	*/
	public default void saveSkillUpdateStat(SkillUpdateStat stat) {
		this.save(SkillUpdateStatEntity.toEntity(stat));
	}
	
	
	
}
