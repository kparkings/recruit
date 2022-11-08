package com.arenella.recruit.candidates.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.services.CandidateStatisticsService.NEW_STATS_TYPE;

/**
* Unit tests for the NewCandidateStatsEntity class
* @author K Parkings
*/
public class NewCandidateStatsEntityTest {

	/**
	* Test constructor
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		final LocalDate 		since 	= LocalDate.of(2022, 11, 5);
		final NEW_STATS_TYPE 	type 	= NEW_STATS_TYPE.NEW_CANDIDATE_BREAKDOWN;
		
		NewCandidateStatsEntity entity = new NewCandidateStatsEntity(type, since);

		assertEquals(since, entity.getLastRequested());
		assertEquals(type, entity.getType());
		
	}
	
	/**
	* Test update of last requested data
	* @throws Exception
	*/
	@Test
	public void testUpdateLastRequested() throws Exception{
		
		final LocalDate 		since 	= LocalDate.of(2022, 11, 4);
		final NEW_STATS_TYPE 	type 	= NEW_STATS_TYPE.NEW_CANDIDATE_BREAKDOWN;
		
		NewCandidateStatsEntity entity = new NewCandidateStatsEntity(type, since);

		assertEquals(since, entity.getLastRequested());
		assertEquals(type, entity.getType());
		
		entity.updateRequested();
		
		assertNotEquals(since, entity.getLastRequested());
		
	}

}