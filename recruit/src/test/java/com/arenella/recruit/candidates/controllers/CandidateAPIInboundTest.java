package com.arenella.recruit.candidates.controllers;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashSet;
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
* Unit tests for the CandidateAPOutnboundTest
* @author K Parkings
*/
public class CandidateAPIInboundTest {

	private static final String 		candidateId 			= "Candidate1";
	private static final String 		firstname				= "Kevin";
	private static final String 		surname					= "Parkings";
	private static final String 		email					= "kparkings@gmail.com";
	private static final String			roleSought				= "Junior C# developer";
	private static final FUNCTION		function				= FUNCTION.JAVA_DEV;
	private static final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
	private static final String 		city 					= "Den Haag";
	private static final boolean 		available 				= true;
	private static final FREELANCE 		freelance 				= FREELANCE.TRUE;
	private static final PERM	 		perm 					= PERM.TRUE;
	//private static final LocalDate 		lastAvailabilityCheck 	= LocalDate.of(1980, 12, 3);
	//private static final LocalDate 		registerd 				= LocalDate.of(2021, 02, 20);
	private static final int 			yearsExperience 		= 21;
	private static final Set<String>	skills					= new LinkedHashSet<>();
	private static final Set<Language>	languages				= new LinkedHashSet<>();
	private static final String			skill					= "Java";
	private static final Language		language				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.PROFICIENT).build();
	
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
	public void testInitializationFromBuilder() {
		
		CandidateAPIInbound candidate = CandidateAPIInbound
						.builder()
							.candidateId(candidateId)
							.firstname(firstname)
							.surname(surname)
							.email(email)
							.roleSought(roleSought)
							.function(function)
							.country(country)
							.city(city)
							.available(available)
							.freelance(freelance)
							.perm(perm)
							.yearsExperience(yearsExperience)
							.skills(skills)
							.languages(languages)
							.build();
		
		assertEquals(candidate.getCandidateId(), 				candidateId);
		assertEquals(candidate.getFirstname(), 					firstname);
		assertEquals(candidate.getSurname(), 					surname);
		assertEquals(candidate.getEmail(), 						email);
		assertEquals(candidate.getFunction(), 					function);
		assertEquals(candidate.getRoleSought(),					roleSought);
		assertEquals(candidate.getCountry(), 					country);
		assertEquals(candidate.getCity(), 						city);
		assertEquals(candidate.isAvailable(), 					available);
		assertEquals(candidate.isFreelance(), 					freelance);
		assertEquals(candidate.isPerm(), 						perm);
		assertEquals(candidate.getYearsExperience(), 			yearsExperience);
		
		assertTrue(candidate.getSkills().contains(skill));
		candidate.getLanguages().stream().filter(l -> l.getLanguage() == language.getLanguage()).findAny().orElseThrow();
		
	}
	
	/**
	* Tests that conversion returns a Domain representation of the 
	* Entity representation of a Candidate and the state is copied 
	* successfully 
	*/
	@Test
	public void testConversionToCandidate() {
		
		CandidateAPIInbound candidateEntity = CandidateAPIInbound
				.builder()
					.candidateId(candidateId)
					.firstname(firstname)
					.surname(surname)
					.email(email)
					.roleSought(roleSought)
					.function(function)
					.country(country)
					.city(city)
					.available(available)
					.freelance(freelance)
					.perm(perm)
					.yearsExperience(yearsExperience)
					.skills(skills)
					.languages(languages)
					.build();
		
		Candidate candidate = CandidateAPIInbound.convertToCandidate(candidateEntity);

		assertEquals(candidate.getCandidateId(), 				candidateId);
		assertEquals(candidate.getFirstname(), 					firstname);
		assertEquals(candidate.getSurname(), 					surname);
		assertEquals(candidate.getEmail(), 						email);
		assertEquals(candidate.getRoleSought(),					roleSought);
		assertEquals(candidate.getFunction(), 					function);
		assertEquals(candidate.getCountry(), 					country);
		assertEquals(candidate.getCity(), 						city);
		assertEquals(candidate.isAvailable(), 					available);
		assertEquals(candidate.isFreelance(), 					freelance);
		assertEquals(candidate.isPerm(), 						perm);
		//assertEquals(candidate.getLastAvailabilityCheckOn(), 	lastAvailabilityCheck);
		//assertEquals(candidate.getRegisteredOn(), 				registerd);
		assertEquals(candidate.getYearsExperience(), 			yearsExperience);
		
		assertTrue(candidate.getSkills().contains(skill));
		assertEquals(candidate.getLanguages().stream().findFirst().get().getLanguage(), language.getLanguage());
	
	}
	
	/**
	* Tests that by default the Candidate is available
	* @throws Exception
	*/
	@Test
	public void testAvailableByDefault() throws Exception {
		
		CandidateAPIInbound candidate = CandidateAPIInbound.builder().build();
		
		assertTrue(candidate.isAvailable());
		
	}
	
}