package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Unit tests for the CandidateExtractedFilters class
* @author K Parkings
*/
public class CandidateExtractedFiltersTest {

	/**
	* Tests construction via the Builder
	* @throws Exception
	*/
	@Test
	public void testConstructor() throws Exception{
		
		final String 		jobTitle			= "Java developer";
		final Set<String> 	skills 				= Set.of("java", "angular");
		final String 		experienceGTE 		= "2";
		final String 		experienceLTE 		= "5";
		final boolean		dutch				= true;
		final boolean		english				= true;
		final boolean		french			 	= true;
		final boolean		netherlands			= true;
		final boolean		uk				 	= true;
		final boolean		belgium				= true;
		final boolean		ireland				= true;
		final FREELANCE 	freelance			= FREELANCE.TRUE;
		final PERM 			perm				= PERM.TRUE;
		
		CandidateExtractedFilters filters = 
				CandidateExtractedFilters.builder()
					.belgium(belgium)
					.dutch(dutch)
					.english(english)
					.experienceGTE(experienceGTE)
					.experienceLTE(experienceLTE)
					.freelance(freelance)
					.french(french)
					.ireland(ireland)
					.jobTitle(jobTitle)
					.netherlands(netherlands)
					.perm(perm)
					.skills(skills)
					.uk(uk)
				.build();
		
		assertEquals(jobTitle, 		filters.getJobTitle());
		assertEquals(experienceGTE, filters.getExperienceGTE());
		assertEquals(experienceLTE, filters.getExperienceLTE());
		assertEquals(dutch, 		filters.getDutch());
		assertEquals(english, 		filters.getEnglish());
		assertEquals(french, 		filters.getFrench());
		assertEquals(netherlands, 	filters.getNetherlands());
		assertEquals(uk, 			filters.getUK());
		assertEquals(belgium, 		filters.getBelgium());
		assertEquals(ireland, 		filters.getIreland());
		assertEquals(freelance, 	filters.getFreelance());
		assertEquals(perm, 			filters.getPerm());
		
		assertTrue(filters.getSkills().contains("java"));
		assertTrue(filters.getSkills().contains("angular"));
		
	}
	
	/**
	* Tests construction via the Builder with default values
	* @throws Exception
	*/
	@Test
	public void testConstructorDefaults() throws Exception{
		
		final String 		jobTitle			= "";
		final String 		experienceGTE 		= "";
		final String 		experienceLTE 		= "";
		final boolean		dutch				= false;
		final boolean		english				= false;
		final boolean		french			 	= false;
		final boolean		netherlands			= false;
		final boolean		uk				 	= false;
		final boolean		belgium				= false;
		final boolean		ireland				= false;
		final FREELANCE 	freelance			= null;
		final PERM 			perm				= null;
		
		CandidateExtractedFilters filters = CandidateExtractedFilters.builder().build();
					
		assertEquals(jobTitle, 		filters.getJobTitle());
		assertEquals(experienceGTE, filters.getExperienceGTE());
		assertEquals(experienceLTE, filters.getExperienceLTE());
		assertEquals(dutch, 		filters.getDutch());
		assertEquals(english, 		filters.getEnglish());
		assertEquals(french, 		filters.getFrench());
		assertEquals(netherlands, 	filters.getNetherlands());
		assertEquals(uk, 			filters.getUK());
		assertEquals(belgium, 		filters.getBelgium());
		assertEquals(ireland, 		filters.getIreland());
		assertEquals(freelance, 	filters.getFreelance());
		assertEquals(perm, 			filters.getPerm());
		
		assertTrue(filters.getSkills().isEmpty());
		
	}
	
}