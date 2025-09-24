package com.arenella.recruit.authentication.controllers;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Candidate.DAYS_ON_SITE;
import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;
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
class CandidateAPIInboundTest {

	private static final String 			CANDIDATE_ID			= "C100";
	private static final  String 			FIRST_NAME				= "Kevin";
	private static final  String 			SURNAME				 	= "Parkings";
	private static final  String 			EMAIL					= "admin@arenella-ict.com";
	private static final  String			ROLE_SOUGHT				= "Java develper";
	private static final  FUNCTION			FUNCTION_VAL			= FUNCTION.JAVA_DEV;
	private static final  COUNTRY 			COUNTRY_VAL				= COUNTRY.NETHERLANDS;
	private static final  String 			CITY					= "Noordwijk";
	private static final  PERM 				PERM_VAL				= PERM.FALSE;
	private static final  FREELANCE 		FREELANCE_VAL			= FREELANCE.TRUE;
	private static final  int				YEARS_EXPERIENCE		= 22;
	private static final  boolean 			AVAILABLE				= true;
	private static final  Set<String> 		SKILLS					= Set.of("Java","Angular");
	private static final  Set<Language> 	LANGUAGES				= Set.of(Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build());
	
	private static final String 			COMMENTS				= "aComment";
	private static final String 			INTRODUCTION 			= "anIntro";
	private static final DAYS_ON_SITE		DAYS_ON_SITE_VAL		= DAYS_ON_SITE.ZERO;
	private static final Rate				RATE_CONTRACT 			= new Rate(CURRENCY.EUR, PERIOD.DAY, 80.50f, 10.0f);
	private static final Rate				RATE_PERM 				= new Rate(CURRENCY.EUR, PERIOD.YEAR, 25000f, 12.0f);
	private static final LocalDate 			AVAILABLE_FROM_DATE 	= LocalDate.of(2023, 7, 21);
	
	/**
	* Tests construction via builder with no values provided
	* @throws Exception
	*/
	@Test
	void testBuilder_defaults() {
		
		CandidateAPIInbound candidate = CandidateAPIInbound.builder().build();
		
		assertTrue("Expect empty but instantiated Set", candidate.getSkills().isEmpty());
		assertTrue("Expect empty but instantiated Set", candidate.getLanguages().isEmpty());
		assertEquals(0, 								candidate.getYearsExperience());
		assertTrue(candidate.isAvailable());
		assertNull(candidate.isFreelance());
		assertNull(candidate.isPerm());
		assertNull(candidate.getCountry());
		
		assertTrue(candidate.getRateContract().isEmpty());
		assertTrue(candidate.getRatePerm().isEmpty());
		assertTrue(candidate.getFunctions().isEmpty());
		
		assertEquals(candidate.getAvailableFromDate(), LocalDate.now()); //[KP] Small chance of failure if test run in exactly midnight
		
		
	}
	
	/**
	* Tests construction via builder
	* @throws Exception
	*/
	@Test
	void testBuilder() {
		
		CandidateAPIInbound candidate = CandidateAPIInbound
													.builder()
														.available(AVAILABLE)
														.candidateId(CANDIDATE_ID)
														.city(CITY)
														.country(COUNTRY_VAL)
														.email(EMAIL)
														.firstname(FIRST_NAME)
														.freelance(FREELANCE_VAL)
														.functions(Set.of(FUNCTION_VAL))
														.languages(LANGUAGES)
														.perm(PERM_VAL)
														.roleSought(ROLE_SOUGHT)
														.skills(SKILLS)
														.surname(SURNAME)
														.yearsExperience(YEARS_EXPERIENCE)
														.comments(COMMENTS)
														.introduction(INTRODUCTION)
														.daysOnSite(DAYS_ON_SITE_VAL)
														.rateContract(RATE_CONTRACT)
														.ratePerm(RATE_PERM)
														.availableFromDate(AVAILABLE_FROM_DATE)
													.build();
		
		assertEquals(CANDIDATE_ID, 			candidate.getCandidateId());
		assertEquals(FIRST_NAME, 			candidate.getFirstname());
		assertEquals(SURNAME, 				candidate.getSurname());
		assertEquals(EMAIL, 				candidate.getEmail());
		assertEquals(ROLE_SOUGHT, 			candidate.getRoleSought());
		assertEquals(FUNCTION_VAL, 			candidate.getFunctions().toArray()[0]);
		assertEquals(COUNTRY_VAL, 			candidate.getCountry());
		assertEquals(CITY, 					candidate.getCity());
		assertEquals(PERM_VAL, 				candidate.isPerm());
		assertEquals(FREELANCE_VAL, 			candidate.isFreelance());
		assertEquals(YEARS_EXPERIENCE, 		candidate.getYearsExperience());
		assertEquals(AVAILABLE, 			candidate.isAvailable());
		assertEquals(DAYS_ON_SITE_VAL, 		candidate.getDaysOnSite());
		assertEquals(RATE_CONTRACT, 		candidate.getRateContract().get());
		assertEquals(RATE_PERM, 			candidate.getRatePerm().get());
		assertEquals(AVAILABLE_FROM_DATE, 	candidate.getAvailableFromDate());
		assertEquals(COMMENTS, 				candidate.getComments());
		assertEquals(INTRODUCTION, 			candidate.getIntroduction());
		
		assertTrue(SKILLS.contains("Java"));
		assertTrue(SKILLS.contains("Angular"));
		assertTrue(LANGUAGES.stream().filter(l ->l.getLanguage() == LANGUAGE.DUTCH).findAny().isPresent());
		
	}
	
