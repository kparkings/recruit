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
public class CandidateSearchAccuracyWrapperTest {

	private static final String 				candidateId 			= "Candidate1";
	private static final FUNCTION				function				= FUNCTION.JAVA_DEV;
	private static final COUNTRY 				country 				= COUNTRY.NETHERLANDS;
	private static final String 				city 					= "Den Haag";
	private static final String 				email					= "kparkings@gmail.com";
	private static final String 				roleSought				= "Senior java Dev";
	private static final boolean 				available 				= true;
	private static final boolean 				flaggedAsUnavailable	= true;
	private static final FREELANCE 				freelance 				= FREELANCE.TRUE;
	private static final PERM 					perm 					= PERM.TRUE;
	private static final LocalDate 				lastAvailabilityCheck 	= LocalDate.of(1980, 12, 3);
	private static final LocalDate 				registerd 				= LocalDate.of(2021, 02, 20);
	private static final int 					yearsExperience 		= 21;
	private static final Set<String>			skills					= new LinkedHashSet<>();
	private static final Set<Language>			languages				= new LinkedHashSet<>();
	private static final String					skill					= "Java";
	private static final Language				language				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
	private static final suggestion_accuracy 	accuracySkills			= suggestion_accuracy.perfect;
	private static final suggestion_accuracy 	accuracyLanguages		= suggestion_accuracy.poor; 
	
	/**
	* Sets up test environment 
	*/
	public CandidateSearchAccuracyWrapperTest(){
		
		languages.add(language);
		skills.add(skill);
		
	}
	
	/**
	* Test construction
	* @throws Exception
	*/
	@Test
	public void testInstantiations() throws Exception {
		
		Candidate candidate = Candidate
				.builder()
					.candidateId(candidateId)
					.function(function)
					.country(country)
					.city(city)
					.email(email)
					.roleSought(roleSought)
					.available(available)
					.flaggedAsUnavailable(flaggedAsUnavailable)
					.freelance(freelance)
					.perm(perm)
					.lastAvailabilityCheck(lastAvailabilityCheck)
					.registerd(registerd)
					.yearsExperience(yearsExperience)
					.skills(skills)
					.languages(languages)
					.build();

			CandidateSearchAccuracyWrapper wrapper = new CandidateSearchAccuracyWrapper(candidate);
		
			wrapper.setAccuracyLanguages(accuracyLanguages);
			wrapper.setAccuracySkills(accuracySkills);
			
			assertEquals(wrapper.get().getCandidateId(), 				candidateId);
			assertEquals(wrapper.get().getFunction(), 					function);
			assertEquals(wrapper.get().getCountry(), 					country);
			assertEquals(wrapper.get().getCity(), 						city);
			assertEquals(wrapper.get().getEmail(), 						email);
			assertEquals(wrapper.get().getRoleSought(), 				roleSought);
			assertEquals(wrapper.get().isAvailable(), 					available);
			assertEquals(wrapper.get().isFlaggedAsUnavailable(), 		flaggedAsUnavailable);
			assertEquals(wrapper.get().isFreelance(), 					freelance);
			assertEquals(wrapper.get().isPerm(), 						perm);
			assertEquals(wrapper.get().getLastAvailabilityCheckOn(), 	lastAvailabilityCheck);
			assertEquals(wrapper.get().getRegisteredOn(), 				registerd);
			assertEquals(wrapper.get().getYearsExperience(), 			yearsExperience);
			assertEquals(wrapper.getAccuracyLanguages(), 				accuracyLanguages);
			assertEquals(wrapper.getAccuracySkills(), 					accuracySkills);
			
			assertTrue(wrapper.get().getSkills().contains(skill));
			wrapper.get().getLanguages().stream().filter(l -> l.getLanguage() == language.getLanguage()).findAny().orElseThrow();

	}
	
}