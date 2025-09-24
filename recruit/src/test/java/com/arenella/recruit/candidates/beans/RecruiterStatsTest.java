package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.RecruiterStats.Search;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Unit tests for the RecruiterStats class
* @author K Parkings
*/
class RecruiterStatsTest {

	/**
	* Tests Stat's are constructed correctly
	* @throws Exception
	*/
	@Test
	void testConstructor() {
		
		final CandidateSearchEvent event0 = CandidateSearchEvent.builder().country(COUNTRY.NETHERLANDS).function(FUNCTION.ARCHITECT).build();
		final CandidateSearchEvent event1 = CandidateSearchEvent.builder().country(COUNTRY.BELGIUM).function(FUNCTION.ARCHITECT).build();
		final CandidateSearchEvent event2 = CandidateSearchEvent.builder().country(COUNTRY.BELGIUM).function(FUNCTION.ARCHITECT).build();
		final CandidateSearchEvent event3 = CandidateSearchEvent.builder().country(COUNTRY.BELGIUM).function(FUNCTION.WEB_DEV).build();
		final CandidateSearchEvent event4 = CandidateSearchEvent.builder().country(COUNTRY.NETHERLANDS).function(FUNCTION.JAVA_DEV).build();
		final CandidateSearchEvent event5 = CandidateSearchEvent.builder().function(FUNCTION.JAVA_DEV).build();
		
		RecruiterStats stats = new RecruiterStats(Set.of(event0,event1,event2,event3,event4,event5));
		
		assertEquals(5, stats.getUniqueSearches().size());
		
		Search[] searches = stats.getUniqueSearches().toArray(new Search[5]);
		
		assertNull(searches[0].getCountry());
		
		assertEquals(COUNTRY.BELGIUM, 		searches[1].getCountry());
		assertEquals(COUNTRY.BELGIUM, 		searches[2].getCountry());
		assertEquals(COUNTRY.NETHERLANDS, 	searches[3].getCountry());
		assertEquals(COUNTRY.NETHERLANDS, 	searches[4].getCountry());
		
	}
	
}
