package com.arenella.recruit.candidates.entities;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.arenella.recruit.candidates.beans.Candidate;
import com.arenella.recruit.candidates.beans.Language;
import com.arenella.recruit.candidates.beans.Language.LANGUAGE;
import com.arenella.recruit.candidates.beans.Language.LEVEL;
import com.arenella.recruit.candidates.enums.COUNTRY;
import com.arenella.recruit.candidates.enums.FREELANCE;
import com.arenella.recruit.candidates.enums.FUNCTION;
import com.arenella.recruit.candidates.enums.PERM;

/**
* Unit tests for the CandidateEntity class
* @author K Parkings
*/
public class CandidateEntityTest {

	private static final String	 		candidateId 			= "100";
	private static final String 		firstname				= "Kevin";
	private static final String 		surname					= "Parkings";
	private static final String 		email					= "kparkings@gmail.com";
	private static final String			roleSought				= "Python Developer";
	private static final FUNCTION		function				= FUNCTION.JAVA_DEV;
	private static final COUNTRY 		country 				= COUNTRY.NETHERLANDS;
	private static final String 		city 					= "Den Haag";
	private static final boolean 		available 				= true;
	private static final boolean		flaggedAsUnavailable	= true;
	private static final FREELANCE 		freelance 				= FREELANCE.TRUE;
	private static final PERM 			perm 					= PERM.TRUE;
	private static final LocalDate 		lastAvailabilityCheck 	= LocalDate.of(1980, 12, 3);
	private static final LocalDate 		registerd 				= LocalDate.of(2021, 02, 20);
	private static final int 			yearsExperience 		= 21;
	private static final Set<String>	skills					= new LinkedHashSet<>();
	private static final Set<Language>	languages				= new LinkedHashSet<>();
	private static final String			skill					= "Java";
	private static final Language		language				= Language.builder().language(LANGUAGE.DUTCH).level(LEVEL.BASIC).build();
	
	/**
	* Sets up test environment 
	*/
	public CandidateEntityTest(){
		
		languages.add(language);
		skills.add(skill);
		
	}
	
	/**
	* Tests that conversion returns an Entity representation of the 
	* Domain representation of a Candidate and the state is copied 
	* successfully 
	*/
	@Test
	public void testConversionToEntity() {
		
		Candidate candidate = Candidate
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
					.flaggedAsUnavailable(flaggedAsUnavailable)
					.freelance(freelance)
					.perm(perm)
					.lastAvailabilityCheck(lastAvailabilityCheck)
					.registerd(registerd)
					.yearsExperience(yearsExperience)
					.build();
		
		CandidateEntity candidateEntity = CandidateEntity.convertToEntity(candidate);

		assertEquals(candidateEntity.getCandidateId(), 				Long.valueOf(candidateId));
		assertEquals(candidateEntity.getFirstname(), 				firstname);
		assertEquals(candidateEntity.getSurname(), 					surname);
		assertEquals(candidateEntity.getEmail(), 					email);
		assertEquals(candidateEntity.getRoleSought(),				roleSought);
		assertEquals(candidateEntity.getFunction(),					function);
		assertEquals(candidateEntity.getCountry(), 					country);
		assertEquals(candidateEntity.getCity(), 					city);
		assertEquals(candidateEntity.isAvailable(), 				available);
		assertEquals(candidateEntity.isFlaggedAsUnavailable(), 		flaggedAsUnavailable);
		assertEquals(candidateEntity.isFreelance(), 				freelance);
		assertEquals(candidateEntity.isPerm(), 						perm);
		assertEquals(candidateEntity.getLastAvailabilityCheckOn(), 	lastAvailabilityCheck);
		assertEquals(candidateEntity.getRegisteredOn(), 			registerd);
		assertEquals(candidateEntity.getYearsExperience(), 			yearsExperience);

	}
	
	/**
	* Tests that conversion returns a Domain representation of the 
	* Entity representation of a Candidate and the state is copied 
	* successfully 
	*/
	@Test
	public void testConversionFromEntity() {
		
		CandidateEntity candidateEntity = CandidateEntity
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
					.flaggedAsUnavailable(flaggedAsUnavailable)
					.freelance(freelance)
					.perm(perm)
					.lastAvailabilityCheck(lastAvailabilityCheck)
					.registerd(registerd)
					.yearsExperience(yearsExperience)
					.skills(skills)
					.languages(languages)
					.build();
		
		Candidate candidate = CandidateEntity.convertFromEntity(candidateEntity);

		assertEquals(candidate.getCandidateId(), 				candidateId);
		assertEquals(candidate.getFirstname(), 					firstname);
		assertEquals(candidate.getSurname(), 					surname);
		assertEquals(candidate.getEmail(), 						email);
		assertEquals(candidate.getRoleSought(),					roleSought);
		assertEquals(candidate.getFunction(), 					function);
		assertEquals(candidate.getCountry(), 					country);
		assertEquals(candidate.getCity(), 						city);
		assertEquals(candidate.isAvailable(), 					available);
		assertEquals(candidate.isFlaggedAsUnavailable(), 		flaggedAsUnavailable);
		assertEquals(candidate.isFreelance(), 					freelance);
		assertEquals(candidate.isPerm(), 						perm);
		assertEquals(candidate.getLastAvailabilityCheckOn(), 	lastAvailabilityCheck);
		assertEquals(candidate.getRegisteredOn(), 				registerd);
		assertEquals(candidate.getYearsExperience(), 			yearsExperience);
		
		assertTrue(candidate.getSkills().contains(skill));
		assertEquals(candidate.getLanguages().stream().findFirst().get().getLanguage(), language.getLanguage());
	
	}
	
	/**
	* Tests updating of attributes on existing entity
	* @throws Exception
	*/
	@Test
	public void testUpdateExistingCandidate() throws Exception {
		
		CandidateEntity candidate = CandidateEntity.builder().build();
		
		assertFalse(candidate.isAvailable());
		
		candidate.setAvailable(true);
		
		assertTrue(candidate.isAvailable());
		
		candidate.setAvailable(false);
		
		assertFalse(candidate.isAvailable());
		
		assertFalse(candidate.isFlaggedAsUnavailable());
		
		candidate.setFlaggedAsUnavailable(true);
		
		assertTrue(candidate.isFlaggedAsUnavailable());
		
		candidate.setFlaggedAsUnavailable(false);
		
		assertFalse(candidate.isFlaggedAsUnavailable());
	
		
	}
	
}