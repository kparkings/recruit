package com.arenella.recruit.candidates.controllers;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateUpdateRequest;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Candidate.DAYS_ON_SITE;
import com.arenella.recruit.candidates.beans.Candidate.SECURITY_CLEARANCE_TYPE;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.controllers.CandidateUpdateRequestAPIInbound.Rate;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Unit tests for the CandidateUpdateRequestAPIInbound class
* @author K Parkings
*/
public class CandidateUpdateRequestAPIInboundTest {

	private static final String 					CANDIDATE_ID 			= "Candidate1";
	private static final FUNCTION					FUNCTION_VAL			= FUNCTION.JAVA_DEV;
	private static final COUNTRY 					COUNTRY_VAL 			= COUNTRY.NETHERLANDS;
	private static final String 					CITY 					= "Den Haag";
	private static final String 					EMAIL					= "admin@arenella-ict.com";
	private static final String 					ROLE_SOUGHT				= "Senior java Dev";
	private static final FREELANCE 					FREELANCE_VAL 			= FREELANCE.TRUE;
	private static final PERM 						PERM_VAL 				= PERM.TRUE;
	private static final int 						YEARS_EXPERIENCE 		= 21;
	private static final Set<String>				SKILLS					= new LinkedHashSet<>();
	private static final Set<Language>				LANGUAGES				= new LinkedHashSet<>();
	private static final String						SKILL					= "Java";
	private static final Language					LANGUAGE_VAL			= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
	private static final String 					COMMENTS				= "aComment";
	private static final String 					INTRODUCTION 			= "anIntro";
	private static final DAYS_ON_SITE				DAYS_ON_SITE_VAL		= DAYS_ON_SITE.ZERO;
	private static final Rate						RATE_CONTRACT 			= new Rate(CURRENCY.EUR, PERIOD.DAY, 80.50f, 90f);
	private static final Rate						RATE_PERM 				= new Rate(CURRENCY.EUR, PERIOD.YEAR, 25000f, 35000);
	
	private static final LocalDate 					AVAILABLE_FROM_DATE 	= LocalDate.of(2023, 7, 21);
	private static final SECURITY_CLEARANCE_TYPE 	SECURITY_CLEARANCE		= SECURITY_CLEARANCE_TYPE.DV;
	private static final boolean					REQUIRES_SPONSORSHIP = true;
	/**
	* Sets up test environment 
	*/
	public CandidateUpdateRequestAPIInboundTest(){
		LANGUAGES.add(LANGUAGE_VAL);
		SKILLS.add(SKILL);
	}
	
