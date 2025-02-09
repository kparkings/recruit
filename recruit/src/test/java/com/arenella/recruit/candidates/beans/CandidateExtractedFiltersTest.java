package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.enums.COUNTRY;
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
		
		final String 					jobTitle			= "Java developer";
		final Set<String> 				skills 				= Set.of("java", "angular");
		final Set<Language.LANGUAGE> 	languages 			= Set.of(Language.LANGUAGE.ENGLISH, Language.LANGUAGE.CROATIAN);
		final String 					experienceGTE 		= "2";
		final String 					experienceLTE 		= "5";
		final Set<COUNTRY>				countries			= Set.of(COUNTRY.ITALY, COUNTRY.BELGIUM);
		final FREELANCE 				freelance			= FREELANCE.TRUE;
		final PERM 						perm				= PERM.TRUE;
		final String					extractedText		= "some text";
		
		CandidateExtractedFilters filters = 
				CandidateExtractedFilters.builder()
					.languages(languages)
					.experienceGTE(experienceGTE)
					.experienceLTE(experienceLTE)
					.freelance(freelance)
					.jobTitle(jobTitle)
					.perm(perm)
					.skills(skills)
					.countries(countries)
					.extractedText(extractedText)
				.build();
		
		assertEquals(jobTitle, 		filters.getJobTitle());
		assertEquals(experienceGTE, filters.getExperienceGTE());
		assertEquals(experienceLTE, filters.getExperienceLTE());
		assertEquals(freelance, 	filters.getFreelance());
		assertEquals(perm, 			filters.getPerm());
		assertEquals(extractedText, filters.getExtractedText());
		assertEquals(2, 			filters.getLanguages().size());
		
		assertTrue(filters.getSkills().contains("java"));
		assertTrue(filters.getSkills().contains("angular"));
		assertTrue(filters.getLanguages().contains(Language.LANGUAGE.CROATIAN));
		assertTrue(filters.getLanguages().contains(Language.LANGUAGE.ENGLISH));
		assertTrue(filters.getCountries().contains(COUNTRY.ITALY));
		assertTrue(filters.getCountries().contains(COUNTRY.BELGIUM));
		
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
		final FREELANCE 	freelance			= null;
		final PERM 			perm				= null;
		
		CandidateExtractedFilters filters = CandidateExtractedFilters.builder().build();
					
		assertEquals(jobTitle, 		filters.getJobTitle());
		assertEquals(experienceGTE, filters.getExperienceGTE());
		assertEquals(experienceLTE, filters.getExperienceLTE());
		assertEquals(freelance, 	filters.getFreelance());
		assertEquals(perm, 			filters.getPerm());
		
		assertTrue(filters.getSkills().isEmpty());
		assertTrue(filters.getLanguages().isEmpty());
		assertTrue(filters.getCountries().isEmpty());
	}
	
}