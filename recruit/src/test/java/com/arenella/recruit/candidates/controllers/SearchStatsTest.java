package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateSearchEvent;
import com.arenella.recruit.candidates.controllers.SearchStats.SearchStatCountry;
import com.arenella.recruit.candidates.controllers.SearchStats.SearchStatFunction;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Unit tests for the SearchStats class 
*/
class SearchStatsTest {

	/**
	* Tests events processed and return the correct results 
	*/
	@Test
	void testConstruction() {
		
		Set<CandidateSearchEvent> events = Set.of(
				CandidateSearchEvent.builder().build(),
				CandidateSearchEvent.builder().country(COUNTRY.BELGIUM).function(FUNCTION.NETWORK_ADMINISTRATOR).build(),
				CandidateSearchEvent.builder().country(COUNTRY.BULGARIA).function(FUNCTION.JAVA_DEV).build(),
				CandidateSearchEvent.builder().country(COUNTRY.BULGARIA).function(FUNCTION.NETWORK_ADMINISTRATOR).build(),
				CandidateSearchEvent.builder().country(COUNTRY.BELGIUM).function(FUNCTION.JAVA_DEV).build(),
				CandidateSearchEvent.builder().country(COUNTRY.INDIA).function(FUNCTION.IT_RECRUITER).build(),
				CandidateSearchEvent.builder().country(COUNTRY.ITALY).function(FUNCTION.IT_SECURITY).build(),
				CandidateSearchEvent.builder().country(COUNTRY.BELGIUM).function(null).build(),
				CandidateSearchEvent.builder().country(COUNTRY.GREECE).function(FUNCTION.NETWORK_ADMINISTRATOR).build(),
				CandidateSearchEvent.builder().country(COUNTRY.GREECE).function(FUNCTION.CSHARP_DEV).build(),
				CandidateSearchEvent.builder().country(COUNTRY.SLOVAKIA).function(FUNCTION.SOFTWARE_DEVELOPER).build(),
				CandidateSearchEvent.builder().country(COUNTRY.BELGIUM).function(FUNCTION.TESTER).build(),
				CandidateSearchEvent.builder().country(COUNTRY.UKRAINE).function(FUNCTION.WEB_DEV).build(),
				CandidateSearchEvent.builder().build(),
				CandidateSearchEvent.builder().country(null).function(FUNCTION.BA).build()
				
		);
		
		SearchStats stats = new SearchStats(events);
		
		assertEquals(7, stats.getCountryStats().size());
		assertEquals(9, stats.getFunctionStats().size());
		
		SearchStatFunction networkAdmin = stats.getFunctionStats().stream().filter(f -> f.function() == FUNCTION.NETWORK_ADMINISTRATOR).findFirst().orElseThrow();
		SearchStatFunction javaDev 		= stats.getFunctionStats().stream().filter(f -> f.function() == FUNCTION.JAVA_DEV).findFirst().orElseThrow();
		SearchStatFunction itRecruiter 	= stats.getFunctionStats().stream().filter(f -> f.function() == FUNCTION.IT_RECRUITER).findFirst().orElseThrow();
		SearchStatFunction itSecurity 	= stats.getFunctionStats().stream().filter(f -> f.function() == FUNCTION.IT_SECURITY).findFirst().orElseThrow();
		SearchStatFunction cSharpDev 	= stats.getFunctionStats().stream().filter(f -> f.function() == FUNCTION.CSHARP_DEV).findFirst().orElseThrow();
		SearchStatFunction softwareDev 	= stats.getFunctionStats().stream().filter(f -> f.function() == FUNCTION.SOFTWARE_DEVELOPER).findFirst().orElseThrow();
		SearchStatFunction tester 		= stats.getFunctionStats().stream().filter(f -> f.function() == FUNCTION.TESTER).findFirst().orElseThrow();
		SearchStatFunction webDev 		= stats.getFunctionStats().stream().filter(f -> f.function() == FUNCTION.WEB_DEV).findFirst().orElseThrow();
		SearchStatFunction ba 			= stats.getFunctionStats().stream().filter(f -> f.function() == FUNCTION.BA).findFirst().orElseThrow();
		
		assertEquals(3, networkAdmin.searches());
		assertEquals(25, networkAdmin.percentageOfTotal());
		
		assertEquals(2, javaDev.searches());
		assertEquals(16, javaDev.percentageOfTotal());
		
		assertEquals(1, itRecruiter.searches());
		assertEquals(8, itRecruiter.percentageOfTotal());
		
		assertEquals(1, itSecurity.searches());
		assertEquals(8, itSecurity.percentageOfTotal());
		
		assertEquals(1, cSharpDev.searches());
		assertEquals(8, cSharpDev.percentageOfTotal());
		
		assertEquals(1, softwareDev.searches());
		assertEquals(8, softwareDev.percentageOfTotal());
		
		assertEquals(1, tester.searches());
		assertEquals(8, tester.percentageOfTotal());
		
		assertEquals(1, webDev.searches());
		assertEquals(8, webDev.percentageOfTotal());
		
		assertEquals(1, ba.searches());
		assertEquals(8, ba.percentageOfTotal());
		
		SearchStatCountry belgium 	= stats.getCountryStats().stream().filter(c -> c.country() == COUNTRY.BELGIUM).findFirst().orElseThrow();
		SearchStatCountry bulgaria 	= stats.getCountryStats().stream().filter(c -> c.country() == COUNTRY.BULGARIA).findFirst().orElseThrow();
		SearchStatCountry india 	= stats.getCountryStats().stream().filter(c -> c.country() == COUNTRY.INDIA).findFirst().orElseThrow();
		SearchStatCountry italy 	= stats.getCountryStats().stream().filter(c -> c.country() == COUNTRY.ITALY).findFirst().orElseThrow();
		SearchStatCountry greece 	= stats.getCountryStats().stream().filter(c -> c.country() == COUNTRY.GREECE).findFirst().orElseThrow();
		SearchStatCountry slovakia 	= stats.getCountryStats().stream().filter(c -> c.country() == COUNTRY.SLOVAKIA).findFirst().orElseThrow();
		SearchStatCountry ukraine 	= stats.getCountryStats().stream().filter(c -> c.country() == COUNTRY.UKRAINE).findFirst().orElseThrow();
		
		assertEquals(4, belgium.searches());
		assertEquals(33, belgium.percentageOfTotal());
		
		assertEquals(2, bulgaria.searches());
		assertEquals(16, bulgaria.percentageOfTotal());
		
		assertEquals(1, india.searches());
		assertEquals(8, india.percentageOfTotal());
		
		assertEquals(1, italy.searches());
		assertEquals(8, italy.percentageOfTotal());
		
		assertEquals(2, greece.searches());
		assertEquals(16, greece.percentageOfTotal());
		
		assertEquals(1, slovakia.searches());
		assertEquals(8, slovakia.percentageOfTotal());
		
		assertEquals(1, ukraine.searches());
		assertEquals(8, ukraine.percentageOfTotal());
		
		
	}

}
