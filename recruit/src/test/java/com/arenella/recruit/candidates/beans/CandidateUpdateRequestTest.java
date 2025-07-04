package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate.DAYS_ON_SITE;
import com.arenella.recruit.candidates.beans.Candidate.Industry;
import com.arenella.recruit.candidates.beans.Candidate.Rate;
import com.arenella.recruit.candidates.beans.Candidate.SECURITY_CLEARANCE_TYPE;
import com.arenella.recruit.candidates.beans.Candidate.Rate.CURRENCY;
import com.arenella.recruit.candidates.beans.Candidate.Rate.PERIOD;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Unit tests for the CandidateUpdateRequest class
* @author K Parkings
*/
public class CandidateUpdateRequestTest {

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
	private static final byte[]						PHOTO_BYTES				= new byte[] {};
	private static final String 					COMMENTS				= "aComment";
	private static final String 					INTRODUCTION 			= "anIntro";
	private static final DAYS_ON_SITE				DAYS_ON_SITE_VAL		= DAYS_ON_SITE.ZERO;
	private static final Rate						RATE_CONTRACT 			= new Rate(CURRENCY.EUR, PERIOD.DAY, 80.50f, 90f);
	private static final Rate						RATE_PERM 				= new Rate(CURRENCY.EUR, PERIOD.YEAR, 25000f, 30000f);
	private static final LocalDate 					AVAILABLE_FROM_DATE 	= LocalDate.of(2023, 7, 21);
	private static final SECURITY_CLEARANCE_TYPE 	SECURITY_CLEARANCE		= SECURITY_CLEARANCE_TYPE.NONE;
	private static final boolean					REQUIRES_SPONSORSHIP	= true;
	private static final Set<Industry> 				INDUSTRIES					= Set.of(Industry.AGRICULTURE, Industry.CYBER_SECURITY);
	
	/**
	* Sets up test environment 
	*/
	public CandidateUpdateRequestTest(){
		
		LANGUAGES.add(LANGUAGE_VAL);
		SKILLS.add(SKILL);
		
	}
	
	/**
	* Tests construction via builder with no values provided
	* @throws Exception
	*/
	@Test
	void testBuilder_defaults() throws Exception {
		
		CandidateUpdateRequest candidate = CandidateUpdateRequest.builder().build();
		
		assertTrue(candidate.getSkills().isEmpty());
		assertTrue(candidate.getLanguages().isEmpty());
		assertEquals(0, 								candidate.getYearsExperience());
		assertNull(candidate.isFreelance());
		assertNull(candidate.isPerm());
		assertNull(candidate.getCountry());
		
		assertTrue(candidate.getRateContract().isEmpty());
		assertTrue(candidate.getRatePerm().isEmpty());
		assertTrue(candidate.getFunctions().isEmpty());
		assertEquals(candidate.getAvailableFromDate(), LocalDate.now()); //[KP] Small chance of failure if test run in exactly midnight
		
	}
	
	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	void testInitializationFromBuilder() {
		
		CandidateUpdateRequest candidate = CandidateUpdateRequest
						.builder()
							.candidateId(CANDIDATE_ID)
							.functions(Set.of(FUNCTION_VAL))
							.country(COUNTRY_VAL)
							.city(CITY)
							.email(EMAIL)
							.roleSought(ROLE_SOUGHT)
							.freelance(FREELANCE_VAL)
							.perm(PERM_VAL)
							.yearsExperience(YEARS_EXPERIENCE)
							.languages(LANGUAGES)
							.photoBytes(PHOTO_BYTES)
							.skills(SKILLS)
							.comments(COMMENTS)
							.introduction(INTRODUCTION)
							.daysOnSite(DAYS_ON_SITE_VAL)
							.rateContract(RATE_CONTRACT)
							.ratePerm(RATE_PERM)
							.availableFromDate(AVAILABLE_FROM_DATE)
							.requiresSponsorship(REQUIRES_SPONSORSHIP)
							.securityClearance(SECURITY_CLEARANCE)
							.industries(INDUSTRIES)
							.build();
		
		assertEquals(CANDIDATE_ID, 			candidate.getCandidateId());
		assertEquals(FUNCTION_VAL, 			candidate.getFunctions().toArray()[0]);
		assertEquals(COUNTRY_VAL, 			candidate.getCountry());
		assertEquals(CITY, 					candidate.getCity());
		assertEquals(EMAIL, 				candidate.getEmail());
		assertEquals(ROLE_SOUGHT, 			candidate.getRoleSought());
		assertEquals(FREELANCE_VAL, 		candidate.isFreelance());
		assertEquals(PERM_VAL, 				candidate.isPerm());
		assertEquals(YEARS_EXPERIENCE, 		candidate.getYearsExperience());
		assertEquals(PHOTO_BYTES, 			candidate.getPhotoBytes().get());
		
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
		
		assertTrue(candidate.getIndustries().contains(Industry.AGRICULTURE));
		assertTrue(candidate.getIndustries().contains(Industry.CYBER_SECURITY));
		assertEquals(2, candidate.getIndustries().size());
		
	}
	
}