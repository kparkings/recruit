package com.arenella.recruit.candidates.beans;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;
import com.arenella.recruit.candidates.utils.CandidateSuggestionUtil.suggestion_accuracy;

/**
* Unit tests for the CandidateSearchAccuracyWrapper class
* @author K Parkings
*/
class CandidateSearchAccuracyWrapperTest {

	private static final String 				CANDIDATE_ID 			= "Candidate1";
	private static final FUNCTION				FUNCTION_VAL			= FUNCTION.JAVA_DEV;
	private static final COUNTRY 				COUNTRY_VAL				= COUNTRY.NETHERLANDS;
	private static final String 				CITY 					= "Den Haag";
	private static final String 				EMAIL					= "admin@arenella-ict.com";
	private static final String 				ROLE_SOUGHT				= "Senior java Dev";
	private static final boolean 				AVAILABLE 				= true;
	private static final boolean 				FLAGGED_AS_UNAVAILABLE	= true;
	private static final FREELANCE 				FREELANCE_VAL			= FREELANCE.TRUE;
	private static final PERM 					PERM_VAL				= PERM.TRUE;
	private static final LocalDate 				LAST_AVAILABILITY_CHK 	= LocalDate.of(1980, 12, 3);
	private static final LocalDate 				REGISTERD 				= LocalDate.of(2021, 02, 20);
	private static final int 					YEARS_EXPERIENCE 		= 21;
	private static final Set<String>			SKILLS					= new LinkedHashSet<>();
	private static final Set<Language>			LANGUAGES				= new LinkedHashSet<>();
	private static final String					SKILL					= "Java";
	private static final Language				LANGUAGE_VAL			= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
	private static final suggestion_accuracy 	ACCURACY_SKILLS			= suggestion_accuracy.perfect;
	private static final suggestion_accuracy 	ACCURACY_LANGUAGES		= suggestion_accuracy.poor; 
	
	/**
	* Sets up test environment 
	*/
	public CandidateSearchAccuracyWrapperTest(){
		
		LANGUAGES.add(LANGUAGE_VAL);
		SKILLS.add(SKILL);
		
	}
	
	/**
	* Test construction
	* @throws Exception
	*/
	@Test
	void testInstantiations() {
		
		Candidate candidate = Candidate
				.builder()
					.candidateId(CANDIDATE_ID)
					.functions(Set.of(FUNCTION_VAL))
					.country(COUNTRY_VAL)
					.city(CITY)
					.email(EMAIL)
					.roleSought(ROLE_SOUGHT)
					.available(AVAILABLE)
					.flaggedAsUnavailable(FLAGGED_AS_UNAVAILABLE)
					.freelance(FREELANCE_VAL)
					.perm(PERM_VAL)
					.lastAvailabilityCheck(LAST_AVAILABILITY_CHK)
					.registerd(REGISTERD)
					.yearsExperience(YEARS_EXPERIENCE)
					.skills(SKILLS)
					.languages(LANGUAGES)
					.build();

			CandidateSearchAccuracyWrapper wrapper = new CandidateSearchAccuracyWrapper(candidate);
		
			wrapper.setAccuracyLanguages(ACCURACY_LANGUAGES);
			wrapper.setAccuracySkills(ACCURACY_SKILLS);
			
			assertEquals(CANDIDATE_ID, 								wrapper.get().getCandidateId());
			assertEquals(FUNCTION_VAL, 								wrapper.get().getFunctions().toArray()[0]);
			assertEquals(COUNTRY_VAL, 								wrapper.get().getCountry());
			assertEquals(CITY, 										wrapper.get().getCity());
			assertEquals(EMAIL, 									wrapper.get().getEmail());
			assertEquals(ROLE_SOUGHT, 								wrapper.get().getRoleSought());
			assertEquals(AVAILABLE, 								wrapper.get().isAvailable());
			assertEquals(FLAGGED_AS_UNAVAILABLE, 					wrapper.get().isFlaggedAsUnavailable());
			assertEquals(FREELANCE_VAL, 							wrapper.get().isFreelance());
			assertEquals(PERM_VAL, 									wrapper.get().isPerm());
			assertEquals(LAST_AVAILABILITY_CHK, 					wrapper.get().getLastAvailabilityCheckOn());
			assertEquals(REGISTERD, 								wrapper.get().getRegisteredOn());
			assertEquals(YEARS_EXPERIENCE, 							wrapper.get().getYearsExperience());
			assertEquals(ACCURACY_LANGUAGES, 						wrapper.getAccuracyLanguages());
			assertEquals(ACCURACY_SKILLS, 							wrapper.getAccuracySkills());
			assertEquals(suggestion_accuracy.perfect.asNumber(), 	wrapper.getAccuracySkillsAsNumber());
			assertEquals(suggestion_accuracy.poor.asNumber(), 		wrapper.getAccuracyLanguagesAsNumber());
			
			assertTrue(wrapper.get().getSkills().contains(SKILL));
			wrapper.get().getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGE_VAL.getLanguage()).findAny().orElseThrow();

	}
	
}