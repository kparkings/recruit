package com.arenella.recruit.candidates.dao;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import com.arenella.recruit.candidates.entities.NewCandidateStatsEntity;
import com.arenella.recruit.candidates.services.CandidateStatisticsService.NEW_STATS_TYPE;

/**
* Repository for keeping track of when various types of new candidate statistic were last requested
* @author K Parkings
*/
public interface NewCandidateStatsTypeDao extends CrudRepository<NewCandidateStatsEntity, NEW_STATS_TYPE>{

	/**
	* Fetches the last time the Stat was requested and then updates the stat to the current date
	* @param statsType - Type of Stat to retreive
	* @return Last date the Stat was requested
	*/
	public default LocalDate fetchAndSetLastRequested(NEW_STATS_TYPE statsType) {
		
		NewCandidateStatsEntity entity = this.findById(statsType).orElseGet(()-> new NewCandidateStatsEntity(statsType, LocalDate.now()));
		
		LocalDate lastRan = entity.getLastRequested();
		
		entity.updateRequested();
		
		this.save(entity);
		
		return lastRan;
		
	}
	
}