	/**
	* Tests construction via builder with no values provided
	* @throws Exception
	*/
	@Test
	public void testBuilder_defaults() throws Exception {
		
		CandidateUpdateRequestAPIInbound candidate = CandidateUpdateRequestAPIInbound.builder().build();
		
		assertTrue("Expect empty but instantiated Set", candidate.getSkills().isEmpty());
		assertTrue("Expect empty but instantiated Set", candidate.getLanguages().isEmpty());
		assertEquals(0, 								candidate.getYearsExperience());
		assertNull(candidate.isFreelance());
		assertNull(candidate.isPerm());
		assertNull(candidate.getFunction());
		assertNull(candidate.getCountry());
		
		assertTrue(candidate.getRateContract().isEmpty());
		assertTrue(candidate.getRatePerm().isEmpty());
		
		assertEquals(candidate.getAvailableFromDate(), LocalDate.now()); //[KP] Small chance of failure if test run in exactly midnight
		
		
	}
	
	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	public void testInitializationFromBuilder() {
		
		CandidateUpdateRequestAPIInbound candidate = CandidateUpdateRequestAPIInbound
						.builder()
							.function(FUNCTION_VAL)
							.country(COUNTRY_VAL)
							.city(CITY)
							.email(EMAIL)
							.roleSought(ROLE_SOUGHT)
							.freelance(FREELANCE_VAL)
							.perm(PERM_VAL)
							.yearsExperience(YEARS_EXPERIENCE)
							.languages(LANGUAGES)
							.skills(SKILLS)
							.comments(COMMENTS)
							.introduction(INTRODUCTION)
							.daysOnSite(DAYS_ON_SITE_VAL)
							.rateContract(RATE_CONTRACT)
							.ratePerm(RATE_PERM)
							.availableFromDate(AVAILABLE_FROM_DATE)
							.requiresSponsorship(REQUIRES_SPONSORSHIP)
							.securityClearance(SECURITY_CLEARANCE)
							.build();
		
		assertEquals(FUNCTION_VAL, 			candidate.getFunction());
		assertEquals(COUNTRY_VAL, 			candidate.getCountry());
		assertEquals(CITY, 					candidate.getCity());
		assertEquals(EMAIL, 				candidate.getEmail());
		assertEquals(ROLE_SOUGHT, 			candidate.getRoleSought());
		assertEquals(FREELANCE_VAL, 		candidate.isFreelance());
		assertEquals(PERM_VAL, 				candidate.isPerm());
		assertEquals(YEARS_EXPERIENCE, 		candidate.getYearsExperience());
		
		assertEquals(DAYS_ON_SITE_VAL, 		candidate.getDaysOnSite());
		assertEquals(RATE_CONTRACT, 		candidate.getRateContract().get());
		assertEquals(RATE_PERM, 			candidate.getRatePerm().get());
		assertEquals(AVAILABLE_FROM_DATE, 	candidate.getAvailableFromDate());
		assertEquals(COMMENTS, 				candidate.getComments());
		assertEquals(INTRODUCTION, 			candidate.getIntroduction());
		assertEquals(REQUIRES_SPONSORSHIP, 	candidate.getRequiresSponsorship());
		assertEquals(SECURITY_CLEARANCE, 	candidate.getSecurityClearance());
		
		candidate.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGE_VAL.getLanguage()).findAny().orElseThrow();
		candidate.getSkills().stream().filter(s -> s.equals(SKILL)).findAny().orElseThrow();
		
	}
	
	/**
	* Tests API Inbound to Domain Converstion 
	*/
	@Test
	public void testConvertToDomain() throws Exception{
		
		CandidateUpdateRequestAPIInbound candidateAPIInboumd = CandidateUpdateRequestAPIInbound
				.builder()
					.function(FUNCTION_VAL)
					.country(COUNTRY_VAL)
					.city(CITY)
					.email(EMAIL)
					.roleSought(ROLE_SOUGHT)
					.freelance(FREELANCE_VAL)
					.perm(PERM_VAL)
					.yearsExperience(YEARS_EXPERIENCE)
					.languages(LANGUAGES)
					.comments(COMMENTS)
					.introduction(INTRODUCTION)
					.daysOnSite(DAYS_ON_SITE_VAL)
					.rateContract(RATE_CONTRACT)
					.ratePerm(RATE_PERM)
					.availableFromDate(AVAILABLE_FROM_DATE)
					.requiresSponsorship(REQUIRES_SPONSORSHIP)
					.securityClearance(SECURITY_CLEARANCE)
					.build();
		
		CandidateUpdateRequest domain = CandidateUpdateRequestAPIInbound.convertToDomain(CANDIDATE_ID, candidateAPIInboumd, Optional.empty());

		assertEquals(CANDIDATE_ID,						domain.getCandidateId());
		assertEquals(FUNCTION_VAL, 						domain.getFunction());
		assertEquals(COUNTRY_VAL, 						domain.getCountry());
		assertEquals(CITY, 								domain.getCity());
		assertEquals(EMAIL, 							domain.getEmail());
		assertEquals(ROLE_SOUGHT, 						domain.getRoleSought());
		assertEquals(FREELANCE_VAL, 					domain.isFreelance());
		assertEquals(PERM_VAL, 							domain.isPerm());
		assertEquals(YEARS_EXPERIENCE, 					domain.getYearsExperience());
		assertEquals(DAYS_ON_SITE_VAL, 					domain.getDaysOnSite());
		assertEquals(REQUIRES_SPONSORSHIP, 				domain.getRequiresSponsorship());
		assertEquals(SECURITY_CLEARANCE, 				domain.getSecurityClearance());
		
		assertEquals(RATE_CONTRACT.getCurrency(), 		domain.getRateContract().get().getCurrency());
		assertEquals(RATE_PERM.getCurrency(), 			domain.getRatePerm().get().getCurrency());
		
		assertEquals(RATE_CONTRACT.getPeriod(), 		domain.getRateContract().get().getPeriod());
		assertEquals(RATE_PERM.getPeriod(), 			domain.getRatePerm().get().getPeriod());
		
		assertEquals(RATE_CONTRACT.getValueMin(), 		domain.getRateContract().get().getValueMin());
		assertEquals(RATE_PERM.getValueMin(), 			domain.getRatePerm().get().getValueMin());
		
		assertEquals(RATE_CONTRACT.getValueMax(), 		domain.getRateContract().get().getValueMax());
		assertEquals(RATE_PERM.getValueMax(), 			domain.getRatePerm().get().getValueMax());
		
		assertEquals(AVAILABLE_FROM_DATE, 	domain.getAvailableFromDate());
		assertEquals(COMMENTS, 				domain.getComments());
		assertEquals(INTRODUCTION, 			domain.getIntroduction());
		
		domain.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGE_VAL.getLanguage()).findAny().orElseThrow();
		
	}
	
}