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
public class CandidateAPIInboundTest {

	private static final String 			candidateId				= "C100";
	private static final  String 			firstname				= "Kevin";
	private static final  String 			surname				 	= "Parkings";
	private static final  String 			email					= "admin@arenella-ict.com";
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
	public void testBuilder_defaults() throws Exception {
		
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
														.functions(Set.of(function))
														.languages(languages)
														.perm(perm)
														.roleSought(roleSought)
														.skills(skills)
														.surname(surname)
														.yearsExperience(yearsExperience)
														.comments(COMMENTS)
														.introduction(INTRODUCTION)
														.daysOnSite(DAYS_ON_SITE_VAL)
														.rateContract(RATE_CONTRACT)
														.ratePerm(RATE_PERM)
														.availableFromDate(AVAILABLE_FROM_DATE)
													.build();
		
		assertEquals(candidateId, 			candidate.getCandidateId());
		assertEquals(firstname, 			candidate.getFirstname());
		assertEquals(surname, 				candidate.getSurname());
		assertEquals(email, 				candidate.getEmail());
		assertEquals(roleSought, 			candidate.getRoleSought());
		assertEquals(function, 				candidate.getFunctions().toArray()[0]);
		assertEquals(country, 				candidate.getCountry());
		assertEquals(city, 					candidate.getCity());
		assertEquals(perm, 					candidate.isPerm());
		assertEquals(freelance, 			candidate.isFreelance());
		assertEquals(yearsExperience, 		candidate.getYearsExperience());
		assertEquals(available, 			candidate.isAvailable());
		assertEquals(DAYS_ON_SITE_VAL, 		candidate.getDaysOnSite());
		assertEquals(RATE_CONTRACT, 		candidate.getRateContract().get());
		assertEquals(RATE_PERM, 			candidate.getRatePerm().get());
		assertEquals(AVAILABLE_FROM_DATE, 	candidate.getAvailableFromDate());
		assertEquals(COMMENTS, 				candidate.getComments());
		assertEquals(INTRODUCTION, 			candidate.getIntroduction());
		
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
					.functions(Set.of(function))
					.languages(languages)
					.perm(perm)
					.roleSought(roleSought)
					.skills(skills)
					.surname(surname)
					.yearsExperience(yearsExperience)
					.comments(COMMENTS)
					.introduction(INTRODUCTION)
					.daysOnSite(DAYS_ON_SITE_VAL)
					.rateContract(RATE_CONTRACT)
					.ratePerm(RATE_PERM)
					.availableFromDate(AVAILABLE_FROM_DATE)
				.build();
		
		Candidate candidate = CandidateAPIInbound.convertToCandidate(candidateAPIInbound, Optional.empty());
		
		assertEquals(candidate.getCandidateId(), 	candidate.getCandidateId());
		assertEquals(candidate.getFirstname(), 		candidate.getFirstname());
		assertEquals(candidate.getSurname(), 		candidate.getSurname());
		assertEquals(candidate.getEmail(), 			candidate.getEmail());
		assertEquals(candidate.getRoleSought(), 	candidate.getRoleSought());
		assertEquals(candidate.getFunctions(), 		candidate.getFunctions().toArray()[0]);
		assertEquals(candidate.getCountry(), 		candidate.getCountry());
		assertEquals(candidate.getCity(), 			candidate.getCity());
		assertEquals(candidate.isPerm(), 			candidate.isPerm());
		
		assertEquals(DAYS_ON_SITE_VAL, 				candidate.getDaysOnSite());
		assertEquals(RATE_CONTRACT, 				candidate.getRateContract().get());
		assertEquals(RATE_PERM, 					candidate.getRatePerm().get());
		assertEquals(AVAILABLE_FROM_DATE, 			candidate.getAvailableFromDate());
		assertEquals(COMMENTS, 						candidate.getComments());
		assertEquals(INTRODUCTION, 					candidate.getIntroduction());
		
		assertEquals(freelance, 					candidate.isFreelance());
		assertEquals(yearsExperience, 				candidate.getYearsExperience());
		assertEquals(available, 					candidate.isAvailable());
		
		assertTrue(skills.contains("Java"));
		assertTrue(skills.contains("Angular"));
		assertTrue(languages.stream().filter(l ->l.getLanguage() == LANGUAGE.DUTCH).findAny().isPresent());
		
		
	}
	
}