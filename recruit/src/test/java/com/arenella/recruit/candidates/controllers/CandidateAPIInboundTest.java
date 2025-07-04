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

	private static final String 					candidateId 			= "Candidate1";
	private static final String 					firstname				= "Kevin";
	private static final String 					surname					= "Parkings";
	private static final String 					email					= "admin@arenella-ict.com";
	private static final String						roleSought				= "Junior C# developer";
	private static final FUNCTION					function				= FUNCTION.JAVA_DEV;
	private static final COUNTRY 					country 				= COUNTRY.NETHERLANDS;
	private static final String 					city 					= "Den Haag";
	private static final boolean 					available 				= true;
	private static final FREELANCE 					freelance 				= FREELANCE.TRUE;
	private static final PERM	 					perm 					= PERM.TRUE;
	private static final int 						yearsExperience 		= 21;
	private static final Set<String>				skills					= new LinkedHashSet<>();
	private static final Set<Language>				languages				= new LinkedHashSet<>();
	private static final String						skill					= "Java";
	private static final Language					language				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
	private static final SECURITY_CLEARANCE_TYPE 	SECURITY_CLEARANCE		= SECURITY_CLEARANCE_TYPE.DV;
	private static final boolean					REQUIRES_SPONSORSHIP	= false;;
	private static final Set<Industry> 				INDUSTRIES				= Set.of(Industry.AGRICULTURE, Industry.CYBER_SECURITY);
	
	/**
	* Sets up test environment 
	*/
	public CandidateAPIInboundTest(){
		
		languages.add(language);
		skills.add(skill);
		
	}
	
	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	void testInitializationFromBuilder() {
		
		CandidateAPIInbound candidate = CandidateAPIInbound
						.builder()
							.candidateId(candidateId)
							.firstname(firstname)
							.surname(surname)
							.email(email)
							.roleSought(roleSought)
							.functions(Set.of(function))
							.country(country)
							.city(city)
							.available(available)
							.freelance(freelance)
							.perm(perm)
							.yearsExperience(yearsExperience)
							.skills(skills)
							.languages(languages)
							.requiresSponsorship(REQUIRES_SPONSORSHIP)
							.securityClearance(SECURITY_CLEARANCE)
							.industries(INDUSTRIES)
							.build();
		
		assertEquals(candidateId, 			candidate.getCandidateId());
		assertEquals(firstname, 			candidate.getFirstname());
		assertEquals(surname, 				candidate.getSurname());
		assertEquals(email, 				candidate.getEmail());
		assertEquals(function, 				candidate.getFunctions().toArray()[0]);
		assertEquals(roleSought, 			candidate.getRoleSought());
		assertEquals(country, 				candidate.getCountry());
		assertEquals(city, 					candidate.getCity());
		assertEquals(available, 			candidate.isAvailable());
		assertEquals(freelance, 			candidate.isFreelance());
		assertEquals(perm, 					candidate.isPerm());
		assertEquals(yearsExperience, 		candidate.getYearsExperience());
		assertEquals(REQUIRES_SPONSORSHIP, 	candidate.getRequiresSponsorship());
		assertEquals(SECURITY_CLEARANCE, 	candidate.getSecurityClearance());
		
		assertTrue(candidate.getSkills().contains(skill));
		candidate.getLanguages().stream().filter(l -> l.getLanguage() == language.getLanguage()).findAny().orElseThrow();
		
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
					.candidateId(candidateId)
					.firstname(firstname)
					.surname(surname)
					.email(email)
					.roleSought(roleSought)
					.functions(Set.of(function))
					.country(country)
					.city(city)
					.available(available)
					.freelance(freelance)
					.perm(perm)
					.yearsExperience(yearsExperience)
					.skills(skills)
					.languages(languages)
					.requiresSponsorship(REQUIRES_SPONSORSHIP)
					.securityClearance(SECURITY_CLEARANCE)
					.industries(INDUSTRIES)
					.build();
		
		Candidate candidate = CandidateAPIInbound.convertToCandidate(candidateEntity, Optional.empty());

		assertEquals(candidateId, 			candidate.getCandidateId());
		assertEquals(firstname, 			candidate.getFirstname());
		assertEquals(surname, 				candidate.getSurname());
		assertEquals(email, 				candidate.getEmail());
		assertEquals(roleSought, 			candidate.getRoleSought());
		assertEquals(function, 				candidate.getFunctions().toArray()[0]);
		assertEquals(country, 				candidate.getCountry());
		assertEquals(city, 					candidate.getCity());
		assertEquals(available, 			candidate.isAvailable());
		assertEquals(freelance, 			candidate.isFreelance());
		assertEquals(perm, 					candidate.isPerm());
		assertEquals(yearsExperience, 		candidate.getYearsExperience());
		assertEquals(REQUIRES_SPONSORSHIP, 	candidate.getRequiresSponsorship());
		assertEquals(SECURITY_CLEARANCE, 	candidate.getSecurityClearance());
		
		assertTrue(candidate.getSkills().contains(skill));
		assertEquals(candidate.getLanguages().stream().findFirst().get().getLanguage(), language.getLanguage());
		
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