package com.arenella.recruit.candidates.entities;

import java.time.LocalDate;

import com.arenella.recruit.candidates.beans.SkillUpdateStat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
* Entity representation of update to a Candidates skills
* @author K Parkings
*/
@Entity
@Table(schema="candidate", name="skills_updated_stats")
public class SkillUpdateStatEntity {

	//TODO: [KP] Delete record when Candidate removed from system
	
	@Id
	@Column(name="candidate_id")
	private long 		candidateId;
	
	@Column(name="last_update")
	private LocalDate 	lastUpdate;
	
	@Column(name="added_skills_count")
	private long 		addedSkillsCount;
	
	/**
	* Constructor 
	*/
	public SkillUpdateStatEntity() {
		//Hibernate
	}
	
	/**
	* Constructor
	* @param candidateId		- Unique id of Candidate
	* @param lastUpdate			- When skills were last updated
	* @param addedSkillsCount	- Number of new skills added
	*/
	public SkillUpdateStatEntity(long candidateId, LocalDate lastUpdate, long addedSkillsCount) {
		this.candidateId 		= candidateId;
		this.lastUpdate 		= lastUpdate;
		this.addedSkillsCount 	= addedSkillsCount;
	}
	
	/**
	* Returns unique Id of the Candidate
	* @return candidateId
	*/
	public long getCandidateId() {
		return this.candidateId;
	}
	
	/**
	* Returns when the skills were last updated
	* @return last update
	*/
	public LocalDate getLastUpdate() {
		return this.lastUpdate;
	}
	
	/**
	* Returns additional skill count added during 
	* the last update
	* @return additional skills
	*/
	public long getAddedSkillsCount() {
		return this.addedSkillsCount;
	}

	/**
	* Converts from Domain to Entity representation
	* @param stat - Domain representation
	* @return Entity representation
	*/
	public static SkillUpdateStatEntity toEntity(SkillUpdateStat stat) {
		return new SkillUpdateStatEntity(stat.getCandidateId(), stat.getLastUpdate(), stat.getAddedSkillsCount());
	}
	
	/**
	* Converts from Entity to Domain representation
	* @param entity - Entity representation
	* @return Domain representation
	*/
	public static SkillUpdateStat fromEntity(SkillUpdateStatEntity entity) {
		return new SkillUpdateStat(entity.getCandidateId(), entity.getLastUpdate(), entity.getAddedSkillsCount());
	}
	
}