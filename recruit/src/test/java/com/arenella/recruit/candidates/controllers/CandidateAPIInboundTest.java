package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Candidate.Industry;
import com.arenella.recruit.candidates.beans.Candidate.SECURITY_CLEARANCE_TYPE;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Unit tests for the CandidateAPOutnboundTest
* @author K Parkings
*/
class CandidateAPIInboundTest {

	private static final String 					CANDIDATE_ID 			= "Candidate1";
	private static final String 					FIRST_NAME				= "Kevin";
	private static final String 					SURNAME					= "Parkings";
	private static final String 					EMAIL					= "admin@arenella-ict.com";
	private static final String						ROLE_SOUGHT				= "Junior C# developer";
	private static final FUNCTION					FUNCTION_VAL			= FUNCTION.JAVA_DEV;
	private static final COUNTRY 					COUNTRY_VAL 			= COUNTRY.NETHERLANDS;
	private static final String 					CITY 					= "Den Haag";
	private static final boolean 					AVAILABLE 				= true;
	private static final FREELANCE 					FREELANCE_VAL			= FREELANCE.TRUE;
	private static final PERM	 					PERM_VAL				= PERM.TRUE;
	private static final int 						YEARS_EXPERIENCE 		= 21;
	private static final Set<String>				SKILLS					= new LinkedHashSet<>();
	private static final Set<Language>				languages				= new LinkedHashSet<>();
	private static final String						SKILL					= "Java";
	private static final Language					LANGUAGE_VAL			= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
	private static final SECURITY_CLEARANCE_TYPE 	SECURITY_CLEARANCE		= SECURITY_CLEARANCE_TYPE.DV;
	private static final boolean					REQUIRES_SPONSORSHIP	= false;
	private static final Set<Industry> 				INDUSTRIES				= Set.of(Industry.AGRICULTURE, Industry.CYBER_SECURITY);
	
	/**
	* Sets up test environment 
	*/
	public CandidateAPIInboundTest(){
		
		languages.add(LANGUAGE_VAL);
		SKILLS.add(SKILL);
		
	}
	
	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	void testInitializationFromBuilder() {
		
		CandidateAPIInbound candidate = CandidateAPIInbound
						.builder()
							.candidateId(CANDIDATE_ID)
							.firstname(FIRST_NAME)
							.surname(SURNAME)
							.email(EMAIL)
							.roleSought(ROLE_SOUGHT)
							.functions(Set.of(FUNCTION_VAL))
							.country(COUNTRY_VAL)
							.city(CITY)
							.available(AVAILABLE)
							.freelance(FREELANCE_VAL)
							.perm(PERM_VAL)
							.yearsExperience(YEARS_EXPERIENCE)
							.skills(SKILLS)
							.languages(languages)
							.requiresSponsorship(REQUIRES_SPONSORSHIP)
							.securityClearance(SECURITY_CLEARANCE)
							.industries(INDUSTRIES)
							.build();
		
		assertEquals(CANDIDATE_ID, 			candidate.getCandidateId());
		assertEquals(FIRST_NAME, 			candidate.getFirstname());
		assertEquals(SURNAME, 				candidate.getSurname());
		assertEquals(EMAIL, 				candidate.getEmail());
		assertEquals(FUNCTION_VAL, 				candidate.getFunctions().toArray()[0]);
		assertEquals(ROLE_SOUGHT, 			candidate.getRoleSought());
		assertEquals(COUNTRY_VAL, 				candidate.getCountry());
		assertEquals(CITY, 					candidate.getCity());
		assertEquals(AVAILABLE, 			candidate.isAvailable());
		assertEquals(FREELANCE_VAL, 			candidate.isFreelance());
		assertEquals(PERM_VAL, 					candidate.isPerm());
		assertEquals(YEARS_EXPERIENCE, 		candidate.getYearsExperience());
		assertEquals(REQUIRES_SPONSORSHIP, 	candidate.getRequiresSponsorship());
		assertEquals(SECURITY_CLEARANCE, 	candidate.getSecurityClearance());
		
		assertTrue(candidate.getSkills().contains(SKILL));
		candidate.getLanguages().stream().filter(l -> l.getLanguage() == LANGUAGE_VAL.getLanguage()).findAny().orElseThrow();
		
		assertTrue(candidate.getIndustries().contains(Industry.AGRICULTURE));
		assertTrue(candidate.getIndustries().contains(Industry.CYBER_SECURITY));
		assertEquals(2, candidate.getIndustries().size());
	}
	
	/**
	* Tests that conversion returns a Domain representation of the 
	* Entity representation of a Candidate and the state is copied 
	* successfully 
	*/
	@Test
	void testConversionToCandidate() {
		
		CandidateAPIInbound candidateEntity = CandidateAPIInbound
				.builder()
					.candidateId(CANDIDATE_ID)
					.firstname(FIRST_NAME)
					.surname(SURNAME)
					.email(EMAIL)
					.roleSought(ROLE_SOUGHT)
					.functions(Set.of(FUNCTION_VAL))
					.country(COUNTRY_VAL)
					.city(CITY)
					.available(AVAILABLE)
					.freelance(FREELANCE_VAL)
					.perm(PERM_VAL)
					.yearsExperience(YEARS_EXPERIENCE)
					.skills(SKILLS)
					.languages(languages)
					.requiresSponsorship(REQUIRES_SPONSORSHIP)
					.securityClearance(SECURITY_CLEARANCE)
					.industries(INDUSTRIES)
					.build();
		
		Candidate candidate = CandidateAPIInbound.convertToCandidate(candidateEntity, Optional.empty());

		assertEquals(CANDIDATE_ID, 			candidate.getCandidateId());
		assertEquals(FIRST_NAME, 			candidate.getFirstname());
		assertEquals(SURNAME, 				candidate.getSurname());
		assertEquals(EMAIL, 				candidate.getEmail());
		assertEquals(ROLE_SOUGHT, 			candidate.getRoleSought());
		assertEquals(FUNCTION_VAL, 				candidate.getFunctions().toArray()[0]);
		assertEquals(COUNTRY_VAL, 				candidate.getCountry());
		assertEquals(CITY, 					candidate.getCity());
		assertEquals(AVAILABLE, 			candidate.isAvailable());
		assertEquals(FREELANCE_VAL, 			candidate.isFreelance());
		assertEquals(PERM_VAL, 					candidate.isPerm());
		assertEquals(YEARS_EXPERIENCE, 		candidate.getYearsExperience());
		assertEquals(REQUIRES_SPONSORSHIP, 	candidate.getRequiresSponsorship());
		assertEquals(SECURITY_CLEARANCE, 	candidate.getSecurityClearance());
		
		assertTrue(candidate.getSkills().contains(SKILL));
		assertEquals(candidate.getLanguages().stream().findFirst().get().getLanguage(), LANGUAGE_VAL.getLanguage());
		
		assertTrue(candidate.getIndustries().contains(Industry.AGRICULTURE));
		assertTrue(candidate.getIndustries().contains(Industry.CYBER_SECURITY));
		assertEquals(2, candidate.getIndustries().size());
	
	}
	
	/**
	* Tests that by default the Candidate is available
	* @throws Exception
	*/
	@Test
	void testAvailableByDefault() {
		
		CandidateAPIInbound candidate = CandidateAPIInbound.builder().build();
		
		assertTrue(candidate.isAvailable());
		
	}
	
}