	@Test
	void testConvertToCandidate() {
		
		CandidateAPIInbound candidateAPIInbound = CandidateAPIInbound
				.builder()
					.available(AVAILABLE)
					.candidateId(CANDIDATE_ID)
					.city(CITY)
					.country(COUNTRY_VAL)
					.email(EMAIL)
					.firstname(FIRST_NAME)
					.freelance(FREELANCE_VAL)
					.functions(Set.of(FUNCTION_VAL))
					.languages(LANGUAGES)
					.perm(PERM_VAL)
					.roleSought(ROLE_SOUGHT)
					.skills(SKILLS)
					.surname(SURNAME)
					.yearsExperience(YEARS_EXPERIENCE)
					.comments(COMMENTS)
					.introduction(INTRODUCTION)
					.daysOnSite(DAYS_ON_SITE_VAL)
					.rateContract(RATE_CONTRACT)
					.ratePerm(RATE_PERM)
					.availableFromDate(AVAILABLE_FROM_DATE)
				.build();
		
		Candidate candidate = CandidateAPIInbound.convertToCandidate(candidateAPIInbound, Optional.empty());
		
		assertEquals(candidateAPIInbound.getCandidateId(), 				candidate.getCandidateId());
		assertEquals(candidateAPIInbound.getFirstname(), 				candidate.getFirstname());
		assertEquals(candidateAPIInbound.getSurname(), 					candidate.getSurname());
		assertEquals(candidateAPIInbound.getEmail(), 					candidate.getEmail());
		assertEquals(candidateAPIInbound.getRoleSought(), 				candidate.getRoleSought());
		assertEquals(candidateAPIInbound.getFunctions().toArray()[0], 	candidate.getFunctions().toArray()[0]);
		assertEquals(candidateAPIInbound.getCountry(), 					candidate.getCountry());
		assertEquals(candidateAPIInbound.getCity(), 					candidate.getCity());
		assertEquals(candidateAPIInbound.isPerm(), 						candidate.isPerm());
		
		assertEquals(DAYS_ON_SITE_VAL, 									candidate.getDaysOnSite());
		assertEquals(RATE_CONTRACT, 									candidate.getRateContract().get());
		assertEquals(RATE_PERM, 										candidate.getRatePerm().get());
		assertEquals(AVAILABLE_FROM_DATE, 								candidate.getAvailableFromDate());
		assertEquals(COMMENTS, 											candidate.getComments());
		assertEquals(INTRODUCTION, 										candidate.getIntroduction());
		
		assertEquals(FREELANCE_VAL,										candidate.isFreelance());
		assertEquals(YEARS_EXPERIENCE, 									candidate.getYearsExperience());
		assertEquals(AVAILABLE, 										candidate.isAvailable());
		
		assertTrue(SKILLS.contains("Java"));
		assertTrue(SKILLS.contains("Angular"));
		assertTrue(LANGUAGES.stream().filter(l ->l.getLanguage() == LANGUAGE.DUTCH).findAny().isPresent());
		
	}
	
}