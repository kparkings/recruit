package com.arenella.recruit.candidates.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.arenella.recruit.candidates.services.CandidateStatisticsService.NEW_STATS_TYPE;

/**
* Stat containing information as to when the last request for a specific 
* type of new candidate data was requested
* @author K Parkings
*/
@Entity
@Table(schema="candidate", name="new_candidate_stats")
public class NewCandidateStatsEntity {
	
	@Id
	@Column(name="type")
	@Enumerated(EnumType.STRING)
	private NEW_STATS_TYPE 	type;

	@Column(name="last_requested")
	private LocalDate 				lastRequested;

	/**
	* Default Constructor required by Hibernate
	*/
	public NewCandidateStatsEntity() {
		//Default Hibernate
	}
	
	/**
	* Constructor
	* @param type			- Type of new candidate stat
	* @param lastRequested  - Last time the stat was requested
	*/
	public NewCandidateStatsEntity(NEW_STATS_TYPE type, LocalDate lastRequested) {
		this.type 			= type;
		this.lastRequested 	= lastRequested;
	}
	
	/**
	* Returns the type of new candidate stat
	* @return Type of new candidate stat
	*/
	public NEW_STATS_TYPE getType() {
		return this.type;
	}
	
	/**
	* Returns the last time the stat was requested
	* @return Last time the stat was requested
	*/
	public LocalDate getLastRequested() {
		return this.lastRequested;
	}
	
	/**
	* Updates the last time the stat was requested to the current date
	*/
	public void updateRequested() {
		this.lastRequested = LocalDate.now();
	}
	
}