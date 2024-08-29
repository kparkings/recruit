package com.arenella.recruit.candidates.beans;

import java.time.LocalDate;

/**
* Stat relating to skills update for Candidate
* @author K Parkings
*/
public class SkillUpdateStat {

	private long 		candidateId;
	private LocalDate 	lastUpdate;
	private long 		addedSkillsCount;
	
	/**
	* Constructor
	* @param candidateId		- Unique id of Candidate
	* @param lastUpdate			- When skills were last updated
	* @param addedSkillsCount	- Number of new skills added
	*/
	public SkillUpdateStat(long candidateId, LocalDate lastUpdate, long addedSkillsCount) {
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
}