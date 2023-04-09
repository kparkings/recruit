package com.arenella.recruit.candidates.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate.Rate;
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

	private static final String 		candidateId 			= "Candidate1";
	private static final FUNCTION		function				= FUNCTION.JAVA_DEV;
	private static final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
	private static final String 		city 					= "Den Haag";
	private static final String 		email					= "kparkings@gmail.com";
	private static final String 		roleSought				= "Senior java Dev";
	private static final FREELANCE 		freelance 				= FREELANCE.TRUE;
	private static final PERM 			perm 					= PERM.TRUE;
	private static final int 			yearsExperience 		= 21;
	private static final Set<String>	skills					= new LinkedHashSet<>();
	private static final Set<Language>	languages				= new LinkedHashSet<>();
	private static final String			skill					= "Java";
	private static final Language		language				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
	private static final Rate			rate					= new Rate(CURRENCY.EUR, PERIOD.DAY, 1);
	private static final String			introduction			= "into";
	private static final byte[]			photoBytes				= new byte[] {};
	
	/**
	* Sets up test environment 
	*/
	public CandidateUpdateRequestTest(){
		
		languages.add(language);
		skills.add(skill);
		
	}
	
	/**
	* Test Builder values used to initialize instance of the Candidate Class 
	*/
	@Test
	public void testInitializationFromBuilder() {
		
		CandidateUpdateRequest candidate = CandidateUpdateRequest
						.builder()
							.candidateId(candidateId)
							.function(function)
							.country(country)
							.city(city)
							.email(email)
							.roleSought(roleSought)
							.freelance(freelance)
							.perm(perm)
							.yearsExperience(yearsExperience)
							.languages(languages)
							.rate(rate)
							.photoBytes(photoBytes)
							.introduction(introduction)
							.build();
		
		assertEquals(candidateId, 		candidate.getCandidateId());
		assertEquals(function, 			candidate.getFunction());
		assertEquals(country, 			candidate.getCountry());
		assertEquals(city, 				candidate.getCity());
		assertEquals(email, 			candidate.getEmail());
		assertEquals(roleSought, 		candidate.getRoleSought());
		assertEquals(freelance, 		candidate.isFreelance());
		assertEquals(perm, 				candidate.isPerm());
		assertEquals(yearsExperience, 	candidate.getYearsExperience());
		assertEquals(rate, 				candidate.getRate().get());
		assertEquals(photoBytes, 		candidate.getPhotoBytes().get());
		assertEquals(introduction, 		candidate.getIntroduction());
		candidate.getLanguages().stream().filter(l -> l.getLanguage() == language.getLanguage()).findAny().orElseThrow();
		
	}
	
}