package com.arenella.recruit.authentication.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.controllers.CandidateStandardAPIOutbound;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Unit tests for the CandidateAPIOutbount class 
* @author K Parkings
*/
public class CandidateStandardAPIOutboundTest {

	private final String 			candidateId				= "999";
	private final String 			firstname				= "Kevin";
	private final String 			surname					= "Parkings";
	private final String 			email					= "admin@arenella-ict.com";
	private final String			roleSought				= "Senior Java Developer";
	private final FUNCTION			function				= FUNCTION.JAVA_DEV;
	private final COUNTRY 			country					= COUNTRY.NETHERLANDS;
	private final String 			city					= "Noordwijk";
	private final PERM 				perm					= PERM.FALSE;
	private final FREELANCE 		freelance				= FREELANCE.TRUE;
	private final int				yearsExperience			= 22;
	private final boolean			flaggedAsUnavailable	= true;
	private final boolean 			available				= true;
	private final Set<String> 		skills					= Set.of("Java","Angular");
	private final Set<Language> 	languages				= Set.of(Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build());
	private final LocalDate			lastAvailabilityCheck	= LocalDate.of(2022, 05, 20);
	private final LocalDate			registered				= LocalDate.of(2021, 05, 20);
	
	/**
	* Tests construction via a Builder
	* @throws Exception
	*/
	@Test
	void testBuilder() throws Exception{
		
		CandidateStandardAPIOutbound candidate = CandidateStandardAPIOutbound
														.builder()
															.available(available)
															.candidateId(candidateId)
															.city(city)
															.country(country)
															.freelance(freelance)
															.functions(Set.of(function))
															.languages(languages)
															.flaggedAsUnavailable(flaggedAsUnavailable)
															.lastAvailabilityCheck(lastAvailabilityCheck)
															.perm(perm)
															.roleSought(roleSought)
															.skills(skills)
															.yearsExperience(yearsExperience)
															.firstname(firstname)
															.surname(surname)
															.email(email)
														.build();
		
		assertEquals(candidateId, 				candidate.getCandidateId());
		assertEquals(roleSought, 				candidate.getRoleSought());
		assertEquals(function, 					candidate.getFunctions().toArray()[0]);
		assertEquals(country, 					candidate.getCountry());
		assertEquals(city, 						candidate.getCity());
		assertEquals(perm, 						candidate.getPerm());
		assertEquals(freelance, 				candidate.getFreelance());
		assertEquals(yearsExperience, 			candidate.getYearsExperience());
		assertEquals(available, 				candidate.isAvailable());
		assertEquals(flaggedAsUnavailable, 		candidate.isFlaggedAsUnavailable());
		assertEquals(lastAvailabilityCheck, 	candidate.getLastAvailabilityCheckOn());
		assertEquals(firstname, 				candidate.getFirstname());
		assertEquals(surname, 					candidate.getSurname());
		assertEquals(email, 					candidate.getEmail());
		
		
		assertTrue( candidate.getSkills().contains("Java"));
		assertTrue(candidate.getSkills().contains("Angular"));
		
		candidate.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGE.DUTCH && l.getLevel() == LEVEL.PROFICIENT).findAny().orElseThrow();
		
	}
	
	/**
	* Tests that by default all collections exist but are empty. This is defensive programing to 
	* avoid unexpected nullPointerExceptions
	* @throws Exception
	*/
	@Test
	public void testBuilderDefaultCollections() throws Exception {
		
		CandidateStandardAPIOutbound candidate = CandidateStandardAPIOutbound.builder().build();
		
		assertTrue(candidate.getSkills().isEmpty());
		assertTrue(candidate.getLanguages().isEmpty());
		
	}
	
	/**
	* Tests convertsion from Domain representatin of Candidate to API Outbound
	* representation
	* @throws Exception
	*/
	@Test
	void testConvertFromCandidate() throws Exception{
		
		Candidate candidate = Candidate
									.builder()
										.flaggedAsUnavailable(flaggedAsUnavailable)
										.available(available)
										.candidateId(candidateId)
										.city(city)
										.email(email)
										.country(country)
										.freelance(freelance)
										.functions(Set.of(function))
										.languages(languages)
										.lastAvailabilityCheck(lastAvailabilityCheck)
										.perm(perm)
										.roleSought(roleSought)
										.skills(skills)
										.yearsExperience(yearsExperience)
										.firstname(firstname)
										.registerd(registered)
										.surname(surname)
										.available(available)
									.build();
		
		CandidateStandardAPIOutbound candidateAPIOutbound = CandidateStandardAPIOutbound.convertFromCandidate(candidate);
		
		assertEquals(candidateId, 				candidateAPIOutbound.getCandidateId());
		assertEquals(roleSought, 				candidateAPIOutbound.getRoleSought());
		assertEquals(function, 					candidateAPIOutbound.getFunctions().toArray()[0]);
		assertEquals(country, 					candidateAPIOutbound.getCountry());
		assertEquals(city, 						candidateAPIOutbound.getCity());
		assertEquals(perm, 						candidateAPIOutbound.getPerm());
		assertEquals(freelance, 				candidateAPIOutbound.getFreelance());
		assertEquals(yearsExperience, 			candidateAPIOutbound.getYearsExperience());
		assertEquals(available, 				candidateAPIOutbound.isAvailable());
		assertEquals(flaggedAsUnavailable, 		candidateAPIOutbound.isFlaggedAsUnavailable());
		assertEquals(lastAvailabilityCheck, 	candidateAPIOutbound.getLastAvailabilityCheckOn());
		assertEquals(firstname, 				candidate.getFirstname());
		assertEquals(surname, 					candidate.getSurname());
		assertEquals(email, 					candidate.getEmail());
		
		assertTrue(candidateAPIOutbound.getSkills().contains("Java"));
		assertTrue(candidateAPIOutbound.getSkills().contains("Angular"));
		
		candidateAPIOutbound.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGE.DUTCH && l.getLevel() == LEVEL.PROFICIENT).findAny().orElseThrow();
		
	}
	
}