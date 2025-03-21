package com.arenella.recruit.candidates.controllers;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Unit tests for the CandidateAPIInboundTest class
* @author K Parkings
*/
class CandidateAPOutboundTest {

	private static final String 		candidateId 			= "Candidate1";
	private static final FUNCTION		function				= FUNCTION.JAVA_DEV;
	private static final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
	private static final String 		city 					= "Den Haag";
	private static final boolean 		available 				= true;
	private static final FREELANCE 		freelance 				= FREELANCE.TRUE;
	private static final PERM	 		perm 					= PERM.TRUE;
	private static final LocalDate 		lastAvailabilityCheck 	= LocalDate.of(1980, 12, 3);
	private static final LocalDate 		registerd 				= LocalDate.of(2021, 02, 20);
	private static final int 			yearsExperience 		= 21;
	private static final Set<String>	skills					= new LinkedHashSet<>();
	private static final Set<Language>	languages				= new LinkedHashSet<>();
	private static final String			skill					= "Java";
	private static final Language		language				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
	
	/**
	* Sets up test environment 
	*/
	public CandidateAPOutboundTest(){
		
		languages.add(language);
		skills.add(skill);
		
	}
	
	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	void testInitializationFromBuilder() {
		
		Candidate candidate = Candidate
						.builder()
							.candidateId(candidateId)
							.functions(Set.of(function))
							.country(country)
							.city(city)
							.available(available)
							.freelance(freelance)
							.perm(perm)
							.lastAvailabilityCheck(lastAvailabilityCheck)
							.registerd(registerd)
							.yearsExperience(yearsExperience)
							.skills(skills)
							.languages(languages)
							.build();
		
		assertEquals(candidateId, 			candidate.getCandidateId());
		assertEquals(function, 				candidate.getFunctions().toArray()[0]);
		assertEquals(country, 				candidate.getCountry());
		assertEquals(city, 					candidate.getCity());
		assertEquals(available, 			candidate.isAvailable());
		assertEquals(freelance, 			candidate.isFreelance());
		assertEquals(perm, 					candidate.isPerm());
		assertEquals(lastAvailabilityCheck, candidate.getLastAvailabilityCheckOn());
		assertEquals(registerd, 			candidate.getRegisteredOn());
		assertEquals(yearsExperience, 		candidate.getYearsExperience());
		
		assertTrue(candidate.getSkills().contains(skill));
		candidate.getLanguages().stream().filter(l -> l.getLanguage() == language.getLanguage()).findAny().orElseThrow();
		
	}
	
}