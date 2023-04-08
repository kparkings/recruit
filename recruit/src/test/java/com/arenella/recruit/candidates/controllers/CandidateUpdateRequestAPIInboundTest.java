package com.arenella.recruit.candidates.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.CandidateUpdateRequest;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Unit tests for the CandidateUpdateRequestAPIInbound class
* @author K Parkings
*/
public class CandidateUpdateRequestAPIInboundTest {

	private static final String 		candidateId 			= "Candidate1";
	private static final FUNCTION		function				= FUNCTION.JAVA_DEV;
	private static final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
	private static final String 		city 					= "Den Haag";
	private static final String 		email					= "kparkings@gmail.com";
	private static final String 		roleSought				= "Senior java Dev";
	private static final boolean 		available 				= true;
	private static final FREELANCE 		freelance 				= FREELANCE.TRUE;
	private static final PERM 			perm 					= PERM.TRUE;
	private static final int 			yearsExperience 		= 21;
	private static final Set<String>	skills					= new LinkedHashSet<>();
	private static final Set<Language>	languages				= new LinkedHashSet<>();
	private static final String			skill					= "Java";
	private static final Language		language				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
	
	/**
	* Sets up test environment 
	*/
	public CandidateUpdateRequestAPIInboundTest(){
		
		languages.add(language);
		skills.add(skill);
		
	}
	
	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	public void testInitializationFromBuilder() {
		
		CandidateUpdateRequestAPIInbound candidate = CandidateUpdateRequestAPIInbound
						.builder()
							.function(function)
							.country(country)
							.city(city)
							.email(email)
							.roleSought(roleSought)
							.available(available)
							.freelance(freelance)
							.perm(perm)
							.yearsExperience(yearsExperience)
							.languages(languages)
							.build();
		
		assertEquals(function, 			candidate.getFunction());
		assertEquals(country, 			candidate.getCountry());
		assertEquals(city, 				candidate.getCity());
		assertEquals(email, 			candidate.getEmail());
		assertEquals(roleSought, 		candidate.getRoleSought());
		assertEquals(available, 		candidate.isAvailable());
		assertEquals(freelance, 		candidate.isFreelance());
		assertEquals(perm, 				candidate.isPerm());
		assertEquals(yearsExperience, 	candidate.getYearsExperience());
		
		candidate.getLanguages().stream().filter(l -> l.getLanguage() == language.getLanguage()).findAny().orElseThrow();
		
	}
	
	/**
	* Tests API Inbound to Domain Converstion 
	*/
	@Test
	public void testConvertToDomain() throws Exception{
		
		CandidateUpdateRequestAPIInbound candidateAPIInboumd = CandidateUpdateRequestAPIInbound
				.builder()
					.function(function)
					.country(country)
					.city(city)
					.email(email)
					.roleSought(roleSought)
					.available(available)
					.freelance(freelance)
					.perm(perm)
					.yearsExperience(yearsExperience)
					.languages(languages)
					.build();
		
		CandidateUpdateRequest domain = CandidateUpdateRequestAPIInbound.convertToDomain(candidateId, candidateAPIInboumd);

		assertEquals(candidateId,		domain.getCandidateId());
		assertEquals(function, 			domain.getFunction());
		assertEquals(country, 			domain.getCountry());
		assertEquals(city, 				domain.getCity());
		assertEquals(email, 			domain.getEmail());
		assertEquals(roleSought, 		domain.getRoleSought());
		assertEquals(available, 		domain.isAvailable());
		assertEquals(freelance, 		domain.isFreelance());
		assertEquals(perm, 				domain.isPerm());
		assertEquals(yearsExperience, 	domain.getYearsExperience());
		
		domain.getLanguages().stream().filter(l -> l.getLanguage() == language.getLanguage()).findAny().orElseThrow();
		
	}
	
}