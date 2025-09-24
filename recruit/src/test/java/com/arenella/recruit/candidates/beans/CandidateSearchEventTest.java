package com.arenella.recruit.candidates.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FUNCTION;

/**
* Unit tests for the CandidateSearchEvent class
* @author K Parkings
*/
class CandidateSearchEventTest {

	/**
	* Tests the builder with default values
	* @throws Exception
	*/
	@Test
	void testBuilder_no_optional_args() {
		
		CandidateSearchEvent event = CandidateSearchEvent.builder().build();
		
		assertTrue(event.getTimestamp() instanceof LocalDate);
		assertTrue(event.getSearchId() instanceof UUID);
		
		assertTrue(event.getCountry().isEmpty());
		assertTrue(event.getDutch().isEmpty());
		assertTrue(event.getEnglish().isEmpty());
		assertTrue(event.getFreelance().isEmpty());
		assertTrue(event.getFrench().isEmpty());
		assertTrue(event.getFunction().isEmpty());
		assertTrue(event.getPerm().isEmpty());
		assertTrue(event.getSkill().isEmpty());
		assertNull(event.getUserId());
		assertTrue(event.getCountry().isEmpty());
		assertTrue(event.getYearsExperienceGtEq().isEmpty());
		assertTrue(event.getYearsExperienceLtEq().isEmpty());
		
	}

	/**
	* Tests the builder with default values
	* @throws Exception
	*/
	@Test
	void testBuilder_with_args() {
		
		final UUID				searchId				= UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
		final String 			userId					= "kparkings";
		final COUNTRY 			country					= COUNTRY.NETHERLANDS;
		final FUNCTION 			function 				= FUNCTION.JAVA_DEV;
		final int 				yearsExperienceGtEq 	= 2;
		final int 				yearsExperienceLtEq 	= 5;
		final boolean 			freelance 				= true;
		final boolean 			perm 					= false;
		final Language.LEVEL  	dutch 					= Language.LEVEL.BASIC;
		final Language.LEVEL 	english 				= Language.LEVEL.PROFICIENT;
		final Language.LEVEL 	french 					= Language.LEVEL.UNKNOWN;
		final String 			skill 					= "Java";
			
		CandidateSearchEvent event = CandidateSearchEvent
													.builder()
														.searchId(searchId)
														.country(country)
														.dutch(dutch)
														.english(english)
														.freelance(freelance)
														.french(french)
														.function(function)
														.perm(perm)
														.skill(skill)
														.userId(userId)
														.yearsExperienceGtEq(yearsExperienceGtEq)
														.yearsExperienceLtEq(yearsExperienceLtEq)
													.build();
		
		assertTrue(event.getTimestamp() instanceof LocalDate);
		assertEquals(searchId, 				event.getSearchId());
		assertEquals(country, 				event.getCountry().get());
		assertEquals(dutch, 				event.getDutch().get());
		assertEquals(english, 				event.getEnglish().get());
		assertEquals(freelance, 			event.getFreelance().get());
		assertEquals(french, 				event.getFrench().get());
		assertEquals(function, 				event.getFunction().get());
		assertEquals(perm, 					event.getPerm().get());
		assertEquals(skill, 				event.getSkill().get());
		assertEquals(userId,		 		event.getUserId());
		assertEquals(yearsExperienceGtEq, 	event.getYearsExperienceGtEq().get().intValue());
		assertEquals(yearsExperienceLtEq, 	event.getYearsExperienceLtEq().get().intValue());
		
	}

}