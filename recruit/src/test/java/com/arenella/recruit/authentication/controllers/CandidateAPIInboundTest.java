package com.arenella.recruit.authentication.controllers;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.controllers.CandidateAPIInbound;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Unit tests for the CandidateAPIInbound class 
* @author K Parkings
*/
public class CandidateAPIInboundTest {

	private static final String 			candidateId				= "C100";
	private static final  String 			firstname				= "Kevin";
	private static final  String 			surname				 	= "Parkings";
	private static final  String 			email					= "kparkings@gmail.com";
	private static final  String			roleSought				= "Java develper";
	private static final  FUNCTION			function				= FUNCTION.JAVA_DEV;
	private static final  COUNTRY 			country					= COUNTRY.NETHERLANDS;
	private static final  String 			city					= "Noordwijk";
	private static final  PERM 				perm					= PERM.FALSE;
	private static final  FREELANCE 		freelance				= FREELANCE.TRUE;
	private static final  int				yearsExperience			= 22;
	private static final  boolean 			available				= true;
	private static final  Set<String> 		skills					= Set.of("Java","Angular");
	private static final  Set<Language> 	languages				= Set.of(Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build());
	
	/**
	* Tests construction via builder with no values provided
	* @throws Exception
	*/
	@Test
	public void testBuilder_defaults() throws Exception {
		
		CandidateAPIInbound candidate = CandidateAPIInbound.builder().build();
		
		assertTrue("Expect empty but instantiated Set", candidate.getSkills().isEmpty());
		assertTrue("Expect empty but instantiated Set", candidate.getLanguages().isEmpty());
		assertEquals(0, 								candidate.getYearsExperience());
		assertFalse(candidate.isAvailable());
		assertNull(candidate.isFreelance());
		assertNull(candidate.isPerm());
		assertNull(candidate.getFunction());
		assertNull(candidate.getCountry());
		
	}
	
	/**
	* Tests construction via builder
	* @throws Exception
	*/
	@Test
	public void testBuilder() throws Exception{
		
		CandidateAPIInbound candidate = CandidateAPIInbound
													.builder()
														.available(available)
														.candidateId(candidateId)
														.city(city)
														.country(country)
														.email(email)
														.firstname(firstname)
														.freelance(freelance)
														.function(function)
														.languages(languages)
														.perm(perm)
														.roleSought(roleSought)
														.skills(skills)
														.surname(surname)
														.yearsExperience(yearsExperience)
													.build();
		
		assertEquals(candidateId, 		candidate.getCandidateId());
		assertEquals(firstname, 		candidate.getFirstname());
		assertEquals(surname, 			candidate.getSurname());
		assertEquals(email, 			candidate.getEmail());
		assertEquals(roleSought, 		candidate.getRoleSought());
		assertEquals(function, 			candidate.getFunction());
		assertEquals(country, 			candidate.getCountry());
		assertEquals(city, 				candidate.getCity());
		assertEquals(perm, 				candidate.isPerm());
		assertEquals(freelance, 		candidate.isFreelance());
		assertEquals(yearsExperience, 	candidate.getYearsExperience());
		assertEquals(available, 		candidate.isAvailable());
		
		assertTrue(skills.contains("Java"));
		assertTrue(skills.contains("Angular"));
		assertTrue(languages.stream().filter(l ->l.getLanguage() == LANGUAGE.DUTCH).findAny().isPresent());
		
	}
	
	@Test
	public void testConvertToCandidate() throws Exception{
		
		CandidateAPIInbound candidateAPIInbound = CandidateAPIInbound
				.builder()
					.available(available)
					.candidateId(candidateId)
					.city(city)
					.country(country)
					.email(email)
					.firstname(firstname)
					.freelance(freelance)
					.function(function)
					.languages(languages)
					.perm(perm)
					.roleSought(roleSought)
					.skills(skills)
					.surname(surname)
					.yearsExperience(yearsExperience)
				.build();
		
		Candidate candidate = CandidateAPIInbound.convertToCandidate(candidateAPIInbound);
		
		assertEquals(candidate.getCandidateId(), 		candidate.getCandidateId());
		assertEquals(candidate.getFirstname(), 		candidate.getFirstname());
		assertEquals(candidate.getSurname(), 			candidate.getSurname());
		assertEquals(candidate.getEmail(), 			candidate.getEmail());
		assertEquals(candidate.getRoleSought(), 		candidate.getRoleSought());
		assertEquals(candidate.getFunction(), 			candidate.getFunction());
		assertEquals(candidate.getCountry(), 			candidate.getCountry());
		assertEquals(candidate.getCity(), 				candidate.getCity());
		assertEquals(candidate.isPerm(), 				candidate.isPerm());
		assertEquals(freelance, 		candidate.isFreelance());
		assertEquals(yearsExperience, 	candidate.getYearsExperience());
		assertEquals(available, 		candidate.isAvailable());
		
		assertTrue(skills.contains("Java"));
		assertTrue(skills.contains("Angular"));
		assertTrue(languages.stream().filter(l ->l.getLanguage() == LANGUAGE.DUTCH).findAny().isPresent());
		
		
	}
	
}